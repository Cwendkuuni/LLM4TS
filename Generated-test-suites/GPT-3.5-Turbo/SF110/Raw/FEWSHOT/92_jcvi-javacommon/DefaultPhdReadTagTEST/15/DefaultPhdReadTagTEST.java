package org.jcvi.jillion.assembly.consed.phd;

import org.junit.Test;

import java.util.Date;

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
        Range ungappedRange = new Range(0, 10);
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", ungappedRange, null, null, null);
        assertEquals(ungappedRange, tag.getUngappedRange());
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
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", null, null, null, "freeFormData");
        assertEquals("freeFormData", tag.getFreeFormData());
    }

    @Test
    public void testHashCode() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", null, null, null, null);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", null, null, null, null);
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testEquals() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", null, null, null, null);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", null, null, null, null);
        assertTrue(tag1.equals(tag2));
    }

}