import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @Test
    public void testDoctype_simple() {
        Token doctype = new Token.Doctype();
        Assert.assertEquals(TokenType.Doctype, doctype.tokenType());
    }

    @Test
    public void testDoctype_getName() {
        Token doctype = new Token.Doctype();
        doctype.name("html");
        Assert.assertEquals("html", doctype.asDoctype().getName());
    }

    @Test
    public void testDoctype_getPublicIdentifier() {
        Token doctype = new Token.Doctype();
        doctype.publicIdentifier("_-_");
        Assert.assertEquals("-_-_", doctype.asDoctype().getPublicIdentifier());
    }

    @Test
    public void testDoctype_getSystemIdentifier() {
        Token doctype = new Token.Doctype();
        doctype.systemIdentifier("about:blank");
        Assert.assertEquals("about:blank", doctype.asDoctype().getSystemIdentifier());
    }

    @Test
    public void testDoctype_isForceQuirks() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().isForceQuirks(); // default is false
        doctype.asDoctype().setForceQuirks(true);
        Assert.assertFalse(doctype.asDoctype().isForceQuirks());
        doctype.asDoctype().setForceQuirks(true);
        Assert.assertTrue(doctype.asDoctype().isForceQuirks());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoctype_invalidName() {
        Token doctype = new Token.Doctype();
        doctype.name(null);
    }

    // ... Add similar test methods for other methods in other classes

    @Test
    public void testStartTag_simple() {
        Token startTag = new Token.StartTag();
        Assert.assertEquals(TokenType.StartTag, startTag.tokenType());
    }

    @Test
    public void testStartTag_name() {
        Token startTag = new Token.StartTag();
        startTag.name("html");
        Assert.assertEquals("html", startTag.asStartTag().name());
    }

    @Test
    public void testStartTag_selfClosing() {
        Token startTag = new Token.StartTag();
        startTag.asStartTag().setSelfClosing(true);
        Assert.assertTrue(startTag.asStartTag().isSelfClosing());
    }

    @Test
    public void testStartTag_attributes() {
        Token startTag = new Token.StartTag();
        startTag.newAttribute();
        startTag.appendAttributeName("id");
        startTag.appendAttributeValue("123");
        Assert.assertNotNull(startTag.asStartTag().getAttributes());
        Assert.assertEquals("123", startTag.asStartTag().getAttributes().value("id"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartTag_invalidName() {
        Token startTag = new Token.StartTag();
        startTag.name(null);
    }

    // ... Add similar test methods for other methods in other classes

    // ... Repeat the test pattern for EndTag, Comment, Character, and EOF classes

    // Testing edge cases and exceptions

    @Test(expected = IllegalStateException.class)
    public void testToken_invalidState() {
        Token token = new Token(); // invalid state
        token.tokenType(); // should throw an exception
    }

    @Test
    public void testToken_nullName() {
        Token startTag = new Token.StartTag();
        Assert.assertThrows(IllegalArgumentException.class, () -> startTag.name(null));
    }

    @Test
    public void testToken_emptyName() {
        Token startTag = new Token.StartTag();
        startTag.name("");
        Assert.assertEquals("", startTag.asStartTag().name());
    }

    @Test
    public void testToken_leadingAndTrailingWhitespace() {
        Token startTag = new Token.StartTag();
        startTag.name("  <html>  ");
        Assert.assertEquals("<html>", startTag.asStartTag().name());
    }

    @Test
    public void testToken_multipleNames() {
        Token startTag = new Token.StartTag();
        startTag.name("html body");
        Assert.assertEquals("html body", startTag.asStartTag().name());
    }

    @Test
    public void testToken_selfClosingWithAttributes() {
        Token startTag = new Token.StartTag();
        startTag.name("img");
        startTag.newAttribute();
        startTag.appendAttributeName("src");
        startTag.appendAttributeValue("image.png");
        startTag.newAttribute();
        startTag.appendAttributeName("alt");
        startTag.appendAttributeValue("An image.");
        startTag.asStartTag().setSelfClosing(true);
        Assert.assertTrue(startTag.asStartTag().isSelfClosing());
        Assert.assertNotNull(startTag.asStartTag().getAttributes());
        Assert.assertEquals("image.png", startTag.asStartTag().getAttributes().value("src"));
        Assert.assertEquals("An image.", startTag.asStartTag().getAttributes().value("alt"));
    }

    @Test
    public void testToken_multipleAttributes() {
        Token startTag = new Token.StartTag();
        startTag.name("a");
        startTag.newAttribute();
        startTag.appendAttributeName("href");
        startTag.appendAttributeValue("https://www.google.com/");
        startTag.newAttribute();
        startTag.appendAttributeName("target");
        startTag.appendAttributeValue("_blank");
        startTag.newAttribute();
        startTag.appendAttributeName("class");
        startTag.appendAttributeValue("link");
        Assert.assertNotNull(startTag.asStartTag().getAttributes());
        Assert.assertEquals("https://www.google.com/", startTag.asStartTag().getAttributes().value("href"));
        Assert.assertEquals("_blank", startTag.asStartTag().getAttributes().value("target"));
        Assert.assertEquals("link", startTag.asStartTag().getAttributes().value("class"));
    }

    @Test
    public void testToken_attributesWithValuesContainingQuotes() {
        Token startTag = new Token.StartTag();
        startTag.name("input");
        startTag.newAttribute();
        startTag.appendAttributeName("type");
        startTag.appendAttributeValue("text");
        startTag.newAttribute();
        startTag.appendAttributeName("name");
        startTag.appendAttributeValue("myTextInput");
        startTag.newAttribute();
        startTag.appendAttributeName("value");
        startTag.appendAttributeValue("\"Hello, World!\"");
        Assert.assertNotNull(startTag.asStartTag().getAttributes());
        Assert.assertEquals("text", startTag.asStartTag().getAttributes().value("type"));
        Assert.assertEquals("myTextInput", startTag.asStartTag().getAttributes().value("name"));
        Assert.assertEquals("Hello, World!", startTag.asStartTag().getAttributes().value("value"));
    }

    @Test
    public void testToken_attributesWithValuesContainingAngleBrackets() {
        Token startTag = new Token.StartTag();
        startTag.name("input");
        startTag.newAttribute();
        startTag.appendAttributeName("type");
        startTag.appendAttributeValue("text");
        startTag.newAttribute();
        startTag.appendAttributeName("name");
        startTag.appendAttributeValue("myTextInput<input>");
        Assert.assertNotNull(startTag.asStartTag().getAttributes());
        Assert.assertEquals("text", startTag.asStartTag().getAttributes().value("type"));
        Assert.assertEquals("myTextInput<input>", startTag.asStartTag().getAttributes().value("name"));
    }

    // ... Add similar test methods for edge cases and exceptions in other classes

}