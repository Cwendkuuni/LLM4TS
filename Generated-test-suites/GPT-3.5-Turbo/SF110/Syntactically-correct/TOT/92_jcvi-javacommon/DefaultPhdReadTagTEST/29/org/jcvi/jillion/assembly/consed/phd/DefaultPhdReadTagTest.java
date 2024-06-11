import org.junit.Test;
import org.joda.time.DateTime;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {
    
    @Test
    public void testGetType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new DateTime(), "Comment", "Form Data");
        assertEquals("Type", tag.getType());
    }
    
    @Test
    public void testGetSource() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new DateTime(), "Comment", "Form Data");
        assertEquals("Source", tag.getSource());
    }
    
    @Test
    public void testGetUngappedRange() {
        Range range = new Range(1, 10);
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", range, new DateTime(), "Comment", "Form Data");
        assertEquals(range, tag.getUngappedRange());
    }
    
    @Test
    public void testGetDate() {
        DateTime date = new DateTime();
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), date, "Comment", "Form Data");
        assertEquals(date, tag.getDate());
    }
    
    @Test
    public void testGetComment() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new DateTime(), "Comment", "Form Data");
        assertEquals("Comment", tag.getComment());
    }
    
    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new DateTime(), "Comment", "Form Data");
        assertEquals("Form Data", tag.getFreeFormData());
    }
    
    @Test
    public void testEquals() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new DateTime(), "Comment", "Form Data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new DateTime(), "Comment", "Form Data");
        DefaultPhdReadTag tag3 = new DefaultPhdReadTag("Type2", "Source2", new Range(11, 20), new DateTime(), "Comment2", "Form Data2");
        
        assertTrue(tag1.equals(tag2));
        assertFalse(tag1.equals(tag3));
    }
    
    @Test
    public void testHashCode() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new DateTime(), "Comment", "Form Data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new DateTime(), "Comment", "Form Data");
        
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }
}