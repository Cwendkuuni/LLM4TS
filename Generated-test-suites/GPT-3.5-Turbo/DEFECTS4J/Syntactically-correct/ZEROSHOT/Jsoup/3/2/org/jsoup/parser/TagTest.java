import org.jsoup.parser.Tag;
import org.junit.Test;
import static org.junit.Assert.*;

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
        assertNotNull(tag);
    }

    @Test
    public void testCanContain() {
        Tag tag = Tag.valueOf("div");
        Tag child = Tag.valueOf("p");
        boolean result = tag.canContain(child);
        assertTrue(result);
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

    @Test
    public void testGetImplicitParent() {
        Tag tag = Tag.valueOf("p");
        Tag implicitParent = tag.getImplicitParent();
        assertNull(implicitParent);
    }

    @Test
    public void testIsValidParent() {
        Tag tag = Tag.valueOf("div");
        Tag child = Tag.valueOf("p");
        boolean isValidParent = tag.isValidParent(child);
        assertTrue(isValidParent);
    }

    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        boolean result = tag1.equals(tag2);
        assertTrue(result);
    }

    @Test
    public void testHashCode() {
        Tag tag = Tag.valueOf("p");
        int hashCode = tag.hashCode();
        assertNotNull(hashCode);
    }

    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("p");
        String str = tag.toString();
        assertNotNull(str);
    }
}