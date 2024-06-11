package org.jcvi.jillion.assembly.consed.phd;

import org.jcvi.jillion.core.*;
import java.util.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class DefaultPhdReadTagTest {
    
    DefaultPhdReadTag defaultPhdReadTag;
    Date date = new Date();
    Range range = new Range(1, 10);
    
    @Before
    public void setup() {
        defaultPhdReadTag = new DefaultPhdReadTag("type", "source", range, date, "comment", "freeFormData");
    }
    
    @Test
    public void testGetType() {
        assertEquals("type", defaultPhdReadTag.getType());
    }
    
    @Test
    public void testGetSource() {
        assertEquals("source", defaultPhdReadTag.getSource());
    }
    
    @Test
    public void testGetUngappedRange() {
        assertEquals(range, defaultPhdReadTag.getUngappedRange());
    }
    
    @Test
    public void testGetDate() {
        assertEquals(date, defaultPhdReadTag.getDate());
    }
    
    @Test
    public void testGetComment() {
        assertEquals("comment", defaultPhdReadTag.getComment());
    }
    
    @Test
    public void testGetFreeFormData() {
        assertEquals("freeFormData", defaultPhdReadTag.getFreeFormData());
    }
    
    @Test
    public void testHashCode() {
        DefaultPhdReadTag other = new DefaultPhdReadTag("type", "source", range, date, "comment", "freeFormData");
        assertEquals(defaultPhdReadTag.hashCode(), other.hashCode());
    }
    
    @Test
    public void testEquals() {
        DefaultPhdReadTag other = new DefaultPhdReadTag("type", "source", range, date, "comment", "freeFormData");
        assertTrue(defaultPhdReadTag.equals(other));
    }
    
    @Test
    public void testEquals_SameObject() {
        assertTrue(defaultPhdReadTag.equals(defaultPhdReadTag));
    }
    
    @Test
    public void testEquals_NullObject() {
        assertFalse(defaultPhdReadTag.equals(null));
    }
    
    @Test
    public void testEquals_DifferentClass() {
        Object obj = new Object();
        assertFalse(defaultPhdReadTag.equals(obj));
    }
    
    @Test
    public void testEquals_DifferentType() {
        DefaultPhdReadTag other = new DefaultPhdReadTag("differentType", "source", range, date, "comment", "freeFormData");
        assertFalse(defaultPhdReadTag.equals(other));
    }
    
    @Test
    public void testEquals_DifferentSource() {
        DefaultPhdReadTag other = new DefaultPhdReadTag("type", "differentSource", range, date, "comment", "freeFormData");
        assertFalse(defaultPhdReadTag.equals(other));
    }
    
    @Test
    public void testEquals_DifferentUngappedRange() {
        Range differentRange = new Range(1, 5);
        DefaultPhdReadTag other = new DefaultPhdReadTag("type", "source", differentRange, date, "comment", "freeFormData");
        assertFalse(defaultPhdReadTag.equals(other));
    }
    
    @Test
    public void testEquals_DifferentDate() {
        Date differentDate = new Date(1);
        DefaultPhdReadTag other = new DefaultPhdReadTag("type", "source", range, differentDate, "comment", "freeFormData");
        assertFalse(defaultPhdReadTag.equals(other));
    }
    
    @Test
    public void testEquals_DifferentComment() {
        DefaultPhdReadTag other = new DefaultPhdReadTag("type", "source", range, date, "differentComment", "freeFormData");
        assertFalse(defaultPhdReadTag.equals(other));
    }
    
    @Test
    public void testEquals_DifferentFreeFormData() {
        DefaultPhdReadTag other = new DefaultPhdReadTag("type", "source", range, date, "comment", "differentFreeFormData");
        assertFalse(defaultPhdReadTag.equals(other));
    }
    
}