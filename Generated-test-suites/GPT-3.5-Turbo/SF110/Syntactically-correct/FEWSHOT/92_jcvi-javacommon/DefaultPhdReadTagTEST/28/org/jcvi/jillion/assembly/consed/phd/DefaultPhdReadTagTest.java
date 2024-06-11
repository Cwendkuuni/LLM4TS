package org.jcvi.jillion.assembly.consed.phd;

import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {
    @Test
    public void testGetType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", null, null, null, null);
        assertEquals("type", tag.getType());
    }
    
    @Test
    public void testGetSource() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", null, null, null, null);
        assertEquals("source", tag.getSource());
    }
    
    @Test
    public void testGetUngappedRange() {
        Range range = new Range(1, 10);
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", range, null, null, null);
        assertEquals(range, tag.getUngappedRange());
    }
    
    @Test
    public void testGetDate() {
        Date date = new Date();
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", null, date, null, null);
        assertEquals(date, tag.getDate());
    }
    
    @Test
    public void testGetComment() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", null, null, "comment", null);
        assertEquals("comment", tag.getComment());
    }
    
    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", null, null, null, "freeData");
        assertEquals("freeData", tag.getFreeFormData());
    }
    
    @Test
    public void testHashCode() {
        Range range = new Range(1, 10);
        Date date = new Date();
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", range, date, "comment", "freeData");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", range, date, "comment", "freeData");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    @Test
    public void testEquals() {
        Range range = new Range(1, 10);
        Date date = new Date();
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", range, date, "comment", "freeData");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", range, date, "comment", "freeData");
        assertEquals(tag1, tag2);
    }
}