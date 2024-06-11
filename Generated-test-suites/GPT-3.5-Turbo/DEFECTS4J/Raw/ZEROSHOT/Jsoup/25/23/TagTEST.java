import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("div");
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

        Tag unknownTag = Tag.valueOf("unknown");
        assertFalse(unknownTag.isKnownTag());
        assertEquals("unknown", unknownTag.getName());
        assertFalse(unknownTag.isBlock());
        assertTrue(unknownTag.formatAsBlock());
        assertTrue(unknownTag.canContainBlock());
        assertTrue(unknownTag.isInline());
        assertTrue(unknownTag.isData());
        assertFalse(unknownTag.isEmpty());
        assertFalse(unknownTag.isSelfClosing());
    }

    @Test
    public void testIsBlock() {
        Tag blockTag = new Tag("p");
        assertTrue(blockTag.isBlock());

        Tag inlineTag = new Tag("span");
        assertFalse(inlineTag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag blockTag = new Tag("div");
        assertTrue(blockTag.formatAsBlock());

        Tag inlineTag = new Tag("span");
        assertTrue(inlineTag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag blockTag = new Tag("ul");
        assertTrue(blockTag.canContainBlock());

        Tag inlineTag = new Tag("span");
        assertFalse(inlineTag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag blockTag = new Tag("div");
        assertFalse(blockTag.isInline());

        Tag inlineTag = new Tag("span");
        assertTrue(inlineTag.isInline());
    }

    @Test
    public void testIsData() {
        Tag dataTag = new Tag("pre");
        assertTrue(dataTag.isData());

        Tag nonDataTag = new Tag("div");
        assertFalse(nonDataTag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag emptyTag = new Tag("img");
        assertTrue(emptyTag.isEmpty());

        Tag nonEmptyTag = new Tag("div");
        assertFalse(nonEmptyTag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag selfClosingTag = new Tag("img");
        assertTrue(selfClosingTag.isSelfClosing());

        Tag nonSelfClosingTag = new Tag("div");
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
    public void testIsKnownTagWithName() {
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag preserveWhitespaceTag = new Tag("pre");
        assertTrue(preserveWhitespaceTag.preserveWhitespace());

        Tag nonPreserveWhitespaceTag = new Tag("div");
        assertFalse(nonPreserveWhitespaceTag.preserveWhitespace());
    }

    @Test
    public void testSetSelfClosing() {
        Tag tag = new Tag("div");
        assertFalse(tag.isSelfClosing());
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("div");
        Tag tag3 = new Tag("span");

        assertTrue(tag1.equals(tag2));
        assertFalse(tag1.equals(tag3));
    }

    @Test
    public void testHashCode() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("div");
        assertTrue(tag1.hashCode() == tag2.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.toString());
    }
    
    // Add any additional test methods here

}