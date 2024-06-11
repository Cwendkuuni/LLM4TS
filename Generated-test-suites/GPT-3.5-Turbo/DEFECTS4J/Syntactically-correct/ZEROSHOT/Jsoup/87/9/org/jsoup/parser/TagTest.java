import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    // Testing getName method
    @Test
    public void testGetName() {
        Tag tag = new Tag("test");
        assertEquals("test", tag.getName());
    }

    // Testing valueOf method with settings
    @Test
    public void testValueOfWithSettings() {
        Tag tag = Tag.valueOf("p", new ParseSettings());
        assertEquals("p", tag.getName());
        assertTrue(tag.isBlock());
        assertTrue(tag.formatAsBlock());
        assertTrue(tag.canContainInline());
        assertFalse(tag.isEmpty());
        assertFalse(tag.isSelfClosing());
        assertFalse(tag.preserveWhitespace());
        assertFalse(tag.isFormListed());
        assertFalse(tag.isFormSubmittable());
    }

    // Testing valueOf method
    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
        assertTrue(tag.isBlock());
        assertTrue(tag.formatAsBlock());
        assertTrue(tag.canContainInline());
        assertFalse(tag.isEmpty());
        assertFalse(tag.isSelfClosing());
        assertFalse(tag.preserveWhitespace());
        assertFalse(tag.isFormListed());
        assertFalse(tag.isFormSubmittable());
    }

    // Testing isBlock method
    @Test
    public void testIsBlock() {
        Tag tag = new Tag("test");
        assertTrue(tag.isBlock());
    }

    // Testing formatAsBlock method
    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("test");
        assertTrue(tag.formatAsBlock());
    }

    // Testing canContainBlock method
    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("test");
        assertTrue(tag.canContainBlock());
    }

    // Testing isInline method
    @Test
    public void testIsInline() {
        Tag tag = new Tag("test");
        assertFalse(tag.isInline());
    }

    // Testing isData method
    @Test
    public void testIsData() {
        Tag tag = new Tag("test");
        assertFalse(tag.isData());
    }

    // Testing isEmpty method
    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("test");
        assertFalse(tag.isEmpty());
    }

    // Testing isSelfClosing method
    @Test
    public void testIsSelfClosing() {
        Tag tag = new Tag("test");
        assertFalse(tag.isSelfClosing());
    }

    // Testing isKnownTag method
    @Test
    public void testIsKnownTag() {
        Tag tag = new Tag("test");
        assertFalse(tag.isKnownTag());
    }

    // Testing isKnownTag method with tag name
    @Test
    public void testIsKnownTagWithName() {
        assertTrue(Tag.isKnownTag("html"));
        assertFalse(Tag.isKnownTag("test"));
    }

    // Testing preserveWhitespace method
    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("test");
        assertFalse(tag.preserveWhitespace());
    }

    // Testing isFormListed method
    @Test
    public void testIsFormListed() {
        Tag tag = new Tag("test");
        assertFalse(tag.isFormListed());
    }

    // Testing isFormSubmittable method
    @Test
    public void testIsFormSubmittable() {
        Tag tag = new Tag("test");
        assertFalse(tag.isFormSubmittable());
    }

    // Testing equals method
    @Test
    public void testEquals() {
        Tag tag1 = new Tag("test");
        Tag tag2 = new Tag("test");
        assertTrue(tag1.equals(tag2));
    }

    // Testing hashCode method
    @Test
    public void testHashCode() {
        Tag tag = new Tag("test");
        assertEquals(110729, tag.hashCode());
    }

    // Testing toString method
    @Test
    public void testToString() {
        Tag tag = new Tag("test");
        assertEquals("test", tag.toString());
    }

}