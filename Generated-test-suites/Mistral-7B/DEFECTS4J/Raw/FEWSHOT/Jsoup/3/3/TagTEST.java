package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TagTest {

    @Test
    public void testTagCreation() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.getName());
        assertTrue(tag.isBlock());
        assertTrue(tag.canContainBlock());
        assertTrue(tag.canContainInline());
        assertFalse(tag.isData());
        assertFalse(tag.isEmpty());
        assertFalse(tag.preserveWhitespace());
        assertEquals(new String[0], tag.getAncestors().toArray(new String[0]));
    }

    @Test
    public void testTagValueOf() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.getName());
        assertTrue(tag.isBlock());
        assertTrue(tag.canContainBlock());
        assertTrue(tag.canContainInline());
        assertFalse(tag.isData());
        assertFalse(tag.isEmpty());
        assertFalse(tag.preserveWhitespace());
        assertEquals(new String[0], tag.getAncestors().toArray(new String[0]));
    }

    @Test
    public void testTagCanContain() {
        Tag parent = new Tag("body");
        Tag child1 = new Tag("p");
        Tag child2 = new Tag("p");
        Tag child3 = new Tag("div");
        Tag child4 = new Tag("body");

        assertTrue(parent.canContain(child1));
        assertTrue(parent.canContain(child2));
        assertTrue(parent.canContain(child3));
        assertFalse(parent.canContain(child4));
    }

    @Test
    public void testTagIsData() {
        Tag tag1 = new Tag("script").setAncestor("head");
        Tag tag2 = new Tag("script").setAncestor("body");
        Tag tag3 = new Tag("textarea").setAncestor("form");

        assertTrue(tag1.isData());
        assertFalse(tag2.isData());
        assertTrue(tag3.isData());
    }

    @Test
    public void testTagIsEmpty() {
        Tag tag1 = new Tag("img").setEmpty();
        Tag tag2 = new Tag("input").setEmpty();
        Tag tag3 = new Tag("div");

        assertTrue(tag1.isEmpty());
        assertFalse(tag2.isEmpty());
        assertFalse(tag3.isEmpty());
    }

    @Test
    public void testTagPreserveWhitespace() {
        Tag tag1 = new Tag("pre").setPreserveWhitespace();
        Tag tag2 = new Tag("div");

        assertTrue(tag1.preserveWhitespace());
        assertFalse(tag2.preserveWhitespace());
    }

    @Test
    public void testTagToString() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("div");

        assertEquals("p", tag1.toString());
        assertEquals("div", tag2.toString());
    }

    @Test
    public void testTagEqualsAndHashCode() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        Tag tag3 = new Tag("div");

        assertTrue(tag1.equals(tag1));
        assertTrue(tag1.equals(tag2));
        assertFalse(tag1.equals(tag3));

        int hashcode1 = tag1.hashCode();
        int hashcode2 = tag1.hashCode();

        assertEquals(hashcode1, hashcode2);
    }

    @Test
    public void testTagGetImplicitParent() {
        Tag body = new Tag("body");
        Tag p1 = new Tag("p");
        Tag p2 = new Tag("p");
        p1.setAncestor(body);
        p2.setAncestor(body);

        Tag implicitParent1 = p1.getImplicitParent();
        Tag implicitParent2 = p2.getImplicitParent();

        assertSame(body, implicitParent1);
        assertSame(body, implicitParent2);
    }

    @Test
    public void testTagGetImplicitParentNull() {
        Tag tag = new Tag("div");
        assertNull(tag.getImplicitParent());
    }

    @Test
    public void testTagIsValidParent() {
        Tag parent1 = new Tag("p");
        Tag child1 = new Tag("p");
        Tag child2 = new Tag("div");

        assertTrue(parent1.isValidParent(child1));
        assertTrue(parent1.isValidParent(child2));

        List<Tag> ancestors1 = new ArrayList<>();
        ancestors1.add(parent1);
        child1.setAncestors(ancestors1);

        assertFalse(parent1.isValidParent(child1));
    }

    @Test
    public void testTagCreateBlock() {
        Tag html = createBlock("HTML");
        Tag head = createBlock("HEAD");
        Tag body = createBlock("BODY");
        Tag form = createBlock("FORM");

        assertEquals("HTML", html.getName());
        assertEquals("HEAD", head.getName());
        assertEquals("BODY", body.getName());
        assertEquals("FORM", form.getName());

        assertEquals(new String[0], html.getAncestors().toArray(new String[0]));
        assertEquals(new String[]{"HTML"}, head.getAncestors().toArray(new String[0]));
        assertEquals(new String[]{"HTML", "BODY"}, body.getAncestors().toArray(new String[0]));
        assertEquals(new String[]{"FORM"}, form.getAncestors().toArray(new String[0]));
    }

    @Test
    public void testTagCreateInline() {
        Tag font = createInline("FONT");
        Tag em = createInline("EM");
        Tag span = createInline("SPAN");

        assertEquals("FONT", font.getName());
        assertEquals("EM", em.getName());
        assertEquals("SPAN", span.getName());

        assertEquals(new String[0], font.getAncestors().toArray(new String[0]));
        assertEquals(new String[0], em.getAncestors().toArray(new String[0]));
        assertEquals(new String[0], span.getAncestors().toArray(new String[0]));
    }

    @Test
    public void testTagRegister() {
        Tag tag = new Tag("custom");
        Tag registeredTag = Tag.register(tag);

        assertEquals("custom", registeredTag.getName());
        assertSame(registeredTag, Tag.valueOf("custom"));
    }
}