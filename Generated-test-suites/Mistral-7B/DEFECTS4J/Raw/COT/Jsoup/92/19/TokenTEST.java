import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Tokenizer;
import org.jsoup.helper.Validate;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getName().equals("");
        doctype.reset();
        doctype.getName().equals("");
        Assert.assertNull(doctype.getPubSysKey());
        Assert.assertNull(doctype.getPublicIdentifier());
        Assert.assertNull(doctype.getSystemIdentifier());
        Assert.assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testDoctypeSetters() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getName().equals("");

        doctype.setName("html");
        doctype.getName().equals("html");

        doctype.setPubSysKey("pubsyskey");
        doctype.getPubSysKey().equals("pubsyskey");

        doctype.setPublicIdentifier("publicidentifier");
        doctype.getPublicIdentifier().equals("publicidentifier");

        doctype.setSystemIdentifier("systemidentifier");
        doctype.getSystemIdentifier().equals("systemidentifier");

        doctype.setForceQuirks(true);
        doctype.isForceQuirks();
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = null;
        tag.normalName = null;
        tag.pendingAttributeName = null;
        tag.pendingAttributeValue.length();
        tag.hasEmptyAttributeValue = false;
        tag.hasPendingAttributeValue = false;
        tag.selfClosing = false;
        tag.attributes = null;

        tag.reset();

        tag.tagName = null;
        tag.normalName = null;
        tag.pendingAttributeName = null;
        tag.pendingAttributeValue.length();
        tag.hasEmptyAttributeValue = false;
        tag.hasPendingAttributeValue = false;
        tag.selfClosing = false;
        tag.attributes = null;

        Assert.assertNull(tag.tagName);
        Assert.assertNull(tag.normalName);
        Assert.assertTrue(tag.pendingAttributeName == null);
        Assert.assertTrue(tag.pendingAttributeValue.length() == 0);
        Assert.assertFalse(tag.hasEmptyAttributeValue);
        Assert.assertFalse(tag.hasPendingAttributeValue);
        Assert.assertFalse(tag.selfClosing);
        Assert.assertNull(tag.attributes);
    }

    @Test
    public void testTagSetters() {
        Token.StartTag tag = new Token.StartTag();

        tag.nameAttr("html", null);
        tag.tagName.equals("html");
        tag.normalName.equals("html");

        tag.nameAttr("html", new Attributes());
        tag.tagName.equals("html");
        tag.normalName.equals("html");

        tag.newAttribute();
        tag.pendingAttributeName.equals("");
        tag.hasPendingAttributeValue = false;

        tag.appendAttributeName("class");
        tag.pendingAttributeName.equals("class");

        tag.appendAttributeValue("test");
        tag.hasPendingAttributeValue = true;
        tag.pendingAttributeValue.toString().equals("test");

        tag.newAttribute();
        Assert.assertFalse(tag.hasPendingAttributeValue);
        Assert.assertTrue(tag.pendingAttributeValue.length() == 0);

        tag.finaliseTag();
        Assert.assertNotNull(tag.getAttributes());
        tag.getAttributes().toString().equals("class=\"test\"");
    }

    @Test(expected = AssertionError.class)
    public void testTagSettersNullAttributes() {
        Token.StartTag tag = new Token.StartTag();
        tag.nameAttr("html", null);
    }

    @Test
    public void testTagToString() {
        Token.StartTag tag = new Token.StartTag();
        tag.nameAttr("html", new Attributes());
        tag.toString().equals("<html>");
    }

    @Test
    public void testTagIsSelfClosing() {
        Token.StartTag tag = new Token.StartTag();
        tag.isSelfClosing = true;
        tag.isSelfClosing();
        assertTrue(tag.isSelfClosing());

        Token.StartTag tag2 = new Token.StartTag();
        tag2.isSelfClosing = false;
        tag2.isSelfClosing();
        assertFalse(tag2.isSelfClosing());
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.length();
        comment.bogus = false;

        comment.reset();

        comment.data.length();
        comment.bogus = false;
    }

    @Test
    public void testCommentSetters() {
        Token.Comment comment = new Token.Comment();

        comment.data.append("test");
        comment.data.toString().equals("test");

        comment.bogus = true;
        comment.isBogus();
        assertTrue(comment.isBogus());
    }

    @Test
    public void testCommentToString() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("<!--test-->");
        comment.toString().equals("<!--test-->");
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = null;

        character.reset();

        character.data = null;
    }

    @Test
    public void testCharacterSetters() {
        Token.Character character = new Token.Character();

        character.data("test");
        character.getData().equals("test");

        character.data("test\u0001");
        character.getData().equals("test\u0001");
    }

    @Test
    public void testCharacterToString() {
        Token.Character character = new Token.Character();
        character.data("test");
        character.toString().equals("test");
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        eof.reset();

        assertTrue(eof.isEOF());
    }

    @Test
    public void testTokenTypeIdentifiers() {
        TokenType[] tokenTypes = TokenType.values();

        for (TokenType tokenType : tokenTypes) {
            String identifier = tokenType.toString();
            TokenType parsedTokenType = TokenType.valueOf(identifier);
            assertEquals(tokenType, parsedTokenType);
        }
    }

    @Test
    public void testTokenIsType() {
        Token token = new Token.StartTag();

        assertTrue(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());

        token = new Token.Doctype();
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());
        assertTrue(token.isDoctype());

        token = new Token.Comment();
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertTrue(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());

        token = new Token.Character();
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertTrue(token.isCharacter());
        assertFalse(token.isEOF());

        token = new Token.EndTag();
        assertFalse(token.isStartTag());
        assertTrue(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isEOF());

        token = new Token.EOF();
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertTrue(token.isEOF());
    }

    @Test
    public void testTokenIsTypeInstanceof() {
        Token token = new Token.StartTag();

        assertFalse(token instanceof Token.Doctype);
        assertFalse(token instanceof Token.Comment);
        assertFalse(token instanceof Token.Character);
        assertFalse(token instanceof Token.EndTag);
        assertFalse(token instanceof Token.EOF);

        token = new Token.Doctype();
        assertFalse(token instanceof Token.StartTag);
        assertFalse(token instanceof Token.Comment);
        assertFalse(token instanceof Token.Character);
        assertFalse(token instanceof Token.EndTag);
        assertFalse(token instanceof Token.EOF);
        assertTrue(token instanceof Token.Doctype);

        token = new Token.Comment();
        assertFalse(token instanceof Token.StartTag);
        assertFalse(token instanceof Token.Doctype);
        assertFalse(token instanceof Token.Character);
        assertFalse(token instanceof Token.EndTag);
        assertFalse(token instanceof Token.EOF);
        assertTrue(token instanceof Token.Comment);

        token = new Token.Character();
        assertFalse(token instanceof Token.StartTag);
        assertFalse(token instanceof Token.Doctype);
        assertFalse(token instanceof Token.Comment);
        assertFalse(token instanceof Token.EndTag);
        assertFalse(token instanceof Token.EOF);
        assertTrue(token instanceof Token.Character);

        token = new Token.EndTag();
        assertFalse(token instanceof Token.StartTag);
        assertFalse(token instanceof Token.Doctype);
        assertFalse(token instanceof Token.Comment);
        assertFalse(token instanceof Token.Character);
        assertFalse(token instanceof Token.EOF);
        assertTrue(token instanceof Token.EndTag);

        token = new Token.EOF();
        assertFalse(token instanceof Token.StartTag);
        assertFalse(token instanceof Token.Doctype);
        assertFalse(token instanceof Token.Comment);
        assertFalse(token instanceof Token.Character);
        assertTrue(token instanceof Token.EOF);
    }
}