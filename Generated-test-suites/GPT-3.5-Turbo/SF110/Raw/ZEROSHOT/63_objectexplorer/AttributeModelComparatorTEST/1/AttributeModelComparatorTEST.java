package de.paragon.explorer.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        // Test o1 and o2 are null
        assertEquals(0, comparator.compare(null, null));
        
        // Test o1 is null
        AttributeModel o2 = new AttributeModel();
        assertEquals(-1, comparator.compare(null, o2));
        
        // Test o2 is null
        AttributeModel o1 = new AttributeModel();
        assertEquals(1, comparator.compare(o1, null));
        
        // Test o1 and o2 have the same name
        o1.setName("name");
        o2.setName("name");
        assertEquals(0, comparator.compare(o1, o2));
        
        // Test o1's name comes before "a" and o2's name comes after "a"
        o1.setName("A");
        o2.setName("B");
        assertEquals(1, comparator.compare(o1, o2));
        
        // Test o1's name comes after "a" and o2's name comes before "a"
        o1.setName("B");
        o2.setName("A");
        assertEquals(-1, comparator.compare(o1, o2));
        
        // Test o1's name comes before "a" and o2's name comes before "a"
        o1.setName("A");
        o2.setName("C");
        assertEquals(-1, comparator.compare(o1, o2));
        
        // Test o1's name comes after "a" and o2's name comes after "a"
        o1.setName("C");
        o2.setName("A");
        assertEquals(1, comparator.compare(o1, o2));
    }
}