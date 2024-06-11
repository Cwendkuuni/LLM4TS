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
        assertTrue(tag.isBlock());
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testCanContain() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("p");
        assertTrue(tag1.canContain(tag2));
    }

    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("span");
        assertFalse(tag.isBlock());
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
        Tag tag = Tag.valueOf("span");
        assertFalse(tag.isData());
        tag.setContainDataOnly();
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
    public void testIsValidParent() {
        Tag parent = Tag.valueOf("body");
        Tag child = Tag.valueOf("div");
        assertTrue(parent.isValidParent(child));
    }

    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("span");
        Tag tag2 = Tag.valueOf("span");
        assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("a");
        assertEquals("a", tag.toString());
    }
}