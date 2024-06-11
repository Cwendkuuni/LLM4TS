package de.paragon.explorer.model;

import static org.junit.Assert.assertEquals;
import java.util.Comparator;

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
        AttributeModel a1 = new AttributeModel("A", "Value");
        AttributeModel a2 = new AttributeModel("B", "Value");
        AttributeModel a3 = new AttributeModel("C", "Value");

        // Test when both attributes are null
        assertEquals(0, comparator.compare(null, null));

        // Test when one attribute is null
        assertEquals(-1, comparator.compare(null, a1));
        assertEquals(1, comparator.compare(a1, null));

        // Test when both attribute names are the same
        assertEquals(0, comparator.compare(a1, a1));

        // Test when o1 has a name less than "a" and o2 has a name greater than or equal to "a"
        assertEquals(1, comparator.compare(a1, a2));
        assertEquals(1, comparator.compare(a1, a3));

        // Test when o2 has a name less than "a" and o1 has a name greater than or equal to "a"
        assertEquals(-1, comparator.compare(a2, a1));
        assertEquals(-1, comparator.compare(a3, a1));

        // Test when both attribute names are greater than "a"
        assertEquals(-1, comparator.compare(a2, a3));
        assertEquals(1, comparator.compare(a3, a2));
    }
}