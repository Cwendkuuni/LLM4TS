import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;

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
  public void testShouldTraverseFunction() {
    Node node = createNode("function foo() { }");
    assertTrue(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldNotTraverseConstructorFunction() {
    Node node = createNode("function foo() { }", Node.FUNCTION_DECLARATION);
    assertFalse(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    Node node = createNode("@this function foo() { }");
    assertFalse(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionWithOverrideAnnotation() {
    Node node = createNode("@override function foo() { }");
    assertFalse(checker.shouldTraverse(traversal, node, null));
  }

  @Test
  public void testShouldNotTraverseFunctionInNonGlobalContext() {
    Node parentNode = createNode("function bar() { function foo() { } }");
    Node node = createNode("function foo() { }", Node.FUNCTION_DECLARATION);
    node.setParent(parentNode);
    assertFalse(checker.shouldTraverse(traversal, node, parentNode));
  }

  @Test
  public void testShouldTraverseLeftSideOfAssignment() {
    Node leftNode = createNode("a = this");
    Node rightNode = createNode("this");
    Node parentNode = createNode("var a = {}", Node.BLOCK);
    Node assignNode = createNode("=", Token.ASSIGN);

    parentNode.addChild(leftNode);
    parentNode.addChild(assignNode);
    parentNode.addChild(rightNode);

    assertTrue(checker.shouldTraverse(traversal, rightNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignmentToPrototypeProperty() {
    Node leftNode = createNode("a.prototype.x = this");
    Node rightNode = createNode("this");
    Node parentNode = createNode("var a = function() { this.x = 0 }");

    assertFalse(checker.shouldTraverse(traversal, rightNode, parentNode));
  }

  @Test
  public void testShouldNotTraverseRightSideOfAssignmentToPrototypeSubproperty() {
    Node leftNode = createNode("a.prototype.x.y = this");
    Node rightNode = createNode("this");
    Node parentNode = createNode("var a = function() { this.x = { y: 0 } }");

    assertFalse(checker.shouldTraverse(traversal, rightNode, parentNode));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfAssignment() {
    Node leftNode = createNode("a = this");
    Node rightNode = createNode("this");
    Node parentNode = createNode("var a = {}", Node.BLOCK);
    Node assignNode = createNode("=", Token.ASSIGN);

    parentNode.addChild(leftNode);
    parentNode.addChild(assignNode);
    parentNode.addChild(rightNode);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(compiler.getTraversal(traversal)).thenReturn(traversal);

    checker.visit(traversal, rightNode, parentNode);

    assertTrue(compiler.reportedErrorWithType(TraversableErrorType.JSC_USED_GLOBAL_THIS));
  }

  @Test
  public void testShouldNotReportThisOnRightSideOfAssignment() {
    Node leftNode = createNode("a = {}", Node.VAR_DECLARATION);
    Node rightNode = createNode("this");
    Node parentNode = createNode("var a = {}", Node.BLOCK);
    Node assignNode = createNode("=", Token.ASSIGN);

    parentNode.addChild(leftNode);
    parentNode.addChild(assignNode);
    parentNode.addChild(rightNode);

    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    NodeTraversal traversal = mock(NodeTraversal.class);
    when(compiler.getTraversal(traversal)).thenReturn(traversal);

    checker.visit(traversal, rightNode, parentNode);

    assertFalse(compiler.reportedErrorWithType(TraversableErrorType.JSC_USED_GLOBAL_THIS));
  }

  @Test
  public void testShouldNotReportThisOnLeftSideOfPropertyAccess() {
    Node leftNode = createNode("a.prototype.x = this");
    Node rightNode = createNode("this");
    Node parentNode = createNode("var a = function() { this.x = 0 }");

    assertFalse(checker.shouldReportThis(rightNode, parentNode));
  }

  @Test
  public void testShouldReportThisOnLeftSideOfPropertyAccessThroughQualifiedName() {
    Node leftNode = createNode("a.prototype.x.y = this");
    Node rightNode = createNode("this");
    Node parentNode = createNode("var a = function() { this.x = { y: 0 } }");

    assertTrue(checker.shouldReportThis(rightNode, parentNode));
  }

  private Node createNode(String code, int nodeType) {
    return new Node(Token.newToken(code, nodeType));
  }

  private Node createNode(String code) {
    return new Node(Token.newToken(code));
  }
}