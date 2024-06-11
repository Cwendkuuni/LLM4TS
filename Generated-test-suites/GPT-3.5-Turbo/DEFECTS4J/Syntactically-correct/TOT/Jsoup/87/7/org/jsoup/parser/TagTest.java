import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
    public void testFormatAsBlock() {
        assertEquals(true, tag.formatAsBlock());
    }

    @Test
    public void testCanContainInline() {
        assertEquals(true, tag.canContainInline());
    }

    @Test
    public void testEmpty() {
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