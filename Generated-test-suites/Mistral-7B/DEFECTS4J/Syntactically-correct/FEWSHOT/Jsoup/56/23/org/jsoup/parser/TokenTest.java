package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.BooleanAttribute;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("html");
        doctype.reset();
        assertEquals(null, doctype.getName());
    }

    @Test
    public void testDoctypeGetters() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("html");
        doctype.getPublicIdentifier().equals("");
        doctype.getSystemIdentifier().equals("");
        doctype.isForceQuirks().equals(false);
    }

    @Test
    public void testTagReset() {
        AbstractTag tag = new StartTag();
        tag.name().equals("div");
        tag.reset();
        assertEquals(null, tag.name());
    }

    @Test
    public void testTagGetters() {
        StartTag tag = new StartTag();
        tag.name().equals("div");
        tag.normalName().equals("div");
        tag.selfClosing().equals(false);
        tag.getAttributes().isEmpty();

        tag.name("p").normalName("p");
        tag.selfClosing(true);
        tag.newAttribute();
        Attributes attributes = tag.getAttributes();
        Attribute attribute = attributes.get("class");
        assertTrue(attributes.size() == 1);
        assertEquals("class", attribute.getName());
        assertEquals("", attribute.getValue());
    }

    @Test
    public void testTagToString() {
        StartTag tag = new StartTag().name("div").attributes(new Attributes().put("class", "foo"));
        assertEquals("<div class=\"foo\"></div>", tag.toString());
    }

    @Test
    public void testEndTagReset() {
        EndTag endTag = new EndTag();
        endTag.name().equals("div");
        endTag.reset();
        assertEquals(null, endTag.name());
    }

    @Test
    public void testEndTagToString() {
        EndTag endTag = new EndTag().name("div");
        assertEquals("</div>", endTag.toString());
    }

    @Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.getData().equals("");
        comment.reset();
        assertEquals(null, comment.getData());
    }

    @Test
    public void testCommentToString() {
        Comment comment = new Comment().getData("test comment");
        assertEquals("<!--test comment-->", comment.toString());
    }

    @Test
    public void testCharacterReset() {
        Character character = new Character();
        character.data().equals("");
        character.reset();
        assertEquals(null, character.getData());
    }

    @Test
    public void testCharacterToString() {
        Character character = new Character().data('a');
        assertEquals("a", character.getData());
    }

    @Test
    public void testCharacterToStringWithAttributes() {
        Character character = new Character().data("&amp;<>&quot;'").attributes(new Attributes().put("class", "foo"));
        assertEquals("&amp;<>&quot;' ", character.getData());
        assertEquals("<character class=\"foo\" >&amp;<>&quot;' </character>", character.toString());
    }

    @Test
    public void testEOFReset() {
        EOF eof = new EOF();
        eof.reset();
        // EOF does not have any getters
    }

    @Test(expected = AssertionError.class)
    public void testInvalidTokenType() {
        Token token = new Object();
        token.type = new Object();
    }
}