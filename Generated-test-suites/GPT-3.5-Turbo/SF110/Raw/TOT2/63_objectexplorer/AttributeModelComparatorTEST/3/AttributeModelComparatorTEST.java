package de.paragon.explorer.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    private AttributeModelComparator comparator = new AttributeModelComparator();
    
    @Test
    public void testCompare() {
        AttributeModel attributeModel1 = new AttributeModel("Test1");
        AttributeModel attributeModel2 = new AttributeModel("Test2");
        
        int result1 = comparator.compare(attributeModel1, attributeModel2);
        assertTrue(result1 < 0);
        
        int result2 = comparator.compare(attributeModel2, attributeModel1);
        assertTrue(result2 > 0);
        
        int result3 = comparator.compare(attributeModel1, attributeModel1);
        assertTrue(result3 == 0);
        
        AttributeModel nullModel = null;
        int result4 = comparator.compare(attributeModel1, nullModel);
        assertTrue(result4 == -1);
        
        int result5 = comparator.compare(nullModel, attributeModel2);
        assertTrue(result5 == -1);
    }
}