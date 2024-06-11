import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        // Test case 1: Compare two null objects
        int result1 = comparator.compare(null, null);
        assertEquals(0, result1);
        
        // Test case 2: Compare a null object with a non-null object
        AttributeModel attribute1 = new AttributeModel("Test");
        int result2 = comparator.compare(null, attribute1);
        assertTrue(result2 < 0);
        
        // Test case 3: Compare a non-null object with a null object
        AttributeModel attribute2 = new AttributeModel("Test");
        int result3 = comparator.compare(attribute2, null);
        assertTrue(result3 > 0);
        
        // Test case 4: Compare two objects with equal names
        AttributeModel attribute3 = new AttributeModel("Test");
        AttributeModel attribute4 = new AttributeModel("Test");
        int result4 = comparator.compare(attribute3, attribute4);
        assertEquals(0, result4);
        
        // Test case 5: Compare two objects with different names
        AttributeModel attribute5 = new AttributeModel("Test1");
        AttributeModel attribute6 = new AttributeModel("Test2");
        int result5 = comparator.compare(attribute5, attribute6);
        assertTrue(result5 < 0);
        
        // Test case 6: Compare two objects with names starting with "a"
        AttributeModel attribute7 = new AttributeModel("Test");
        AttributeModel attribute8 = new AttributeModel("apple");
        int result6 = comparator.compare(attribute7, attribute8);
        assertTrue(result6 < 0);
        
        // Test case 7: Compare two objects with names not starting with "a"
        AttributeModel attribute9 = new AttributeModel("apple");
        AttributeModel attribute10 = new AttributeModel("Test");
        int result7 = comparator.compare(attribute9, attribute10);
        assertTrue(result7 > 0);
    }
}

