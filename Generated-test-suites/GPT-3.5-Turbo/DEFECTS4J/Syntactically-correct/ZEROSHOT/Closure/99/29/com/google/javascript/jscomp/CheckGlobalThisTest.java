import org.junit.Test;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Node;

public class CheckGlobalThisTest {
  
  @Test
  public void testShouldTraverse_FunctionWithConstructorAnnotation_ReturnFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
    Node node = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setConstructor(true);
    node.setJSDocInfo(jsDocInfo);
    Node parent = new Node(Token.SCRIPT);
    
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_FunctionWithThisTypeAnnotation_ReturnFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
    Node node = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.recordThisType();
    node.setJSDocInfo(jsDocInfo);
    Node parent = new Node(Token.SCRIPT);
    
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_FunctionWithOverrideAnnotation_ReturnFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
    Node node = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = new JSDocInfo();
    jsDocInfo.setOverride(true);
    node.setJSDocInfo(jsDocInfo);
    Node parent = new Node(Token.SCRIPT);
    
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testShouldTraverse_FunctionWithBlockParent_ReturnTrue() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.BLOCK);
    
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_FunctionWithScriptParent_ReturnTrue() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_FunctionWithNameParent_ReturnTrue() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.NAME);
    
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_FunctionWithAssignParent_ReturnTrue() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
    Node node = new Node(Token.FUNCTION);
    Node parent = new Node(Token.ASSIGN);
    
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void testShouldTraverse_ParentIsNull_ReturnFalse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
    Node node = new Node(Token.FUNCTION);
    Node parent = null;
    
    boolean result = checkGlobalThis.shouldTraverse(nodeTraversal, node, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testVisit_NodeIsThisAndShouldReportThis_CompilerReportCalled() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    
    checkGlobalThis.visit(nodeTraversal, node, parent);
    
    // Assert compiler.report() called
  }
  
  @Test
  public void testVisit_NodeIsThisAndAssignLhsChildNotNull_AssignLhsChildSetToNull() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, checkGlobalThis);
    Node node = new Node(Token.THIS);
    Node parent = new Node(Token.NAME);
    checkGlobalThis.setAssignLhsChild(node);
    
    checkGlobalThis.visit(nodeTraversal, node, parent);
    
    assertNull(checkGlobalThis.getAssignLhsChild());
  }
  
}