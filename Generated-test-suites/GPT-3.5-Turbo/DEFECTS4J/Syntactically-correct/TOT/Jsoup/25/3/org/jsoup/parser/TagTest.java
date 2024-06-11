import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Tag;

public class TagTest {

    private Tag tag1;
    private Tag tag2;
    private Tag tag3;

    @Before
    public void setup() {
        tag1 = Tag.valueOf("p");
        tag2 = Tag.valueOf("div");
        tag3 = Tag.valueOf("a");
    }

    // Test for getName() method
    @Test
    public void testGetName() {
        assertEquals("p", tag1.getName());
        assertEquals("div", tag2.getName());
        assertEquals("a", tag3.getName());
    }

    // Test for isBlock() method
    @Test
    public void testIsBlock() {
        assertTrue(tag1.isBlock());
        assertTrue(tag2.isBlock());
        assertFalse(tag3.isBlock());
    }

    // Test for formatAsBlock() method
    @Test
    public void testFormatAsBlock() {
        assertTrue(tag1.formatAsBlock());
        assertTrue(tag2.formatAsBlock());
        assertTrue(tag3.formatAsBlock());
    }

    // Test for canContainBlock() method
    @Test
    public void testCanContainBlock() {
        assertTrue(tag1.canContainBlock());
        assertTrue(tag2.canContainBlock());
        assertTrue(tag3.canContainBlock());
    }

    // Test for isInline() method
    @Test
    public void testIsInline() {
        assertFalse(tag1.isInline());
        assertFalse(tag2.isInline());
        assertTrue(tag3.isInline());
    }

    // Test for isData() method
    @Test
    public void testIsData() {
        assertFalse(tag1.isData());
        assertFalse(tag2.isData());
        assertFalse(tag3.isData());
    }

    // Test for isEmpty() method
    @Test
    public void testIsEmpty() {
        assertFalse(tag1.isEmpty());
        assertFalse(tag2.isEmpty());
        assertFalse(tag3.isEmpty());
    }

    // Test for isSelfClosing() method
    @Test
    public void testIsSelfClosing() {
        assertFalse(tag1.isSelfClosing());
        assertFalse(tag2.isSelfClosing());
        assertFalse(tag3.isSelfClosing());
    }

    // Test for isKnownTag() method
    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("p"));
        assertTrue(Tag.isKnownTag("div"));
        assertTrue(Tag.isKnownTag("a"));
        assertFalse(Tag.isKnownTag("tag"));
    }

    // Test for preserveWhitespace() method
    @Test
    public void testPreserveWhitespace() {
        assertFalse(tag1.preserveWhitespace());
        assertFalse(tag2.preserveWhitespace());
        assertFalse(tag3.preserveWhitespace());
    }

    // Test for setSelfClosing() method
    @Test
    public void testSetSelfClosing() {
        Tag tag4 = Tag.valueOf("img");
        tag4.setSelfClosing();
        assertTrue(tag4.isSelfClosing());
    }

    // Test for equals() method
    @Test
    public void testEquals() {
        Tag tag5 = Tag.valueOf("p");
        Tag tag6 = Tag.valueOf("div");

        assertTrue(tag1.equals(tag5));
        assertFalse(tag1.equals(tag2));
        assertFalse(tag1.equals(tag6));
    }

    // Test for hashCode() method
    @Test
    public void testHashCode() {
        Tag tag5 = Tag.valueOf("p");
        Tag tag6 = Tag.valueOf("div");

        assertEquals(tag1.hashCode(), tag5.hashCode());
        assertNotEquals(tag1.hashCode(), tag2.hashCode());
        assertNotEquals(tag1.hashCode(), tag6.hashCode());
    }

    // Test for toString() method
    @Test
    public void testToString() {
        assertEquals("p", tag1.toString());
        assertEquals("div", tag2.toString());
        assertEquals("a", tag3.toString());
    }
}