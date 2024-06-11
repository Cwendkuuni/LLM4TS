
import org.jsoup.helper.Validate;
import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest {
    
    /**
     * Test for getName method
     */
    @Test
    public void testGetName() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.getName());
    }

    /**
     * Test for isBlock method
     */
    @Test
    public void testIsBlock() {
        Tag tag = new Tag("div");
        assertTrue(tag.isBlock());
    }
    
    /**
     * Test for formatAsBlock method
     */
    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("div");
        assertTrue(tag.formatAsBlock());
    }

    /**
     * Test for canContainBlock method
     * Deprecated method, should return the same result as isBlock method
     */
    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("div");
        assertTrue(tag.canContainBlock());
    }

    /**
     * Test for isInline method
     */
    @Test
    public void testIsInline() {
        Tag tag = new Tag("div");
        assertFalse(tag.isInline());
    }

    /**
     * Test for isData method
     */
    @Test
    public void testIsData() {
        Tag tag = new Tag("div");
        assertFalse(tag.isData());
    }

    /**
     * Test for isEmpty method
     */
    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("div");
        assertFalse(tag.isEmpty());
    }
    
    /**
     * Test for isSelfClosing method
     */
    @Test
    public void testIsSelfClosing() {
        Tag tag = new Tag("div");
        assertFalse(tag.isSelfClosing());
    }

    /**
     * Test for isKnownTag method
     */
    @Test
    public void testIsKnownTag() {
        Tag tag = new Tag("div");
        assertFalse(tag.isKnownTag());
    }

    /**
     * Test for isKnownTag method with a known tag name
     */
    @Test
    public void testIsKnownKnownTag() {
        Tag tag = new Tag("h1");
        assertTrue(tag.isKnownTag());
    }

    /**
     * Test for isKnownTag method with an unknown tag name
     */
    @Test
    public void testIsKnownUnknownTag() {
        Tag tag = new Tag("unknowntag");
        assertFalse(tag.isKnownTag());
    }

    /**
     * Test for preserveWhitespace method
     */
    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("div");
        assertFalse(tag.preserveWhitespace());
    }

    /**
     * Test for isFormListed method
     */
    @Test
    public void testIsFormListed() {
        Tag tag = new Tag("div");
        assertFalse(tag.isFormListed());
    }

    /**
     * Test for isFormSubmittable method
     */
    @Test
    public void testIsFormSubmittable() {
        Tag tag = new Tag("div");
        assertFalse(tag.isFormSubmittable());
    }

    /**
     * Test for setSelfClosing method
     */
    @Test
    public void testSetSelfClosing() {
        Tag tag = new Tag("div");
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    /**
     * Test for equals method
     */
    @Test
    public void testEquals() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("div");
        assertTrue(tag1.equals(tag2));
    }

    /**
     * Test for hashCode method
     */
    @Test
    public void testHashCode() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("div");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    /**
     * Test for toString method
     */
    @Test
    public void testToString() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.toString());
    }
}
