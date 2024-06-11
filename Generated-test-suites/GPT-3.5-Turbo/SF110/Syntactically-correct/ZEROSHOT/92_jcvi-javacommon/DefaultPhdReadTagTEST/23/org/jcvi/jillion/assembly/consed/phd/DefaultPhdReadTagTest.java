package org.jcvi.jillion.assembly.consed.phd;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "data");
        
        String expectedType = "type";
        String actualType = tag.getType();
        
        assertEquals(expectedType, actualType);
    }
    
    @Test
    public void testGetSource() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "data");
        
        String expectedSource = "source";
        String actualSource = tag.getSource();
        
        assertEquals(expectedSource, actualSource);
    }
    
    @Test
    public void testGetUngappedRange() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "data");
        
        Range expectedRange = new Range(0, 10);
        Range actualRange = tag.getUngappedRange();
        
        assertEquals(expectedRange, actualRange);
    }
    
    @Test
    public void testGetDate() {
        Date date = new Date();
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), date, "comment", "data");
        
        Date expectedDate = date;
        Date actualDate = tag.getDate();
        
        assertEquals(expectedDate, actualDate);
    }
    
    @Test
    public void testGetComment() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "data");
        
        String expectedComment = "comment";
        String actualComment = tag.getComment();
        
        assertEquals(expectedComment, actualComment);
    }
    
    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "data");
        
        String expectedData = "data";
        String actualData = tag.getFreeFormData();
        
        assertEquals(expectedData, actualData);
    }
    
    @Test
    public void testHashCode() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "data");
        
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    @Test
    public void testEquals() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "data");
        
        assertTrue(tag1.equals(tag2));
    }
}