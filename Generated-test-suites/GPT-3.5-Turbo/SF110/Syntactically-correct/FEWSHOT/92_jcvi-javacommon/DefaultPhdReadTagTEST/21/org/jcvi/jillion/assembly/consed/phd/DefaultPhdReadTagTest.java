package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jcvi.jillion.core.Range;
import java.util.Date;
import org.junit.Test;

public class DefaultPhdReadTagTest {
    @Test
    public void testGetType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "data");
        assertEquals("type", tag.getType());
    }

    @Test
    public void testGetSource() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "data");
        assertEquals("source", tag.getSource());
    }

    @Test
    public void testGetUngappedRange() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "data");
        assertEquals(new Range(0, 10), tag.getUngappedRange());
    }

    @Test
    public void testGetDate() {
        Date date = new Date();
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), date, "comment", "data");
        assertEquals(date, tag.getDate());
    }

    @Test
    public void testGetComment() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "data");
        assertEquals("comment", tag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "data");
        assertEquals("data", tag.getFreeFormData());
    }

    @Test
    public void testHashCode() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "data");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testEquals() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "data");
        assertEquals(tag1, tag2);
    }
}