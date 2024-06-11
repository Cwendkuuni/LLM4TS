import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("p");
        assertEquals("p", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("div");
        Tag tag3 = Tag.valueOf("unknown");
        assertFalse(tag1.isBlock());
        assertTrue(tag2.isBlock());
        assertFalse(tag3.isKnownTag());
    }

    @Test
    public void testIsBlock() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("span");
        assertTrue(tag1.isBlock());
        assertFalse(tag2.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("a");
        assertTrue(tag1.formatAsBlock());
        assertFalse(tag2.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("span");
        assertTrue(tag1.canContainBlock());
        assertFalse(tag2.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag1 = new Tag("span");
        Tag tag2 = new Tag("div");
        assertTrue(tag1.isInline());
        assertFalse(tag2.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("img");
        assertFalse(tag1.isData());
        assertFalse(tag2.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("img");
        assertFalse(tag1.isEmpty());
        assertFalse(tag2.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("img");
        Tag tag3 = new Tag("br");
        assertFalse(tag1.isSelfClosing());
        assertFalse(tag2.isSelfClosing());
        assertTrue(tag3.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("div");
        assertFalse(tag1.isKnownTag());
        assertTrue(tag2.isKnownTag());
    }

    @Test
    public void testIsKnownTagWithStringParam() {
        assertFalse(Tag.isKnownTag("p"));
        assertTrue(Tag.isKnownTag("div"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag1 = new Tag("pre");
        Tag tag2 = new Tag("div");
        assertTrue(tag1.preserveWhitespace());
        assertFalse(tag2.preserveWhitespace());
    }
}