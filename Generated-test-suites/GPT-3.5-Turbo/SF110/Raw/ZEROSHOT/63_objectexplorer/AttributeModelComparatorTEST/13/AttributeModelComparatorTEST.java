package de.paragon.explorer.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare() {
        // Test case 1: Both attributes are null
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, null);
        assertEquals(0, result);
        
        // Test case 2: First attribute is null, second attribute is not null
        AttributeModel attribute1 = null;
        AttributeModel attribute2 = new AttributeModel("Example");
        result = comparator.compare(attribute1, attribute2);
        assertEquals(-1, result);
        
        // Test case 3: First attribute is not null, second attribute is null
        attribute1 = new AttributeModel("Example");
        attribute2 = null;
        result = comparator.compare(attribute1, attribute2);
        assertEquals(1, result);
        
        // Test case 4: Both attributes have same name
        attribute1 = new AttributeModel("Example");
        attribute2 = new AttributeModel("Example");
        result = comparator.compare(attribute1, attribute2);
        assertEquals(0, result);
        
        // Test case 5: First attribute name comes before "a" and second attribute name comes after "a"
        attribute1 = new AttributeModel("Apple");
        attribute2 = new AttributeModel("Banana");
        result = comparator.compare(attribute1, attribute2);
        assertEquals(-1, result);
        
        // Test case 6: First attribute name comes after "a" and second attribute name comes before "a"
        attribute1 = new AttributeModel("Banana");
        attribute2 = new AttributeModel("Apple");
        result = comparator.compare(attribute1, attribute2);
        assertEquals(1, result);
        
        // Additional test cases can be added here to further test all scenarios
    }
}