import org.junit.Test;

public class TagTest {
    @Test
    public void testGetName() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.getName());
    }
    
    @Test
    public void testValueOf() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
    }
    
    @Test
    public void testIsBlock() {
        Tag tag = new Tag("div");
        assertTrue(tag.isBlock());
    }
    
    @Test
    public void testFormatAsBlock() {
        Tag tag = new Tag("div");
        assertTrue(tag.formatAsBlock());
    }
    
    @Test
    public void testCanContainBlock() {
        Tag tag = new Tag("div");
        assertTrue(tag.canContainBlock());
    }
    
    @Test
    public void testIsInline() {
        Tag tag = new Tag("div");
        assertFalse(tag.isInline());
    }
    
    @Test
    public void testIsData() {
        Tag tag = new Tag("div");
        assertFalse(tag.isData());
    }
    
    @Test
    public void testIsEmpty() {
        Tag tag = new Tag("div");
        assertFalse(tag.isEmpty());
    }
    
    @Test
    public void testIsSelfClosing() {
        Tag tag = new Tag("div");
        assertFalse(tag.isSelfClosing());
    }
    
    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("div"));   
    }
    
    @Test
    public void testPreserveWhitespace() {
        Tag tag = new Tag("div");
        assertFalse(tag.preserveWhitespace());
    }
    
    @Test
    public void testSetSelfClosing() {
        Tag tag = new Tag("div");
        tag.setSelfClosing();
        assertTrue(tag.isSelfClosing());
    }
    
    @Test
    public void testEquals() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("div");
        assertEquals(tag1, tag2);
    }
    
    @Test
    public void testHashCode() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("div");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    @Test
    public void testToString() {
        Tag tag = new Tag("div");
        assertEquals("div", tag.toString());
    }
    
    // Additional test cases for edge cases and exceptions
    
    @Test(expected = NullPointerException.class)
    public void testNullValueOf() {
        Tag.valueOf(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyValueOf() {
        Tag.valueOf("");
    }
    
    @Test
    public void testUnknownTag() {
        Tag tag = Tag.valueOf("unknown");
        assertEquals("unknown", tag.getName());
    }
    
    @Test
    public void testIsKnownTagInvalid() {
        assertFalse(Tag.isKnownTag("unknown"));
    }
}