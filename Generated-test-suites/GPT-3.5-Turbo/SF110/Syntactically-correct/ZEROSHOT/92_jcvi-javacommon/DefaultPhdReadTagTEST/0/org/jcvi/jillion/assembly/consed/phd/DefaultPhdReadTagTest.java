package org.jcvi.jillion.assembly.consed.phd;

import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

    // Testing getType() method

    @Test
    public void testGetType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(), new Date(), "comment", "freeFormData");
        String result = tag.getType();
        assertEquals("type", result);
    }

    // Testing getSource() method

    @Test
    public void testGetSource() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(), new Date(), "comment", "freeFormData");
        String result = tag.getSource();
        assertEquals("source", result);
    }

    // Testing getUngappedRange() method

    @Test
    public void testGetUngappedRange() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(), new Date(), "comment", "freeFormData");
        Range result = tag.getUngappedRange();
        assertNotNull(result);
    }

    // Testing getDate() method

    @Test
    public void testGetDate() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(), new Date(), "comment", "freeFormData");
        Date result = tag.getDate();
        assertNotNull(result);
    }

    // Testing getComment() method

    @Test
    public void testGetComment() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(), new Date(), "comment", "freeFormData");
        String result = tag.getComment();
        assertEquals("comment", result);
    }

    // Testing getFreeFormData() method

    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(), new Date(), "comment", "freeFormData");
        String result = tag.getFreeFormData();
        assertEquals("freeFormData", result);
    }

    // Testing hashCode() method

    @Test
    public void testHashCode() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(), new Date(), "comment", "freeFormData");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", new Range(), new Date(), "comment", "freeFormData");
        int hashCode1 = tag1.hashCode();
        int hashCode2 = tag2.hashCode();
        assertEquals(hashCode1, hashCode2);
    }

    // Testing equals() method

    @Test
    public void testEquals() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(), new Date(), "comment", "freeFormData");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", new Range(), new Date(), "comment", "freeFormData");
        boolean result = tag1.equals(tag2);
        assertTrue(result);
    }
}