import org.junit.Assert;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldTraverse() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();

    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);

    Assert.assertEquals(true, result);
  }

  @Test
  public void testVisit() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckLevel level = CheckLevel.WARNING;
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler, level);

    NodeTraversal t = new NodeTraversal();
    Node n = new Node();
    Node parent = new Node();

    checkGlobalThis.visit(t, n, parent);

    // Verify that no exceptions are thrown
    Assert.assertTrue(true);
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, null);

    Node n = new Node();
    JSDocInfo jsDoc = checkGlobalThis.getFunctionJsDocInfo(n);

    Assert.assertNull(jsDoc);
  }

}