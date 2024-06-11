package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("Test");
        doctype.reset();
        
        assertEquals("", doctype.getName());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTag() {
        Token.Tag startTag = new Token.StartTag();
        startTag.name("div");
        startTag.appendAttributeValue("id");
        startTag.appendAttributeValue("content");
        startTag.newAttribute();
        
        assertNotNull(startTag.name());
        assertFalse(startTag.isSelfClosing());
        assertNotNull(startTag.getAttributes());
    }

    @Test
    public void testEndTag() {
        Token.Tag endTag = new Token.EndTag();
        endTag.name("div");

        assertEquals("</div>", endTag.toString());
    }

    @Test
    public void testComment() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("Test Comment");

        assertEquals("Test Comment", comment.getData());
    }

    @Test
    public void testCharacter() {
        Token.Character character = new Token.Character();
        character.data("Test Character");

        assertEquals("Test Character", character.getData());
    }

    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();

        assertTrue(eof.isEOF());
    }

}