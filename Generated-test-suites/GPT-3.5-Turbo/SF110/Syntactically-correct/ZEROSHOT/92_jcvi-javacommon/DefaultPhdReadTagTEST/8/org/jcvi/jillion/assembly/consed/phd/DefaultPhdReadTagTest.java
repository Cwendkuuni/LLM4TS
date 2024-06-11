package org.jcvi.jillion.assembly.consed.phd;

import org.jcvi.jillion.core.*;
import org.junit.Test;
import java.util.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        // Test case 1
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 2);
        Date date = new Date();
        String comment = "comment";
        String freeFormData = "free form data";
        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        String result = tag.getType();
        String expected = "type";
        assertEquals(expected, result);
        
        // Test case 2 - Add more test cases
        
        
    }
    
    @Test
    public void testGetSource() {
        // Test case 1
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 2);
        Date date = new Date();
        String comment = "comment";
        String freeFormData = "free form data";
        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        String result = tag.getSource();
        String expected = "source";
        assertEquals(expected, result);
        
        // Test case 2 - Add more test cases
        
        
    }
    
    @Test
    public void testGetUngappedRange() {
        // Test case 1
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 2);
        Date date = new Date();
        String comment = "comment";
        String freeFormData = "free form data";
        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        Range result = tag.getUngappedRange();
        Range expected = new Range(1,2);
        assertEquals(expected, result);
        
        // Test case 2 - Add more test cases
        
        
    }
    
    @Test
    public void testGetDate() {
        // Test case 1
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 2);
        Date date = new Date();
        String comment = "comment";
        String freeFormData = "free form data";
        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        Date result = tag.getDate();
        Date expected = new Date();
        assertEquals(expected, result);
        
        // Test case 2 - Add more test cases
        
        
    }
    
    @Test
    public void testGetComment() {
        // Test case 1
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 2);
        Date date = new Date();
        String comment = "comment";
        String freeFormData = "free form data";
        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        String result = tag.getComment();
        String expected = "comment";
        assertEquals(expected, result);
        
        // Test case 2 - Add more test cases
        
        
    }
    
    @Test
    public void testGetFreeFormData() {
        // Test case 1
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 2);
        Date date = new Date();
        String comment = "comment";
        String freeFormData = "free form data";
        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        String result = tag.getFreeFormData();
        String expected = "free form data";
        assertEquals(expected, result);
        
        // Test case 2 - Add more test cases
        
        
    }
    
    @Test
    public void testHashCode() {
        // Test case 1
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 2);
        Date date = new Date();
        String comment = "comment";
        String freeFormData = "free form data";
        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        int result = tag.hashCode();
        int expected = 0;
        assertEquals(expected, result);
        
        // Test case 2 - Add more test cases
        
        
    }
    
    @Test
    public void testEquals() {
        // Test case 1
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 2);
        Date date = new Date();
        String comment = "comment";
        String freeFormData = "free form data";
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        boolean result = tag1.equals(tag2);
        boolean expected = true;
        assertEquals(expected, result);
        
        // Test case 2 - Add more test cases
        
        
    }
    
}