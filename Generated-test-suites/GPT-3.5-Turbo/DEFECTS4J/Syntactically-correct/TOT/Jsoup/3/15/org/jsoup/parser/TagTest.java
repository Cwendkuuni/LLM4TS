import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest {

    private Tag tag;

    @Before
    public void setup() {
        tag = new Tag("test");
    }

    @Test
    public void getName_validName_returnsTagName() {
        assertEquals("test", tag.getName());
    }

    @Test
    public void valueOf_existingTagName_returnsDefinedTag() {
        Tag tag = Tag.valueOf("P");
        assertEquals("P", tag.getName());
    }

    @Test
    public void valueOf_unknownTagName_returnsNewGenericTag() {
        Tag tag = Tag.valueOf("unknown");
        assertEquals("unknown", tag.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void valueOf_nullTagName_throwsIllegalArgumentException() {
        Tag.valueOf(null);
    }

    @Test
    public void canContain_blockTagAndCanContainBlockSetToTrue_returnsTrue() {
        Tag parentTag = new Tag("parentTag");
        parentTag.canContainBlock = true;

        Tag childTag = new Tag("childTag");
        childTag.isBlock = true;

        assertTrue(parentTag.canContain(childTag));
    }

    @Test
    public void canContain_blockTagAndCanContainBlockSetToFalse_returnsFalse() {
        Tag parentTag = new Tag("parentTag");
        parentTag.canContainBlock = false;

        Tag childTag = new Tag("childTag");
        childTag.isBlock = true;

        assertFalse(parentTag.canContain(childTag));
    }

    @Test
    public void canContain_inlineTagAndCanContainInlineSetToTrue_returnsTrue() {
        Tag parentTag = new Tag("parentTag");
        parentTag.canContainInline = true;

        Tag childTag = new Tag("childTag");
        childTag.isBlock = false;

        assertTrue(parentTag.canContain(childTag));
    }

    @Test
    public void canContain_inlineTagAndCanContainInlineSetToFalse_returnsFalse() {
        Tag parentTag = new Tag("parentTag");
        parentTag.canContainInline = false;

        Tag childTag = new Tag("childTag");
        childTag.isBlock = false;

        assertFalse(parentTag.canContain(childTag));
    }

    @Test
    public void canContain_optionalClosingAndChildTagIsSameAsParent_returnsFalse() {
        Tag parentTag = new Tag("parentTag");
        parentTag.optionalClosing = true;

        assertTrue(parentTag.canContain(parentTag));
    }

    @Test
    public void canContain_emptyTag_returnsFalse() {
        Tag parentTag = new Tag("parentTag");
        parentTag.empty = true;

        Tag childTag = new Tag("childTag");

        assertFalse(parentTag.canContain(childTag));
    }

    @Test
    public void canContain_dataTag_returnsFalse() {
        Tag parentTag = new Tag("parentTag");
        parentTag.canContainInline = false;
        parentTag.empty = false;

        Tag childTag = new Tag("childTag");

        assertFalse(parentTag.canContain(childTag));
    }

    @Test
    public void equals_sameTag_returnsTrue() {
        Tag tag1 = new Tag("test");
        Tag tag2 = new Tag("test");

        assertTrue(tag1.equals(tag2));
    }

    @Test
    public void equals_differentTag_returnsFalse() {
        Tag tag1 = new Tag("test1");
        Tag tag2 = new Tag("test2");

        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void toString_returnsTagName() {
        assertEquals("test", tag.toString());
    }
}