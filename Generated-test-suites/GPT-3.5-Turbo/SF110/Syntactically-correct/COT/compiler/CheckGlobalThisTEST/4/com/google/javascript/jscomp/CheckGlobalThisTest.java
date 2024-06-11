import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldReportThis() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    boolean shouldReport = checkGlobalThis.shouldReportThis(null);
    assertFalse(shouldReport);
  }

  @Test
  public void testShouldTraverse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    boolean shouldTraverse = checkGlobalThis.shouldTraverse(null, null, null);
    assertFalse(shouldTraverse);
  }

  @Test
  public void testVisit() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    checkGlobalThis.visit(null, null, null);
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(null);
    assertNull(jsDocInfo);
  }

  @Test
  public void testConstructor() {
    AbstractCompiler compiler = new AbstractCompiler();
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(compiler);
    assertNotNull(checkGlobalThis);
  }
}