import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Date;

public class DefaultPhdReadTagTest {
    
    @Test
    public void testGetType() {
        // Test for typical use case 
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "data1");
        assertEquals("type1", tag.getType());
        
        // Test for null type 
        tag = new DefaultPhdReadTag(null, "source1", new Range(1, 10), new Date(), "comment1", "data1");
        assertNull(tag.getType());
    }
    
    @Test
    public void testGetSource() {
        // Test for typical use case
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "data1");
        assertEquals("source1", tag.getSource());
        
        // Test for null source
        tag = new DefaultPhdReadTag("type1", null, new Range(1, 10), new Date(), "comment1", "data1");
        assertNull(tag.getSource());
    }
    
    @Test
    public void testGetUngappedRange() {
        // Test for typical use case
        Range range = new Range(1, 10);
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", range, new Date(), "comment1", "data1");
        assertEquals(range, tag.getUngappedRange());
        
        // Test for null ungappedRange
        tag = new DefaultPhdReadTag("type1", "source1", null, new Date(), "comment1", "data1");
        assertNull(tag.getUngappedRange());
    }
    
    @Test
    public void testGetDate() {
        Date date = new Date();
        
        // Test for typical use case
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), date, "comment1", "data1");
        assertEquals(date, tag.getDate());
        
        // Test for null date
        tag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), null, "comment1", "data1");
        assertNull(tag.getDate());
    }
    
    @Test
    public void testGetComment() {
        // Test for typical use case
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "data1");
        assertEquals("comment1", tag.getComment());
        
        // Test for null comment
        tag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), null, "data1");
        assertNull(tag.getComment());
    }
    
    @Test
    public void testGetFreeFormData() {
        // Test for typical use case
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "data1");
        assertEquals("data1", tag.getFreeFormData());
        
        // Test for null freeFormData
        tag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", null);
        assertNull(tag.getFreeFormData());
    }
    
    @Test
    public void testEquals() {
        Range range1 = new Range(1, 10);
        Range range2 = new Range(10, 20);
        Date date1 = new Date(2021, 1, 1);
        Date date2 = new Date(2022, 1, 1);
        
        // Test for equal objects
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", range1, date1, "comment1", "data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source1", range1, date1, "comment1", "data1");
        assertTrue(tag1.equals(tag2));
        
        // Test for different type
        tag2 = new DefaultPhdReadTag("type2", "source1", range1, date1, "comment1", "data1");
        assertFalse(tag1.equals(tag2));
        
        // Test for different source
        tag2 = new DefaultPhdReadTag("type1", "source2", range1, date1, "comment1", "data1");
        assertFalse(tag1.equals(tag2));
        
        // Test for different ungappedRange
        tag2 = new DefaultPhdReadTag("type1", "source1", range2, date1, "comment1", "data1");
        assertFalse(tag1.equals(tag2));
        
        // Test for different date
        tag2 = new DefaultPhdReadTag("type1", "source1", range1, date2, "comment1", "data1");
        assertFalse(tag1.equals(tag2));
        
        // Test for different comment
        tag2 = new DefaultPhdReadTag("type1", "source1", range1, date1, "comment2", "data1");
        assertFalse(tag1.equals(tag2));
        
        // Test for different freeFormData
        tag2 = new DefaultPhdReadTag("type1", "source1", range1, date1, "comment1", "data2");
        assertFalse(tag1.equals(tag2));
    }
    
    @Test
    public void testHashCode() {
        Range range1 = new Range(1, 10);
        Range range2 = new Range(10, 20);
        Date date1 = new Date(2021, 1, 1);
        Date date2 = new Date(2022, 1, 1);
        
        // Test for equal objects
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", range1, date1, "comment1", "data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source1", range1, date1, "comment1", "data1");
        assertEquals(tag1.hashCode(), tag2.hashCode());
        
        // Test for different type
        tag2 = new DefaultPhdReadTag("type2", "source1", range1, date1, "comment1", "data1");
        assertNotEquals(tag1.hashCode(), tag2.hashCode());
        
        // Test for different source
        tag2 = new DefaultPhdReadTag("type1", "source2", range1, date1, "comment1", "data1");
        assertNotEquals(tag1.hashCode(), tag2.hashCode());
        
        // Test for different ungappedRange
        tag2 = new DefaultPhdReadTag("type1", "source1", range2, date1, "comment1", "data1");
        assertNotEquals(tag1.hashCode(), tag2.hashCode());
        
        // Test for different date
        tag2 = new DefaultPhdReadTag("type1", "source1", range1, date2, "comment1", "data1");
        assertNotEquals(tag1.hashCode(), tag2.hashCode());
        
        // Test for different comment
        tag2 = new DefaultPhdReadTag("type1", "source1", range1, date1, "comment2", "data1");
        assertNotEquals(tag1.hashCode(), tag2.hashCode());
        
        // Test for different freeFormData
        tag2 = new DefaultPhdReadTag("type1", "source1", range1, date1, "comment1", "data2");
        assertNotEquals(tag1.hashCode(), tag2.hashCode());
    }
}