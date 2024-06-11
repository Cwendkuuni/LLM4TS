import org.jsoup.parser.Tag;
import org.jsoup.parser.ParseSettings;
import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testName() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.isBlock());
    }

    @Test
    public void testValueOfCaseSensitive() {
        Tag tag = Tag.valueOf("DIV");
        assertTrue(tag.isInline());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("span");
        assertFalse(tag.formatAsBlock());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("img");
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = new Tag("img").setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("div"));
        assertFalse(Tag.isKnownTag("custom"));
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("p");
        Tag tag2 = Tag.valueOf("p");
        assertEquals(tag1, tag2);
    }

    @Test
    public void testHashCode() {
        Tag tag = new Tag("span");
        assertEquals("span".hashCode(), tag.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("a");
        assertEquals("a", tag.toString());
    }
}