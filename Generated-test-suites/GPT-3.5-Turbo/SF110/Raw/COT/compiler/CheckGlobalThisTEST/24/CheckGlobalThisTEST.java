
import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldReportThis() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    assertFalse(checkGlobalThis.shouldReportThis(new Node(Token.NAME, "test")));
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);
    assertNull(checkGlobalThis.getFunctionJsDocInfo(new Node(Token.NAME, "test")));
  }
}

