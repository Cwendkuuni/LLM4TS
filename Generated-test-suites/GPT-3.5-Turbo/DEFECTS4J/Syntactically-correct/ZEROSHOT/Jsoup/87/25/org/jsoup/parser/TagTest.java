import org.jsoup.parser.Tag;
import org.jsoup.helper.Validate;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("tagName");
        assertEquals("tagName", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("div", ParseSettings.preserveCase);
        assertTrue(tag.isBlock());
        assertFalse(tag.formatAsBlock());
    }

    @Test
    public void testValueOfCaseSensitive() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.isBlock());
        assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("div");
        tag.isBlock = true;
        assertTrue(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("div");
        tag.formatAsBlock = true;
        assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("div");
        tag.isBlock = true;
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = new Tag("div");
        tag.isBlock = false;
        assertTrue(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = new Tag("div");
        tag.canContainInline = false;
        assertFalse(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("div");
        assertFalse(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = new Tag("div");
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Tag tag = new Tag("p");
        assertTrue(tag.isKnownTag());
    }

    @Test
    public void testIsKnownTagWithTagName() {
        assertTrue(Tag.isKnownTag("p"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("pre");
        tag.preserveWhitespace = true;
        assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Tag tag = new Tag("input");
        tag.formList = true;
        assertTrue(tag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag tag = new Tag("input");
        tag.formSubmit = true;
        assertTrue(tag.isFormSubmittable());
    }

    @Test
    public void testSetSelfClosing() {
        Tag tag = new Tag("div");
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("div");
        assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testHashCode() {
        Tag tag = new Tag("div");
        int expectedHashCode = tag.getName().hashCode();
        expectedHashCode = 31 * expectedHashCode + (tag.isBlock ? 1 : 0);
        expectedHashCode = 31 * expectedHashCode + (tag.formatAsBlock ? 1 : 0);
        expectedHashCode = 31 * expectedHashCode + (tag.canContainInline ? 1 : 0);
        expectedHashCode = 31 * expectedHashCode + (tag.empty ? 1 : 0);
        expectedHashCode = 31 * expectedHashCode + (tag.selfClosing ? 1 : 0);
        expectedHashCode = 31 * expectedHashCode + (tag.preserveWhitespace ? 1 : 0);
        expectedHashCode = 31 * expectedHashCode + (tag.formList ? 1 : 0);
        expectedHashCode = 31 * expectedHashCode + (tag.formSubmit ? 1 : 0);
        assertEquals(expectedHashCode, tag.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.toString());
    }

    @Test
    public void testBlockTags() {
        for (String tagName : Tag.blockTags) {
            Tag tag = Tag.valueOf(tagName, ParseSettings.preserveCase);
            assertTrue(tag.isBlock());
        }
    }

    @Test
    public void testInlineTags() {
        for (String tagName : Tag.inlineTags) {
            Tag tag = Tag.valueOf(tagName, ParseSettings.preserveCase);
            assertFalse(tag.isBlock());
        }
    }

    @Test
    public void testEmptyTags() {
        for (String tagName : Tag.emptyTags) {
            Tag tag = Tag.valueOf(tagName, ParseSettings.preserveCase);
            assertTrue(tag.isEmpty());
            assertFalse(tag.canContainInline);
        }
    }

    @Test
    public void testFormatAsInlineTags() {
        for (String tagName : Tag.formatAsInlineTags) {
            Tag tag = Tag.valueOf(tagName, ParseSettings.preserveCase);
            assertFalse(tag.formatAsBlock());
        }
    }

    @Test
    public void testPreserveWhitespaceTags() {
        for (String tagName : Tag.preserveWhitespaceTags) {
            Tag tag = Tag.valueOf(tagName, ParseSettings.preserveCase);
            assertTrue(tag.preserveWhitespace());
        }
    }

    @Test
    public void testFormListedTags() {
        for (String tagName : Tag.formListedTags) {
            Tag tag = Tag.valueOf(tagName, ParseSettings.preserveCase);
            assertTrue(tag.isFormListed());
        }
    }

    @Test
    public void testFormSubmitTags() {
        for (String tagName : Tag.formSubmitTags) {
            Tag tag = Tag.valueOf(tagName, ParseSettings.preserveCase);
            assertTrue(tag.isFormSubmittable());
        }
    }
}