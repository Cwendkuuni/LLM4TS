import org.junit.*;
import static org.junit.Assert.*;
import org.jcvi.jillion.core.Range;
import java.util.Date;

public class DefaultPhdReadTagTest {

    @Test
    public void testGetType() {
        // Create object
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("Type1", "Source1", new Range(1, 10), new Date(), "Comment1", "FreeForm1");
        
        // Test for correct type
        assertEquals("Type1", phdReadTag.getType());
    }
    
    @Test
    public void testGetSource() {
        // Create object
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("Type2", "Source2", new Range(1, 10), new Date(), "Comment2", "FreeForm2");
        
        // Test for correct source
        assertEquals("Source2", phdReadTag.getSource());
    }
    
    @Test
    public void testGetUngappedRange() {
        // Create object
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("Type3", "Source3", new Range(1, 10), new Date(), "Comment3", "FreeForm3");
        
        // Test for correct ungapped range
        assertEquals(new Range(1, 10), phdReadTag.getUngappedRange());
    }
    
    @Test
    public void testGetDate() {
        // Create object
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("Type4", "Source4", new Range(1, 10), new Date(), "Comment4", "FreeForm4");
        
        // Test for correct date
        assertNotNull(phdReadTag.getDate());
    }
    
    @Test
    public void testGetComment() {
        // Create object
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("Type5", "Source5", new Range(1, 10), new Date(), "Comment5", "FreeForm5");
        
        // Test for correct comment
        assertEquals("Comment5", phdReadTag.getComment());
    }
    
    @Test
    public void testGetFreeFormData() {
        // Create object
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("Type6", "Source6", new Range(1, 10), new Date(), "Comment6", "FreeForm6");
        
        // Test for correct free form data
        assertEquals("FreeForm6", phdReadTag.getFreeFormData());
    }
    
    @Test
    public void testHashCode() {
        // Create objects with same values
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag("Type7", "Source7", new Range(1, 10), new Date(), "Comment7", "FreeForm7");
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag("Type7", "Source7", new Range(1, 10), new Date(), "Comment7", "FreeForm7");
        
        // Test for equal hash codes
        assertEquals(phdReadTag1.hashCode(), phdReadTag2.hashCode());
    }
    
    @Test
    public void testEquals() {
        // Create objects with same values
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag("Type8", "Source8", new Range(1, 10), new Date(), "Comment8", "FreeForm8");
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag("Type8", "Source8", new Range(1, 10), new Date(), "Comment8", "FreeForm8");
        
        // Test for equality
        assertTrue(phdReadTag1.equals(phdReadTag2));
    }

}