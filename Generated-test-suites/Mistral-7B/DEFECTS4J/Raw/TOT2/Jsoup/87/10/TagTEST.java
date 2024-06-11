
import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("p");
        String expected = "p";
        String actual = tag.getName();
        assertEquals(expected, actual);
    }

    @Test
    public void testValueOfKnownTag() {
        Tag tag = Tag.valueOf("p");
        String expectedTagName = "p";
        assertEquals(expectedTagName, tag.getName());
    }

    @Test
    public void testIsBlock() {
        Tag pTag = Tag.valueOf("p");
        assertTrue(pTag.isBlock());

        Tag imgTag = Tag.valueOf("img");
        assertFalse(imgTag.isBlock());
    }

    @Test
    public void testValueOfUnknownTag() {
        Tag tag = Tag.valueOf("unknownTag");
        assertNotEquals("unknownTag", tag.getName());
    }

    @Test
    public void testIsBlockAndFormatAsBlock() {
        Tag pTag = Tag.valueOf("p");
        assertTrue(pTag.isBlock());
        assertTrue(pTag.formatAsBlock());

        Tag divTag = new Tag("div").setFormatAsBlock(false);
        assertFalse(divTag.isBlock());
        assertFalse(divTag.formatAsBlock());
    }

    @Test
    public void testEmptyTag() {
        Tag metaTag = new Tag("meta").setEmpty(true);
        assertFalse(metaTag.canContainInline);
        assertTrue(metaTag.isEmpty());
    }

    @Test
    public void testSpecialCharacterTag() {
        Tag tag = new Tag("tag#with%special%characters");
        assertEquals("tag#with%special%characters", tag.getName());
    }

    @Test
    public void testLongTagName() {
        String tagName = "tagWithAVeryLongTagNameThatIsLongerThanTheMaximumAllowedLength";
        Tag tag = new Tag(tagName);
        assertEquals(tagName, tag.getName());
    }

    @Test(expected = NullPointerException.class)
    public void testNullTagName() {
        Tag tag = new Tag(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyOrWhitespaceTagName() {
        Tag tag = new Tag("");
    }
}