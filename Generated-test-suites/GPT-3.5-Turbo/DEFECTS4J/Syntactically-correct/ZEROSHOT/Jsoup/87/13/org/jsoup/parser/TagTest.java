import org.jsoup.helper.Validate;
import org.junit.Test;

import org.jsoup.parser.ParseSettings;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("tag");
        assertEquals("tag", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("tag", ParseSettings.preserveCase);
        assertNotNull(tag);
    }

    @Test
    public void testValueOf_withSettings() {
        Tag tag = Tag.valueOf("tag", ParseSettings.preserveCase);
        assertNotNull(tag);
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("tag");
        tag.isBlock = true;
        assertTrue(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("tag");
        tag.formatAsBlock = true;
        assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("tag");
        tag.isBlock = true;
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        Tag tag = new Tag("tag");
        tag.isBlock = false;
        assertTrue(tag.isInline());
    }

    @Test
    public void testIsData() {
        Tag tag = new Tag("tag");
        tag.canContainInline = false;
        assertFalse(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("tag");
        assertFalse(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = new Tag("tag");
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        assertFalse(Tag.isKnownTag("tag"));
    }

    @Test
    public void testIsKnownTag_String() {
        assertFalse(Tag.isKnownTag("tag"));
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("tag");
        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        Tag tag = new Tag("tag");
        assertFalse(tag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        Tag tag = new Tag("tag");
        assertFalse(tag.isFormSubmittable());
    }

    @Test
    public void testSetSelfClosing() {
        Tag tag = new Tag("tag");
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("tag");
        Tag tag2 = new Tag("tag");
        assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testHashCode() {
        Tag tag = new Tag("tag");
        assertNotNull(tag.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("tag");
        assertEquals("tag", tag.toString());
    }

    @Test
    public void testRegister() {
        Tag tag = new Tag("tag");
        Tag.register(tag);
        assertTrue(Tag.isKnownTag("tag"));
    }
}