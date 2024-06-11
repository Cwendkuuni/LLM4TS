import org.junit.jupiter.api.Test;

class TagTest {

    @Test
    void testName() {
        Tag tag = new Tag("test");
        assertEquals("test", tag.getName());
    }

    @Test
    void testValueOfKnownTag() {
        Tag tag = Tag.valueOf("p");
        assertTrue(tag.isKnownTag());
    }

    @Test
    void testValueOfUnknownTag() {
        Tag tag = Tag.valueOf("test");
        assertFalse(tag.isKnownTag());
    }

    @Test
    void testIsBlock() {
        Tag tag = new Tag("test");
        assertFalse(tag.isBlock());
    }

    @Test
    void testFormatAsBlock() {
        Tag tag = new Tag("test");
        assertTrue(tag.formatAsBlock());
    }

    @Test
    void testCanContainBlock() {
        Tag tag = new Tag("test");
        assertTrue(tag.canContainBlock());
    }

    @Test
    void testIsInline() {
        Tag tag = new Tag("test");
        assertTrue(tag.isInline());
    }

    @Test
    void testIsData() {
        Tag tag = new Tag("test");
        assertFalse(tag.isData());
    }

    @Test
    void testIsEmpty() {
        Tag tag = new Tag("test");
        assertFalse(tag.isEmpty());
    }

    @Test
    void testIsSelfClosing() {
        Tag tag = new Tag("test");
        assertFalse(tag.isSelfClosing());
    }

    @Test
    void testIsKnownTag() {
        Tag tag = new Tag("test");
        assertFalse(tag.isKnownTag());
    }

    @Test
    void testIsKnownTagWithName() {
        assertFalse(Tag.isKnownTag("test"));
    }

    @Test
    void testPreserveWhitespace() {
        Tag tag = new Tag("test");
        assertFalse(tag.preserveWhitespace());
    }

    @Test
    void testSetSelfClosing() {
        Tag tag = new Tag("test");
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }

    @Test
    void testEquals() {
        Tag tag1 = new Tag("test");
        Tag tag2 = new Tag("test");
        assertTrue(tag1.equals(tag2));
    }

    @Test
    void testHashCode() {
        Tag tag = new Tag("test");
        assertEquals(tag.getName().hashCode(), tag.hashCode());
    }

    @Test
    void testToString() {
        Tag tag = new Tag("test");
        assertEquals("test", tag.toString());
    }

}