package de.paragon.explorer.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_NullObjects_ReturnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, null);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_FirstObjectNull_ReturnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel obj2 = new AttributeModel("TestName");
        int result = comparator.compare(null, obj2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_SecondObjectNull_ReturnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel obj1 = new AttributeModel("TestName");
        int result = comparator.compare(obj1, null);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_SameObjectNames_ReturnsZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel obj1 = new AttributeModel("TestName");
        AttributeModel obj2 = new AttributeModel("testname");
        int result = comparator.compare(obj1, obj2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_FirstObjectBeforeA_ReturnsOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel obj1 = new AttributeModel("Apple");
        AttributeModel obj2 = new AttributeModel("Banana");
        int result = comparator.compare(obj1, obj2);
        assertEquals(1, result);
    }

    @Test
    public void testCompare_SecondObjectBeforeA_ReturnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel obj1 = new AttributeModel("Banana");
        AttributeModel obj2 = new AttributeModel("Apple");
        int result = comparator.compare(obj1, obj2);
        assertEquals(-1, result);
    }
}