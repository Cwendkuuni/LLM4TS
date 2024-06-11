
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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
    public void testToString() {
        assertEquals("test", tag.toString());
    }

    @Test
    public void testIsBlock() {
        assertFalse(tag.isBlock());
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
        assertTrue(tag.isInline());
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
    public void testPreserveWhitespace() {
        assertFalse(tag.preserveWhitespace());
    }
}
