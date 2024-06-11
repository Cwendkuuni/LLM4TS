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
        Tag tag1 = Tag.valueOf("span");
        Tag tag2 = Tag.valueOf("");
        Tag tag3 = Tag.valueOf("unknown");

        assertTrue(tag1.isInline());
        assertTrue(tag2.canContainBlock());
        assertTrue(tag3.isInline());
    }

    @Test
    public void testCanContain() {
        Tag parentTag = Tag.valueOf("p");
        Tag childTag1 = Tag.valueOf("div");
        Tag childTag2 = Tag.valueOf("a");

        assertTrue(parentTag.canContain(childTag1));
        assertFalse(parentTag.canContain(childTag2));
    }

    @Test
    public void testIsBlock() {
        Tag blockTag = Tag.valueOf("div");
        assertTrue(blockTag.isBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("span");
        assertFalse(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag inlineTag = Tag.valueOf("span");
        assertTrue(inlineTag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("p");
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
        Tag tag = Tag.valueOf("div");
        assertNull(tag.getImplicitParent());
    }

    @Test
    public void testIsValidParent() {
        Tag parentTag = Tag.valueOf("body");
        Tag childTag = Tag.valueOf("div");

        assertTrue(parentTag.isValidParent(childTag));
    }
}