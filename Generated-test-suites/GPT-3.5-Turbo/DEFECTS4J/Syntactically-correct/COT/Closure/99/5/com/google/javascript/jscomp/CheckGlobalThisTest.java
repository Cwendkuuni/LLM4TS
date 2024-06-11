import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {
  
  @Test
  public void testShouldTraverse_shouldReturnTrueWhenFunctionNotConstructorOrHasThisOrOverrideAnnotation() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_shouldReturnFalseWhenFunctionIsConstructor() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setConstructor(true);
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.BLOCK);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_shouldReturnFalseWhenFunctionHasThisAnnotation() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.BLOCK);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_shouldReturnFalseWhenFunctionHasOverrideAnnotation() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.setOverride(true);
    n.setJSDocInfo(jsDoc);
    Node parent = new Node(Token.BLOCK);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_shouldReturnFalseWhenFunctionParentNotBlockScriptNameOrAssign() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.LABEL);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_shouldReturnFalseWhenLeftSideOfAssignmentIsPrototypeProperty() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.NAME, new Node(Token.GETPROP, new Node(Token.STRING, "prototype")));
    Node parent = new Node(Token.ASSIGN);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_shouldReturnFalseWhenLeftSideOfAssignmentIsPrototypeSubproperty() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.NAME, new Node(Token.GETPROP, new Node(Token.NAME, "object")));
    Node parent = new Node(Token.ASSIGN);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_shouldReturnTrueWhenLeftSideOfAssignmentNotPrototypePropertyOrSubproperty() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.NAME, new Node(Token.NAME, "object"));
    Node parent = new Node(Token.ASSIGN);
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertTrue(result);
  }
  
  @Test
  public void testVisit_shouldReportErrorWhenThisIsUsedOnLeftSideOfAssignment() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    checkGlobalThis.visit(t, n, parent);
    assertEquals(1, compiler.getErrorCount());
  }
  
  @Test
  public void testVisit_shouldNotReportErrorWhenThisIsUsedOnRightSideOfAssignment() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.EXPR_RESULT);
    parent.addChildToFront(new Node(Token.ASSIGN));
    checkGlobalThis.visit(t, n, parent);
    assertEquals(0, compiler.getErrorCount());
  }
  
  @Test
  public void testVisit_shouldNotReportErrorWhenThisIsUsedInPropertyAccess() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    parent.addChildToFront(n);
    checkGlobalThis.visit(t, n, parent);
    assertEquals(0, compiler.getErrorCount());
  }
  
  @Test
  public void testVisit_shouldNotReportErrorWhenThisIsNotUsedInInvalidContext() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.SCRIPT);
    checkGlobalThis.visit(t, n, parent);
    assertEquals(0, compiler.getErrorCount());
  }
  
  @Test
  public void testGetFunctionJsDocInfo_shouldReturnJsDocWhenFunctionHasJsDoc() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    n.setJSDocInfo(jsDoc);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertEquals(jsDoc, result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_shouldReturnJsDocOfVariableWhenFunctionParentIsNameNode() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    n.getParent().setJSDocInfo(jsDoc);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertEquals(jsDoc, result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_shouldReturnJsDocOfVariableWhenFunctionParentIsAssignNode() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo jsDoc = new JSDocInfo();
    n.getGrandparent().setJSDocInfo(jsDoc);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertEquals(jsDoc, result);
  }
  
  @Test
  public void testGetFunctionJsDocInfo_shouldReturnNullWhenFunctionHasNoJsDoc() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    Node n = new Node(Token.FUNCTION);
    JSDocInfo result = checkGlobalThis.getFunctionJsDocInfo(n);
    assertNull(result);
  }
  
  @Test
  public void testShouldReportThis_whenAssignLhsChildNotNull_ShouldReturnTrue() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    checkGlobalThis.shouldReportThis(n, parent);
    assertTrue(result);
  }
  
  @Test
  public void testShouldReportThis_whenThisIsPropertyAccess_ShouldReturnTrue() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    checkGlobalThis.shouldReportThis(n, parent);
    assertTrue(result);
  }
  
  @Test
  public void testShouldReportThis_whenThisIsNotOnLeftSideOfAssignmentOrPropertyAccess_ShouldReturnFalse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler(), CheckLevel.WARNING);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.EXPR_RESULT);
    checkGlobalThis.shouldReportThis(n, parent);
    assertFalse(result);
  }
}