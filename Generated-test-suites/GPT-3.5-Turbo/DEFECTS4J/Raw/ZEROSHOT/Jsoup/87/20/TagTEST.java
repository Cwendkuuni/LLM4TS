import org.jsoup.parser.ParseSettings;
import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;
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
    public void testValueOfWithNameAndSettings() {
        Tag newTag = Tag.valueOf("testTag", ParseSettings.preserveCase);
        assertEquals(tag, newTag);
    }

    @Test
    public void testValueOfWithName() {
        Tag newTag = Tag.valueOf("testTag");
        assertEquals(tag, newTag);
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
        assertFalse(Tag.isKnownTag("testTag"));
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
    public void testSetSelfClosing() {
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        Tag newTag = new Tag("testTag");
        assertTrue(tag.equals(newTag));
    }

    @Test
    public void testHashCode() {
        Tag newTag = new Tag("testTag");
        assertEquals(tag.hashCode(), newTag.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("testTag", tag.toString());
    }
}