
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
    public void testValueOfWithTagNameAndSettings() {
        ParseSettings settings = ParseSettings.preserveCase;
        Tag valueOfTag = Tag.valueOf("test", settings);
        assertEquals(tag, valueOfTag);
    }

    @Test
    public void testValueOfWithTagName() {
        Tag valueOfTag = Tag.valueOf("test");
        assertEquals(tag, valueOfTag);
    }

    @Test
    public void testIsBlock() {
        assertEquals(true, tag.isBlock());
    }

    @Test
    public void testFormatAsBlock() {
        assertEquals(true, tag.formatAsBlock());
    }

    @Test
    public void testCanContainBlock() {
        assertEquals(true, tag.canContainBlock());
    }

    @Test
    public void testIsInline() {
        assertEquals(false, tag.isInline());
    }

    @Test
    public void testIsData() {
        assertEquals(false, tag.isData());
    }

    @Test
    public void testIsEmpty() {
        assertEquals(false, tag.isEmpty());
    }

    @Test
    public void testIsSelfClosing() {
        assertEquals(false, tag.isSelfClosing());
    }

    @Test
    public void testIsKnownTag() {
        assertEquals(false, tag.isKnownTag());
    }

    @Test
    public void testIsKnownTagWithName() {
        assertEquals(false, tag.isKnownTag("test"));
    }

    @Test
    public void testPreserveWhitespace() {
        assertEquals(false, tag.preserveWhitespace());
    }

    @Test
    public void testIsFormListed() {
        assertEquals(false, tag.isFormListed());
    }

    @Test
    public void testIsFormSubmittable() {
        assertEquals(false, tag.isFormSubmittable());
    }
}
