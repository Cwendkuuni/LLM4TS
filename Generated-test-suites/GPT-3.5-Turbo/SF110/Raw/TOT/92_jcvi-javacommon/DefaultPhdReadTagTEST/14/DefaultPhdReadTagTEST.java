import java.util.Date;
import org.jcvi.jillion.core.Range;
import org.junit.Assert;
import org.junit.Test;

public class DefaultPhdReadTagTest {

    // Test case for getType method
    @Test
    public void testGetType() {
        // Create a DefaultPhdReadTag object
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        
        // Verify that the getType method returns the correct value
        Assert.assertEquals("type", phdReadTag.getType());
    }
    
    // Test case for getSource method
    @Test
    public void testGetSource() {
        // Create a DefaultPhdReadTag object
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        
        // Verify that the getSource method returns the correct value
        Assert.assertEquals("source", phdReadTag.getSource());
    }
    
    // Test case for getUngappedRange method
    @Test
    public void testGetUngappedRange() {
        // Create a DefaultPhdReadTag object
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        
        // Verify that the getUngappedRange method returns the correct value
        Assert.assertEquals(new Range(1, 10), phdReadTag.getUngappedRange());
    }
    
    // Test case for getDate method
    @Test
    public void testGetDate() {
        // Create a Date object
        Date date = new Date();
        
        // Create a DefaultPhdReadTag object
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("type", "source", new Range(1, 10), date, "comment", "freeFormData");
        
        // Verify that the getDate method returns the correct value
        Assert.assertEquals(date, phdReadTag.getDate());
    }
    
    // Test case for getComment method
    @Test
    public void testGetComment() {
        // Create a DefaultPhdReadTag object
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        
        // Verify that the getComment method returns the correct value
        Assert.assertEquals("comment", phdReadTag.getComment());
    }
    
    // Test case for getFreeFormData method
    @Test
    public void testGetFreeFormData() {
        // Create a DefaultPhdReadTag object
        DefaultPhdReadTag phdReadTag = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        
        // Verify that the getFreeFormData method returns the correct value
        Assert.assertEquals("freeFormData", phdReadTag.getFreeFormData());
    }
    
    // Test case for hashCode method
    @Test
    public void testHashCode() {
        // Create two DefaultPhdReadTag objects with the same values
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        
        // Verify that their hash codes are equal
        Assert.assertEquals(phdReadTag1.hashCode(), phdReadTag2.hashCode());
    }
    
    // Test case for equals method (when objects are equal)
    @Test
    public void testEqualsEqualObjects() {
        // Create two DefaultPhdReadTag objects with the same values
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag("type", "source", new Range(1, 10), new Date(), "comment", "freeFormData");
        
        // Verify that they are considered equal
        Assert.assertTrue(phdReadTag1.equals(phdReadTag2));
    }
    
    // Test case for equals method (when objects are not equal)
    @Test
    public void testEqualsNotEqualObjects() {
        // Create two DefaultPhdReadTag objects with different values
        DefaultPhdReadTag phdReadTag1 = new DefaultPhdReadTag("type1", "source1", new Range(1, 10), new Date(), "comment", "freeFormData");
        DefaultPhdReadTag phdReadTag2 = new DefaultPhdReadTag("type2", "source2", new Range(1, 10), new Date(), "comment", "freeFormData");
        
        // Verify that they are not considered equal
        Assert.assertFalse(phdReadTag1.equals(phdReadTag2));
    }
    
}