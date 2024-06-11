import static org.junit.Assert.*;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals(Token.TokenType.Doctype, token.type);

        token = new Token.StartTag();
        assertEquals(Token.TokenType.StartTag, token.type);

        token = new Token.EndTag();
        assertEquals(Token.TokenType.EndTag, token.type);

        token = new Token.Comment();
        assertEquals(Token.TokenType.Comment, token.type);

        token = new Token.Character();
        assertEquals(Token.TokenType.Character, token.type);

        token = new Token.CData("");
        assertEquals(Token.TokenType.Character, token.type); // CData is treated as an extension of Character token type

        token = new Token.EOF();
        assertEquals(Token.TokenType.EOF, token.type);
    }

    @Test
    public void testTokenReset() {
        Token token = new Token.Doctype();
        assertNotNull(token.name);
        assertNotNull(token.publicIdentifier);
        assertNotNull(token.systemIdentifier);
        assertTrue(token.forceQuirks);
        token.reset();
        assertEquals("", token.name.toString());
        assertNull(token.pubSysKey);
        assertEquals("", token.publicIdentifier.toString());
        assertEquals("", token.systemIdentifier.toString());
        assertFalse(token.forceQuirks);

        token = new Token.StartTag();
        assertNotNull(token.tagName);
        assertNotNull(token.normalName);
        assertNotNull(token.pendingAttributeName);
        assertNotNull(token.pendingAttributeValue);
        assertNotNull(token.pendingAttributeValueS);
        assertFalse(token.hasEmptyAttributeValue);
        assertFalse(token.hasPendingAttributeValue);
        assertFalse(token.selfClosing);
        assertNotNull(token.attributes);
        token.reset();
        assertNull(token.tagName);
        assertNull(token.normalName);
        assertNull(token.pendingAttributeName);
        assertEquals("", token.pendingAttributeValue.toString());
        assertNull(token.pendingAttributeValueS);
        assertFalse(token.hasEmptyAttributeValue);
        assertFalse(token.hasPendingAttributeValue);
        assertFalse(token.selfClosing);
        assertNull(token.attributes);

        token = new Token.EndTag();
        assertNotNull(token.tagName);
        assertNotNull(token.normalName);
        assertNotNull(token.pendingAttributeName);
        assertNotNull(token.pendingAttributeValue);
        assertNotNull(token.pendingAttributeValueS);
        assertFalse(token.hasEmptyAttributeValue);
        assertFalse(token.hasPendingAttributeValue);
        assertFalse(token.selfClosing);
        assertNotNull(token.attributes);
        token.reset();
        assertNull(token.tagName);
        assertNull(token.normalName);
        assertNull(token.pendingAttributeName);
        assertEquals("", token.pendingAttributeValue.toString());
        assertNull(token.pendingAttributeValueS);
        assertFalse(token.hasEmptyAttributeValue);
        assertFalse(token.hasPendingAttributeValue);
        assertFalse(token.selfClosing);
        assertNull(token.attributes);

        token = new Token.Comment();
        assertNotNull(token.data);
        assertFalse(token.bogus);
        token.reset();
        assertEquals("", token.data.toString());
        assertFalse(token.bogus);

        token = new Token.Character();
        assertNull(token.data);
        token.reset();
        assertNull(token.data);

        token = new Token.CData("");
        assertNull(token.data);
        token.reset();
        assertNull(token.data);

        token = new Token.EOF();
        token.reset();
        assertNotNull(token); // reset() method does nothing for EOF token
    }

    @Test
    public void testDoctypeGetters() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "pubkey";
        doctype.publicIdentifier.append("pubid");
        doctype.systemIdentifier.append("sysid");
        doctype.forceQuirks = true;

        assertEquals("html", doctype.getName());
        assertEquals("pubkey", doctype.getPubSysKey());
        assertEquals("pubid", doctype.getPublicIdentifier());
        assertEquals("sysid", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTagMethods() {
        Token.Tag tag = new Token.StartTag();
        tag.name("div");
        tag.normalName(); // lower case version
        tag.appendTagName(">");
        tag.appendTagName('>');
        tag.appendAttributeName("class");
        tag.appendAttributeName('c');
        tag.appendAttributeValue("value");
        tag.appendAttributeValue('v');
        tag.appendAttributeValue(new char[]{'a', 'l', 'u', 'e'});
        tag.appendAttributeValue(new int[]{118, 97, 108, 117, 101});
        tag.setEmptyAttributeValue();

        assertEquals("div", tag.name());
        assertEquals("div", tag.normalName());
        assertEquals("div>>", tag.tagName);
        assertEquals("dicvc", tag.pendingAttributeName);
        assertEquals("valuevvvvvv", tag.pendingAttributeValue.toString());
        assertTrue(tag.hasEmptyAttributeValue);

        // Test appending attribute value when it is already in a second hit state
        tag.ensureAttributeValue();
        tag.appendAttributeValue("second hit");

        assertEquals("div", tag.name());
        assertEquals("div", tag.normalName());
        assertEquals("div>>", tag.tagName);
        assertEquals("dicvc", tag.pendingAttributeName);
        assertEquals("valuevvvvvvsecond hit", tag.pendingAttributeValue.toString());
        assertTrue(tag.hasEmptyAttributeValue);
        assertFalse(tag.pendingAttributeValueS != null);

        tag.finaliseTag();
        assertNull(tag.pendingAttributeName);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertEquals("", tag.pendingAttributeValue.toString());
        assertNull(tag.pendingAttributeValueS);
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag tag = new Token.StartTag();
        assertEquals("<>", tag.toString());

        tag.nameAttr("div", null);
        assertEquals("<div>", tag.toString());

        Attributes attributes = new Attributes();
        attributes.put("class", "container");
        tag.nameAttr("div", attributes);
        assertEquals("<div class=\"container\">", tag.toString());
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag tag = new Token.EndTag();
        assertEquals("</>", tag.toString());

        tag.name("div");
        assertEquals("</div>", tag.toString());
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character();
        character.data("This is a character");
        assertEquals("This is a character", character.getData());
    }

    @Test
    public void testCDataToString() {
        Token.CData cdata = new Token.CData("This is a CDATA section");
        assertEquals("<![CDATA[This is a CDATA section]]>", cdata.toString());

        Token.Character character = new Token.Character();
        character.data("This is a CDATA section");
        assertEquals("This is a CDATA section", character.toString());
    }

    @Test
    public void testTokenIsMethods() {
        Token token = new Token.Doctype();
        assertTrue(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isCData());
        assertFalse(token.isEOF());

        token = new Token.StartTag();
        assertFalse(token.isDoctype());
        assertTrue(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isCData());
        assertFalse(token.isEOF());

        token = new Token.EndTag();
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertTrue(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isCData());
        assertFalse(token.isEOF());

        token = new Token.Comment();
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertTrue(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isCData());
        assertFalse(token.isEOF());

        token = new Token.Character();
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertTrue(token.isCharacter());
        assertFalse(token.isCData());
        assertFalse(token.isEOF());

        token = new Token.CData("");
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertTrue(token.isCharacter()); // CData is treated as an extension of Character token type
        assertTrue(token.isCData());
        assertFalse(token.isEOF());

        token = new Token.EOF();
        assertFalse(token.isDoctype());
        assertFalse(token.isStartTag());
        assertFalse(token.isEndTag());
        assertFalse(token.isComment());
        assertFalse(token.isCharacter());
        assertFalse(token.isCData());
        assertTrue(token.isEOF());
    }
}