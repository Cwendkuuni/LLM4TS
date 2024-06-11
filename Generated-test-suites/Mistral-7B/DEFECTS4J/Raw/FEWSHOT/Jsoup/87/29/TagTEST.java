package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TagTest {

    @Test
    public void testTagValues() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");
        Tag unknownTag = Tag.valueOf("unknown");

        assertEquals("p", pTag.getName());
        assertEquals("p", pTag.toString());
        assertEquals("div", divTag.getName());
        assertEquals("div", divTag.toString());
        assertEquals("unknown", unknownTag.getName());
        assertEquals("unknown", unknownTag.toString());

        assertTrue(pTag.isBlock());
        assertTrue(pTag.formatAsBlock());
        assertTrue(pTag.canContainInline());
        assertFalse(pTag.isEmpty());
        assertFalse(pTag.isSelfClosing());
        assertFalse(pTag.preserveWhitespace());
        assertFalse(pTag.isFormListed());
        assertFalse(pTag.isFormSubmittable());

        assertTrue(divTag.isBlock());
        assertTrue(divTag.formatAsBlock());
        assertTrue(divTag.canContainInline());
        assertFalse(divTag.isEmpty());
        assertFalse(divTag.isSelfClosing());
        assertFalse(divTag.preserveWhitespace());
        assertFalse(divTag.isFormListed());
        assertFalse(divTag.isFormSubmittable());

        assertNull(unknownTag.isBlock);
        assertNull(unknownTag.formatAsBlock);
        assertNull(unknownTag.canContainInline);
        assertNull(unknownTag.empty);
        assertNull(unknownTag.selfClosing);
        assertNull(unknownTag.preserveWhitespace);
        assertNull(unknownTag.formList);
        assertNull(unknownTag.formSubmit);
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("p"));
        assertTrue(Tag.isKnownTag("div"));
        assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testRegisteredTags() {
        Tag pTag = Tag.valueOf("p");
        Tag divTag = Tag.valueOf("div");

        assertEquals(Tag.class, pTag.getClass());
        assertEquals(Tag.class, divTag.getClass());

        assertEquals(2, Tag.tags.size());

        assertTrue(Tag.tags.containsKey("p"));
        assertTrue(Tag.tags.containsKey("div"));
        assertFalse(Tag.tags.containsKey("unknown"));
    }

    @Test
    public void testEmptyTags() {
        Tag imgTag = Tag.valueOf("img");
        assertTrue(imgTag.isEmpty());
        assertTrue(imgTag.isData());
        assertFalse(imgTag.canContainInline());
    }

    @Test
    public void testFormatAsInlineTags() {
        Tag pTag = Tag.valueOf("p");
        Tag preTag = Tag.valueOf("pre");

        assertFalse(pTag.formatAsInline());
        assertTrue(preTag.formatAsInline());
    }

    @Test
    public void testPreserveWhitespaceTags() {
        Tag preTag = Tag.valueOf("pre");
        Tag textareaTag = Tag.valueOf("textarea");

        assertTrue(preTag.preserveWhitespace());
        assertTrue(textareaTag.preserveWhitespace());
    }

    @Test
    public void testFormListedTags() {
        Tag inputTag = Tag.valueOf("input");
        Tag buttonTag = Tag.valueOf("button");

        assertTrue(inputTag.isFormListed());
        assertTrue(buttonTag.isFormListed());
    }

    @Test
    public void testFormSubmittableTags() {
        Tag inputTag = Tag.valueOf("input");
        Tag buttonTag = Tag.valueOf("button");

        assertTrue(inputTag.isFormSubmittable());
        assertTrue(buttonTag.isFormSubmittable());
    }
}