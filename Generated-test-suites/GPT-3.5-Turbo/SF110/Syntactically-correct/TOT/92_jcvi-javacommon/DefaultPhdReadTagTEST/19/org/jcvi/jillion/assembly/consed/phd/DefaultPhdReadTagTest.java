import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Date;
import org.jcvi.jillion.core.Range;

public class DefaultPhdReadTagTest {
    
    // Test case for getType()
    @Test
    public void testGetType() {
        // Typical use case
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "data");
        assertEquals("type", tag.getType());
    }
    
    // Test case for getSource()
    @Test
    public void testGetSource() {
        // Typical use case
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "data");
        assertEquals("source", tag.getSource());
    }
    
    // Test case for getUngappedRange()
    @Test
    public void testGetUngappedRange() {
        // Typical use case
        Range range = new Range(0, 10);
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", range, new Date(), "comment", "data");
        assertEquals(range, tag.getUngappedRange());
    }
    
    // Test case for getDate()
    @Test
    public void testGetDate() {
        // Typical use case
        Date date = new Date();
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), date, "comment", "data");
        assertEquals(date, tag.getDate());
    }
    
    // Test case for getComment()
    @Test
    public void testGetComment() {
        // Typical use case
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "data");
        assertEquals("comment", tag.getComment());
    }
    
    // Test case for getFreeFormData()
    @Test
    public void testGetFreeFormData() {
        // Typical use case
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "data");
        assertEquals("data", tag.getFreeFormData());
    }
    
    // Test case for hashCode()
    @Test
    public void testHashCode() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "data");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    // Test case for equals()
    @Test
    public void testEquals() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "data");
        assertTrue(tag1.equals(tag2));
    }
}