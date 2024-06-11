
import org.junit.Test;

public class TagTest {
    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("p");
        String name = tag.getName();
        assertEquals("p", name);
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
    }

    @Test
    public void testCanContain() {
        Tag parentTag = Tag.valueOf("div");
        Tag childTag = Tag.valueOf("p");
        boolean canContain = parentTag.canContain(childTag);
        assertFalse(canContain);
    }

    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("p");
        boolean isBlock = tag.isBlock();
        assertTrue(isBlock);
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("div");
        boolean canContainBlock = tag.canContainBlock();
        assertTrue(canContainBlock);
    }

    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("p");
        boolean isInline = tag.isInline();
        assertFalse(isInline);
    }

    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("p");
        boolean isData = tag.isData();
        assertFalse(isData);
    }

    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("img");
        boolean isEmpty = tag.isEmpty();
        assertTrue(isEmpty);
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("pre");
        boolean preserveWhitespace = tag.preserveWhitespace();
        assertTrue(preserveWhitespace);
    }
}
