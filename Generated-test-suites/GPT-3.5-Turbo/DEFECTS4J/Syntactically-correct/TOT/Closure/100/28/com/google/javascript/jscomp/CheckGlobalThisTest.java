import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {

  private CheckGlobalThis check;

  @Before
  public void setUp() {
    check = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
  }

  @Test
  public void testShouldTraverse_GlobalContext_ReturnsTrue() {
    Node testNode = new Node(Token.SCRIPT, new Node(Token.EMPTY));
    NodeTraversal testTraversal = new NodeTraversal(new AbstractCompiler(), check);
    assertTrue(check.shouldTraverse(testTraversal, testNode, null));
  }

  @Test
  public void testShouldTraverse_FunctionWithConstructorJsDoc_ReturnsFalse() {
    Node testNode = new Node(Token.FUNCTION);
    NodeTraversal testTraversal = new NodeTraversal(new AbstractCompiler(), check);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    testNode.setJSDocInfo(jsDoc);
    assertFalse(check.shouldTraverse(testTraversal, testNode, null));
  }

  @Test
  public void testShouldTraverse_FunctionWithThisJsDoc_ReturnsFalse() {
    Node testNode = new Node(Token.FUNCTION);
    NodeTraversal testTraversal = new NodeTraversal(new AbstractCompiler(), check);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    testNode.setJSDocInfo(jsDoc);
    assertFalse(check.shouldTraverse(testTraversal, testNode, null));
  }

  @Test
  public void testShouldTraverse_FunctionWithNestedFunction_ReturnsTrue() {
    Node testNode = new Node(Token.FUNCTION);
    NodeTraversal testTraversal = new NodeTraversal(new AbstractCompiler(), check);
    Node parent = new Node(Token.NAME);
    parent.getFirstChild().setString("parentFunction");
    parent.addChildToFront(testNode);
    assertTrue(check.shouldTraverse(testTraversal, testNode, parent));
  }

  @Test
  public void testVisit_ThisWithUnsafeUse_AddsWarning() {
    Node testNode = new Node(Token.THIS);
    NodeTraversal testTraversal = new NodeTraversal(new AbstractCompiler(), check);
    Node parent = new Node(Token.NAME);
    parent.getLastChild().setString("propertyName");
    check.visit(testTraversal, testNode, parent);
    assertTrue(testTraversal.getErrors().toString().contains("dangerous use of the global 'this' object"));
  }

  @Test
  public void testVisit_LhsChildOfAssign_ReportsThisAsUnsafe() {
    Node testNode = new Node(Token.THIS);
    NodeTraversal testTraversal = new NodeTraversal(new AbstractCompiler(), check);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(testNode);
    check.visit(testTraversal, testNode, parent);
    assertTrue(testTraversal.getErrors().toString().contains("dangerous use of the global 'this' object"));
  }

  @Test
  public void testVisit_LhsChildOfAssign_ClearsAssignLhsChild() {
    Node testNode = new Node(Token.THIS);
    NodeTraversal testTraversal = new NodeTraversal(new AbstractCompiler(), check);
    Node parent = new Node(Token.ASSIGN);
    parent.addChildToFront(testNode);
    check.visit(testTraversal, testNode, parent);
    assertNull(check.assignLhsChild);
  }

  @Test
  public void testShouldReportThis_WithAssignLhsChild_ReturnsTrue() {
    Node testNode = new Node(Token.THIS);
    NodeTraversal testTraversal = new NodeTraversal(new AbstractCompiler(), check);
    check.assignLhsChild = new Node(Token.NAME);
    assertTrue(check.shouldReportThis(testNode, null));
  }

  @Test
  public void testShouldReportThis_PropertyAccess_ReturnsFalse() {
    Node testNode = new Node(Token.THIS);
    NodeTraversal testTraversal = new NodeTraversal(new AbstractCompiler(), check);
    Node parent = new Node(Token.NAME);
    parent.getFirstChild().setString("propertyName");
    assertFalse(check.shouldReportThis(testNode, parent));
  }

  @Test
  public void testGetFunctionJsDocInfo_FunctionWithJsDoc_ReturnsJsDoc() {
    Node testNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    parent.getFirstChild().setString("functionName");
    testNode.setParent(parent);
    JSDocInfo jsDoc = new JSDocInfo();
    testNode.setJSDocInfo(jsDoc);
    assertEquals(jsDoc, check.getFunctionJsDocInfo(testNode));
  }

  @Test
  public void testGetFunctionJsDocInfo_ParentAssign_ReturnsJsDoc() {
    Node testNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    parent.getFirstChild().setString("functionName");
    testNode.setParent(parent);
    JSDocInfo jsDoc = new JSDocInfo();
    parent.setJSDocInfo(jsDoc);
    assertEquals(jsDoc, check.getFunctionJsDocInfo(testNode));
  }

}