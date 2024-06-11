package org.jsoup.parser;
import org.junit.Test;

import static org.junit.Assert.*;

public class TagTest {
    
    @Test
    public void testGetName() {
        Tag tag = Tag.valueOf("p");
        assertEquals("p", tag.getName());
    }
    
    @Test
    public void testValueOfWithSettings() {
        Tag tag = Tag.valueOf("p", ParseSettings.preserveCase);
        assertEquals(false, tag.isBlock());
    }
    
    @Test
    public void testValueOfWithoutSettings() {
        Tag tag = Tag.valueOf("p");
        assertEquals(false, tag.isBlock());
    }
    
    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("p");
        assertEquals(true, tag.isBlock());
    }
    
    @Test
    public void testFormatAsBlock() {
        Tag tag = Tag.valueOf("p");
        assertEquals(true, tag.formatAsBlock());
    }
    
    @Test
    public void testCanContainBlock() {
        Tag tag = Tag.valueOf("p");
        assertEquals(true, tag.canContainBlock());
    }
    
    @Test
    public void testIsInline() {
        Tag tag = Tag.valueOf("p");
        assertEquals(false, tag.isInline());
    }
    
    @Test
    public void testIsData() {
        Tag tag = Tag.valueOf("p");
        assertEquals(false, tag.isData());
    }
    
    @Test
    public void testIsEmpty() {
        Tag tag = Tag.valueOf("p");
        assertEquals(false, tag.isEmpty());
    }
    
    @Test
    public void testIsSelfClosing() {
        Tag tag = Tag.valueOf("p");
        assertEquals(false, tag.isSelfClosing());
    }
    
    @Test
    public void testIsKnownTag() {
        Tag tag = Tag.valueOf("p");
        assertEquals(true, tag.isKnownTag());
    }
    
    @Test
    public void testIsKnownTagWithStringParam() {
        boolean result = Tag.isKnownTag("p");
        assertEquals(true, result);
    }
    
    @Test
    public void testPreserveWhitespace() {
        Tag tag = Tag.valueOf("p");
        assertEquals(false, tag.preserveWhitespace());
    }
    
    @Test
    public void testIsFormListed() {
        Tag tag = Tag.valueOf("p");
        assertEquals(false, tag.isFormListed());
    }
    
    @Test
    public void testIsFormSubmittable() {
        Tag tag = Tag.valueOf("p");
        assertEquals(false, tag.isFormSubmittable());
    }
    
    @Test
    public void testSetSelfClosing() {
        Tag tag = Tag.valueOf("p");
        Tag modifiedTag = tag.setSelfClosing();
        assertEquals(true, modifiedTag.isSelfClosing());
    }
    
    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("p");
        assertEquals(true, tag1.equals(tag2));
    }
    
    @Test
    public void testHashCode() {
        Tag tag = Tag.valueOf("p");
        int hashCode = tag.hashCode();
        assertNotNull(hashCode);
    }
    
    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("p");
        String str = tag.toString();
        assertNotNull(str);
    }

}