import org.junit.Test;
import static org.junit.Assert.*;

public class TagTest {
    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.getName());
    }
    
    @Test
    public void testValueOf() {
        Tag tag1 = Tag.valueOf("p");
        assertTrue(tag1.isBlock());
        assertTrue(tag1.canContainBlock());
        
        Tag tag2 = Tag.valueOf("unknown");
        assertFalse(tag2.isBlock());
        assertTrue(tag2.canContainBlock());
    }
    
    @Test
    public void testIsBlock() {
        Tag tag1 = Tag.valueOf("div");
        assertTrue(tag1.isBlock());
        
        Tag tag2 = Tag.valueOf("span");
        assertFalse(tag2.isBlock());
    }
    
    @Test
    public void testFormatAsBlock() {
        Tag tag1 = Tag.valueOf("p");
        assertTrue(tag1.formatAsBlock());
        
        Tag tag2 = Tag.valueOf("a");
        assertFalse(tag2.formatAsBlock());
    }
    
    @Test
    public void testCanContainBlock() {
        Tag tag1 = Tag.valueOf("div");
        assertTrue(tag1.canContainBlock());
        
        Tag tag2 = Tag.valueOf("span");
        assertFalse(tag2.canContainBlock());
    }
    
    @Test
    public void testIsInline() {
        Tag tag1 = Tag.valueOf("div");
        assertFalse(tag1.isInline());
        
        Tag tag2 = Tag.valueOf("span");
        assertTrue(tag2.isInline());
    }
    
    @Test
    public void testIsData() {
        Tag tag1 = Tag.valueOf("script");
        assertFalse(tag1.isData());
        
        Tag tag2 = Tag.valueOf("img");
        assertTrue(tag2.isData());
    }
    
    @Test
    public void testIsEmpty() {
        Tag tag1 = Tag.valueOf("img");
        assertFalse(tag1.isEmpty());
        
        Tag tag2 = Tag.valueOf("br");
        assertTrue(tag2.isEmpty());
    }
    
    @Test
    public void testIsSelfClosing() {
        Tag tag1 = Tag.valueOf("img");
        assertTrue(tag1.isSelfClosing());
        
        Tag tag2 = Tag.valueOf("div");
        assertFalse(tag2.isSelfClosing());
    }
    
    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("p"));
        assertFalse(Tag.isKnownTag("unknown"));
    }
    
    @Test
    public void testPreserveWhitespace() {
        Tag tag1 = Tag.valueOf("pre");
        assertTrue(tag1.preserveWhitespace());
        
        Tag tag2 = Tag.valueOf("span");
        assertFalse(tag2.preserveWhitespace());
    }
    
    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("div");
        assertEquals(tag1, tag2);
    }
    
    @Test
    public void testHashCode() {
        Tag tag1 = Tag.valueOf("div");
        Tag tag2 = Tag.valueOf("div");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("a");
        assertEquals("a", tag.toString());
    }
    
    // Additional tests for internal static initialisers
    
    @Test
    public void testBlockTags() {
        for (String tagName : Tag.blockTags) {
            Tag tag = Tag.valueOf(tagName);
            assertTrue(tag.isBlock());
        }
    }
    
    @Test
    public void testInlineTags() {
        for (String tagName : Tag.inlineTags) {
            Tag tag = Tag.valueOf(tagName);
            assertFalse(tag.isBlock());
            assertFalse(tag.canContainBlock());
            assertFalse(tag.formatAsBlock());
        }
    }
    
    @Test
    public void testEmptyTags() {
        for (String tagName : Tag.emptyTags) {
            Tag tag = Tag.valueOf(tagName);
            assertFalse(tag.canContainBlock());
            assertFalse(tag.canContainInline());
            assertTrue(tag.isEmpty());
        }
    }
    
    @Test
    public void testFormatAsInlineTags() {
        for (String tagName : Tag.formatAsInlineTags) {
            Tag tag = Tag.valueOf(tagName);
            assertFalse(tag.formatAsBlock());
        }
    }
    
    @Test
    public void testPreserveWhitespaceTags() {
        for (String tagName : Tag.preserveWhitespaceTags) {
            Tag tag = Tag.valueOf(tagName);
            assertTrue(tag.preserveWhitespace());
        }
    }
}