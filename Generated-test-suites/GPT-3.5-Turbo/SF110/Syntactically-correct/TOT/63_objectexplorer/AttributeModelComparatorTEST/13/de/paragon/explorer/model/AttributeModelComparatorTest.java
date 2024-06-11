package de.paragon.explorer.model;

import org.junit.Test;
import java.util.*;
import java.io.*;

import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    // Test cases for compare method
    @Test
    public void testCompare_NullObjects_ReturnsZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, null);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_FirstObjectNull_ReturnsNegativeValue() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel = new AttributeModel("TestAttribute", 10.0);
        int result = comparator.compare(null, attributeModel);
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_SecondObjectNull_ReturnsPositiveValue() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel = new AttributeModel("TestAttribute", 10.0);
        int result = comparator.compare(attributeModel, null);
        assertTrue(result > 0);
    }

    @Test
    public void testCompare_BothObjectsNull_ReturnsZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, null);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_SameObject_ReturnsZero() {
        AttributeModel attributeModel = new AttributeModel("TestAttribute", 10.0);
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attributeModel, attributeModel);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_DifferentNames_ReturnsCorrectValue() {
        AttributeModel attributeModel1 = new AttributeModel("AttributeA", 10.0);
        AttributeModel attributeModel2 = new AttributeModel("AttributeB", 10.0);
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_SameNames_ReturnsZero() {
        AttributeModel attributeModel1 = new AttributeModel("AttributeA", 10.0);
        AttributeModel attributeModel2 = new AttributeModel("AttributeA", 5.0);
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_DifferentNamesWithA_ReturnsCorrectValue() {
        AttributeModel attributeModel1 = new AttributeModel("AttributeA", 10.0);
        AttributeModel attributeModel2 = new AttributeModel("AttributeB", 10.0);
        attributeModel1.setName("ZattributeA");
        attributeModel2.setName("ZattributeB");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_SameNamesWithA_ReturnsZero() {
        AttributeModel attributeModel1 = new AttributeModel("AttributeA", 10.0);
        AttributeModel attributeModel2 = new AttributeModel("AttributeA", 5.0);
        attributeModel1.setName("ZattributeA");
        attributeModel2.setName("ZattributeA");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(0, result);
    }

    // Additional test cases for potential edge cases and error scenarios can be added here

}