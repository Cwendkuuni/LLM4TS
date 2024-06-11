package org.mockito.internal.matchers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class EqualityTest {
    private Equality equality;

    @Before
    public void setUp() {
        equality = new Equality();
    }

    @Test
    public void testAreEqual() {
        assertTrue(Equality.areEqual(null, null));
        assertFalse(Equality.areEqual(null, new Object()));
        assertFalse(Equality.areEqual(new Object(), null));
        assertFalse(Equality.areEqual(new Object(), new Object()));

        String string1 = "Hello";
        String string2 = new String("Hello");
        assertTrue(Equality.areEqual(string1, string2));

        Integer int1 = 5;
        Integer int2 = new Integer(5);
        assertTrue(Equality.areEqual(int1, int2));
    }

    @Test
    public void testAreArraysEqual() {
        String[] array1 = {"Hello", "World"};
        String[] array2 = {"Hello", "World"};
        assertTrue(Equality.areArraysEqual(array1, array2));

        String[] array3 = {"Hello", "World"};
        String[] array4 = {"Hello", "Universe"};
        assertFalse(Equality.areArraysEqual(array3, array4));

        Integer[] array5 = {1, 2, 3};
        Integer[] array6 = {1, 2, 3};
        assertTrue(Equality.areArraysEqual(array5, array6));

        Integer[] array7 = {1, 2, 3};
        Integer[] array8 = {1, 2, 4};
        assertFalse(Equality.areArraysEqual(array7, array8));
    }

    @Test
    public void testAreArrayLengthsEqual() {
        String[] array1 = {"Hello", "World"};
        String[] array2 = {"Hello", "World"};
        assertTrue(Equality.areArrayLengthsEqual(array1, array2));

        String[] array3 = {"Hello", "World"};
        String[] array4 = {"Hello"};
        assertFalse(Equality.areArrayLengthsEqual(array3, array4));

        Integer[] array5 = {1, 2, 3};
        Integer[] array6 = {1, 2, 3};
        assertTrue(Equality.areArrayLengthsEqual(array5, array6));

        Integer[] array7 = {1, 2, 3};
        Integer[] array8 = {1, 2};
        assertFalse(Equality.areArrayLengthsEqual(array7, array8));
    }

    @Test
    public void testAreArrayElementsEqual() {
        String[] array1 = {"Hello", "World"};
        String[] array2 = {"Hello", "World"};
        assertTrue(Equality.areArrayElementsEqual(array1, array2));

        String[] array3 = {"Hello", "World"};
        String[] array4 = {"Hello", "Universe"};
        assertFalse(Equality.areArrayElementsEqual(array3, array4));

        Integer[] array5 = {1, 2, 3};
        Integer[] array6 = {1, 2, 3};
        assertTrue(Equality.areArrayElementsEqual(array5, array6));

        Integer[] array7 = {1, 2, 3};
        Integer[] array8 = {1, 2, 4};
        assertFalse(Equality.areArrayElementsEqual(array7, array8));
    }

    @Test
    public void testIsArray() {
        String[] array1 = {"Hello", "World"};
        assertTrue(Equality.isArray(array1));

        String string1 = "Hello";
        assertFalse(Equality.isArray(string1));

        Integer[] array2 = {1, 2, 3};
        assertTrue(Equality.isArray(array2));

        Integer int1 = 5;
        assertFalse(Equality.isArray(int1));
    }
}