
package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

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
        doctype.name.append("html");
        doctype.publicIdentifier.append("//publicId");
        doctype.systemIdentifier.append("//systemId");
        doctype.forceQuirks = true;

        assertEquals("html", doctype.getName());
        assertEquals("//publicId", doctype.getPublicIdentifier());
        assertEquals("//systemId", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name = "div";
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();
        startTag.attributes.put("id", "myId");
        startTag.attributes.put("class", "myClass");

        assertEquals("div", startTag.name());
        assertTrue(startTag.isSelfClosing());
        assertEquals("myId", startTag.getAttributes().get("id"));
        assertEquals("myClass", startTag.getAttributes().get("class"));
    }

    @Test
    public void testEndTag() {
        Token.EndTag endTag = new Token.EndTag("div");
        assertEquals("div", endTag.name());
    }

    @Test
    public void testComment() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");

        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCharacter() {
        Token.Character character = new Token.Character("This is a character");
        assertEquals("This is a character", character.getData());
    }

    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();
        assertTrue(eof.isEOF());
    }

    @Test
    public void testIsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        assertTrue(doctype.isDoctype());

        Token.StartTag startTag = new Token.StartTag();
        assertFalse(startTag.isDoctype());

        Token.EndTag endTag = new Token.EndTag();
        assertFalse(endTag.isDoctype());

        Token.Comment comment = new Token.Comment();
        assertFalse(comment.isDoctype());

        Token.Character character = new Token.Character("");
        assertFalse(character.isDoctype());

        Token.EOF eof = new Token.EOF();
        assertFalse(eof.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        Doctype actualDoctype = doctype.asDoctype();
        assertEquals(doctype, actualDoctype);

        Token.StartTag startTag = new Token.StartTag();
        assertNull(startTag.asDoctype());

        Token.EndTag endTag = new Token.EndTag();
        assertNull(endTag.asDoctype());

        Token.Comment comment = new Token.Comment();
        assertNull(comment.asDoctype());

        Token.Character character = new Token.Character("");
        assertNull(character.asDoctype());

        Token.EOF eof = new Token.EOF();
        assertNull(eof.asDoctype());
    }

    @Test
    public void testIsStartTag() {
        Token.Doctype doctype = new Token.Doctype();
        assertFalse(doctype.isStartTag());

        Token.StartTag startTag = new Token.StartTag();
        assertTrue(startTag.isStartTag());

        Token.EndTag endTag = new Token.EndTag();
        assertFalse(endTag.isStartTag());

        Token.Comment comment = new Token.Comment();
        assertFalse(comment.isStartTag());

        Token.Character character = new Token.Character("");
        assertFalse(character.isStartTag());

        Token.EOF eof = new Token.EOF();
        assertFalse(eof.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        Token.Doctype doctype = new Token.Doctype();
        assertNull(doctype.asStartTag());

        Token.StartTag startTag = new Token.StartTag();
        StartTag actualStartTag = startTag.asStartTag();
        assertEquals(startTag, actualStartTag);

        Token.EndTag endTag = new Token.EndTag();
        assertNull(endTag.asStartTag());

        Token.Comment comment = new Token.Comment();
        assertNull(comment.asStartTag());

        Token.Character character = new Token.Character("");
        assertNull(character.asStartTag());

        Token.EOF eof = new Token.EOF();
        assertNull(eof.asStartTag());
    }

    @Test
    public void testIsEndTag() {
        Token.Doctype doctype = new Token.Doctype();
        assertFalse(doctype.isEndTag());

        Token.StartTag startTag = new Token.StartTag();
        assertFalse(startTag.isEndTag());

        Token.EndTag endTag = new Token.EndTag();
        assertTrue(endTag.isEndTag());

        Token.Comment comment = new Token.Comment();
        assertFalse(comment.isEndTag());

        Token.Character character = new Token.Character("");
        assertFalse(character.isEndTag());

        Token.EOF eof = new Token.EOF();
        assertFalse(eof.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        Token.Doctype doctype = new Token.Doctype();
        assertNull(doctype.asEndTag());

        Token.StartTag startTag = new Token.StartTag();
        assertNull(startTag.asEndTag());

        Token.EndTag endTag = new Token.EndTag();
        EndTag actualEndTag = endTag.asEndTag();
        assertEquals(endTag, actualEndTag);

        Token.Comment comment = new Token.Comment();
        assertNull(comment.asEndTag());

        Token.Character character = new Token.Character("");
        assertNull(character.asEndTag());

        Token.EOF eof = new Token.EOF();
        assertNull(eof.asEndTag());
    }

    @Test
    public void testIsComment() {
        Token.Doctype doctype = new Token.Doctype();
        assertFalse(doctype.isComment());

        Token.StartTag startTag = new Token.StartTag();
        assertFalse(startTag.isComment());

        Token.EndTag endTag = new Token.EndTag();
        assertFalse(endTag.isComment());

        Token.Comment comment = new Token.Comment();
        assertTrue(comment.isComment());

        Token.Character character = new Token.Character("");
        assertFalse(character.isComment());

        Token.EOF eof = new Token.EOF();
        assertFalse(eof.isComment());
    }

    @Test
    public void testAsComment() {
        Token.Doctype doctype = new Token.Doctype();
        assertNull(doctype.asComment());

        Token.StartTag startTag = new Token.StartTag();
        assertNull(startTag.asComment());

        Token.EndTag endTag = new Token.EndTag();
        assertNull(endTag.asComment());

        Token.Comment comment = new Token.Comment();
        Comment actualComment = comment.asComment();
        assertEquals(comment, actualComment);

        Token.Character character = new Token.Character("");
        assertNull(character.asComment());

        Token.EOF eof = new Token.EOF();
        assertNull(eof.asComment());
    }

    @Test
    public void testIsCharacter() {
        Token.Doctype doctype = new Token.Doctype();
        assertFalse(doctype.isCharacter());

        Token.StartTag startTag = new Token.StartTag();
        assertFalse(startTag.isCharacter());

        Token.EndTag endTag = new Token.EndTag();
        assertFalse(endTag.isCharacter());

        Token.Comment comment = new Token.Comment();
        assertFalse(comment.isCharacter());

        Token.Character character = new Token.Character("");
        assertTrue(character.isCharacter());

        Token.EOF eof = new Token.EOF();
        assertFalse(eof.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Token.Doctype doctype = new Token.Doctype();
        assertNull(doctype.asCharacter());

        Token.StartTag startTag = new Token.StartTag();
        assertNull(startTag.asCharacter());

        Token.EndTag endTag = new Token.EndTag();
        assertNull(endTag.asCharacter());

        Token.Comment comment = new Token.Comment();
        assertNull(comment.asCharacter());

        Token.Character character = new Token.Character("");
        Character actualCharacter = character.asCharacter();
        assertEquals(character, actualCharacter);

        Token.EOF eof = new Token.EOF();
        assertNull(eof.asCharacter());
    }

    @Test
    public void testIsEOF() {
        Token.Doctype doctype = new Token.Doctype();
        assertFalse(doctype.isEOF());

        Token.StartTag startTag = new Token.StartTag();
        assertFalse(startTag.isEOF());

        Token.EndTag endTag = new Token.EndTag();
        assertFalse(endTag.isEOF());

        Token.Comment comment = new Token.Comment();
        assertFalse(comment.isEOF());

        Token.Character character = new Token.Character("");
        assertFalse(character.isEOF());

        Token.EOF eof = new Token.EOF();
        assertTrue(eof.isEOF());
    }
}
