package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;

import org.jcvi.jillion.core.Range;
import org.junit.Test;

import java.util.Date;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        String type = "type";
        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, null, null, null, null, null);
        assertEquals(type, tag.getType());
    }

    @Test
    public void testGetSource() {
        String source = "source";
        DefaultPhdReadTag tag = new DefaultPhdReadTag(null, source, null, null, null, null);
        assertEquals(source, tag.getSource());
    }

    @Test
    public void testGetUngappedRange() {
        Range range = new Range(1, 10);
        DefaultPhdReadTag tag = new DefaultPhdReadTag(null, null, range, null, null, null);
        assertEquals(range, tag.getUngappedRange());
    }

    @Test
    public void testGetDate() {
        Date date = new Date();
        DefaultPhdReadTag tag = new DefaultPhdReadTag(null, null, null, date, null, null);
        assertEquals(date, tag.getDate());
    }

    @Test
    public void testGetComment() {
        String comment = "comment";
        DefaultPhdReadTag tag = new DefaultPhdReadTag(null, null, null, null, comment, null);
        assertEquals(comment, tag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        String freeFormData = "free form data";
        DefaultPhdReadTag tag = new DefaultPhdReadTag(null, null, null, null, null, freeFormData);
        assertEquals(freeFormData, tag.getFreeFormData());
    }

    @Test
    public void testHashCode() {
        Range range = new Range(1, 10);
        Date date = new Date();
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", range, date, "comment", "data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", range, date, "comment", "data");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testEquals() {
        Range range = new Range(1, 10);
        Date date = new Date();
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", range, date, "comment", "data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", range, date, "comment", "data");
        assertTrue(tag1.equals(tag2));
    }
}