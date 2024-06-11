package de.paragon.explorer.model;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Comparator;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();

        AttributeModel attribute1 = new AttributeModel("A");
        AttributeModel attribute2 = new AttributeModel("B");

        int result = comparator.compare(attribute1, attribute2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareIgnoreCase() {
        AttributeModelComparator comparator = new AttributeModelComparator();

        AttributeModel attribute1 = new AttributeModel("A");
        AttributeModel attribute2 = new AttributeModel("b");

        int result = comparator.compare(attribute1, attribute2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareNull() {
        AttributeModelComparator comparator = new AttributeModelComparator();

        AttributeModel attribute1 = null;
        AttributeModel attribute2 = new AttributeModel("B");

        int result = comparator.compare(attribute1, attribute2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareBothNull() {
        AttributeModelComparator comparator = new AttributeModelComparator();

        AttributeModel attribute1 = null;
        AttributeModel attribute2 = null;

        int result = comparator.compare(attribute1, attribute2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareBothA() {
        AttributeModelComparator comparator = new AttributeModelComparator();

        AttributeModel attribute1 = new AttributeModel("A");
        AttributeModel attribute2 = new AttributeModel("A");

        int result = comparator.compare(attribute1, attribute2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareBothANegative() {
        AttributeModelComparator comparator = new AttributeModelComparator();

        AttributeModel attribute1 = new AttributeModel("A");
        AttributeModel attribute2 = new AttributeModel("B");

        int result = comparator.compare(attribute1, attribute2);
        assertNotEquals(1, result);
    }

    @Test
    public void testCompareBothAPositive() {
        AttributeModelComparator comparator = new AttributeModelComparator();

        AttributeModel attribute1 = new AttributeModel("B");
        AttributeModel attribute2 = new AttributeModel("A");

        int result = comparator.compare(attribute1, attribute2);
        assertNotEquals(-1, result);
    }
}