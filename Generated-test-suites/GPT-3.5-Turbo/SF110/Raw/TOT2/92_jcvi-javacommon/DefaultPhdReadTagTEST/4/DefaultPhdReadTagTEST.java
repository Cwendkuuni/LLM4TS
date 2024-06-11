import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Date;

public class DefaultPhdReadTagTest {
    
    @Test
    public void testGetType() {
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        assertEquals("type", defaultPhdReadTag.getType());
    }
    
    @Test
    public void testGetSource() {
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        assertEquals("source", defaultPhdReadTag.getSource());
    }
    
    @Test
    public void testGetUngappedRange() {
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        assertEquals(new Range(0, 10), defaultPhdReadTag.getUngappedRange());
    }
    
    @Test
    public void testGetDate() {
        Date date = new Date();
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("type", "source", new Range(0, 10), date, "comment", "freeFormData");
        assertEquals(date, defaultPhdReadTag.getDate());
    }
    
    @Test
    public void testGetComment() {
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        assertEquals("comment", defaultPhdReadTag.getComment());
    }
    
    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        assertEquals("freeFormData", defaultPhdReadTag.getFreeFormData());
    }
    
    // Additional test cases for edge cases and exceptions can be added here
    
}