import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {
    private final String type = "Type";
    private final String source = "Source";
    private final Range ungappedRange = new Range(0, 10);
    private final Date date = new Date();
    private final String comment = "Comment";
    private final String freeFormData = "FreeFormData";
    
    @Test
    public void testGetType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertEquals(type, tag.getType());
    }
    
    @Test
    public void testGetSource() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertEquals(source, tag.getSource());
    }
    
    @Test
    public void testGetUngappedRange() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertEquals(ungappedRange, tag.getUngappedRange());
    }
    
    @Test
    public void testGetDate() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertEquals(date, tag.getDate());
    }
    
    @Test
    public void testGetComment() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertEquals(comment, tag.getComment());
    }
    
    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertEquals(freeFormData, tag.getFreeFormData());
    }
    
    @Test
    public void testHashCode() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    @Test
    public void testEquals() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        assertTrue(tag1.equals(tag2));
    }
    
    // Add additional test cases for potential edge cases and exception handling scenarios
    
    
    // Add additional test cases here
    
}