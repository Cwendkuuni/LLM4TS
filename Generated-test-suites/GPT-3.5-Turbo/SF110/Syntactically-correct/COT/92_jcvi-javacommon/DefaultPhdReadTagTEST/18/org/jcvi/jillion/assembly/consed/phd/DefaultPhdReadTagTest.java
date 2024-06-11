import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "FreeForm");
        assertEquals("Type", tag.getType());
    }
    
    @Test
    public void testGetSource() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "FreeForm");
        assertEquals("Source", tag.getSource());
    }
    
    @Test
    public void testGetUngappedRange() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "FreeForm");
        assertEquals(new Range(1, 10), tag.getUngappedRange());
    }
    
    @Test
    public void testGetDate() {
        Date now = new Date();
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), now, "Comment", "FreeForm");
        assertEquals(now, tag.getDate());
    }
    
    @Test
    public void testGetComment() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "FreeForm");
        assertEquals("Comment", tag.getComment());
    }
    
    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "FreeForm");
        assertEquals("FreeForm", tag.getFreeFormData());
    }
    
    @Test
    public void testHashCode() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "FreeForm");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "FreeForm");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    @Test
    public void testEquals() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "FreeForm");
        DefaultPhdRaeTag tag2 = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "FreeForm");
        assertEquals(tag1, tag2);
    }
}