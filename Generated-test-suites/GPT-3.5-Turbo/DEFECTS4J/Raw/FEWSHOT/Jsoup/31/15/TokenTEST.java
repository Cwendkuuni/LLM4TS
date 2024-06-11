
package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {
    @Test
    public void testTokenType() {
        Token.Doctype doctype = new Token.Doctype();
        assertEquals("Doctype", doctype.tokenType());

        Token.StartTag startTag = new Token.StartTag();
        assertEquals("StartTag", startTag.tokenType());

        Token.EndTag endTag = new Token.EndTag();
        assertEquals("EndTag", endTag.tokenType());

        Token.Comment comment = new Token.Comment();
        assertEquals("Comment", comment.tokenType());

        Token.Character character = new Token.Character("");
        assertEquals("Character", character.tokenType());

        Token.EOF eof = new Token.EOF();
        assertEquals("EOF", eof.tokenType());
    }

    @Test
    public void testDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        
        assertFalse(doctype.isForceQuirks());

        doctype.forceQuirks = true;
        assertTrue(doctype.isForceQuirks());

        assertEquals("", doctype.getSystemIdentifier());

        doctype.systemIdentifier.append("SYSTEM");
        assertEquals("SYSTEM", doctype.getSystemIdentifier());

        assertEquals("", doctype.getPublicIdentifier());

        doctype.publicIdentifier.append("PUBLIC");
        assertEquals("PUBLIC", doctype.getPublicIdentifier());

        assertEquals("", doctype.getName());

        doctype.name.append("DOCTYPE");
        assertEquals("DOCTYPE", doctype.getName());
    }

    @Test
    public void testStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        
        assertFalse(startTag.isSelfClosing());

        assertNull(startTag.getAttributes());

        startTag.tagName = "tag";
        assertEquals("tag", startTag.name());

        startTag.selfClosing = true;
        assertTrue(startTag.isSelfClosing());

        Attributes attributes = new Attributes();
        attributes.put(new Attribute("attr1", "value1"));
        attributes.put(new Attribute("attr2", "value2"));
        attributes.put(new Attribute("attr3", "value3"));

        startTag.attributes = attributes;

        assertEquals(attributes, startTag.getAttributes());
    }

    @Test
    public void testEndTag() {
        Token.EndTag endTag = new Token.EndTag();

        assertEquals("", endTag.name());

        endTag.tagName = "tag";
        assertEquals("tag", endTag.name());
    }

    @Test
    public void testComment() {
        Token.Comment comment = new Token.Comment();

        assertEquals("", comment.getData());

        comment.data.append("This is a comment");
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCharacter() {
        Token.Character character = new Token.Character("");

        assertEquals("", character.getData());

        character.data = "This is a character";
        assertEquals("This is a character", character.getData());
    }

    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();
    }
}
