import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class DefaultPhdReadTagTest {
    
    @Test
    public void testGetType() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment1", "FreeForm1");
        assertEquals("Type1", tag.getType());
    }
    
    @Test
    public void testGetSource() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type2", "Source2", new Range(2, 20), new Date(), "Comment2", "FreeForm2");
        assertEquals("Source2", tag.getSource());
    }
    
    @Test
    public void testGetUngappedRange() {
        Range range = new Range(3, 30);
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type3", "Source3", range, new Date(), "Comment3", "FreeForm3");
        assertEquals(range, tag.getUngappedRange());
    }
    
    @Test
    public void testGetDate() {
        Date date = new Date();
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type4", "Source4", new Range(4, 40), date, "Comment4", "FreeForm4");
        assertEquals(date, tag.getDate());
    }
    
    @Test
    public void testGetComment() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type5", "Source5", new Range(5, 50), new Date(), "Comment5", "FreeForm5");
        assertEquals("Comment5", tag.getComment());
    }
    
    @Test
    public void testGetFreeFormData() {
        DefaultPhdReadTag tag = new DefaultPhdReadTag("Type6", "Source6", new Range(6, 60), new Date(), "Comment6", "FreeForm6");
        assertEquals("FreeForm6", tag.getFreeFormData());
    }
    
    @Test
    public void testEquals() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("Type7", "Source7", new Range(7, 70), new Date(), "Comment7", "FreeForm7");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("Type7", "Source7", new Range(7, 70), new Date(), "Comment7", "FreeForm7");
        assertTrue(tag1.equals(tag2));
    }
    
    @Test
    public void testNotEquals() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("Type8", "Source8", new Range(8, 80), new Date(), "Comment8", "FreeForm8");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("Type8", "Source8", new Range(8, 80), new Date(), "Comment9", "FreeForm8");
        assertFalse(tag1.equals(tag2));
    }
    
    @Test
    public void testHashCode() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("Type9", "Source9", new Range(9, 90), new Date(), "Comment9", "FreeForm9");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("Type9", "Source9", new Range(9, 90), new Date(), "Comment9", "FreeForm9");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    @Test
    public void testNotHashCode() {
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("Type10", "Source10", new Range(10, 100), new Date(), "Comment10", "FreeForm10");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("Type11", "Source11", new Range(11, 110), new Date(), "Comment11", "FreeForm11");
        assertNotEquals(tag1.hashCode(), tag2.hashCode());
    }
}