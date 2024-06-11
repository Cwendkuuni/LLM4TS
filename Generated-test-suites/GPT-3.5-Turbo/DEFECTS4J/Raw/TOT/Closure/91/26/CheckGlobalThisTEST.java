import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.diagnostic.DiagnosticType;
import com.google.javascript.rhino.Node;
import com.google.javascript.jscomp.NodeTraversal;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse_GlobalContext_ReturnsTrue() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.EMPTY);
    Node parent = new Node(Token.EMPTY);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertEquals(true, result);
  }
  
  @Test
  public void testShouldTraverse_FunctionWithConstructorAnnotation_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.recordConstructor();
    n.setJSDocInfo(jsDocInfo);
    Node parent = new Node(Token.EMPTY);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertEquals(false, result);
  }
  
  @Test
  public void testVisit_ThisKeywordAndParentIsGetProperty_ShouldReportError() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToFront(new Node(Token.EMPTY)); // Add dummy child to parent
    boolean errorReported = false;
    
    try {
      checkGlobalThis.visit(t, n, parent);
    } catch(Exception e) {
      errorReported = true;
    }
    
    assertEquals(true, errorReported);
  }
  
  @Test
  public void testVisit_ThisKeywordAndAssignLhsChildIsNull_ShouldNotReportError() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.EMPTY);
    boolean errorReported = false;
    
    try {
      checkGlobalThis.visit(t, n, parent);
    } catch(Exception e) {
      errorReported = true;
    }
    
    assertEquals(false, errorReported);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_FunctionWithJSDocInfo_ReturnsJSDocInfo() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = new JSDocInfo();
    n.setJSDocInfo(jsDocInfo);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertEquals(jsDocInfo, result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_FunctionWithParentNameAndJSDocInfo_ReturnsParentJSDocInfo() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    JSDocInfo jsDocInfo = new JSDocInfo();
    parent.setJSDocInfo(jsDocInfo);
    n.setParent(parent);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertEquals(jsDocInfo, result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_FunctionWithNameAndParentVar_ReturnsParentParentJSDocInfo() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    Node grandparent = new Node(Token.VAR);
    JSDocInfo jsDocInfo = new JSDocInfo();
    grandparent.setJSDocInfo(jsDocInfo);
    parent.setParent(grandparent);
    n.setParent(parent);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertEquals(jsDocInfo, result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_FunctionWithNoJSDocInfo_ReturnsNull() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertEquals(null, result);
  }
  
  @Test
  public void testShouldReportThis_AssignLhsChildNotNull_ReturnsTrue() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.EMPTY);
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    assertEquals(true, result);
  }
  
  @Test
  public void testShouldReportThis_AssignLhsChildNullAndParentIsGetProperty_ReturnsTrue() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToFront(new Node(Token.EMPTY)); // Add dummy child to parent
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    assertEquals(true, result);
  }
  
  @Test
  public void testShouldReportThis_AssignLhsChildNullAndParentIsNotGetProperty_ReturnsFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.EMPTY);
    boolean result = checkGlobalThis.shouldReportThis(n, parent);
    assertEquals(false, result);
  }
  
}