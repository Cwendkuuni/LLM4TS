import org.junit.Test;

public class TokenMgrErrorTest {

    @Test
    public void testGetMessage() {
        TokenMgrError error = new TokenMgrError("Test Message", 0);
        String message = error.getMessage();
        assertEquals("Test Message", message);
    }
    
    @Test
    public void testTokenMgrError() {
        TokenMgrError error = new TokenMgrError();
        String message = error.getMessage();
        assertNull(message);
    }
    
    @Test
    public void testTokenMgrErrorWithMessageAndReason() {
        TokenMgrError error = new TokenMgrError("Test Message", 1);
        String message = error.getMessage();
        assertEquals("Test Message", message);
        assertEquals(1, error.errorCode);
    }
    
    @Test
    public void testLexicalError() {
        String error = TokenMgrError.LexicalError(true, 0, 1, 2, "Test Error", 'a');
        assertEquals("Lexical error at line 1, column 2. Encountered: \"a\" (97), after : \"Test Error\"", error);
    }

}