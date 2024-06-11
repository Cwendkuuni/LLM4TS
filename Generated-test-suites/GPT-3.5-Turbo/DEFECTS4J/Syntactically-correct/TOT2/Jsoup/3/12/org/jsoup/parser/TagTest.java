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
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.isBlock());
        assertFalse(tag.canContainBlock());
    }

    @Test
    public void testCanContain() {
        Tag parent = Tag.valueOf("div");
        Tag childBlock = Tag.valueOf("p");
        assertTrue(parent.canContain(childBlock));

        Tag childInline = Tag.valueOf("span");
        assertFalse(parent.canContain(childInline));
    }

    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.isBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("span");
        assertTrue(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("input");
        assertTrue(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("img");
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("pre");
        assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        Tag parent = Tag.valueOf("div");
        Tag child = Tag.valueOf("span");
        child.setAncestor("div");
        assertEquals(parent, child.getImplicitParent());
    }

    @Test
    public void testIsValidParent() {
        Tag parent = Tag.valueOf("div");
        Tag child = Tag.valueOf("span");
        child.setAncestor("div");
        assertTrue(parent.isValidParent(child));
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("test");
        Tag tag2 = new Tag("test");
        assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testHashCode() {
        Tag tag = new Tag("test");
        assertEquals("test".hashCode(), tag.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("test");
        assertEquals("test", tag.toString());
    }

}