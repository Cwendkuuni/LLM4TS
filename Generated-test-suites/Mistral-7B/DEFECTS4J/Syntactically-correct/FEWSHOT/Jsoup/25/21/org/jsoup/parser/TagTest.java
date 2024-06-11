package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;

public class TagTest {

    @Test
    public void testIsKnownTag_knownTag() {
        Tag tag = Tag.valueOf("p");
        assertTrue(Tag.isKnownTag(tag.getName()));
    }

    @Test
    public void testIsKnownTag_unknownTag() {
        Tag tag = new Tag("unknown");
        assertFalse(Tag.isKnownTag(tag.getName()));
    }

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

        Tag imgTag = Tag.valueOf("img");
        assertFalse(imgTag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag pTag = Tag.valueOf("p");
        assertTrue(pTag.canContainBlock());

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
    public void testIsData() {
        Tag scriptTag = Tag.valueOf("script");
        assertTrue(scriptTag.isData());

        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.isData());
    }

    @Test
    public void testIsInline() {
        Tag imgTag = Tag.valueOf("img");
        assertTrue(imgTag.isInline());

        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.isInline());
    }

    @Test
    public void testIsKnownTag_emptyString() {
        assertFalse(Tag.isKnownTag(""));
    }

    @Test(expected = NullPointerException.class)
    public void testIsKnownTag_null() {
        Tag.isKnownTag(null);
    }

    @Test
    public void testValueOf_emptyString() {
        Tag tag = Tag.valueOf("");
        assertFalse(tag.isBlock());
        assertFalse(tag.formatAsBlock());
        assertFalse(tag.canContainBlock());
        assertFalse(tag.canContainInline());
        assertFalse(tag.isEmpty());
        assertFalse(tag.selfClosing);
        assertFalse(tag.preserveWhitespace);
        assertEquals("", tag.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOf_null() {
        Tag.valueOf(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOf_emptyTrimmedString() {
        Tag.valueOf(" ".trim());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOf_trailingQuotes() {
        Tag.valueOf("tagName\"");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOf_leadingQuotes() {
        Tag.valueOf("\"tagName");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOf_leadingAndTrailingQuotes() {
        Tag.valueOf("\"tagName\"");
    }

    @Test
    public void testEquals() {
        Tag pTag1 = Tag.valueOf("p");
        Tag pTag2 = Tag.valueOf("p");

        assertTrue(pTag1.equals(pTag2));
        assertTrue(pTag2.equals(pTag1));

        Tag imgTag1 = Tag.valueOf("img");
        Tag imgTag2 = Tag.valueOf("img");

        assertTrue(imgTag1.equals(imgTag2));
        assertTrue(imgTag2.equals(imgTag1));

        Tag unknownTag1 = new Tag("unknown");
        Tag unknownTag2 = new Tag("unknown");

        assertTrue(unknownTag1.equals(unknownTag2));
        assertTrue(unknownTag2.equals(unknownTag1));

        Tag pTag3 = new Tag("p");
        assertFalse(pTag1.equals(pTag3));
        assertFalse(pTag3.equals(pTag1));
    }

    @Test
    public void testHashCode() {
        Tag pTag1 = Tag.valueOf("p");
        Tag pTag2 = Tag.valueOf("p");

        int hashCode1 = pTag1.hashCode();
        int hashCode2 = pTag2.hashCode();

        assertEquals(hashCode1, hashCode2);

        Tag imgTag1 = Tag.valueOf("img");
        Tag imgTag2 = Tag.valueOf("img");

        int hashCode3 = imgTag1.hashCode();
        int hashCode4 = imgTag2.hashCode();

        assertEquals(hashCode3, hashCode4);

        Tag unknownTag1 = new Tag("unknown");
        Tag unknownTag2 = new Tag("unknown");

        int hashCode5 = unknownTag1.hashCode();
        int hashCode6 = unknownTag2.hashCode();

        assertEquals(hashCode5, hashCode6);

        Tag pTag3 = new Tag("p");
        int hashCode7 = pTag1.hashCode();
        int hashCode8 = pTag3.hashCode();

        assertNotEquals(hashCode7, hashCode8);

        Tag imgTag3 = new Tag("img");
        int hashCode9 = imgTag1.hashCode();
        int hashCode10 = imgTag3.hashCode();

        assertNotEquals(hashCode9, hashCode10);
    }

    @Test
    public void testToString() {
        Tag pTag = Tag.valueOf("p");
        assertEquals("p", pTag.toString());
    }
}