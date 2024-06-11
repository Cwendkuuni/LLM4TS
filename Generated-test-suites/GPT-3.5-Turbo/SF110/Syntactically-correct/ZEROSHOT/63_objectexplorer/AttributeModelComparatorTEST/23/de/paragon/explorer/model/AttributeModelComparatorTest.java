package de.paragon.explorer.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_withNullObjects_shouldReturnNegative() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, null);
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_withNullObject_shouldReturnPositive() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel();
        int result = comparator.compare(o1, null);
        assertTrue(result > 0);
    }

    @Test
    public void testCompare_withNullName_shouldReturnPositive() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o2 = new AttributeModel();
        int result = comparator.compare(null, o2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_withEqualNames_shouldReturnZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel();
        o1.setName("test");
        AttributeModel o2 = new AttributeModel();
        o2.setName("test");
        int result = comparator.compare(o1, o2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_withDifferentNames_shouldReturnNegative() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel();
        o1.setName("abc");
        AttributeModel o2 = new AttributeModel();
        o2.setName("def");
        int result = comparator.compare(o1, o2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_withDifferentNamesAndAFirstName_shouldReturnNegative() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel();
        o1.setName("def");
        AttributeModel o2 = new AttributeModel();
        o2.setName("abc");
        int result = comparator.compare(o1, o2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_withDifferentNamesAndAFirstName_shouldReturnPositive() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel();
        o1.setName("abc");
        AttributeModel o2 = new AttributeModel();
        o2.setName("def");
        int result = comparator.compare(o1, o2);
        assertTrue(result > 0);
    }
}