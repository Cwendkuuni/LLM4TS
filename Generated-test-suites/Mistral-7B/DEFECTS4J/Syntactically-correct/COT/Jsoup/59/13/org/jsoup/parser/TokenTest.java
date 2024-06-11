import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @Test
    public void testResetDoctype() {
        Token doctype = new Token.Doctype();
        doctype.asDoctype().getName(); // initialize some state
        doctype.reset();
        Assert.assertNull(doctype.asDoctype().getName());
    }

    @Test
    public void testResetTag() {
        Token tag = new Token.StartTag();
        tag.asStartTag().name("test");
        tag.reset();
        Assert.assertNull(tag.asStartTag().name());
    }

    @Test
    public void testNewAttributeForStartTag() {
        Token tag = new Token.StartTag();
        tag.asStartTag().name("test").newAttribute().key("test").value("test");
        Assert.assertEquals("test test=\"test\"", tag.toString());
    }

    @Test
    public void testFinaliseTagForStartTag() {
        Token tag = new Token.StartTag();
        tag.asStartTag().name("test").newAttribute().key("test").value("test");
        tag.asStartTag().finaliseTag();
        Assert.assertEquals("<test test=\"test\"/>", tag.toString());
    }

    @Test
    public void testSelfClosingTag() {
        Token tag = new Token.StartTag();
        tag.asStartTag().name("img").selfClosing(true);
        Assert.assertEquals("<img />", tag.toString());
    }

    @Test
    public void testResetComment() {
        Token comment = new Token.Comment();
        comment.asComment().getData("test"); // initialize some state
        comment.reset();
        Assert.assertNull(comment.asComment().getData());
    }

    @Test
    public void testResetCharacter() {
        Token character = new Token.Character();
        character.asCharacter().data("test"); // initialize some state
        character.reset();
        Assert.assertNull(character.asCharacter().getData());
    }

    @Test(expected = AssertionError.class)
    public void testResetEOF() {
        Token eof = new Token.EOF();
        eof.reset(); // should not be able to reset EOF token
    }

    @Test
    public void testGetTokenType() {
        Token doctype = new Token.Doctype();
        Token startTag = new Token.StartTag();
        Token endTag = new Token.EndTag();
        Token comment = new Token.Comment();
        Token character = new Token.Character();
        Token eof = new Token.EOF();

        Assert.assertEquals(TokenType.Doctype, doctype.isDoctype());
        Assert.assertEquals(TokenType.StartTag, startTag.isStartTag());
        Assert.assertEquals(TokenType.EndTag, endTag.isEndTag());
        Assert.assertEquals(TokenType.Comment, comment.isComment());
        Assert.assertEquals(TokenType.Character, character.isCharacter());
        Assert.assertEquals(TokenType.EOF, eof.isEOF());
    }
}