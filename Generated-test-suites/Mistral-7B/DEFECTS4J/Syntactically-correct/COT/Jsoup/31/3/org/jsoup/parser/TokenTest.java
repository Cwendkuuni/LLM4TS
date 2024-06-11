import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @Test
    public void testDoctype_Basic() {
        Token doctype = new Token.Doctype();
        Assert.assertTrue(doctype.isDoctype());
    }

    @Test
    public void testDoctype_getName_Basic() {
        Token doctype = new Token.Doctype();
        String name = doctype.asDoctype().getName();
        Assert.assertEquals(Token.Doctype.class.getSimpleName(), name);
    }

    @Test
    public void testDoctype_getPublicIdentifier_Basic() {
        Token doctype = new Token.Doctype();
        String publicIdentifier = doctype.asDoctype().getPublicIdentifier();
        Assert.assertNull(publicIdentifier);
    }

    @Test(expected = IllegalStateException.class)
    public void testDoctype_getSystemIdentifier_NoValue() {
        Token doctype = new Token.Doctype();
        String systemIdentifier = doctype.asDoctype().getSystemIdentifier();
        Assert.fail("Expected IllegalStateException but got: " + systemIdentifier);
    }

    @Test
    public void testDoctype_getSystemIdentifier_WithValue() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().setSystemIdentifier("http://example.com");
        String systemIdentifier = doctype.asDoctype().getSystemIdentifier();
        Assert.assertEquals("http://example.com", systemIdentifier);
    }

    @Test
    public void testDoctype_isForceQuirks_Basic() {
        Token doctype = new Token.Doctype();
        Assert.assertFalse(doctype.asDoctype().isForceQuirks());
    }

    @Test
    public void testDoctype_isForceQuirks_SetTrue() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().setForceQuirks(true);
        Assert.assertTrue(doctype.asDoctype().isForceQuirks());
    }

    // Repeat the above test cases for other methods in the class, e.g. for StartTag, EndTag, Comment, Character, EOF

}