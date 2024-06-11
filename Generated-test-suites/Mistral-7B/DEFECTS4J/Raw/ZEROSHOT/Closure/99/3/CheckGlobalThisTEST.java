import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private static final DiagnosticType GLOBAL_THIS = DiagnosticType.warning("JSC_USED_GLOBAL_THIS", "dangerous use of the global 'this' object");
  private AbstractCompiler compiler;
  private CheckGlobalThis checker;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testShouldNotTraverseConstructorOrAnnotatedFunctions() {
    Node constructor = Node.newFunction("MyConstructor", new Token(Token.FUNCTION, 0));
    Node annotatedFunction = Node.newFunction("MyAnnotatedFunction", new Token(Token.FUNCTION, 0));
    Node annotation = Node.newIdentifier("@constructor");
    Node annotatedAnnotation = Node.newExpression(new Token(Token.NEW, 0), Node.newList(annotatedFunction, annotation));
    when(constructor.getLastChild()).thenReturn(Node.newList(annotation));

    assertFalse(checker.shouldTraverse(mock(NodeTraversal.class), annotatedFunction, null));
    assertFalse(checker.shouldTraverse(mock(NodeTraversal.class), constructor, null));
  }

  @Test
  public void testShouldNotTraverseNonGlobalContexts() {
    Node nonGlobalContext = Node.newBlock(null);

    assertFalse(checker.shouldTraverse(mock(NodeTraversal.class), Node.newFunction("MyFunction", new Token(Token.FUNCTION, 0)), nonGlobalContext));
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    Node assignParent = Node.newExpression(new Token(Token.ASSIGN, 0), Node.newIdentifier("a"), Node.newIdentifier("this"));

    assertTrue(checker.shouldTraverse(mock(NodeTraversal.class), Node.newIdentifier("this"), assignParent));
  }

  @Test
  public void testShouldTraverseRightSideOfAssignment() {
    Node assignParent = Node.newExpression(new Token(Token.ASSIGN, 0), Node.newIdentifier("a"), Node.newIdentifier("this"));
    Node assignLeftChild = Node.newIdentifier("this");

    assertTrue(checker.shouldTraverse(mock(NodeTraversal.class), assignLeftChild, assignParent));
  }

  @Test
  public void testShouldReportThisInFunction() {
    Node function = Node.newFunction("MyFunction", new Token(Token.FUNCTION, 0));
    Node thisNode = Node.newIdentifier("this");

    checker.visit(mock(NodeTraversal.class), thisNode, function);

    assertTrue(compiler.hasDiagnostic(GLOBAL_THIS));
  }

  @Test
  public void testShouldReportThisInPropertyAccess() {
    Node parent = Node.newIdentifier("parent");
    Node thisNode = Node.newIdentifier("this");
    Node propertyAccess = Node.newGetProp(parent, "property");
    Node thisInPropertyAccess = Node.newGetProp(thisNode, "property");

    checker.visit(mock(NodeTraversal.class), thisInPropertyAccess, propertyAccess);

    assertTrue(compiler.hasDiagnostic(GLOBAL_THIS));
  }

  @Test
  public void testShouldNotReportThisInPrototypePropertyAccess() {
    Node parent = Node.newIdentifier("parent");
    Node prototype = Node.newGetProp(parent, "prototype");
    Node thisNode = Node.newIdentifier("this");
    Node prototypePropertyAccess = Node.newGetProp(prototype, "property");
    Node thisInPrototypePropertyAccess = Node.newGetProp(thisNode, "property");

    checker.visit(mock(NodeTraversal.class), thisInPrototypePropertyAccess, prototypePropertyAccess);

    assertFalse(compiler.hasDiagnostic(GLOBAL_THIS));
  }
}