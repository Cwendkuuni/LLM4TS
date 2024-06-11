import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest {
    private Tag tag;

    @Before
    public void setUp() {
        tag = new Tag("div");
    }

    @Test
    public void testGetName() {
        assertEquals("div", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag newTag = Tag.valueOf("span");
        assertNotNull(newTag);
        assertTrue(newTag.isInline());
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
        assertTrue(Tag.isKnownTag("div"));
    }

    @Test
    public void testIsKnownTagFalse() {
        assertFalse(Tag.isKnownTag("customTag"));
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
    public void testEquals() {
        Tag sameTag = new Tag("div");
        assertTrue(tag.equals(sameTag));
    }

    @Test
    public void testHashCode() {
        assertEquals(tag.hashCode(), new Tag("div").hashCode());
    }
}