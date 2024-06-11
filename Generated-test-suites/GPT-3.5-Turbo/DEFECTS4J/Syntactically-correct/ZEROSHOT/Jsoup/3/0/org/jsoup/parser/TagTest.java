import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("p");
        assertNotNull(tag);
        assertEquals("p", tag.getName());
        assertTrue(tag.isBlock());
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testCanContain() {
        Tag parent = Tag.valueOf("div");
        Tag child = Tag.valueOf("p");
        assertTrue(parent.canContain(child));

        child = Tag.valueOf("img");
        assertFalse(parent.canContain(child));
    }

    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.isBlock());

        tag = Tag.valueOf("span");
        assertFalse(tag.isBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.canContainBlock());

        tag = Tag.valueOf("span");
        assertFalse(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isInline());

        tag = Tag.valueOf("span");
        assertTrue(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isData());

        tag = Tag.valueOf("img");
        assertFalse(tag.isData());

        tag = Tag.valueOf("script");
        assertTrue(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isEmpty());

        tag = Tag.valueOf("img");
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.preserveWhitespace());

        tag = Tag.valueOf("pre");
        assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        Tag child = Tag.valueOf("p");
        Tag parent = Tag.valueOf("div");
        parent.setAncestor("body");
        child.setAncestor(parent.getName());

        assertEquals(parent, child.getImplicitParent());

        parent = Tag.valueOf("body");
        parent.setAncestor(Tag.valueOf("html").getName());
        child.setAncestor(parent.getName());

        assertNull(child.getImplicitParent());
    }

    @Test
    public void testIsValidParent() {
        Tag parent = Tag.valueOf("div");
        Tag child = Tag.valueOf("p");
        child.setAncestor(parent.getName());

        assertTrue(parent.isValidParent(child));
    }

    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("div");

        assertEquals(tag1, tag2);
    }

    @Test
    public void testHashCode() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("div");

        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

}