
import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Tag;

import static org.junit.Assert.*;

public class TagTest {

    private Tag tag;

    @Before
    public void setUp() {
        tag = new Tag("testTag");
    }

    @Test
    public void testGetName() {
        assertEquals("testTag", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag1 = Tag.valueOf("testTag");
        Tag tag2 = Tag.valueOf("testtag");
        assertEquals(tag1, tag2);
    }

    @Test
    public void testValueOfWithSettings() {
        ParseSettings parseSettings = ParseSettings.preserveCase;
        tag = Tag.valueOf("TestTag", parseSettings);
        assertEquals("TestTag", tag.getName());
    }

    @Test
    public void testIsBlock() {
        assertTrue(tag.isBlock());
        tag.setSelfClosing();
        assertFalse(tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        assertTrue(tag.formatAsBlock());
        tag.setSelfClosing();
        assertTrue(tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        assertTrue(tag.canContainBlock());
        tag.setSelfClosing();
        assertTrue(tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        assertFalse(tag.isInline());
        tag.setSelfClosing();
        assertTrue(tag.isInline());
    }

    @Test
    public void testIsData() {
        assertFalse(tag.isData());
        tag.setSelfClosing();
        assertTrue(tag.isData());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(tag.isEmpty());
        tag.setSelfClosing();
        assertTrue(tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        assertFalse(tag.isSelfClosing());
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        assertFalse(tag.isKnownTag());
    }

    @Test
    public void testIsKnownTagWithName() {
        assertFalse(Tag.isKnownTag("testTag"));
    }

    @Test
    public void testPreserveWhitespace() {
        assertFalse(tag.preserveWhitespace());
        tag.setSelfClosing();
        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        assertFalse(tag.isFormListed());
        tag.setSelfClosing();
        assertFalse(tag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        assertFalse(tag.isFormSubmittable());
        tag.setSelfClosing();
        assertFalse(tag.isFormSubmittable());
    }

    @Test
    public void testSetSelfClosing() {
        assertFalse(tag.isSelfClosing());
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("testTag");
        assertEquals(tag, tag1);
    }

    @Test
    public void testHashCode() {
        Tag tag1 = Tag.valueOf("testTag");
        assertEquals(tag.hashCode(), tag1.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("testTag", tag.toString());
    }
}

