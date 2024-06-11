package org.jsoup.parser;
import org.jsoup.parser.ParseSettings;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TagTest {

    private static ParseSettings parseSettings;

    @BeforeClass
    public static void setUp() {
        parseSettings = ParseSettings.preserveCase;
    }

    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("div", parseSettings);
        assertEquals("div", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("p", parseSettings);
        assertFalse(tag.isBlock());
        assertTrue(tag.formatAsBlock());
        assertFalse(tag.canContainBlock());

        Tag unknownTag = Tag.valueOf("unknown", parseSettings);
        assertFalse(unknownTag.isBlock());
        assertTrue(unknownTag.formatAsBlock());
        assertFalse(unknownTag.canContainBlock());
    }

    @Test
    public void testValueOfCaseSensitive() {
        Tag tag = Tag.valueOf("h1");
        assertEquals(tag.getName(), "h1");

        Tag anotherTag = Tag.valueOf("H1");
        assertFalse(tag.equals(anotherTag));
    }

    @Test
    public void testIsBlock() {
        Tag blockTag = Tag.valueOf("p", parseSettings);
        assertTrue(blockTag.isBlock());

        Tag inlineTag = Tag.valueOf("a", parseSettings);
        assertFalse(inlineTag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag blockTag = Tag.valueOf("p", parseSettings);
        assertTrue(blockTag.formatAsBlock());

        Tag inlineTag = Tag.valueOf("a", parseSettings);
        assertFalse(inlineTag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag blockTag = Tag.valueOf("p", parseSettings);
        assertTrue(blockTag.canContainBlock());

        Tag inlineTag = Tag.valueOf("a", parseSettings);
        assertFalse(inlineTag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag blockTag = Tag.valueOf("p", parseSettings);
        assertFalse(blockTag.isInline());

        Tag inlineTag = Tag.valueOf("a", parseSettings);
        assertTrue(inlineTag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("img", parseSettings);
        assertFalse(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("img", parseSettings);
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag emptyTag = Tag.valueOf("img", parseSettings);
        assertTrue(emptyTag.isSelfClosing());

        Tag nonEmptyTag = Tag.valueOf("div", parseSettings);
        assertFalse(nonEmptyTag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Tag knownTag = Tag.valueOf("p", parseSettings);
        assertTrue(knownTag.isKnownTag());

        Tag unknownTag = Tag.valueOf("unknown", parseSettings);
        assertFalse(unknownTag.isKnownTag());
    }

    @Test
    public void testIsKnownTagWithTagName() {
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("pre", parseSettings);
        assertTrue(tag.preserveWhitespace());

        Tag nonPreserveTag = Tag.valueOf("div", parseSettings);
        assertFalse(nonPreserveTag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Tag listedTag = Tag.valueOf("input", parseSettings);
        assertTrue(listedTag.isFormListed());

        Tag nonListedTag = Tag.valueOf("a", parseSettings);
        assertFalse(nonListedTag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag submittableTag = Tag.valueOf("input", parseSettings);
        assertTrue(submittableTag.isFormSubmittable());

        Tag nonSubmittableTag = Tag.valueOf("a", parseSettings);
        assertFalse(nonSubmittableTag.isFormSubmittable());
    }   

}