package org.jcvi.jillion.assembly.consed.phd;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {
    
    private DefaultPhdReadTag defaultPhdReadTag;
    
    @Before
    public void setUp() {
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 5);
        Date date = new Date();
        String comment = "comment";
        String freeFormData = "freeForm";
        
        defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }
    
    @Test
    public void testGetType() {
        assertEquals("type", defaultPhdReadTag.getType());
    }
    
    @Test
    public void testGetSource() {
        assertEquals("source", defaultPhdReadTag.getSource());
    }
    
    @Test
    public void testGetUngappedRange() {
        assertEquals(new Range(1, 5), defaultPhdReadTag.getUngappedRange());
    }
    
    @Test
    public void testGetDate() {
        assertNotNull(defaultPhdReadTag.getDate());
    }
    
    @Test
    public void testGetComment() {
        assertEquals("comment", defaultPhdReadTag.getComment());
    }
    
    @Test
    public void testGetFreeFormData() {
        assertEquals("freeForm", defaultPhdReadTag.getFreeFormData());
    }
    
    @Test
    public void testHashCode() {
        int expectedHashCode = 31 * "comment".hashCode() +
                                31 * "date".hashCode() +
                                31 * "freeFormData".hashCode() +
                                31 * "source".hashCode() +
                                31 * "type".hashCode() +
                                31 * new Range(1, 5).hashCode();
        
        assertEquals(expectedHashCode, defaultPhdReadTag.hashCode());
    }
    
    @Test
    public void testEquals_sameObject() {
        assertTrue(defaultPhdReadTag.equals(defaultPhdReadTag));
    }
    
    @Test
    public void testEquals_differentObject() {
        assertFalse(defaultPhdReadTag.equals(null));
        assertFalse(defaultPhdReadTag.equals("test"));
        assertFalse(defaultPhdReadTag.equals(new DefaultPhdReadTag("", "", null, null, "", "")));
    }
    
    @Test
    public void testEquals_equalObjects() {
        DefaultPhdReadTag other = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(),
                                                          "comment", "freeForm");
        assertTrue(defaultPhdReadTag.equals(other));
    }
    
    @Test
    public void testEquals_notEqualObjects() {
        DefaultPhdReadTag other = new DefaultPhdReadTag("differentType", "differentSource", new Range(1, 5), new Date(),
                                                          "comment", "freeForm");
        assertFalse(defaultPhdReadTag.equals(other));
    }
}