package de.paragon.explorer.model;
import org.junit.*;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        // Test case 1: o1 and o2 are null
        assertEquals(0, comparator.compare(null, null));
        
        // Test case 2: o1 is null, o2 is not null
        AttributeModel o2 = new AttributeModel();
        assertEquals(-1, comparator.compare(null, o2));
        
        // Test case 3: o1 is not null, o2 is null
        AttributeModel o1 = new AttributeModel();
        assertEquals(1, comparator.compare(o1, null));
        
        // Test case 4: o1 and o2 have null names
        o1.setName(null);
        o2.setName(null);
        assertEquals(0, comparator.compare(o1, o2));
        
        // Test case 5: o1 and o2 have the same name
        o1.setName("abc");
        o2.setName("abc");
        assertEquals(0, comparator.compare(o1, o2));
        
        // Test case 6: o1 name is "a", o2 name is not "a"
        o1.setName("a");
        o2.setName("b");
        assertEquals(1, comparator.compare(o1, o2));
        
        // Test case 7: o2 name is "a", o1 name is not "a"
        o1.setName("b");
        o2.setName("a");
        assertEquals(-1, comparator.compare(o1, o2));
        
        // Test case 8: o1 name is "A", o2 name is "a"
        o1.setName("A");
        o2.setName("a");
        assertEquals(0, comparator.compare(o1, o2));
        
        // Test case 9: o1 name is "aA", o2 name is "aa"
        o1.setName("aA");
        o2.setName("aa");
        assertEquals(1, comparator.compare(o1, o2));
        
        // Test case 10: o1 name is "abc", o2 name is "abcd"
        o1.setName("abc");
        o2.setName("abcd");
        assertEquals(-1, comparator.compare(o1, o2));
        
    }
}