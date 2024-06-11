import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        // Test cases for isKeyword method
        assert(TokenStream.isKeyword("if"));
        assert(!TokenStream.isKeyword("xyz"));
        assert(TokenStream.isKeyword("break"));
    }

    @Test
    public void testIsJSIdentifier() {
        // Test cases for isJSIdentifier method
        assert(TokenStream.isJSIdentifier("abc"));
        assert(TokenStream.isJSIdentifier("ABCD"));
        assert(!TokenStream.isJSIdentifier("1234"));
    }
}