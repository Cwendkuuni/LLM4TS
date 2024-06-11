import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest {

    private Tag tag;

    @Before
    public void setup() {
        tag = new Tag("test");
    }

    @Test
    public void testGetName() {
        assertEquals("test", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag newTag = Tag.valueOf("test");
        assertEquals(tag, newTag);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfNull() {
        Tag.valueOf(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testValueOfEmpty() {
        Tag.valueOf("");
    }

    @Test
    public void testCanContain_BlockTag_CanContainBlock() {
        Tag child = new Tag("child");
        child.isBlock = true;
        tag.canContainBlock = true;

        assertTrue(tag.canContain(child));
    }

    @Test
    public void testCanContain_BlockTag_CannotContainBlock() {
        Tag child = new Tag("child");
        child.isBlock = true;
        tag.canContainBlock = false;

        assertFalse(tag.canContain(child));
    }

    @Test
    public void testCanContain_InlineTag_CanContainInline() {
        Tag child = new Tag("child");
        child.isBlock = false;
        tag.canContainInline = true;

        assertTrue(tag.canContain(child));
    }

    @Test
    public void testCanContain_InlineTag_CannotContainInline() {
        Tag child = new Tag("child");
        child.isBlock = false;
        tag.canContainInline = false;

        assertFalse(tag.canContain(child));
    }

    @Test
    public void testCanContain_OptionalClosingSelf() {
        Tag child = new Tag("test");
        tag.optionalClosing = true;

        assertFalse(tag.canContain(child));
    }

    @Test
    public void testCanContain_EmptyTag_CannotContainEmpty() {
        Tag child = new Tag("child");
        tag.empty = true;

        assertFalse(tag.canContain(child));
    }

    @Test
    public void testIsBlock_True() {
        tag.isBlock = true;

        assertTrue(tag.isBlock());
    }

    @Test
    public void testIsBlock_False() {
        tag.isBlock = false;

        assertFalse(tag.isBlock());
    }

    @Test
    public void testCanContainBlock_True() {
        tag.canContainBlock = true;

        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testCanContainBlock_False() {
        tag.canContainBlock = false;

        assertFalse(tag.canContainBlock());
    }

    @Test
    public void testIsInline_True() {
        tag.isBlock = false;

        assertTrue(tag.isInline());
    }

    @Test
    public void testIsInline_False() {
        tag.isBlock = true;

        assertFalse(tag.isInline());
    }

    @Test
    public void testIsData_True() {
        tag.canContainInline = false;
        tag.empty = false;

        assertTrue(tag.isData());
    }

    @Test
    public void testIsData_False() {
        tag.canContainInline = true;
        tag.empty = false;

        assertFalse(tag.isData());
    }

    @Test
    public void testIsEmpty_True() {
        tag.empty = true;

        assertTrue(tag.isEmpty());
    }

    @Test
    public void testIsEmpty_False() {
        tag.empty = false;

        assertFalse(tag.isEmpty());
    }

    @Test
    public void testPreserveWhitespace_True() {
        tag.preserveWhitespace = true;

        assertTrue(tag.preserveWhitespace());
    }
    
    // TODO: Add more test cases for the remaining methods
    
}