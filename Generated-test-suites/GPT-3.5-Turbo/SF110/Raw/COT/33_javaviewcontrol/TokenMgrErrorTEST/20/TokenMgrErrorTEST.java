
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenMgrErrorTest {
    
    @Test
    public void testGetMessage(){
        TokenMgrError error = new TokenMgrError("Error message", 0);
        assertEquals("Error message", error.getMessage());
    }
    
    @Test
    public void testTokenMgrErrorNoArgs(){
        TokenMgrError error = new TokenMgrError();
        assertNull(error.getMessage());
    }
    
    @Test
    public void testTokenMgrErrorWithArgs(){
        TokenMgrError error = new TokenMgrError("Error message", 1);
        assertEquals("Error message", error.getMessage());
        assertEquals(1, error.errorCode);
    }
    
    @Test
    public void testTokenMgrErrorWithLexicalError(){
        TokenMgrError error = new TokenMgrError(true, 0, 1, 2, "Error", 'a', 3);
        assertEquals("Lexical error at line 1, column 2.  Encountered: \"a\" (97), after : \"Error\"", error.getMessage());
        assertEquals(3, error.errorCode);
    }
    
    @Test
    public void testAddEscapes(){
        String input = "This is a test";
        String expectedOutput = "This is a test";
        assertEquals(expectedOutput, TokenMgrError.addEscapes(input));
        
        input = "This is a \t test";
        expectedOutput = "This is a \\t test";
        assertEquals(expectedOutput, TokenMgrError.addEscapes(input));
        
        // Add test cases for other escape characters
    }
    
}
