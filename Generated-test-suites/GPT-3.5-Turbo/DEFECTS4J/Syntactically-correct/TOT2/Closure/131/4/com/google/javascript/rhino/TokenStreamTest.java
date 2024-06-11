
import static org.junit.Assert.*;
import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("if"));
        assertFalse(TokenStream.isKeyword("function"));
        assertTrue(TokenStream.isKeyword("byte"));
    }

    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("validID"));
        assertFalse(TokenStream.isJSIdentifier("123invalid"));
        assertTrue(TokenStream.isJSIdentifier("camelCase"));
    }
}
