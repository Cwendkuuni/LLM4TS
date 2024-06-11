
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
        assertTrue(tag.formatAsBlock());
        assertTrue(tag.canContainBlock());
        assertFalse(tag.isInline());
        assertFalse(tag.isData());
        assertFalse(tag.isEmpty());
        assertFalse(tag.isSelfClosing());
        assertTrue(tag.isKnownTag());

        Tag newTag = Tag.valueOf("newTag");
        assertFalse(newTag.isBlock());
        assertTrue(newTag.formatAsBlock());
        assertTrue(newTag.canContainBlock());
        assertTrue(newTag.isInline());
        assertTrue(newTag.isData());
        assertFalse(newTag.isEmpty());
        assertFalse(newTag.isSelfClosing());
        assertFalse(newTag.isKnownTag());
    }

    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.isBlock());

        Tag inlineTag = Tag.valueOf("span");
        assertFalse(inlineTag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.formatAsBlock());

        Tag inlineTag = Tag.valueOf("span");
        assertTrue(inlineTag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.canContainBlock());

        Tag inlineTag = Tag.valueOf("span");
        assertFalse(inlineTag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isInline());

        Tag inlineTag = Tag.valueOf("span");
        assertTrue(inlineTag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.isData());

        Tag dataTag = Tag.valueOf("script");
        assertTrue(dataTag.isData());

        Tag emptyTag = Tag.valueOf("img");
        assertFalse(emptyTag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isEmpty());

        Tag emptyTag = Tag.valueOf("img");
        assertTrue(emptyTag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isSelfClosing());

        Tag selfClosingTag = Tag.valueOf("img");
        assertTrue(selfClosingTag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.isKnownTag());

        Tag unknownTag = Tag.valueOf("newTag");
        assertFalse(unknownTag.isKnownTag());
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("p");
        assertFalse(tag.preserveWhitespace());

        Tag whitespaceTag = Tag.valueOf("pre");
        assertTrue(whitespaceTag.preserveWhitespace());
    }

    @Test
    public void testSetSelfClosing() {
        Tag tag = Tag.valueOf("div");
        assertFalse(tag.isSelfClosing());

        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("div");
        assertTrue(tag1.equals(tag2));

        Tag tag3 = Tag.valueOf("span");
        assertFalse(tag1.equals(tag3));
    }

    @Test
    public void testHashCode() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("div");
        assertEquals(tag1.hashCode(), tag2.hashCode());

        Tag tag3 = Tag.valueOf("span");
        assertNotEquals(tag1.hashCode(), tag3.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.toString());
    }

}
