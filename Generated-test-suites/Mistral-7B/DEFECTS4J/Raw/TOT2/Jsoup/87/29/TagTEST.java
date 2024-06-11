import org.jsoup.parser.Tag;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TagTest {
    @Test
    public void testTagName() {
        Tag tag = new Tag("p");
        assertEquals("p", tag.getName());
    }

    @Test
    public void testUnknownTag() {
        Tag tag = Tag.valueOf("unknown");
        assertTrue(tag.isBlock()); // default value for unknown tags
        assertEquals("unknown", tag.getName());
    }

    @Test
    public void testPredefinedTag() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.isBlock());
        assertEquals("p", tag.getName());
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("p");
        assertTrue(tag.isBlock());

        Tag inlineTag = new Tag("span");
        assertFalse(inlineTag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag blockTag = new Tag("p");
        assertTrue(blockTag.formatAsBlock());

        Tag inlineTag = new Tag("span");
        assertFalse(inlineTag.formatAsBlock());
    }

    @Test
    public void testIsData() {
        Tag dataTag = new Tag("script");
        assertTrue(dataTag.isData());

        Tag blockTag = new Tag("p");
        assertFalse(blockTag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag emptyTag = new Tag("img");
        assertTrue(emptyTag.isEmpty());

        Tag nonEmptyTag = new Tag("p");
        assertFalse(nonEmptyTag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag selfClosingTag = new Tag("img");
        assertTrue(selfClosingTag.isSelfClosing());

        Tag nonSelfClosingTag = new Tag("p");
        assertFalse(nonSelfClosingTag.isSelfClosing());
    }

    @Test
    public void testIsFormListed() {
        Tag formListedTag = new Tag("input");
        assertTrue(formListedTag.isFormListed());

        Tag nonFormListedTag = new Tag("p");
        assertFalse(nonFormListedTag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag formSubmittableTag = new Tag("input");
        assertTrue(formSubmittableTag.isFormSubmittable());

        Tag nonFormSubmittableTag = new Tag("p");
        assertFalse(nonFormSubmittableTag.isFormSubmittable());
    }
}