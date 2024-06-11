package org.jcvi.jillion.assembly.consed.phd;

import org.jcvi.jillion.core.*;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 2), new Date(), "comment", "data");
        assertEquals("type", tag.getType());
    }

    @Test
    public void testGetSource() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 2), new Date(), "comment", "data");
        assertEquals("source", tag.getSource());
    }

    @Test
    public void testGetUngappedRange() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 2), new Date(), "comment", "data");
        assertEquals(new Range(1, 2), tag.getUngappedRange());
    }

    @Test
    public void testGetDate() {
        Date date = new Date();
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 2), date, "comment", "data");
        assertEquals(date, tag.getDate());
    }

    @Test
    public void testGetComment() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 2), new Date(), "comment", "data");
        assertEquals("comment", tag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 2), new Date(), "comment", "data");
        assertEquals("data", tag.getFreeFormData());
    }

    @Test
    public void testHashCode() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(1, 2), new Date(), "comment", "data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", new Range(1, 2), new Date(), "comment", "data");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testEquals() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(1, 2), new Date(), "comment", "data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", new Range(1, 2), new Date(), "comment", "data");
        assertTrue(tag1.equals(tag2));
    }

    @Test
    public void testEquals_sameObject() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 2), new Date(), "comment", "data");
        assertTrue(tag.equals(tag));
    }

    @Test
    public void testEquals_differentObjectType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 2), new Date(), "comment", "data");
        assertFalse(tag.equals(new String("type")));
    }

    @Test
    public void testEquals_differentType() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(1, 2), new Date(), "comment", "data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type2", "source", new Range(1, 2), new Date(), "comment", "data");
        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testEquals_differentSource() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(1, 2), new Date(), "comment", "data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source2", new Range(1, 2), new Date(), "comment", "data");
        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testEquals_differentUngappedRange() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(1, 2), new Date(), "comment", "data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", new Range(2, 3), new Date(), "comment", "data");
        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testEquals_differentDate() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(1, 2), new Date(123), "comment", "data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", new Range(1, 2), new Date(456), "comment", "data");
        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testEquals_differentComment() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(1, 2), new Date(), "comment", "data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", new Range(1, 2), new Date(), "comment2", "data");
        assertFalse(tag1.equals(tag2));
    }

    @Test
    public void testEquals_differentFreeFormData() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(1, 2), new Date(), "comment", "data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", new Range(1, 2), new Date(), "comment", "data2");
        assertFalse(tag1.equals(tag2));
    }
}