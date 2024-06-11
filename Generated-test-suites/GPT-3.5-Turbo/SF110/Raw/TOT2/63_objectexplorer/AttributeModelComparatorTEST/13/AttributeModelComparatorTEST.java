import static org.junit.Assert.*;
import org.junit.Test;

public class AttributeModelComparatorTest {
    
    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        // Typical use case
        AttributeModel attribute1 = new AttributeModel("apple");
        AttributeModel attribute2 = new AttributeModel("banana");
        assertEquals(-1, comparator.compare(attribute1, attribute2));
        
        // Edge cases
        // Test when both attributes are null
        assertEquals(-1, comparator.compare(null, null));
        
        // Test when one attribute is null
        assertEquals(1, comparator.compare(attribute1, null));
        
        // Test for equal attribute names
        AttributeModel attribute3 = new AttributeModel("apple");
        AttributeModel attribute4 = new AttributeModel("apple");
        assertEquals(0, comparator.compare(attribute3, attribute4));
        
        // Error scenarios
        // Test for exception handling when attribute name is null
        try {
            AttributeModel attribute5 = new AttributeModel(null);
            AttributeModel attribute6 = new AttributeModel("banana");
            comparator.compare(attribute5, attribute6);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }
}