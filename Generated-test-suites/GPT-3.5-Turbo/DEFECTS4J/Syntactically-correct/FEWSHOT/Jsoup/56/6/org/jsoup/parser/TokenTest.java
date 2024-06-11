package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.name.append("test");
        doctype.publicIdentifier.append("public");
        doctype.systemIdentifier.append("system");
        doctype.forceQuirks = true;

        Doctype resetDoctype = doctype.reset();

        assertEquals("", resetDoctype.name.toString());
        assertEquals("", resetDoctype.publicIdentifier.toString());
        assertEquals("", resetDoctype.systemIdentifier.toString());
        assertFalse(resetDoctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        StartTag startTag = new StartTag();
        startTag.name = "test";
        startTag.normalName = "test";
        startTag.pendingAttributeName = "attr";
        startTag.pendingAttributeValue = new StringBuilder("value");
        startTag.pendingAttributeValueS = "valueS";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();

        StartTag resetStartTag = startTag.reset();

        assertNull(resetStartTag.tagName);
        assertNull(resetStartTag.normalName);
        assertNull(resetStartTag.pendingAttributeName);
        assertEquals("", resetStartTag.pendingAttributeValue.toString());
        assertNull(resetStartTag.pendingAttributeValueS);
        assertFalse(resetStartTag.hasEmptyAttributeValue);
        assertFalse(resetStartTag.hasPendingAttributeValue);
        assertFalse(resetStartTag.isSelfClosing());
        assertNull(resetStartTag.attributes);
    }

    @Test
    public void testEndTagReset() {
        EndTag endTag = new EndTag();
        endTag.tagName = "test";
        endTag.normalName = "test";

        EndTag resetEndTag = endTag.reset();

        assertNull(resetEndTag.tagName);
        assertNull(resetEndTag.normalName);
    }

    @Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.data.append("test");
        comment.bogus = true;

        Comment resetComment = comment.reset();

        assertEquals("", resetComment.data.toString());
        assertFalse(resetComment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Character character = new Character();
        character.data = "test";

        Character resetCharacter = character.reset();

        assertNull(resetCharacter.getData());
    }

    @Test
    public void testDoctypeGetters() {
        Doctype doctype = new Doctype();
        doctype.name.append("name");
        doctype.publicIdentifier.append("public");
        doctype.systemIdentifier.append("system");
        doctype.forceQuirks = true;

        assertEquals("name", doctype.getName());
        assertEquals("public", doctype.getPublicIdentifier());
        assertEquals("system", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagMethods() {
        StartTag startTag = new StartTag();
        startTag.tagName = "test";
        startTag.normalName = "test";
        startTag.pendingAttributeName = "attr";
        startTag.pendingAttributeValue = new StringBuilder("value");
        startTag.pendingAttributeValueS = "valueS";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();

        startTag.newAttribute();

        assertEquals("", startTag.pendingAttributeName);
        assertTrue(startTag.hasPendingAttributeValue);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);

        startTag.finaliseTag();

        assertNull(startTag.pendingAttributeName);
    }

    @Test
    public void testEndTagToString() {
        EndTag endTag = new EndTag();
        endTag.tagName = "test";

        assertEquals("</test>", endTag.toString());
    }

    @Test
    public void testCommentGetData() {
        Comment comment = new Comment();
        comment.data.append("test");

        assertEquals("test", comment.getData());
    }

    @Test
    public void testCharacterToString() {
        Character character = new Character();
        character.data = "test";

        assertEquals("test", character.toString());
    }

    @Test
    public void testIsDoctype() {
        Token token = new Doctype();

        assertTrue(token.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        Token token = new Doctype();
        Doctype doctype = token.asDoctype();

        assertTrue(doctype instanceof Doctype);
    }

    @Test
    public void testIsStartTag() {
        Token token = new StartTag();

        assertTrue(token.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        Token token = new StartTag();
        StartTag startTag = token.asStartTag();

        assertTrue(startTag instanceof StartTag);
    }

    @Test
    public void testIsEndTag() {
        Token token = new EndTag();

        assertTrue(token.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        Token token = new EndTag();
        EndTag endTag = token.asEndTag();

        assertTrue(endTag instanceof EndTag);
    }

    @Test
    public void testIsComment() {
        Token token = new Comment();

        assertTrue(token.isComment());
    }

    @Test
    public void testAsComment() {
        Token token = new Comment();
        Comment comment = token.asComment();

        assertTrue(comment instanceof Comment);
    }

    @Test
    public void testIsCharacter() {
        Token token = new Character();

        assertTrue(token.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Token token = new Character();
        Character character = token.asCharacter();

        assertTrue(character instanceof Character);
    }

    @Test
    public void testIsEOF() {
        Token token = new Token.EOF();

        assertTrue(token.isEOF());
    }
}