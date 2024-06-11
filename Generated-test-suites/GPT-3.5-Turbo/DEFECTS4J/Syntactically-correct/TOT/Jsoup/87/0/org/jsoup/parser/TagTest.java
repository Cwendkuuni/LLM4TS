
import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("span");
        assertTrue(tag.isInline());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("br");
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = Tag.valueOf("img");
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("div"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("pre");
        assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Tag tag = Tag.valueOf("input");
        assertTrue(tag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag tag = Tag.valueOf("input");
        assertTrue(tag.isFormSubmittable());
    }

    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("div");
        assertTrue(tag1.equals(tag2));
    }
}
