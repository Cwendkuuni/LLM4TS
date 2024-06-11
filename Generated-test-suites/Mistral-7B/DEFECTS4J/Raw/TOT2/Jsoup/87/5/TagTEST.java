import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.Tag;

public class TagTest {
    @Test
    public void testTagName() {
        Tag htmlTag = Tag.valueOf("html");
        assertEquals("html", htmlTag.getName());
    }

    @Test
    public void testUnknownTag() {
        Tag unknownTag = Tag.valueOf("unknown");
        assertEquals("unknown", unknownTag.getName());
        // Check if unknown tag behaves like a generic tag
        assertEquals(false, unknownTag.isBlock());
    }

    @Test
    public void testKnownTag() {
        Tag pTag = Tag.valueOf("p");
        assertEquals("p", pTag.getName());
        assertEquals(true, pTag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag pTag = Tag.valueOf("p");
        Tag spanTag = Tag.valueOf("span");
        pTag.formatAsBlock(false);
        spanTag.formatAsBlock(false);
        assertEquals(false, pTag.formatAsBlock());
        assertEquals(false, spanTag.formatAsBlock());
    }

    @Test
    public void testIsInline() {
        Tag pTag = Tag.valueOf("p");
        Tag spanTag = Tag.valueOf("span");
        assertEquals(true, pTag.isInline());
        assertEquals(false, spanTag.isInline());
    }

    @Test
    public void testIsData() {
        Tag scriptTag = Tag.valueOf("script");
        assertEquals(true, scriptTag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag imgTag = Tag.valueOf("img");
        imgTag.empty = true;
        assertEquals(true, imgTag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag imgTag = Tag.valueOf("img");
        imgTag.selfClosing = true;
        assertEquals(true, imgTag.isSelfClosing());
    }

    @Test
    public void testIsKnownTagForString() {
        boolean result = Tag.isKnownTag("html");
        assertTrue(result);
    }

    @Test
    public void testIsKnownTagForUnknownTag() {
        boolean result = Tag.isKnownTag("unknown");
        assertFalse(result);
    }

    @Test
    public void testPreserveWhitespace() {
        Tag preTag = Tag.valueOf("pre");
        preTag.preserveWhitespace = true;
        assertEquals(true, preTag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Tag inputTag = Tag.valueOf("input");
        inputTag.formList = true;
        assertEquals(true, inputTag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag textareaTag = Tag.valueOf("textarea");
        textareaTag.formSubmit = true;
        assertEquals(true, textareaTag.isFormSubmittable());
    }

    @Test
    public void testEmptyTagName() {
        assertThrows(IllegalArgumentException.class, () -> Tag.valueOf(""));
    }

    @Test
    public void testNullTagName() {
        assertThrows(NullPointerException.class, () -> Tag.valueOf(null));
    }

    @Test
    public void testSpecialCharacterTagName() {
        Tag tag = Tag.valueOf("#mySpecialTag");
        assertEquals("#mySpecialTag", tag.getName());
    }

    @Test
    public void testLowerCaseTagName() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName().toLowerCase());
    }

    @Test
    public void testUpperCaseTagName() {
        Tag tag = Tag.valueOf("P");
        assertEquals("p", tag.getName());
    }
}