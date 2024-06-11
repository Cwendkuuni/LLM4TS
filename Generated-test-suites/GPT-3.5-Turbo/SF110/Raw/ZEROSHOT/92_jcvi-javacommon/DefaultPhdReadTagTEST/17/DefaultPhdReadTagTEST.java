import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import org.jcvi.jillion.core.Range;
import java.util.Date;

public class DefaultPhdReadTagTest {
    
    private DefaultPhdReadTag tag;
    private Range ungappedRange;
    private Date date;
    
    @Before
    public void setUp() {
        String type = "testType";
        String source = "testSource";
        ungappedRange = new Range(1, 10);
        date = new Date();
        String comment = "testComment";
        String freeFormData = "testFreeFormData";
        
        tag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
    }
    
    @Test
    public void testGetType() {
        assertEquals("testType", tag.getType());
    }
    
    @Test
    public void testGetSource() {
        assertEquals("testSource", tag.getSource());
    }
    
    @Test
    public void testGetUngappedRange() {
        assertEquals(ungappedRange, tag.getUngappedRange());
    }
    
    @Test
    public void testGetDate() {
        assertEquals(date, tag.getDate());
    }
    
    @Test
    public void testGetComment() {
        assertEquals("testComment", tag.getComment());
    }
    
    @Test
    public void testGetFreeFormData() {
        assertEquals("testFreeFormData", tag.getFreeFormData());
    }
    
    @Test
    public void testHashCode() {
        DefaultPhdReadTag other = new DefaultPhdReadTag("testType", "testSource", ungappedRange, date, "testComment", "testFreeFormData");
        assertEquals(tag.hashCode(), other.hashCode());
    }
    
    @Test
    public void testEquals() {
        DefaultPhdReadTag other = new DefaultPhdReadTag("testType", "testSource", ungappedRange, date, "testComment", "testFreeFormData");
        assertTrue(tag.equals(other));
    }
}