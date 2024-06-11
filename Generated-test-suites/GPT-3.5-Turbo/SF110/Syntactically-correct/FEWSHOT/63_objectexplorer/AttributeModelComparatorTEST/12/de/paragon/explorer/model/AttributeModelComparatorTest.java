package de.paragon.explorer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class AttributeModelComparatorTest {
    private AttributeModelComparator comparator;

    @Before
    public void setUp() {
        comparator = new AttributeModelComparator();
    }

    @Test
    public void testCompare() {
        AttributeModel a1 = new AttributeModel("A");
        AttributeModel a2 = new AttributeModel("A");
        AttributeModel b = new AttributeModel("B");
        
        // Test when o1 and o2 are null
        assertEquals(0, comparator.compare(null, null));
        
        // Test when o1 is null and o2 is not null
        assertTrue(comparator.compare(null, a1) < 0);
        
        // Test when o1 is not null and o2 is null
        assertTrue(comparator.compare(a1, null) > 0);
        
        // Test when o1 and o2 are not null and have the same name
        assertEquals(0, comparator.compare(a1, a2));
        
        // Test when o1 and o2 are not null and o1's name is before "a" and o2's name is after "a"
        assertTrue(comparator.compare(a1, b) < 0);
        
        // Test when o1 and o2 are not null and o1's name is after "a" and o2's name is before "a"
        assertTrue(comparator.compare(b, a1) > 0);
    }
}