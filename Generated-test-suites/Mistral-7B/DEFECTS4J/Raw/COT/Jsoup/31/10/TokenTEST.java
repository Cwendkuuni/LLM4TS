import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @Test
    public void testDoctype_Basic() {
        Token doctype = new Token.Doctype();
        Assert.assertEquals(TokenType.Doctype, doctype.getType());
    }

    @Test
    public void testDoctype_getName_Basic() {
        Token doctype = new Token.Doctype();
        doctype.getName().append("HTML");
        Assert.assertEquals("Doctype", doctype.tokenType());
        Assert.assertEquals("HTML", doctype.asDoctype().getName());
    }

    @Test(expected = IllegalStateException.class)
    public void testDoctype_getName_InvalidUsage() {
        Token doctype = new Token.Doctype();
        Assert.assertNull(doctype.asDoctype().getSystemIdentifier());
        doctype.getName().append("HTML");
        Assert.assertNotNull(doctype.asDoctype().getSystemIdentifier());
    }

    @Test
    public void testStartTag_Basic() {
        Token startTag = new Token.StartTag();
        Assert.assertEquals(TokenType.StartTag, startTag.getType());
    }

    @Test
    public void testStartTag_Name_Basic() {
        Token startTag = new Token.StartTag("div");
        Assert.assertEquals("StartTag", startTag.tokenType());
        Assert.assertEquals("div", startTag.asStartTag().getName());
    }

    @Test
    public void testStartTag_Attributes_Basic() {
        Token startTag = new Token.StartTag("img", new Attributes().put("src", "image.jpg"));
        Assert.assertEquals("StartTag", startTag.tokenType());
        Assert.assertEquals("img", startTag.asStartTag().getName());
        Assert.assertEquals(1, startTag.asStartTag().getAttributes().size());
        Assert.assertEquals("src", startTag.asStartTag().getAttributes().get("src").getName());
        Assert.assertEquals("image.jpg", startTag.asStartTag().getAttributes().get("src").getValue());
    }

    @Test
    public void testStartTag_SelfClosing_Basic() {
        Token startTag = new Token.StartTag("img", new Attributes());
        Assert.assertEquals(false, startTag.asStartTag().isSelfClosing());

        startTag = new Token.StartTag("img", new Attributes()).name("img").selfClosing(true);
        Assert.assertEquals(true, startTag.asStartTag().isSelfClosing());
    }

    @Test
    public void testEndTag_Basic() {
        Token endTag = new Token.EndTag();
        Assert.assertEquals(TokenType.EndTag, endTag.getType());
    }

    @Test
    public void testEndTag_Name_Basic() {
        Token endTag = new Token.EndTag("div");
        Assert.assertEquals("EndTag", endTag.tokenType());
        Assert.assertEquals("div", endTag.asEndTag().getName());
    }

    @Test
    public void testComment_Basic() {
        Token comment = new Token.Comment();
        Assert.assertEquals(TokenType.Comment, comment.getType());
    }

    @Test
    public void testComment_Data_Basic() {
        Token comment = new Token.Comment();
        comment.getData().append("This is a comment.");
        Assert.assertEquals("Comment", comment.tokenType());
        Assert.assertEquals("This is a comment.", comment.asComment().getData());
    }

    @Test
    public void testCharacter_Basic() {
        Token character = new Token.Character("a");
        Assert.assertEquals(TokenType.Character, character.getType());
        Assert.assertEquals('a', character.asCharacter().getData().charAt(0));
    }

    @Test
    public void testEOF_Basic() {
        Token eof = new Token.EOF();
        Assert.assertEquals(TokenType.EOF, eof.getType());
    }

    @Test(expected = ClassCastException.class)
    public void testInvalidCast() {
        Token token = new Token.StartTag();
        Token doctype = (Token.Doctype) token;
    }
}