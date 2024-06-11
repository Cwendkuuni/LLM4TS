
package de.paragon.explorer.model;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class AttributeModelComparatorTest {

    private AttributeModelComparator comparator;

    @Before
    public void setUp() {
        comparator = new AttributeModelComparator();
    }

    @Test
    public void testCompare() {
        AttributeModel a = new AttributeModel("a");
        AttributeModel b = new AttributeModel("b");
        AttributeModel c = new AttributeModel("c");
        
        // a.compareToIgnoreCase(b)
        assertTrue(comparator.compare(a, b) < 0);
        
        // b.compareToIgnoreCase(a)
        assertTrue(comparator.compare(b, a) > 0);
        
        // a.compareToIgnoreCase(c)
        assertTrue(comparator.compare(a, c) < 0);
        
        // c.compareToIgnoreCase(a)
        assertTrue(comparator.compare(c, a) > 0);
        
        // b.compareToIgnoreCase(c)
        assertTrue(comparator.compare(b, c) < 0);
        
        // c.compareToIgnoreCase(b)
        assertTrue(comparator.compare(c, b) > 0);
        
        // a.compareToIgnoreCase(a)
        assertEquals(0, comparator.compare(a, a));
        
        // b.compareToIgnoreCase(b)
        assertEquals(0, comparator.compare(b, b));
        
        // c.compareToIgnoreCase(c)
        assertEquals(0, comparator.compare(c, c));
    }

}
