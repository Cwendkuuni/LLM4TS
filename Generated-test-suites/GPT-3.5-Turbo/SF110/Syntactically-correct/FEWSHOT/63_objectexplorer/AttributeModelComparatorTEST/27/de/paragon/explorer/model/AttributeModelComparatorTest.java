package de.paragon.explorer.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();
        
        AttributeModel a1 = new AttributeModel("test1");
        AttributeModel a2 = new AttributeModel("test2");
        assertEquals(-1, comparator.compare(a1, a2));
        
        AttributeModel b1 = new AttributeModel("apple");
        AttributeModel b2 = new AttributeModel("banana");
        assertEquals(-1, comparator.compare(b1, b2));
        
        AttributeModel c1 = new AttributeModel("Elephant");
        AttributeModel c2 = new AttributeModel("elephant");
        assertEquals(1, comparator.compare(c1, c2));
        
        AttributeModel d1 = new AttributeModel("Zebra");
        AttributeModel d2 = new AttributeModel("zoo");
        assertEquals(1, comparator.compare(d1, d2));
        
        AttributeModel e1 = new AttributeModel("xyz");
        AttributeModel e2 = new AttributeModel("XYZ");
        assertEquals(-1, comparator.compare(e1, e2));
        
        AttributeModel f1 = new AttributeModel("a");
        AttributeModel f2 = new AttributeModel("b");
        assertEquals(-1, comparator.compare(f1, f2));
        
        AttributeModel g1 = new AttributeModel("a");
        AttributeModel g2 = new AttributeModel("A");
        assertEquals(1, comparator.compare(g1, g2));
        
        AttributeModel h1 = null;
        AttributeModel h2 = new AttributeModel("orange");
        assertEquals(-1, comparator.compare(h1, h2));
        
        AttributeModel i1 = new AttributeModel("car");
        AttributeModel i2 = null;
        assertEquals(1, comparator.compare(i1, i2));
    }
}