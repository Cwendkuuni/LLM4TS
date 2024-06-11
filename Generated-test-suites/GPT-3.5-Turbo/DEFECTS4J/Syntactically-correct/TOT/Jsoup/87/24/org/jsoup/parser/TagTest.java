import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.ParseSettings;

import static org.junit.Assert.*;

public class TagTest {
    private Tag tag;

    @Before
    public void setUp() {
        tag = Tag.valueOf("p");
    }

    @Test
    public void testGetName() {
        assertEquals("p", tag.getName());
    }

    @Test
    public void testValueOfWithSettings() {
        tag = Tag.valueOf("P", ParseSettings.preserveCase);
        assertEquals("P", tag.getName());
    }

    @Test
    public void testValueOfWithoutSettings() {
        tag = Tag.valueOf("P");
        assertEquals("p", tag.getName());
    }

    @Test
    public void testIsBlock() {
        assertTrue(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        assertFalse(tag.isInline());
    }

    @Test
    public void testIsData() {
        assertFalse(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(tag.isKnownTag());
    }

    @Test
    public void testIsKnownTagWithName() {
        assertTrue(Tag.isKnownTag("p"));
    }

    @Test
    public void testIsKnownTagWithInvalidName() {
        assertFalse(Tag.isKnownTag("test"));
    }

    @Test
    public void testPreserveWhitespace() {
        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        assertFalse(tag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        assertFalse(tag.isFormSubmittable());
    }

    @Test
    public void testSetSelfClosing() {
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testEqualsWithItself() {
        assertTrue(tag.equals(tag));
    }

    @Test
    public void testEqualsWithDifferentTag() {
        Tag tag2 = Tag.valueOf("div");
        assertFalse(tag.equals(tag2));
    }

    @Test
    public void testToString() {
        assertEquals("p", tag.toString());
    }

    @Test
    public void testBlockTags() {
        for (String tag : Tag.blockTags) {
            assertNotNull(Tag.valueOf(tag));
        }
    }

    @Test
    public void testInlineTags() {
        for (String tag : Tag.inlineTags) {
            assertNotNull(Tag.valueOf(tag));
        }
    }

    @Test
    public void testEmptyTags() {
        for (String tag : Tag.emptyTags) {
            Tag emptyTag = Tag.valueOf(tag);
            assertTrue(emptyTag.isEmpty());
            assertFalse(emptyTag.canContainBlock());
        }
    }

    @Test
    public void testFormatAsInlineTags() {
        for (String tag : Tag.formatAsInlineTags) {
            assertFalse(Tag.valueOf(tag).formatAsBlock());
        }
    }

    @Test
    public void testPreserveWhitespaceTags() {
        for (String tag : Tag.preserveWhitespaceTags) {
            assertTrue(Tag.valueOf(tag).preserveWhitespace());
        }
    }

    @Test
    public void testFormListedTags() {
        for (String tag : Tag.formListedTags) {
            assertTrue(Tag.valueOf(tag).isFormListed());
        }
    }

    @Test
    public void testFormSubmitTags() {
        for (String tag : Tag.formSubmitTags) {
            assertTrue(Tag.valueOf(tag).isFormSubmittable());
        }
    }
}