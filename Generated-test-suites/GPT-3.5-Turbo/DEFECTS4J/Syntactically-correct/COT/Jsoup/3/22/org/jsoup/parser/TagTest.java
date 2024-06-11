
import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("p");
        assertEquals("p", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
    }

    @Test
    public void testCanContain() {
        Tag tag1 = new Tag("head");
        Tag tag2 = new Tag("base");
        assertTrue(tag1.canContain(tag2));
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("h1");
        assertTrue(tag.isBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("ul");
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = new Tag("i");
        assertTrue(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = new Tag("br");
        assertTrue(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("img");
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("pre");
        assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        Tag tag = new Tag("span");
        assertNull(tag.getImplicitParent());
    }

    @Test
    public void testIsValidParent() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("div");
        assertTrue(tag1.isValidParent(tag2));
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        assertEquals(tag1, tag2);
    }
}