import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        // Typical case
        String str1 = "This is a string";
        String result1 = TokenMgrError.addEscapes(str1);
        assertEquals("This is a string", result1);
        
        // Edge case: empty string
        String str2 = "";
        String result2 = TokenMgrError.addEscapes(str2);
        assertEquals("", result2);
        
        // Edge case: string with escape characters
        String str3 = "This is a string\nwith new line";
        String result3 = TokenMgrError.addEscapes(str3);
        assertEquals("This is a string\\nwith new line", result3);
    }

    @Test
    public void testLexicalError() {
        // Typical case
        boolean EOFSeen1 = false;
        int lexState1 = 0;
        int errorLine1 = 1;
        int errorColumn1 = 5;
        String errorAfter1 = "is";
        char curChar1 = ' ';
        String result1 = TokenMgrError.LexicalError(EOFSeen1, lexState1, errorLine1, errorColumn1, errorAfter1, curChar1);
        String expected1 = "Lexical error at line 1, column 5.  Encountered: \" \" (32), after : \"is\"";
        assertEquals(expected1, result1);
        
        // Edge case: EOFSeen is true
        boolean EOFSeen2 = true;
        int lexState2 = 0;
        int errorLine2 = 2;
        int errorColumn2 = 10;
        String errorAfter2 = "error";
        char curChar2 = '\0';
        String result2 = TokenMgrError.LexicalError(EOFSeen2, lexState2, errorLine2, errorColumn2, errorAfter2, curChar2);
        String expected2 = "Lexical error at line 2, column 10.  Encountered: <EOF> , after : \"error\"";
        assertEquals(expected2, result2);
    }

    @Test
    public void testGetMessage() {
        // Typical case
        TokenMgrError error1 = new TokenMgrError("Error message", 1);
        String result1 = error1.getMessage();
        assertEquals("Error message", result1);
        
        // Edge case: Empty message
        TokenMgrError error2 = new TokenMgrError("", 2);
        String result2 = error2.getMessage();
        assertEquals("", result2);
    }

    @Test
    public void testTokenMgrErrorConstructor() {
        // Typical case
        TokenMgrError error1 = new TokenMgrError("Error message", 1);
        String message1 = error1.getMessage();
        int reason1 = error1.errorCode;
        assertEquals("Error message", message1);
        assertEquals(1, reason1);
        
        // Edge case: Empty message and 0 reason
        TokenMgrError error2 = new TokenMgrError("", 0);
        String message2 = error2.getMessage();
        int reason2 = error2.errorCode;
        assertEquals("", message2);
        assertEquals(0, reason2);
    }

    @Test
    public void testTokenMgrErrorConstructorWithParams() {
        // Typical case
        boolean EOFSeen1 = false;
        int lexState1 = 0;
        int errorLine1 = 1;
        int errorColumn1 = 5;
        String errorAfter1 = "is";
        char curChar1 = ' ';
        int reason1 = 2;
        TokenMgrError error1 = new TokenMgrError(EOFSeen1, lexState1, errorLine1, errorColumn1, errorAfter1, curChar1, reason1);
        String message1 = error1.getMessage();
        int reason1 = error1.errorCode;
        String expected1 = "Lexical error at line 1, column 5.  Encountered: \" \" (32), after : \"is\"";
        assertEquals(expected1, message1);
        assertEquals(2, reason1);
        
        // Edge case: EOFSeen is true and 0 reason
        boolean EOFSeen2 = true;
        int lexState2 = 0;
        int errorLine2 = 5;
        int errorColumn2 = 10;
        String errorAfter2 = "error";
        char curChar2 = '\0';
        int reason2 = 0;
        TokenMgrError error2 = new TokenMgrError(EOFSeen2, lexState2, errorLine2, errorColumn2, errorAfter2, curChar2, reason2);
        String message2 = error2.getMessage();
        int reason2 = error2.errorCode;
        String expected2 = "Lexical error at line 5, column 10.  Encountered: <EOF> , after : \"error\"";
        assertEquals(expected2, message2);
        assertEquals(0, reason2);
    }
}