package de.paragon.explorer.model;

import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AttributeModelComparatorTest {

    private AttributeModelComparator comparator;

    @Before
    public void setup() {
        comparator = new AttributeModelComparator();
    }

    @Test
    public void testCompare_withNullParameters_shouldReturnNegativeValue() {
        int result = comparator.compare(null, null);
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_withNullParameter1_shouldReturnNegativeValue() {
        AttributeModel model2 = new AttributeModel();
        int result = comparator.compare(null, model2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_withNullParameter2_shouldReturnPositiveValue() {
        AttributeModel model1 = new AttributeModel();
        int result = comparator.compare(model1, null);
        assertTrue(result > 0);
    }

    @Test
    public void testCompare_withEqualNames_shouldReturnZero() {
        AttributeModel model1 = new AttributeModel();
        model1.setName("a");
        AttributeModel model2 = new AttributeModel();
        model2.setName("a");
        int result = comparator.compare(model1, model2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_withDifferentNames_shouldReturnCorrectValue() {
        AttributeModel model1 = new AttributeModel();
        model1.setName("b");
        AttributeModel model2 = new AttributeModel();
        model2.setName("a");
        int result = comparator.compare(model1, model2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_withOneNameStartingWithA_shouldReturnCorrectValue() {
        AttributeModel model1 = new AttributeModel();
        model1.setName("a");
        AttributeModel model2 = new AttributeModel();
        model2.setName("b");
        int result = comparator.compare(model1, model2);
        assertTrue(result > 0);
    }

    @Test
    public void testCompare_withBothNamesStartingWithA_shouldReturnCorrectValue() {
        AttributeModel model1 = new AttributeModel();
        model1.setName("a");
        AttributeModel model2 = new AttributeModel();
        model2.setName("a");
        int result = comparator.compare(model1, model2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_withBothNamesNotStartingWithA_shouldReturnCorrectValue() {
        AttributeModel model1 = new AttributeModel();
        model1.setName("b");
        AttributeModel model2 = new AttributeModel();
        model2.setName("c");
        int result = comparator.compare(model1, model2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_withOneNameStartingWithAAndOneNotStartingWithA_shouldReturnCorrectValue() {
        AttributeModel model1 = new AttributeModel();
        model1.setName("a");
        AttributeModel model2 = new AttributeModel();
        model2.setName("b");
        int result = comparator.compare(model1, model2);
        assertTrue(result > 0);
    }

}