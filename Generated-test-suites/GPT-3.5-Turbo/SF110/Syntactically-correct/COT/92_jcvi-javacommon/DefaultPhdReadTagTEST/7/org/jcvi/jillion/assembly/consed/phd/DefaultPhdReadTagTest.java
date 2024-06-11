import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.jcvi.jillion.core.Range;
import java.util.Date;

public class DefaultPhdReadTagTest {
    
    @Test
    public void testDefaultPhdReadTag() {
        // Initialize test data
        String type = "Type";
        String source = "Source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "Comment";
        String freeFormData = "FreeForm";
        
        // Create instance of DefaultPhdReadTag
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        
        // Perform assertions
        assertNotNull(defaultPhdReadTag);
        assertEquals(type, defaultPhdReadTag.getType());
        assertEquals(source, defaultPhdReadTag.getSource());
        assertEquals(ungappedRange, defaultPhdReadTag.getUngappedRange());
        assertEquals(date, defaultPhdReadTag.getDate());
        assertEquals(comment, defaultPhdReadTag.getComment());
        assertEquals(freeFormData, defaultPhdReadTag.getFreeFormData());
    }
    
    @Test
    public void testGetType() {
        // Initialize test data
        String type = "Type";
        String source = "Source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "Comment";
        String freeFormData = "FreeForm";
        
        // Create instance of DefaultPhdReadTag
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        
        // Perform assertion
        assertEquals(type, defaultPhdReadTag.getType());
    }
    
    @Test
    public void testGetSource() {
        // Initialize test data
        String type = "Type";
        String source = "Source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "Comment";
        String freeFormData = "FreeForm";
        
        // Create instance of DefaultPhdReadTag
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        
        // Perform assertion
        assertEquals(source, defaultPhdReadTag.getSource());
    }
    
    @Test
    public void testGetUngappedRange() {
        // Initialize test data
        String type = "Type";
        String source = "Source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "Comment";
        String freeFormData = "FreeForm";
        
        // Create instance of DefaultPhdReadTag
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        
        // Perform assertion
        assertEquals(ungappedRange, defaultPhdReadTag.getUngappedRange());
    }
    
    @Test
    public void testGetDate() {
        // Initialize test data
        String type = "Type";
        String source = "Source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "Comment";
        String freeFormData = "FreeForm";
        
        // Create instance of DefaultPhdReadTag
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        
        // Perform assertion
        assertEquals(date, defaultPhdReadTag.getDate());
    }
    
    @Test
    public void testGetComment() {
        // Initialize test data
        String type = "Type";
        String source = "Source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "Comment";
        String freeFormData = "FreeForm";
        
        // Create instance of DefaultPhdReadTag
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        
        // Perform assertion
        assertEquals(comment, defaultPhdReadTag.getComment());
    }
    
    @Test
    public void testGetFreeFormData() {
        // Initialize test data
        String type = "Type";
        String source = "Source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "Comment";
        String freeFormData = "FreeForm";
        
        // Create instance of DefaultPhdReadTag
        DefaultPhdReadTag defaultPhdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        
        // Perform assertion
        assertEquals(freeFormData, defaultPhdReadTag.getFreeFormData());
    }
    
    @Test
    public void testHashCode() {
        String type = "Type";
        String source = "Source";
        Range ungappedRange = new Range(1, 10);
        Date date = new Date();
        String comment = "Comment";
        String freeFormData = "FreeForm";
        DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        
        assertEquals(defaultPhdReadTag1.hashCode(), defaultPhdReadTag2.hashCode());
    }
    
    @Test
    public void testEquals() {
        String type = "Type";
        String source = "Source";
        Range ungappedRange1 = new Range(1, 10);
        Range ungappedRange2 = new Range(5, 15);
        Date date = new Date();
        String comment = "Comment";
        String freeFormData = "FreeForm";
        DefaultPhdReadTag defaultPhdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange1, date, comment, freeFormData);
        DefaultPhdReadTag defaultPhdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange1, date, comment, freeFormData);
        DefaultPhdReadTag defaultPhdReadTag3 = new DefaultPhdReadTag("DifferentType", "DifferentSource", ungappedRange2, date, comment, freeFormData);
        
        assertTrue(defaultPhdReadTag1.equals(defaultPhdReadTag2));
        assertTrue(!defaultPhdReadTag1.equals(defaultPhdReadTag3));
    }
}