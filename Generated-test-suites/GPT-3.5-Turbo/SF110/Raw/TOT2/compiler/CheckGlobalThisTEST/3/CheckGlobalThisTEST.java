import static org.junit.Assert.*;
import org.junit.Test;

public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThisConstructor() {
    // Given
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);

    // When
    JSDocInfo info = checkGlobalThis.getFunctionJsDocInfo(new Node(Token.FUNCTION));

    // Then
    assertNull(info);
  }

  @Test
  public void testCheckGlobalThisAssignment() {
    // Given
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);

    // When
    Node node = new Node(Token.THIS);
    boolean shouldReport = checkGlobalThis.shouldReportThis(node);

    // Then
    assertTrue(shouldReport);
  }

  @Test
  public void testCheckGlobalThisShouldTraverse() {
    // Given
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);

    // When
    boolean shouldTraverse = checkGlobalThis.shouldTraverse(null, new Node(Token.BLOCK), new Node(Token.FUNCTION));

    // Then
    assertFalse(shouldTraverse);
  }

  @Test
  public void testCheckGlobalThisShouldNotReport() {
    // Given
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null);

    // When
    Node node = new Node(Token.ASSIGN, new Node(Token.THIS), new Node(Token.NAME));
    boolean shouldNotReport = checkGlobalThis.shouldReportThis(node);

    // Then
    assertFalse(shouldNotReport);
  }

}