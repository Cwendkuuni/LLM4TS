package org.jsoup.parser;

import org.junit.*;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
    }

    @Test
    public void testResetMethod() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.reset();
        assertNull(doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testDoctypeMethods() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "-//W3C//DTD HTML 4.01 Transitional//EN";
        doctype.publicIdentifier.append("http://www.w3.org/TR/html4/loose.dtd");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/loose.dtd");
        doctype.forceQuirks = true;
        assertEquals("html", doctype.getName());
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", doctype.getPubSysKey());
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", doctype.getPublicIdentifier());
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagMethods() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.nameAttr("a", null);
        assertEquals("a", startTag.name());
        assertFalse(startTag.isSelfClosing());
    }

    @Test
    public void testEndTagMethods() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("a");
        assertEquals("a", endTag.name());
        assertFalse(endTag.isSelfClosing());
    }

    @Test
    public void testCommentMethods() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("test comment");
        assertFalse(comment.bogus);
        assertEquals("test comment", comment.getData());
    }

    @Test
    public void testCharacterMethods() {
        Token.Character character = new Token.Character();
        character.data("test data");
        assertEquals("test data", character.getData());
    }

    @Test
    public void testEOFMethods() {
        Token.EOF eof = new Token.EOF();
        assertNotNull(eof);
    }
}