import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {

    private Tag tag;

    @Before
    public void setUp() {
        tag = Tag.valueOf("p");
    }

    @Test
    public void testGetName() {
        assertEquals("p", tag.getName());
    }

    @Test
    public void testIsBlock() {
        assertTrue(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        assertFalse(tag.isInline());
    }

    @Test
    public void testIsData() {
        assertFalse(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(tag.isKnownTag());
    }

    @Test
    public void testIsKnownTagWithUnknownTag() {
        assertFalse(Tag.isKnownTag("unknown"));
    }

    @Test
    public void testPreserveWhitespace() {
        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testSetSelfClosing() {
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        Tag otherTag = Tag.valueOf("p");
        assertTrue(tag.equals(otherTag));
    }

    @Test
    public void testNotEquals() {
        Tag otherTag = Tag.valueOf("div");
        assertFalse(tag.equals(otherTag));
    }

    @Test
    public void testToString() {
        assertEquals("p", tag.toString());
    }

    @Test
    public void testValueOfWithKnownTag() {
        Tag otherTag = Tag.valueOf("p");
        assertEquals(tag, otherTag);
    }

    @Test
    public void testValueOfWithUnknownTag() {
        Tag otherTag = Tag.valueOf("unknown");
        assertEquals("unknown", otherTag.getName());
    }

    @Test
    public void testInternalInitializationForBlockTags() {
        for (String tagName : Tag.blockTags) {
            Tag blockTag = Tag.valueOf(tagName);
            assertTrue(blockTag.isBlock());
            assertTrue(blockTag.formatAsBlock());
            assertTrue(blockTag.canContainBlock());
            assertFalse(blockTag.isInline());
            assertFalse(blockTag.isData());
        }
    }

    @Test
    public void testInternalInitializationForInlineTags() {
        for (String tagName : Tag.inlineTags) {
            Tag inlineTag = Tag.valueOf(tagName);
            assertFalse(inlineTag.isBlock());
            assertFalse(inlineTag.formatAsBlock());
            assertFalse(inlineTag.canContainBlock());
        }
    }

    @Test
    public void testInternalInitializationForEmptyTags() {
        for (String tagName : Tag.emptyTags) {
            Tag emptyTag = Tag.valueOf(tagName);
            assertFalse(emptyTag.canContainBlock());
            assertFalse(emptyTag.canContainInline());
            assertTrue(emptyTag.isEmpty());
        }
    }

    @Test
    public void testInternalInitializationForFormatAsInlineTags() {
        for (String tagName : Tag.formatAsInlineTags) {
            Tag formatAsInlineTag = Tag.valueOf(tagName);
            assertFalse(formatAsInlineTag.formatAsBlock());
        }
    }

    @Test
    public void testInternalInitializationForPreserveWhitespaceTags() {
        for (String tagName : Tag.preserveWhitespaceTags) {
            Tag preserveWhitespaceTag = Tag.valueOf(tagName);
            assertTrue(preserveWhitespaceTag.preserveWhitespace());
        }
    }

    
}