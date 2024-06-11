
import org.jsoup.parser.*;
import org.jsoup.helper.Validate;
import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.*;
import org.junit.Test;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("test");
        assertEquals("test", tag.getName());
    }

    @Test
    public void testValueOfWithSettings() {
        ParseSettings settings = ParseSettings.htmlDefault;
        Tag tag = Tag.valueOf("div", settings);
        assertNotNull(tag);
        assertEquals("div", tag.getName());
    }

    @Test
    public void testValueOfWithoutSettings() {
        Tag tag = Tag.valueOf("p");
        assertNotNull(tag);
        assertEquals("p", tag.getName());
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
    public void testIsKnownTagWithNameInTags() {
        Tag.register(new Tag("test"));
        assertTrue(Tag.isKnownTag("test"));
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
    public void testSetSelfClosing() {
        Tag tag = new Tag("test");
        Tag selfClosingTag = tag.setSelfClosing();
        assertNotSame(tag, selfClosingTag);
        assertTrue(selfClosingTag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("test");
        Tag tag2 = new Tag("test");
        assertEquals(tag1, tag2);
    }

    @Test
    public void testHashCode() {
        Tag tag = new Tag("test");
        assertNotNull(tag.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("test");
        assertEquals("test", tag.toString());
    }
}
