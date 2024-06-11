package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.jcvi.jillion.core.Range;
import org.junit.Test;
import java.util.Date;

public class DefaultPhdReadTagTest {
    @Test
    public void testGetType() {
        DefaultPhdReadTag readTag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        assertEquals("type", readTag.getType());
    }
    
    @Test
    public void testGetSource() {
        DefaultPhdReadTag readTag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        assertEquals("source", readTag.getSource());
    }
    
    @Test
    public void testGetUngappedRange() {
        Range range = new Range(1, 10);
        DefaultPhdReadTag readTag = new DefaultPhdReadTag("type", "source", range, new Date(), "comment", "freeFormData");
        assertEquals(range, readTag.getUngappedRange());
    }
    
    @Test
    public void testGetDate() {
        Date date = new Date();
        DefaultPhdReadTag readTag = new DefaultPhdReadTag("type", "source", new Range(1, 10), date, "comment", "freeFormData");
        assertEquals(date, readTag.getDate());
    }
    
    @Test
    public void testGetComment() {
        DefaultPhdReadTag readTag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        assertEquals("comment", readTag.getComment());
    }
    
    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag readTag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        assertEquals("freeFormData", readTag.getFreeFormData());
    }
    
    @Test
    public void testHashCode() {
        DefaultPhdReadTag readTag1 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        DefaultPhdReadTag readTag2 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        assertNotNull(readTag1.hashCode());
        assertEquals(readTag1.hashCode(), readTag2.hashCode());
    }
    
    @Test
    public void testEquals() {
        DefaultPhdReadTag readTag1 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        DefaultPhdReadTag readTag2 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        assertEquals(true, readTag1.equals(readTag2));
    }
}