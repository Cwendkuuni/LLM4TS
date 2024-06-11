package de.paragon.explorer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class AttributeModelComparatorTest {
    
    @Test
    public void testCompare() {
        AttributeModelComparator comp = new AttributeModelComparator();
        
        // Test when both objects are null
        assertEquals(0, comp.compare(null, null));
        
        // Test when o1 is null and o2 is not null
        AttributeModel o2 = new AttributeModel();
        o2.setName("b");
        assertEquals(-1, comp.compare(null, o2));
        
        // Test when o1 is not null and o2 is null
        AttributeModel o1 = new AttributeModel();
        o1.setName("b");
        assertEquals(1, comp.compare(o1, null));
        
        // Test when both objects are not null
        // Test when names are equal
        o1.setName("a");
        o2.setName("a");
        assertEquals(0, comp.compare(o1, o2));
        
        // Test when name of o1 is less than name of o2
        o1.setName("a");
        o2.setName("b");
        assertEquals(-1, comp.compare(o1, o2));
        
        // Test when name of o1 is greater than name of o2
        o1.setName("b");
        o2.setName("a");
        assertEquals(1, comp.compare(o1, o2));
        
        // Test when names are not equal and name of o1 is less than "a"
        o1.setName("a");
        o2.setName("c");
        assertEquals(-1, comp.compare(o1, o2));
        
        // Test when names are not equal and name of o2 is less than "a"
        o1.setName("c");
        o2.setName("a");
        assertEquals(1, comp.compare(o1, o2));
    }
}