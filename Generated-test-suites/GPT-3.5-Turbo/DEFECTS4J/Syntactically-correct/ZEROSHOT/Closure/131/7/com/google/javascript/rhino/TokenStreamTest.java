import org.junit.Assert;
import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        // Test cases for valid keywords
        Assert.assertTrue(TokenStream.isKeyword("if"));
        Assert.assertTrue(TokenStream.isKeyword("for"));
        Assert.assertTrue(TokenStream.isKeyword("while"));
        Assert.assertTrue(TokenStream.isKeyword("null"));
        Assert.assertTrue(TokenStream.isKeyword("class"));
        Assert.assertTrue(TokenStream.isKeyword("break"));
        Assert.assertTrue(TokenStream.isKeyword("false"));
        // Test cases for invalid keywords
        Assert.assertFalse(TokenStream.isKeyword("abc"));
        Assert.assertFalse(TokenStream.isKeyword("123"));
        Assert.assertFalse(TokenStream.isKeyword("if1"));
        Assert.assertFalse(TokenStream.isKeyword("for_"));
        Assert.assertFalse(TokenStream.isKeyword("while1"));
        Assert.assertFalse(TokenStream.isKeyword("null123"));
        Assert.assertFalse(TokenStream.isKeyword("class_"));
        Assert.assertFalse(TokenStream.isKeyword("break123"));
        Assert.assertFalse(TokenStream.isKeyword("false_"));
    }

    @Test
    public void testIsJSIdentifier() {
        // Test cases for valid JavaScript identifiers
        Assert.assertTrue(TokenStream.isJSIdentifier("abc"));
        Assert.assertTrue(TokenStream.isJSIdentifier("abc123"));
        Assert.assertTrue(TokenStream.isJSIdentifier("_abc"));
        Assert.assertTrue(TokenStream.isJSIdentifier("$abc"));
        // Test cases for invalid JavaScript identifiers
        Assert.assertFalse(TokenStream.isJSIdentifier("1abc"));
        Assert.assertFalse(TokenStream.isJSIdentifier("abc-"));
        Assert.assertFalse(TokenStream.isJSIdentifier("abc+"));
        Assert.assertFalse(TokenStream.isJSIdentifier("abc."));
        Assert.assertFalse(TokenStream.isJSIdentifier("abc@"));
        Assert.assertFalse(TokenStream.isJSIdentifier("abc#"));
        Assert.assertFalse(TokenStream.isJSIdentifier("abc%"));
        Assert.assertFalse(TokenStream.isJSIdentifier("abc&"));
        Assert.assertFalse(TokenStream.isJSIdentifier("abc*"));
    }
}