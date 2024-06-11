package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.jsoup.parser.Tag;

public class TagTest {

    @Test
    public void testIsBlock() {
        Tag pTag = Tag.valueOf("p");
        assertTrue(pTag.isBlock());

        Tag imgTag = Tag.valueOf("img");
        assertFalse(imgTag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag pTag = Tag.valueOf("p");
        assertTrue(pTag.formatAsBlock());

        Tag inputTag = Tag.valueOf("input");
        assertFalse(inputTag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag divTag = Tag.valueOf("div");
        assertTrue(divTag.canContainBlock());

        Tag imgTag = Tag.valueOf("img");
        assertFalse(imgTag.canContainBlock());
    }

    @Test
    public void testIsEmpty() {
        Tag imgTag = Tag.valueOf("img");
        assertFalse(imgTag.isEmpty());

        Tag metaTag = Tag.valueOf("meta");
        assertTrue(metaTag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag imgTag = Tag.valueOf("img");
        assertTrue(imgTag.isSelfClosing());

        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag preTag = Tag.valueOf("pre");
        assertTrue(preTag.preserveWhitespace());

        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.preserveWhitespace());
    }

    @Test
    public void testEquals() {
        Tag pTag1 = Tag.valueOf("p");
        Tag pTag2 = Tag.valueOf("p");

        assertEquals(pTag1, pTag2);
    }

    @Test
    public void testHashCode() {
        Tag pTag = Tag.valueOf("p");
        Tag qTag = Tag.valueOf("q");

        int hashCodeP = pTag.hashCode();
        int hashCodeQ = qTag.hashCode();

        assertNotEquals(hashCodeP, hashCodeQ);
    }

    @Test
    public void testToString() {
        Tag pTag = Tag.valueOf("p");
        assertEquals("p", pTag.toString());
    }
}