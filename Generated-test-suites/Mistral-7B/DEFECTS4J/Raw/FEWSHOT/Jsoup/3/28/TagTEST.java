package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TagTest {

    @Test
    public void testTagCreation() {
        Tag html = Tag.valueOf("HTML");
        Tag head = Tag.valueOf("HEAD");
        Tag body = Tag.valueOf("BODY");
        Tag frameSet = Tag.valueOf("FRAMESET");

        assertEquals("html", html.tagName);
        assertEquals("HTML", html.toString());
        assertEquals(new String[0], html.ancestors.toArray(new String[0]));
        assertFalse(html.isBlock);
        assertFalse(html.canContainBlock);
        assertFalse(html.canContainInline);
        assertFalse(html.optionalClosing);
        assertFalse(html.empty);
        assertFalse(html.preserveWhitespace);

        assertEquals("head", head.tagName);
        assertEquals("HEAD", head.toString());
        assertEquals(new String[]{"HTML"}, head.ancestors.toArray(new String[0]));
        assertFalse(head.isBlock);
        assertFalse(head.canContainBlock);
        assertFalse(head.canContainInline);
        assertFalse(head.optionalClosing);
        assertFalse(head.empty);
        assertFalse(head.preserveWhitespace);

        assertEquals("body", body.tagName);
        assertEquals("BODY", body.toString());
        assertEquals(new String[]{"HTML"}, body.ancestors.toArray(new String[0]));
        assertTrue(body.isBlock);
        assertTrue(body.canContainBlock);
        assertFalse(body.canContainInline);
        assertFalse(body.optionalClosing);
        assertFalse(body.empty);
        assertFalse(body.preserveWhitespace);

        assertEquals("frameset", frameSet.tagName);
        assertEquals("FRAMESET", frameSet.toString());
        assertEquals(new String[]{"HTML"}, frameSet.ancestors.toArray(new String[0]));
        assertTrue(frameSet.isBlock);
        assertTrue(frameSet.canContainBlock);
        assertFalse(frameSet.canContainInline);
        assertFalse(frameSet.optionalClosing);
        assertFalse(frameSet.empty);
        assertFalse(frameSet.preserveWhitespace);
    }

    @Test
    public void testTagEqualsAndHashCode() {
        Tag tag1 = Tag.valueOf("P");
        Tag tag2 = Tag.valueOf("P");
        Tag tag3 = Tag.valueOf("DIV");

        assertEquals(tag1, tag1);
        assertEquals(tag1, tag1);
        assertEquals(tag1, tag2);
        assertEquals(tag1.hashCode(), tag2.hashCode());

        assertNotEquals(tag1, tag3);
        assertNotEquals(tag1.hashCode(), tag3.hashCode());

        Tag tag4 = new Tag("foo");
        assertEquals(tag1, Tag.valueOf("P"));
        assertNotEquals(tag1, tag4);
        assertNotEquals(tag1.hashCode(), tag4.hashCode());
    }

    @Test
    public void testTagToString() {
        Tag html = Tag.valueOf("HTML");
        Tag head = Tag.valueOf("HEAD");
        Tag body = Tag.valueOf("BODY");
        Tag p = Tag.valueOf("P");

        assertEquals("HTML", html.toString());
        assertEquals("HEAD", head.toString());
        assertEquals("BODY", body.toString());
        assertEquals("P", p.toString());
    }

    @Test
    public void testTagName() {
        Tag html = Tag.valueOf("HTML");
        Tag head = Tag.valueOf("HEAD");
        Tag body = Tag.valueOf("BODY");
        Tag p = Tag.valueOf("P");

        assertEquals("html", html.getName());
        assertEquals("HEAD", head.getName());
        assertEquals("BODY", body.getName());
        assertEquals("P", p.getName());
    }

    @Test
    public void testTagIsBlock() {
        Tag html = Tag.valueOf("HTML");
        Tag head = Tag.valueOf("HEAD");
        Tag body = Tag.valueOf("BODY");
        Tag p = Tag.valueOf("P");

        assertFalse(html.isBlock());
        assertTrue(body.isBlock());
        assertTrue(p.isBlock());
    }

    @Test
    public void testTagCanContainBlock() {
        Tag html = Tag.valueOf("HTML");
        Tag head = Tag.valueOf("HEAD");
        Tag body = Tag.valueOf("BODY");
        Tag p = Tag.valueOf("P");

        assertFalse(html.canContainBlock());
        assertFalse(head.canContainBlock());
        assertTrue(body.canContainBlock());
        assertTrue(p.canContainBlock());
    }

    @Test
    public void testTagCanContainInline() {
        Tag html = Tag.valueOf("HTML");
        Tag head = Tag.valueOf("HEAD");
        Tag body = Tag.valueOf("BODY");
        Tag p = Tag.valueOf("P");

        assertFalse(html.canContainInline());
        assertTrue(head.canContainInline());
        assertTrue(body.canContainInline());
        assertTrue(p.canContainInline());
    }

    @Test
    public void testTagIsEmpty() {
        Tag html = Tag.valueOf("HTML");
        Tag img = Tag.valueOf("IMG");

        assertFalse(html.isEmpty());
        assertFalse(img.isEmpty());
    }

    @Test
    public void testTagIsData() {
        Tag html = Tag.valueOf("HTML");
        Tag script = Tag.valueOf("SCRIPT");

        assertFalse(html.isData());
        assertTrue(script.isData());
    }

    @Test
    public void testTagCanContain() {
        Tag html = Tag.valueOf("HTML");
        Tag head = Tag.valueOf("HEAD");
        Tag body = Tag.valueOf("BODY");
        Tag p = Tag.valueOf("P");
        Tag input = Tag.valueOf("INPUT");
        Tag table = Tag.valueOf("TABLE");

        assertTrue(body.canContain(p));
        assertTrue(head.canContain(script));
        assertFalse(html.canContain(p));
        assertFalse(html.canContain(input));
        assertFalse(table.canContain(html));
        assertFalse(table.canContain(body));
    }

    @Test
    public void testTagIsValidParent() {
        Tag html = Tag.valueOf("HTML");
        Tag head = Tag.valueOf("HEAD");
        Tag body = Tag.valueOf("BODY");
        Tag p = Tag.valueOf("P");
        Tag div = Tag.valueOf("DIV");

        assertTrue(html.isValidParent(body));
        assertTrue(body.isValidParent(p));
        assertFalse(html.isValidParent(p));
        assertFalse(div.isValidParent(html));
    }

    @Test
    public void testTagGetImplicitParent() {
        Tag div = new Tag("DIV");
        Tag p = new Tag("P");
        Tag body = new Tag("BODY");
        Tag html = new Tag("HTML");

        div.ancestors = Arrays.asList(html, body, div);
        assertEquals(body, div.getImplicitParent());
        assertEquals(html, div.getImplicitParent().getImplicitParent());
        assertNull(p.getImplicitParent());
    }

    @Test
    public void testTagValueOfUnknownTag() {
        Tag unknownTag = Tag.valueOf("UNKNOWN");

        assertEquals("UNKNOWN", unknownTag.tagName);
        assertFalse(unknownTag.isBlock);
        assertFalse(unknownTag.canContainBlock);
        assertFalse(unknownTag.canContainInline);
        assertFalse(unknownTag.optionalClosing);
        assertFalse(unknownTag.empty);
        assertFalse(unknownTag.preserveWhitespace);
    }

    @Test
    public void testTagValueOfEmptyString() {
        Tag emptyTag = Tag.valueOf("");
        assertEquals("", emptyTag.tagName);
        assertFalse(emptyTag.isBlock);
        assertFalse(emptyTag.canContainBlock);
        assertFalse(emptyTag.canContainInline);
        assertFalse(emptyTag.optionalClosing);
        assertTrue(emptyTag.empty);
        assertFalse(emptyTag.preserveWhitespace);
    }

    @Test
    public void testTagValueOfNull() {
        try {
            Tag tag = Tag.valueOf(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testTagValueOfInvalidTagName() {
        try {
            Tag tag = new Tag("Tag Name");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testTagGetAncestors() {
        Tag html = new Tag("HTML");
        Tag head = new Tag("HEAD");
        Tag body = new Tag("BODY");
        Tag div = new Tag("DIV");
        Tag p = new Tag("P");

        html.ancestors = new ArrayList<>();
        head.ancestors = Arrays.asList(html);
        body.ancestors = Arrays.asList(html);
        div.ancestors = Arrays.asList(html, body);
        p.ancestors = Arrays.asList(body);

        assertEquals(Collections.emptyList(), html.getAncestors());
        assertEquals(Arrays.asList(html), head.getAncestors());
        assertEquals(Arrays.asList(html), body.getAncestors());
        assertEquals(Arrays.asList(html, body), div.getAncestors());
        assertEquals(Arrays.asList(body), p.getAncestors());
    }

    @Test
    public void testTagSetAncestors() {
        Tag html = new Tag("HTML");
        Tag head = new Tag("HEAD");
        Tag body = new Tag("BODY");
        Tag div = new Tag("DIV");

        html.ancestors = new ArrayList<>();
        html.setAncestor("HTML");
        assertEquals(Arrays.asList("HTML"), html.ancestors);

        html.ancestors = new ArrayList<>();
        html.setAncestor("HEAD", "BODY");
        assertEquals(Arrays.asList("HEAD", "BODY"), html.ancestors);

        html.ancestors = new ArrayList<>();
        html.setAncestor(new String[0]);
        assertEquals(Collections.emptyList(), html.ancestors);

        div.ancestors = new ArrayList<>();
        div.setAncestor(html);
        assertEquals(Arrays.asList("HTML"), div.ancestors);

        div.ancestors = new ArrayList<>();
        div.ancestors.add(head);
        div.ancestors.add(body);
        div.setAncestor(html);
        assertEquals(Arrays.asList("HTML"), div.ancestors);
    }
}