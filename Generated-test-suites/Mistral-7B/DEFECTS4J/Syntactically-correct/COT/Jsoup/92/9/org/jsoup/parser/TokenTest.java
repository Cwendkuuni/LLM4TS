package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.parser.Token.*;
import org.jsoup.parser.TokenType;

import java.util.Arrays;

public class TokenTest {

    @org.junit.Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("html"); // assert initial state

        doctype.reset();
        doctype.getName().isEmpty(); // assert reset state
    }

    @org.junit.Test
    public void testDoctypeGetters() {
        Doctype doctype = new Doctype();
        doctype.getName().equals("html");
        doctype.setName("html5");
        doctype.getPublicIdentifier().equals("XHTML1-Compatible");
        doctype.setPublicIdentifier("XHTML1-Compatible");
        doctype.getSystemIdentifier().equals("about:blank");
        doctype.setSystemIdentifier("about:blank");
        doctype.setForceQuirks(true);
        assertTrue(doctype.isForceQuirks());

        doctype.reset();
        doctype.getName().isEmpty();
        doctyp.getPublicIdentifier().isEmpty();
        doctype.getSystemIdentifier().isEmpty();
        assertFalse(doctype.isForceQuirks());
    }

    @org.junit.Test
    public void testTagReset() {
        Tag tag = new StartTag();
        tag.name().equals("p"); // assert initial state

        tag.reset();
        tag.getName().isEmpty();
        tag.getAttributes().isEmpty();
        tag.setAttributes(new Attributes());
        tag.setSelfClosing(false);
    }

    @org.junit.Test
    public void testTagSetters() {
        StartTag startTag = new StartTag();
        startTag.name("p");
        startTag.attributes().put("class", "class1 class2");
        startTag.setSelfClosing(true);

        String name = startTag.name();
        assertEquals("p", name);
        String[] classes = startTag.getAttributes().get("class").split("\\s+");
        assertArrayEquals(new String[]{"class1", "class2"}, classes);
        assertTrue(startTag.isSelfClosing());

        startTag.reset();
        name.isEmpty();
        assertNull(startTag.getAttributes());
        assertFalse(startTag.isSelfClosing());
    }

    @org.junit.Test
    public void testTagToString() {
        StartTag startTag = new StartTag();
        startTag.name("p");
        startTag.attributes().put("class", "class1 class2");

        String result = startTag.toString();
        String expected = "<p class=\"class1 class2\" />";
        assertEquals(expected, result);

        startTag.setSelfClosing(false);
        result = startTag.toString();
        expected = "<p class=\"class1 class2\">";
        assertEquals(expected, result);
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testTagAddDuplicateAttribute() {
        StartTag startTag = new StartTag();
        startTag.name("p");
        startTag.attributes().put("class", "class1");
        startTag.attributes().put("class", "class2");
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testTagNullAttributeName() {
        StartTag startTag = new StartTag();
        startTag.name("p");
        startTag.attributes().put(null, "class1");
    }

    @org.junit.Test(expected = NullPointerException.class)
    public void testTagNullAttributeValue() {
        StartTag startTag = new StartTag();
        startTag.name("p");
        startTag.attributes().put("class", null);
    }

    @org.junit.Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.getData().equals(""); // assert initial state

        comment.reset();
        comment.getData().isEmpty();
    }

    @org.junit.Test
    public void testCommentGetter() {
        Comment comment = new Comment();
        comment.getData().equals("<!-- Comment -->");
        comment.setData("<!-- Another Comment -->");
        assertEquals("Another Comment", comment.getData());

        comment.reset();
        comment.getData().isEmpty();
    }

    @org.junit.Test
    public void testCharacterReset() {
        Character character = new Character();
        character.getData().equals(""); // assert initial state

        character.reset();
        character.getData().isEmpty();
    }

    @org.junit.Test
    public void testCharacterGetter() {
        Character character = new Character();
        character.data("Hello, World!");
        assertEquals("Hello, World!", character.getData());

        character.reset();
        character.getData().isEmpty();
    }

    @org.junit.Test
    public void testEOFReset() {
        EOF eof = new EOF();
        eof.reset();
        assertTrue(eof.isEOF());
    }

}