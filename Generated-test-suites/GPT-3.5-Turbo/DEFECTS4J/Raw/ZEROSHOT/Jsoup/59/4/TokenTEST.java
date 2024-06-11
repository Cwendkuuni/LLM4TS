package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    private Token.Doctype doctype;
    private Token.StartTag startTag;
    private Token.EndTag endTag;
    private Token.Comment comment;
    private Token.Character character;
    private Token.EOF eof;

    @Before
    public void setUp() {
        doctype = new Token.Doctype();
        startTag = new Token.StartTag();
        endTag = new Token.EndTag();
        comment = new Token.Comment();
        character = new Token.Character();
        eof = new Token.EOF();
    }

    @Test
    public void testDoctypeReset() {
        doctype.reset();
        assertEquals("", doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        startTag.reset();
        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
        assertNull(startTag.attributes);
    }

    @Test
    public void testEndTagReset() {
        endTag.reset();
        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
        assertNull(endTag.pendingAttributeName);
        assertEquals("", endTag.pendingAttributeValue.toString());
        assertNull(endTag.pendingAttributeValueS);
        assertFalse(endTag.hasEmptyAttributeValue);
        assertFalse(endTag.hasPendingAttributeValue);
        assertFalse(endTag.selfClosing);
        assertNull(endTag.attributes);
    }

    @Test
    public void testCommentReset() {
        comment.reset();
        assertEquals("", comment.getData());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        character.reset();
        assertNull(character.getData());
    }

    @Test
    public void testDoctypeGetType() {
        assertEquals(Token.TokenType.Doctype, doctype.type);
    }

    @Test
    public void testStartTagGetType() {
        assertEquals(Token.TokenType.StartTag, startTag.type);
    }

    @Test
    public void testEndTagGetType() {
        assertEquals(Token.TokenType.EndTag, endTag.type);
    }

    @Test
    public void testCommentGetType() {
        assertEquals(Token.TokenType.Comment, comment.type);
    }

    @Test
    public void testCharacterGetType() {
        assertEquals(Token.TokenType.Character, character.type);
    }

    @Test
    public void testEOFGetType() {
        assertEquals(Token.TokenType.EOF, eof.type);
    }

    @Test
    public void testDoctypeSetName() {
        doctype.name.append("name");
        assertEquals("name", doctype.getName());
    }

    @Test
    public void testDoctypeSetPubSysKey() {
        doctype.pubSysKey = "pubSysKey";
        assertEquals("pubSysKey", doctype.getPubSysKey());
    }

    @Test
    public void testDoctypeSetPublicIdentifier() {
        doctype.publicIdentifier.append("publicIdentifier");
        assertEquals("publicIdentifier", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeSetSystemIdentifier() {
        doctype.systemIdentifier.append("systemIdentifier");
        assertEquals("systemIdentifier", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeSetForceQuirks() {
        doctype.forceQuirks = true;
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagName() {
        startTag.tagName = "tagName";
        assertEquals("tagName", startTag.name());
    }

    @Test
    public void testStartTagIsSelfClosing() {
        startTag.selfClosing = true;
        assertTrue(startTag.isSelfClosing());
    }

    @Test
    public void testCharacterData() {
        character.data = "data";
        assertEquals("data", character.getData());
    }

    @Test
    public void testDoctypeResetString() {
        StringBuilder sb = new StringBuilder("name");
        Token.reset(sb);
        assertEquals("", sb.toString());
    }

}