import org.junit.Assert;
import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        // Basic test cases
        boolean result = TokenStream.isKeyword("if");
        Assert.assertTrue(result);

        result = TokenStream.isKeyword("for");
        Assert.assertTrue(result);

        result = TokenStream.isKeyword("while");
        Assert.assertTrue(result);

        // Edge cases
        result = TokenStream.isKeyword("");
        Assert.assertFalse(result);
        // Add more edge cases test cases

        // Exception handling
        // Add exception handling test cases

    }

    @Test
    public void testIsJSIdentifier() {
        // Basic test cases
        boolean result = TokenStream.isJSIdentifier("test");
        Assert.assertTrue(result);

        result = TokenStream.isJSIdentifier("_test");
        Assert.assertTrue(result);

        result = TokenStream.isJSIdentifier("$test");
        Assert.assertTrue(result);

        // Edge cases
        result = TokenStream.isJSIdentifier("");
        Assert.assertFalse(result);

        result = TokenStream.isJSIdentifier(" test ");
        Assert.assertFalse(result);

        // Add more edge cases test cases

        // Exception handling
        // Add exception handling test cases

    }
}