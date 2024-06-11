import static org.junit.Assert.*;
import org.junit.Test;
import org.jcvi.jillion.core.Range;

public class DefaultPhdReadTagTest {
    
    @Test
    public void testGetType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "data");
        assertEquals("type", tag.getType());
    }
    
    @Test
    public void testGetSource() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "data");
        assertEquals("source", tag.getSource());
    }
    
    @Test
    public void testGetUngappedRange() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "data");
        assertEquals(new Range(1, 10), tag.getUngappedRange());
    }
    
    @Test
    public void testGetDate() {
        Date now = new Date();
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 10), now, "comment", "data");
        assertEquals(now, tag.getDate());
    }
    
    @Test
    public void testGetComment() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "data");
        assertEquals("comment", tag.getComment());
    }
    
    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "data");
        assertEquals("data", tag.getFreeFormData());
    }
    
    @Test
    public void testEquals() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "data");
        DefaultPhdReadTag tag3 = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "data");
        DefaultPhdReadTag tag4 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "data2");
        
        assertEquals(true, tag1.equals(tag2));
        assertEquals(false, tag1.equals(tag3));
        assertEquals(false, tag1.equals(tag4));
    }
    
    @Test
    public void testHashCode() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "data");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "data");
        
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }
    
}