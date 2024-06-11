package de.paragon.explorer.model;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class AttributeModelComparatorTest {
    
    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel model1 = new AttributeModel("abc");
        AttributeModel model2 = new AttributeModel("def");
        
        // Compare two models with different names
        int result1 = comparator.compare(model1, model2);
        assertEquals(-1, result1);
        
        // Compare two models with same names
        AttributeModel model3 = new AttributeModel("abc");
        int result2 = comparator.compare(model1, model3);
        assertEquals(0, result2);
        
        // Compare two models where alphabetical order is influenced by "a" condition
        AttributeModel model4 = new AttributeModel("Axyz");
        AttributeModel model5 = new AttributeModel("xyz");
        int result3 = comparator.compare(model4, model5);
        assertEquals(1, result3);
        
        AttributeModel model6 = new AttributeModel("A");
        AttributeModel model7 = new AttributeModel("xyz");
        int result4 = comparator.compare(model6, model7);
        assertEquals(-1, result4);
        
        AttributeModel model8 = new AttributeModel("abc");
        AttributeModel model9 = new AttributeModel("Axyz");
        int result5 = comparator.compare(model8, model9);
        assertEquals(-1, result5);
    }
}