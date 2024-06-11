import org.junit.Test;
import static org.junit.Assert.*;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("for"));
        assertFalse(TokenStream.isKeyword("test"));
    }

    @Test
    public void testIsJSIdentifier() {
        assertTrue(TokenStream.isJSIdentifier("validIdentifier"));
        assertFalse(TokenStream.isJSIdentifier("123NotIdentifier"));
    }

    @Test
    public void testIsKeyword_edgeCase() {
        assertFalse(TokenStream.isKeyword("test"));
    }

    @Test
    public void testIsKeyword_nullInput() {
        assertFalse(TokenStream.isKeyword(null));
    }

    @Test
    public void testIsJSIdentifier_edgeCase() {
        assertFalse(TokenStream.isJSIdentifier("123NotIdentifier"));
    }

    @Test
    public void testIsJSIdentifier_emptyInput() {
        assertFalse(TokenStream.isJSIdentifier(""));
    }
}