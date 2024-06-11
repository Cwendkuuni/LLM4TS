import static org.junit.Assert.*;
import org.junit.Test;

public class TokenStreamTest {
    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("for"));
        assertTrue(TokenStream.isKeyword("null"));
        assertFalse(TokenStream.isKeyword("abc"));
        assertFalse(TokenStream.isKeyword("123"));
        assertFalse(TokenStream.isKeyword("_abc"));
    }
}