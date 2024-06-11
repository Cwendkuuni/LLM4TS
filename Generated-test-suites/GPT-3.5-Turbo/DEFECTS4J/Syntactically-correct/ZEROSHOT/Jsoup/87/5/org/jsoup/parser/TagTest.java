import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void getNameTest() {
        Tag tag = new Tag("test");
        assertEquals("test", tag.getName());
    }

    @Test
    public void valueOfTest() {
        ParseSettings settings = ParseSettings.preserveCase;
        Tag tag = Tag.valueOf("p", settings);
        assertTrue(tag.isBlock());

        Tag unknownTag = Tag.valueOf("unknown");
        assertFalse(unknownTag.isKnownTag());
    }

    @Test
    public void isBlockTest() {
        Tag tag = new Tag("test");
        assertTrue(tag.isBlock());
    }

    @Test
    public void formatAsBlockTest() {
        Tag tag = new Tag("test");
        assertTrue(tag.formatAsBlock());
    }

    @Test
    public void canContainBlockTest() {
        Tag tag = new Tag("test");
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void isInlineTest() {
        Tag tag = new Tag("test");
        assertFalse(tag.isInline());
    }

    @Test
    public void isDataTest() {
        Tag tag = new Tag("test");
        assertFalse(tag.isData());
    }

    @Test
    public void isEmptyTest() {
        Tag tag = new Tag("test");
        assertFalse(tag.isEmpty());
    }

    @Test
    public void isSelfClosingTest() {
        Tag tag = new Tag("test");
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void isKnownTagTest() {
        Tag tag = new Tag("test");
        assertFalse(tag.isKnownTag());
    }

    @Test
    public void isKnownTagWithStringTest() {
        assertFalse(Tag.isKnownTag("test"));
    }

    @Test
    public void preserveWhitespaceTest() {
        Tag tag = new Tag("test");
        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void isFormListedTest() {
        Tag tag = new Tag("test");
        assertFalse(tag.isFormListed());
    }

    @Test
    public void isFormSubmittableTest() {
        Tag tag = new Tag("test");
        assertFalse(tag.isFormSubmittable());
    }

    @Test
    public void setSelfClosingTest() {
        Tag tag = new Tag("test");
        assertFalse(tag.isSelfClosing());
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void equalsTest() {
        Tag tag1 = new Tag("test");
        Tag tag2 = new Tag("test");
        assertEquals(tag1, tag2);
    }

    @Test
    public void hashCodeTest() {
        Tag tag1 = new Tag("test");
        Tag tag2 = new Tag("test");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void toStringTest() {
        Tag tag = new Tag("test");
        assertEquals("test", tag.toString());
    }
}