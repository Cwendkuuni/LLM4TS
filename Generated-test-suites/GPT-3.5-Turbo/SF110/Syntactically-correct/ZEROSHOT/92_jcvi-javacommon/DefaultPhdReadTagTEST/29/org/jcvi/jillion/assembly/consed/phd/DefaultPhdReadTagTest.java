package org.jcvi.jillion.assembly.consed.phd;

import org.jcvi.jillion.core.*;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        PhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "data");
        String expected = "type";
        String actual = tag.getType();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetSource() {
        PhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "data");
        String expected = "source";
        String actual = tag.getSource();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetUngappedRange() {
        PhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "data");
        Range expected = new Range(0, 10);
        Range actual = tag.getUngappedRange();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDate() {
        Date now = new Date();
        PhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), now, "comment", "data");
        Date expected = now;
        Date actual = tag.getDate();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetComment() {
        PhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "data");
        String expected = "comment";
        String actual = tag.getComment();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetFreeFormData() {
        PhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "data");
        String expected = "data";
        String actual = tag.getFreeFormData();
        assertEquals(expected, actual);
    }

    @Test
    public void testHashCode() {
        PhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "data");
        PhdReadTag tag2 = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "data");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testEquals() {
        PhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "data");
        PhdReadTag tag2 = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "data");
        assertTrue(tag1.equals(tag2));
    }

}