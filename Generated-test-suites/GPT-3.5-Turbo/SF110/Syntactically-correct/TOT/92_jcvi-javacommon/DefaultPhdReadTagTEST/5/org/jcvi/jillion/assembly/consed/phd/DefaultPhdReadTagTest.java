import org.junit.*;
import static org.junit.Assert.*;
import org.jcvi.jillion.core.*;
import java.util.*;

public class DefaultPhdReadTagTest {
    
    // Test case for getType()
    @Test
    public void testGetType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "freeFormData");
        assertEquals("type", tag.getType());
    }
    
    // Test case for getSource()
    @Test
    public void testGetSource() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "freeFormData");
        assertEquals("source", tag.getSource());
    }
    
    // Test case for getUngappedRange()
    @Test
    public void testGetUngappedRange() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "freeFormData");
        Range expectedRange = new Range(1, 5);
        assertEquals(expectedRange, tag.getUngappedRange());
    }
    
    // Test case for getDate()
    @Test
    public void testGetDate() {
        Date currentDate = new Date();
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 5), currentDate, "comment", "freeFormData");
        assertEquals(currentDate, tag.getDate());
    }
    
    // Test case for getComment()
    @Test
    public void testGetComment() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "freeFormData");
        assertEquals("comment", tag.getComment());
    }
    
    // Test case for getFreeFormData()
    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "freeFormData");
        assertEquals("freeFormData", tag.getFreeFormData());
    }
    
    // Test case for hashCode()
    @Test
    public void testHashCode() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 5), new Date(), "comment1", "freeFormData1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type2", "source2", new Range(1, 5), new Date(), "comment2", "freeFormData2");
        assertNotEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    // Test case for equals()
    @Test
    public void testEquals() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "freeFormData");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "freeFormData");
        assertEquals(tag1, tag2);
    }
}