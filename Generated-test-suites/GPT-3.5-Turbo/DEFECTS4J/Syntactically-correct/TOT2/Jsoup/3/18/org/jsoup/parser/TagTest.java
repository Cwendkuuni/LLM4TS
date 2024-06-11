import org.jsoup.parser.Tag;
import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("test");
        assertEquals("test", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("p");
        assertNotNull(tag);
    }

    @Test
    public void testCanContain() {
        Tag parent = new Tag("div");
        Tag child = new Tag("p");
        assertTrue(parent.canContain(child));
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("div");
        assertTrue(tag.isBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("div");
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = new Tag("span");
        assertTrue(tag.isInline());
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
        Tag parent = new Tag("div");
        Tag child = new Tag("p");
        child.setAncestor("div");
        assertEquals(parent, child.getImplicitParent());
    }

    @Test
    public void testIsValidParent() {
        Tag parent = new Tag("div");
        Tag child = new Tag("p");
        child.setAncestor("div");
        assertTrue(parent.isValidParent(child));
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("a");
        Tag tag2 = new Tag("a");
        assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testHashCode() {
        Tag tag = new Tag("div");
        assertEquals(961, tag.hashCode());
    }

}