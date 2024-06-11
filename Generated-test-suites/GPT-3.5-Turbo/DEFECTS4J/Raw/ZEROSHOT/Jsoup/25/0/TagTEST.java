import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("test");
        assertEquals("test", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("test");
        assertEquals("test", tag.getName());
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("test");
        assertTrue(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("test");
        assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("test");
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = new Tag("test");
        assertFalse(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = new Tag("test");
        assertFalse(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("test");
        assertFalse(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = new Tag("test");
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        assertFalse(Tag.isKnownTag("test"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("test");
        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testSetSelfClosing() {
        Tag tag = new Tag("test");
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("test");
        Tag tag2 = new Tag("test");
        assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testHashCode() {
        Tag tag = new Tag("test");
        int hashCode = tag.hashCode();
        assertNotNull(hashCode);
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("test");
        assertEquals("test", tag.toString());
    }
    
    // Additional Test Cases
    
    @Test
    public void testGetNameWithDifferentTag() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.getName());
    }

    @Test
    public void testValueOfWithDifferentTag() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.getName());
    }

    @Test
    public void testIsBlockWithDifferentTag() {
        Tag tag = new Tag("div");
        assertTrue(tag.isBlock());
    }

    @Test
    public void testFormatAsBlockWithDifferentTag() {
        Tag tag = new Tag("div");
        assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlockWithDifferentTag() {
        Tag tag = new Tag("div");
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInlineWithDifferentTag() {
        Tag tag = new Tag("div");
        assertFalse(tag.isInline());
    }

    @Test
    public void testIsDataWithDifferentTag() {
        Tag tag = new Tag("div");
        assertFalse(tag.isData());
    }

    @Test
    public void testIsEmptyWithDifferentTag() {
        Tag tag = new Tag("div");
        assertFalse(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosingWithDifferentTag() {
        Tag tag = new Tag("div");
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTagWithDifferentTag() {
        assertTrue(Tag.isKnownTag("div"));
    }

    @Test
    public void testPreserveWhitespaceWithDifferentTag() {
        Tag tag = new Tag("div");
        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testSetSelfClosingWithDifferentTag() {
        Tag tag = new Tag("div");
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testEqualsWithDifferentTag() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("div");
        assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testHashCodeWithDifferentTag() {
        Tag tag = new Tag("div");
        int hashCode = tag.hashCode();
        assertNotNull(hashCode);
    }

    @Test
    public void testToStringWithDifferentTag() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.toString());
    }

}