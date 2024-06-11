import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    
    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();
    
    boolean result = checker.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void testVisit() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);
    
    NodeTraversal t = new NodeTraversal();
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    
    checker.visit(t, n, parent);
    
    DiagnosticType expected = CheckGlobalThis.GLOBAL_THIS;
    DiagnosticType actual = compiler.getErrors().get(0).getType();
    
    assertEquals(expected, actual);
  }
  
  @Test
  public void testGetFunctionJsDocInfo() {
    CheckGlobalThis checker = new CheckGlobalThis(null, null);
    
    Node n = new Node(Token.FUNCTION);
    n.setJSDocInfo(new JSDocInfo());
    
    JSDocInfo expected = n.getJSDocInfo();
    JSDocInfo actual = checker.getFunctionJsDocInfo(n);
    
    assertEquals(expected, actual);
  }
}