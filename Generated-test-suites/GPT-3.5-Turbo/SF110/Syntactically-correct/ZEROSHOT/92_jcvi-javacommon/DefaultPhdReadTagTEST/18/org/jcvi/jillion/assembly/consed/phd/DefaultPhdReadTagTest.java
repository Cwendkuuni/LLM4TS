package org.jcvi.jillion.assembly.consed.phd;

import org.jcvi.jillion.core.*;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        assertEquals("type", tag.getType());
    }

    @Test
    public void testGetSource() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        assertEquals("source", tag.getSource());
    }

    @Test
    public void testGetUngappedRange() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        assertEquals(new Range(1, 10), tag.getUngappedRange());
    }

    @Test
    public void testGetDate() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        assertNotNull(tag.getDate());
    }

    @Test
    public void testGetComment() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        assertEquals("comment", tag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        assertEquals("freeFormData", tag.getFreeFormData());
    }

    @Test
    public void testHashCode() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testEquals() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        assertEquals(tag1, tag2);
    }
}