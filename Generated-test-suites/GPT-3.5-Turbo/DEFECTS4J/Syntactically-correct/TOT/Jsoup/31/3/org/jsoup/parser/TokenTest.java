
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TokenTest {

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");

        assertEquals("html", doctype.getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01//EN");

        assertEquals("-//W3C//DTD HTML 4.01//EN", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");

        assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;

        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTagIsSelfClosing() {
        Token.Tag tag = mock(Token.Tag.class);
        when(tag.isSelfClosing()).thenReturn(true);

        assertTrue(tag.isSelfClosing());
        verify(tag).isSelfClosing();
    }

    @Test
    public void testTagGetName() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "div";

        assertEquals("div", tag.name());
    }

    @Test
    public void testTagNewAttribute() {
        Token.Tag tag = new Token.StartTag();
        tag.newAttribute();

        assertNotNull(tag.attributes);
    }

    @Test
    public void testTagFinaliseTag() {
        Token.Tag tag = new Token.StartTag();
        tag.pendingAttributeName = "class";
        tag.pendingAttributeValue = new StringBuilder("container");
        tag.finaliseTag();

        assertNull(tag.pendingAttributeName);
        assertNull(tag.pendingAttributeValue);

        Attribute expectedAttribute = new Attribute("class", "container");
        assertEquals(expectedAttribute, tag.attributes.get("class"));
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag("div");
        Attribute attribute = new Attribute("class", "container");
        startTag.attributes.put(attribute);

        assertEquals("<div class=\"container\">", startTag.toString());
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag("div");

        assertEquals("</div>", endTag.toString());
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");

        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character("A");

        assertEquals("A", character.getData());
    }

    @Test
    public void testIsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        Token token = doctype;

        assertTrue(token.isDoctype());
    }

    @Test
    public void testIsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        Token token = startTag;

        assertTrue(token.isStartTag());
    }

    @Test
    public void testIsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        Token token = endTag;

        assertTrue(token.isEndTag());
    }

    @Test
    public void testIsComment() {
        Token.Comment comment = new Token.Comment();
        Token token = comment;

        assertTrue(token.isComment());
    }

    @Test
    public void testIsCharacter() {
        Token.Character character = new Token.Character("A");
        Token token = character;

        assertTrue(token.isCharacter());
    }

    @Test
    public void testIsEOF() {
        Token.EOF eof = new Token.EOF();
        Token token = eof;

        assertTrue(token.isEOF());
    }
}
