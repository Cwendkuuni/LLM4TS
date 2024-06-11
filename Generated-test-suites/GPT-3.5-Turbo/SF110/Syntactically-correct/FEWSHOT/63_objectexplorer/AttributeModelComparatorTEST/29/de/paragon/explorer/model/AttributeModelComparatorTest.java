
package de.paragon.explorer.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();

        AttributeModel a = new AttributeModel("apple", "red");
        AttributeModel b = new AttributeModel("banana", "yellow");
        AttributeModel c = new AttributeModel("cherry", "red");
        AttributeModel d = new AttributeModel("date", "brown");

        // Test when both objects are null
        assertEquals(0, comparator.compare(null, null));

        // Test when one object is null and the other is not
        assertTrue(comparator.compare(a, null) > 0);
        assertTrue(comparator.compare(null, a) < 0);

        // Test when both objects are not null
        assertEquals(0, comparator.compare(a, a));

        assertTrue(comparator.compare(a, b) < 0);
        assertTrue(comparator.compare(b, a) > 0);

        assertEquals(0, comparator.compare(a, c));
        assertEquals(0, comparator.compare(c, a));

        assertTrue(comparator.compare(a, d) < 0);
        assertTrue(comparator.compare(d, a) > 0);

        assertTrue(comparator.compare(b, c) < 0);
        assertTrue(comparator.compare(c, b) > 0);

        assertTrue(comparator.compare(b, d) < 0);
        assertTrue(comparator.compare(d, b) > 0);

        assertTrue(comparator.compare(c, d) < 0);
        assertTrue(comparator.compare(d, c) > 0);
    }
}
