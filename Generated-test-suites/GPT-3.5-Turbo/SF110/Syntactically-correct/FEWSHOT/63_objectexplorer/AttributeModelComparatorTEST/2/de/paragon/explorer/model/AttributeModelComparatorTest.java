package de.paragon.explorer.model;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.ArrayList;

import org.junit.Test;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_SameName() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        AttributeModel model1 = new AttributeModel("Name");
        AttributeModel model2 = new AttributeModel("Name");
        
        int result = comparator.compare(model1, model2);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_O1Null() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        AttributeModel model1 = null;
        AttributeModel model2 = new AttributeModel("Name");
        
        int result = comparator.compare(model1, model2);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_O2Null() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        AttributeModel model1 = new AttributeModel("Name");
        AttributeModel model2 = null;
        
        int result = comparator.compare(model1, model2);
        
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_O1NameBeforeA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        AttributeModel model1 = new AttributeModel("B");
        AttributeModel model2 = new AttributeModel("A");
        
        int result = comparator.compare(model1, model2);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_O1NameAfterA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        AttributeModel model1 = new AttributeModel("B");
        AttributeModel model2 = new AttributeModel("C");
        
        int result = comparator.compare(model1, model2);
        
        assertEquals(1, result);
    }
    
}