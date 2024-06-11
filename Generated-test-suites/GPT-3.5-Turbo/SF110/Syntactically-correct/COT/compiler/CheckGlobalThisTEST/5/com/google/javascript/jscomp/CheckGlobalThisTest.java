
import org.junit.Test;
import org.junit.Assert;

public class CheckGlobalThisTest {

  @Test
  public void testShouldReportThis() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    boolean result = checkGlobalThis.shouldReportThis(n);
    Assert.assertTrue(result);
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    JSDocInfo jsDoc = checkGlobalThis.getFunctionJsDocInfo(n);
    Assert.assertNull(jsDoc);
  }

  @Test
  public void testShouldTraverse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    Assert.assertTrue(result);
  }

  @Test
  public void testVisit() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis();
    checkGlobalThis.visit(t, n, parent);
    Assert.assertEquals(null, checkGlobalThis.assignLhsChild);
  }

}
