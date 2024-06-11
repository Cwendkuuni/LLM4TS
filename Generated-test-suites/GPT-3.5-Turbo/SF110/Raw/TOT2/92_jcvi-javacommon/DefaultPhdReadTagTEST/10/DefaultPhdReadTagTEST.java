package org.jcvi.jillion.assembly.consed.phd;

import static org.junit.Assert.*;
import org.junit.Test;

public class DefaultPhdReadTagTest {
    
    // Test case for getType method
    @Test
    public void testGetType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "data");
        assertEquals("type", tag.getType());
    }
    
    // Test case for getSource method
    @Test
    public void testGetSource() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "data");
        assertEquals("source", tag.getSource());
    }
    
    // Test case for getUngappedRange method
    @Test
    public void testGetUngappedRange() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "data");
        assertEquals(new Range(1, 5), tag.getUngappedRange());
    }
    
    // Test case for getDate method
    @Test
    public void testGetDate() {
        Date currentDate = new Date();
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 5), currentDate, "comment", "data");
        assertEquals(currentDate, tag.getDate());
    }
    
    // Test case for getComment method
    @Test
    public void testGetComment() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "data");
        assertEquals("comment", tag.getComment());
    }
    
    // Test case for getFreeFormData method
    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "data");
        assertEquals("data", tag.getFreeFormData());
    }

    // Test case for hashCode method
    // Additional test cases can be added to cover different scenarios
    @Test
    public void testHashCode() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "data");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    // Test case for equals method
    // Additional test cases should be added to cover different scenarios
    @Test
    public void testEquals() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "data");
        assertTrue(tag1.equals(tag2));
    }

}