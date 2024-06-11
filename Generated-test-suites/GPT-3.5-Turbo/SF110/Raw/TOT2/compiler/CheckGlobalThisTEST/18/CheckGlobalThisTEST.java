import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testShouldReportThis() {
    // Given
    Node node = new Node(Token.THIS);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());

    // When
    boolean result = checkGlobalThis.shouldReportThis(node);

    // Then
    assertEquals(true, result);
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    // Given
    Node node = new Node(Token.FUNCTION);
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());

    // When
    JSDocInfo jsDocInfo = checkGlobalThis.getFunctionJsDocInfo(node);

    // Then
    assertEquals(null, jsDocInfo);
  }

}