import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("if"));
        assertFalse(TokenStream.isKeyword("test"));
        assertTrue(TokenStream.isKeyword("If")); // Testing different casing
        assertFalse(TokenStream.isKeyword("incomplete")); // Testing partial match
    }

    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("validIdentifier"));
        assertFalse(TokenStream.isJSIdentifier("123InvalidIdentifier"));
        assertFalse(TokenStream.isJSIdentifier("")); // Testing empty string
    }
}