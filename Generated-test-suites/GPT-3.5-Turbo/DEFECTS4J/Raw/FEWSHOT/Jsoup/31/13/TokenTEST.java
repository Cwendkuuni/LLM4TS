import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Token.*;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token() {};
        assertEquals("Token", token.tokenType());
    }

    @Test
    public void testDoctype() {
        Doctype doctype = new Doctype();
        doctype.name.append("html");
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        assertTrue(doctype.isDoctype());
        assertFalse(doctype.isStartTag());
        assertFalse(doctype.isEndTag());
        assertFalse(doctype.isComment());
        assertFalse(doctype.isCharacter());
        assertFalse(doctype.isEOF());
        assertEquals("html", doctype.getName());
        assertEquals("-//W3C//DTD HTML 4.01//EN", doctype.getPublicIdentifier());
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTag() {
        StartTag startTag = new StartTag();
        startTag.tagName = "div";
        startTag.attributes = new Attributes();
        startTag.selfClosing = false;
        assertTrue(startTag.isStartTag());
        assertFalse(startTag.isDoctype());
        assertFalse(startTag.isEndTag());
        assertFalse(startTag.isComment());
        assertFalse(startTag.isCharacter());
        assertFalse(startTag.isEOF());
        assertEquals("div", startTag.name());
        assertFalse(startTag.isSelfClosing());
        assertNull(startTag.getAttributes());
        startTag.selfClosing = true;
        assertTrue(startTag.isSelfClosing());
    }

    @Test
    public void testEndTag() {
        EndTag endTag = new EndTag();
        endTag.tagName = "div";
        assertTrue(endTag.isEndTag());
        assertFalse(endTag.isDoctype());
        assertFalse(endTag.isStartTag());
        assertFalse(endTag.isComment());
        assertFalse(endTag.isCharacter());
        assertFalse(endTag.isEOF());
        assertEquals("div", endTag.name());
    }

    @Test
    public void testComment() {
        Comment comment = new Comment();
        comment.data.append("This is a comment");
        assertTrue(comment.isComment());
        assertFalse(comment.isDoctype());
        assertFalse(comment.isStartTag());
        assertFalse(comment.isEndTag());
        assertFalse(comment.isCharacter());
        assertFalse(comment.isEOF());
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCharacter() {
        Character character = new Character("A");
        assertTrue(character.isCharacter());
        assertFalse(character.isDoctype());
        assertFalse(character.isStartTag());
        assertFalse(character.isEndTag());
        assertFalse(character.isComment());
        assertFalse(character.isEOF());
        assertEquals("A", character.getData());
    }

    @Test
    public void testEOF() {
        EOF eof = new EOF();
        assertTrue(eof.isEOF());
        assertFalse(eof.isDoctype());
        assertFalse(eof.isStartTag());
        assertFalse(eof.isEndTag());
        assertFalse(eof.isComment());
        assertFalse(eof.isCharacter());
    }
}