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
        Tag tag = Tag.valueOf("div");
        assertNotNull(tag);
    }

    @Test
    public void testCanContain() {
        Tag parent = Tag.valueOf("div");
        assertTrue(parent.canContain(Tag.valueOf("p")));
        assertFalse(parent.canContain(Tag.valueOf("script")));
    }

    // Edge cases and exception handling scenarios

    @Test
    public void testCanContain_BlockChildNotAllow() {
        Tag parent = Tag.valueOf("p");
        assertFalse(parent.canContain(Tag.valueOf("div")));
    }

    @Test
    public void testCanContain_InlineChildNotAllow() {
        Tag parent = Tag.valueOf("span");
        assertFalse(parent.canContain(Tag.valueOf("p")));
    }

    @Test
    public void testCanContain_OptionalClosing() {
        Tag parent = Tag.valueOf("li");
        assertFalse(parent.canContain(Tag.valueOf("li")));
    }

    @Test
    public void testCanContain_EmptyTag() {
        Tag parent = Tag.valueOf("img");
        assertFalse(parent.canContain(Tag.valueOf("p")));
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
        Tag tag = Tag.valueOf("pre");
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
        Tag child = Tag.valueOf("span");
        Tag parent = Tag.valueOf("div");
        parent.isBlock = false;
        parent.canContainBlock = true;
        parent.ancestors.add(child);

        assertEquals(child, parent.getImplicitParent());
    }

    @Test
    public void testIsValidParent() {
        Tag parent = Tag.valueOf("p");
        Tag child = Tag.valueOf("span");

        parent.ancestors.add(child);

        assertTrue(parent.isValidParent(child));
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

    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.toString());
    }
}