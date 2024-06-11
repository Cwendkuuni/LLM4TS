
package org.jcvi.jillion.assembly.consed.phd;

import org.jcvi.jillion.core.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class DefaultPhdReadTagTest {

    private DefaultPhdReadTag tag1;
    private DefaultPhdReadTag tag2;
    private DefaultPhdReadTag tag3;

    @Before
    public void setup() {
        Range range1 = new Range(1, 10);
        Range range2 = new Range(11, 20);
        Date date1 = new Date(123456789);
        Date date2 = new Date(987654321);
        tag1 = new DefaultPhdReadTag("type1", "source1", range1, date1, "comment1", "data1");
        tag2 = new DefaultPhdReadTag("type2", "source2", range2, date2, "comment2", "data2");
        tag3 = new DefaultPhdReadTag(null, null, null, null, null, null);
    }
    
    @Test
    public void testGetType() {
        assertEquals("type1", tag1.getType());
        assertEquals("type2", tag2.getType());
        assertNull(tag3.getType());
    }
    
    @Test
    public void testGetSource() {
        assertEquals("source1", tag1.getSource());
        assertEquals("source2", tag2.getSource());
        assertNull(tag3.getSource());
    }
    
    @Test
    public void testGetUngappedRange() {
        assertEquals(new Range(1, 10), tag1.getUngappedRange());
        assertEquals(new Range(11, 20), tag2.getUngappedRange());
        assertNull(tag3.getUngappedRange());
    }
    
    @Test
    public void testGetDate() {
        assertEquals(new Date(123456789), tag1.getDate());
        assertEquals(new Date(987654321), tag2.getDate());
        assertNull(tag3.getDate());
    }
    
    @Test
    public void testGetComment() {
        assertEquals("comment1", tag1.getComment());
        assertEquals("comment2", tag2.getComment());
        assertNull(tag3.getComment());
    }
    
    @Test
    public void testGetFreeFormData() {
        assertEquals("data1", tag1.getFreeFormData());
        assertEquals("data2", tag2.getFreeFormData());
        assertNull(tag3.getFreeFormData());
    }
    
    @Test
    public void testHashCode() {
        assertNotEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    @Test
    public void testEquals() {
        assertNotEquals(tag1, tag2);
        assertEquals(tag1, tag1);
        assertNotNull(tag1);
        assertNotEquals(tag1, null);
        assertNotEquals(tag1, "String");
    }
}
