import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {
  
  @Test
  public void testShouldTraverse_noJsDocInfoAssigned_correctExit() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.EMPTY);
    Node parent = new Node(Token.EMPTY);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_constructorJsDocInfoAssigned_correctExit() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.EMPTY);
    
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setConstructor(true);
    n.setJSDocInfo(jsDocInfo);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_thisTypeJsDocInfoAssigned_correctExit() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.EMPTY);
    
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.addThisType();
    n.setJSDocInfo(jsDocInfo);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_overrideJsDocInfoAssigned_correctExit() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.EMPTY);
    
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setOverride(true);
    n.setJSDocInfo(jsDocInfo);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_blockTokenParent_correctExit() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_scriptTokenParent_correctExit() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_nameTokenParent_correctExit() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_assignTokenParent_correctExit() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testVisit_tokenThis_shouldReportThis() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.EMPTY);
    
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testVisit_notAssignLhsChild_shouldNotReportThis() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testVisit_assignLhsChild_shouldReportThis() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    
    NodeTraversal t = new NodeTraversal(null, null);
    Node n = new Node(Token.THIS);
    
    checkGlobalThis.visit(t, n, n);
    
    assertNull(checkGlobalThis.getAssignLhsChild());
  }
  
  @Test
  public void testGetFunctionJsDocInfo_jsDocInfoExists_correctResult() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = new JSDocInfo();
    n.setJSDocInfo(jsDocInfo);
    
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    
    assertEquals(jsDocInfo, result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_parentNameNoJsDocInfo_correctResult() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    n.setParent(parent);
    
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    
    assertNull(result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_parentAssignNoJsDocInfo_correctResult() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    n.setParent(parent);
    
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    
    assertNull(result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_nameParentJsDocInfoExists_correctResult() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    n.setParent(parent);
    
    JSDocInfo jsDocInfo = new JSDocInfo();
    parent.setJSDocInfo(jsDocInfo);
    
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    
    assertEquals(jsDocInfo, result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_assignParentJsDocInfoExists_correctResult() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    n.setParent(parent);
    
    JSDocInfo jsDocInfo = new JSDocInfo();
    parent.setJSDocInfo(jsDocInfo);
    
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    
    assertEquals(jsDocInfo, result);
  }
}