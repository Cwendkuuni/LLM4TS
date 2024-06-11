import com.google.javascript.rhino.TokenStream;
import org.junit.Assert;
import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword_True() {
        Assert.assertTrue(TokenStream.isKeyword("if"));
    }

    @Test
    public void testIsKeyword_False() {
        Assert.assertFalse(TokenStream.isKeyword("abcdef"));
    }

    @Test
    public void testIsJSIdentifier_True() {
        Assert.assertTrue(TokenStream.isJSIdentifier("test_123"));
    }

    @Test
    public void testIsJSIdentifier_False() {
        Assert.assertFalse(TokenStream.isJSIdentifier("!notValid"));
    }
}