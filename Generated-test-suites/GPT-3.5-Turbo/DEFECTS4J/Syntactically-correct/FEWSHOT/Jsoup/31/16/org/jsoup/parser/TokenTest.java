
package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());

        token = new Token.StartTag();
        assertEquals("StartTag", token.tokenType());

        token = new Token.EndTag();
        assertEquals("EndTag", token.tokenType());

        token = new Token.Comment();
        assertEquals("Comment", token.tokenType());

        token = new Token.Character("");
        assertEquals("Character", token.tokenType());

        token = new Token.EOF();
        assertEquals("EOF", token.tokenType());
    }

    @Test
    public void testDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        assertEquals("html", doctype.getName());

        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01//EN");
        assertEquals("-//W3C//DTD HTML 4.01//EN", doctype.getPublicIdentifier());

        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.getSystemIdentifier());

        assertFalse(doctype.isForceQuirks());

        doctype.forceQuirks = true;
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTag() {
        Token.Tag tag = new Token.StartTag();
        tag.name = "div";
        assertEquals("div", tag.name());

        assertTrue(tag.attributes == null);

        tag.selfClosing = true;
        assertTrue(tag.isSelfClosing());

        Attributes attrs = new Attributes();
        attrs.put(new Attribute("id", "sample"));
        tag.attributes = attrs;

        assertEquals(attrs, tag.getAttributes());
    }

    @Test
    public void testStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        assertEquals("<>", startTag.toString());

        startTag.name("div");
        assertEquals("<div>", startTag.toString());

        Attributes attrs = new Attributes();
        attrs.put(new Attribute("id", "sample"));
        startTag.attributes = attrs;
        assertEquals("<div id=\"sample\">", startTag.toString());
    }

    @Test
    public void testEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        assertEquals("</>", endTag.toString());

        endTag.name("div");
        assertEquals("</div>", endTag.toString());
    }

    @Test
    public void testComment() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        assertEquals("This is a comment", comment.getData());

        assertEquals("<!--This is a comment-->", comment.toString());
    }

    @Test
    public void testCharacter() {
        Token.Character character = new Token.Character("A");
        assertEquals("A", character.getData());

        assertEquals("A", character.toString());
    }

    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();
        assertEquals(Token.TokenType.EOF, eof.type);
    }

    @Test
    public void testIsMethods() {
        Token.Doctype doctype = new Token.Doctype();
        assertTrue(doctype.isDoctype());
        assertFalse(doctype.isStartTag());
        assertFalse(doctype.isEndTag());
        assertFalse(doctype.isComment());
        assertFalse(doctype.isCharacter());
        assertFalse(doctype.isEOF());

        Token.StartTag startTag = new Token.StartTag();
        assertFalse(startTag.isDoctype());
        assertTrue(startTag.isStartTag());
        assertFalse(startTag.isEndTag());
        assertFalse(startTag.isComment());
        assertFalse(startTag.isCharacter());
        assertFalse(startTag.isEOF());

        Token.EndTag endTag = new Token.EndTag();
        assertFalse(endTag.isDoctype());
        assertFalse(endTag.isStartTag());
        assertTrue(endTag.isEndTag());
        assertFalse(endTag.isComment());
        assertFalse(endTag.isCharacter());
        assertFalse(endTag.isEOF());

        Token.Comment comment = new Token.Comment();
        assertFalse(comment.isDoctype());
        assertFalse(comment.isStartTag());
        assertFalse(comment.isEndTag());
        assertTrue(comment.isComment());
        assertFalse(comment.isCharacter());
        assertFalse(comment.isEOF());

        Token.Character character = new Token.Character("");
        assertFalse(character.isDoctype());
        assertFalse(character.isStartTag());
        assertFalse(character.isEndTag());
        assertFalse(character.isComment());
        assertTrue(character.isCharacter());
        assertFalse(character.isEOF());

        Token.EOF eof = new Token.EOF();
        assertFalse(eof.isDoctype());
        assertFalse(eof.isStartTag());
        assertFalse(eof.isEndTag());
        assertFalse(eof.isComment());
        assertFalse(eof.isCharacter());
        assertTrue(eof.isEOF());
    }
}
