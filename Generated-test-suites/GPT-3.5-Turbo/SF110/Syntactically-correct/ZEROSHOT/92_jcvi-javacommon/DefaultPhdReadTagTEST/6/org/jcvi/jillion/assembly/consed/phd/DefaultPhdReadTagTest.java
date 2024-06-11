package org.jcvi.jillion.assembly.consed.phd;

import org.jcvi.jillion.core.*;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

    private DefaultPhdReadTag tag;
    
    @Before
    public void setUp() {
        String type = "Type";
        String source = "Source";
        Range ungappedRange = new Range(0, 10);
        Date date = new Date();
        String comment = "Comment";
        String freeFormData = "Free Form Data";
        
        tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }
    
    @Test
    public void testGetType() {
        String expected = "Type";
        String actual = tag.getType();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetSource() {
        String expected = "Source";
        String actual = tag.getSource();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetUngappedRange() {
        Range expected = new Range(0, 10);
        Range actual = tag.getUngappedRange();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetDate() {
        Date expected = new Date();
        Date actual = tag.getDate();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetComment() {
        String expected = "Comment";
        String actual = tag.getComment();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetFreeFormData() {
        String expected = "Free Form Data";
        String actual = tag.getFreeFormData();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHashCode() {
        int expected = 1234; // replace with the expected hash code value
        int actual = tag.hashCode();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals_SameObject() {
        boolean expected = true;
        boolean actual = tag.equals(tag);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals_NullObject() {
        boolean expected = false;
        boolean actual = tag.equals(null);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals_DifferentClass() {
        boolean expected = false;
        boolean actual = tag.equals("Different Class");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals_CommentNull() {
        DefaultPhdReadTag other = new DefaultPhdReadTag("Type", "Source", new Range(0, 10), new Date(), null, "Free Form Data");
        boolean expected = false;
        boolean actual = tag.equals(other);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals_CommentDifferent() {
        DefaultPhdReadTag other = new DefaultPhdReadTag("Type", "Source", new Range(0, 10), new Date(), "Different Comment", "Free Form Data");
        boolean expected = false;
        boolean actual = tag.equals(other);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals_DateNull() {
        DefaultPhdReadTag other = new DefaultPhdReadTag("Type", "Source", new Range(0, 10), null, "Comment", "Free Form Data");
        boolean expected = false;
        boolean actual = tag.equals(other);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals_DateDifferent() {
        DefaultPhdReadTag other = new DefaultPhdReadTag("Type", "Source", new Range(0, 10), new Date(2000, 1, 1), "Comment", "Free Form Data");
        boolean expected = false;
        boolean actual = tag.equals(other);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals_FreeFormDataNull() {
        DefaultPhdReadTag other = new DefaultPhdReadTag("Type", "Source", new Range(0, 10), new Date(), "Comment", null);
        boolean expected = false;
        boolean actual = tag.equals(other);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals_FreeFormDataDifferent() {
        DefaultPhdReadTag other = new DefaultPhdReadTag("Type", "Source", new Range(0, 10), new Date(), "Comment", "Different Free Form Data");
        boolean expected = false;
        boolean actual = tag.equals(other);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals_SourceNull() {
        DefaultPhdReadTag other = new DefaultPhdReadTag("Type", null, new Range(0, 10), new Date(), "Comment", "Free Form Data");
        boolean expected = false;
        boolean actual = tag.equals(other);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals_SourceDifferent() {
        DefaultPhdReadTag other = new DefaultPhdReadTag("Type", "Different Source", new Range(0, 10), new Date(), "Comment", "Free Form Data");
        boolean expected = false;
        boolean actual = tag.equals(other);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals_TypeNull() {
        DefaultPhdReadTag other = new DefaultPhdReadTag(null, "Source", new Range(0, 10), new Date(), "Comment", "Free Form Data");
        boolean expected = false;
        boolean actual = tag.equals(other);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals_TypeDifferent() {
        DefaultPhdReadTag other = new DefaultPhdReadTag("Different Type", "Source", new Range(0, 10), new Date(), "Comment", "Free Form Data");
        boolean expected = false;
        boolean actual = tag.equals(other);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals_UngappedRangeNull() {
        DefaultPhdReadTag other = new DefaultPhdReadTag("Type", "Source", null, new Date(), "Comment", "Free Form Data");
        boolean expected = false;
        boolean actual = tag.equals(other);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals_UngappedRangeDifferent() {
        DefaultPhdReadTag other = new DefaultPhdReadTag("Type", "Source", new Range(5, 15), new Date(), "Comment", "Free Form Data");
        boolean expected = false;
        boolean actual = tag.equals(other);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals_AllFieldsNull() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(null, null, null, null, null, null);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(null, null, null, null, null, null);
        boolean expected = true;
        boolean actual = tag1.equals(tag2);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals_AllFieldsSame() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("Type", "Source", new Range(0, 10), new Date(), "Comment", "Free Form Data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("Type", "Source", new Range(0, 10), new Date(), "Comment", "Free Form Data");
        boolean expected = true;
        boolean actual = tag1.equals(tag2);
        assertEquals(expected, actual);
    }
    
}