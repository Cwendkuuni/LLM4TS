import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("test");
        assertEquals("test", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.getName());
    }

    @Test
    public void testCanContain() {
        Tag parent = new Tag("div");
        Tag child = new Tag("span");
        assertTrue(parent.canContain(child));
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("p");
        assertTrue(tag.isBlock());
    }

    // Add more test cases for other methods as required

}