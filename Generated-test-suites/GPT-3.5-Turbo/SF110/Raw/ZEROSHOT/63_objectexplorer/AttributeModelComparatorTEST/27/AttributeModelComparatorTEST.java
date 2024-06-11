package de.paragon.explorer.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();

        // Test case 1: o1 is null
        AttributeModel o1 = null;
        AttributeModel o2 = new AttributeModel("Test");
        int result = comparator.compare(o1, o2);
        assertEquals(-1, result);
        
        // Test case 2: o2 is null
        o1 = new AttributeModel("Test");
        o2 = null;
        result = comparator.compare(o1, o2);
        assertEquals(1, result);
        
        // Test case 3: o1 and o2 are null
        o1 = null;
        o2 = null;
        result = comparator.compare(o1, o2);
        assertEquals(-1, result);
        
        // Test case 4: o1 and o2 have same name
        o1 = new AttributeModel("Test");
        o2 = new AttributeModel("Test");
        result = comparator.compare(o1, o2);
        assertEquals(0, result);
        
        // Test case 5: o1 and o2 have different names
        o1 = new AttributeModel("Apple");
        o2 = new AttributeModel("Banana");
        result = comparator.compare(o1, o2);
        assertTrue(result < 0);
        
        // Test case 6: o1 is greater than o2
        o1 = new AttributeModel("Zebra");
        o2 = new AttributeModel("Apple");
        result = comparator.compare(o1, o2);
        assertTrue(result > 0);
        
        // Test case 7: o1 is less than o2 and contains "a" in name
        o1 = new AttributeModel("aba");
        o2 = new AttributeModel("banana");
        result = comparator.compare(o1, o2);
        assertEquals(1, result);
        
        // Test case 8: o2 is less than o1 and contains "a" in name
        o1 = new AttributeModel("banana");
        o2 = new AttributeModel("aba");
        result = comparator.compare(o1, o2);
        assertEquals(-1, result);
    }
}