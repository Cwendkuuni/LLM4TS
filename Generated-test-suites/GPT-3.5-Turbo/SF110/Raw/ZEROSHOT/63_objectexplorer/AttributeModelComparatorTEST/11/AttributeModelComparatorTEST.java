package de.paragon.explorer.model;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare() {
        AttributeModelComparator comparator = new AttributeModelComparator();

        // Test when o1 and o2 are null
        assertEquals(0, comparator.compare(null, null));

        // Test when o1 is null and o2 is not null
        AttributeModel o1 = null;
        AttributeModel o2 = new AttributeModel("test");
        assertEquals(-1, comparator.compare(o1, o2));

        // Test when o1 is not null adn o2 is null
        o1 = new AttributeModel("test");
        o2 = null;
        assertEquals(1, comparator.compare(o1, o2));

        // Test when o1 and o2 are not null
        o1 = new AttributeModel("apple");
        o2 = new AttributeModel("banana");
        assertEquals(-1, comparator.compare(o1, o2));
        
        o1 = new AttributeModel("banana");
        o2 = new AttributeModel("apple");
        assertEquals(1, comparator.compare(o1, o2));
        
        o1 = new AttributeModel("apple");
        o2 = new AttributeModel("apple");
        assertEquals(0, comparator.compare(o1, o2));

        o1 = new AttributeModel("abc");
        o2 = new AttributeModel("def");
        assertEquals(-1, comparator.compare(o1, o2));

        o1 = new AttributeModel("def");
        o2 = new AttributeModel("abc");
        assertEquals(1, comparator.compare(o1, o2));

        o1 = new AttributeModel("abc");
        o2 = new AttributeModel("ABC");
        assertEquals(0, comparator.compare(o1, o2));
    }
}