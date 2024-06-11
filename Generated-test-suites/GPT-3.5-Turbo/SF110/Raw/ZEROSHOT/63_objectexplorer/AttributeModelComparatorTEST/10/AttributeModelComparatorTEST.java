package de.paragon.explorer.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AttributeModelComparatorTest {

    private AttributeModel o1;
    private AttributeModel o2;
    private AttributeModelComparator comparator;

    @Before
    public void setUp() {
        o1 = new AttributeModel("test1");
        o2 = new AttributeModel("test2");
        comparator = new AttributeModelComparator();
    }

    @Test
    public void testCompare_NullObjects_ReturnsNegativeOne() {
        int result = comparator.compare(null, null);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_NullObjectOne_ReturnsNegativeOne() {
        int result = comparator.compare(null, o2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_NullObjectTwo_ReturnsNegativeOne() {
        int result = comparator.compare(o1, null);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_ObjectOneNameEqualsObjectTwoName_ReturnsZero() {
        int result = comparator.compare(o1, o1);
        assertEquals(0, result);
    }

    @Test
    public void testCompare_ObjectOneNameGreaterThanObjectTwoName_ReturnsPositiveOne() {
        int result = comparator.compare(o2, o1);
        assertEquals(1, result);
    }

    @Test
    public void testCompare_ObjectOneNameLessThanObjectTwoName_ReturnsNegativeOne() {
        int result = comparator.compare(o1, o2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_ObjectOneNameLessThanIgnoreCaseA_ObjectTwoNameGreaterThanIgnoreCaseAReturnsPositiveOne() {
        o1.setName("TestA");
        o2.setName("TestB");
        int result = comparator.compare(o1, o2);
        assertEquals(1, result);
    }

    @Test
    public void testCompare_ObjectOneNameGreaterThanIgnoreCaseA_ObjectTwoNameLessThanIgnoreCaseAReturnsNegativeOne() {
        o1.setName("TestB");
        o2.setName("TestA");
        int result = comparator.compare(o1, o2);
        assertEquals(-1, result);
    }

    @Test
    public void testCompare_ObjectOneNameEqualToIgnoreCaseA_ObjectTwoNameGreaterThanIgnoreCaseAReturnsPositiveOne() {
        o1.setName("TestA");
        o2.setName("TestB");
        int result = comparator.compare(o1, o2);
        assertEquals(1, result);
    }

    @Test
    public void testCompare_ObjectOneNameGreaterThanIgnoreCaseA_ObjectTwoNameEqualToIgnoreCaseAReturnsNegativeOne() {
        o1.setName("TestB");
        o2.setName("TestA");
        int result = comparator.compare(o1, o2);
        assertEquals(-1, result);
    }

}