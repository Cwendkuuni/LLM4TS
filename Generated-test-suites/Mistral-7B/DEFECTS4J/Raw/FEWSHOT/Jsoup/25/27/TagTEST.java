package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TagTest {

    @Test
    public void testTagCreationAndRegistration() {
        Tag tag = new Tag("p");
        Tag registeredTag = Tag.valueOf("p");
        assertEquals(tag, registeredTag);
    }

    @Test
    public void testIsBlock() {
        Tag pTag = Tag.valueOf("p");
        assertTrue(pTag.isBlock());

        Tag spanTag = Tag.valueOf("span");
        assertFalse(spanTag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag pTag = Tag.valueOf("p");
        assertTrue(pTag.formatAsBlock());

        Tag spanTag = Tag.valueOf("span");
        assertFalse(spanTag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag pTag = Tag.valueOf("p");
        assertTrue(pTag.canContainBlock());

        Tag imgTag = Tag.valueOf("img");
        assertFalse(imgTag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.isInline());

        Tag spanTag = Tag.valueOf("span");
        assertTrue(spanTag.isInline());
    }

    @Test
    public void testIsData() {
        Tag scriptTag = Tag.valueOf("script");
        assertTrue(scriptTag.isData());

        Tag divTag = Tag.valueOf("div");
        assertFalse(divTag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag imgTag = Tag.valueOf("img");
        assertTrue(imgTag.isEmpty());

        Tag pTag = Tag.valueOf("p");
        assertFalse(pTag.isEmpty());
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
        assertTrue(Tag.isKnownTag("div"));
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
    public void testSetSelfClosing() {
        Tag imgTag = Tag.valueOf("img");
        Tag selfClosingImgTag = imgTag.setSelfClosing();
        assertTrue(selfClosingImgTag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        Tag pTag1 = Tag.valueOf("p");
        Tag pTag2 = Tag.valueOf("p");
        Tag spanTag = Tag.valueOf("span");

        assertEquals(pTag1, pTag2);
        assertNotEquals(pTag1, spanTag);
    }

    @Test
    public void testHashCode() {
        Tag pTag1 = Tag.valueOf("p");
        Tag pTag2 = Tag.valueOf("p");
        Tag spanTag = Tag.valueOf("span");

        int hashCode1 = pTag1.hashCode();
        int hashCode2 = pTag2.hashCode();
        int hashCode3 = spanTag.hashCode();

        assertEquals(hashCode1, hashCode2);
        assertNotEquals(hashCode1, hashCode3);
    }

    @Test
    public void testToString() {
        Tag pTag = Tag.valueOf("p");
        assertEquals("p", pTag.toString());
    }
}