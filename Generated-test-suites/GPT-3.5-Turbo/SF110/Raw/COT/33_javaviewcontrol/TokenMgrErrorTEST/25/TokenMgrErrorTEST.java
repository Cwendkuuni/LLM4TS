
import org.junit.Test;

public class TokenMgrErrorTest {

    @Test
    public void testAddEscapes() {
        String str = "Hello, World!";
        String expectedResult = "Hello, World!";
        String actualResult = TokenMgrError.addEscapes(str);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testAddEscapes_NullString() {
        String str = null;
        String expectedResult = null;
        String actualResult = TokenMgrError.addEscapes(str);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testAddEscapes_EmptyString() {
        String str = "";
        String expectedResult = "";
        String actualResult = TokenMgrError.addEscapes(str);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testLexicalError() {
        boolean EOFSeen = false;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "";
        char curChar = 'a';
        String expectedResult = "Lexical error at line 1, column 1.  Encountered: \"a\" (97), after : \"\"";
        String actualResult = TokenMgrError.LexicalError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testTokenMgrError_DefaultConstructor() {
        TokenMgrError tokenMgrError = new TokenMgrError();
        assertNull(tokenMgrError.getMessage());
    }

    @Test
    public void testTokenMgrError_MessageAndReasonConstructor() {
        String message = "Error";
        int reason = 0;
        TokenMgrError tokenMgrError = new TokenMgrError(message, reason);
        assertEquals(message, tokenMgrError.getMessage());
        assertEquals(reason, tokenMgrError.errorCode);
    }

    @Test
    public void testTokenMgrError_FullConstructor() {
        boolean EOFSeen = false;
        int lexState = 0;
        int errorLine = 1;
        int errorColumn = 1;
        String errorAfter = "";
        char curChar = 'a';
        int reason = 0;
        TokenMgrError tokenMgrError = new TokenMgrError(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar, reason);
        String expectedMessage = "Lexical error at line 1, column 1.  Encountered: \"a\" (97), after : \"\"";
        assertEquals(expectedMessage, tokenMgrError.getMessage());
        assertEquals(reason, tokenMgrError.errorCode);
    }

}
