import static org.junit.Assert.*;
import org.junit.Test;
import org.jcvi.jillion.core.Range;
import java.util.Date;

public class DefaultPhdReadTagTest {
    private DefaultPhdReadTag tag;
    
    @Test
    public void testGetType() {
        tag = new DefaultPhdReadTag(null, "source", new Range(), new Date(), "comment", "data");
        assertNull(tag.getType());
        
        tag = new DefaultPhdReadTag("", "source", new Range(), new Date(), "comment", "data");
        assertEquals("", tag.getType());
    }
    
    @Test
    public void testGetSource() {
        tag = new DefaultPhdReadTag("type", null, new Range(), new Date(), "comment", "data");
        assertNull(tag.getSource());
        
        tag = new DefaultPhdReadTag("type", "", new Range(), new Date(), "comment", "data");
        assertEquals("", tag.getSource());
    }
    
    @Test
    public void testGetUngappedRange() {
        Range range = new Range();
        tag = new DefaultPhdReadTag("type", "source", range, new Date(), "comment", "data");
        assertEquals(range, tag.getUngappedRange());
    }
    
    @Test
    public void testGetDate() {
        tag = new DefaultPhdReadTag("type", "source", new Range(), null, "comment", "data");
        assertNull(tag.getDate());
    }
    
    @Test
    public void testGetComment() {
        tag = new DefaultPhdReadTag("type", "source", new Range(), new Date(), null, "data");
        assertNull(tag.getComment());
        
        tag = new DefaultPhdReadTag("type", "source", new Range(), new Date(), "", "data");
        assertEquals("", tag.getComment());
    }
    
    @Test
    public void testGetFreeFormData() {
        tag = new DefaultPhdReadTag("type", "source", new Range(), new Date(), "comment", null);
        assertNull(tag.getFreeFormData());
        
        tag = new DefaultPhdReadTag("type", "source", new Range(), new Date(), "comment", "");
        assertEquals("", tag.getFreeFormData());
    }
    
    // Additional test cases for the basic functionality of each method
    
    @Test
    public void testGetTypeBasic() {
        tag = new DefaultPhdReadTag("type", "source", new Range(), new Date(), "comment", "data");
        assertEquals("type", tag.getType());
    }
    
    @Test
    public void testGetSourceBasic() {
        tag = new DefaultPhdReadTag("type", "source", new Range(), new Date(), "comment", "data");
        assertEquals("source", tag.getSource());
    }
    
    @Test
    public void testGetUngappedRangeBasic() {
        Range range = new Range();
        tag = new DefaultPhdReadTag("type", "source", range, new Date(), "comment", "data");
        assertEquals(range, tag.getUngappedRange());
    }
    
    @Test
    public void testGetDateBasic() {
        Date date = new Date();
        tag = new DefaultPhdReadTag("type", "source", new Range(), date, "comment", "data");
        assertEquals(date, tag.getDate());
    }
    
    @Test
    public void testGetCommentBasic() {
        tag = new DefaultPhdReadTag("type", "source", new Range(), new Date(), "comment", "data");
        assertEquals("comment", tag.getComment());
    }
    
    @Test
    public void testGetFreeFormDataBasic() {
        tag = new DefaultPhdReadTag("type", "source", new Range(), new Date(), "comment", "data");
        assertEquals("data", tag.getFreeFormData());
    }
}