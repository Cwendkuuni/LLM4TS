package org.jcvi.jillion.assembly.consed.phd;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import java.util.Date;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", null, new Date(), "comment", "freeFormData");
        assertEquals("type", tag.getType());
    }

    @Test
    public void testGetSource() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", null, new Date(), "comment", "freeFormData");
        assertEquals("source", tag.getSource());
    }

    @Test
    public void testGetUngappedRange() {
        Range range = new Range(0, 10);
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", range, new Date(), "comment", "freeFormData");
        assertEquals(range, tag.getUngappedRange());
    }

    @Test
    public void testGetDate() {
        Date date = new Date();
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", null, date, "comment", "freeFormData");
        assertEquals(date, tag.getDate());
    }

    @Test
    public void testGetComment() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", null, new Date(), "comment", "freeFormData");
        assertEquals("comment", tag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", null, new Date(), "comment", "freeFormData");
        assertEquals("freeFormData", tag.getFreeFormData());
    }

    @Test
    public void testHashCode() {
        Range range = new Range(0, 10);
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", range, new Date(), "comment", "freeFormData");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", range, new Date(), "comment", "freeFormData");
        assertTrue(tag1.hashCode() == tag2.hashCode());
    }

    @Test
    public void testEquals() {
        Range range = new Range(0, 10);
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", range, new Date(), "comment", "freeFormData");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", range, new Date(), "comment", "freeFormData");
        assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testEqualsNull() {
        Range range = new Range(0, 10);
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", range, new Date(), "comment", "freeFormData");
        assertFalse(tag1.equals(null));
    }

    @Test
    public void testEqualsDifferentType() {
        Range range = new Range(0, 10);
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", range, new Date(), "comment", "freeFormData");
        assertFalse(tag1.equals("string"));
    }

    @Test
    public void testEqualsDifferentComment() {
        Range range = new Range(0, 10);
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", range, new Date(), "comment", "freeFormData");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", range, new Date(), "different comment", "freeFormData");
        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testEqualsDifferentDate() {
        Range range = new Range(0, 10);
        Date date1 = new Date();
        Date date2 = new Date(date1.getTime() + 1000);
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", range, date1, "comment", "freeFormData");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", range, date2, "comment", "freeFormData");
        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testEqualsDifferentFreeFormData() {
        Range range = new Range(0, 10);
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", range, new Date(), "comment", "freeFormData");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", range, new Date(), "comment", "different freeFormData");
        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testEqualsDifferentSource() {
        Range range = new Range(0, 10);
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", range, new Date(), "comment", "freeFormData");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "different source", range, new Date(), "comment", "freeFormData");
        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testEqualsDifferentType() {
        Range range = new Range(0, 10);
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", range, new Date(), "comment", "freeFormData");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("different type", "source", range, new Date(), "comment", "freeFormData");
        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testEqualsDifferentUngappedRange() {
        Range range1 = new Range(0, 10);
        Range range2 = new Range(0, 5);
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", range1, new Date(), "comment", "freeFormData");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", range2, new Date(), "comment", "freeFormData");
        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testEqualsSameObject() {
        Range range = new Range(0, 10);
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", range, new Date(), "comment", "freeFormData");
        assertTrue(tag.equals(tag));
    }
    
}