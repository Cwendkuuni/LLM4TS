import static org.junit.Assert.*;
import org.junit.Test;
import com.pmdesigns.jvc.tools.TokenMgrError;

public class TokenMgrErrorTest {
  
  @Test
  public void testAddEscapes() {
    String input = "Test String";
    String expectedOutput = "Test String";
    assertEquals(expectedOutput, TokenMgrError.addEscapes(input));
  }
  
  @Test
  public void testLexicalError() {
    boolean EOFSeen = true;
    int lexState = 0;
    int errorLine = 1;
    int errorColumn = 1;
    String errorAfter = "";
    char curChar = 'a';
    
    String expectedOutput = "Lexical error at line 1, column 1.  Encountered: <EOF>  after : \"\"";
    assertEquals(expectedOutput, TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar));
  }
  
  @Test
  public void testGetMessage() {
    TokenMgrError error = new TokenMgrError("Test message", 0);
    String expectedOutput = "Test message";
    assertEquals(expectedOutput, error.getMessage());
  }
  
  @Test
  public void testTokenMgrErrorWithMessage() {
    TokenMgrError error = new TokenMgrError("Test message", 0);
    String expectedOutput = "Test message";
    assertEquals(expectedOutput, error.getMessage());
    assertEquals(0, error.errorCode);
  }
  
  @Test
  public void testTokenMgrErrorWithParams() {
    boolean EOFSeen = true;
    int lexState = 0;
    int errorLine = 1;
    int errorColumn = 1;
    String errorAfter = "";
    char curChar = 'a';
    int reason = 0;
    
    TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
    
    String expectedOutput = "Lexical error at line 1, column 1.  Encountered: <EOF>  after : \"\"";
    assertEquals(expectedOutput, error.getMessage());
    assertEquals(0, error.errorCode);
  }
}