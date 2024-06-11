import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Date;
import org.jcvi.jillion.core.Range;

public class DefaultPhdReadTagTest {
    
    @Test
    public void testGetType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "data1");
        assertEquals("type1", tag.getType());
    }
    
    @Test
    public void testGetSource() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "data1");
        assertEquals("source1", tag.getSource());
    }
    
    @Test
    public void testGetUngappedRange() {
        Range range = new Range(1, 10);
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", range, new Date(), "comment1", "data1");
        assertEquals(range, tag.getUngappedRange());
    }
    
    @Test
    public void testGetDate() {
        Date date = new Date();
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), date, "comment1", "data1");
        assertEquals(date, tag.getDate());
    }
    
    @Test
    public void testGetComment() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "data1");
        assertEquals("comment1", tag.getComment());
    }
    
    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "data1");
        assertEquals("data1", tag.getFreeFormData());
    }
    
    @Test
    public void testEquals() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "data1");
        assertEquals(tag1, tag2);
    }
    
    @Test
    public void testEqualsWithDifferentType() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type2", "source1", new Range(1, 10), new Date(), "comment1", "data1");
        assertNotEquals(tag1, tag2);
    }
    
    @Test
    public void testEqualsWithDifferentSource() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source2", new Range(1, 10), new Date(), "comment1", "data1");
        assertNotEquals(tag1, tag2);
    }
    
    @Test
    public void testEqualsWithDifferentUngappedRange() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source1", new Range(10, 20), new Date(), "comment1", "data1");
        assertNotEquals(tag1, tag2);
    }
    
    @Test
    public void testEqualsWithDifferentDate() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(123456789), "comment1", "data1");
        assertNotEquals(tag1, tag2);
    }
    
    @Test
    public void testEqualsWithDifferentComment() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment2", "data1");
        assertNotEquals(tag1, tag2);
    }
    
    @Test
    public void testEqualsWithDifferentFreeFormData() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "data2");
        assertNotEquals(tag1, tag2);
    }
    
    @Test
    public void testHashCode() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "data1");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    @Test
    public void testHashCodeWithDifferentType() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type2", "source1", new Range(1, 10), new Date(), "comment1", "data1");
        assertNotEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    @Test
    public void testHashCodeWithDifferentSource() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source2", new Range(1, 10), new Date(), "comment1", "data1");
        assertNotEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    @Test
    public void testHashCodeWithDifferentUngappedRange() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source1", new Range(10, 20), new Date(), "comment1", "data1");
        assertNotEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    @Test
    public void testHashCodeWithDifferentDate() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(123456789), "comment1", "data1");
        assertNotEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    @Test
    public void testHashCodeWithDifferentComment() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment2", "data1");
        assertNotEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    @Test
    public void testHashCodeWithDifferentFreeFormData() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment1", "data2");
        assertNotEquals(tag1.hashCode(), tag2.hashCode());
    }
}