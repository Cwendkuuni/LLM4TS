import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @Test
    public void testDoctype() {
        Token doctype = new Token.Doctype();
        Assert.assertEquals(TokenType.Doctype, doctype.tokenType());
    }

    @Test
    public void testDoctype_getName() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().getName("html");
        Assert.assertEquals("html", doctype.asDoctype().getName());
    }

    @Test(expected = AssertionError.class)
    public void testDoctype_forceQuirks() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().setForceQuirks(true);
        Assert.assertTrue(doctype.asDoctype().isForceQuirks());

        // Edge case: forceQuirks should not be settable after instance creation
        Token doctype2 = new Token.Doctype();
        Assert.assertFalse(doctype2.asDoctype().isForceQuirks());
        doctype2.asDoctype().setForceQuirks(true);
        Assert.assertFalse(doctype2.asDoctype().isForceQuirks());
    }

    @Test
    public void testStartTag() {
        Token startTag = new Token.StartTag();
        Assert.assertEquals(TokenType.StartTag, startTag.tokenType());
    }

    @Test
    public void testStartTag_name() {
        Token startTag = new Token.StartTag("html");
        Assert.assertEquals("html", startTag.asStartTag().name());
    }

    @Test
    public void testStartTag_attributes() {
        Token startTag = new Token.StartTag("html").newAttribute("class", "test");
        Assert.assertNotNull(startTag.asStartTag().getAttributes());
        Assert.assertEquals("class", startTag.asStartTag().getAttributes().get("class"));
    }

    @Test(expected = AssertionError.class)
    public void testStartTag_selfClosing() {
        Token startTag = new Token.StartTag("img");
        startTag.asStartTag().setSelfClosing(true);
        Assert.assertTrue(startTag.asStartTag().isSelfClosing());

        // Edge case: selfClosing should not be settable after instance creation
        Token startTag2 = new Token.StartTag("div");
        Assert.assertFalse(startTag2.asStartTag().isSelfClosing());
        startTag2.asStartTag().setSelfClosing(true);
        Assert.assertFalse(startTag2.asStartTag().isSelfClosing());
    }

    @Test
    public void testEndTag() {
        Token endTag = new Token.EndTag();
        Assert.assertEquals(TokenType.EndTag, endTag.tokenType());
    }

    @Test
    public void testEndTag_name() {
        Token endTag = new Token.EndTag("html");
        Assert.assertEquals("html", endTag.asEndTag().name());
    }

    @Test
    public void testComment() {
        Token comment = new Token.Comment();
        Assert.assertEquals(TokenType.Comment, comment.tokenType());
    }

    @Test
    public void testComment_data() {
        Token comment = new Token.Comment("This is a comment");
        Assert.assertEquals("This is a comment", comment.asComment().getData());
    }

    @Test
    public void testCharacter() {
        Token character = new Token.Character("&");
        Assert.assertEquals(TokenType.Character, character.tokenType());
        Assert.assertEquals("&", character.asCharacter().getData());
    }

    @Test
    public void testCharacter_dataWithEntity() {
        Token character = new Token.Character("&amp;");
        Assert.assertEquals("&amp;", character.asCharacter().getData());
    }

    @Test
    public void testEOF() {
        Token eof = new Token.EOF();
        Assert.assertEquals(TokenType.EOF, eof.tokenType());
    }
}