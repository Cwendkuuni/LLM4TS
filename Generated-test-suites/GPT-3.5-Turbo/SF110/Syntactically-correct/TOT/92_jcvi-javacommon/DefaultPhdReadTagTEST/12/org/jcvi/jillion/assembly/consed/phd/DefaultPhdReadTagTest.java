import org.junit.Test;
import static org.junit.Assert.*;

import org.jcvi.jillion.core.*;
import java.util.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        // typical use case
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", new Range(0, 10), new Date(), "comment1", "data1");
        assertEquals("type1", tag.getType());
        
        // edge case: type is null
        DefaultPhdReadTag nullTypeTag = new DefaultPhdReadTag(null, "source2", new Range(0, 10), new Date(), "comment2", "data2");
        assertNull(nullTypeTag.getType());
    }
    
    @Test
    public void testGetSource() {
        // typical use case
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", new Range(0, 10), new Date(), "comment1", "data1");
        assertEquals("source1", tag.getSource());
        
        // edge case: source is null
        DefaultPhdReadTag nullSourceTag = new DefaultPhdReadTag("type2", null, new Range(0, 10), new Date(), "comment2", "data2");
        assertNull(nullSourceTag.getSource());
    }
    
    @Test
    public void testGetUngappedRange() {
        // typical use case
        Range range = new Range(0, 10);
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", range, new Date(), "comment1", "data1");
        assertEquals(range, tag.getUngappedRange());
        
        // edge case: range is null
        DefaultPhdReadTag nullRangeTag = new DefaultPhdReadTag("type2", "source2", null, new Date(), "comment2", "data2");
        assertNull(nullRangeTag.getUngappedRange());
    }
    
    @Test
    public void testGetDate() {
        // typical use case
        Date date = new Date();
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", new Range(0, 10), date, "comment1", "data1");
        assertEquals(date, tag.getDate());
        
        // edge case: date is null
        DefaultPhdReadTag nullDateTag = new DefaultPhdReadTag("type2", "source2", new Range(0, 10), null, "comment2", "data2");
        assertNull(nullDateTag.getDate());
    }
    
    @Test
    public void testGetComment() {
        // typical use case
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", new Range(0, 10), new Date(), "comment1", "data1");
        assertEquals("comment1", tag.getComment());
        
        // edge case: comment is null
        DefaultPhdReadTag nullCommentTag = new DefaultPhdReadTag("type2", "source2", new Range(0, 10), new Date(), null, "data2");
        assertNull(nullCommentTag.getComment());
    }
    
    @Test
    public void testGetFreeFormData() {
        // typical use case
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", new Range(0, 10), new Date(), "comment1", "data1");
        assertEquals("data1", tag.getFreeFormData());
        
        // edge case: free form data is null
        DefaultPhdReadTag nullDataTag = new DefaultPhdReadTag("type2", "source2", new Range(0, 10), new Date(), "comment2", null);
        assertNull(nullDataTag.getFreeFormData());
    }
    
    @Test
    public void testHashCode() {
        // typical use case
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", new Range(0, 10), new Date(), "comment1", "data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source1", new Range(0, 10), new Date(), "comment1", "data1");
        assertEquals(tag1.hashCode(), tag2.hashCode());
        
        // edge case: null values
        DefaultPhdReadTag tag3 = new DefaultPhdReadTag(null, "source2", new Range(0, 10), new Date(), "comment2", "data2");
        DefaultPhdReadTag tag4 = new DefaultPhdReadTag(null, "source2", new Range(0, 10), new Date(), "comment2", "data2");
        assertEquals(tag3.hashCode(), tag4.hashCode());
    }
    
    @Test
    public void testEquals() {
        // typical use case
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", new Range(0, 10), new Date(), "comment1", "data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source1", new Range(0, 10), new Date(), "comment1", "data1");
        assertTrue(tag1.equals(tag2));
        
        // edge case: null values
        DefaultPhdReadTag tag3 = new DefaultPhdReadTag(null, "source2", new Range(0, 10), new Date(), "comment2", "data2");
        DefaultPhdReadTag tag4 = new DefaultPhdReadTag(null, "source2", new Range(0, 10), new Date(), "comment2", "data2");
        assertTrue(tag3.equals(tag4));
        
        // error scenario: different type
        DefaultPhdReadTag tag5 = new DefaultPhdReadTag("type3", "source3", new Range(0, 10), new Date(), "comment3", "data3");
        assertFalse(tag1.equals(tag5));
        
        // error scenario: different source
        DefaultPhdReadTag tag6 = new DefaultPhdReadTag("type1", "source4", new Range(0, 10), new Date(), "comment4", "data4");
        assertFalse(tag1.equals(tag6));
        
        // error scenario: different range
        DefaultPhdReadTag tag7 = new DefaultPhdReadTag("type1", "source1", new Range(0, 20), new Date(), "comment5", "data5");
        assertFalse(tag1.equals(tag7));
        
        // error scenario: different date
        DefaultPhdReadTag tag8 = new DefaultPhdReadTag("type1", "source1", new Range(0, 10), new Date(10000), "comment6", "data6");
        assertFalse(tag1.equals(tag8));
        
        // error scenario: different comment
        DefaultPhdReadTag tag9 = new DefaultPhdReadTag("type1", "source1", new Range(0, 10), new Date(), "comment7", "data7");
        assertFalse(tag1.equals(tag9));
        
        // error scenario: different data
        DefaultPhdReadTag tag10 = new DefaultPhdReadTag("type1", "source1", new Range(0, 10), new Date(), "comment8", "data8");
        assertFalse(tag1.equals(tag10));
    }
}