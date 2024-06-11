import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest {

    @BeforeClass
    public static void setUp() {
        // Set up any necessary resources before running the tests
    }

    // Test case for getName() method
    @Test
    public void testGetName() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.getName());
    }

    // Test case for valueOf() method with known tag
    @Test
    public void testValueOfKnownTag() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.isBlock());
    }

    // Test case for valueOf() method with unknown tag
    @Test
    public void testValueOfUnknownTag() {
        Tag tag = Tag.valueOf("unknown");
        assertFalse(tag.canContainInline());
    }

    // Test case for isBlock() method
    @Test
    public void testIsBlock() {
        Tag tag = new Tag("p");
        assertTrue(tag.isBlock());
    }

    // Test case for formatAsBlock() method
    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("p");
        assertTrue(tag.formatAsBlock());
    }

    // Test case for canContainBlock() method
    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("div");
        assertTrue(tag.canContainBlock());
    }

    // Test case for isInline() method
    @Test
    public void testIsInline() {
        Tag tag = new Tag("span");
        assertTrue(tag.isInline());
    }

    // Test case for isEmpty() method
    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("img");
        assertTrue(tag.isEmpty());
    }

    // Test case for isSelfClosing() method
    @Test
    public void testIsSelfClosing() {
        Tag tag = new Tag("br");
        assertTrue(tag.isSelfClosing());
    }

    // Test case for isKnownTag() method
    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("div"));
    }

    // Test case for isKnownTag(String tagName) method with known tag
    @Test
    public void testIsKnownTagWithName() {
        assertTrue(Tag.isKnownTag("span"));
    }

    // Test case for preserveWhitespace() method
    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("pre");
        assertTrue(tag.preserveWhitespace());
    }

    // Test case for setSelfClosing() method
    @Test
    public void testSetSelfClosing() {
        Tag tag = new Tag("img");
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    // Test case for equals() method
    @Test
    public void testEquals() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("div");
        assertTrue(tag1.equals(tag2));
    }

    // Test case for hashCode() method
    @Test
    public void testHashCode() {
        Tag tag = new Tag("p");
        int hashCode = tag.hashCode();
        assertEquals(hashCode, tag.hashCode());
    }

    // Test case for toString() method
    @Test
    public void testToString() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.toString());
    }

}