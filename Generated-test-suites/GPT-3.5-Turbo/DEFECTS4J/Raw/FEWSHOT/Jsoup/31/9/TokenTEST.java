package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

public class TokenTest {
    // Doctype tests
    @Test
    public void testDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        assertFalse(doctype.isForceQuirks());
        
        assertEquals("html", doctype.getName());
        assertEquals("-//W3C//DTD HTML 4.01//EN", doctype.getPublicIdentifier());
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
        
        doctype.forceQuirks = true;
        assertTrue(doctype.isForceQuirks());
    }

    // StartTag tests
    @Test
    public void testStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        assertTrue(startTag.isStartTag());
        assertFalse(startTag.isEndTag());
        assertFalse(startTag.isDoctype());
        assertFalse(startTag.isComment());
        assertFalse(startTag.isCharacter());
        assertFalse(startTag.isEOF());
        assertEquals("div", startTag.name());
        assertFalse(startTag.isSelfClosing());

        startTag.selfClosing = true;
        assertTrue(startTag.isSelfClosing());
    }

    @Test
    public void testStartTagWithAttributes() {
        Token.StartTag startTag = new Token.StartTag("div");
        startTag.attributes = new Attributes();
        startTag.attributes.put("id", "myDiv");
        startTag.attributes.put("class", "container");
        assertEquals("div", startTag.name());
        assertEquals(2, startTag.getAttributes().size());
        
        Attribute idAttr = startTag.getAttributes().get("id");
        assertEquals("id", idAttr.getKey());
        assertEquals("myDiv", idAttr.getValue());
        
        Attribute classAttr = startTag.getAttributes().get("class");
        assertEquals("class", classAttr.getKey());
        assertEquals("container", classAttr.getValue());
    }

    // EndTag tests
    @Test
    public void testEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("div");
        assertFalse(endTag.isStartTag());
        assertTrue(endTag.isEndTag());
        assertFalse(endTag.isDoctype());
        assertFalse(endTag.isComment());
        assertFalse(endTag.isCharacter());
        assertFalse(endTag.isEOF());
        assertEquals("div", endTag.name());
    }

    // Comment tests
    @Test
    public void testComment() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        assertTrue(comment.isComment());
        assertEquals("This is a comment", comment.getData());
    }

    // Character tests
    @Test
    public void testCharacter() {
        Token.Character character = new Token.Character("a");
        assertTrue(character.isCharacter());
        assertEquals("a", character.getData());
    }

    // EOF tests
    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();
        assertTrue(eof.isEOF());
    }

    // Other methods test
    @Test
    public void testMethods() {
        Token token = new Token() {};
        assertEquals("Token", token.tokenType());
    }
}