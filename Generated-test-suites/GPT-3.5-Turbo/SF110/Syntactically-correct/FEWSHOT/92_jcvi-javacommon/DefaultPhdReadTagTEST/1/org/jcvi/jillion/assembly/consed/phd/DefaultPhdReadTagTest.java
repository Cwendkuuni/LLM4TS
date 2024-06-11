package org.jcvi.jillion.assembly.consed.phd;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import org.jcvi.jillion.core.Range;

import java.util.Date;

public class DefaultPhdReadTagTest {
    
    @Test
    public void testGetType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        assertEquals("type", tag.getType());
        
        tag = new DefaultPhdReadTag(null, "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        assertNull(tag.getType());
    }
    
    @Test
    public void testGetSource() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        assertEquals("source", tag.getSource());
        
        tag = new DefaultPhdReadTag("type", null, new Range(0, 10), new Date(), "comment", "freeFormData");
        assertNull(tag.getType());
    }
    
    @Test
    public void testGetUngappedRange() {
        Range range = new Range(0, 10);
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", range, new Date(), "comment", "freeFormData");
        assertEquals(range, tag.getUngappedRange());
        
        tag = new DefaultPhdReadTag("type", "source", null, new Date(), "comment", "freeFormData");
        assertNull(tag.getUngappedRange());
    }
    
    @Test
    public void testGetDate() {
        Date date = new Date();
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), date, "comment", "freeFormData");
        assertEquals(date, tag.getDate());
        
        tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), null, "comment", "freeFormData");
        assertNull(tag.getDate());
    }
    
    @Test
    public void testGetComment() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        assertEquals("comment", tag.getComment());
        
        tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), null, "freeFormData");
        assertNull(tag.getComment());
    }
    
    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        assertEquals("freeFormData", tag.getFreeFormData());
        
        tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", null);
        assertNull(tag.getFreeFormData());
    }
    
    @Test
    public void testHashCode() {
        Range range = new Range(0, 10);
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", range, new Date(), "comment", "freeFormData");
        
        String type = "type";
        String source = "source";
        Date date = new Date();
        String comment = "comment";
        String freeFormData = "freeFormData";
        
        int expectedHashCode = 31 * 1 + (comment == null ? 0 : comment.hashCode());
        expectedHashCode = 31 * expectedHashCode + (date == null ? 0 : date.hashCode());
        expectedHashCode = 31 * expectedHashCode + (freeFormData == null ? 0 : freeFormData.hashCode());
        expectedHashCode = 31 * expectedHashCode + (source == null ? 0 : source.hashCode());
        expectedHashCode = 31 * expectedHashCode + (type == null ? 0 : type.hashCode());
        expectedHashCode = 31 * expectedHashCode + (range == null ? 0 : range.hashCode());
        
        assertEquals(expectedHashCode, tag1.hashCode());
    }
    
    @Test
    public void testEquals() {
        Range range = new Range(0, 10);
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", range, new Date(), "comment", "freeFormData");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", range, new Date(), "comment", "freeFormData");
        
        assertEquals(tag1, tag2);
        
        DefaultPhdReadTag tag3 = new DefaultPhdReadTag(null, "source", range, new Date(), "comment", "freeFormData");
        assertNotEquals(tag1, tag3);
        
        DefaultPhdReadTag tag4 = new DefaultPhdReadTag("type", null, range, new Date(), "comment", "freeFormData");
        assertNotEquals(tag1, tag4);
        
        DefaultPhdReadTag tag5 = new DefaultPhdReadTag("type", "source", null, new Date(), "comment", "freeFormData");
        assertNotEquals(tag1, tag5);
        
        DefaultPhdReadTag tag6 = new DefaultPhdReadTag("type", "source", range, null, "comment", "freeFormData");
        assertNotEquals(tag1, tag6);
        
        DefaultPhdReadTag tag7 = new DefaultPhdReadTag("type", "source", range, new Date(), null, "freeFormData");
        assertNotEquals(tag1, tag7);
        
        DefaultPhdReadTag tag8 = new DefaultPhdReadTag("type", "source", range, new Date(), "comment", null);
        assertNotEquals(tag1, tag8);
    }
    
}