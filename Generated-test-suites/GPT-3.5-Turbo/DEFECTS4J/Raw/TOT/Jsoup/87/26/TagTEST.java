import org.jsoup.parser.Tag;
import org.jsoup.helper.Validate;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest {
    private static Tag tag;

    @BeforeClass
    public static void setUp() {
        tag = new Tag("test");
    }

    @Test
    public void testGetName() {
        assertEquals("test", tag.getName());
    }

    @Test
    public void testValueOfWithTagName() {
        Tag genericTag = Tag.valueOf("genericTag");
        assertNotNull(genericTag);
        assertEquals("genericTag", genericTag.getName());
        assertFalse(genericTag.isBlock());
        assertFalse(genericTag.isKnownTag());

        Tag knownTag = Tag.valueOf("p");
        assertNotNull(knownTag);
        assertEquals("p", knownTag.getName());
        assertTrue(knownTag.isBlock());
        assertTrue(knownTag.isKnownTag());
    }

    @Test
    public void testValueOfWithTagAndSettings() {
        Tag tag = Tag.valueOf("Tag", ParseSettings.preserveCase);
        assertNotNull(tag);
        assertEquals("Tag", tag.getName());
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
    public void testIsKnownTagWithName() {
        assertFalse(Tag.isKnownTag("test"));
    }

    @Test
    public void testPreserveWhitespace() {
        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        assertFalse(tag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        assertFalse(tag.isFormSubmittable());
    }

    @Test
    public void testEquals() {
        assertEquals(tag, new Tag("test"));
        assertNotEquals(tag, new Tag("other"));
    }

    @Test
    public void testHashCode() {
        assertEquals(tag.hashCode(), new Tag("test").hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("test", tag.toString());
    }

    @Test
    public void testInternalStaticInitializers() {
        assertNotNull(Tag.valueOf("script"));
        assertTrue(Tag.isKnownTag("html"));
        assertTrue(Tag.isKnownTag("body"));
        assertTrue(Tag.isKnownTag("title"));
        assertFalse(Tag.isKnownTag("unknownTag"));
    }
}