import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    // Test for getName method
    @Test
    public void testGetName() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.getName());
    }

    // Test for valueOf method
    @Test
    public void testValueOf() {
        // Known tag
        Tag tag1 = Tag.valueOf("p");
        assertEquals("p", tag1.getName());

        // Unknown tag
        Tag tag2 = Tag.valueOf("custom");
        assertNotEquals("custom", tag2.getName());
    }

    // Test for isBlock method
    @Test
    public void testIsBlock() {
        // Block tag
        Tag tag1 = new Tag("div");
        assertTrue(tag1.isBlock());

        // Inline tag
        Tag tag2 = new Tag("span");
        assertFalse(tag2.isBlock());
    }

    // Test for formatAsBlock method
    @Test
    public void testFormatAsBlock() {
        // Format as block
        Tag tag1 = new Tag("div");
        assertTrue(tag1.formatAsBlock());

        // Format as inline
        Tag tag2 = new Tag("span");
        assertFalse(tag2.formatAsBlock());
    }

    // Test for canContainBlock method
    @Test
    public void testCanContainBlock() {
        // Can contain block
        Tag tag1 = new Tag("div");
        assertTrue(tag1.canContainBlock());

        // Cannot contain block
        Tag tag2 = new Tag("span");
        assertFalse(tag2.canContainBlock());
    }

    // Test for isInline method
    @Test
    public void testIsInline() {
        // Inline tag
        Tag tag1 = new Tag("span");
        assertTrue(tag1.isInline());

        // Block tag
        Tag tag2 = new Tag("div");
        assertFalse(tag2.isInline());
    }

    // Test for isData method
    @Test
    public void testIsData() {
        // Data only tag
        Tag tag1 = new Tag("script");
        assertTrue(tag1.isData());

        // Non-data tag
        Tag tag2 = new Tag("span");
        assertFalse(tag2.isData());
    }

    // Test for isEmpty method
    @Test
    public void testIsEmpty() {
        // Empty tag
        Tag tag1 = new Tag("img");
        assertTrue(tag1.isEmpty());

        // Non-empty tag
        Tag tag2 = new Tag("div");
        assertFalse(tag2.isEmpty());
    }

    // Test for isSelfClosing method
    @Test
    public void testIsSelfClosing() {
        // Self closing tag
        Tag tag1 = new Tag("img");
        assertTrue(tag1.isSelfClosing());

        // Non-self closing tag
        Tag tag2 = new Tag("div");
        assertFalse(tag2.isSelfClosing());
    }

    // Test for isKnownTag method
    @Test
    public void testIsKnownTag() {
        // Known tag
        assertTrue(Tag.isKnownTag("div"));

        // Unknown tag
        assertFalse(Tag.isKnownTag("custom"));
    }

    // Test for preserveWhitespace method
    @Test
    public void testPreserveWhitespace() {
        // Preserve whitespace tag
        Tag tag1 = new Tag("pre");
        assertTrue(tag1.preserveWhitespace());

        // Non-preserve whitespace tag
        Tag tag2 = new Tag("span");
        assertFalse(tag2.preserveWhitespace());
    }

    // Test for equals method
    @Test
    public void testEquals() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("div");
        Tag tag3 = new Tag("span");

        // Same tag names, should be equal
        assertTrue(tag1.equals(tag2));

        // Different tag names, should not be equal
        assertFalse(tag1.equals(tag3));
    }

}