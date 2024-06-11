import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node();
    Node parent = new Node();
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    assertFalse(result);
  }

  @Test
  public void testVisit() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    NodeTraversal t = new NodeTraversal(compiler, checkGlobalThis);
    Node n = new Node();
    Node parent = new Node();
    
    checkGlobalThis.visit(t, n, parent);
    // Add assertions here
  }

  @Test
  public void testShouldReportThis() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    Node n = new Node();
    boolean result = checkGlobalThis.shouldReportThis(n);
    assertFalse(result);
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    Node n = new Node();
    JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(n);
    assertNull(jsDocInfo);
  }

}