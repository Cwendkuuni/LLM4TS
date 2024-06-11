
import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());

        tag = Tag.valueOf("");
        assertEquals("body", tag.getName());
    }

    @Test
    public void testCanContain() {
        Tag parent = Tag.valueOf("p");
        Tag child1 = Tag.valueOf("div");
        Tag child2 = Tag.valueOf("span");

        assertTrue(parent.canContain(child1));
        assertFalse(parent.canContain(child2));
    }

    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.isBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag1 = Tag.valueOf("p");
        assertFalse(tag1.isData());

        Tag tag2 = Tag.valueOf("textarea");
        assertTrue(tag2.isData());

        Tag tag3 = Tag.valueOf("img");
        assertFalse(tag3.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag1 = Tag.valueOf("p");
        assertFalse(tag1.isEmpty());

        Tag tag2 = Tag.valueOf("img");
        assertTrue(tag2.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("pre");
        assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        Tag tag = Tag.valueOf("div");
        assertNull(tag.getImplicitParent());
    }

    @Test
    public void testIsValidParent() {
        Tag parent = Tag.valueOf("div");
        Tag child1 = Tag.valueOf("p");
        Tag child2 = Tag.valueOf("span");

        assertTrue(parent.isValidParent(child1));
        assertFalse(parent.isValidParent(child2));
    }

}
