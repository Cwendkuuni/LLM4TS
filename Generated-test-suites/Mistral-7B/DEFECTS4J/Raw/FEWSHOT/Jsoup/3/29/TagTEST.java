package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TagTest {

    @Test
    public void testTagCreationAndEquals() {
        Tag tag1 = new Tag("HTML");
        Tag tag2 = new Tag("HTML");
        Tag tag3 = new Tag("BODY");
        Tag tag4 = new Tag("HEAD");

        assertEquals(tag1, tag1);
        assertEquals(tag1, tag2); // equals for pre-defined tags
        assertNotEquals(tag1, tag3);
        assertNotEquals(tag1, tag4);
    }

    @Test
    public void testTagCreationAndHashCode() {
        Tag tag1 = new Tag("HTML");
        Tag tag2 = new Tag("HTML");
        Tag tag3 = new Tag("BODY");
        Tag tag4 = new Tag("HEAD");

        int hashCode1 = tag1.hashCode();
        int hashCode2 = tag2.hashCode();
        int hashCode3 = tag3.hashCode();
        int hashCode4 = tag4.hashCode();

        assertEquals(hashCode1, hashCode2); // equals -> same hashcode
        assertNotEquals(hashCode1, hashCode3);
        assertNotEquals(hashCode1, hashCode4);
    }

    @Test
    public void testTagValueOf() {
        Tag tag1 = Tag.valueOf("HTML");
        Tag tag2 = Tag.valueOf("HTML");
        Tag tag3 = Tag.valueOf("BODY");
        Tag tag4 = Tag.valueOf("HEAD");

        assertEquals(tag1, tag2); // equals for pre-defined tags
        assertEquals(tag1, tag1.getValueOf("HTML"));
        assertEquals(tag3, tag3.getValueOf("BODY"));
        assertEquals(tag4, tag4.getValueOf("HEAD"));
        assertNotEquals(tag1, tag3.getValueOf("BODY"));
        assertNotEquals(tag1, tag4.getValueOf("HEAD"));

        Tag unknownTag = Tag.valueOf("UNKNOWN");
        assertNotEquals(tag1, unknownTag);
    }

    @Test
    public void testTagCanContain() {
        Tag pTag = new Tag("P");
        Tag divTag = new Tag("DIV");
        Tag spanTag = new Tag("SPAN");
        Tag inputTag = new Tag("INPUT");

        assertTrue(pTag.canContain(spanTag));
        assertTrue(divTag.canContain(pTag));
        assertTrue(divTag.canContain(spanTag));
        assertTrue(divTag.canContain(inputTag));
        assertFalse(pTag.canContain(inputTag));
        assertFalse(inputTag.canContain(pTag));
    }

    @Test
    public void testTagIsBlock() {
        Tag pTag = new Tag("P");
        Tag spanTag = new Tag("SPAN");

        assertTrue(pTag.isBlock());
        assertFalse(spanTag.isBlock());
    }

    @Test
    public void testTagCanContainBlock() {
        Tag divTag = new Tag("DIV");
        Tag pTag = new Tag("P");
        Tag spanTag = new Tag("SPAN");

        assertTrue(divTag.canContainBlock());
        assertTrue(divTag.canContain(pTag));
        assertTrue(pTag.isBlock());
        assertFalse(spanTag.isBlock());
        assertFalse(spanTag.canContainBlock());
        assertFalse(spanTag.canContain(pTag));
    }

    @Test
    public void testTagIsInline() {
        Tag pTag = new Tag("P");
        Tag spanTag = new Tag("SPAN");

        assertFalse(pTag.isInline());
        assertTrue(spanTag.isInline());
    }

    @Test
    public void testTagIsData() {
        Tag scriptTag = new Tag("SCRIPT");
        Tag pTag = new Tag("P");

        assertFalse(pTag.isData());
        assertTrue(scriptTag.isData());
    }

    @Test
    public void testTagIsEmpty() {
        Tag imgTag = new Tag("IMG");
        Tag pTag = new Tag("P");

        assertFalse(pTag.isEmpty());
        assertTrue(imgTag.isEmpty());
    }

    @Test
    public void testTagPreserveWhitespace() {
        Tag preTag = new Tag("PRE");
        Tag pTag = new Tag("P");

        assertFalse(pTag.preserveWhitespace());
        assertTrue(preTag.preserveWhitespace());
    }

    @Test
    public void testTagGetImplicitParent() {
        Tag divTag = new Tag("DIV");
        Tag pTag = new Tag("P");
        pTag.ancestors = new ArrayList<>();
        pTag.ancestors.add(divTag);

        assertEquals(divTag, pTag.getImplicitParent());
    }

    @Test
    public void testTagIsValidParent() {
        Tag parentTag = new Tag("DIV");
        Tag childTag = new Tag("P");

        assertTrue(parentTag.isValidParent(childTag));
    }

    @Test
    public void testTagInvalidParent() {
        Tag parentTag = new Tag("DIV");
        Tag childTag = new Tag("DIV");

        assertFalse(parentTag.isValidParent(childTag));
    }
}