import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.getName());
    }

    @Test
    public void testValueOfWithSettings() {
        Tag tag = Tag.valueOf("p", new ParseSettings(true));
        assertTrue(tag.isBlock());
        assertFalse(tag.formatAsBlock());
    }

    @Test
    public void testValueOfWithoutSettings() {
        Tag tag = Tag.valueOf("img");
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testIsBlock() {
        Tag tag1 = Tag.valueOf("h1");
        assertTrue(tag1.isBlock());

        Tag tag2 = Tag.valueOf("span");
        assertFalse(tag2.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag1 = Tag.valueOf("p");
        assertTrue(tag1.formatAsBlock());

        Tag tag2 = Tag.valueOf("pre");
        assertFalse(tag2.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag1 = Tag.valueOf("div");
        assertTrue(tag1.canContainBlock());

        Tag tag2 = Tag.valueOf("span");
        assertFalse(tag2.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag1 = Tag.valueOf("span");
        assertTrue(tag1.isInline());

        Tag tag2 = Tag.valueOf("div");
        assertFalse(tag2.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag1 = Tag.valueOf("img");
        assertFalse(tag1.isData());

        Tag tag2 = Tag.valueOf("p");
        assertFalse(tag2.isData());

        Tag tag3 = Tag.valueOf("br");
        assertFalse(tag3.isData());

        Tag tag4 = Tag.valueOf("span");
        assertTrue(tag4.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag1 = Tag.valueOf("img");
        assertTrue(tag1.isEmpty());

        Tag tag2 = Tag.valueOf("p");
        assertFalse(tag2.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag1 = Tag.valueOf("img");
        assertTrue(tag1.isSelfClosing());

        Tag tag2 = Tag.valueOf("div");
        assertFalse(tag2.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("p"));

        assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag1 = Tag.valueOf("pre");
        assertTrue(tag1.preserveWhitespace());

        Tag tag2 = Tag.valueOf("input");
        assertFalse(tag2.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Tag tag1 = Tag.valueOf("input");
        assertTrue(tag1.isFormListed());

        Tag tag2 = Tag.valueOf("div");
        assertFalse(tag2.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag tag1 = Tag.valueOf("input");
        assertTrue(tag1.isFormSubmittable());

        Tag tag2 = Tag.valueOf("div");
        assertFalse(tag2.isFormSubmittable());
    }
}