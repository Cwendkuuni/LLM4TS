
package de.paragon.explorer.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompareBothNull() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, null);
        assertEquals(0, result);
    }

    @Test
    public void testCompareFirstNull() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model = new AttributeModel("test");
        int result = comparator.compare(null, model);
        assertTrue(result < 0);
    }

    @Test
    public void testCompareSecondNull() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model = new AttributeModel("test");
        int result = comparator.compare(model, null);
        assertTrue(result > 0);
    }

    @Test
    public void testCompareSameName() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("test");
        AttributeModel model2 = new AttributeModel("test");
        int result = comparator.compare(model1, model2);
        assertEquals(0, result);
    }

}
