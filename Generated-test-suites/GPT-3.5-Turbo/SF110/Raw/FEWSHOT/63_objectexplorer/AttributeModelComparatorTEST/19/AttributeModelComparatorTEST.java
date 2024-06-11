package de.paragon.explorer.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AttributeModelComparatorTest {
    @Test
    public void testCompare() {
        AttributeModel a1 = new AttributeModel("test1");
        AttributeModel a2 = new AttributeModel("test2");
        
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        // Test when both names are equal
        assertEquals(0, comparator.compare(a1, a1));
        
        // Test when name of o1 is less than name of o2
        assertEquals(-1, comparator.compare(a1, a2));
        
        // Test when name of o1 is greater than name of o2
        assertEquals(1, comparator.compare(a2, a1));
        
        // Test when name of o1 is equal to "a" and name of o2 is greater
        a1 = new AttributeModel("a");
        a2 = new AttributeModel("test");
        assertEquals(1, comparator.compare(a1, a2));
        
        // Test when name of o2 is equal to "a" and name of o1 is greater
        assertEquals(-1, comparator.compare(a2, a1));
        
        // Test when both names are equal to "a"
        a1 = new AttributeModel("a");
        a2 = new AttributeModel("a");
        assertEquals(0, comparator.compare(a1, a2));
        
        // Test when both names are null
        a1 = null;
        a2 = null;
        assertEquals(-1, comparator.compare(a1, a2));
        
        // Test when name of o1 is null
        a1 = null;
        a2 = new AttributeModel("test");
        assertEquals(-1, comparator.compare(a1, a2));
        
        // Test when name of o2 is null
        a1 = new AttributeModel("test");
        a2 = null;
        assertEquals(1, comparator.compare(a1, a2));
    }
}