package de.paragon.explorer.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import java.util.Comparator;

public class AttributeModelComparatorTest {

    AttributeModelComparator comparator = new AttributeModelComparator();

    @Test
    public void testCompareFirstNull() {
        AttributeModel o1 = null;
        AttributeModel o2 = new AttributeModel("Test");
        int result = comparator.compare(o1, o2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareSecondNull() {
        AttributeModel o1 = new AttributeModel("Test");
        AttributeModel o2 = null;
        int result = comparator.compare(o1, o2);
        assertEquals(1, result);
    }

    @Test
    public void testCompareBothNull() {
        AttributeModel o1 = null;
        AttributeModel o2 = null;
        int result = comparator.compare(o1, o2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareSameName() {
        AttributeModel o1 = new AttributeModel("Test");
        AttributeModel o2 = new AttributeModel("Test");
        int result = comparator.compare(o1, o2);
        assertEquals(0, result);
    }

    @Test
    public void testCompareFirstHasLargerName() {
        AttributeModel o1 = new AttributeModel("ZTest");
        AttributeModel o2 = new AttributeModel("Test");
        int result = comparator.compare(o1, o2);
        assertEquals(1, result);
    }

    @Test
    public void testCompareFirstHasSmallerName() {
        AttributeModel o1 = new AttributeModel("ATest");
        AttributeModel o2 = new AttributeModel("Test");
        int result = comparator.compare(o1, o2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareFirstHasNameWithLessPriority() {
        AttributeModel o1 = new AttributeModel("b");
        AttributeModel o2 = new AttributeModel("a");
        int result = comparator.compare(o1, o2);
        assertEquals(1, result);
    }

    @Test
    public void testCompareFirstHasNameWithMorePriority() {
        AttributeModel o1 = new AttributeModel("a");
        AttributeModel o2 = new AttributeModel("b");
        int result = comparator.compare(o1, o2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareBothHaveNameWithLessPriority() {
        AttributeModel o1 = new AttributeModel("b");
        AttributeModel o2 = new AttributeModel("c");
        int result = comparator.compare(o1, o2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompareBothHaveNameWithMorePriority() {
        AttributeModel o1 = new AttributeModel("c");
        AttributeModel o2 = new AttributeModel("b");
        int result = comparator.compare(o1, o2);
        assertEquals(1, result);
    }
}