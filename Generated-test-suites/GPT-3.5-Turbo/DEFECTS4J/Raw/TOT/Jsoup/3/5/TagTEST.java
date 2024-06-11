import org.junit.*;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
    }

    @Test
    public void testGetValueOf() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.canContainInline());
        assertFalse(tag.canContainBlock());
    }

    @Test
    public void testCanContain() {
        Tag parent = Tag.valueOf("div");
        Tag child1 = Tag.valueOf("p");
        assertTrue(parent.canContain(child1));

        Tag child2 = Tag.valueOf("img");
        assertFalse(parent.canContain(child2));
    }

    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.isBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag1 = Tag.valueOf("p");
        assertFalse(tag1.isData());

        Tag tag2 = Tag.valueOf("script");
        assertTrue(tag2.isData());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag1 = Tag.valueOf("p");
        assertFalse(tag1.preserveWhitespace());

        Tag tag2 = Tag.valueOf("pre");
        assertTrue(tag2.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        Tag tag1 = Tag.valueOf("p");
        assertNull(tag1.getImplicitParent());

        Tag tag2 = Tag.valueOf("div");
        assertEquals("body", tag2.getImplicitParent().getName());
    }

    @Test
    public void testIsValidParent() {
        Tag parent = Tag.valueOf("div");
        Tag child1 = Tag.valueOf("p");
        assertTrue(parent.isValidParent(child1));

        Tag child2 = Tag.valueOf("script");
        assertFalse(parent.isValidParent(child2));
    }

    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");

        assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testHashCode() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");

        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

}