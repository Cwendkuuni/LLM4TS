package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TagTest {

    @Test
    public void testDefaultAncestor() {
        Tag defaultAncestor = Tag.valueOf("BODY");
        assertEquals("BODY", defaultAncestor.getName());
        assertNotNull(defaultAncestor.getImplicitParent());
        assertTrue(defaultAncestor.isBlock());
        assertFalse(defaultAncestor.canContainBlock());
        assertFalse(defaultAncestor.isInline());
        assertFalse(defaultAncestor.isEmpty());
        assertFalse(defaultAncestor.preserveWhitespace());
        assertNull(defaultAncestor.ancestors);
    }

    @Test
    public void testHTMLTag() {
        Tag htmlTag = Tag.valueOf("HTML");
        assertEquals("html", htmlTag.getName());
        assertNull(htmlTag.getImplicitParent());
        assertFalse(htmlTag.isBlock());
        assertTrue(htmlTag.canContainBlock());
        assertFalse(htmlTag.isInline());
        assertFalse(htmlTag.isEmpty());
        assertFalse(htmlTag.preserveWhitespace());
        assertEmpty(htmlTag.ancestors);
    }

    @Test
    public void testHeadTag() {
        Tag headTag = Tag.valueOf("HEAD");
        assertEquals("head", headTag.getName());
        assertEquals(new String[]{"HTML"}, headTag.ancestors.toArray(new String[0]));
        assertFalse(headTag.isBlock());
        assertTrue(headTag.canContainBlock());
        assertFalse(headTag.isInline());
        assertFalse(headTag.isEmpty());
        assertFalse(headTag.preserveWhitespace());
    }

    @Test
    public void testBodyTag() {
        Tag bodyTag = Tag.valueOf("BODY");
        assertEquals("body", bodyTag.getName());
        assertEquals(new String[]{"HTML"}, bodyTag.ancestors.toArray(new String[0]));
        assertTrue(bodyTag.isBlock());
        assertTrue(bodyTag.canContainBlock());
        assertFalse(bodyTag.isInline());
        assertFalse(bodyTag.isEmpty());
        assertFalse(bodyTag.preserveWhitespace());
    }

    @Test
    public void testBlockTags() {
        Tag pTag = Tag.valueOf("P");
        assertEquals("p", pTag.getName());
        assertEquals(new String[]{"BODY"}, pTag.ancestors.toArray(new String[0]));
        assertTrue(pTag.isBlock());
        assertTrue(pTag.canContainBlock());
        assertFalse(pTag.isInline());
        assertFalse(pTag.isEmpty());
        assertFalse(pTag.preserveWhitespace());

        Tag h1Tag = Tag.valueOf("H1");
        assertEquals("h1", h1Tag.getName());
        assertEquals(new String[]{"BODY"}, h1Tag.ancestors.toArray(new String[0]));
        assertTrue(h1Tag.isBlock());
        assertTrue(h1Tag.canContainBlock());
        assertFalse(h1Tag.isInline());
        assertFalse(h1Tag.isEmpty());
        assertFalse(h1Tag.preserveWhitespace());
    }

    @Test
    public void testInlineTags() {
        Tag aTag = Tag.valueOf("A");
        assertEquals("a", aTag.getName());
        assertEquals(new String[0], aTag.ancestors.toArray(new String[0]));
        assertFalse(aTag.isBlock());
        assertFalse(aTag.canContainBlock());
        assertTrue(aTag.isInline());
        assertFalse(aTag.isEmpty());
        assertFalse(aTag.preserveWhitespace());
        assertFalse(aTag.optionalClosing);

        Tag imgTag = Tag.valueOf("IMG");
        assertEquals("img", imgTag.getName());
        assertEquals(new String[0], imgTag.ancestors.toArray(new String[0]));
        assertFalse(imgTag.isBlock());
        assertFalse(imgTag.canContainBlock());
        assertTrue(imgTag.isInline());
        assertTrue(imgTag.isEmpty());
        assertFalse(imgTag.preserveWhitespace());
    }

    @Test
    public void testEmptyTags() {
        Tag brTag = Tag.valueOf("BR");
        assertEquals("br", brTag.getName());
        assertEquals(new String[0], brTag.ancestors.toArray(new String[0]));
        assertFalse(brTag.isBlock());
        assertFalse(brTag.canContainBlock());
        assertFalse(brTag.isInline());
        assertTrue(brTag.isEmpty());
        assertFalse(brTag.preserveWhitespace());
    }

    @Test
    public void testCanContain() {
        Tag pTag = Tag.valueOf("P");
        Tag divTag = Tag.valueOf("DIV");
        assertTrue(pTag.canContain(divTag));

        Tag pTag2 = Tag.valueOf("P");
        Tag pTag3 = Tag.valueOf("P");
        assertTrue(pTag2.canContain(pTag3));

        Tag pTag4 = Tag.valueOf("P");
        Tag h1Tag = Tag.valueOf("H1");
        assertFalse(pTag4.canContain(h1Tag));

        Tag h1Tag2 = Tag.valueOf("H1");
        Tag pTag5 = Tag.valueOf("P");
        assertFalse(h1Tag2.canContain(pTag5));

        Tag divTag2 = Tag.valueOf("DIV");
        Tag pTag6 = Tag.valueOf("P");
        assertTrue(divTag2.canContain(pTag6));

        Tag divTag3 = Tag.valueOf("DIV");
        Tag divTag4 = Tag.valueOf("DIV");
        assertTrue(divTag3.canContain(divTag4));
    }

    @Test
    public void testIsValidParent() {
        Tag divTag = Tag.valueOf("DIV");
        Tag pTag = Tag.valueOf("P");
        Tag bodyTag = Tag.valueOf("BODY");
        assertTrue(divTag.isValidParent(pTag));
        assertTrue(divTag.isValidParent(bodyTag));
        assertFalse(pTag.isValidParent(divTag));
        assertFalse(bodyTag.isValidParent(divTag));
    }

    @Test
    public void testEqualsAndHashCode() {
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag1");
        Tag tag3 = new Tag("tag2");
        assertEquals(tag1, tag2);
        assertEquals(tag1.hashCode(), tag2.hashCode());
        assertNotEquals(tag1, tag3);
        assertNotEquals(tag1.hashCode(), tag3.hashCode());
    }
}