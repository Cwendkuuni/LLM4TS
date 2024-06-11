package de.paragon.explorer.model;

import org.junit.Test;
import org.junit.Assert;
import java.util.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_NullObjects_ReturnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, null);
        Assert.assertEquals(-1, result);
    }

    @Test
    public void testCompare_NullObjectAndNonNullObject_ReturnsNegativeOne() {
        AttributeModel o1 = null;
        AttributeModel o2 = new AttributeModel("Test");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(o1, o2);
        Assert.assertEquals(-1, result);
    }

    @Test
    public void testCompare_NonNullObjectAndNullObject_ReturnsOne() {
        AttributeModel o1 = new AttributeModel("Test");
        AttributeModel o2 = null;
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(o1, o2);
        Assert.assertEquals(1, result);
    }

    @Test
    public void testCompare_BothObjectsNull_ReturnsZero() {
        AttributeModel o1 = null;
        AttributeModel o2 = null;
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(o1, o2);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testCompare_EqualNamesIgnoreCase_ReturnsZero() {
        AttributeModel o1 = new AttributeModel("test");
        AttributeModel o2 = new AttributeModel("TEST");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(o1, o2);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testCompare_NonEqualNamesIgnoreCase_ReturnsNegativeValue() {
        AttributeModel o1 = new AttributeModel("abc");
        AttributeModel o2 = new AttributeModel("def");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(o1, o2);
        Assert.assertTrue(result < 0);
    }

    @Test
    public void testCompare_NameOneIsA_ReturnsOne() {
        AttributeModel o1 = new AttributeModel("a");
        AttributeModel o2 = new AttributeModel("b");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(o1, o2);
        Assert.assertEquals(1, result);
    }

    @Test
    public void testCompare_NameTwoIsA_ReturnsNegativeOne() {
        AttributeModel o1 = new AttributeModel("b");
        AttributeModel o2 = new AttributeModel("a");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(o1, o2);
        Assert.assertEquals(-1, result);
    }

    @Test
    public void testCompare_NameOneIsAAndNameTwoIsNot_ReturnsOne() {
        AttributeModel o1 = new AttributeModel("a");
        AttributeModel o2 = new AttributeModel("c");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(o1, o2);
        Assert.assertEquals(1, result);
    }

    @Test
    public void testCompare_NameTwoIsAAndNameOneIsNot_ReturnsNegativeOne() {
        AttributeModel o1 = new AttributeModel("c");
        AttributeModel o2 = new AttributeModel("a");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(o1, o2);
        Assert.assertEquals(-1, result);
    }

    @Test
    public void testCompare_EqualNamesNotStartingWithA_ReturnsNegativeValue() {
        AttributeModel o1 = new AttributeModel("test");
        AttributeModel o2 = new AttributeModel("sample");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(o1, o2);
        Assert.assertTrue(result < 0);
    }

    @Test
    public void testCompare_BothNamesStartingWithA_ReturnsZero() {
        AttributeModel o1 = new AttributeModel("a");
        AttributeModel o2 = new AttributeModel("a");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(o1, o2);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testCompare_EqualNamesStartingWithA_ReturnsZero() {
        AttributeModel o1 = new AttributeModel("apple");
        AttributeModel o2 = new AttributeModel("apple");
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(o1, o2);
        Assert.assertEquals(0, result);
    }

}