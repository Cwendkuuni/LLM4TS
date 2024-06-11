
package de.paragon.explorer.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {
    
    private AttributeModelComparator comparator;
    private AttributeModel attribute1;
    private AttributeModel attribute2;
    
    @Test
    public void testCompare_SameName_ReturnsZero() {
        attribute1 = new AttributeModel("abc");
        attribute2 = new AttributeModel("abc");
        comparator = new AttributeModelComparator();
        
        int result = comparator.compare(attribute1, attribute2);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_O1NameBeforeA_ReturnsNegativeOne() {
        attribute1 = new AttributeModel("abc");
        attribute2 = new AttributeModel("xyz");
        comparator = new AttributeModelComparator();
        
        int result = comparator.compare(attribute1, attribute2);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_O2NameBeforeA_ReturnsOne() {
        attribute1 = new AttributeModel("xyz");
        attribute2 = new AttributeModel("abc");
        comparator = new AttributeModelComparator();
        
        int result = comparator.compare(attribute1, attribute2);
        
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_O1Null_ReturnsNegativeOne() {
        attribute1 = null;
        attribute2 = new AttributeModel("abc");
        comparator = new AttributeModelComparator();
        
        int result = comparator.compare(attribute1, attribute2);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_O2Null_ReturnsOne() {
        attribute1 = new AttributeModel("abc");
        attribute2 = null;
        comparator = new AttributeModelComparator();
        
        int result = comparator.compare(attribute1, attribute2);
        
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_BothNull_ReturnsZero() {
        attribute1 = null;
        attribute2 = null;
        comparator = new AttributeModelComparator();
        
        int result = comparator.compare(attribute1, attribute2);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_EmptyStringBeforeA_ReturnsNegativeOne() {
        attribute1 = new AttributeModel("");
        attribute2 = new AttributeModel("abc");
        comparator = new AttributeModelComparator();
        
        int result = comparator.compare(attribute1, attribute2);
        
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_ABeforeEmptyString_ReturnsOne() {
        attribute1 = new AttributeModel("abc");
        attribute2 = new AttributeModel("");
        comparator = new AttributeModelComparator();
        
        int result = comparator.compare(attribute1, attribute2);
        
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_CaseInsensitiveComparison() {
        attribute1 = new AttributeModel("AbC");
        attribute2 = new AttributeModel("aBC");
        comparator = new AttributeModelComparator();
        
        int result = comparator.compare(attribute1, attribute2);
        
        assertEquals(0, result);
    }
}

