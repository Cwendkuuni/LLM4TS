import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @Test
    public void testDoctype_basic() {
        Token doctype = new Token.Doctype();
        Assert.assertEquals(TokenType.Doctype, doctype.tokenType());
    }

    @Test
    public void testDoctype_getName_basic() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().name("html");
        Assert.assertEquals("html", doctype.asDoctype().getName());
    }

    @Test(expected = IllegalStateException.class)
    public void testDoctype_getName_setTwice() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().name("html");
        doctype.asDoctype().name("body");
    }

    @Test
    public void testDoctype_getPublicIdentifier_basic() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().setPublicIdentifier("_public");
        Assert.assertEquals("_public", doctype.asDoctype().getPublicIdentifier());
    }

    @Test
    public void testDoctype_getSystemIdentifier_basic() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().setSystemIdentifier("_system");
        Assert.assertEquals("_system", doctype.asDoctype().getSystemIdentifier());
    }

    @Test
    public void testDoctype_isForceQuirks_basic() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().setForceQuirks(true);
        Assert.assertTrue(doctype.asDoctype().isForceQuirks());
    }

    @Test
    public void testTag_basic() {
        Token tag = new Token.StartTag();
        Assert.assertEquals(TokenType.StartTag, tag.tokenType());
    }

    @Test
    public void testTag_name_basic() {
        Token tag = new Token.StartTag();
        tag.asStartTag().name("div");
        Assert.assertEquals("div", tag.asStartTag().name());
    }

    @Test
    public void testTag_setSelfClosing_basic() {
        Token tag = new Token.StartTag();
        tag.asStartTag().setSelfClosing(true);
        Assert.assertTrue(tag.asStartTag().isSelfClosing());
    }

    @Test
    public void testTag_attributes_basic() {
        Token tag = new Token.StartTag();
        tag.asStartTag().newAttribute();
        tag.asStartTag().appendAttributeName("class");
        tag.asStartTag().appendAttributeValue("test");
        Assert.assertNotNull(tag.asStartTag().getAttributes());
    }

    @Test(expected = IllegalStateException.class)
    public void testTag_appendAttributeName_sameNameTwice() {
        Token tag = new Token.StartTag();
        tag.asStartTag().newAttribute();
        tag.asStartTag().appendAttributeName("class");
        tag.asStartTag().appendAttributeName("class");
    }

    @Test
    public void testTag_appendAttributeValue_null() {
        Token tag = new Token.StartTag();
        tag.asStartTag().newAttribute();
        tag.asStartTag().appendAttributeName("class");
        tag.asStartTag().appendAttributeValue(null);
        Assert.assertEquals("class", tag.asStartTag().getAttributes().keySet().iterator().next());
        Assert.assertNull(tag.asStartTag().getAttributes().get("class"));
    }

    @Test
    public void testTag_toString_basic() {
        Token tag = new Token.StartTag("div");
        tag.asStartTag().newAttribute();
        tag.asStartTag().appendAttributeName("class");
        tag.asStartTag().appendAttributeValue("test");
        Assert.assertEquals("<div class=\"test\"></div>", tag.toString());
    }

    @Test
    public void testTag_toString_withoutAttributes() {
        Token tag = new Token.StartTag("div");
        Assert.assertEquals("<div>", tag.toString());
    }

    @Test
    public void testComment_basic() {
        Token comment = new Token.Comment();
        Assert.assertEquals(TokenType.Comment, comment.tokenType());
    }

    @Test
    public void testComment_getData_basic() {
        Token comment = new Token.Comment();
        comment.asComment().appendData("This is a comment.");
        Assert.assertEquals("This is a comment.", comment.asComment().getData());
    }

    @Test
    public void testCharacter_basic() {
        Token character = new Token.Character("a");
        Assert.assertEquals(TokenType.Character, character.tokenType());
        Assert.assertEquals("a", character.asCharacter().getData());
    }

    @Test
    public void testEOF_basic() {
        Token eof = new Token.EOF();
        Assert.assertEquals(TokenType.EOF, eof.tokenType());
    }

}