import static org.junit.Assert.*;
import org.junit.Test;

public class TagTest {
    
    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("tag");
        
        assertEquals("tag", tag.getName());
    }
    
    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("tag");
        
        assertTrue(tag.isBlock());
    }
    
    @Test
    public void testFormatAsBlock() {
        Tag tag = Tag.valueOf("tag");
        
        assertTrue(tag.formatAsBlock());
    }
    
    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("tag");
        
        assertTrue(tag.canContainBlock());
    }
    
    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("tag");
        
        assertFalse(tag.isInline());
    }
    
    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("tag");
        
        assertFalse(tag.isData());
    }
    
    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("tag");
        
        assertFalse(tag.isEmpty());
    }
    
    @Test
    public void testIsSelfClosing() {
        Tag tag = Tag.valueOf("tag");
        
        assertFalse(tag.isSelfClosing());
    }
    
    @Test
    public void testIsKnownTag() {
        assertTrue(Tag.isKnownTag("tag"));
    }
    
    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("tag");
        
        assertFalse(tag.preserveWhitespace());
    }
    
    @Test
    public void testSetSelfClosing() {
        Tag tag = Tag.valueOf("tag");
        tag.setSelfClosing();
        
        assertTrue(tag.isSelfClosing());
    }
    
    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("tag");
        Tag tag2 = Tag.valueOf("tag");
        
        assertTrue(tag1.equals(tag2));
    }
    
    @Test
    public void testHashCode() {
        Tag tag = Tag.valueOf("tag");
        
        assertNotNull(tag.hashCode());
    }
    
    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("tag");
        
        assertEquals("tag", tag.toString());
    }
    
    // Additional test cases for edge cases and exceptions
    
    @Test
    public void testValueOfUnknownTag() {
        Tag tag = Tag.valueOf("unknown");
        
        // Since unknown tags are not registered, equals() should return false
        assertFalse(tag.isKnownTag());
    }
    
    @Test(expected = NullPointerException.class)
    public void testValueOfNull() {
        Tag tag = Tag.valueOf(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testValueOfEmptyString() {
        Tag tag = Tag.valueOf("");
    }
    
    @Test
    public void testIsKnownTagWithKnownTag() {
        assertTrue(Tag.isKnownTag("p"));
    }
    
    @Test
    public void testIsKnownTagWithUnknownTag() {
        assertFalse(Tag.isKnownTag("unknown"));
    }
    
    @Test
    public void testCanContainInline() {
        Tag tag = Tag.valueOf("tag");
        
        assertTrue(tag.canContainInline());
    }
    
    @Test
    public void testIsKnownTagWithUnknownTagAfterRegister() {
        Tag.register(new Tag("unknown"));
        
        assertTrue(Tag.isKnownTag("unknown"));
    }
    
    @Test
    public void testIsKnownTagWithEmptyTagAfterRegister() {
        Tag.register(new Tag("empty"));
        
        assertTrue(Tag.isKnownTag("empty"));
    }
    
    @Test
    public void testFormatAsInlineTags() {
        Tag tag = Tag.valueOf("tag");
        
        assertTrue(tag.formatAsBlock());
    }
    
    @Test
    public void testPreserveWhitespaceTags() {
        Tag tag = Tag.valueOf("tag");
        
        assertFalse(tag.preserveWhitespace());
    }
    
    // Merge all the individual test cases into a complete JUnit 4 test file
    
    // 
    
    @Test
    public void testGetName() {
        // ...
    }
    
    @Test
    public void testIsBlock() {
        // ...
    }
    
    @Test
    public void testFormatAsBlock() {
        // ...
    }
    
    @Test
    public void testCanContainBlock() {
        // ...
    }
    
    @Test
    public void testIsInline() {
        // ...
    }
    
    @Test
    public void testIsData() {
        // ...
    }
    
    @Test
    public void testIsEmpty() {
        // ...
    }
    
    @Test
    public void testIsSelfClosing() {
        // ...
    }
    
    @Test
    public void testIsKnownTag() {
        // ...
    }
    
    @Test
    public void testPreserveWhitespace() {
        // ...
    }
    
    @Test
    public void testSetSelfClosing() {
        // ...
    }
    
    @Test
    public void testEquals() {
        // ...
    }
    
    @Test
    public void testHashCode() {
        // ...
    }
    
    @Test
    public void testToString() {
        // ...
    }
    
    @Test
    public void testValueOfUnknownTag() {
        // ...
    }
    
    @Test(expected = NullPointerException.class)
    public void testValueOfNull() {
        // ...
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testValueOfEmptyString() {
        // ...
    }
    
    @Test
    public void testIsKnownTagWithKnownTag() {
        // ...
    }
    
    @Test
    public void testIsKnownTagWithUnknownTag() {
        // ...
    }
    
    @Test
    public void testCanContainInline() {
        // ...
    }
    
    @Test
    public void testIsKnownTagWithUnknownTagAfterRegister() {
        // ...
    }
    
    @Test
    public void testIsKnownTagWithEmptyTagAfterRegister() {
        // ...
    }
    
    @Test
    public void testFormatAsInlineTags() {
        // ...
    }
    
    @Test
    public void testPreserveWhitespaceTags() {
        // ...
    }
    
    // 
}