import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {
    
    @Test
    public void testGetType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        assertEquals("type", tag.getType());
    }
    
    @Test
    public void testGetSource() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        assertEquals("source", tag.getSource());
    }
    
    @Test
    public void testGetUngappedRange() {
        Range range = new Range(0, 10);
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", range, new Date(), "comment", "freeFormData");
        assertEquals(range, tag.getUngappedRange());
    }
    
    @Test
    public void testGetDate() {
        Date date = new Date();
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), date, "comment", "freeFormData");
        assertEquals(date, tag.getDate());
    }
    
    @Test
    public void testGetComment() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        assertEquals("comment", tag.getComment());
    }
    
    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        assertEquals("freeFormData", tag.getFreeFormData());
    }
    
    @Test
    public void testHashCode() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    @Test
    public void testEquals() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", new Range(0, 10), new Date(), "comment", "freeFormData");
        assertTrue(tag1.equals(tag2));
    }
}