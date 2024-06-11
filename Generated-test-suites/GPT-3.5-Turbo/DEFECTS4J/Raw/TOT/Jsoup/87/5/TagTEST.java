import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest {

    private ParseSettings parseSettings;
    private Tag unknownTag;

    @Before
    public void setUp() {
        parseSettings = ParseSettings.preserveCase;
        unknownTag = Tag.valueOf("unknown", parseSettings);
    }

    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("div", parseSettings);
        assertEquals("div", tag.getName());
    }

    @Test
    public void testValueOfWithSettings_knownTag() {
        Tag tag = Tag.valueOf("div", parseSettings);
        Tag expectedTag = Tag.valueOf("div");

        assertEquals(expectedTag, tag);
    }

    @Test
    public void testValueOfWithSettings_unknownTag() {
        Tag tag = Tag.valueOf("unknown", parseSettings);

        assertFalse(tag.isBlock());
        assertFalse(tag.formatAsBlock());
        assertTrue(tag.isInline());
        assertFalse(tag.canContainBlock());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("span");

        assertTrue(tag.isBlock());
        assertTrue(tag.formatAsBlock());
        assertFalse(tag.isInline());
        assertTrue(tag.canContainBlock());
        assertFalse(tag.isEmpty());
        assertFalse(tag.isSelfClosing());
        assertFalse(tag.preserveWhitespace());
        assertFalse(tag.isFormListed());
        assertFalse(tag.isFormSubmittable());
    }

    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("h1");

        assertTrue(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = Tag.valueOf("h1");

        assertEquals("h1", tag.getName());
        assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("p");

        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("span");

        assertTrue(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("script");

        assertFalse(tag.canContainInline());
        assertFalse(tag.isEmpty());
        assertTrue(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("img");

        assertTrue(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = Tag.valueOf("img");

        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Tag tag = Tag.valueOf("div");

        assertTrue(tag.isKnownTag());
    }

    @Test
    public void testIsKnownTag_unknownTag() {
        assertFalse(unknownTag.isKnownTag());
    }

    @Test
    public void testIsKnownTag_withTagName() {
        assertTrue(Tag.isKnownTag("div"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("pre");

        assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Tag tag = Tag.valueOf("input");

        assertTrue(tag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag tag = Tag.valueOf("input");

        assertTrue(tag.isFormSubmittable());
    }

    @Test
    public void testSetSelfClosing() {
        Tag tag = new Tag("customTag");

        assertFalse(tag.isSelfClosing());
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("div");

        assertEquals(tag1, tag2);
    }

    @Test
    public void testHashCode() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("div");

        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("div");

        assertEquals("div", tag.toString());
    }

    @Test
    public void testIsBlockTagsLength() {
        assertEquals(86, Tag.blockTags.length);
    }

    @Test
    public void testIsInlineTagsLength() {
        assertEquals(77, Tag.inlineTags.length);
    }

    @Test
    public void testEmptyTagsLength() {
        assertEquals(14, Tag.emptyTags.length);
    }

    @Test
    public void testFormatAsInlineTagsLength() {
        assertEquals(16, Tag.formatAsInlineTags.length);
    }

    @Test
    public void testPreserveWhitespaceTagsLength() {
        assertEquals(4, Tag.preserveWhitespaceTags.length);
    }

    @Test
    public void testFormListedTagsLength() {
        assertEquals(8, Tag.formListedTags.length);
    }

    @Test
    public void testFormSubmitTagsLength() {
        assertEquals(5, Tag.formSubmitTags.length);
    }
}