package de.paragon.explorer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class AttributeModelComparatorTest {
    
    AttributeModelComparator attributeModelComparator;
    AttributeModel model1;
    AttributeModel model2;
    
    @Before
    public void setUp() {
        attributeModelComparator = new AttributeModelComparator();
        model1 = new AttributeModel("abc");
        model2 = new AttributeModel("def");
    }
    
    @Test
    public void testCompare_nullObjects() {
        assertEquals(0, attributeModelComparator.compare(null, null));
    }
    
    @Test
    public void testCompare_nullObjectAndNonNullObject() {
        assertEquals(-1, attributeModelComparator.compare(null, model1));
        assertEquals(1, attributeModelComparator.compare(model1, null));
    }
    
    @Test
    public void testCompare_sameNames() {
        AttributeModel model3 = new AttributeModel("abc");
        assertEquals(0, attributeModelComparator.compare(model1, model3));
    }
    
    @Test
    public void testCompare_differentNames() {
        assertEquals(-1, attributeModelComparator.compare(model1, model2));
        assertEquals(1, attributeModelComparator.compare(model2, model1));
    }
    
    @Test
    public void testCompare_nameAIsSmaller() {
        AttributeModel model4 = new AttributeModel("aba");
        assertEquals(-1, attributeModelComparator.compare(model1, model4));
        assertEquals(1, attributeModelComparator.compare(model4, model1));
    }
    
    @Test
    public void testCompare_nameAIsGreater() {
        AttributeModel model5 = new AttributeModel("aca");
        assertEquals(1, attributeModelComparator.compare(model1, model5));
        assertEquals(-1, attributeModelComparator.compare(model5, model1));
    }
    
}