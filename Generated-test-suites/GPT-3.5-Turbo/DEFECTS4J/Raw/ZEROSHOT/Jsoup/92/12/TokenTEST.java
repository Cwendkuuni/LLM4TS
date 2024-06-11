package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token.Doctype doctypeToken = new Token.Doctype();
        assertEquals(Token.TokenType.Doctype, doctypeToken.type);

        Token.StartTag startTagToken = new Token.StartTag();
        assertEquals(Token.TokenType.StartTag, startTagToken.type);

        Token.EndTag endTagToken = new Token.EndTag();
        assertEquals(Token.TokenType.EndTag, endTagToken.type);

        Token.Comment commentToken = new Token.Comment();
        assertEquals(Token.TokenType.Comment, commentToken.type);

        Token.Character characterToken = new Token.Character();
        assertEquals(Token.TokenType.Character, characterToken.type);

        Token.EOF eofToken = new Token.EOF();
        assertEquals(Token.TokenType.EOF, eofToken.type);
    }

    @Test
    public void testTokenReset() {
        Token.Doctype doctypeToken = new Token.Doctype();
        doctypeToken.forceQuirks = true;
        doctypeToken.name.append("html");
        doctypeToken.pubSysKey = "pub";
        doctypeToken.publicIdentifier.append("public");
        doctypeToken.systemIdentifier.append("system");

        doctypeToken.reset();

        assertEquals("", doctypeToken.name.toString());
        assertNull(doctypeToken.pubSysKey);
        assertEquals("", doctypeToken.publicIdentifier.toString());
        assertEquals("", doctypeToken.systemIdentifier.toString());
        assertFalse(doctypeToken.forceQuirks);
    }

    @Test
    public void testDoctypeGetters() {
        Token.Doctype doctypeToken = new Token.Doctype();
        doctypeToken.name.append("html");
        doctypeToken.pubSysKey = "pub";
        doctypeToken.publicIdentifier.append("public");
        doctypeToken.systemIdentifier.append("system");
        doctypeToken.forceQuirks = true;

        assertEquals("html", doctypeToken.getName());
        assertEquals("pub", doctypeToken.getPubSysKey());
        assertEquals("public", doctypeToken.getPublicIdentifier());
        assertEquals("system", doctypeToken.getSystemIdentifier());
        assertTrue(doctypeToken.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token.StartTag startTagToken = new Token.StartTag();
        startTagToken.tagName = "div";
        startTagToken.selfClosing = true;
        startTagToken.attributes = new Attributes();
        startTagToken.attributes.put("id", "myDiv");

        startTagToken.reset();

        assertNull(startTagToken.tagName);
        assertFalse(startTagToken.selfClosing);
        assertNull(startTagToken.attributes);
    }

    @Test
    public void testTagFinalise() {
        Token.StartTag startTagToken = new Token.StartTag();
        startTagToken.tagName = "div";
        startTagToken.pendingAttributeName = "id";
        startTagToken.pendingAttributeValue.append("myDiv");
        startTagToken.hasPendingAttributeValue = true;

        startTagToken.finaliseTag();

        assertNull(startTagToken.pendingAttributeName);
        assertNull(startTagToken.pendingAttributeValue);
        assertFalse(startTagToken.hasPendingAttributeValue);
    }

    @Test
    public void testTagAttributes() {
        Token.StartTag startTagToken = new Token.StartTag();
        
        startTagToken.newAttribute();
        startTagToken.pendingAttributeName = "id";
        startTagToken.pendingAttributeValue.append("myDiv");
        startTagToken.pendingAttributeValueS = null;
        startTagToken.hasPendingAttributeValue = true;
        startTagToken.finaliseTag();

        Attributes attributes = startTagToken.getAttributes();
        assertNotNull(attributes);
        assertEquals(1, attributes.size());
        assertTrue(attributes.hasKey("id"));
        assertEquals("myDiv", attributes.get("id"));

        startTagToken.newAttribute();
        startTagToken.pendingAttributeName = "class";
        startTagToken.pendingAttributeValue.append("myClass");
        startTagToken.pendingAttributeValueS = null;
        startTagToken.hasPendingAttributeValue = true;
        startTagToken.finaliseTag();

        attributes = startTagToken.getAttributes();
        assertNotNull(attributes);
        assertEquals(2, attributes.size());
        assertTrue(attributes.hasKey("class"));
        assertEquals("myClass", attributes.get("class"));

        startTagToken.newAttribute();
        startTagToken.pendingAttributeName = "data-value";
        startTagToken.pendingAttributeValue.append("123");
        startTagToken.pendingAttributeValueS = null;
        startTagToken.hasPendingAttributeValue = true;
        startTagToken.finaliseTag();

        attributes = startTagToken.getAttributes();
        assertNotNull(attributes);
        assertEquals(3, attributes.size());
        assertTrue(attributes.hasKey("data-value"));
        assertEquals("123", attributes.get("data-value"));
    }

    @Test
    public void testEndTagName() {
        Token.EndTag endTagToken = new Token.EndTag();
        endTagToken.tagName = "p";

        assertEquals("p", endTagToken.name());
        assertEquals("p", endTagToken.normalName());
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTagToken = new Token.EndTag();
        endTagToken.tagName = "p";
        
        assertEquals("</p>", endTagToken.toString());
    }

    @Test
    public void testCommentReset() {
        Token.Comment commentToken = new Token.Comment();
        commentToken.data.append("Hello");
        commentToken.bogus = true;

        commentToken.reset();

        assertEquals("", commentToken.data.toString());
        assertFalse(commentToken.bogus);
    }

    @Test
    public void testCommentGetData() {
        Token.Comment commentToken = new Token.Comment();
        commentToken.data.append("Hello");

        assertEquals("Hello", commentToken.getData());
    }

    @Test
    public void testCommentToString() {
        Token.Comment commentToken = new Token.Comment();
        commentToken.data.append("Hello");

        assertEquals("<!--Hello-->", commentToken.toString());
    }

    @Test
    public void testCharacterReset() {
        Token.Character characterToken = new Token.Character();
        characterToken.data = "abc";

        characterToken.reset();

        assertNull(characterToken.data);
    }

    @Test
    public void testCharacterGetData() {
        Token.Character characterToken = new Token.Character();
        characterToken.data = "abc";

        assertEquals("abc", characterToken.getData());
    }

    @Test
    public void testCharacterToString() {
        Token.Character characterToken = new Token.Character();
        characterToken.data = "abc";

        assertEquals("abc", characterToken.toString());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eofToken = new Token.EOF();

        assertEquals(eofToken, eofToken.reset());
    }

    @Test
    public void testDoctypeType() {
        Token.Doctype doctypeToken = new Token.Doctype();
        assertTrue(doctypeToken.isDoctype());
        assertFalse(doctypeToken.isStartTag());
        assertFalse(doctypeToken.isEndTag());
        assertFalse(doctypeToken.isComment());
        assertFalse(doctypeToken.isCharacter());
        assertFalse(doctypeToken.isEOF());
    }

    @Test
    public void testStartTagType() {
        Token.StartTag startTagToken = new Token.StartTag();
        assertFalse(startTagToken.isDoctype());
        assertTrue(startTagToken.isStartTag());
        assertFalse(startTagToken.isEndTag());
        assertFalse(startTagToken.isComment());
        assertFalse(startTagToken.isCharacter());
        assertFalse(startTagToken.isEOF());
    }

    @Test
    public void testEndTagType() {
        Token.EndTag endTagToken = new Token.EndTag();
        assertFalse(endTagToken.isDoctype());
        assertFalse(endTagToken.isStartTag());
        assertTrue(endTagToken.isEndTag());
        assertFalse(endTagToken.isComment());
        assertFalse(endTagToken.isCharacter());
        assertFalse(endTagToken.isEOF());
    }

    @Test
    public void testCommentType() {
        Token.Comment commentToken = new Token.Comment();
        assertFalse(commentToken.isDoctype());
        assertFalse(commentToken.isStartTag());
        assertFalse(commentToken.isEndTag());
        assertTrue(commentToken.isComment());
        assertFalse(commentToken.isCharacter());
        assertFalse(commentToken.isEOF());
    }

    @Test
    public void testCharacterType() {
        Token.Character characterToken = new Token.Character();
        assertFalse(characterToken.isDoctype());
        assertFalse(characterToken.isStartTag());
        assertFalse(characterToken.isEndTag());
        assertFalse(characterToken.isComment());
        assertTrue(characterToken.isCharacter());
        assertFalse(characterToken.isEOF());
    }

    @Test
    public void testCDataType() {
        Token.CData cdataToken = new Token.CData("abc");
        assertFalse(cdataToken.isDoctype());
        assertFalse(cdataToken.isStartTag());
        assertFalse(cdataToken.isEndTag());
        assertFalse(cdataToken.isComment());
        assertTrue(cdataToken.isCharacter());
        assertFalse(cdataToken.isEOF());
    }

    @Test
    public void testEOFType() {
        Token.EOF eofToken = new Token.EOF();
        assertFalse(eofToken.isDoctype());
        assertFalse(eofToken.isStartTag());
        assertFalse(eofToken.isEndTag());
        assertFalse(eofToken.isComment());
        assertFalse(eofToken.isCharacter());
        assertTrue(eofToken.isEOF());
    }
}