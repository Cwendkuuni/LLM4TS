import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        Assert.assertEquals(TokenType.Doctype, token.type);
    }

    @Test
    public void testTokenTypeToString() {
        Token token = new Token.Doctype();
        Assert.assertEquals("Doctype", token.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "pubSys";
        doctype.publicIdentifier.append("publicId");
        doctype.systemIdentifier.append("systemId");
        doctype.forceQuirks = true;

        doctype.reset();

        Assert.assertEquals("", doctype.getName());
        Assert.assertNull(doctype.getPubSysKey());
        Assert.assertEquals("", doctype.getPublicIdentifier());
        Assert.assertEquals("", doctype.getSystemIdentifier());
        Assert.assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "id";
        startTag.pendingAttributeValue.append("example");
        startTag.pendingAttributeValueS = null;
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();

        startTag.reset();

        Assert.assertNull(startTag.tagName);
        Assert.assertNull(startTag.normalName);
        Assert.assertNull(startTag.pendingAttributeName);
        Assert.assertEquals("", startTag.pendingAttributeValue.toString());
        Assert.assertNull(startTag.pendingAttributeValueS);
        Assert.assertFalse(startTag.hasEmptyAttributeValue);
        Assert.assertFalse(startTag.hasPendingAttributeValue);
        Assert.assertFalse(startTag.selfClosing);
        Assert.assertNull(startTag.attributes);
    }

    @Test
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        endTag.normalName = "div";

        endTag.reset();

        Assert.assertNull(endTag.tagName);
        Assert.assertNull(endTag.normalName);
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        comment.bogus = true;

        comment.reset();

        Assert.assertEquals("", comment.getData());
        Assert.assertFalse(comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "A";

        character.reset();

        Assert.assertNull(character.getData());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        eof.reset();
    }

    @Test
    public void testIsDoctype() {
        Token token = new Token.Doctype();
        Assert.assertTrue(token.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        Token token = new Token.Doctype();
        Token.Doctype doctype = token.asDoctype();
        Assert.assertNotNull(doctype);
    }

    @Test
    public void testIsStartTag() {
        Token token = new Token.StartTag();
        Assert.assertTrue(token.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        Token token = new Token.StartTag();
        Token.StartTag startTag = token.asStartTag();
        Assert.assertNotNull(startTag);
    }

    @Test
    public void testIsEndTag() {
        Token token = new Token.EndTag();
        Assert.assertTrue(token.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        Token token = new Token.EndTag();
        Token.EndTag endTag = token.asEndTag();
        Assert.assertNotNull(endTag);
    }

    @Test
    public void testIsComment() {
        Token token = new Token.Comment();
        Assert.assertTrue(token.isComment());
    }

    @Test
    public void testAsComment() {
        Token token = new Token.Comment();
        Token.Comment comment = token.asComment();
        Assert.assertNotNull(comment);
    }

    @Test
    public void testIsCharacter() {
        Token token = new Token.Character();
        Assert.assertTrue(token.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Token token = new Token.Character();
        Token.Character character = token.asCharacter();
        Assert.assertNotNull(character);
    }

    @Test
    public void testIsEOF() {
        Token token = new Token.EOF();
        Assert.assertTrue(token.isEOF());
    }
}