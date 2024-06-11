package de.paragon.explorer.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {
    
    private AttributeModelComparator comparator;

    @Before
    public void setUp() {
        comparator = new AttributeModelComparator();
    }

    @Test
    public void testCompare() {
        AttributeModel a = new AttributeModel("apple");
        AttributeModel b = new AttributeModel("banana");

        int result = comparator.compare(a, b);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_NullObjects() {
        int result = comparator.compare(null, null);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_SameObject() {
        AttributeModel a = new AttributeModel("apple");
        int result = comparator.compare(a, a);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_WithAFirst() {
        AttributeModel a = new AttributeModel("apple");
        AttributeModel b = new AttributeModel("banana");

        int result = comparator.compare(b, a);
        assertEquals(1, result);
    }

    @Test
    public void testCompare_WithSpecialCharacters() {
        AttributeModel a = new AttributeModel("apple");
        AttributeModel b = new AttributeModel("!banana");

        int result = comparator.compare(a, b);
        assertEquals(-1, result);
    }
}