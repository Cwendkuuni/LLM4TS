import org.jsoup.parser.Tag;
import org.jsoup.parser.ParseSettings;
import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("test");
        assertEquals("test", tag.getName());
    }

    @Test
    public void testValueOfWithSettings() {
        Tag tag = Tag.valueOf("div", ParseSettings.preserveCase);
        assertTrue(tag.isBlock());
    }

    @Test
    public void testValueOfWithoutSettings() {
        Tag tag = Tag.valueOf("span");
        assertFalse(tag.isBlock());
    }

    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.canContainBlock());
    }

    // additional test cases for other methods should be included

}