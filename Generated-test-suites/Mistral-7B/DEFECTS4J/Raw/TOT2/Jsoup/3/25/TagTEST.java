package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testTag_getValue() {
        Tag tag = new Tag("DIV");
        assertEquals("DIV", tag.getValue());
    }

    @Test
    public void testTag_selfContain() {
        Tag tag = new Tag("DIV");
        assertFalse(tag.canContain(tag));
    }

    @Test
    public void testTag_blockTagContainInline() {
        Tag parent = new Tag("DIV");
        Tag child = new Tag("SPAN");
        assertTrue(parent.canContain(child));
    }

    @Test
    public void testTag_emptyName() {
        try {
            new Tag("");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testTag_canContainEdgeCases() {
        Tag parent = new Tag("DIV");
        Tag childBlock = new Tag("DIV");
        Tag childInline = new Tag("SPAN");
        Tag childBlockOptionalClosing = new Tag("DIV");
        Tag childInlineOptionalClosing = new Tag("SPAN");

        // Test block tag cannot contain block tag with optional closing
        assertFalse(parent.canContain(childBlockOptionalClosing));

        // Test inline tag can contain block tag
        assertTrue(parent.canContain(childBlock));

        // Test inline tag cannot contain inline tag with optional closing
        assertFalse(parent.canContain(childInlineOptionalClosing));

        // Test inline tag can contain inline tag
        assertTrue(parent.canContain(childInline));

        // Test tag with optional closing cannot contain itself
        assertFalse(childOptionalClosing.canContain(childOptionalClosing));
    }
}