
package de.paragon.explorer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AttributeModelComparatorTest {
    private AttributeModelComparator comparator;
    private List<AttributeModel> attributeModels;

    @Before
    public void setup() {
        comparator = new AttributeModelComparator();
        attributeModels = new ArrayList<>();
    }

    @Test
    public void testCompare_FirstAttributeIsNull_ReturnNegativeResult() {
        AttributeModel attributeModel1 = null;
        AttributeModel attributeModel2 = new AttributeModel("b");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_SecondAttributeIsNull_ReturnPositiveResult() {
        AttributeModel attributeModel1 = new AttributeModel("b");
        AttributeModel attributeModel2 = null;
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(result > 0);
    }

    @Test
    public void testCompare_BothAttributesAreNull_ReturnZero() {
        AttributeModel attributeModel1 = null;
        AttributeModel attributeModel2 = null;
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_FirstAttributeNameIsLessThanSecondAttributeName_ReturnNegativeResult() {
        AttributeModel attributeModel1 = new AttributeModel("b");
        AttributeModel attributeModel2 = new AttributeModel("c");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(result < 0);
    }

    @Test
    public void testCompare_FirstAttributeNameIsGreaterThanSecondAttributeName_ReturnPositiveResult() {
        AttributeModel attributeModel1 = new AttributeModel("d");
        AttributeModel attributeModel2 = new AttributeModel("c");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(result > 0);
    }

    @Test
    public void testCompare_FirstAttributeNameIsEqualToSecondAttributeName_ReturnZero() {
        AttributeModel attributeModel1 = new AttributeModel("e");
        AttributeModel attributeModel2 = new AttributeModel("e");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_FirstAttributeIsBeforeTheLetter_A_ReturnPositiveResult() {
        AttributeModel attributeModel1 = new AttributeModel("b");
        AttributeModel attributeModel2 = new AttributeModel("a");
        int result = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(result > 0);
    }

    @Test
    public void testCompare_FirstAttributeIsAfterTheLetter_A_ReturnNegativeResult() {
        AttributeModel attributeModel1 = new AttributeModel("b");
        AttributeModel attributeModel2 = new AttributeModel("c");
        attributeModels.add(attributeModel1);
        attributeModels.add(attributeModel2);
        Collections.sort(attributeModels, comparator);
        assertTrue(attributeModels.get(0).getName().compareToIgnoreCase("a") < 0);
        assertTrue(attributeModels.get(1).getName().compareToIgnoreCase("a") >= 0);
    }
}

