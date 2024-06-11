package de.paragon.explorer.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AttributeModelComparatorTest {
    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        AttributeModel a = new AttributeModel("a");
        AttributeModel b = new AttributeModel("b");
        AttributeModel c = new AttributeModel("c");
        AttributeModel d = new AttributeModel("d");
        AttributeModel e = new AttributeModel("e");
        AttributeModel f = new AttributeModel("f");
        
        // Test when o1 and o2 are null
        assertEquals(0, comparator.compare(null, null));
        
        // Test when o1 is null
        assertEquals(-1, comparator.compare(null, b));
        
        // Test when o2 is null
        assertEquals(1, comparator.compare(a, null));
        
        // Test when both o1 and o2 are not null
        assertEquals(0, comparator.compare(a, a));
        assertEquals(-1, comparator.compare(a, b));
        assertEquals(1, comparator.compare(b, a));
        assertEquals(0, comparator.compare(c, c));
        
        // Test when o1 and o2 are "a"
        assertEquals(0, comparator.compare(a, a));
        assertEquals(-1, comparator.compare(a, b));
        assertEquals(-1, comparator.compare(a, c));
        assertEquals(-1, comparator.compare(a, d));
        assertEquals(-1, comparator.compare(a, e));
        assertEquals(-1, comparator.compare(a, f));
        assertEquals(1, comparator.compare(b, a));
        assertEquals(1, comparator.compare(c, a));
        assertEquals(1, comparator.compare(d, a));
        assertEquals(1, comparator.compare(e, a));
        assertEquals(1, comparator.compare(f, a));
    }
}