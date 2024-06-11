import org.jsoup.parser.Tag;
import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testName() {
        Tag tag = new Tag("testTag");
        assertEquals("testTag", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("div");
        assertNotNull(tag);
        assertEquals("div", tag.getName());
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("blockTag");
        assertTrue(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("blockTag");
        assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("blockTag");
        assertTrue(tag.canContainBlock());
    }

    // Add more test cases for other methods

}