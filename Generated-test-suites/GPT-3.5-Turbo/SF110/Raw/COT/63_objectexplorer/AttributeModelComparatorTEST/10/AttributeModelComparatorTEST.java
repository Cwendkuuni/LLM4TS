package de.paragon.explorer.model;

import org.junit.*;
import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    private AttributeModelComparator comparator;
    
    @Before
    public void setUp() {
        comparator = new AttributeModelComparator();
    }
    
    @Test
    public void testCompare_bothNull() {
        AttributeModel o1 = null;
        AttributeModel o2 = null;
        int result = comparator.compare(o1, o2);
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_onlyO1Null() {
        AttributeModel o1 = null;
        AttributeModel o2 = new AttributeModel();
        int result = comparator.compare(o1, o2);
        assertTrue(result < 0);
    }
    
    @Test
    public void testCompare_onlyO2Null() {
        AttributeModel o1 = new AttributeModel();
        AttributeModel o2 = null;
        int result = comparator.compare(o1, o2);
        assertTrue(result > 0);
    }
    
    @Test
    public void testCompare_bothHaveNullNames() {
        AttributeModel o1 = new AttributeModel();
        AttributeModel o2 = new AttributeModel();
        int result = comparator.compare(o1, o2);
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_onlyO1HasNullName() {
        AttributeModel o1 = new AttributeModel();
        o1.setName(null);
        AttributeModel o2 = new AttributeModel();
        o2.setName("attribute");
        int result = comparator.compare(o1, o2);
        assertTrue(result < 0);
    }
    
    @Test
    public void testCompare_onlyO2HasNullName() {
        AttributeModel o1 = new AttributeModel();
        o1.setName("attribute");
        AttributeModel o2 = new AttributeModel();
        o2.setName(null);
        int result = comparator.compare(o1, o2);
        assertTrue(result > 0);
    }
    
    @Test
    public void testCompare_bothHaveNamesStartingWithA() {
        AttributeModel o1 = new AttributeModel();
        o1.setName("apple");
        AttributeModel o2 = new AttributeModel();
        o2.setName("apple");
        int result = comparator.compare(o1, o2);
        assertEquals(0, result);
    }
    
    @Test
    public void testCompare_onlyO1HasNameStartingWithA() {
        AttributeModel o1 = new AttributeModel();
        o1.setName("apple");
        AttributeModel o2 = new AttributeModel();
        o2.setName("banana");
        int result = comparator.compare(o1, o2);
        assertTrue(result < 0);
    }
    
    @Test
    public void testCompare_onlyO2HasNameStartingWithA() {
        AttributeModel o1 = new AttributeModel();
        o1.setName("banana");
        AttributeModel o2 = new AttributeModel();
        o2.setName("apple");
        int result = comparator.compare(o1, o2);
        assertTrue(result > 0);
    }
    
    @Test
    public void testCompare_bothHaveNamesStartingWithDifferentLetters() {
        AttributeModel o1 = new AttributeModel();
        o1.setName("apple");
        AttributeModel o2 = new AttributeModel();
        o2.setName("banana");
        int result = comparator.compare(o1, o2);
        assertEquals(-1, result);
    }
}