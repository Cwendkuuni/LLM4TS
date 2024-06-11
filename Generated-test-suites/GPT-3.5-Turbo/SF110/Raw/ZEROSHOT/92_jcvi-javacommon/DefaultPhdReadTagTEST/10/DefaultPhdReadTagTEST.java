package org.jcvi.jillion.assembly.consed.phd;

import org.junit.Test;
import org.junit.Assert;

import org.jcvi.jillion.core.*;
import java.util.*;

public class DefaultPhdReadTagTest {
    
    @Test
    public void getTypeTest() {
        String type = "TestType";
        String source = "TestSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "TestComment";
        String freeFormData = "TestFreeFormData";
        
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        
        String actualType = defaultPhdReadTag.getType();
        
        Assert.assertEquals(type, actualType);
    }
    
    @Test
    public void getSourceTest() {
        String type = "TestType";
        String source = "TestSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "TestComment";
        String freeFormData = "TestFreeFormData";
        
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        
        String actualSource = defaultPhdReadTag.getSource();
        
        Assert.assertEquals(source, actualSource);
    }
    
    @Test
    public void getUngappedRangeTest() {
        String type = "TestType";
        String source = "TestSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "TestComment";
        String freeFormData = "TestFreeFormData";
        
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        
        Range actualUngappedRange = defaultPhdReadTag.getUngappedRange();
        
        Assert.assertEquals(ungappedRange, actualUngappedRange);
    }
    
    @Test
    public void getDateTest() {
        String type = "TestType";
        String source = "TestSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "TestComment";
        String freeFormData = "TestFreeFormData";
        
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        
        Date actualDate = defaultPhdReadTag.getDate();
        
        Assert.assertEquals(date, actualDate);
    }
    
    @Test
    public void getCommentTest() {
        String type = "TestType";
        String source = "TestSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "TestComment";
        String freeFormData = "TestFreeFormData";
        
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        
        String actualComment = defaultPhdReadTag.getComment();
        
        Assert.assertEquals(comment, actualComment);
    }
    
    @Test
    public void getFreeFormDataTest() {
        String type = "TestType";
        String source = "TestSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "TestComment";
        String freeFormData = "TestFreeFormData";
        
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        
        String actualFreeFormData = defaultPhdReadTag.getFreeFormData();
        
        Assert.assertEquals(freeFormData, actualFreeFormData);
    }
    
    @Test
    public void hashCodeTest() {
        String type = "TestType";
        String source = "TestSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "TestComment";
        String freeFormData = "TestFreeFormData";
        
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        
        int hashCode = defaultPhdReadTag.hashCode();
        
        Assert.assertNotNull(hashCode);
    }
    
    @Test
    public void equalsTest() {
        String type = "TestType";
        String source = "TestSource";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "TestComment";
        String freeFormData = "TestFreeFormData";
        
        DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        
        boolean equals = defaultPhdReadTag1.equals(defaultPhdReadTag2);
        
        Assert.assertEquals(true, equals);
    }
    
}