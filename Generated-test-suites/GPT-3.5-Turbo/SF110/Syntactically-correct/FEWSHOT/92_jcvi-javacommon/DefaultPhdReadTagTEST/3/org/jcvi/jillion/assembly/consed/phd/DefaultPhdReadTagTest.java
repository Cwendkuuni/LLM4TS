package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jcvi.jillion.core.Range;
import org.junit.Test;

import java.util.Date;

public class DefaultPhdReadTagTest {
    @Test
    public void testConstructorAndGetters() {
        Date date = new Date();
        Range range = new Range(1, 10);
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", range, date, "comment", "freeFormData");

        assertEquals("type", tag.getType());
        assertEquals("source", tag.getSource());
        assertEquals(range, tag.getUngappedRange());
        assertEquals(date, tag.getDate());
        assertEquals("comment", tag.getComment());
        assertEquals("freeFormData", tag.getFreeFormData());
    }

    @Test
    public void testHashCodeAndEquals() {
        Date date1 = new Date();
        Date date2 = new Date();
        Range range1 = new Range(1, 10);
        Range range2 = new Range(5, 15);
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", range1, date1, "comment", "freeFormData");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", range1, date1, "comment", "freeFormData");
        DefaultPhdReadTag tag3 = new DefaultPhdReadTag("type", "source2", range1, date1, "comment", "freeFormData");
        DefaultPhdReadTag tag4 = new DefaultPhdReadTag("type", "source", range2, date1, "comment", "freeFormData");
        DefaultPhdReadTag tag5 = new DefaultPhdReadTag("type", "source", range1, date2, "comment", "freeFormData");
        DefaultPhdReadTag tag6 = new DefaultPhdReadTag("type", "source", range1, date1, "comment2", "freeFormData");
        DefaultPhdReadTag tag7 = new DefaultPhdReadTag("type", "source", range1, date1, "comment", "freeFormData2");

        assertEquals(tag1.hashCode(), tag2.hashCode());
        assertEquals(tag1, tag2);

        assertNotEquals(tag1.hashCode(), tag3.hashCode());
        assertNotEquals(tag1, tag3);

        assertNotEquals(tag1.hashCode(), tag4.hashCode());
        assertNotEquals(tag1, tag4);

        assertNotEquals(tag1.hashCode(), tag5.hashCode());
        assertNotEquals(tag1, tag5);

        assertNotEquals(tag1.hashCode(), tag6.hashCode());
        assertNotEquals(tag1, tag6);

        assertNotEquals(tag1.hashCode(), tag7.hashCode());
        assertNotEquals(tag1, tag7);
    }
}