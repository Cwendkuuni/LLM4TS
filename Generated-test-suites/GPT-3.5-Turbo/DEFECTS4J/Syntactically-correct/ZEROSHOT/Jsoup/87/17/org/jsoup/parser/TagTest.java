import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("testTag");
        assertEquals("testTag", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.isBlock());
        assertTrue(tag.formatAsBlock());
        assertTrue(tag.canContainBlock());

        Tag unknownTag = Tag.valueOf("unknown");
        assertFalse(unknownTag.isBlock());
        assertFalse(unknownTag.formatAsBlock());

        Tag caseSensitiveTag = Tag.valueOf("p", ParseSettings.preserveCase);
        assertTrue(caseSensitiveTag.isBlock());
        assertTrue(caseSensitiveTag.formatAsBlock());
        assertTrue(caseSensitiveTag.canContainBlock());
    }

    @Test
    public void testIsBlock() {
        Tag blockTag = new Tag("blockTag");
        assertTrue(blockTag.isBlock());
        Tag inlineTag = new Tag("inlineTag");
        assertFalse(inlineTag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag blockTag = new Tag("blockTag");
        assertTrue(blockTag.formatAsBlock());
        Tag inlineTag = new Tag("inlineTag");
        assertTrue(inlineTag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag blockTag = new Tag("blockTag");
        assertTrue(blockTag.canContainBlock());
        Tag inlineTag = new Tag("inlineTag");
        assertFalse(inlineTag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag blockTag = new Tag("blockTag");
        assertFalse(blockTag.isInline());
        Tag inlineTag = new Tag("inlineTag");
        assertTrue(inlineTag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = new Tag("testTag");
        tag.canContainInline = false;
        tag.empty = false;
        assertTrue(tag.isData());
        tag.canContainInline = true;
        assertFalse(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag emptyTag = new Tag("emptyTag");
        emptyTag.empty = true;
        assertTrue(emptyTag.isEmpty());
        Tag nonEmptyTag = new Tag("nonEmptyTag");
        assertFalse(nonEmptyTag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag selfClosingTag = new Tag("selfClosingTag");
        selfClosingTag.empty = true;
        assertTrue(selfClosingTag.isSelfClosing());
        Tag nonSelfClosingTag = new Tag("nonSelfClosingTag");
        assertFalse(nonSelfClosingTag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Tag knownTag = Tag.valueOf("p");
        assertTrue(knownTag.isKnownTag());
        Tag unknownTag = Tag.valueOf("unknown");
        assertFalse(unknownTag.isKnownTag());
    }

    @Test
    public void testIsKnownTagWithTagName() {
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag preserveWhitespaceTag = new Tag("preserveWhitespaceTag");
        preserveWhitespaceTag.preserveWhitespace = true;
        assertTrue(preserveWhitespaceTag.preserveWhitespace());
        Tag nonPreserveWhitespaceTag = new Tag("nonPreserveWhitespaceTag");
        assertFalse(nonPreserveWhitespaceTag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Tag formListedTag = new Tag("formListedTag");
        formListedTag.formList = true;
        assertTrue(formListedTag.isFormListed());
        Tag nonFormListedTag = new Tag("nonFormListedTag");
        assertFalse(nonFormListedTag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag formSubmittableTag = new Tag("formSubmittableTag");
        formSubmittableTag.formSubmit = true;
        assertTrue(formSubmittableTag.isFormSubmittable());
        Tag nonFormSubmittableTag = new Tag("nonFormSubmittableTag");
        assertFalse(nonFormSubmittableTag.isFormSubmittable());
    }

    @Test
    public void testSetSelfClosing() {
        Tag tag = new Tag("testTag");
        assertFalse(tag.isSelfClosing());
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("testTag");
        Tag tag2 = new Tag("testTag");
        assertTrue(tag1.equals(tag2));
        Tag tag3 = new Tag("differentTag");
        assertFalse(tag1.equals(tag3));
    }

    @Test
    public void testHashCode() {
        Tag tag1 = new Tag("testTag");
        Tag tag2 = new Tag("testTag");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("testTag");
        assertEquals("testTag", tag.toString());
    }
}