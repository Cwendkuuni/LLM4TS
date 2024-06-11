package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Date;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "freeFormData");
        assertEquals("type", defaultPhdReadTag.getType());
    }

    @Test
    public void testGetSource() {
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "freeFormData");
        assertEquals("source", defaultPhdReadTag.getSource());
    }

    @Test
    public void testGetUngappedRange() {
        Range range = new Range(1, 5);
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("type", "source", range, new Date(), "comment", "freeFormData");
        assertEquals(range, defaultPhdReadTag.getUngappedRange());
    }

    @Test
    public void testGetDate() {
        Date date = new Date();
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("type", "source", new Range(1, 5), date, "comment", "freeFormData");
        assertEquals(date, defaultPhdReadTag.getDate());
    }

    @Test
    public void testGetComment() {
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "freeFormData");
        assertEquals("comment", defaultPhdReadTag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "freeFormData");
        assertEquals("freeFormData", defaultPhdReadTag.getFreeFormData());
    }

    @Test
    public void testHashCode() {
        DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "freeFormData");
        DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "freeFormData");
        assertEquals(defaultPhdReadTag1.hashCode(), defaultPhdReadTag2.hashCode());
    }

    @Test
    public void testEquals() {
        DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "freeFormData");
        DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "freeFormData");
        assertTrue(defaultPhdReadTag1.equals(defaultPhdReadTag2));
    }
}