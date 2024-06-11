package org.jcvi.jillion.assembly.consed.phd;

import org.jcvi.jillion.core.*;
import java.util.*;
import org.junit.Test;

public class DefaultPhdReadTagTest {

    //Typical use case
    @Test
    public void testGetType() {
        DefaultPhdReadTag defaultTag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        assertEquals("type", defaultTag.getType());
    }
    
    //Typical use case
    @Test
    public void testGetSource() {
        DefaultPhdReadTag defaultTag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        assertEquals("source", defaultTag.getSource());
    }
    
    //Typical use case
    @Test
    public void testGetUngappedRange() {
        Range range = new Range(0, 10);
        DefaultPhdReadTag defaultTag = new DefaultPhdReadTag("type", "source", range, new Date(), "comment", "freeFormData");
        assertEquals(range, defaultTag.getUngappedRange());
    }
    
    //Typical use case
    @Test
    public void testGetDate() {
        Date date = new Date();
        DefaultPhdReadTag defaultTag = new DefaultPhdReadTag("type", "source", new Range(0, 10), date, "comment", "freeFormData");
        assertEquals(date, defaultTag.getDate());
    }
    
    //Typical use case
    @Test
    public void testGetComment() {
        DefaultPhdReadTag defaultTag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        assertEquals("comment", defaultTag.getComment());
    }
    
    //Typical use case
    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag defaultTag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        assertEquals("freeFormData", defaultTag.getFreeFormData());
    }
    
    //Edge case - empty type
    @Test
    public void testGetTypeEmpty() {
        DefaultPhdReadTag defaultTag = new DefaultPhdReadTag("", "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        assertEquals("", defaultTag.getType());
    }
    
    //Edge case - empty source
    @Test
    public void testGetSourceEmpty() {
        DefaultPhdReadTag defaultTag = new DefaultPhdReadTag("type", "", new Range(0, 10), new Date(), "comment", "freeFormData");
        assertEquals("", defaultTag.getSource());
    }
    
    //Edge case - negative range
    @Test(expected = IllegalArgumentException.class)
    public void testGetUngappedRangeNegative() {
        Range range = new Range(-10, 0);
        DefaultPhdReadTag defaultTag = new DefaultPhdReadTag("type", "source", range, new Date(), "comment", "freeFormData");
    }
    
    //Edge case - null date
    @Test
    public void testGetDateNull() {
        DefaultPhdReadTag defaultTag = new DefaultPhdReadTag("type", "source", new Range(0, 10), null, "comment", "freeFormData");
        assertNull(defaultTag.getDate());
    }
    
    //Edge case - empty comment
    @Test
    public void testGetCommentEmpty() {
        DefaultPhdReadTag defaultTag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "", "freeFormData");
        assertEquals("", defaultTag.getComment());
    }
    
    //Edge case - empty free form data
    @Test
    public void testGetFreeFormDataEmpty() {
        DefaultPhdReadTag defaultTag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "");
        assertEquals("", defaultTag.getFreeFormData());
    }
    
    //Error scenario - null type
    @Test(expected = NullPointerException.class)
    public void testGetTypeNull() {
        DefaultPhdReadTag defaultTag = new DefaultPhdReadTag(null, "source", new Range(0, 10), new Date(), "comment", "freeFormData");
    }
    
    //Error scenario - null source
    @Test(expected = NullPointerException.class)
    public void testGetSourceNull() {
        DefaultPhdReadTag defaultTag = new DefaultPhdReadTag("type", null, new Range(0, 10), new Date(), "comment", "freeFormData");
    }
    
    //Error scenario - null ungapped range
    @Test(expected = NullPointerException.class)
    public void testGetUngappedRangeNull() {
        DefaultPhdReadTag defaultTag = new DefaultPhdReadTag("type", "source", null, new Date(), "comment", "freeFormData");
    }
}