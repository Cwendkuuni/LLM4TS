package org.mockito.internal.matchers;

import org.junit.Test;
import org.junit.Assert;
import org.hamcrest.Matcher;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class SameTest {

    @Test
    public void testSameMatchesSameObjects() {
        Object wantedObject = new Object();
        ArgumentMatcher<Object> sameMatcher = new Same(wantedObject);

        Object sameObject = wantedObject;
        Assert.assertTrue(sameMatcher.matches(sameObject));
    }

    @Test
    public void testSameMatchesNullAndNull() {
        ArgumentMatcher<Object> sameMatcher = new Same(null);

        Assert.assertTrue(sameMatcher.matches(null));
    }

    @Test
    public void testSameMatchesDifferentClassesButSameObjects() {
        Object wantedObject = new Object();
        ArgumentMatcher<Object> sameMatcher = new Same(wantedObject);

        String sameString = (String) wantedObject;
        Assert.assertTrue(sameMatcher.matches(sameString));
    }

    @Test
    public void testSameDoesNotMatchDifferentObjects() {
        Object wantedObject1 = new Object();
        ArgumentMatcher<Object> sameMatcher = new Same(wantedObject1);

        Object differentObject = new Object();
        Assert.assertFalse(sameMatcher.matches(differentObject));
    }

    @Test
    public void testSameDescribesCorrectly() {
        Object wantedObject = new Object();
        ArgumentMatcher<Object> sameMatcher = new Same(wantedObject);

        Description description = new Description();
        sameMatcher.describeTo(description);

        String expectedDescription = "same(" + wantedObject.toString() + ")";
        String actualDescription = description.toString();

        Assert.assertEquals(expectedDescription, actualDescription);
    }

    @Test
    public void testSameWithStringArg() {
        String wantedString = "test";
        ArgumentMatcher<Object> sameMatcher = new Same(wantedString);

        String sameString = wantedString;
        Assert.assertTrue(sameMatcher.matches(sameString));
    }

    @Test
    public void testSameWithCharacterArg() {
        char wantedChar = 'a';
        ArgumentMatcher<Object> sameMatcher = new Same(wantedChar);

        char sameChar = wantedChar;
        Assert.assertTrue(sameMatcher.matches(sameChar));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSameThrowsExceptionWithNullMatcher() {
        new Same(null);
    }
}