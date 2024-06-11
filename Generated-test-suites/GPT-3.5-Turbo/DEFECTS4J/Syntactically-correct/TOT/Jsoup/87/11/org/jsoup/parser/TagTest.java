import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest {

    private ParseSettings parseSettings;

    @Before
    public void setUp() {
        parseSettings = ParseSettings.preserveCase;
    }

    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("div", parseSettings);
        assertEquals("div", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("p", parseSettings);
        assertTrue(tag.isBlock());
        assertFalse(tag.formatAsBlock());
        assertTrue(tag.canContainBlock());
        assertFalse(tag.isInline());
        assertFalse(tag.isData());
    }

    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("h1", parseSettings);
        assertTrue(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = Tag.valueOf("pre", parseSettings);
        assertFalse(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("div", parseSettings);
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("a", parseSettings);
        assertFalse(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("img", parseSettings);
        assertTrue(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("img", parseSettings);
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = Tag.valueOf("script", parseSettings);
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("unknownTag"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("pre", parseSettings);
        assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Tag tag = Tag.valueOf("input", parseSettings);
        assertTrue(tag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag tag = Tag.valueOf("input", parseSettings);
        assertTrue(tag.isFormSubmittable());
    }

    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("div", parseSettings);
        Tag tag2 = Tag.valueOf("div", parseSettings);
        assertEquals(tag1, tag2);
    }

    @Test
    public void testHashCode() {
        Tag tag1 = Tag.valueOf("div", parseSettings);
        Tag tag2 = Tag.valueOf("div", parseSettings);
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("div", parseSettings);
        assertEquals("div", tag.toString());
    }
}