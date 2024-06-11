import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class DefaultPhdReadTagTest {
    
    private DefaultPhdReadTag tag;
    
    @Before
    public void setUp() {
        String type = "Type";
        String source = "Source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "Comment";
        String freeFormData = "Free Form Data";
        
        tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }
    
    @Test
    public void testGetType() {
        String type = tag.getType();
        assertNotNull(type);
        assertEquals("Type", type);
    }
    
    @Test
    public void testGetSource() {
        String source = tag.getSource();
        assertNotNull(source);
        assertEquals("Source", source);
    }
    
    @Test
    public void testGetUngappedRange() {
        Range ungappedRange = tag.getUngappedRange();
        assertNotNull(ungappedRange);
        assertEquals(1, ungappedRange.getStart());
        assertEquals(10, ungappedRange.getEnd());
    }
    
    @Test
    public void testGetDate() {
        Date date = tag.getDate();
        assertNotNull(date);
        assertEquals(new Date().getTime(), date.getTime(), 1000); // Allow a difference of 1 second
    }
    
    @Test
    public void testGetComment() {
        String comment = tag.getComment();
        assertNotNull(comment);
        assertEquals("Comment", comment);
    }
    
    @Test
    public void testGetFreeFormData() {
        String freeFormData = tag.getFreeFormData();
        assertNotNull(freeFormData);
        assertEquals("Free Form Data", freeFormData);
    }
    
    @Test
    public void testHashCode() {
        int hashCode = tag.hashCode();
        assertEquals(161288452, hashCode);
    }
    
    @Test
    public void testEqualsWhenEqual() {
        DefaultPhdReadTag other = new DefaultPhdReadTag("Type", "Source", new Range(1, 10), new Date(), "Comment", "Free Form Data");
        assertTrue(tag.equals(other));
    }
    
    @Test
    public void testEqualsWhenNotEqual() {
        DefaultPhdReadTag other = new DefaultPhdReadTag("Type", "Different Source", new Range(1, 10), new Date(), "Comment", "Free Form Data");
        assertFalse(tag.equals(other));
    }
}