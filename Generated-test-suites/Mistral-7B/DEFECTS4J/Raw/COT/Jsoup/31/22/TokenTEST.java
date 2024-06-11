import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Tokenizer;
import org.jsoup.helper.Validate;

public class TokenTest {

    @Test
    public void testDoctype_basic() {
        Token doctype = new Token.Doctype();
        Assert.assertEquals(TokenType.Doctype, doctype.isDoctype());
    }

    @Test
    public void testDoctype_getName_basic() {
        Token doctype = new Token.Doctype();
        doctype.name("html");
        Assert.assertEquals("html", doctype.asDoctype().getName());
    }

    @Test(expected = AssertionError.class)
    public void testDoctype_getName_invalid() {
        Token doctype = new Token.Doctype();
        doctype.name(null);
    }

    @Test
    public void testDoctype_getPublicIdentifier_basic() {
        Token doctype = new Token.Doctype();
        doctype.publicIdentifier("_public");
        Assert.assertEquals("_public", doctype.asDoctype().getPublicIdentifier());
    }

    @Test(expected = AssertionError.class)
    public void testDoctype_getPublicIdentifier_invalid() {
        Token doctype = new Token.Doctype();
        doctype.publicIdentifier(null);
    }

    @Test
    public void testDoctype_getSystemIdentifier_basic() {
        Token doctype = new Token.Doctype();
        doctype.systemIdentifier("_system");
        Assert.assertEquals("_system", doctype.asDoctype().getSystemIdentifier());
    }

    @Test(expected = AssertionError.class)
    public void testDoctype_getSystemIdentifier_invalid() {
        Token doctype = new Token.Doctype();
        doctype.systemIdentifier(null);
    }

    @Test
    public void testDoctype_isForceQuirks_basic() {
        Token doctype = new Token.Doctype();
        doctype.forceQuirks(true);
        Assert.assertTrue(doctype.asDoctype().isForceQuirks());
    }

    @Test
    public void testTag_basic() {
        Token tag = new Token.StartTag();
        Assert.assertEquals(TokenType.StartTag, tag.isStartTag());
    }

    @Test
    public void testTag_name_basic() {
        Token tag = new Token.StartTag();
        tag.name("div");
        Assert.assertEquals("div", tag.asStartTag().name());
    }

    @Test(expected = AssertionError.class)
    public void testTag_name_invalid() {
        Token tag = new Token.StartTag();
        tag.name(null);
    }

    @Test
    public void testTag_isSelfClosing_basic() {
        Token tag = new Token.StartTag();
        tag.isSelfClosing(true);
        Assert.assertTrue(tag.asStartTag().isSelfClosing());
    }

    @Test
    public void testTag_attributes_basic() {
        Token tag = new Token.StartTag();
        tag.newAttribute();
        tag.appendAttributeName("class");
        tag.appendAttributeValue("test");
        tag.newAttribute();
        tag.appendAttributeName("id");
        tag.appendAttributeValue("myId");
        Assert.assertNotNull(tag.asStartTag().getAttributes());
        Assert.assertEquals(2, tag.asStartTag().getAttributes().size());
        Assert.assertEquals("test", tag.asStartTag().getAttributes().get("class"));
        Assert.assertEquals("myId", tag.asStartTag().getAttributes().get("id"));
    }

    @Test
    public void testTag_finaliseTag_basic() {
        Token tag = new Token.StartTag();
        tag.appendAttributeName("class");
        tag.appendAttributeValue("test");
        tag.finaliseTag();
        Assert.assertNotNull(tag.asStartTag().getAttributes());
        Assert.assertEquals(1, tag.asStartTag().getAttributes().size());
        Assert.assertEquals("test", tag.asStartTag().getAttributes().get("class"));
    }

    @Test(expected = AssertionError.class)
    public void testTag_finaliseTag_beforeAttribute_set() {
        Token tag = new Token.StartTag();
        tag.finaliseTag();
    }

    @Test
    public void testComment_basic() {
        Token comment = new Token.Comment();
        Assert.assertEquals(TokenType.Comment, comment.isComment());
    }

    @Test
    public void testComment_getData_basic() {
        Token comment = new Token.Comment();
        comment.data("This is a comment");
        Assert.assertEquals("This is a comment", comment.asComment().getData());
    }

    @Test(expected = AssertionError.class)
    public void testComment_getData_invalid() {
        Token comment = new Token.Comment();
        comment.data(null);
    }

    @Test
    public void testCharacter_basic() {
        Token character = new Token.Character("A");
        Assert.assertEquals(TokenType.Character, character.isCharacter());
    }

    @Test
    public void testCharacter_getData_basic() {
        Token character = new Token.Character("A");
        Assert.assertEquals("A", character.asCharacter().getData());
    }

    @Test(expected = AssertionError.class)
    public void testCharacter_getData_invalid() {
        Token character = new Token.Character(null);
    }

    @Test
    public void testEOF_basic() {
        Token eof = new Token.EOF();
        Assert.assertEquals(TokenType.EOF, eof.isEOF());
    }

    @Test
    public void testToken_isDoctype_whenDoctype() {
        Token token = new Token.Doctype();
        Assert.assertTrue(token.isDoctype());
    }

    @Test
    public void testToken_isStartTag_whenStartTag() {
        Token token = new Token.StartTag();
        Assert.assertTrue(token.isStartTag());
    }

    @Test
    public void testToken_isEndTag_whenEndTag() {
        Token token = new Token.EndTag();
        Assert.assertTrue(token.isEndTag());
    }

    @Test
    public void testToken_isComment_whenComment() {
        Token token = new Token.Comment();
        Assert.assertTrue(token.isComment());
    }

    @Test
    public void testToken_isCharacter_whenCharacter() {
        Token token = new Token.Character("A");
        Assert.assertTrue(token.isCharacter());
    }

    @Test
    public void testToken_isEOF_whenEOF() {
        Token token = new Token.EOF();
        Assert.assertTrue(token.isEOF());
    }
}