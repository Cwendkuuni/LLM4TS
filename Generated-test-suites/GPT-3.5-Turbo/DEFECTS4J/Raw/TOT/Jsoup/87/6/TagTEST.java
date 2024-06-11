import org.junit.Test;
import org.jsoup.parser.Tag;
import org.jsoup.helper.Validate;

import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testName() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
    }

    @Test
    public void testValueOfWithSettings() {
        Tag tag = Tag.valueOf("DIV", ParseSettings.htmlDefault);
        assertNotNull(tag);
        assertEquals("div", tag.getName());
        assertFalse(tag.isBlock());
    }

    @Test
    public void testValueOfWithoutSettings() {
        Tag tag = Tag.valueOf("span");
        assertNotNull(tag);
        assertEquals("span", tag.getName());
        assertFalse(tag.isBlock());
    }

    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("b", ParseSettings.htmlDefault);
        assertFalse(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = Tag.valueOf("h1", ParseSettings.htmlDefault);
        assertTrue(tag.formatAsBlock());
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
    }

    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("meta");
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
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("a"));
        assertFalse(Tag.isKnownTag("custom-tag"));
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
        Tag tag = Tag.valueOf("button");
        assertTrue(tag.isFormSubmittable());
    }

    @Test
    public void testSetSelfClosing() {
        Tag tag = Tag.valueOf("custom-tag");
        assertFalse(tag.isSelfClosing());
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("div");
        assertEquals(tag1, tag2);
    }

    @Test
    public void testHashCode() {
        Tag tag1 = Tag.valueOf("h1");
        Tag tag2 = Tag.valueOf("h1");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("a");
        assertEquals("a", tag.toString());
    }
}