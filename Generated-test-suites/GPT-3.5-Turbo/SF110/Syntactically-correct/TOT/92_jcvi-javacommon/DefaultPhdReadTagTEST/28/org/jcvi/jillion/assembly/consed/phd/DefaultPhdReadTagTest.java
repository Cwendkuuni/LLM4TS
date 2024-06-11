import static org.junit.Assert.*;
import org.junit.Test;
import org.jcvi.jillion.core.*;
import java.util.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment1", "Data1");
        assertEquals("Type1", tag.getType());
    }
    
    @Test
    public void testGetSource() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment1", "Data1");
        assertEquals("Source1", tag.getSource());
    }
    
    @Test
    public void testGetUngappedRange() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment1", "Data1");
        Range range = tag.getUngappedRange();
        assertEquals(1, range.getStart());
        assertEquals(10, range.getEnd());
    }
    
    @Test
    public void testGetDate() {
        Date currentDate = new Date();
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), currentDate, "Comment1", "Data1");
        assertEquals(currentDate, tag.getDate());
    }
    
    @Test
    public void testGetComment() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment1", "Data1");
        assertEquals("Comment1", tag.getComment());
    }
    
    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment1", "Data1");
        assertEquals("Data1", tag.getFreeFormData());
    }
    
    @Test
    public void testEquals() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment1", "Data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment1", "Data1");
        assertTrue(tag1.equals(tag2));
    }
    
    @Test
    public void testNotEquals() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment1", "Data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("Type2", "Source1", new Range(1, 10), new Date(), "Comment1", "Data1");
        assertFalse(tag1.equals(tag2));
    }
    
    @Test
    public void testHashCode() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment1", "Data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment1", "Data1");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    @Test
    public void testHashCodeDifferent() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment1", "Data1");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("Type2", "Source1", new Range(1, 10), new Date(), "Comment1", "Data1");
        assertNotEquals(tag1.hashCode(), tag2.hashCode());
    }
   
}