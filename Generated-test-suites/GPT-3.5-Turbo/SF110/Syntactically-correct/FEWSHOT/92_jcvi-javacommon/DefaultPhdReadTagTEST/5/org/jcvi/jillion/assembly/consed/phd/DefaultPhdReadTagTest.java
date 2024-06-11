import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;
import org.junit.Test;

import java.util.Date;

public class DefaultPhdReadTagTest {
    
    @Test
    public void testConstructorAndGetters() {
        Date date = new Date();
        Range range = new Range(0, 10);
        String type = "type";
        String source = "source";
        String comment = "comment";
        String freeFormData = "free form data";
        
        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, range, date, comment, freeFormData);
        
        assertEquals(type, tag.getType());
        assertEquals(source, tag.getSource());
        assertEquals(range, tag.getUngappedRange());
        assertEquals(date, tag.getDate());
        assertEquals(comment, tag.getComment());
        assertEquals(freeFormData, tag.getFreeFormData());
    }
    
    @Test
    public void testEqualsAndHashCode() {
        Date date1 = new Date();
        Date date2 = new Date();
        Range range1 = new Range(0, 10);
        Range range2 = new Range(10, 20);
        String type1 = "type1";
        String type2 = "type2";
        String source1 = "source1";
        String source2 = "source2";
        String comment1 = "comment1";
        String comment2 = "comment2";
        String freeFormData1 = "free form data1";
        String freeFormData2 = "free form data2";
        
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type1, source1, range1, date1, comment1, freeFormData1);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type1, source1, range1, date1, comment1, freeFormData1);
        DefaultPhdReadTag tag3 = new DefaultPhdReadTag(type1, source1, range1, date2, comment1, freeFormData1);
        DefaultPhdReadTag tag4 = new DefaultPhdReadTag(type1, source1, range1, date1, comment2, freeFormData1);
        DefaultPhdReadTag tag5 = new DefaultPhdReadTag(type1, source1, range1, date1, comment1, freeFormData2);
        DefaultPhdReadTag tag6 = new DefaultPhdReadTag(type2, source1, range1, date1, comment1, freeFormData1);
        DefaultPhdReadTag tag7 = new DefaultPhdReadTag(type1, source2, range1, date1, comment1, freeFormData1);
        DefaultPhdReadTag tag8 = new DefaultPhdReadTag(type1, source1, range2, date1, comment1, freeFormData1);
        DefaultPhdReadTag tag9 = new DefaultPhdReadTag(type1, source1, range1, date1, comment1, freeFormData1);
        
        assertEquals(tag1, tag2);
        assertEquals(tag1.hashCode(), tag2.hashCode());
        
        assertEquals(tag1, tag9);
        assertEquals(tag1.hashCode(), tag9.hashCode());
        
        assertEquals(tag1.equals(tag3), false);
        assertEquals(tag1.equals(tag4), false);
        assertEquals(tag1.equals(tag5), false);
        assertEquals(tag1.equals(tag6), false);
        assertEquals(tag1.equals(tag7), false);
        assertEquals(tag1.equals(tag8), false);
    }
}