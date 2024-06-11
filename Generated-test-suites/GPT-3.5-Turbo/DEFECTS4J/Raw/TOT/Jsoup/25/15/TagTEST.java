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
    public void testIsBlock() {
        Tag tag = Tag.valueOf("div");
        boolean isBlock = tag.isBlock();
        assertTrue(isBlock);
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = Tag.valueOf("a");
        boolean formatAsBlock = tag.formatAsBlock();
        assertFalse(formatAsBlock);
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("div");
        boolean canContainBlock = tag.canContainBlock();
        assertTrue(canContainBlock);
    }

    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("span");
        boolean isInline = tag.isInline();
        assertTrue(isInline);
    }

    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("script");
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
    public void testIsSelfClosing() {
        Tag tag = Tag.valueOf("input");
        boolean isSelfClosing = tag.isSelfClosing();
        assertTrue(isSelfClosing);
    }

    @Test
    public void testIsKnownTag() {
        boolean isKnownTag = Tag.isKnownTag("p");
        assertTrue(isKnownTag);
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("pre");
        boolean preserveWhitespace = tag.preserveWhitespace();
        assertTrue(preserveWhitespace);
    }

    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("div");
        assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testHashCode() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        assertTrue(tag1.hashCode() == tag2.hashCode());
    }

}