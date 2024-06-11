package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.jcvi.jillion.core.*;
import java.util.*;

import org.junit.Test;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetters() {
        Date date = new Date();
        Range range = new Range(1, 10);
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", range, date, "comment", "form data");

        assertEquals("type", tag.getType());
        assertEquals("source", tag.getSource());
        assertEquals(range, tag.getUngappedRange());
        assertEquals(date, tag.getDate());
        assertEquals("comment", tag.getComment());
        assertEquals("form data", tag.getFreeFormData());
    }

    @Test
    public void testHashCode() {
        Date date = new Date();
        Range range = new Range(1, 10);
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", range, date, "comment", "form data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", range, date, "comment", "form data");

        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testEquals() {
        Date date = new Date();
        Range range = new Range(1, 10);
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", range, date, "comment", "form data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", range, date, "comment", "form data");

        assertEquals(tag1, tag2);
    }

}