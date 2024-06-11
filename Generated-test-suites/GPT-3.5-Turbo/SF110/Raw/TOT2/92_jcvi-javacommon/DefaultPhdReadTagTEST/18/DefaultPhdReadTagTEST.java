import static org.junit.Assert.*;
import org.junit.Test;
import org.jcvi.jillion.core.Range;
import org.jcvi.jillion.assembly.consed.phd.DefaultPhdReadTag;

public class DefaultPhdReadTagTest {
    
    @Test
    public void testGetType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(0, 10), new Date(), "Comment", "FreeForm");
        assertEquals("Type", tag.getType());
    }
    
    @Test
    public void testGetSource() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(0, 10), new Date(), "Comment", "FreeForm");
        assertEquals("Source", tag.getSource());
    }
    
    @Test
    public void testGetUngappedRange() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(0, 10), new Date(), "Comment", "FreeForm");
        assertEquals(new Range(0, 10), tag.getUngappedRange());
    }
    
    @Test
    public void testGetDate() {
        Date date = new Date();
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(0, 10), date, "Comment", "FreeForm");
        assertEquals(date, tag.getDate());
    }
    
    @Test
    public void testGetComment() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(0, 10), new Date(), "Comment", "FreeForm");
        assertEquals("Comment", tag.getComment());
    }
    
    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type", "Source", new Range(0, 10), new Date(), "Comment", "FreeForm");
        assertEquals("FreeForm", tag.getFreeFormData());
    }
    
    // Edge Cases and Exception Handling tests to be added here
    
    // Test for hashCode method could be added here
    
    // Test for equals method could be added here
    
}