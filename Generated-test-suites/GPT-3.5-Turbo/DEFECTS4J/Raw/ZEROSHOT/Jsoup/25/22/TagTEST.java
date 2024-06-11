import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest {

    @Test
    public void testGetName() {
        Tag tag = new Tag("p");
        String name = tag.getName();
        assertEquals("p", name);
    }

    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("p");
        assertNotNull(tag);
    }

    @Test
    public void testIsBlock() {
        Tag tag = new Tag("p");
        boolean isBlock = tag.isBlock();
        assertTrue(isBlock);
    }

    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("p");
        boolean formatAsBlock = tag.formatAsBlock();
        assertTrue(formatAsBlock);
    }

    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("p");
        boolean canContainBlock = tag.canContainBlock();
        assertTrue(canContainBlock);
    }

    @Test
    public void testIsInline() {
        Tag tag = new Tag("p");
        boolean isInline = tag.isInline();
        assertFalse(isInline);
    }

    @Test
    public void testIsData() {
        Tag tag = new Tag("p");
        boolean isData = tag.isData();
        assertFalse(isData);
    }

    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("p");
        boolean isEmpty = tag.isEmpty();
        assertFalse(isEmpty);
    }

    @Test
    public void testIsSelfClosing() {
        Tag tag = new Tag("p");
        boolean isSelfClosing = tag.isSelfClosing();
        assertFalse(isSelfClosing);
    }

    @Test
    public void testIsKnownTag() {
        Tag tag = new Tag("p");
        boolean isKnownTag = tag.isKnownTag();
        assertTrue(isKnownTag);
    }

    @Test
    public void testIsKnownTagWithName() {
        boolean isKnownTag = Tag.isKnownTag("p");
        assertTrue(isKnownTag);
    }

    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("p");
        boolean preserveWhitespace = tag.preserveWhitespace();
        assertFalse(preserveWhitespace);
    }

    @Test
    public void testSetSelfClosing() {
        Tag tag = new Tag("p");
        Tag selfClosingTag = tag.setSelfClosing();
        assertTrue(selfClosingTag.isSelfClosing());
    }

    @Test
    public void testEquals() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testHashCode() {
        Tag tag1 = new Tag("p");
        Tag tag2 = new Tag("p");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testToString() {
        Tag tag = new Tag("p");
        String string = tag.toString();
        assertEquals("p", string);
    }
}