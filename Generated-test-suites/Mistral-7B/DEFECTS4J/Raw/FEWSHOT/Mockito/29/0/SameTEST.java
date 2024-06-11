package org.mockito.internal.matchers;

import org.junit.Test;
import org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class SameTest {

    @Test
    public void testSameObject() {
        Same sameObject = new Same(new Object());
        Object actual = new Object();
        assertTrue(sameObject.matches(actual));
    }

    @Test
    public void testDifferentObjects() {
        Same sameObject = new Same(new Object());
        Object actual = new String("new Object");
        assertFalse(sameObject.matches(actual));
    }

    @Test
    public void testSameString() {
        Same sameString = new Same("test String");
        String actual = "test String";
        assertTrue(sameString.matches(actual));
    }

    @Test
    public void testDifferentStrings() {
        Same sameString = new Same("test String");
        String actual = "different String";
        assertFalse(sameString.matches(actual));
    }

    @Test
    public void testSameCharacter() {
        Same sameCharacter = new Same('a');
        Character actual = 'a';
        assertTrue(sameCharacter.matches(actual));
    }

    @Test
    public void testDifferentCharacters() {
        Same sameCharacter = new Same('a');
        Character actual = 'b';
        assertFalse(sameCharacter.matches(actual));
    }

    @Test
    public void testSameInteger() {
        Same sameInteger = new Same(1);
        Integer actual = 1;
        assertTrue(sameInteger.matches(actual));
    }

    @Test
    public void testDifferentIntegers() {
        Same sameInteger = new Same(1);
        Integer actual = 2;
        assertFalse(sameInteger.matches(actual));
    }

    @Test
    public void testSameDouble() {
        Same sameDouble = new Same(1.0);
        Double actual = 1.0;
        assertTrue(sameDouble.matches(actual));
    }

    @Test
    public void testDifferentDoubles() {
        Same sameDouble = new Same(1.0);
        Double actual = 2.0;
        assertFalse(sameDouble.matches(actual));
    }

    @Test
    public void testSameArray() {
        Object[] wanted = new Object[]{1, 2, 3};
        Same sameArray = new Same(wanted);
        Object[] actual = new Object[]{1, 2, 3};
        assertTrue(sameArray.matches(actual));
    }

    @Test
    public void testDifferentArrays() {
        Object[] wanted = new Object[]{1, 2, 3};
        Same sameArray = new Same(wanted);
        Object[] actual = new Object[]{1, 2, 4};
        assertFalse(sameArray.matches(actual));
    }

    @Test
    public void testSameCollection() {
        java.util.List<Object> wanted = java.util.Arrays.asList(new Object[]{1, 2, 3});
        Same sameCollection = new Same(wanted);
        java.util.List<Object> actual = java.util.Arrays.asList(new Object[]{1, 2, 3});
        assertTrue(sameCollection.matches(actual));
    }

    @Test
    public void testDifferentCollections() {
        java.util.List<Object> wanted = java.util.Arrays.asList(new Object[]{1, 2, 3});
        Same sameCollection = new Same(wanted);
        java.util.List<Object> actual = java.util.Arrays.asList(new Object[]{1, 2, 4});
        assertFalse(sameCollection.matches(actual));
    }

    @Test
    public void testDescribeTo() {
        Same sameString = new Same("test String");
        Description description = new Description();
        sameString.describeTo(description);
        String expected = "same(test String)";
        assertEquals(expected, description.toString());
    }
}