import static org.junit.Assert.*;
import org.junit.Test;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare() {
        AttributeModel model1 = new AttributeModel("abc");
        AttributeModel model2 = new AttributeModel("def");
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        // Typical case
        int result = comparator.compare(model1, model2);
        assertTrue(result > 0);
        
        // Edge case: Both AttributeModel objects are null
        result = comparator.compare(null, null);
        assertEquals(-1, result);
        
        // Edge case: One AttributeModel object is null
        result = comparator.compare(model1, null);
        assertEquals(-1, result);
        
        // Edge case: Comparing with null object
        try {
            comparator.compare(model1, null); // This should throw NullPointerException
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertTrue(true);
        }
        
        // Error scenario: AttributeModel objects with same name
        model2 = new AttributeModel("abc");
        result = comparator.compare(model1, model2);
        assertEquals(0, result);
    }
}