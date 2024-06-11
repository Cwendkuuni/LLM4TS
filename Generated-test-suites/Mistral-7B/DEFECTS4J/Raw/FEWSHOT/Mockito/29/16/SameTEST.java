package org.mockito.internal.matchers;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.same;

import org.hamcrest.Description;
import org.junit.Test;

public class SameTest {

    @Test
    public void testSameInstance() {
        Object obj = new Object();
        assertTrue(same(obj).matches(obj));
    }

    @Test
    public void testSameString() {
        String str = "test";
        assertTrue(same(str).matches(str));
    }

    @Test
    public void testSameCharacter() {
        char c = 'a';
        assertTrue(same(c).matches(c));
    }

    @Test
    public void testSameNull() {
        Object nullObj = null;
        assertTrue(same(nullObj).matches(nullObj));
    }

    @Test
    public void testDifferentInstances() {
        Object obj1 = new Object();
        Object obj2 = new Object();
        assertFalse(same(obj1).matches(obj2));
    }

    @Test
    public void testDifferentString() {
        String str1 = "test1";
        String str2 = "test2";
        assertFalse(same(str1).matches(str2));
    }

    @Test
    public void testDifferentCharacter() {
        char c1 = 'a';
        char c2 = 'b';
        assertFalse(same(c1).matches(c2));
    }

    @Test
    public void testDescribesCorrectly() {
        int i = 1;
        Description description = new Description();
        same(i).describeTo(description);
        String expectedDescription = "same(1)";
        String actualDescription = description.toString();
        assertTrue(expectedDescription.equals(actualDescription));
    }
}