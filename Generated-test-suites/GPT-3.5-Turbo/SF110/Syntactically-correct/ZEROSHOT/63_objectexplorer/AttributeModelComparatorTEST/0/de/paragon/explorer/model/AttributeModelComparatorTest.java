package de.paragon.explorer.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;

public class AttributeModelComparatorTest {

    @Test
    public void testCompareNullObjects() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, null);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testCompareNullObjectWithNonNullObject() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel = new AttributeModel();
        int result = comparator.compare(null, attributeModel);
        Assert.assertEquals(-1, result);
    }

    @Test
    public void testCompareNonNullObjectWithNullObject() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel attributeModel = new AttributeModel();
        int result = comparator.compare(attributeModel, null);
        Assert.assertEquals(1, result);
    }

    @Test
    public void testCompareEqualObjects() {
        AttributeModel attributeModel1 = new AttributeModel();
        attributeModel1.setName("A");
        AttributeModel attributeModel2 = new AttributeModel();
        attributeModel2.setName("A");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attributeModel1, attributeModel2);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testCompareFirstObjectComesBeforeSecondObject() {
        AttributeModel attributeModel1 = new AttributeModel();
        attributeModel1.setName("A");
        AttributeModel attributeModel2 = new AttributeModel();
        attributeModel2.setName("B");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attributeModel1, attributeModel2);
        Assert.assertEquals(-1, result);
    }

    @Test
    public void testCompareFirstObjectComesAfterSecondObject() {
        AttributeModel attributeModel1 = new AttributeModel();
        attributeModel1.setName("B");
        AttributeModel attributeModel2 = new AttributeModel();
        attributeModel2.setName("A");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attributeModel1, attributeModel2);
        Assert.assertEquals(1, result);
    }

    @Test
    public void testCompareFirstObjectComesBeforeA() {
        AttributeModel attributeModel1 = new AttributeModel();
        attributeModel1.setName("A");
        AttributeModel attributeModel2 = new AttributeModel();
        attributeModel2.setName("B");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attributeModel1, attributeModel2);
        Assert.assertEquals(-1, result);
    }

    @Test
    public void testCompareFirstObjectComesAfterA() {
        AttributeModel attributeModel1 = new AttributeModel();
        attributeModel1.setName("B");
        AttributeModel attributeModel2 = new AttributeModel();
        attributeModel2.setName("A");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attributeModel1, attributeModel2);
        Assert.assertEquals(1, result);
    }

    @Test
    public void testCompareFirstObjectWithNameAComesAfterA() {
        AttributeModel attributeModel1 = new AttributeModel();
        attributeModel1.setName("AA");
        AttributeModel attributeModel2 = new AttributeModel();
        attributeModel2.setName("A");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attributeModel1, attributeModel2);
        Assert.assertEquals(1, result);
    }

    @Test
    public void testCompareFirstObjectWithNameAComesBeforeA() {
        AttributeModel attributeModel1 = new AttributeModel();
        attributeModel1.setName("A");
        AttributeModel attributeModel2 = new AttributeModel();
        attributeModel2.setName("AA");

        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(attributeModel1, attributeModel2);
        Assert.assertEquals(-1, result);
    }
}