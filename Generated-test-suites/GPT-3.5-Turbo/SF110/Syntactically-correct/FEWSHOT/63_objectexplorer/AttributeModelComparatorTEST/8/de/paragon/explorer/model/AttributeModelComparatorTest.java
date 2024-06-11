
package de.paragon.explorer.model;

import static org.junit.Assert.*;

import java.util.Comparator;

import org.junit.Test;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare_BothNull() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        int result = comparator.compare(null, null);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_O1Null() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o2 = new AttributeModel("Test2");
        
        int result = comparator.compare(null, o2);
        
        assertTrue(result < 0);
    }
    
    @Test
    public void testCompare_O2Null() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("Test1");
        
        int result = comparator.compare(o1, null);
        
        assertTrue(result > 0);
    }
    
    @Test
    public void testCompare_BothNotNull_O1NameBeforeA_O2NameBeforeA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("b");
        AttributeModel o2 = new AttributeModel("c");
        
        int result = comparator.compare(o1, o2);
        
        assertTrue(result < 0);
    }
    
    @Test
    public void testCompare_BothNotNull_O1NameAfterA_O2NameAfterA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("f");
        AttributeModel o2 = new AttributeModel("e");
        
        int result = comparator.compare(o1, o2);
        
        assertTrue(result > 0);
    }
    
    @Test
    public void testCompare_BothNotNull_O1NameBeforeA_O2NameAfterA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("b");
        AttributeModel o2 = new AttributeModel("d");
        
        int result = comparator.compare(o1, o2);
        
        assertTrue(result < 0);
    }
    
    @Test
    public void testCompare_BothNotNull_O1NameAfterA_O2NameBeforeA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("f");
        AttributeModel o2 = new AttributeModel("a");
        
        int result = comparator.compare(o1, o2);
        
        assertTrue(result > 0);
    }
    
    @Test
    public void testCompare_BothNotNull_O1NameEqualToA_O2NameAfterA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("a");
        AttributeModel o2 = new AttributeModel("c");
        
        int result = comparator.compare(o1, o2);
        
        assertTrue(result < 0);
    }
    
    @Test
    public void testCompare_BothNotNull_O1NameAfterA_O2NameEqualToA() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("f");
        AttributeModel o2 = new AttributeModel("a");
        
        int result = comparator.compare(o1, o2);
        
        assertTrue(result > 0);
    }
}
