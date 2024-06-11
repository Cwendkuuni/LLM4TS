import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Tag;
import org.junit.Test;

public class TagTest {
    private ParseSettings settings = ParseSettings.preserveCase;

    @Test
    public void testGetName() {
        Tag tag = new Tag("a");
        assertEquals("a", tag.getName());
    }

    @Test
    public void testValueOfWithTagNameAndSettings() {
        Tag tag = Tag.valueOf("p", settings);
        assertTrue(tag.isBlock());
        assertFalse(tag.formatAsBlock());
    }

    @Test
    public void testValueOfWithTagName() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.isBlock());
        assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("pre");
        assertTrue(tag.isBlock());
        assertFalse(tag.isInline());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = Tag.valueOf("span");
        assertFalse(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("strong");
        assertTrue(tag.isInline());
        assertFalse(tag.isBlock());
    }

    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("img");
        assertFalse(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("br");
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = Tag.valueOf("input");
        assertTrue(tag.isSelfClosing());
        assertFalse(tag.isBlock());
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("customtag"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("pre");
        assertTrue(tag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Tag tag = Tag.valueOf("input");
        assertTrue(tag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag tag = Tag.valueOf("select");
        assertTrue(tag.isFormSubmittable());
    }

    @Test
    public void testSetSelfClosing() {
        Tag tag = new Tag("customtag");
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("div");
        assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testHashCode() {
        Tag tag1 = Tag.valueOf("span");
        Tag tag2 = Tag.valueOf("span");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("a");
        assertEquals("a", tag.toString());
    }
}