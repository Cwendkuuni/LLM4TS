import static org.junit.Assert.*;
import org.junit.Test;
import org.jcvi.jillion.core.Range;

public class DefaultPhdReadTagTest {
    
    @Test
    public void testGetType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", Range.empty(), new Date(), "comment", "data");
        assertEquals("type", tag.getType());
    }
    
    @Test
    public void testGetSource() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", Range.empty(), new Date(), "comment", "data");
        assertEquals("source", tag.getSource());
    }
    
    @Test
    public void testGetUngappedRange() {
        Range range = Range.empty();
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", range, new Date(), "comment", "data");
        assertEquals(range, tag.getUngappedRange());
    }
    
    @Test
    public void testGetDate() {
        Date date = new Date();
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", Range.empty(), date, "comment", "data");
        assertEquals(date, tag.getDate());
    }
    
    @Test
    public void testGetComment() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", Range.empty(), new Date(), "comment", "data");
        assertEquals("comment", tag.getComment());
    }
    
    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", Range.empty(), new Date(), "comment", "data");
        assertEquals("data", tag.getFreeFormData());
    }
    
    @Test
    public void testEquals() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", Range.empty(), new Date(), "comment", "data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", Range.empty(), new Date(), "comment", "data");
        assertEquals(true, tag1.equals(tag2));
    }
    
    @Test
    public void testHashCode() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", Range.empty(), new Date(), "comment", "data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", Range.empty(), new Date(), "comment", "data");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    // Additional test cases for potential edge cases and exceptions
    // ...
    // ...
}