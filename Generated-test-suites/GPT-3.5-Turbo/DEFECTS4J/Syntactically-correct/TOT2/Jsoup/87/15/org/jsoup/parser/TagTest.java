import org.jsoup.parser.Tag;
import org.jsoup.parser.ParseSettings;
import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    // Test for getName() method
    @Test
    public void testGetName() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.getName());
    }

    // Test for valueOf() method with tag name sensitivity
    @Test
    public void testValueOfWithNameSensitive() {
        Tag tag = Tag.valueOf("DIV", ParseSettings.preserveCase);
        assertTrue(tag.isBlock());
    }

    // Test for valueOf() method without tag name sensitivity
    @Test
    public void testValueOfWithoutNameSensitive() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isBlock());
    }

    // Test for isBlock() method
    @Test
    public void testIsBlock() {
        Tag tag = new Tag("div");
        assertTrue(tag.isBlock());
    }

    // Test for formatAsBlock() method
    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("p");
        assertTrue(tag.formatAsBlock());
    }

    // Add more test cases for other methods as needed

}