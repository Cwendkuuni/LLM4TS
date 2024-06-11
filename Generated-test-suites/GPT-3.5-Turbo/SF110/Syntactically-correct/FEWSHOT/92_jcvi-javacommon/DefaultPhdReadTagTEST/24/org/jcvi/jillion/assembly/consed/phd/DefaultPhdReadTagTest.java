package org.jcvi.jillion.assembly.consed.phd;

import org.jcvi.jillion.core.Range;
import java.util.Date;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DefaultPhdReadTagTest {
    @Test
    public void testGetType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "freeFormData");
        assertEquals("type", tag.getType());
    }
    
    @Test
    public void testGetSource() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "freeFormData");
        assertEquals("source", tag.getSource());
    }
    
    @Test
    public void testGetUngappedRange() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "freeFormData");
        assertEquals(new Range(1, 5), tag.getUngappedRange());
    }
    
    @Test
    public void testGetDate() {
        Date date = new Date();
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 5), date, "comment", "freeFormData");
        assertEquals(date, tag.getDate());
    }
    
    @Test
    public void testGetComment() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "freeFormData");
        assertEquals("comment", tag.getComment());
    }
    
    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "freeFormData");
        assertEquals("freeFormData", tag.getFreeFormData());
    }
    
    @Test
    public void testHashCode() {
        Date date = new Date();
        Range range = new Range(1, 5);
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", range, date, "comment", "freeFormData");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", range, date, "comment", "freeFormData");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    @Test
    public void testEquals() {
        Date date = new Date();
        Range range = new Range(1, 5);
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", range, date, "comment", "freeFormData");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", range, date, "comment", "freeFormData");
        assertEquals(tag1, tag2);
    }
}