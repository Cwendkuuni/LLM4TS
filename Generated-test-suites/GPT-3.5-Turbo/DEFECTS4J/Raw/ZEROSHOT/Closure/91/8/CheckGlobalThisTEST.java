import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse() {
    // Test shouldTraverse method, when n.getType() is not Token.FUNCTION
    // and parent.getType() is not Token.ASSIGN
    // It should return true
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_FunctionNotConstructor() {
    // Test shouldTraverse method, when n.getType() is Token.FUNCTION
    // and constructor is false in JSDocInfo
    // It should return true
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_FunctionNotInterface() {
    // Test shouldTraverse method, when n.getType() is Token.FUNCTION
    // and interface is false in JSDocInfo
    // It should return true
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(false);
    n.setJSDocInfo(jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_FunctionNotThisType() {
    // Test shouldTraverse method, when n.getType() is Token.FUNCTION
    // and thisType is false in JSDocInfo
    // It should return true
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(false);
    jsDoc.setInterface(false);
    jsDoc.setThisType(false);
    n.setJSDocInfo(jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_FunctionNotOverride() {
    // Test shouldTraverse method, when n.getType() is Token.FUNCTION
    // and override is false in JSDocInfo
    // It should return true
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(false);
    jsDoc.setInterface(false);
    jsDoc.setThisType(false);
    jsDoc.setOverride(false);
    n.setJSDocInfo(jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }

  @Test
  public void testShouldTraverse_ParentIsNotBlock() {
    // Test shouldTraverse method, when n.getType() is Token.FUNCTION 
    // and parent.getType() is not Token.BLOCK
    // It should return false
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(false);
    jsDoc.setInterface(false);
    jsDoc.setThisType(false);
    jsDoc.setOverride(false);
    n.setJSDocInfo(jsDoc);
    Node grandparent = new Node();
    parent.setParent(grandparent);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_ParentIsNotScript() {
    // Test shouldTraverse method, when n.getType() is Token.FUNCTION 
    // and parent.getType() is not Token.SCRIPT
    // It should return false
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(false);
    jsDoc.setInterface(false);
    jsDoc.setThisType(false);
    jsDoc.setOverride(false);
    n.setJSDocInfo(jsDoc);
    Node grandparent = new Node(Token.BLOCK);
    parent.setParent(grandparent);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_ParentIsNotName() {
    // Test shouldTraverse method, when n.getType() is Token.FUNCTION 
    // and parent.getType() is not Token.NAME
    // It should return false
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(false);
    jsDoc.setInterface(false);
    jsDoc.setThisType(false);
    jsDoc.setOverride(false);
    n.setJSDocInfo(jsDoc);
    Node grandparent = new Node(Token.SCRIPT);
    parent.setParent(grandparent);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void testShouldTraverse_ParentIsNotAssign() {
    // Test shouldTraverse method, when n.getType() is Token.FUNCTION 
    // and parent.getType() is not Token.ASSIGN
    // It should return false
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node();
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(false);
    jsDoc.setInterface(false);
    jsDoc.setThisType(false);
    jsDoc.setOverride(false);
    n.setJSDocInfo(jsDoc);
    Node grandparent = new Node(Token.NAME);
    parent.setParent(grandparent);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void testVisit() {
    // Test visit method, when n.getType() is Token.THIS and shouldReportThis is true
    // It should call compiler.report with t.makeError(n, level, GLOBAL_THIS)
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node();
    checkGlobalThis.visit(t, n, parent);
    // Verify compiler.report was called
    // Verify t.makeError was called with n, level, GLOBAL_THIS
  }

  @Test
  public void testVisit_AssignLhsChildNotNull() {
    // Test visit method, when assignLhsChild is not null
    // It should set assignLhsChild to null
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    checkGlobalThis.setAssignLhsChild(new Node());
    checkGlobalThis.visit(new NodeTraversal(), new Node(), new Node());
    assertNull(checkGlobalThis.getAssignLhsChild());
  }

  @Test
  public void testShouldReportThis_AssignLhsChildNotNull() {
    // Test shouldReportThis method, when assignLhsChild is not null
    // It should return true
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    checkGlobalThis.setAssignLhsChild(new Node());
    boolean result = checkGlobalThis.shouldReportThis(new Node(), new Node());
    assertTrue(result);
  }

  @Test
  public void testShouldReportThis() {
    // Test shouldReportThis method, when assignLhsChild is null and parent is not null
    // and parent is a GET node
    // It should return true
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    boolean result = checkGlobalThis.shouldReportThis(new Node(), new Node(Token.GET));
    assertTrue(result);
  }

  @Test
  public void testShouldReportThis_ParentIsNull() {
    // Test shouldReportThis method, when parent is null
    // It should return false
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    boolean result = checkGlobalThis.shouldReportThis(new Node(), null);
    assertFalse(result);
  }

  @Test
  public void testShouldReportThis_ParentIsNotGet() {
    // Test shouldReportThis method, when parent is not a GET node
    // It should return false
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    boolean result = checkGlobalThis.shouldReportThis(new Node(), new Node());
    assertFalse(result);
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    // Test getFunctionJsDocInfo method, when n.getJSDocInfo is not null
    // It should return the JSDocInfo object from n
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node();
    JSDocInfo jsDocInfo = new JSDocInfo();
    n.setJSDocInfo(jsDocInfo);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertEquals(jsDocInfo, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_ParentIsName() {
    // Test getFunctionJsDocInfo method, when n.getJSDocInfo is null
    // and parent.getType() is Token.NAME and parent.getJSDocInfo() is not null
    // It should return the JSDocInfo object from parent
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node();
    Node parent = new Node();
    JSDocInfo jsDocInfo = new JSDocInfo();
    parent.setJSDocInfo(jsDocInfo);
    n.setParent(parent);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertEquals(jsDocInfo, result);
  }

  @Test
  public void testGetFunctionJsDocInfo_ParentIsNameAndTypeIsNotVar() {
    // Test getFunctionJsDocInfo method, when n.getJSDocInfo is null
    // and parent.getType() is Token.NAME and parent.getJSDocInfo() is null
    // and grandparent.getType() is not Token.VAR and grandparent.getJSDocInfo() is not null
    // It should return the JSDocInfo object from grandparent
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);
    Node n = new Node();
    Node parent = new Node();
    Node grandparent = new Node();
    JSDocInfo jsDocInfo = new JSDocInfo();
    grandparent.setJSDocInfo(jsDocInfo);
    parent.setParent(grandparent);
    n.setParent(parent);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertEquals(jsDocInfo, result);
  }
}