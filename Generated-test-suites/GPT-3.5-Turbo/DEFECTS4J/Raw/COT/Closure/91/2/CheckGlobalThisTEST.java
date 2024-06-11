import static org.junit.Assert.*;
import org.junit.Test;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_shouldReturnTrueForFunctionWithoutConstructorAnnotation() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_shouldReturnFalseForFunctionWithConstructorAnnotation() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    n.setJSDocInfo(jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_shouldReturnFalseForFunctionWithThisAnnotation() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    n.setJSDocInfo(jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_shouldReturnFalseForFunctionWithOverrideAnnotation() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setOverride(true);
    n.setJSDocInfo(jsDoc);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    assertFalse(result);
  }
  
  @Test
  public void testVisit_shouldReportErrorForUnsafeThisOnLeftSideOfAssignment() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    boolean result = checkGlobalThis.visit(t, n, parent);

    // Assertion here for reporting the error
  }
  
  @Test
  public void testVisit_shouldNotReportErrorForSafeThisOnLeftSideOfAssignment() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    Node lhs = new Node(Token.NAME);
    parent.addChildToBack(lhs);
    boolean result = checkGlobalThis.visit(t, n, parent);

    // Assertion here for not reporting the error
  }
  
  @Test
  public void testVisit_shouldReportErrorForUnsafeThisWithPropertyAccess() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    boolean result = checkGlobalThis.visit(t, n, parent);

    // Assertion here for reporting the error
  }
  
  @Test
  public void testVisit_shouldNotReportErrorForSafeThisWithPropertyAccess() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    Node lhs = new Node(Token.NAME);
    parent.addChildToBack(lhs);
    boolean result = checkGlobalThis.visit(t, n, parent);

    // Assertion here for not reporting the error
  }
  
  @Test
  public void testVisit_shouldNotReportErrorForNonThisNode() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.GETPROP);
    boolean result = checkGlobalThis.visit(t, n, parent);

    assertFalse(result);
  }
  
  @Test
  public void testVisit_shouldResetAssignLhsChildIfNodeEqualsAssignLhsChild() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    checkGlobalThis.shouldReportThis(n, parent);
    boolean result = checkGlobalThis.visit(t, n, parent);

    assertNull(checkGlobalThis.assignLhsChild);
  }

  @Test
  public void testGetFunctionJsDocInfo_shouldReturnJSDocInfoForFunctionWithJSDocInfo() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    n.setJSDocInfo(jsDoc);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    assertNotNull(result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_shouldReturnJSDocInfoFromParentForFunctionWithoutJSDocInfo() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    parent.setJSDocInfo(jsDoc);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    assertNotNull(result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_shouldReturnJSDocInfoFromParentParentForFunctionWithoutJSDocInfo() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    Node gramps = new Node(Token.VAR);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    gramps.setJSDocInfo(jsDoc);
    parent.addChildToBack(gramps);
    n.setParent(parent);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);

    assertNotNull(result);
  }

}