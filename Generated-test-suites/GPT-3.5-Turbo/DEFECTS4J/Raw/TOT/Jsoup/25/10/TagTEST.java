import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.isBlock());
        assertTrue(tag.canContainBlock());
        assertFalse(tag.isInline());
        assertFalse(tag.isData());
        assertFalse(tag.isEmpty());
        assertFalse(tag.isSelfClosing());
        assertFalse(tag.isKnownTag());

        tag = Tag.valueOf("random");
        assertFalse(tag.isBlock());
        assertTrue(tag.canContainBlock());
        assertTrue(tag.isInline());
        assertFalse(tag.isData());
        assertFalse(tag.isEmpty());
        assertFalse(tag.isSelfClosing());
        assertFalse(tag.isKnownTag());
    }

    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.isBlock());

        tag = Tag.valueOf("img");
        assertFalse(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.formatAsBlock());

        tag = Tag.valueOf("a");
        assertFalse(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.canContainBlock());

        tag = Tag.valueOf("br");
        assertFalse(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isInline());

        tag = Tag.valueOf("span");
        assertTrue(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isData());

        tag = Tag.valueOf("img");
        assertFalse(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isEmpty());

        tag = Tag.valueOf("img");
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isSelfClosing());

        tag = Tag.valueOf("img");
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.isKnownTag());

        tag = Tag.valueOf("random");
        assertFalse(tag.isKnownTag());
    }

    @Test
    public void testIsKnownTagWithName() {
        assertTrue(Tag.isKnownTag("div"));
        assertFalse(Tag.isKnownTag("random"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.preserveWhitespace());

        tag = Tag.valueOf("pre");
        assertTrue(tag.preserveWhitespace());
    }
}