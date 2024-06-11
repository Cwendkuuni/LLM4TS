package de.paragon.explorer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import java.util.*;

public class AttributeModelComparatorTest {
    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        // Testing when both attributes are null
        int result1 = comparator.compare(null, null);
        assertEquals(0, result1);
        
        // Testing when o1 is null and o2 is not null
        AttributeModel o2 = new AttributeModel();
        int result2 = comparator.compare(null, o2);
        assertEquals(-1, result2);
        
        // Testing when o1 is not null and o2 is null
        AttributeModel o1 = new AttributeModel();
        int result3 = comparator.compare(o1, null);
        assertEquals(1, result3);
        
        // Testing when both attributes have same name
        o1.setName("Test");
        o2.setName("Test");
        int result4 = comparator.compare(o1, o2);
        assertEquals(0, result4);
        
        // Testing when o1 name starts with 'a' and o2 name does not start with 'a'
        o1.setName("Apple");
        o2.setName("Banana");
        int result5 = comparator.compare(o1, o2);
        assertEquals(-1, result5);
        
        // Testing when o1 name does not start with 'a' and o2 name starts with 'a'
        o1.setName("Banana");
        o2.setName("Apple");
        int result6 = comparator.compare(o1, o2);
        assertEquals(1, result6);
        
        // Testing when o1 name comes before o2 name alphabetically
        o1.setName("Apple");
        o2.setName("Banana");
        int result7 = comparator.compare(o1, o2);
        assertEquals(-1, result7);
        
        // Testing when o1 name comes after o2 name alphabetically
        o1.setName("Banana");
        o2.setName("Apple");
        int result8 = comparator.compare(o1, o2);
        assertEquals(1, result8);
    }
}