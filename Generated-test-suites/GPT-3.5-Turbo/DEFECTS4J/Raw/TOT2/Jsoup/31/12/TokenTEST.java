
package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token.Doctype doctype = new Token.Doctype();
        assertNotNull(doctype);
        assertFalse(doctype.isForceQuirks());
        assertEquals("", doctype.getName());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
    }

    @Test
    public void testStartTagToken() {
        Token.StartTag startTag = new Token.StartTag();
        assertNotNull(startTag);
        assertFalse(startTag.isSelfClosing());
        assertEquals("", startTag.name());
        assertNotNull(startTag.getAttributes());
        assertEquals("<>", startTag.toString());
    }

    @Test
    public void testEndTagToken() {
        Token.EndTag endTag = new Token.EndTag();
        assertNotNull(endTag);
        assertEquals("", endTag.name());
        assertEquals("</>", endTag.toString());
    }

    @Test
    public void testCommentToken() {
        Token.Comment comment = new Token.Comment();
        assertNotNull(comment);
        assertEquals("", comment.getData());
        assertEquals("<!-- -->", comment.toString());
    }

    @Test
    public void testCharacterToken() {
        Token.Character character = new Token.Character("test");
        assertNotNull(character);
        assertEquals("test", character.getData());
        assertEquals("test", character.toString());
    }

    @Test
    public void testEOFToken() {
        Token.EOF eof = new Token.EOF();
        assertNotNull(eof);
        assertTrue(eof.isEOF());
    }

    @Test
    public void testTokenIsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        assertTrue(doctype.isDoctype());
    }
}
