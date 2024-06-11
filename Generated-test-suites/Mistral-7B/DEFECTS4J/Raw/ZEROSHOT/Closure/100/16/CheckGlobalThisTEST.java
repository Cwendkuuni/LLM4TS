import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class CheckGlobalThisTest {

  private AbstractCompiler compiler;
  private CheckGlobalThis checker;
  private NodeTraversal traversal;
  private Node root;

  @Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    traversal = mock(NodeTraversal.class);
    root = new Node(Token.PROGRAM);
  }

  @Test
  public void testShouldTraverseGlobalContext() {
    Node globalContext = new Node(Token.GLOBAL);
    Node function = new Node(Token.FUNCTION);
    Node body = new Node(Token.BLOCK);
    globalContext.addChild(function);
    function.addChild(body);
    root.addChild(globalContext);

    when(compiler.getTraverser()).thenReturn(traversal);

    assertEquals(true, checker.shouldTraverse(traversal, function, globalContext));
  }

  @Test
  public void testShouldNotTraverseConstructor() {
    Node constructor = new Node(Token.FUNCTION);
    constructor.addAttribute(new JSDocInfo("constructor", new String[] {}));
    Node body = new Node(Token.BLOCK);
    constructor.addChild(body);
    root.addChild(constructor);

    when(compiler.getTraverser()).thenReturn(traversal);

    assertEquals(false, checker.shouldTraverse(traversal, constructor, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisType() {
    Node function = new Node(Token.FUNCTION);
    function.addAttribute(new JSDocInfo("this", new String[] {}));
    Node body = new Node(Token.BLOCK);
    function.addChild(body);
    root.addChild(function);

    when(compiler.getTraverser()).thenReturn(traversal);

    assertEquals(false, checker.shouldTraverse(traversal, function, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithoutAnnotation() {
    Node function = new Node(Token.FUNCTION);
    Node body = new Node(Token.BLOCK);
    function.addChild(body);
    root.addChild(function);

    when(compiler.getTraverser()).thenReturn(traversal);

    assertEquals(true, checker.shouldTraverse(traversal, function, null));
  }

  @Test
  public void testShouldTraverseAssignment() {
    Node assign = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME);
    Node rhs = new Node(Token.THIS);
    assign.addChild(lhs);
    assign.addChild(rhs);
    root.addChild(assign);

    when(compiler.getTraverser()).thenReturn(traversal);

    checker.visit(traversal, rhs, assign);

    ArgumentCaptor<DiagnosticType> diagnosticCaptor = ArgumentCaptor.forClass(DiagnosticType.class);
    verify(compiler).report(eq(traversal), equalTo(checker.LEVEL), diagnosticCaptor.capture());
    assertEquals("JSC_USED_GLOBAL_THIS", diagnosticCaptor.getValue().getTypeName());
  }

  @Test
  public void testShouldNotTraverseAssignmentToPrototypeProperty() {
    Node assign = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.GETPROP, new Node(Token.NAME, "prototype"), new Node(Token.NAME, "a"));
    Node rhs = new Node(Token.THIS);
    assign.addChild(lhs);
    assign.addChild(rhs);
    root.addChild(assign);

    when(compiler.getTraverser()).thenReturn(traversal);

    checker.visit(traversal, rhs, assign);

    ArgumentCaptor<DiagnosticType> diagnosticCaptor = ArgumentCaptor.forClass(DiagnosticType.class);
    verify(compiler, never()).report(eq(traversal), equalTo(checker.LEVEL), diagnosticCaptor.capture());
  }

  @Test
  public void testShouldNotTraverseAssignmentToPrototypeSubproperty() {
    Node assign = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.GETPROP, new Node(Token.NAME, "prototype"), new Node(Token.GETPROP, new Node(Token.NAME, "a"), new Node(Token.NAME, "b")));
    Node rhs = new Node(Token.THIS);
    assign.addChild(lhs);
    assign.addChild(rhs);
    root.addChild(assign);

    when(compiler.getTraverser()).thenReturn(traversal);

    checker.visit(traversal, rhs, assign);

    ArgumentCaptor<DiagnosticType> diagnosticCaptor = ArgumentCaptor.forClass(DiagnosticType.class);
    verify(compiler, never()).report(eq(traversal), equalTo(checker.LEVEL), diagnosticCaptor.capture());
  }
}