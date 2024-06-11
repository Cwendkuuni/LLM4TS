package org.jcvi.jillion.assembly.consed.phd;

import org.jcvi.jillion.core.*;
import java.util.*;

import org.junit.Assert;
import org.junit.Test;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "testComment";
        String freeFormData = "testFreeFormData";
        
        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        Assert.assertEquals(type, tag.getType());
    }
    
    @Test
    public void testGetSource() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "testComment";
        String freeFormData = "testFreeFormData";
        
        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        Assert.assertEquals(source, tag.getSource());
    }
    
    @Test
    public void testGetUngappedRange() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "testComment";
        String freeFormData = "testFreeFormData";
        
        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        Assert.assertEquals(ungappedRange, tag.getUngappedRange());
    }
    
    @Test
    public void testGetDate() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "testComment";
        String freeFormData = "testFreeFormData";
        
        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        Assert.assertEquals(date, tag.getDate());
    }
    
    @Test
    public void testGetComment() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "testComment";
        String freeFormData = "testFreeFormData";
        
        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        Assert.assertEquals(comment, tag.getComment());
    }
    
    @Test
    public void testGetFreeFormData() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "testComment";
        String freeFormData = "testFreeFormData";
        
        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        Assert.assertEquals(freeFormData, tag.getFreeFormData());
    }
    
    @Test
    public void testHashCode() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "testComment";
        String freeFormData = "testFreeFormData";
        
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        
        Assert.assertEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    @Test
    public void testEquals_WhenEqual() {
        String type = "testType";
        String source = "testSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "testComment";
        String freeFormData = "testFreeFormData";
        
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        
        Assert.assertTrue(tag1.equals(tag2));
    }
    
    @Test
    public void testEquals_WhenNotEqual() {
        String type1 = "testType1";
        String source1 = "testSource1";
        Range ungappedRange1 = new Range(1, 10);
        Date date1 = new Date();
        String comment1 = "testComment1";
        String freeFormData1 = "testFreeFormData1";
        
        String type2 = "testType2";
        String source2 = "testSource2";
        Range ungappedRange2 = new Range(1, 5);
        Date date2 = new Date();
        String comment2 = "testComment2";
        String freeFormData2 = "testFreeFormData2";
        
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type1, source1, ungappedRange1, date1, comment1, freeFormData1);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type2, source2, ungappedRange2, date2, comment2, freeFormData2);
        
        Assert.assertFalse(tag1.equals(tag2));
    }
}