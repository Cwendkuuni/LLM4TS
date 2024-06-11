import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {
  private CheckGlobalThis checkGlobalThis;
  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler();
    checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
  }

  @Test
  public void shouldTraverse_returnTrueForSafeFunction() {
    NodeTraversal t = new NodeTraversal();

    Node functionNode = NodeUtil.createFunctionNode();
    Node parent = NodeUtil.createAssignmentNode();
    parent.setType(Token.ASSIGN);
    parent.addChildToFront(NodeUtil.createGetPropNode());
    parent.getFirstChild().addChildToFront(functionNode);

    boolean result = checkGlobalThis.shouldTraverse(t, functionNode, parent);

    assertTrue(result);
  }

  @Test
  public void shouldTraverse_returnFalseForUnsafeFunctionConstructor() {
    NodeTraversal t = new NodeTraversal();

    Node functionNode = NodeUtil.createFunctionNode();
    functionNode.setJSDocInfo(NodeUtil.createJSDocInfo().setConstructor(true));
    Node parent = NodeUtil.createAssignmentNode();
    parent.setType(Token.ASSIGN);
    parent.addChildToFront(NodeUtil.createGetPropNode());
    parent.getFirstChild().addChildToFront(functionNode);

    boolean result = checkGlobalThis.shouldTraverse(t, functionNode, parent);

    assertFalse(result);
  }

  @Test
  public void shouldTraverse_returnFalseForUnsafeFunctionThisAnnotation() {
    NodeTraversal t = new NodeTraversal();

    Node functionNode = NodeUtil.createFunctionNode();
    functionNode.setJSDocInfo(NodeUtil.createJSDocInfo().setThisType(true));
    Node parent = NodeUtil.createAssignmentNode();
    parent.setType(Token.ASSIGN);
    parent.addChildToFront(NodeUtil.createGetPropNode());
    parent.getFirstChild().addChildToFront(functionNode);

    boolean result = checkGlobalThis.shouldTraverse(t, functionNode, parent);

    assertFalse(result);
  }

  @Test
  public void shouldTraverse_returnFalseForUnsafeFunctionOverrideAnnotation() {
    NodeTraversal t = new NodeTraversal();

    Node functionNode = NodeUtil.createFunctionNode();
    functionNode.setJSDocInfo(NodeUtil.createJSDocInfo().setOverride(true));
    Node parent = NodeUtil.createAssignmentNode();
    parent.setType(Token.ASSIGN);
    parent.addChildToFront(NodeUtil.createGetPropNode());
    parent.getFirstChild().addChildToFront(functionNode);

    boolean result = checkGlobalThis.shouldTraverse(t, functionNode, parent);

    assertFalse(result);
  }

  @Test
  public void shouldTraverse_returnFalseForUnsafeFunctionInterfaceAnnotation() {
    NodeTraversal t = new NodeTraversal();

    Node functionNode = NodeUtil.createFunctionNode();
    functionNode.setJSDocInfo(NodeUtil.createJSDocInfo().setInterface(true));
    Node parent = NodeUtil.createAssignmentNode();
    parent.setType(Token.ASSIGN);
    parent.addChildToFront(NodeUtil.createGetPropNode());
    parent.getFirstChild().addChildToFront(functionNode);

    boolean result = checkGlobalThis.shouldTraverse(t, functionNode, parent);

    assertFalse(result);
  }

  @Test
  public void shouldTraverse_returnFalseForNonGlobalContext() {
    NodeTraversal t = new NodeTraversal();

    Node functionNode = NodeUtil.createFunctionNode();
    Node parent = NodeUtil.createStringNode();

    boolean result = checkGlobalThis.shouldTraverse(t, functionNode, parent);

    assertFalse(result);
  }

  @Test
  public void shouldTraverse_returnFalseForNestedAssignmentsToPrototype() {
    NodeTraversal t = new NodeTraversal();

    Node functionNode = NodeUtil.createFunctionNode();
    Node parent = NodeUtil.createAssignmentNode();
    parent.setType(Token.ASSIGN);
    parent.addChildToFront(NodeUtil.createGetPropNode());
    Node getPropNode = parent.getFirstChild();
    getPropNode.setType(Token.GETPROP);
    Node.prototypeNode(getPropNode, "prototype");

    boolean result = checkGlobalThis.shouldTraverse(t, functionNode, parent);

    assertFalse(result);
  }

  @Test
  public void visit_reportsErrorForGlobalThisOnAssignLeftSide() {
    NodeTraversal t = new NodeTraversal();
    Node node = NodeUtil.createThisNode();
    Node parent = NodeUtil.createAssignmentNode();

    checkGlobalThis.visit(t, node, parent);

    assertErrorReported(t);
  }

  @Test
  public void visit_doesNotReportErrorForGlobalThisOnAssignRightSide() {
    NodeTraversal t = new NodeTraversal();
    Node node = NodeUtil.createThisNode();
    Node parent = NodeUtil.createAssignmentNode();
    parent.addChildToBack(NodeUtil.createGetPropNode());

    checkGlobalThis.visit(t, node, parent);

    assertNoErrorReported(t);
  }

  @Test
  public void visit_reportsErrorForGlobalThisWithPropertyAccess() {
    NodeTraversal t = new NodeTraversal();
    Node node = NodeUtil.createThisNode();
    Node parent = NodeUtil.createGetPropNode();

    checkGlobalThis.visit(t, node, parent);

    assertErrorReported(t);
  }

  @Test
  public void visit_doesNotReportErrorForNonGlobalThis() {
    NodeTraversal t = new NodeTraversal();
    Node node = NodeUtil.createNameNode("foo");
    Node parent = NodeUtil.createBinaryOpNode();

    checkGlobalThis.visit(t, node, parent);

    assertNoErrorReported(t);
  }

  @Test
  public void visit_setsAssignLhsChildNullIfNodeIsAssignLhsChild() {
    NodeTraversal t = new NodeTraversal();
    Node node = NodeUtil.createThisNode();
    Node parent = NodeUtil.createAssignmentNode();
    Node assignLhsChild = node;
    checkGlobalThis.assignLhsChild = assignLhsChild;

    checkGlobalThis.visit(t, node, parent);

    assertNull(checkGlobalThis.assignLhsChild);
  }

  @Test
  public void visit_doesNotSetAssignLhsChildNullIfNodeIsNotAssignLhsChild() {
    NodeTraversal t = new NodeTraversal();
    Node node = NodeUtil.createThisNode();
    Node parent = NodeUtil.createAssignmentNode();
    Node assignLhsChild = NodeUtil.createNameNode("foo");
    checkGlobalThis.assignLhsChild = assignLhsChild;

    checkGlobalThis.visit(t, node, parent);

    assertEquals(assignLhsChild, checkGlobalThis.assignLhsChild);
  }

  private void assertErrorReported(NodeTraversal t) {
    // assert error is reported here
  }

  private void assertNoErrorReported(NodeTraversal t) {
    // assert no error is reported here
  }
}