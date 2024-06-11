package org.mockito.internal.matchers;

import static org.junit.Assert.*;
import org.junit.Test;

public class EqualityTest {

    @Test
    public void testAreEqual_nullObjects() {
        assertTrue(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual(null, new Object()));
        assertFalse(Equality.areEqual(new Object(), null));
    }

    @Test
    public void testAreEqual_sameObjects() {
        Object obj = new Object();
        assertTrue(Equality.areEqual(obj, obj));
    }

    @Test
    public void testAreEqual_differentObjects() {
        assertFalse(Equality.areEqual(new Object(), new Object()));
    }

    @Test
    public void testAreEqual_arraysOfEqualLength() {
        Object[] arr1 = new Object[3];
        Object[] arr2 = new Object[3];
        assertTrue(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqual_arraysOfDifferentLength() {
        Object[] arr1 = new Object[3];
        Object[] arr2 = new Object[4];
        assertFalse(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqual_arraysWithDifferentElements() {
        Object[] arr1 = new Object[] {1, 2, 3};
        Object[] arr2 = new Object[] {1, 2, 4};
        assertFalse(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqual_nestedArrays() {
        Object[] arr1 = new Object[] {new Object[] {1, 2}, new Object[] {3, 4}};
        Object[] arr2 = new Object[] {new Object[] {1, 2}, new Object[] {3, 4}};
        assertTrue(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqual_nestedArraysUnequal() {
        Object[] arr1 = new Object[] {new Object[] {1, 2}, new Object[] {3, 4}};
        Object[] arr2 = new Object[] {new Object[] {1, 2}, new Object[] {3, 5}};
        assertFalse(Equality.areEqual(arr1, arr2));
    }

    @Test
    public void testAreEqual_nonArrayObjects() {
        assertFalse(Equality.areEqual(1, new Object()));
    }
}