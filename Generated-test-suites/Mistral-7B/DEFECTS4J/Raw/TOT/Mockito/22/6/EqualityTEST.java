package org.mockito.internal.matchers.test;

import org.junit.Test;
import org.mockito.internal.matchers.Equality;

import static org.junit.Assert.*;

public class EqualityTest {
    @Test
    public void testAreEqualWithIdenticalObjects() {
        Object o1 = new Object();
        Object o2 = o1;
        assertTrue(Equality.areEqual(o1, o2));
    }

    @Test
    public void testAreEqualWithNullObjects() {
        Object o1 = null;
        Object o2 = null;
        assertTrue(Equality.areEqual(o1, o2));
    }

    @Test
    public void testAreEqualWithEqualObjects() {
        Object o1 = new Object() {};
        Object o2 = new Object() {};
        assertTrue(Equality.areEqual(o1, o2));
    }

    @Test
    public void testAreEqualWithDifferentObjects() {
        Object o1 = new Object() {/* some value */};
        Object o2 = new Object() {/* different value */};
        assertFalse(Equality.areEqual(o1, o2));
    }

    @Test
    public void testAreEqualWithArrayAndObject() {
        Object o1 = new Object[] {1, 2, 3};
        Object o2 = new Object();
        assertFalse(Equality.areEqual(o1, o2));
    }

    @Test
    public void testAreArraysEqualWithIdenticalArrays() {
        Object[] o1 = new Object[] {1, 2, 3};
        Object[] o2 = new Object[] {1, 2, 3};
        assertTrue(Equality.areArraysEqual(o1, o2));
    }

    @Test
    public void testAreArraysEqualWithArraysOfDifferentLengths() {
        Object[] o1 = new Object[] {1, 2, 3};
        Object[] o2 = new Object[] {1, 2};
        assertFalse(Equality.areArraysEqual(o1, o2));
    }

    @Test
    public void testAreArraysEqualWithArraysWithDifferentElements() {
        Object[] o1 = new Object[] {1, 2, 3};
        Object[] o2 = new Object[] {1, 4, 3};
        assertFalse(Equality.areArraysEqual(o1, o2));
    }

    @Test
    public void testAreArraysEqualWithArraysWithEqualElements() {
        Object[] o1 = new Object[] {1, 2, 3};
        Object[] o2 = new Object[] {1, 2, 3};
        assertTrue(Equality.areArraysEqual(o1, o2));
    }

    @Test
    public void testIsArrayWithArray() {
        Object[] o1 = new Object[] {1, 2, 3};
        assertTrue(Equality.isArray(o1));
    }

    @Test
    public void testIsArrayWithObject() {
        Object o1 = new Object();
        assertFalse(Equality.isArray(o1));
    }
}