
package org.jsoup.parser;

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
        assertEquals("", doctype.getName());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());

        doctype.name.append("html");
        assertEquals("html", doctype.getName());

        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.0//EN");
        assertEquals("-//W3C//DTD HTML 4.0//EN", doctype.getPublicIdentifier());

        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.getSystemIdentifier());

        doctype.forceQuirks = true;
        assertTrue(doctype.isForceQuirks());
    }

    // Test cases for Tag class omitted for brevity

    @Test
    public void testComment() {
        Token.Comment comment = new Token.Comment();
        assertEquals("", comment.getData());

        comment.data.append("This is a comment.");
        assertEquals("This is a comment.", comment.getData());
    }

    @Test
    public void testCharacter() {
        Token.Character character = new Token.Character("");
        assertEquals("", character.getData());

        character = new Token.Character("A");
        assertEquals("A", character.getData());

        character = new Token.Character("B");
        assertEquals("B", character.getData());
    }

    // Test cases for EOF class omitted for brevity

}