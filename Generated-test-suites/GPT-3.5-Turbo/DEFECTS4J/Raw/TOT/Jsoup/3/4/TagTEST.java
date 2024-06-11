package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

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
    public void testValueOf() {
        Tag tag1 = Tag.valueOf("p");
        Tag tag2 = Tag.valueOf("test");
        
        assertTrue(tag1.isInline());
        assertEquals("p", tag1.getName());
        assertFalse(tag1.canContainBlock());
        assertFalse(tag1.isData());
        assertFalse(tag1.isEmpty());
        assertFalse(tag1.preserveWhitespace());
        assertNull(tag1.getImplicitParent());
        assertTrue(tag1.isValidParent(tag2));
        
        assertTrue(tag2.isBlock());
        assertEquals("test", tag2.getName());
        assertTrue(tag2.canContainBlock());
        assertFalse(tag2.isData());
        assertFalse(tag2.isEmpty());
        assertFalse(tag2.preserveWhitespace());
        assertNull(tag2.getImplicitParent());
        assertTrue(tag2.isValidParent(tag1));
    }
    
    @Test
    public void testCanContain() {
        Tag tag1 = new Tag("div");
        Tag tag2 = new Tag("p");
        
        tag1.canContainInline = true;
        tag1.canContainBlock = false;
        tag2.isBlock = true;
        
        assertTrue(tag1.canContain(tag2));
        assertFalse(tag2.canContain(tag1));
    }
    
    @Test
    public void testIsBlock() {
        assertTrue(tag.isBlock());
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
        tag.canContainInline = false;
        assertFalse(tag.isData());
    }
    
    @Test
    public void testIsEmpty() {
        assertFalse(tag.isEmpty());
    }
    
    @Test
    public void testPreserveWhitespace() {
        assertFalse(tag.preserveWhitespace());
    }
    
    @Test
    public void testGetImplicitParent() {
        assertNull(tag.getImplicitParent());
    }
    
    @Test
    public void testIsValidParent() {
        Tag child = new Tag("p");
        child.ancestors.add(tag);
        
        assertTrue(tag.isValidParent(child));
    }
    
    @Test
    public void testEquals() {
        Tag tag1 = new Tag("test");
        tag1.canContainBlock = true;
        
        assertTrue(tag.equals(tag));
        assertFalse(tag.equals(null));
        assertFalse(tag.equals("test"));
        assertTrue(tag.equals(tag1));
    }
    
    @Test
    public void testHashCode() {
        Tag tag1 = new Tag("test");
        tag1.canContainBlock = true;
        
        assertEquals(tag.hashCode(), tag1.hashCode());
    }

}