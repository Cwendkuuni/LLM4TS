package de.paragon.explorer.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {
    
    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        // Test when both objects are null
        assertEquals(0, comparator.compare(null, null));
        
        // Test when o1 is null
        AttributeModel o1 = null;
        AttributeModel o2 = new AttributeModel();
        o2.setName("abc");
        assertEquals(-1, comparator.compare(o1, o2));
        
        // Test when o2 is null
        o1 = new AttributeModel();
        o1.setName("abc");
        o2 = null;
        assertEquals(1, comparator.compare(o1, o2));
        
        // Test when both names are equal
        o1 = new AttributeModel();
        o1.setName("abc");
        o2 = new AttributeModel();
        o2.setName("abc");
        assertEquals(0, comparator.compare(o1, o2));
        
        // Test when o1 name is less than o2 name
        o1 = new AttributeModel();
        o1.setName("aaa");
        o2 = new AttributeModel();
        o2.setName("bbb");
        assertEquals(-1, comparator.compare(o1, o2));
        
        // Test when o2 name is less than o1 name
        o1 = new AttributeModel();
        o1.setName("bbb");
        o2 = new AttributeModel();
        o2.setName("aaa");
        assertEquals(1, comparator.compare(o1, o2));
        
        // Test when o1 name is less than "a" and o2 name is greater than or equal to "a"
        o1 = new AttributeModel();
        o1.setName("abc");
        o2 = new AttributeModel();
        o2.setName("zxy");
        assertEquals(1, comparator.compare(o1, o2));
        
        // Test when o2 name is less than "a" and o1 name is greater than or equal to "a"
        o1 = new AttributeModel();
        o1.setName("zxy");
        o2 = new AttributeModel();
        o2.setName("abc");
        assertEquals(-1, comparator.compare(o1, o2));
    }
}