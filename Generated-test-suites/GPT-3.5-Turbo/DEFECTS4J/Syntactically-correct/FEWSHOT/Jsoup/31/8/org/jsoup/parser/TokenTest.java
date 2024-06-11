package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

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

        Token.Character character = new Token.Character("abc");
        assertEquals("Character", character.tokenType());

        Token.EOF eof = new Token.EOF();
        assertEquals("EOF", eof.tokenType());
    }

    @Test
    public void testDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        assertTrue(doctype.isDoctype());

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
    public void testStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        assertTrue(startTag.isStartTag());

        startTag.tagName = "div";
        assertEquals("div", startTag.name());

        startTag.selfClosing = true;
        assertTrue(startTag.isSelfClosing());

        startTag.newAttribute();
        assertTrue(startTag.attributes != null);

        startTag.appendTagName("span");
        assertEquals("divspan", startTag.name());

        startTag.appendTagName('i');
        assertEquals("divspani", startTag.name());

        startTag.appendAttributeName("class");
        assertEquals("class", startTag.pendingAttributeName);

        startTag.appendAttributeName("id");
        assertEquals("classid", startTag.pendingAttributeName);

        startTag.appendAttributeValue("main");
        assertEquals("main", startTag.pendingAttributeValue.toString());

        startTag.appendAttributeValue("user");
        assertEquals("mainuser", startTag.pendingAttributeValue.toString());

        startTag.finaliseTag();
        assertTrue(startTag.attributes != null);
    }

    @Test
    public void testEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        assertTrue(endTag.isEndTag());

        endTag.tagName = "span";
        assertEquals("span", endTag.name());
    }

    @Test
    public void testComment() {
        Token.Comment comment = new Token.Comment();
        assertTrue(comment.isComment());

        comment.data.append("This is a comment.");
        assertEquals("This is a comment.", comment.getData());
    }

    @Test
    public void testCharacter() {
        Token.Character character = new Token.Character("abc");
        assertTrue(character.isCharacter());

        assertEquals("abc", character.getData());
    }

    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();
        assertTrue(eof.isEOF());
    }
}