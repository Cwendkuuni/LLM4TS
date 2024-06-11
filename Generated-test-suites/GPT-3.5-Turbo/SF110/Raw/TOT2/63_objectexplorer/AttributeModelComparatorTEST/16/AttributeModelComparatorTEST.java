import static org.junit.Assert.*;
import org.junit.Test;

public class AttributeModelComparatorTest {
    
    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        // Typical use case
        AttributeModel attribute1 = new AttributeModel("Apple");
        AttributeModel attribute2 = new AttributeModel("Banana");
        assertEquals(-1, comparator.compare(attribute1, attribute2));
        
        // Edge case where both attributes have same name
        AttributeModel attribute3 = new AttributeModel("Apple");
        assertEquals(0, comparator.compare(attribute1, attribute3));
        
        // Edge case where one attribute is null
        assertNull(comparator.compare(attribute1, null));
        
        // Exception handling scenario
        try {
            comparator.compare(attribute1, new AttributeModel(null));
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Exception expected
        }
    }
}