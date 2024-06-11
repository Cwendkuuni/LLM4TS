package org.mockito.internal.matchers.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.hamcrest.CoreMatchers;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void testSameMethodWithSameObjects() {
        Same same = new Same("testString");
        String testString = "testString";
        assertTrue(same.matches(testString));
    }

    @Test
    public void testSameMethodWithSameInteger() {
        Same same = new Same(10);
        int testInt = 10;
        assertTrue(same.matches(testInt));
    }

    @Test
    public void testSameMethodWithSameCharacter() {
        Same same = new Same('a');
        char testChar = 'a';
        assertTrue(same.matches(testChar));
    }

    @Test(expected = AssertionError.class)
    public void testSameMethodWithDifferentObjects() {
        Same same = new Same("testString");
        String differentString = "differentString";
        assertFalse(same.matches(differentString));
    }

    @Test(expected = AssertionError.class)
    public void testSameMethodWithDifferentIntegers() {
        Same same = new Same(10);
        int differentInt = 20;
        assertFalse(same.matches(differentInt));
    }

    @Test(expected = AssertionError.class)
    public void testSameMethodWithDifferentCharacters() {
        Same same = new Same('a');
        char differentChar = 'b';
        assertFalse(same.matches(differentChar));
    }

    @Test
    public void testSameMethodWithNullObject() {
        Same same = new Same(null);
        assertNull(same.matches(null));
    }

    @Test
    public void testDescribeToWithString() {
        Same same = new Same("testString");
        Description description = new Description();
        same.describeTo(description);
        String expectedDescription = "same(\"testString\")";
        String actualDescription = description.toString();
        assertEquals(expectedDescription, actualDescription);
    }

    @Test
    public void testDescribeToWithCharacter() {
        Same same = new Same('a');
        Description description = new Description();
        same.describeTo(description);
        String expectedDescription = "same('a')";
        String actualDescription = description.toString();
        assertEquals(expectedDescription, actualDescription);
    }

    @Test
    public void testDescribeToWithObjectArray() {
        Object[] array = new Object[]{"testString1", "testString2"};
        Same same = ArgumentMatcher.not(new Same(array[0]));
        Description description = new Description();
        same.describeTo(description);
        String expectedDescription = "not same([testString1, testString2])";
        String actualDescription = description.toString();
        assertEquals(expectedDescription, actualDescription);
    }
}