
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
        assertNotNull(tag);
    }

    @Test
    public void testCanContain() {
        Tag parent = new Tag("p");
        Tag child = new Tag("a");
        assertTrue(parent.canContain(child));
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("p");
        assertTrue(tag.isBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("p");
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = new Tag("p");
        assertFalse(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = new Tag("p");
        assertFalse(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("img");
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("pre");
        assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        Tag tag = new Tag("p");
        assertNull(tag.getImplicitParent());
    }

    @Test
    public void testIsValidParent() {
        Tag parent = new Tag("p");
        Tag child = new Tag("a");
        assertTrue(parent.isValidParent(child));
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testHashCode() {
        Tag tag = new Tag("p");
        assertEquals(112, tag.hashCode());
    }

    @Test
    public void testAncestorTags() {
        Tag tag = Tag.valueOf("p");
        assertEquals("BODY", tag.getAncestorTags().get(0));
    }

}
