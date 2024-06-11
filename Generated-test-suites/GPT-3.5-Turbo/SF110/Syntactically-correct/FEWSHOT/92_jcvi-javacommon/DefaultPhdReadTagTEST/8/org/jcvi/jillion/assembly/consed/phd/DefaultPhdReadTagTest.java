package org.jcvi.jillion.assembly.consed.phd;

import org.jcvi.jillion.core.*;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        final DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        assertEquals("type", tag.getType());
    }

    @Test
    public void testGetSource() {
        final DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        assertEquals("source", tag.getSource());
    }

    @Test
    public void testGetUngappedRange() {
        final DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        assertNotNull(tag.getUngappedRange());
    }

    @Test
    public void testGetDate() {
        final DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        assertNotNull(tag.getDate());
    }

    @Test
    public void testGetComment() {
        final DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        assertEquals("comment", tag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        final DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        assertEquals("freeFormData", tag.getFreeFormData());
    }

    @Test
    public void testHashCode() {
        final DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        final DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testEquals() {
        final DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        final DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        assertEquals(tag1, tag2);
    }
}