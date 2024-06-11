import static org.junit.Assert.*;
import org.junit.Test;
import org.jcvi.jillion.core.Range;

public class DefaultPhdReadTagTest {
    
    private DefaultPhdReadTag defaultPhdReadTag;
    
    @Test
    public void testGetType() {
        defaultPhdReadTag = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment1", "FreeFormData1");
        assertEquals("Type1", defaultPhdReadTag.getType());
    }
    
    @Test
    public void testGetSource() {
        defaultPhdReadTag = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment1", "FreeFormData1");
        assertEquals("Source1", defaultPhdReadTag.getSource());
    }
    
    @Test
    public void testGetUngappedRange() {
        Range range = new Range(1, 10);
        defaultPhdReadTag = new DefaultPhdReadTag("Type1", "Source1", range, new Date(), "Comment1", "FreeFormData1");
        assertEquals(range, defaultPhdReadTag.getUngappedRange());
    }
    
    @Test
    public void testGetDate() {
        Date date = new Date();
        defaultPhdReadTag = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), date, "Comment1", "FreeFormData1");
        assertEquals(date, defaultPhdReadTag.getDate());
    }
    
    @Test
    public void testGetComment() {
        defaultPhdReadTag = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment1", "FreeFormData1");
        assertEquals("Comment1", defaultPhdReadTag.getComment());
    }
    
    @Test
    public void testGetFreeFormData() {
        defaultPhdReadTag = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment1", "FreeFormData1");
        assertEquals("FreeFormData1", defaultPhdReadTag.getFreeFormData());
    }
    
    @Test
    public void testHashCode() {
        defaultPhdReadTag = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment1", "FreeFormData1");
        DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment1", "FreeFormData1");
        assertEquals(defaultPhdReadTag.hashCode(), defaultPhdReadTag2.hashCode());
    }
    
    @Test
    public void testEquals() {
        defaultPhdReadTag = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment1", "FreeFormData1");
        DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment1", "FreeFormData1");
        assertTrue(defaultPhdReadTag.equals(defaultPhdReadTag2));
    }
    
    @Test
    public void testEqualsWithDifferentType() {
        defaultPhdReadTag = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment1", "FreeFormData1");
        DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag("Type2", "Source1", new Range(1, 10), new Date(), "Comment1", "FreeFormData1");
        assertFalse(defaultPhdReadTag.equals(defaultPhdReadTag2));
    }
    
    @Test
    public void testEqualsWithDifferentSource() {
        defaultPhdReadTag = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment1", "FreeFormData1");
        DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag("Type1", "Source2", new Range(1, 10), new Date(), "Comment1", "FreeFormData1");
        assertFalse(defaultPhdReadTag.equals(defaultPhdReadTag2));
    }
    
    @Test
    public void testEqualsWithDifferentRange() {
        defaultPhdReadTag = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment1", "FreeFormData1");
        DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 20), new Date(), "Comment1", "FreeFormData1");
        assertFalse(defaultPhdReadTag.equals(defaultPhdReadTag2));
    }
    
    @Test
    public void testEqualsWithDifferentDate() {
        Date date1 = new Date();
        Date date2 = new Date(date1.getTime() + 1000);
        defaultPhdReadTag = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), date1, "Comment1", "FreeFormData1");
        DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), date2, "Comment1", "FreeFormData1");
        assertFalse(defaultPhdReadTag.equals(defaultPhdReadTag2));
    }
    
    @Test
    public void testEqualsWithDifferentComment() {
        defaultPhdReadTag = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment1", "FreeFormData1");
        DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment2", "FreeFormData1");
        assertFalse(defaultPhdReadTag.equals(defaultPhdReadTag2));
    }
    
    @Test
    public void testEqualsWithDifferentFreeFormData() {
        defaultPhdReadTag = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment1", "FreeFormData1");
        DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment1", "FreeFormData2");
        assertFalse(defaultPhdReadTag.equals(defaultPhdReadTag2));
    }
}