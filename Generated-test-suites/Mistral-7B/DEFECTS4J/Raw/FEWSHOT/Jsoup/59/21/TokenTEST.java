package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @Test
    public void testDoctype() {
        Token doctype = new Token.Doctype();
        Token resetDoctype = doctype.reset();
        assertEquals(TokenType.Doctype, resetDoctype.type);

        doctype.name.append("html");
        assertEquals("html", doctype.getName());

        doctype.pubSysKey = "pubSysKey";
        assertEquals("pubSysKey", doctype.getPubSysKey());

        doctype.publicIdentifier.append("publicIdentifier");
        assertEquals("publicIdentifier", doctype.getPublicIdentifier());

        doctype.systemIdentifier.append("systemIdentifier");
        assertEquals("systemIdentifier", doctype.getSystemIdentifier());

        doctype.forceQuirks = true;
        assertTrue(doctype.isForceQuirks());

        resetDoctype = doctype.reset();
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTag() {
        Token startTag = new Token.StartTag();
        Token resetStartTag = startTag.reset();
        assertEquals(TokenType.StartTag, resetStartTag.type);

        startTag.name.append("div");
        assertEquals("div", startTag.name());

        startTag.name("p");
        assertEquals("p", startTag.normalName());

        startTag.attributes = new org.jsoup.nodes.Attributes();
        startTag.attributes.put("class", "class1 class2");
        assertEquals("class1 class2", startTag.getAttributes().toString());

        resetStartTag = startTag.reset();
        assertEquals(0, resetStartTag.getAttributes().size());

        startTag.newAttribute();
        startTag.appendAttributeName("id");
        startTag.appendAttributeValue("idValue");
        assertEquals("idValue", startTag.getAttributes().get("id").getValue());

        resetStartTag = startTag.reset();
        assertEquals(0, resetStartTag.getAttributes().size());

        startTag.selfClosing = true;
        assertTrue(startTag.isSelfClosing());

        resetStartTag = startTag.reset();
        assertFalse(startTag.isSelfClosing());
    }

    @Test
    public void testEndTag() {
        Token endTag = new Token.EndTag();
        Token resetEndTag = endTag.reset();
        assertEquals(TokenType.EndTag, resetEndTag.type);

        endTag.name.append("p");
        assertEquals("p", endTag.name());

        resetEndTag = endTag.reset();
        assertEquals(null, resetEndTag.name());

        endTag.finaliseTag();
        assertNull(endTag.name());
    }

    @Test
    public void testComment() {
        Token comment = new Token.Comment();
        Token resetComment = comment.reset();
        assertEquals(TokenType.Comment, resetComment.type);

        comment.data.append("This is a comment.");
        assertEquals("This is a comment.", comment.getData());

        resetComment = comment.reset();
        assertNull(comment.getData());

        comment.bogus = true;
        assertTrue(comment.isBogus());

        resetComment = comment.reset();
        assertFalse(comment.isBogus());
    }

    @Test
    public void testCharacter() {
        Token character = new Token.Character();
        Token resetCharacter = character.reset();
        assertEquals(TokenType.Character, resetCharacter.type);

        character.data = "ABCDEF";
        assertEquals("ABCDEF", character.getData());

        resetCharacter = character.reset();
        assertNull(character.getData());
    }

    @Test
    public void testEOF() {
        Token eof = new Token.EOF();
        Token resetEof = eof.reset();
        assertEquals(TokenType.EOF, resetEof.type);

        resetEof = eof.reset();
        assertEquals(TokenType.EOF, resetEof.type);
    }
}