import static org.junit.Assert.*;
import org.junit.Test;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("testName");
        assertEquals("testName", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag defaultTag = Tag.valueOf("unknownTag");
        assertEquals("unknownTag", defaultTag.getName());
        assertTrue(defaultTag.canContainBlock());
        assertFalse(defaultTag.isEmpty());
    }

    @Test
    public void testCanContain() {
        Tag parentTag = Tag.valueOf("div");
        Tag childBlockTag = Tag.valueOf("p");
        assertTrue(parentTag.canContain(childBlockTag));

        Tag childInlineTag = Tag.valueOf("b");
        assertFalse(parentTag.canContain(childInlineTag));
    }

    @Test
    public void testIsBlock() {
        Tag blockTag = new Tag("table");
        assertTrue(blockTag.isBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("div");
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag inlineTag = new Tag("em");
        assertTrue(inlineTag.isInline());
    }

    @Test
    public void testIsData() {
        Tag dataTag = new Tag("script");
        assertTrue(dataTag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag emptyTag = new Tag("img");
        assertTrue(emptyTag.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("pre");
        assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        Tag parent = Tag.valueOf("div");
        Tag child = new Tag("p");
        child.setAncestor("div");
        assertEquals(parent, child.getImplicitParent());
    }

    @Test
    public void testIsValidParent() {
        Tag parent = new Tag("ul");
        Tag child = new Tag("li");
        child.setAncestor("ul");
        assertTrue(parent.isValidParent(child));
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("div");
        assertEquals(tag1, tag2);
    }

    @Test
    public void testHashCode() {
        Tag tag = new Tag("span");
        assertEquals(tag.getName().hashCode(), tag.hashCode());
    }

}