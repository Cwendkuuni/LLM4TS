
import org.jsoup.parser.Tag;
import org.jsoup.helper.Validate;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class TagTest {
    private Tag tag;

    @Test
    public void testGetName() {
        tag = new Tag("p");
        assertEquals("p", tag.getName());
    }

    @Test
    public void testValueOfWithSettings() {
        Tag tag = Tag.valueOf("div", ParseSettings.preserveCase);
        assertNotNull(tag);
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("div");
        assertNotNull(tag);
    }

    @Test
    public void testIsBlock() {
        tag = new Tag("p");
        assertTrue(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        tag = new Tag("p");
        assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        tag = new Tag("p");
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        tag = new Tag("div");
        assertTrue(tag.isInline());
    }

    @Test
    public void testIsData() {
        tag = new Tag("div");
        assertFalse(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        tag = new Tag("br");
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        tag = new Tag("br");
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Tag tag = new Tag("p");
        assertTrue(tag.isKnownTag());
    }

    @Test
    public void testIsKnownTagWithName() {
        assertTrue(Tag.isKnownTag("p"));
    }

    @Test
    public void testPreserveWhitespace() {
        tag = new Tag("pre");
        assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        tag = new Tag("input");
        assertTrue(tag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        tag = new Tag("input");
        assertTrue(tag.isFormSubmittable());
    }

    @Test
    public void testSetSelfClosing() {
        tag = new Tag("input");
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("div");
        assertEquals(tag1, tag2);
    }

    @Test
    public void testHashCode() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("div");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testToString() {
        tag = new Tag("p");
        assertEquals("p", tag.toString());
    }

    // Additional test cases for edge cases and exceptions

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfNullTagName() {
        Tag.valueOf(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfEmptyTagName() {
        Tag.valueOf("");
    }

    @Test
    public void testGetKnownTag() {
        Tag tag = Tag.valueOf("p");
        assertNotNull(tag);
        assertTrue(tag.isKnownTag());
    }

    @Test
    public void testGetUnknownTag() {
        Tag tag = Tag.valueOf("customtag");
        assertNotNull(tag);
        assertFalse(tag.isKnownTag());
    }

    @Test(expected = NullPointerException.class)
    public void testRegisterNullTag() {
        Tag.register(null);
    }

    @Test
    public void testBlockTags() {
        for(String tagName : Tag.blockTags) {
            Tag tag = new Tag(tagName);
            assertTrue(tag.isBlock());
        }
    }

    @Test
    public void testInlineTags() {
        for(String tagName : Tag.inlineTags) {
            Tag tag = new Tag(tagName);
            assertTrue(tag.isInline());
        }
    }

    @Test
    public void testEmptyTags() {
        for(String tagName : Tag.emptyTags) {
            Tag tag = new Tag(tagName);
            assertTrue(tag.isEmpty());
        }
    }

    @Test
    public void testFormatAsInlineTags() {
        for(String tagName : Tag.formatAsInlineTags) {
            Tag tag = new Tag(tagName);
            assertFalse(tag.formatAsBlock());
        }
    }

    @Test
    public void testPreserveWhitespaceTags() {
        for(String tagName : Tag.preserveWhitespaceTags) {
            Tag tag = new Tag(tagName);
            assertTrue(tag.preserveWhitespace());
        }
    }

    @Test
    public void testFormListedTags() {
        for(String tagName : Tag.formListedTags) {
            Tag tag = new Tag(tagName);
            assertTrue(tag.isFormListed());
        }
    }

    @Test
    public void testFormSubmitTags() {
        for(String tagName : Tag.formSubmitTags) {
            Tag tag = new Tag(tagName);
            assertTrue(tag.isFormSubmittable());
        }
    }
}
