package de.paragon.explorer.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare() {
        AttributeModel a1 = new AttributeModel("abc");
        AttributeModel a2 = new AttributeModel("def");
        AttributeModel a3 = new AttributeModel("ghi");

        AttributeModelComparator comparator = new AttributeModelComparator();

        // Test with both attributes being null
        int result = comparator.compare(null, null);
        assertEquals(0, result);

        // Test with one attribute being null
        result = comparator.compare(a1, null);
        assertEquals(-1, result);

        result = comparator.compare(null, a1);
        assertEquals(1, result);

        // Test with both attributes having same name
        result = comparator.compare(a1, a1);
        assertEquals(0, result);

        // Test with both attributes having different name
        result = comparator.compare(a1, a2);
        assertEquals(-1, result);

        result = comparator.compare(a2, a1);
        assertEquals(1, result);

        // Test with one attribute starting with "a" and the other not
        result = comparator.compare(a1, a3);
        assertEquals(-1, result);

        result = comparator.compare(a3, a1);
        assertEquals(1, result);
    }
}