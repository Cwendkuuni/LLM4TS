import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @org.junit.Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());

        token = new Token.StartTag();
        assertEquals("StartTag", token.tokenType());

        token = new Token.EndTag();
        assertEquals("EndTag", token.tokenType());

        token = new Token.Comment();
        assertEquals("Comment", token.tokenType());

        token = new Token.Character();
        assertEquals("Character", token.tokenType());

        token = new Token.EOF();
        assertEquals("EOF", token.tokenType());
    }

    @org.junit.Test
    public void testDoctypeReset() {
        Token doctype = new Token.Doctype();
        assertEquals(TokenType.Doctype, doctype.type);

        doctype.reset();

        assertNull(doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
        assertEquals(TokenType.Doctype, doctype.type);
    }

    @org.junit.Test
    public void testStartTagReset() {
        Token startTag = new Token.StartTag();
        assertEquals(TokenType.StartTag, startTag.type);

        startTag.reset();

        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertNull(startTag.pendingAttributeValue);
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
        assertNull(startTag.attributes);
        assertEquals(TokenType.StartTag, startTag.type);
    }

    @org.junit.Test
    public void testEndTagReset() {
        Token endTag = new Token.EndTag();
        assertEquals(TokenType.EndTag, endTag.type);

        endTag.reset();

        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
        assertNull(endTag.attributes);
        assertEquals(TokenType.EndTag, endTag.type);
    }

    @org.junit.Test
    public void testCommentReset() {
        Token comment = new Token.Comment();
        assertEquals(TokenType.Comment, comment.type);

        comment.reset();

        assertNull(comment.data);
        assertFalse(comment.bogus);
        assertEquals(TokenType.Comment, comment.type);
    }

    @org.junit.Test
    public void testCharacterReset() {
        Token character = new Token.Character();
        assertEquals(TokenType.Character, character.type);

        character.data("test");
        assertEquals("test", character.getData());

        character.reset();

        assertNull(character.getData());
        assertEquals(TokenType.Character, character.type);
    }

    @org.junit.Test
    public void testEOFReset() {
        Token eof = new Token.EOF();
        assertEquals(TokenType.EOF, eof.type);

        eof.reset();

        assertEquals(TokenType.EOF, eof.type);
    }

    @org.junit.Test
    public void testDoctypeNewAttribute() {
        Token doctype = new Token.Doctype();

        doctype.newAttribute();
        assertFalse(doctype.hasPendingAttributeValue);
        assertNull(doctype.attributes);

        String attributeName = "test";
        doctype.appendAttributeName(attributeName);
        assertEquals(attributeName, doctype.pendingAttributeName);
        assertFalse(doctype.hasPendingAttributeValue);
        assertNull(doctype.attributes);

        String attributeValue = "value";
        doctype.appendAttributeValue(attributeValue);
        assertFalse(doctype.hasEmptyAttributeValue);
        assertFalse(doctype.hasPendingAttributeValue);
        assertNull(doctype.attributes);

        doctype.newAttribute();
        assertFalse(doctype.hasPendingAttributeValue);
        assertNotNull(doctype.attributes);
        assertEquals(1, doctype.attributes.size());
        Attribute attribute = doctype.attributes.get("test");
        assertEquals("value", attribute.getValue());
        assertFalse(doctype.hasPendingAttributeValue);
        assertNull(doctype.pendingAttributeName);
        assertNull(doctype.pendingAttributeValue);
        assertNull(doctype.pendingAttributeValueS);
        assertFalse(doctype.hasEmptyAttributeValue);
    }

    @org.junit.Test
    public void testStartTagNewAttribute() {
        Token startTag = new Token.StartTag();

        startTag.newAttribute();
        assertFalse(startTag.hasPendingAttributeValue);
        assertNull(startTag.attributes);

        String attributeName = "test";
        startTag.appendAttributeName(attributeName);
        assertEquals(attributeName, startTag.pendingAttributeName);
        assertFalse(startTag.hasPendingAttributeValue);
        assertNull(startTag.attributes);

        String attributeValue = "value";
        startTag.appendAttributeValue(attributeValue);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertNull(startTag.attributes);

        startTag.newAttribute();
        assertFalse(startTag.hasPendingAttributeValue);
        assertNotNull(startTag.attributes);
        assertEquals(1, startTag.attributes.size());
        Attribute attribute = startTag.attributes.get("test");
        assertEquals("value", attribute.getValue());
        assertFalse(startTag.hasPendingAttributeValue);
        assertNull(startTag.pendingAttributeName);
        assertNull(startTag.pendingAttributeValue);
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
    }

    @org.junit.Test
    public void testStartTagFinaliseTag() {
        Token startTag = new Token.StartTag();

        String tagName = "test";
        startTag.name(tagName);

        String attributeName = "test";
        startTag.newAttribute();
        startTag.appendAttributeName(attributeName);
        String attributeValue = "value";
        startTag.appendAttributeValue(attributeValue);
        startTag.newAttribute();

        startTag.finaliseTag();

        assertEquals(tagName, startTag.tagName);
        assertEquals("test", startTag.normalName());
        assertEquals("value", startTag.getAttributes().get("test").getValue());
        assertFalse(startTag.hasPendingAttributeValue);
        assertNull(startTag.pendingAttributeName);
        assertNull(startTag.pendingAttributeValue);
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
    }
}