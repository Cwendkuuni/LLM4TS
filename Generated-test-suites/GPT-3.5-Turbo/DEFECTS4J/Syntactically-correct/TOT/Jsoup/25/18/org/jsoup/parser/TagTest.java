
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest {

    @BeforeClass
    public static void setUp() {
        // Pre-defined tags used for some of the test cases
        Tag.valueOf("p");
        Tag.valueOf("div");
        Tag.valueOf("img");
        Tag.valueOf("meta");
        Tag.valueOf("blockquote");
        Tag.valueOf("ol");
        Tag.valueOf("ul");
        Tag.valueOf("pre");
        Tag.valueOf("span");
        Tag.valueOf("input");
        Tag.valueOf("select");
        Tag.valueOf("textarea");
        Tag.valueOf("label");
        Tag.valueOf("button");
        Tag.valueOf("optgroup");
        Tag.valueOf("option");
        Tag.valueOf("caption");
        Tag.valueOf("thead");
        Tag.valueOf("tfoot");
        Tag.valueOf("tbody");
        Tag.valueOf("colgroup");
        Tag.valueOf("col");
        Tag.valueOf("tr");
        Tag.valueOf("th");
        Tag.valueOf("td");
        Tag.valueOf("script");
        Tag.valueOf("style");
        Tag.valueOf("wbr");
        Tag.valueOf("embed");
        Tag.valueOf("hr");
        Tag.valueOf("command");
        Tag.valueOf("device");
    }

    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("tagName");
        assertEquals("tagName", tag.getName());
    }

    @Test
    public void testValueOf() {
        // Test with known tag
        Tag tag1 = Tag.valueOf("p");
        assertTrue(tag1.isKnownTag());

        // Test with unknown tag
        Tag tag2 = Tag.valueOf("unknown");
        assertFalse(tag2.isKnownTag());

        // Test with empty tag name
        try {
            Tag tag3 = Tag.valueOf("");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("tagName must not be empty.", e.getMessage());
        }

    }

    @Test
    public void testIsBlock() {
        // Test with block tag
        Tag tag1 = Tag.valueOf("p");
        assertTrue(tag1.isBlock());

        // Test with inline tag
        Tag tag2 = Tag.valueOf("span");
        assertFalse(tag2.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        // Test with block tag
        Tag tag1 = Tag.valueOf("p");
        assertTrue(tag1.formatAsBlock());

        // Test with inline tag
        Tag tag2 = Tag.valueOf("span");
        assertFalse(tag2.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        // Test with tag that can contain block tags
        Tag tag1 = Tag.valueOf("div");
        assertTrue(tag1.canContainBlock());

        // Test with tag that cannot contain block tags
        Tag tag2 = Tag.valueOf("meta");
        assertFalse(tag2.canContainBlock());
    }

    @Test
    public void testIsInline() {
        // Test with block tag
        Tag tag1 = Tag.valueOf("p");
        assertFalse(tag1.isInline());

        // Test with inline tag
        Tag tag2 = Tag.valueOf("span");
        assertTrue(tag2.isInline());
    }

    @Test
    public void testIsData() {
        // Test with tag that can contain inline tags
        Tag tag1 = Tag.valueOf("p");
        assertFalse(tag1.isData());

        // Test with tag that cannot contain inline tags
        Tag tag2 = Tag.valueOf("img");
        assertFalse(tag2.isData());

        // Test with empty tag
        Tag tag3 = Tag.valueOf("img");
        assertFalse(tag3.isData());
    }

    @Test
    public void testIsEmpty() {
        // Test with non-empty tag
        Tag tag1 = Tag.valueOf("p");
        assertFalse(tag1.isEmpty());

        // Test with empty tag
        Tag tag2 = Tag.valueOf("img");
        assertTrue(tag2.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        // Test with empty tag
        Tag tag1 = Tag.valueOf("img");
        assertTrue(tag1.isSelfClosing());

        // Test with tag that can self close
        Tag tag2 = Tag.valueOf("div");
        assertFalse(tag2.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        // Test with known tag
        assertTrue(Tag.isKnownTag("p"));

        // Test with unknown tag
        assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testSetSelfClosing() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isSelfClosing());
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        // Test with equal tags
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        assertTrue(tag1.equals(tag2));

        // Test with different tags
        Tag tag3 = Tag.valueOf("p");
        Tag tag4 = Tag.valueOf("div");
        assertFalse(tag3.equals(tag4));

        // Test with different object type
        assertFalse(tag3.equals("p"));
    }

    @Test
    public void testHashCode() {
        // Test with equal tags
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        assertEquals(tag1.hashCode(), tag2.hashCode());

        // Test with different tags
        Tag tag3 = Tag.valueOf("p");
        Tag tag4 = Tag.valueOf("div");
        assertNotEquals(tag3.hashCode(), tag4.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.toString());
    }
}

