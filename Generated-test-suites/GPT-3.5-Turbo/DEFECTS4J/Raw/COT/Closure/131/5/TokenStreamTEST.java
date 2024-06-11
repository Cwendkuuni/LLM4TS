import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        // Test case for isKeyword() method
        // Test with valid keywords
        assertTrue(TokenStream.isKeyword("if"));
        assertTrue(TokenStream.isKeyword("for"));
        assertTrue(TokenStream.isKeyword("do"));

        // Test with invalid keywords
        assertFalse(TokenStream.isKeyword("random"));
        assertFalse(TokenStream.isKeyword("test"));
        assertFalse(TokenStream.isKeyword("hello"));
    }

    @Test
    public void testIsJSIdentifier() {
        // Test case for isJSIdentifier() method
        // Test with valid JavaScript identifiers
        assertTrue(TokenStream.isJSIdentifier("myVar"));
        assertTrue(TokenStream.isJSIdentifier("helloWorld"));
        assertTrue(TokenStream.isJSIdentifier("_test"));

        // Test with invalid JavaScript identifiers
        assertFalse(TokenStream.isJSIdentifier("123var"));
        assertFalse(TokenStream.isJSIdentifier("var test"));
        assertFalse(TokenStream.isJSIdentifier("my-var"));
    }
}