import org.jsoup.parser.Tag;
import static org.junit.Assert.*;
import org.junit.Test;

public class TagTest {

    // Test for getName() method
    @Test
    public void testGetName() {
        Tag tag = new Tag("p");
        assertEquals("p", tag.getName());
    }

    // Test for valueOf() method when tag is defined
    @Test
    public void testValueOfDefinedTag() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
        assertTrue(tag.isBlock());
        assertTrue(tag.canContainBlock());
    }

    // Test for valueOf() method when tag is not defined
    @Test
    public void testValueOfNewTag() {
        Tag tag = Tag.valueOf("customTag");
        assertEquals("customTag", tag.getName());
        assertFalse(tag.isBlock());
        assertTrue(tag.canContainBlock());
        assertTrue(tag.canContainInline());
    }

    // Test for canContain() method when child is block and parent can contain block
    @Test
    public void testCanContainBlockChild() {
        Tag parent = new Tag("div");
        Tag child = new Tag("p");
        assertTrue(parent.canContain(child));
    }

    // Test for canContain() method when child is block and parent cannot contain block
    @Test
    public void testCannotContainBlockChild() {
        Tag parent = new Tag("span");
        Tag child = new Tag("p");
        assertFalse(parent.canContain(child));
    }

    // Test for canContain() method when child is inline and parent can contain inline
    @Test
    public void testCanContainInlineChild() {
        Tag parent = new Tag("p");
        Tag child = new Tag("span");
        assertTrue(parent.canContain(child));
    }

    // Test for canContain() method when child is inline and parent cannot contain inline
    @Test
    public void testCannotContainInlineChild() {
        Tag parent = new Tag("div");
        Tag child = new Tag("span");
        assertFalse(parent.canContain(child));
    }

    // Test for canContain() method when parent is optional closing and child is same tag
    @Test
    public void testCannotContainSameTagChild() {
        Tag parent = new Tag("div");
        parent.setOptionalClosing();
        Tag child = new Tag("div");
        assertFalse(parent.canContain(child));
    }

    // Test for isEmpty() method when tag is not empty
    @Test
    public void testNotEmpty() {
        Tag tag = new Tag("p");
        assertFalse(tag.isEmpty());
    }

    // Test for isEmpty() method when tag is empty
    @Test
    public void testEmpty() {
        Tag tag = new Tag("img");
        assertTrue(tag.isEmpty());
    }

    // Test for preserveWhitespace() method when tag does not preserve whitespace
    @Test
    public void testNotPreserveWhitespace() {
        Tag tag = new Tag("p");
        assertFalse(tag.preserveWhitespace());
    }

    // Test for preserveWhitespace() method when tag preserves whitespace
    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("pre");
        assertTrue(tag.preserveWhitespace());
    }

    // Test for getImplicitParent() method when parent exists
    @Test
    public void testGetImplicitParent() {
        Tag parent = new Tag("div");
        Tag child = new Tag("p");
        child.setAncestor(parent.getName());
        assertEquals(parent, child.getImplicitParent());
    }

    // Test for getImplicitParent() method when parent does not exist
    @Test
    public void testGetImplicitParentNoParent() {
        Tag child = new Tag("p");
        assertNull(child.getImplicitParent());
    }

    // Test for isValidParent() method when child has valid parent
    @Test
    public void testValidParent() {
        Tag parent = new Tag("div");
        Tag child = new Tag("p");
        child.setAncestor(parent.getName());
        assertTrue(child.isValidParent(parent));
    }

    // Test for isValidParent() method when child does not have valid parent
    @Test
    public void testInvalidParent() {
        Tag parent = new Tag("div");
        Tag child = new Tag("p");
        assertFalse(child.isValidParent(parent));
    }
    
}