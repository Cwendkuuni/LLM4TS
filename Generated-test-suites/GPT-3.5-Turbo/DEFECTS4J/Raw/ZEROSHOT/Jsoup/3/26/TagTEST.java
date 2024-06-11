import org.jsoup.parser.Tag;
import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("testTag");
        assertEquals("testTag", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("testTag");
        assertEquals("testTag", tag.getName());
    }

    @Test
    public void testCanContain() {
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag2");
        tag1.setContainInlineOnly();
        assertTrue(tag1.canContain(tag2));
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("testTag");
        assertTrue(tag.isBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("testTag");
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = new Tag("testTag");
        assertFalse(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = new Tag("testTag");
        assertFalse(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("testTag");
        assertFalse(tag.isEmpty());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("testTag");
        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testGetImplicitParent() {
        Tag tag = new Tag("testTag");
        assertNull(tag.getImplicitParent());
    }

    @Test
    public void testIsValidParent() {
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag2");
        assertTrue(tag1.isValidParent(tag2));
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("tag");
        Tag tag2 = new Tag("tag");
        assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testHashCode() {
        Tag tag1 = new Tag("tag");
        Tag tag2 = new Tag("tag");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("testTag");
        assertEquals("testTag", tag.toString());
    }
}