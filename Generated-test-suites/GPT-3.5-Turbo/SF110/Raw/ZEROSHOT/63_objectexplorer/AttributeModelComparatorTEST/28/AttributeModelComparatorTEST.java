
package de.paragon.explorer.model;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Comparator;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();

        // Test when both objects are null
        int result = comparator.compare(null, null);
        assertEquals(0, result);

        // Test when first object is null and second object is not null
        AttributeModel o2 = new AttributeModel();
        result = comparator.compare(null, o2);
        assertTrue(result < 0);

        // Test when first object is not null and second object is null
        AttributeModel o1 = new AttributeModel();
        result = comparator.compare(o1, null);
        assertTrue(result > 0);

        // Test when both objects have the same name
        o1.setName("name");
        o2.setName("name");
        result = comparator.compare(o1, o2);
        assertEquals(0, result);

        // Test when first object's name is "a" and second object's name is not "a"
        o1.setName("a");
        o2.setName("b");
        result = comparator.compare(o1, o2);
        assertTrue(result < 0);

        // Test when second object's name is "a" and first object's name is not "a"
        o1.setName("b");
        o2.setName("a");
        result = comparator.compare(o1, o2);
        assertTrue(result > 0);

        // Test when both objects have names that come before "a" in alphabetical order
        o1.setName("apple");
        o2.setName("banana");
        result = comparator.compare(o1, o2);
        assertTrue(result < 0);

        // Test when both objects have names that come after "a" in alphabetical order
        o1.setName("zoo");
        o2.setName("yacht");
        result = comparator.compare(o1, o2);
        assertTrue(result > 0);
    }
}
