import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.jsoup.parser.Tag;

public class TagTest {

    private static Tag genericTag;
    private static Tag knownTag;
    private static Tag emptyTag;
    private static Tag selfClosingTag;
    private static Tag preserveWhitespaceTag;

    @BeforeAll
    public static void setUp() {
        genericTag = Tag.valueOf("genericTag");
        knownTag = Tag.valueOf("p");
        emptyTag = Tag.valueOf("img");
        selfClosingTag = Tag.valueOf("selfClosingTag");
        selfClosingTag.setSelfClosing();
        preserveWhitespaceTag = Tag.valueOf("pre");
    }

    @Test
    public void testGetName() {
        assertEquals("genericTag", genericTag.getName());
        assertEquals("p", knownTag.getName());
        assertEquals("img", emptyTag.getName());
        assertEquals("selfClosingTag", selfClosingTag.getName());
        assertEquals("pre", preserveWhitespaceTag.getName());
    }

    @Test
    public void testIsBlock() {
        assertFalse(genericTag.isBlock());
        assertTrue(knownTag.isBlock());
        assertFalse(emptyTag.isBlock());
        assertFalse(selfClosingTag.isBlock());
        assertTrue(preserveWhitespaceTag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        assertTrue(genericTag.formatAsBlock());
        assertTrue(knownTag.formatAsBlock());
        assertTrue(emptyTag.formatAsBlock());
        assertTrue(selfClosingTag.formatAsBlock());
        assertTrue(preserveWhitespaceTag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        assertTrue(genericTag.canContainBlock());
        assertTrue(knownTag.canContainBlock());
        assertFalse(emptyTag.canContainBlock());
        assertTrue(selfClosingTag.canContainBlock());
        assertTrue(preserveWhitespaceTag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        assertTrue(genericTag.isInline());
        assertFalse(knownTag.isInline());
        assertTrue(emptyTag.isInline());
        assertTrue(selfClosingTag.isInline());
        assertFalse(preserveWhitespaceTag.isInline());
    }

    @Test
    public void testIsData() {
        assertTrue(genericTag.isData());
        assertFalse(knownTag.isData());
        assertFalse(emptyTag.isData());
        assertFalse(selfClosingTag.isData());
        assertFalse(preserveWhitespaceTag.isData());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(genericTag.isEmpty());
        assertFalse(knownTag.isEmpty());
        assertTrue(emptyTag.isEmpty());
        assertTrue(selfClosingTag.isEmpty());
        assertFalse(preserveWhitespaceTag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        assertFalse(genericTag.isSelfClosing());
        assertFalse(knownTag.isSelfClosing());
        assertTrue(emptyTag.isSelfClosing());
        assertTrue(selfClosingTag.isSelfClosing());
        assertFalse(preserveWhitespaceTag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        assertFalse(genericTag.isKnownTag());
        assertTrue(knownTag.isKnownTag());
        assertTrue(emptyTag.isKnownTag());
        assertTrue(selfClosingTag.isKnownTag());
        assertTrue(preserveWhitespaceTag.isKnownTag());
    }

    @Test
    public void testIsKnownTagWithName() {
        assertFalse(Tag.isKnownTag("genericTag"));
        assertTrue(Tag.isKnownTag("p"));
        assertTrue(Tag.isKnownTag("img"));
        assertTrue(Tag.isKnownTag("selfClosingTag"));
        assertTrue(Tag.isKnownTag("pre"));
    }

    @Test
    public void testPreserveWhitespace() {
        assertFalse(genericTag.preserveWhitespace());
        assertFalse(knownTag.preserveWhitespace());
        assertFalse(emptyTag.preserveWhitespace());
        assertFalse(selfClosingTag.preserveWhitespace());
        assertTrue(preserveWhitespaceTag.preserveWhitespace());
    }

    @Test
    public void testSetSelfClosing() {
        assertFalse(genericTag.isSelfClosing());
        assertFalse(knownTag.isSelfClosing());
        assertFalse(emptyTag.isSelfClosing());
        assertFalse(preserveWhitespaceTag.isSelfClosing());

        assertTrue(selfClosingTag.isSelfClosing());
        selfClosingTag.setSelfClosing();
        assertTrue(selfClosingTag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        assertNotEquals(genericTag, knownTag);
        assertNotEquals(knownTag, emptyTag);
        assertNotEquals(emptyTag, selfClosingTag);
        assertNotEquals(selfClosingTag, preserveWhitespaceTag);
        assertNotEquals(preserveWhitespaceTag, genericTag);
    }

    @Test
    public void testHashCode() {
        assertNotEquals(genericTag.hashCode(), knownTag.hashCode());
        assertNotEquals(knownTag.hashCode(), emptyTag.hashCode());
        assertNotEquals(emptyTag.hashCode(), selfClosingTag.hashCode());
        assertNotEquals(selfClosingTag.hashCode(), preserveWhitespaceTag.hashCode());
        assertNotEquals(preserveWhitespaceTag.hashCode(), genericTag.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("genericTag", genericTag.toString());
        assertEquals("p", knownTag.toString());
        assertEquals("img", emptyTag.toString());
        assertEquals("selfClosingTag", selfClosingTag.toString());
        assertEquals("pre", preserveWhitespaceTag.toString());
    }
}