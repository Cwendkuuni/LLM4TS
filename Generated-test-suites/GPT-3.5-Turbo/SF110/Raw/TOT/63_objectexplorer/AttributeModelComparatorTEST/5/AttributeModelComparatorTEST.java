package de.paragon.explorer.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_SameName_ReturnsZero() {
        AttributeModel a1 = new AttributeModel("A");
        AttributeModel a2 = new AttributeModel("A");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(a1, a2);

        assertEquals(0, result);
    }

    @Test
    public void testCompare_FirstNull_ReturnsNegativeOne() {
        AttributeModel a1 = null;
        AttributeModel a2 = new AttributeModel("B");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(a1, a2);

        assertEquals(-1, result);
    }

    @Test
    public void testCompare_SecondNull_ReturnsPositiveOne() {
        AttributeModel a1 = new AttributeModel("A");
        AttributeModel a2 = null;
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(a1, a2);

        assertEquals(1, result);
    }

    @Test
    public void testCompare_BothNull_ReturnsZero() {
        AttributeModel a1 = null;
        AttributeModel a2 = null;
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(a1, a2);

        assertEquals(0, result);
    }

    @Test
    public void testCompare_FirstNameNull_ReturnsNegativeOne() {
        AttributeModel a1 = new AttributeModel(null);
        AttributeModel a2 = new AttributeModel("B");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(a1, a2);

        assertEquals(-1, result);
    }

    @Test
    public void testCompare_SecondNameNull_ReturnsPositiveOne() {
        AttributeModel a1 = new AttributeModel("A");
        AttributeModel a2 = new AttributeModel(null);
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(a1, a2);

        assertEquals(1, result);
    }

    @Test
    public void testCompare_BothNamesNull_ReturnsZero() {
        AttributeModel a1 = new AttributeModel(null);
        AttributeModel a2 = new AttributeModel(null);
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(a1, a2);

        assertEquals(0, result);
    }

    @Test
    public void testCompare_FirstNameBeforeA_ReturnsNegativeOne() {
        AttributeModel a1 = new AttributeModel("B");
        AttributeModel a2 = new AttributeModel("A");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(a1, a2);

        assertEquals(-1, result);
    }

    @Test
    public void testCompare_SecondNameBeforeA_ReturnsPositiveOne() {
        AttributeModel a1 = new AttributeModel("A");
        AttributeModel a2 = new AttributeModel("B");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(a1, a2);

        assertEquals(1, result);
    }

    @Test
    public void testCompare_BothNamesBeforeA_ReturnsZero() {
        AttributeModel a1 = new AttributeModel("A");
        AttributeModel a2 = new AttributeModel("B");
        AttributeModelComparator comparator = new AttributeModelComparator();

        int result = comparator.compare(a1, a2);

        assertEquals(0, result);
    }
}