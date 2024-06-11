import static org.junit.Assert.*;
import org.junit.Test;


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
        assertEquals("p", tag.getName());
    }

    @Test
    public void testCanContain() {
        Tag parent = new Tag("parent");
        parent.setContainInlineOnly();
        Tag child = new Tag("child");
        assertTrue(parent.canContain(child));
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("test");
        assertTrue(tag.isBlock());
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
    public void testPreserveWhitespace() {
        Tag tag = new Tag("test");
        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        Tag parent = new Tag("parent");
        Tag child = new Tag("child");
        parent.setAncestor("ancestor");
        child.setAncestor("parent");
        assertEquals(parent, child.getImplicitParent());
    }

    @Test
    public void testIsValidParent() {
        Tag parent = new Tag("parent");
        Tag child = new Tag("child");
        parent.setAncestor("ancestor");
        child.setAncestor("parent");
        assertTrue(parent.isValidParent(child));
    }
}