package org.jcvi.jillion.assembly.consed.phd;

import org.jcvi.jillion.core.*;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "free form data");
        assertEquals("type", tag.getType());
    }

    @Test
    public void testGetSource() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "free form data");
        assertEquals("source", tag.getSource());
    }

    @Test
    public void testGetUngappedRange() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "free form data");
        assertEquals(new Range(1, 10), tag.getUngappedRange());
    }

    @Test
    public void testGetDate() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "free form data");
        assertNotNull(tag.getDate());
    }

    @Test
    public void testGetComment() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "free form data");
        assertEquals("comment", tag.getComment());
    }

    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "free form data");
        assertEquals("free form data", tag.getFreeFormData());
    }

    @Test
    public void testHashCode() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "free form data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "free form data");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void testEquals() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "free form data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "free form data");
        assertTrue(tag1.equals(tag2));
    }
    
}