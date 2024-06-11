
package de.paragon.explorer.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompareWhenBothInputsAreNull() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        assertEquals(-1, comparator.compare(null, null));
    }
    
    @Test
    public void testCompareWhenOneInputIsNull() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model = new AttributeModel("test");
        assertEquals(-1, comparator.compare(model, null));
        assertEquals(1, comparator.compare(null, model));
    }
    
    @Test
    public void testCompareWhenBothInputsAreNotNullAndEqual() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("test");
        AttributeModel model2 = new AttributeModel("test");
        assertEquals(0, comparator.compare(model1, model2));
    }
    
    @Test
    public void testCompareWhenBothInputsAreNotNullAndDifferent() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("apple");
        AttributeModel model2 = new AttributeModel("banana");
        assertTrue(comparator.compare(model1, model2) < 0);
        assertTrue(comparator.compare(model2, model1) > 0);
    }
    
    @Test
    public void testCompareWhenBothInputsAreNotNullWithEdgeCase() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("banana");
        AttributeModel model2 = new AttributeModel("ant");
        assertTrue(comparator.compare(model1, model2) > 0);
        assertTrue(comparator.compare(model2, model1) < 0);
    }
}
