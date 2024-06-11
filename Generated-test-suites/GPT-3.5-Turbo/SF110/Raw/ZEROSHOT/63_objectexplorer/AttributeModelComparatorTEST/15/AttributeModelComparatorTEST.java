package de.paragon.explorer.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_whenO1IsNullAndO2IsNull_returnZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int actualResult = comparator.compare(null, null);
        Assert.assertEquals(0, actualResult);
    }

    @Test
    public void testCompare_whenO1IsNullAndO2IsNotNull_returnNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int actualResult = comparator.compare(null, new AttributeModel());
        Assert.assertEquals(-1, actualResult);
    }

    @Test
    public void testCompare_whenO1IsNotNullAndO2IsNull_returnOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int actualResult = comparator.compare(new AttributeModel(), null);
        Assert.assertEquals(1, actualResult);
    }

    @Test
    public void testCompare_whenO1AndO2AreBothNull_returnZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int actualResult = comparator.compare(new AttributeModel(), new AttributeModel());
        Assert.assertEquals(0, actualResult);
    }

    @Test
    public void testCompare_whenO1NameIsEqualToO2Name_returnZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel();
        o1.setName("a");
        AttributeModel o2 = new AttributeModel();
        o2.setName("a");
        int actualResult = comparator.compare(o1, o2);
        Assert.assertEquals(0, actualResult);
    }

    @Test
    public void testCompare_whenO1NameIsLessThanO2Name_returnNegativeValue() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel();
        o1.setName("aa");
        AttributeModel o2 = new AttributeModel();
        o2.setName("ab");
        int actualResult = comparator.compare(o1, o2);
        Assert.assertTrue(actualResult < 0);
    }

    @Test
    public void testCompare_whenO1NameIsGreaterThanO2Name_returnPositiveValue() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel();
        o1.setName("ab");
        AttributeModel o2 = new AttributeModel();
        o2.setName("aa");
        int actualResult = comparator.compare(o1, o2);
        Assert.assertTrue(actualResult > 0);
    }

    @Test
    public void testCompare_whenO1NameIsLessThanAAndO2NameIsGreaterThanOrEqualToA_returnPositiveOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel();
        o1.setName("Aa");
        AttributeModel o2 = new AttributeModel();
        o2.setName("aa");
        int actualResult = comparator.compare(o1, o2);
        Assert.assertEquals(1, actualResult);
    }

    @Test
    public void testCompare_whenO1NameIsGreaterThanAAndO2NameIsLessThanA_returnNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel();
        o1.setName("aa");
        AttributeModel o2 = new AttributeModel();
        o2.setName("Aa");
        int actualResult = comparator.compare(o1, o2);
        Assert.assertEquals(-1, actualResult);
    }
}