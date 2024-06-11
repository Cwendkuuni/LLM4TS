import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

   private TokenMgrError tokenMgrError;
   
   @Before
   public void setUp() {
      tokenMgrError = new TokenMgrError();
   }

   @Test
   public void testGetMessage() {
      String expectedMessage = null;
      String actualMessage = tokenMgrError.getMessage();
      assertEquals(expectedMessage, actualMessage);
   }
   
   @Test
   public void testTokenMgrError() {
      TokenMgrError error = new TokenMgrError();
      assertNotNull(error);
   }
   
   @Test
   public void testTokenMgrErrorWithMessageAndReason() {
      String expectedMessage = "Test Message";
      int expectedReason = 2;
      TokenMgrError error = new TokenMgrError(expectedMessage, expectedReason);
      assertEquals(expectedMessage, error.getMessage());
      assertEquals(expectedReason, error.errorCode);
   }
   
   @Test
   public void testTokenMgrErrorWithAllParameters() {
      boolean EOFSeen = true;
      int lexState = 1;
      int errorLine = 10;
      int errorColumn = 5;
      String errorAfter = "error";
      char curChar = 'a';
      int reason = 3;
      
      String expectedErrorMessage = "Lexical error at line 10, column 5.  Encountered: <EOF>  after : \"error\"";
      
      TokenMgrError error = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
      assertEquals(expectedErrorMessage, error.getMessage());
      assertEquals(reason, error.errorCode);
   }
   
   @Test
   public void testAddEscapesWithSpecialCharacters() {
      String inputString = "\b\t\n\f\r\"\'\\";
      String expectedString = "\\b\\t\\n\\f\\r\\\"\\\'\\\\";
      String actualString = TokenMgrError.addEscapes(inputString);
      assertEquals(expectedString, actualString);
   }
   
   @Test
   public void testAddEscapesWithNonSpecialCharacters() {
      String inputString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()-_=+[{]}|;:,.<>/?";
      String expectedString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()-_=+[{]}|;:,.<>/?";
      String actualString = TokenMgrError.addEscapes(inputString);
      assertEquals(expectedString, actualString);
   }

}