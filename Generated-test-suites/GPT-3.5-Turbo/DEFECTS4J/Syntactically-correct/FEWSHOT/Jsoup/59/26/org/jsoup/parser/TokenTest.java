package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.jsoup.nodes.Attribute;
import org.jsoup.parser.Token.*;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenType() {
        Doctype doctype = new Doctype();
        StartTag startTag = new StartTag();
        EndTag endTag = new EndTag();
        Comment comment = new Comment();
        Character character = new Character();
        EOF eof = new EOF();

        assertEquals(TokenType.Doctype, doctype.type);
        assertEquals(TokenType.StartTag, startTag.type);
        assertEquals(TokenType.EndTag, endTag.type);
        assertEquals(TokenType.Comment, comment.type);
        assertEquals(TokenType.Character, character.type);
        assertEquals(TokenType.EOF, eof.type);
    }

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "pub";
        doctype.publicIdentifier.append("//public");
        doctype.systemIdentifier.append("//system");
        doctype.forceQuirks = true;

        Doctype resetDoctype = doctype.reset();

        assertEquals("", resetDoctype.name.toString());
        assertNull(resetDoctype.pubSysKey);
        assertEquals("", resetDoctype.publicIdentifier.toString());
        assertEquals("", resetDoctype.systemIdentifier.toString());
        assertFalse(resetDoctype.forceQuirks);
    }

    @Test
    public void testDoctypeGetters() {
        Doctype doctype = new Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "pub";
        doctype.publicIdentifier.append("//public");
        doctype.systemIdentifier.append("//system");
        doctype.forceQuirks = true;

        assertEquals("html", doctype.getName());
        assertEquals("pub", doctype.getPubSysKey());
        assertEquals("//public", doctype.getPublicIdentifier());
        assertEquals("//system", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        StartTag startTag = new StartTag();
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "id";
        startTag.pendingAttributeValue.append("main");
        startTag.pendingAttributeValueS = "test";
        startTag.hasEmptyAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();
        startTag.attributes.put("class", "container");

        StartTag resetStartTag = startTag.reset();

        assertNull(resetStartTag.tagName);
        assertNull(resetStartTag.normalName);
        assertNull(resetStartTag.pendingAttributeName);
        assertEquals("", resetStartTag.pendingAttributeValue.toString());
        assertNull(resetStartTag.pendingAttributeValueS);
        assertFalse(resetStartTag.hasEmptyAttributeValue);
        assertFalse(resetStartTag.hasPendingAttributeValue);
        assertFalse(resetStartTag.selfClosing);
        assertNull(resetStartTag.attributes);
    }

    @Test
    public void testEndTagReset() {
        EndTag endTag = new EndTag();
        endTag.tagName = "div";
        endTag.normalName = "div";

        EndTag resetEndTag = endTag.reset();

        assertNull(resetEndTag.tagName);
        assertNull(resetEndTag.normalName);
    }

    @Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.data.append("This is a comment");
        comment.bogus = true;

        Comment resetComment = comment.reset();

        assertEquals("", resetComment.data.toString());
        assertFalse(resetComment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Character character = new Character();
        character.data = "Some text";

        Character resetCharacter = character.reset();

        assertNull(resetCharacter.data);
    }

    @Test
    public void testStartTagToString() {
        StartTag startTag1 = new StartTag();
        startTag1.tagName = "div";
        startTag1.attributes = new Attributes();
        startTag1.attributes.put("class", "container");

        StartTag startTag2 = new StartTag();
        startTag2.tagName = "p";
        startTag2.attributes = new Attributes();

        assertEquals("<div class=\"container\">", startTag1.toString());
        assertEquals("<p>", startTag2.toString());
    }

    @Test
    public void testEndTagToString() {
        EndTag endTag = new EndTag();
        endTag.tagName = "div";

        assertEquals("</div>", endTag.toString());
    }

    @Test
    public void testDoctypeIsDoctype() {
        assertTrue(new Doctype().isDoctype());
    }

    @Test
    public void testStartTagIsStartTag() {
        assertTrue(new StartTag().isStartTag());
    }

    @Test
    public void testEndTagIsEndTag() {
        assertTrue(new EndTag().isEndTag());
    }

    @Test
    public void testCommentIsComment() {
        assertTrue(new Comment().isComment());
    }

    @Test
    public void testCharacterIsCharacter() {
        assertTrue(new Character().isCharacter());
    }

    @Test
    public void testEOFIsEOF() {
        assertTrue(new EOF().isEOF());
    }
}