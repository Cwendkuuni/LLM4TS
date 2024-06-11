import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.canContainInline());
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testCanContain() {
        Tag parentTag = Tag.valueOf("p");
        Tag childTag = Tag.valueOf("strong");
        assertTrue(parentTag.canContain(childTag));
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
        Tag tag = Tag.valueOf("input");
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
        Tag parentTag = Tag.valueOf("div");
        Tag childTag = Tag.valueOf("p");
        childTag.setAncestor(parentTag.getName());
        assertEquals(parentTag, childTag.getImplicitParent());
    }

    @Test
    public void testIsValidParent() {
        Tag parentTag = Tag.valueOf("p");
        Tag childTag = Tag.valueOf("a");
        childTag.setAncestor(parentTag.getName());
        assertTrue(parentTag.isValidParent(childTag));
    }

    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("a");
        Tag tag2 = Tag.valueOf("a");
        assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testHashCode() {
        Tag tag1 = Tag.valueOf("strong");
        Tag tag2 = Tag.valueOf("strong");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

}