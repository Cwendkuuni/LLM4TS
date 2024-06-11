
package de.paragon.explorer.model;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Comparator;

public class AttributeModelComparatorTest {
    
    @Test
    public void testCompare_nullObjects_returnsZero() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, null);
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_nullObjectAndNonNullObject_returnsNegativeOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o2 = new AttributeModel();
        int result = comparator.compare(null, o2);
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_nonNullObjectAndNullObject_returnsOne() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel();
        int result = comparator.compare(o1, null);
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_equalObjects_returnsZero() {
        AttributeModel o1 = new AttributeModel();
        o1.setName("A");
        AttributeModel o2 = new AttributeModel();
        o2.setName("A");
        
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(o1, o2);
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_firstObjectBeforeSecondObject_returnsNegativeOne() {
        AttributeModel o1 = new AttributeModel();
        o1.setName("B");
        AttributeModel o2 = new AttributeModel();
        o2.setName("C");
        
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(o1, o2);
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_firstObjectAfterSecondObject_returnsOne() {
        AttributeModel o1 = new AttributeModel();
        o1.setName("D");
        AttributeModel o2 = new AttributeModel();
        o2.setName("C");
        
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(o1, o2);
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_firstObjectBeforeSecondObjectIgnoreCase_returnsNegativeOne() {
        AttributeModel o1 = new AttributeModel();
        o1.setName("b");
        AttributeModel o2 = new AttributeModel();
        o2.setName("C");
        
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(o1, o2);
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_firstObjectAfterSecondObjectIgnoreCase_returnsOne() {
        AttributeModel o1 = new AttributeModel();
        o1.setName("D");
        AttributeModel o2 = new AttributeModel();
        o2.setName("c");
        
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(o1, o2);
        assertEquals(1, result);
    }
    
    @Test
    public void testCompare_firstObjectBeforeSecondObjectWithAName_returnsNegativeOne() {
        AttributeModel o1 = new AttributeModel();
        o1.setName("B");
        AttributeModel o2 = new AttributeModel();
        o2.setName("A");
        
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(o1, o2);
        assertEquals(-1, result);
    }
    
    @Test
    public void testCompare_firstObjectAfterSecondObjectWithAName_returnsOne() {
        AttributeModel o1 = new AttributeModel();
        o1.setName("C");
        AttributeModel o2 = new AttributeModel();
        o2.setName("A");
        
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(o1, o2);
        assertEquals(1, result);
    }
}
