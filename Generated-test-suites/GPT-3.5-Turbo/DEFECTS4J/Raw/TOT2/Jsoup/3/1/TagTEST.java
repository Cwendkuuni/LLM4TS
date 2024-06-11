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
        Tag tag1 = Tag.valueOf("div");
        assertEquals("div", tag1.getName());

        Tag tag2 = Tag.valueOf("unknown");
        assertEquals("unknown", tag2.getName());
    }

    @Test
    public void testCanContain() {
        Tag parent = new Tag("parent");
        parent.setContainInlineOnly();

        Tag child1 = new Tag("child1");
        child1.setContainBlock();
        assertTrue(parent.canContain(child1));

        Tag child2 = new Tag("child2");
        child2.setContainInline();
        assertTrue(parent.canContain(child2));
    }

    @Test
    public void testBlockInlineChecks() {
        Tag blockTag = new Tag("block");
        blockTag.setContainBlock();

        Tag inlineTag = new Tag("inline");
        inlineTag.setContainInline();

        assertFalse(blockTag.isInline());
        assertTrue(inlineTag.isInline());

        assertFalse(blockTag.canContainInline());
        assertTrue(inlineTag.canContainInline());
    }

    @Test
    public void testEmpty() {
        Tag emptyTag = new Tag("empty");
        emptyTag.setEmpty();
        assertTrue(emptyTag.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("tag");
        tag.setPreserveWhitespace();
        assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("tag");
        Tag tag2 = new Tag("tag");

        assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testHashCode() {
        Tag tag = new Tag("tag");
        assertNotNull(tag.hashCode());
    }

}