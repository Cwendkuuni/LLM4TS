package de.paragon.explorer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class AttributeModelComparatorTest {
    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();

        // Test when both inputs are null
        int result = comparator.compare(null, null);
        assertEquals(0, result);

        // Test when o1 is null and o2 is not null
        AttributeModel o2 = new AttributeModel();
        result = comparator.compare(null, o2);
        assertEquals(-1, result);

        // Test when o1 is not null and o2 is null
        AttributeModel o1 = new AttributeModel();
        result = comparator.compare(o1, null);
        assertEquals(1, result);

        // Test when both inputs are not null

        // Test when o1 and o2 have the same name
        o1.setName("abc");
        o2.setName("abc");
        result = comparator.compare(o1, o2);
        assertEquals(0, result);

        // Test when o1's name comes before "a" and o2's name comes after "a"
        o1.setName("abc");
        o2.setName("b");
        result = comparator.compare(o1, o2);
        assertEquals(-1, result);

        // Test when o1's name comes after "a" and o2's name comes before "a"
        o1.setName("b");
        o2.setName("abc");
        result = comparator.compare(o1, o2);
        assertEquals(1, result);

        // Test when o1 and o2 have different names
        o1.setName("abc");
        o2.setName("def");
        result = comparator.compare(o1, o2);
        assertEquals(-1, result);

        o1.setName("def");
        o2.setName("abc");
        result = comparator.compare(o1, o2);
        assertEquals(1, result);
    }
}