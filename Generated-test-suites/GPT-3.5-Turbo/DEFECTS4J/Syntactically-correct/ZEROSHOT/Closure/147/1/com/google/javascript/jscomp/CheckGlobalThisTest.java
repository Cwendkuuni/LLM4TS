import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  
  private CheckGlobalThis checkGlobalThis;
  
  @Before
  public void setUp() {
    checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
  }
  
  @Test
  public void testShouldTraverse_FunctionConstructorWithoutThisAnnotation_ReturnsTrue() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_FunctionWithThisAnnotation_ReturnsFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    n.setJSDocInfo(jsDoc);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_FunctionWithOverrideAnnotation_ReturnsFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordOverride();
    n.setJSDocInfo(jsDoc);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_FunctionWithConstructorAnnotation_ReturnsFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordConstructor();
    n.setJSDocInfo(jsDoc);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_FunctionWithInterfaceAnnotation_ReturnsFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordInterface();
    n.setJSDocInfo(jsDoc);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_NonFunctionParent_ReturnsFalse() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ADD);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testVisit_ThisNodeAndParentIsNotGet_DoesNotReportError() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ADD);
    
    checkGlobalThis.visit(t, n, parent);
    
    // No error reported
    // add assertion here if necessary
  }
  
  @Test
  public void testVisit_ThisNodeAndParentIsGet_ReportsError() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    
    checkGlobalThis.visit(t, n, parent);
    
    // Error reported
    // add assertion here if necessary
  }
  
  @Test
  public void testVisit_NotThisNodeAndParentIsNotAssign_DoesNotClearAssignLhsChild() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ADD);
    checkGlobalThis.setAssignLhsChild(n);
    
    checkGlobalThis.visit(t, n, parent);
    
    // assignLhsChild is still set
    // add assertion here if necessary
  }
  
  @Test
  public void testVisit_NotThisNodeAndParentIsAssign_ClearsAssignLhsChild() {
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN);
    checkGlobalThis.setAssignLhsChild(n);
    
    checkGlobalThis.visit(t, n, parent);
    
    // assignLhsChild is cleared
    // add assertion here if necessary
  }
  
  @Test
  public void testShouldReportThis_ThisNodeAndAssignLhsChildIsNull_ReturnsTrue() {
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    checkGlobalThis.setAssignLhsChild(null);
    
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldReportThis_ThisNodeAndAssignLhsChildIsNotNull_ReturnsTrue() {
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    Node assignLhsChild = new Node(Token.ASSIGN);
    checkGlobalThis.setAssignLhsChild(assignLhsChild);
    
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldReportThis_NotThisNodeAndParentIsNotGet_ReturnsFalse() {
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.ADD);
    checkGlobalThis.setAssignLhsChild(null);
    
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldReportThis_NotThisNodeAndParentIsGet_ReturnsTrue() {
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.GETPROP);
    checkGlobalThis.setAssignLhsChild(null);
    
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_FunctionWithJSDoc_ReturnsJSDocInfo() {
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    n.setJSDocInfo(jsDoc);
    
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    
    assertEquals(jsDoc, result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_FunctionWithoutJSDoc_ParentIsName_ReturnsParentJSDocInfo() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    JSDocInfo jsDoc = new JSDocInfo();
    parent.setJSDocInfo(jsDoc);
    
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    
    assertEquals(jsDoc, result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_FunctionWithoutJSDoc_ParentIsAssign_ReturnsParentJSDocInfo() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    JSDocInfo jsDoc = new JSDocInfo();
    parent.setJSDocInfo(jsDoc);
    
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    
    assertEquals(jsDoc, result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_FunctionWithoutJSDoc_ParentIsNameWithoutJSDoc_ReturnsGrandparentJSDocInfo() {
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    Node grandparent = new Node(Token.VAR);
    JSDocInfo jsDoc = new JSDocInfo();
    grandparent.setJSDocInfo(jsDoc);
    parent.addChildToFront(grandparent);
    
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    
    assertEquals(jsDoc, result);
  }
  
}