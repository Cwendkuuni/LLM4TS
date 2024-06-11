import org.junit.Test;
import org.jsoup.parser.ParseSettings;
import org.jsoup.helper.Validate;

public class TagTest {
    
    @Test
    public void testGetName() {
        Tag tag = new Tag("test");
        assertEqulas("test", tag.getName());
    }

    @Test
    public void testValueOf() {
        ParseSettings settings = ParseSettings.preserveCase;
        Tag tag = Tag.valueOf("test", settings);
        assertTrue(tag.isInline());
    }

    @Test
    public void testValueOfWithoutSettings() {
        Tag tag = Tag.valueOf("test");
        assertFalse(tag.isBlock());
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("test");
        tag.isBlock = true;
        assertTrue(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("test");
        tag.formatAsBlock = false;
        assertFalse(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("test");
        assertFalse(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = new Tag("test");
        tag.isBlock = false;
        assertTrue(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = new Tag("test");
        tag.canContainInline = false;
        assertFalse(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("test");
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = new Tag("test");
        tag.empty = true;
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Tag tag = new Tag("test");
        assertFalse(tag.isKnownTag());
    }

    @Test
    public void testIsKnownTagWithName() {
        assertTrue(Tag.isKnownTag("test"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("test");
        tag.preserveWhitespace = true;
        assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Tag tag = new Tag("test");
        assertTrue(tag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag tag = new Tag("test");
        assertTrue(tag.isFormSubmittable());
    }

    @Test
    public void testSetSelfClosing() {
        Tag tag = new Tag("test");
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("test1");
        Tag tag2 = new Tag("test2");
        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testHashCode() {
        Tag tag = new Tag("test");
        assertEquals(tag.getName().hashCode(), tag.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("test");
        assertEquals("test", tag.toString());
    }
}