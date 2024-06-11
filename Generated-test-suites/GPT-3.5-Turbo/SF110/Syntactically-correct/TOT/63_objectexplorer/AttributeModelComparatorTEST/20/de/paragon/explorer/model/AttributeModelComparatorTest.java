package de.paragon.explorer.model;

import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    @Test
    public void testCompare() {
        // Test when both AttributeModel objects are null
        AttributeModelComparator comparator = new AttributeModelComparator();
        int result = comparator.compare(null, null);
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_BothNamesNull() {
        // Test when both AttributeModel names are null
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel(null);
        AttributeModel o2 = new AttributeModel(null);
        int result = comparator.compare(o1, o2);
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_O1NameNull() {
        // Test when o1's name is null and o2's name is not null
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel(null);
        AttributeModel o2 = new AttributeModel("b");
        int result = comparator.compare(o1, o2);
        assertTrue(result < 0);
    }
    
    @Test
    public void testCompare_O2NameNull() {
        // Test when o2's name is null and o1's name is not null
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("b");
        AttributeModel o2 = new AttributeModel(null);
        int result = comparator.compare(o1, o2);
        assertTrue(result > 0);
    }
    
    @Test
    public void testCompare_BothNamesNotNull_LessThan() {
        // Test when o1's name is lexicographically less than o2's name
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("apple");
        AttributeModel o2 = new AttributeModel("banana");
        int result = comparator.compare(o1, o2);
        assertTrue(result < 0);
    }
    
    @Test
    public void testCompare_BothNamesNotNull_GreaterThan() {
        // Test when o1's name is lexicographically greater than o2's name
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("banana");
        AttributeModel o2 = new AttributeModel("apple");
        int result = comparator.compare(o1, o2);
        assertTrue(result > 0);
    }
    
    @Test
    public void testCompare_EqualNames_IgnoreCase() {
        // Test when o1's name is equal to o2's name regardless of case
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("apple");
        AttributeModel o2 = new AttributeModel("APPLE");
        int result = comparator.compare(o1, o2);
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_EqualNames_A_EdgeCase() {
        // Test when o1 and o2 names are equal to "a"
        AttributeModelComparator comparator = new AttributeModelComparator();
        AttributeModel o1 = new AttributeModel("a");
        AttributeModel o2 = new AttributeModel("a");
        int result = comparator.compare(o1, o2);
        assertEquals(0, result);
        o1 = new AttributeModel("a");
        o2 = new AttributeModel("b");
        result = comparator.compare(o1, o2);
        assertTrue(result < 0);
        o1 = new AttributeModel("b");
        o2 = new AttributeModel("a");
        result = comparator.compare(o1, o2);
        assertTrue(result > 0);
    }
}