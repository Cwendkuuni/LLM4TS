import org.junit.Test;
import static org.junit.Assert.*;

import org.jcvi.jillion.core.Range;
import java.util.Date;

public class DefaultPhdReadTagTest {
    
    @Test
    public void testGetType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Free form data");
        assertEquals("Type", tag.getType());
    }
    
    @Test
    public void testGetSource() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Free form data");
        assertEquals("Source", tag.getSource());
    }
    
    @Test
    public void testGetUngappedRange() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Free form data");
        assertEquals(new Range(1, 10), tag.getUngappedRange());
    }
    
    @Test
    public void testGetDate() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Free form data");
        assertNotNull(tag.getDate());
    }
    
    @Test
    public void testGetComment() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Free form data");
        assertEquals("Comment", tag.getComment());
    }
    
    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Free form data");
        assertEquals("Free form data", tag.getFreeFormData());
    }
    
    @Test
    public void testHashCode() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Free form data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Free form data");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    @Test
    public void testEquals() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Free form data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Free form data");
        assertTrue(tag1.equals(tag2));
    }
    
    @Test
    public void testNotEquals() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Free form data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("Type2", "Source", new Range(1, 10), new Date(), "Comment", "Free form data");
        assertFalse(tag1.equals(tag2));
    }
}