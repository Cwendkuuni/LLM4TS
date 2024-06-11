import org.junit.*;
import static org.junit.Assert.*;

public class TagTest {
    
    private Tag tag;

    @Before
    public void setUp() {
        tag = new Tag("test");
    }

    @Test
    public void testGetName() {
        assertEquals("test", tag.getName());
    }

    @Test
    public void testValueOf_existingTag() {
        Tag existingTag = Tag.valueOf("p");
        assertEquals("p", existingTag.getName());
        assertTrue(existingTag.isBlock());
        assertTrue(existingTag.formatAsBlock());
        assertTrue(existingTag.canContainBlock());
        assertFalse(existingTag.canContainInline());
        assertFalse(existingTag.isEmpty());
        assertFalse(existingTag.isSelfClosing());
        assertFalse(existingTag.preserveWhitespace());
    }

    @Test
    public void testValueOf_unknownTag() {
        Tag unknownTag = Tag.valueOf("unknown");
        assertEquals("unknown", unknownTag.getName());
        assertFalse(unknownTag.isBlock());
        assertTrue(unknownTag.formatAsBlock());
        assertTrue(unknownTag.canContainBlock());
        assertTrue(unknownTag.canContainInline());
        assertFalse(unknownTag.isEmpty());
        assertFalse(unknownTag.isSelfClosing());
        assertFalse(unknownTag.preserveWhitespace());
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
        assertFalse(tag.isKnownTag());
    }

    @Test
    public void testIsKnownTag_existingTag() {
        assertTrue(Tag.isKnownTag("pre"));
    }

    @Test
    public void testIsKnownTag_unknownTag() {
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
    public void testEquals_sameObject() {
        assertTrue(tag.equals(tag));
    }

    @Test
    public void testEquals_differentClass() {
        assertFalse(tag.equals(new Object()));
    }

    @Test
    public void testEquals_differentTagName() {
        Tag differentTag = new Tag("different");
        assertFalse(tag.equals(differentTag));
    }

    @Test
    public void testEquals_differentIsBlock() {
        Tag differentTag = new Tag("test");
        differentTag.isBlock = false;
        assertFalse(tag.equals(differentTag));
    }

    @Test
    public void testEquals_differentFormatAsBlock() {
        Tag differentTag = new Tag("test");
        differentTag.formatAsBlock = false;
        assertFalse(tag.equals(differentTag));
    }

    @Test
    public void testEquals_differentCanContainBlock() {
        Tag differentTag = new Tag("test");
        differentTag.canContainBlock = false;
        assertFalse(tag.equals(differentTag));
    }

    @Test
    public void testEquals_differentCanContainInline() {
        Tag differentTag = new Tag("test");
        differentTag.canContainInline = false;
        assertFalse(tag.equals(differentTag));
    }

    @Test
    public void testEquals_differentIsEmpty() {
        Tag differentTag = new Tag("test");
        differentTag.empty = true;
        assertFalse(tag.equals(differentTag));
    }

    @Test
    public void testEquals_differentIsSelfClosing() {
        Tag differentTag = new Tag("test");
        differentTag.selfClosing = true;
        assertFalse(tag.equals(differentTag));
    }

    @Test
    public void testEquals_differentPreserveWhitespace() {
        Tag differentTag = new Tag("test");
        differentTag.preserveWhitespace = true;
        assertFalse(tag.equals(differentTag));
    }

    @Test
    public void testHashCode() {
        assertEquals(tag.getName().hashCode(), tag.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("test", tag.toString());
    }

    @Test
    public void testBlockTags() {
        for (String tagName : Tag.blockTags) {
            assertTrue(Tag.isKnownTag(tagName));
            assertTrue(Tag.valueOf(tagName).isBlock());
        }
    }

    @Test
    public void testInlineTags() {
        for (String tagName : Tag.inlineTags) {
            assertTrue(Tag.isKnownTag(tagName));
            assertFalse(Tag.valueOf(tagName).isBlock());
        }
    }

    @Test
    public void testEmptyTags() {
        for (String tagName : Tag.emptyTags) {
            assertTrue(Tag.isKnownTag(tagName));
            assertTrue(Tag.valueOf(tagName).isEmpty());
        }
    }

    @Test
    public void testFormatAsInlineTags() {
        for (String tagName : Tag.formatAsInlineTags) {
            assertTrue(Tag.isKnownTag(tagName));
            assertFalse(Tag.valueOf(tagName).formatAsBlock());
        }
    }

    @Test
    public void testPreserveWhitespaceTags() {
        for (String tagName : Tag.preserveWhitespaceTags) {
            assertTrue(Tag.isKnownTag(tagName));
            assertTrue(Tag.valueOf(tagName).preserveWhitespace());
        }
    }

    @After
    public void tearDown() {
        tag = null;
    }
}