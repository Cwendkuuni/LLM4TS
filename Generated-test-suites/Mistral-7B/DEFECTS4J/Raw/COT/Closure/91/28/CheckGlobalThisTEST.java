import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;

public class CheckGlobalThisTest {

  private CheckGlobalThis checker;
  private AbstractCompiler compiler;
  private NodeTraversal traversal;

  @Before
  public void setup() {
    compiler = mock(AbstractCompiler.class);
    traversal = mock(NodeTraversal.class);
    checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverseFunctionAnnotations() {
    Node node = Node.newFunctionNode(Token.FUNCTION, new int[] {Token.NAME}, null, null, null);
    JSDocInfo jsDoc = Node.newJSDocInfo("constructor");
    node.setJSDocInfo(jsDoc);

    assertTrue(checker.shouldTraverse(ArgumentMatchers.eq(traversal), ArgumentMatchers.eq(node), ArgumentMatchers.eq(null)));
  }

  @Test
  public void testShouldNotTraverseFunctionWithoutAnnotations() {
    Node node = Node.newFunctionNode(Token.FUNCTION, new int[] {Token.NAME}, null, null, null);

    assertFalse(checker.shouldTraverse(ArgumentMatchers.eq(traversal), ArgumentMatchers.eq(node), ArgumentMatchers.eq(null)));
  }

  @Test
  public void testShouldTraverseFunctionWithThisAnnotation() {
    Node node = Node.newFunctionNode(Token.FUNCTION, new int[] {Token.NAME}, null, null, null);
    JSDocInfo jsDoc = Node.newJSDocInfo("@this", null);
    node.setJSDocInfo(jsDoc);

    assertTrue(checker.shouldTraverse(ArgumentMatchers.eq(traversal), ArgumentMatchers.eq(node), ArgumentMatchers.eq(null)));
  }

  @Test
  public void testShouldNotTraversePrototypeFunction() {
    Node functionNode = Node.newFunctionNode(Token.FUNCTION, new int[] {Token.NAME}, null, null, null);
    Node prototypeNode = Node.newNameNode("prototype");
    Node prototypeAccessNode = Node.newGetPropNode(prototypeNode, functionNode);

    assertTrue(checker.shouldTraverse(ArgumentMatchers.eq(traversal), ArgumentMatchers.eq(functionNode), ArgumentMatchers.eq(prototypeAccessNode)));
  }

  @Test
  public void testShouldNotTraverseAssignmentToPrototypeProperty() {
    Node assignNode = Node.newAssignNode(Token.ASSIGN, Node.newNameNode("prototype"), Node.newThisNode());
    Node functionNode = Node.newFunctionNode(Token.FUNCTION, new int[] {Token.NAME}, null, null, null);

    assertFalse(checker.shouldTraverse(ArgumentMatchers.eq(traversal), ArgumentMatchers.eq(functionNode), ArgumentMatchers.eq(assignNode)));
  }

  @Test
  public void testShouldTraverseDirectAssignmentToThis() {
    Node assignNode = Node.newAssignNode(Token.ASSIGN, Node.newThisNode(), Node.newNameNode("useful"));

    assertTrue(checker.shouldTraverse(ArgumentMatchers.eq(traversal), ArgumentMatchers.eq(assignNode), ArgumentMatchers.eq(null)));
  }

  @Test
  public void testShouldNotTraverseAssignmentToVariable() {
    Node assignNode = Node.newAssignNode(Token.ASSIGN, Node.newNameNode("a"), Node.newThisNode());

    assertFalse(checker.shouldTraverse(ArgumentMatchers.eq(traversal), ArgumentMatchers.eq(assignNode), ArgumentMatchers.eq(null)));
  }

  @Test
  public void testShouldTraverseAssignmentToObjectLiteral() {
    Node objectNode = Node.newObjectLiteralNode(Token.NAME, new Node[]{Node.newNameNode("x"), Node.newFunctionNode(Token.FUNCTION, new int[] {Token.NAME}, null, null, null)});

    Node assignNode = Node.newAssignNode(Token.ASSIGN, objectNode, Node.newThisNode());

    assertTrue(checker.shouldTraverse(ArgumentMatchers.eq(traversal), ArgumentMatchers.eq(assignNode), ArgumentMatchers.eq(null)));
  }

  @Test
  public void testShouldReportGlobalThis() {
    Node thisNode = Node.newThisNode();

    Node node = Node.newAssignNode(Token.ASSIGN, Node.newNameNode("globalThis"), thisNode);
    Node parent = Node.newNameNode("globalThis");

    when(compiler.report(ArgumentMatchers.eq(traversal), ArgumentMatchers.eq(ArgumentMatchers.isA(Node.class)), ArgumentMatchers.eq(ArgumentMatchers.isA(CheckLevel.class)), ArgumentMatchers.eq(DiagnosticType.warning(ArgumentMatchers.contains("JSC_USED_GLOBAL_THIS")))))
        .thenReturn(null);

    checker.visit(ArgumentMatchers.eq(traversal), ArgumentMatchers.eq(node), ArgumentMatchers.eq(parent));

    verify(compiler).report(ArgumentMatchers.eq(traversal), ArgumentMatchers.eq(node), ArgumentMatchers.eq(level), ArgumentMatchers.eq(GLOBAL_THIS));
  }
}