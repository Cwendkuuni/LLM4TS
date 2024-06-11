import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @Test
    public void testTokenTypeDoctype() {
        Token token = new Token.Doctype();
        Assert.assertEquals(TokenType.Doctype, token.type);
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getName().equals("html");
        doctype.reset();
        Assert.assertNull(doctype.getName());
        Assert.assertNull(doctype.getPubSysKey());
        Assert.assertNull(doctype.getPublicIdentifier());
        Assert.assertNull(doctype.getSystemIdentifier());
        Assert.assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTokenTypeStartTag() {
        Token token = new Token.StartTag();
        Assert.assertEquals(TokenType.StartTag, token.type);
    }

    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("html").attributes(new Attributes().put("class", "test"));
        startTag.reset();
        Assert.assertNull(startTag.tagName);
        Assert.assertNull(startTag.normalName);
        Assert.assertNull(startTag.pendingAttributeName);
        Assert.assertNull(startTag.pendingAttributeValue);
        Assert.assertNull(startTag.pendingAttributeValueS);
        Assert.assertFalse(startTag.hasEmptyAttributeValue);
        Assert.assertFalse(startTag.hasPendingAttributeValue);
        Assert.assertFalse(startTag.selfClosing);
        Assert.assertNull(startTag.attributes);
    }

    @Test
    public void testTokenTypeEndTag() {
        Token token = new Token.EndTag();
        Assert.assertEquals(TokenType.EndTag, token.type);
    }

    @Test
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("html");
        endTag.reset();
        Assert.assertNull(endTag.tagName);
        Assert.assertNull(endTag.normalName);
        Assert.assertNull(endTag.pendingAttributeName);
        Assert.assertNull(endTag.pendingAttributeValue);
        Assert.assertNull(endTag.pendingAttributeValueS);
        Assert.assertFalse(endTag.hasEmptyAttributeValue);
        Assert.assertFalse(endTag.hasPendingAttributeValue);
        Assert.assertFalse(endTag.selfClosing);
        Assert.assertNull(endTag.attributes);
    }

    @Test
    public void testTokenTypeComment() {
        Token token = new Token.Comment();
        Assert.assertEquals(TokenType.Comment, token.type);
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data("test comment");
        comment.reset();
        Assert.assertNull(comment.getData());
        Assert.assertFalse(comment.bogus);
    }

    @Test
    public void testTokenTypeCharacter() {
        Token token = new Token.Character();
        Assert.assertEquals(TokenType.Character, token.type);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data("test character");
        character.reset();
        Assert.assertNull(character.getData());
    }

    @Test
    public void testTokenTypeEOF() {
        Token token = new Token.EOF();
        Assert.assertEquals(TokenType.EOF, token.type);
    }

    @Test
    public void testEOFReset() {
        Token eof = new Token.EOF();
        eof.reset();
        // EOF does not have a reset method, so no need to test it.
    }
}