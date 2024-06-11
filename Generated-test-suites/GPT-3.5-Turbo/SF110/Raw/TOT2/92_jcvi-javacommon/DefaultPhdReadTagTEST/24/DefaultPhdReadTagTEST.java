import static org.junit.Assert.*;
import org.junit.Test;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "freeFormData");
        assertEquals("type", phdReadTag.getType());
    }
    
    @Test
    public void testGetSource() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "freeFormData");
        assertEquals("source", phdReadTag.getSource());
    }
    
    @Test
    public void testGetUngappedRange() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "freeFormData");
        assertEquals(new Range(1, 5), phdReadTag.getUngappedRange());
    }
    
    @Test
    public void testGetDate() {
        Date date = new Date();
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("type", "source", new Range(1, 5), date, "comment", "freeFormData");
        assertEquals(date, phdReadTag.getDate());
    }
    
    @Test
    public void testGetComment() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "freeFormData");
        assertEquals("comment", phdReadTag.getComment());
    }
    
    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "freeFormData");
        assertEquals("freeFormData", phdReadTag.getFreeFormData());
    }
    
    @Test
    public void testEquals_SameObject() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "freeFormData");
        assertTrue(phdReadTag.equals(phdReadTag));
    }
    
    @Test
    public void testEquals_NullObject() {
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("type", "source", new Range(1, 5), new Date(), "comment", "freeFormData");
        assertFalse(phdReadTag.equals(null));
    }
    
    // Add more edge case and exception handling test cases as needed
    
}