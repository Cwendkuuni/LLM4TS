import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {
  
  private CheckGlobalThis checkGlobalThis;
  
  @Before
  public void setUp() {
    checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
  }
  
  @Test
  public void testShouldTraverse_FunctionWithConstructorJsDoc() {
    // Setup
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordConstructor();
    n.setJSDocInfo(jsDoc);
    // Exercise
    boolean result = checkGlobalThis.shouldTraverse(t, n, null);
    // Verify
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_FunctionWithThisJsDoc() {
    // Setup
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    n.setJSDocInfo(jsDoc);
    // Exercise
    boolean result = checkGlobalThis.shouldTraverse(t, n, null);
    // Verify
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_FunctionWithNoJsDoc() {
    // Setup
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    // Exercise
    boolean result = checkGlobalThis.shouldTraverse(t, n, null);
    // Verify
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_Assignment_LeftHandSide() {
    // Setup
    NodeTraversal t = new NodeTraversal();
    Node parent = new Node(Token.ASSIGN);
    Node n = new Node(Token.NAME);
    parent.addChildToFront(n);
    // Exercise
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    // Verify
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_Assignment_RightHandSide() {
    // Setup
    NodeTraversal t = new NodeTraversal();
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.GETPROP);
    parent.addChildToFront(lhs);
    Node n = new Node(Token.NAME);
    lhs.addChildToBack(n);
    // Exercise
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    // Verify
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_Assignment_RightHandSide_PrototypeProperty() {
    // Setup
    NodeTraversal t = new NodeTraversal();
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.GETPROP);
    parent.addChildToFront(lhs);
    Node prototype = new Node(Token.STRING);
    prototype.setString("prototype");
    lhs.addChildToBack(prototype);
    Node n = new Node(Token.NAME);
    lhs.addChildToBack(n);
    // Exercise
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    // Verify
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_Assignment_RightHandSide_NestedAssignment() {
    // Setup
    NodeTraversal t = new NodeTraversal();
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.GETPROP);
    parent.addChildToFront(lhs);
    Node n = new Node(Token.NAME);
    lhs.addChildToBack(n);
    Node property = new Node(Token.GETPROP);
    lhs.addChildToBack(property);
    property.addChildToBack(new Node(Token.STRING, "property"));
    // Exercise
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    // Verify
    assertFalse(result);
  }
  
  @Test
  public void testVisit_GlobalThisReported() {
    // Setup
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NAME);
    // Exercise
    checkGlobalThis.visit(t, n, parent);
    // Verify
    // TODO verify the error is reported
  }
  
  @Test
  public void testVisit_AssignLhsChildReset() {
    // Setup
    NodeTraversal t = new NodeTraversal();
    checkGlobalThis.assignLhsChild = new Node();
    Node n = new Node();
    Node parent = new Node();
    // Exercise
    checkGlobalThis.visit(t, n, parent);
    // Verify
    assertNull(checkGlobalThis.assignLhsChild);
  }
  
  @Test
  public void testVisit_AssignLhsChildNotReset() {
    // Setup
    NodeTraversal t = new NodeTraversal();
    checkGlobalThis.assignLhsChild = new Node();
    Node n = new Node();
    Node parent = checkGlobalThis.assignLhsChild.getParent();
    // Exercise
    checkGlobalThis.visit(t, n, parent);
    // Verify
    assertNotNull(checkGlobalThis.assignLhsChild);
  }
  
  @Test
  public void testVisit_GlobalThisNotReported_AssignLhsChildNull() {
    // Setup
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node();
    checkGlobalThis.assignLhsChild = null;
    // Exercise
    checkGlobalThis.visit(t, n, parent);
    // Verify
    // TODO verify that the error is not reported
  }
  
  @Test
  public void testShouldReportThis_AssignLhsChildNotNull() {
    // Setup
    Node n = new Node();
    Node parent = new Node();
    checkGlobalThis.assignLhsChild = new Node();
    // Exercise
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    // Verify
    assertTrue(result);
  }
  
  @Test
  public void testShouldReportThis_AssignLhsChildNull() {
    // Setup
    Node n = new Node();
    Node parent = new Node();
    checkGlobalThis.assignLhsChild = null;
    // Exercise
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    // Verify
    assertFalse(result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_JsDocOnFunction() {
    // Setup
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    n.setJSDocInfo(jsDoc);
    // Exercise
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    // Verify
    assertEquals(jsDoc, result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_JsDocOnName() {
    // Setup
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    parent.addChildToBack(n);
    JSDocInfo jsDoc = new JSDocInfo();
    parent.setJSDocInfo(jsDoc);
    // Exercise
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    // Verify
    assertEquals(jsDoc, result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_JsDocOnName_ParentIsVar() {
    // Setup
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    Node gramps = new Node(Token.VAR);
    gramps.addChildToBack(parent);
    parent.addChildToBack(n);
    JSDocInfo jsDoc = new JSDocInfo();
    gramps.setJSDocInfo(jsDoc);
    // Exercise
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    // Verify
    assertEquals(jsDoc, result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_JsDocOnName_ParentIsAssign() {
    // Setup
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    parent.addChildToBack(n);
    Node gramps = new Node(Token.ASSIGN);
    gramps.addChildToFront(parent);
    JSDocInfo jsDoc = new JSDocInfo();
    gramps.setJSDocInfo(jsDoc);
    // Exercise
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    // Verify
    assertEquals(jsDoc, result);
  }
}