
package de.paragon.explorer.model;

import static org.junit.Assert.assertEquals;

import java.util.Comparator;

import org.junit.Test;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        // Test with two AttributeModels where o1 > o2
        AttributeModel o1 = new AttributeModel("bbb");
        AttributeModel o2 = new AttributeModel("aaa");
        int result = comparator.compare(o1, o2);
        assertEquals(1, result);
        
        // Test with two AttributeModels where o1 < o2
        o1 = new AttributeModel("aaa");
        o2 = new AttributeModel("bbb");
        result = comparator.compare(o1, o2);
        assertEquals(-1, result);
        
        // Test with two AttributeModels where o1 == o2
        o1 = new AttributeModel("aaa");
        o2 = new AttributeModel("aaa");
        result = comparator.compare(o1, o2);
        assertEquals(0, result);
        
        // Test with o1 null, o2 not null
        o1 = null;
        o2 = new AttributeModel("aaa");
        result = comparator.compare(o1, o2);
        assertEquals(-1, result);
        
        // Test with o1 not null, o2 null
        o1 = new AttributeModel("aaa");
        o2 = null;
        result = comparator.compare(o1, o2);
        assertEquals(1, result);
        
        // Test with both o1 and o2 null
        o1 = null;
        o2 = null;
        result = comparator.compare(o1, o2);
        assertEquals(-1, result);
    }
}
