import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.rhino.Node;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

  @Test
  public void testShouldReportThisWhenLeftSideOfAssign() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    boolean shouldReportThis = checkGlobalThis.shouldReportThis(n, parent);
    
    assertTrue(shouldReportThis);
  }

  @Test
  public void testShouldReportThisWhenPropertyAccess() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    boolean shouldReportThis = checkGlobalThis.shouldReportThis(n, parent);
    
    assertTrue(shouldReportThis);
  }

  @Test
  public void testShouldNotReportThisWhenRightSideOfAssign() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.NUMBER);
    Node parent = new Node(Token.ASSIGN);
    boolean shouldReportThis = checkGlobalThis.shouldReportThis(n, parent);
    
    assertFalse(shouldReportThis);
  }

  @Test
  public void testShouldNotReportThisWhenNotPropertyAccessOrAssignLhs() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.NAME);
    boolean shouldReportThis = checkGlobalThis.shouldReportThis(n, parent);
    
    assertFalse(shouldReportThis);
  }
  
  @Test
  public void testVisitShouldReportGlobalThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    
    checkGlobalThis.visit(t, n, parent);
    Diagnostic warning = t.reportWarning(GLOBAL_THIS);
    
    assertNotNull(warning);
  }
  
  @Test
  public void testVisitShouldNotReportGlobalThis() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.NAME);
    Node parent = new Node(Token.NAME);
    
    checkGlobalThis.visit(t, n, parent);
    Diagnostic warning = t.reportWarning(GLOBAL_THIS);
    
    assertNull(warning);
  }
  
  @Test
  public void testGetFunctionJsDocInfoWithoutJsDoc() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    
    JSDocInfo jsDoc = checkGlobalThis.getFunctionJsDocInfo(n);
    
    assertNull(jsDoc);
  }
  
  @Test
  public void testGetFunctionJsDocInfoWithFunctionJsDoc() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    
    JSDocInfo jsDoc = new JSDocInfo();
    n.setJSDocInfo(jsDoc);
    
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    
    assertEquals(jsDoc, result);
  }
  
  @Test
  public void testGetFunctionJsDocInfoWithParentAssignJsDoc() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.FUNCTION);
    Node name = new Node(Token.NAME);
    Node parent = new Node(Token.ASSIGN, name, n);
    
    JSDocInfo jsDoc = new JSDocInfo();
    name.setJSDocInfo(jsDoc);
    
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    
    assertEquals(jsDoc, result);
  }
  
  @Test
  public void testGetFunctionJsDocInfoWithVarAssignJsDoc() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node(Token.FUNCTION);
    Node name = new Node(Token.NAME);
    Node assign = new Node(Token.ASSIGN, name, n);
    Node var = new Node(Token.VAR, assign);
    
    JSDocInfo jsDoc = new JSDocInfo();
    var.setJSDocInfo(jsDoc);
    
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    
    assertEquals(jsDoc, result);
  }

}