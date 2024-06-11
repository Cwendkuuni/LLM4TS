
import org.junit.Assert;
import org.junit.Test;

public class TokenStreamTest {

    @Test
    public void testIsKeyword() {
        String keyword = "if";
        boolean result = TokenStream.isKeyword(keyword);
        Assert.assertTrue(result);

        keyword = "for";
        result = TokenStream.isKeyword(keyword);
        Assert.assertTrue(result);

        keyword = "else";
        result = TokenStream.isKeyword(keyword);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsJSIdentifier() {
        String identifier = "variable";
        boolean result = TokenStream.isJSIdentifier(identifier);
        Assert.assertTrue(result);

        identifier = "123abc";
        result = TokenStream.isJSIdentifier(identifier);
        Assert.assertFalse(result);

        identifier = "function";
        result = TokenStream.isJSIdentifier(identifier);
        Assert.assertTrue(result);
    }
}
