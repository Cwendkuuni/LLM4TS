import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TagTest {

    private Tag tag;

    @Before
    public void setup() {
        tag = new Tag("test");
    }

    @Test
    public void testGetName() {
        assertEquals("test", tag.getName());
    }

    @Test
    public void testValueOf() {
        Tag tag1 = Tag.valueOf("p");
        assertTrue(tag1.isKnownTag());

        Tag tag2 = Tag.valueOf("div");
        assertTrue(tag2.isKnownTag());

        Tag tag3 = Tag.valueOf("customtag");
        assertFalse(tag3.isKnownTag());
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
        assertFalse(Tag.isKnownTag("test"));
    }

    @Test
    public void testPreserveWhitespace() {
        assertFalse(tag.preserveWhitespace());
    }

    @Test
    public void testSetSelfClosing() {
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testEquality() {
        Tag tag1 = new Tag("test");
        assertEquals(tag, tag1);

        Tag tag2 = new Tag("test2");
        assertFalse(tag.equals(tag2));
    }

    @Test
    public void testHashCode() {
        Tag tag1 = new Tag("test");
        assertEquals(tag.hashCode(), tag1.hashCode());

        Tag tag2 = new Tag("test2");
        assertNotEquals(tag.hashCode(), tag2.hashCode());
    }
    
    @Test
    public void testToString() {
        assertEquals("test", tag.toString());
    }
}