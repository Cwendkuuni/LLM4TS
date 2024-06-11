import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CheckLevel.*;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {

  @org.junit.Test
  public void testShouldTraverseFunctions() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);
    NodeTraversal traversal = new NodeTraversal(null, checker);

    Node functionNode = createFunctionNode("function foo() { }");

    assertTrue(checker.shouldTraverse(traversal, functionNode, null));
  }

  @org.junit.Test
  public void testShouldNotTraverseConstructor() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);
    NodeTraversal traversal = new NodeTraversal(null, checker);

    Node constructorNode = createFunctionNode("function Foo() { }");

    assertFalse(checker.shouldTraverse(traversal, constructorNode, null));
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionWithThisAnnotation() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);
    NodeTraversal traversal = new NodeTraversal(null, checker);

    Node functionNodeWithThisAnnotation = createFunctionNode(
        "/**@this {String}*/ function foo() { }");

    assertFalse(checker.shouldTraverse(traversal, functionNodeWithThisAnnotation, null));
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionInsideBlock() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);
    NodeTraversal traversal = new NodeTraversal(null, checker);

    Node parentBlockNode = createBlockNode("{}");
    Node functionNode = createFunctionNode("function foo() { }", parentBlockNode);

    assertFalse(checker.shouldTraverse(traversal, functionNode, parentBlockNode));
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionInsideScript() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);
    NodeTraversal traversal = new NodeTraversal(null, checker);

    Node scriptNode = createScriptNode("function foo() { }");
    Node functionNode = createFunctionNode("function foo() { }", scriptNode);

    assertFalse(checker.shouldTraverse(traversal, functionNode, scriptNode));
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionInsideNameNode() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);
    NodeTraversal traversal = new NodeTraversal(null, checker);

    Node nameNode = createNameNode("var foo = function() { }");
    Node functionNode = createFunctionNode("function foo() { }", nameNode);

    assertFalse(checker.shouldTraverse(traversal, functionNode, nameNode));
  }

  @org.junit.Test
  public void testShouldNotTraverseFunctionInsideAssignment() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);
    NodeTraversal traversal = new NodeTraversal(null, checker);

    Node parentAssignmentNode = createAssignmentNode("a = function foo() { }");
    Node functionNode = createFunctionNode("function foo() { }", parentAssignmentNode);

    assertFalse(checker.shouldTraverse(traversal, functionNode, parentAssignmentNode));
  }

  @org.junit.Test
  public void testShouldTraverseLeftSideOfAssignment() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);
    NodeTraversal traversal = new NodeTraversal(null, checker);

    Node parentAssignmentNode = createAssignmentNode("a = this");
    Node leftSideNode = parentAssignmentNode.getFirstChild();

    assertTrue(checker.shouldTraverse(traversal, leftSideNode, parentAssignmentNode));
  }

  @org.junit.Test
  public void testShouldTraversePropertyAccess() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);
    NodeTraversal traversal = new NodeTraversal(null, checker);

    Node parentNode = createNameNode("var a = { prop: this }");
    Node propertyAccessNode = createGetPropertyNode(parentNode, "prop");

    assertTrue(checker.shouldTraverse(traversal, propertyAccessNode, parentNode));
  }

  @org.junit.Test
  public void testShouldTraverseNestedAssignments() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);
    NodeTraversal traversal = new NodeTraversal(null, checker);

    Node parentAssignmentNode = createAssignmentNode("a = this");
    Node nestedAssignmentNode = createAssignmentNode("(a = this).prop = this");

    Node leftSideNode = parentAssignmentNode.getFirstChild();
    assertTrue(checker.shouldTraverse(traversal, leftSideNode, parentAssignmentNode));

    assertTrue(checker.shouldTraverse(traversal, nestedAssignmentNode, parentAssignmentNode));
  }

  @org.junit.Test(expected = AssertionError.class)
  public void testShouldReportThis() {
    CheckGlobalThis checker = new CheckGlobalThis(null, WARNING);
    AbstractCompiler compiler = new AbstractCompiler();

    Node parentNode = createNameNode("function test() { this; }");
    Node nodeToTest = parentNode.getFirstChild();

    NodeTraversal traversal = new NodeTraversal(compiler, checker);
    traversal.traverse(parentNode);

    fail("Expected a diagnostic message, but none was reported.");
  }

  private Node createFunctionNode(String code, Node parentNode) {
    return new Node(Token.FUNCTION, null, code.split("\n"), null, null, null, null, null, null, null, null, parentNode);
  }

  private Node createFunctionNode(String code) {
    return createFunctionNode(code, null);
  }

  private Node createScriptNode(String code) {
    return new Node(Token.SCRIPT, null, code.split("\n"), null, null, null, null, null, null, null, null, null);
  }

  private Node createBlockNode(String code) {
    return new Node(Token.BLOCK, null, code.split("\n"), null, null, null, null, null, null, null, null, null);
  }

  private Node createNameNode(String code) {
    return new Node(Token.NAME, null, code.split("\n"), null, null, null, null, null, null, null, null, null);
  }

  private Node createAssignmentNode(String code) {
    return new Node(Token.ASSIGN, null, code.split("\n"), null, null, null, null, null, null, null, null, null);
  }

  private Node createGetPropertyNode(Node objectNode, String propertyName) {
    return new Node(Token.GETPROP, null, null, null, null, null, null, null, null, null, null, objectNode, null, propertyName, null, null, null, null, null, null, null, null);
  }
}