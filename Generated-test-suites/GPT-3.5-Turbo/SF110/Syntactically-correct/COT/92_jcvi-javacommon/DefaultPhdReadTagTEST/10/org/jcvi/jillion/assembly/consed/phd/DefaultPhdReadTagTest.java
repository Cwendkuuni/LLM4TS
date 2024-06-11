
import org.junit.Test;
import static org.junit.Assert.*;

public class DefaultPhdReadTagTest {
    
    @Test
    public void testGetType() {
        // Create a DefaultPhdReadTag object and set its type
        DefaultPhdReadTag tag = new DefaultPhdReadTag("type", "", null, null, "", "");
        
        // Test if the getType() method returns the correct type
        assertEquals("type", tag.getType());
    }
    
    @Test
    public void testGetSource() {
         // Create a DefaultPhdReadTag object and set its source
        DefaultPhdReadTag tag = new DefaultPhdReadTag("", "source", null, null, "", "");
        
        // Test if the getSource() method returns the correct source
        assertEquals("source", tag.getSource());
    }
    
    @Test
    public void testGetUngappedRange() {
         // Create a Range object
        Range range = new Range(0, 10);
        
        // Create a DefaultPhdReadTag object and set its ungapped range
        DefaultPhdReadTag tag = new DefaultPhdReadTag("", "", range, null, "", "");
        
        // Test if the getUngappedRange() method returns the correct ungapped range
        assertEquals(range, tag.getUngappedRange());
    }
    
    @Test
    public void testGetDate() {
         // Create a Date object
        Date date = new Date();
        
        // Create a DefaultPhdReadTag object and set its date
        DefaultPhdReadTag tag = new DefaultPhdReadTag("", "", null, date, "", "");
        
        // Test if the getDate() method returns the correct date
        assertEquals(date, tag.getDate());
    }
    
    @Test
    public void testGetComment() {
         // Create a DefaultPhdReadTag object and set its comment
        DefaultPhdReadTag tag = new DefaultPhdReadTag("", "", null, null, "comment", "");
        
        // Test if the getComment() method returns the correct comment
        assertEquals("comment", tag.getComment());
    }
    
    @Test
    public void testGetFreeFormData() {
         // Create a DefaultPhdReadTag object and set its free form data
        DefaultPhdReadTag tag = new DefaultPhdReadTag("", "", null, null, "", "freeFormData");
        
        // Test if the getFreeFormData() method returns the correct free form data
        assertEquals("freeFormData", tag.getFreeFormData());
    }
    
    @Test
    public void testHashCode() {
        // Create two DefaultPhdReadTag objects with the same values
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", null, null, "comment", "freeFormData");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", null, null, "comment", "freeFormData");
        
        // Test if the hashCode() method returns the same value for equal objects
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }
    
    @Test
    public void testEquals() {
        // Create two DefaultPhdReadTag objects with the same values
        DefaultPhdReadTag tag1 = new DefaultPhdReadTag("type", "source", null, null, "comment", "freeFormData");
        DefaultPhdReadTag tag2 = new DefaultPhdReadTag("type", "source", null, null, "comment", "freeFormData");
        
        // Test if the equals() method returns true for equal objects
        assertTrue(tag1.equals(tag2));
    }
}
