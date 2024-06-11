
package de.paragon.explorer.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AttributeModelComparatorTest {
   
    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();

        AttributeModel model1 = new AttributeModel("abc");
        AttributeModel model2 = new AttributeModel("def");

        assertEquals(0, comparator.compare(model1, model1));

        assertEquals(-1, comparator.compare(null, model2));
        assertEquals(1, comparator.compare(model2, null));
        assertEquals(0, comparator.compare(null, null));

        assertEquals(-1, comparator.compare(model1, model2));
        assertEquals(1, comparator.compare(model2, model1));

        AttributeModel model3 = new AttributeModel("aaa");
        AttributeModel model4 = new AttributeModel("zzz");

        assertEquals(-1, comparator.compare(model1, model3));
        assertEquals(1, comparator.compare(model3, model1));

        assertEquals(1, comparator.compare(model1, model4));
        assertEquals(-1, comparator.compare(model4, model1));
    }
}

