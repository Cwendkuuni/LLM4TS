import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.parser.ParseSettings;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("test");
        assertEquals("test", tag.getName());
    }

    @Test
    public void testValueOfWithSettings() {
        Tag tag = Tag.valueOf("test", ParseSettings.html);
        assertNotNull(tag);
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("test");
        assertNotNull(tag);
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
        Tag tag = new Tag("test");
        assertFalse(tag.isKnownTag());
    }

    @Test
    public void testIsKnownTagWithName() {
        assertFalse(Tag.isKnownTag("test"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("test");
        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Tag tag = new Tag("test");
        assertFalse(tag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag tag = new Tag("test");
        assertFalse(tag.isFormSubmittable());
    }

    @Test
    public void testEquals() {
        Tag tag = new Tag("test");
        assertTrue(tag.equals(tag));
    }

    @Test
    public void testHashCode() {
        Tag tag = new Tag("test");
        assertEquals(tag.hashCode(), tag.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("test");
        assertEquals("test", tag.toString());
    }
}