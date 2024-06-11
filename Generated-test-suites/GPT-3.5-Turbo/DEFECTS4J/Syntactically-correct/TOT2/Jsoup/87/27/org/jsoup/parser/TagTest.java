import org.jsoup.parser.Tag;
import org.jsoup.parser.ParseSettings;
import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest {

    private Tag testTag;

    @Before
    public void setUp() {
        testTag = new Tag("testTag");
    }

    @Test
    public void testGetName() {
        assertEquals("testTag", testTag.getName());
    }

    @Test
    public void testValueOfParseSettings() {
        Tag testTag1 = Tag.valueOf("testTag", ParseSettings.preserveCase);
        assertEquals(testTag, testTag1);
    }

    @Test
    public void testValueOf() {
        Tag testTag2 = Tag.valueOf("testTag");
        assertEquals(testTag, testTag2);
    }

    @Test
    public void testIsBlock() {
        assertTrue(testTag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        assertTrue(testTag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        assertTrue(testTag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        assertFalse(testTag.isInline());
    }

    @Test
    public void testIsData() {
        assertFalse(testTag.isData());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(testTag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        assertFalse(testTag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        assertTrue(testTag.isKnownTag());
    }

    @Test
    public void testIsKnownTagString() {
        assertTrue(Tag.isKnownTag("testTag"));
    }

    @Test
    public void testPreserveWhitespace() {
        assertFalse(testTag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        assertFalse(testTag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        assertFalse(testTag.isFormSubmittable());
    }

    @Test
    public void testSetSelfClosing() {
        testTag.setSelfClosing();
        assertTrue(testTag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        assertEquals(testTag, new Tag("testTag"));
    }

    @Test
    public void testHashCode() {
        assertEquals(testTag.hashCode(), new Tag("testTag").hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("testTag", testTag.toString());
    }
}