import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_FunctionIsAConstructor_NotTraversed() {
    // Create the necessary objects and inputs
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobal = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal();

    // Set up the test case
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    functionNode.setJSDocInfo(jsDoc);

    // Assertion
    assertFalse(checkGlobal.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldTraverse_FunctionHasThisAnnotation_NotTraversed() {
    // Create the necessary objects and inputs
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobal = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal();

    // Set up the test case
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType("TestClass");
    functionNode.setJSDocInfo(jsDoc);

    // Assertion
    assertFalse(checkGlobal.shouldTraverse(traversal, functionNode, null));
  }

  @Test
  public void testShouldTraverse_FunctionHasNoLogicalPlace_NotTraversed() {
    // Create the necessary objects and inputs
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobal = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal();

    // Set up the test case
    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.STRING_KEY);
    parent.addChildToFront(functionNode);

    // Assertion
    assertFalse(checkGlobal.shouldTraverse(traversal, functionNode, parent));
  }

  @Test
  public void testShouldTraverse_VariableAssignment_FunctionNotTraversed() {
    // Create the necessary objects and inputs
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobal = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal();

    // Set up the test case
    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(functionNode);

    // Assertion
    assertFalse(checkGlobal.shouldTraverse(traversal, functionNode, parent));
  }

  @Test
  public void testShouldTraverse_ObjectLiteral_FunctionNotTraversed() {
    // Create the necessary objects and inputs
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobal = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal();

    // Set up the test case
    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    Node grandParent = new Node(Token.NAME);
    parent.addChildToFront(functionNode);
    grandParent.addChildToFront(parent);

    // Assertion
    assertFalse(checkGlobal.shouldTraverse(traversal, functionNode, grandParent));
  }

  @Test
  public void testShouldTraverse_PrototypeAssignment_NotTraversed() {
    // Create the necessary objects and inputs
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobal = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal();

    // Set up the test case
    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToFront(functionNode);
    Node lastChild = new Node(Token.STRING);
    lastChild.setString("prototype");
    parent.addChildToBack(lastChild);

    // Assertion
    assertFalse(checkGlobal.shouldTraverse(traversal, functionNode, parent));
  }

  @Test
  public void testShouldTraverse_PrototypeAssignment_Subproperty_NotTraversed() {
    // Create the necessary objects and inputs
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobal = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal();

    // Set up the test case
    Node functionNode = new Node(Token.FUNCTION);
    Node grandParent = new Node(Token.STRING_KEY);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToFront(functionNode);
    Node propNode = new Node(Token.GETPROP);
    propNode.addChildToFront(functionNode);
    Node lastChild = new Node(Token.STRING);
    lastChild.setString("prototype");
    propNode.addChildToBack(lastChild);
    grandParent.addChildToFront(parent);
    parent.addChildToFront(propNode);

    // Assertion
    assertFalse(checkGlobal.shouldTraverse(traversal, functionNode, grandParent));
  }

  @Test
  public void testShouldTraverse_RightSideAssignment_PropertyAccess_NotTraversed() {
    // Create the necessary objects and inputs
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobal = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal();

    // Set up the test case
    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToFront(functionNode);

    // Assertion
    assertFalse(checkGlobal.shouldTraverse(traversal, functionNode, parent));
  }

  @Test
  public void testShouldReportThis_AssignLhsChild_ReturnsTrue() {
    // Create the necessary objects and inputs
    CheckGlobalThis checkGlobal = new CheckGlobalThis(new AbstractCompiler());
    Node assignLhsChild = new Node(Token.THIS);
    checkGlobal.assignLhsChild = assignLhsChild;

    // Assertion
    assertTrue(checkGlobal.shouldReportThis(assignLhsChild));
  }

  @Test
  public void testShouldReportThis_ParentIsPropertyAccess_ReturnsTrue() {
    // Create the necessary objects and inputs
    CheckGlobalThis checkGlobal = new CheckGlobalThis(new AbstractCompiler());
    Node parent = new Node(Token.GETPROP);

    // Assertion
    assertTrue(checkGlobal.shouldReportThis(parent));
  }

  @Test
  public void testShouldReportThis_ParentIsNotPropertyAccess_ReturnsFalse() {
    // Create the necessary objects and inputs
    CheckGlobalThis checkGlobal = new CheckGlobalThis(new AbstractCompiler());
    Node parent = new Node(Token.ADD);

    // Assertion
    assertFalse(checkGlobal.shouldReportThis(parent));
  }

  @Test
  public void testVisit_ThisShouldBeReported_Reported() {
    // Create the necessary objects and inputs
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobal = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal();
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);

    // Assertion
    assertTrue(compiler.report(t.makeError(node, checkGlobal.GLOBAL_THIS)));
  }

  @Test
  public void testVisit_AssignLhsChild_Nullified() {
    // Create the necessary objects and inputs
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobal = new CheckGlobalThis(compiler);
    NodeTraversal traversal = new NodeTraversal();
    Node node = new Node(Token.THIS);
    checkGlobal.assignLhsChild = node;

    // Assertion
    assertNull(checkGlobal.assignLhsChild);
  }
}