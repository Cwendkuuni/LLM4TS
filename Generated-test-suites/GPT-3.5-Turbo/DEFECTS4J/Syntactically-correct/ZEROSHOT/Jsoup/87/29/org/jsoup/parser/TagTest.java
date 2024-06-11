import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("tagName");
        assertEquals("tagName", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
        assertTrue(tag.isBlock());
        assertTrue(tag.formatAsBlock());
        assertTrue(tag.canContainBlock());
        assertFalse(tag.isInline());
    }

    @Test
    public void testValueOfWithSettings() {
        ParseSettings settings = new ParseSettings(false, false);
        Tag tag = Tag.valueOf("div", settings);
        assertEquals("div", tag.getName());
        assertTrue(tag.isBlock());
        assertTrue(tag.formatAsBlock());
        assertTrue(tag.canContainBlock());
        assertFalse(tag.isInline());
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("tagName");
        assertTrue(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("tagName");
        assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("tagName");
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = new Tag("tagName");
        assertFalse(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = new Tag("tagName");
        assertFalse(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("tagName");
        assertFalse(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = new Tag("tagName");
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Tag tag = new Tag("tagName");
        assertFalse(tag.isKnownTag());
    }

    @Test
    public void testIsKnownTagWithName() {
        assertFalse(Tag.isKnownTag("tagName"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("tagName");
        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Tag tag = new Tag("tagName");
        assertFalse(tag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag tag = new Tag("tagName");
        assertFalse(tag.isFormSubmittable());
    }

    @Test
    public void testSetSelfClosing() {
        Tag tag = new Tag("tagName");
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("tagName");
        Tag tag2 = new Tag("tagName");
        assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testHashCode() {
        Tag tag = new Tag("tagName");
        assertEquals(514343345, tag.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("tagName");
        assertEquals("tagName", tag.toString());
    }
}