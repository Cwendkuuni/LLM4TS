
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.jcvi.jillion.assembly.consed.phd.PhdReadTag;
import org.jcvi.jillion.core.Range;
import java.util.Date;
import org.junit.Test;

public class DefaultPhdReadTagTest {
    
    // Constructor
    @Test
    public void testDefaultPhdReadTagConstructor() {
        // Generate sample input
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 1);
        Date date = new Date();
        String comment = "comment";
        String freeFormData = "freeFormData";

        // Create object using constructor
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        // Check if object is not null
        assertNotNull(phdReadTag);
        // Check if the values are set correctly
        assertEquals(type, phdReadTag.getType());
        assertEquals(source, phdReadTag.getSource());
        assertEquals(ungappedRange, phdReadTag.getUngappedRange());
        assertEquals(date, phdReadTag.getDate());
        assertEquals(comment, phdReadTag.getComment());
        assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }
    
    // getType method
    @Test
    public void testGetType() {
        // Generate sample input
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 1);
        Date date = new Date();
        String comment = "comment";
        String freeFormData = "freeFormData";

        // Create object using constructor
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        // Check if getType() returns the correct value
        assertEquals(type, phdReadTag.getType());
    }
    
    // getSource method
    @Test
    public void testGetSource() {
        // Generate sample input
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 1);
        Date date = new Date();
        String comment = "comment";
        String freeFormData = "freeFormData";

        // Create object using constructor
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        // Check if getSource() returns the correct value
        assertEquals(source, phdReadTag.getSource());
    }
    
    // getUngappedRange method
    @Test
    public void testGetUngappedRange() {
        // Generate sample input
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 1);
        Date date = new Date();
        String comment = "comment";
        String freeFormData = "freeFormData";

        // Create object using constructor
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        // Check if getUngappedRange() returns the correct value
        assertEquals(ungappedRange, phdReadTag.getUngappedRange());
    }
    
    // getDate method
    @Test
    public void testGetDate() {
        // Generate sample input
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 1);
        Date date = new Date();
        String comment = "comment";
        String freeFormData = "freeFormData";

        // Create object using constructor
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        // Check if getDate() returns the correct value
        assertEquals(date, phdReadTag.getDate());
    }
    
    // getComment method
    @Test
    public void testGetComment() {
        // Generate sample input
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 1);
        Date date = new Date();
        String comment = "comment";
        String freeFormData = "freeFormData";

        // Create object using constructor
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        // Check if getComment() returns the correct value
        assertEquals(comment, phdReadTag.getComment());
    }
    
    // getFreeFormData method
    @Test
    public void testGetFreeFormData() {
        // Generate sample input
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 1);
        Date date = new Date();
        String comment = "comment";
        String freeFormData = "freeFormData";

        // Create object using constructor
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        // Check if getFreeFormData() returns the correct value
        assertEquals(freeFormData, phdReadTag.getFreeFormData());
    }
    
    // hashCode method
    @Test
    public void testHashCode() {
        // Generate sample input
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 1);
        Date date = new Date();
        String comment = "comment";
        String freeFormData = "freeFormData";

        // Create object using constructor
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        // Check if hashCode() returns the correct value
        assertEquals(phdReadTag.hashCode(), phdReadTag.hashCode());
    }
    
    // equals method
    @Test
    public void testEquals() {
        // Generate sample input
        String type = "type";
        String source = "source";
        Range ungappedRange = new Range(1, 1);
        Date date = new Date();
        String comment = "comment";
        String freeFormData = "freeFormData";

        // Create object using constructor
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);
        
        // Create another object with the same values
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag(type, source, ungappedRange, date, comment, freeFormData);

        // Check if equals() returns true when called with the same object
        assertEquals(phdReadTag1.equals(phdReadTag1), true);
        // Check if equals() returns true when called with an equal object
        assertEquals(phdReadTag1.equals(phdReadTag2), true);
    }
}

