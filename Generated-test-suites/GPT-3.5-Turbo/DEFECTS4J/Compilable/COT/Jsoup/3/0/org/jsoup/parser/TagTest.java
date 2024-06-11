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
    public void testValueOf() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.getName());
    }
    
    @Test
    public void testCanContain() {
        Tag parentTag = Tag.valueOf("div");
        Tag childTag = Tag.valueOf("p");
        assertTrue(parentTag.canContain(childTag));
    }
    
    // Add more test methods for each public method in the Tag class
    
    @Test
    public void testIsBlock() {
        Tag tag = Tag.valueOf("div");
        assertTrue(tag.isBlock());
    }
    
    // Add more test methods for each public method in the Tag class
    
    @Test
    public void testEquals() {
        Tag tag1 = Tag.valueOf("a");
        Tag tag2 = Tag.valueOf("a");
        assertTrue(tag1.equals(tag2));
    }
    
    // Add more test methods for each public method in the Tag class
    
    @Test
    public void testHashCode() {
        Tag tag = Tag.valueOf("p");
        assertNotNull(tag.hashCode());
    }
    
    // Add more test methods for each public method in the Tag class
    
    @Test
    public void testToString() {
        Tag tag = Tag.valueOf("div");
        assertEquals("div", tag.toString());
    }
    
    // Add more test methods for each public method in the Tag class
}