package org.mockito.internal.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class SameTest {

    @Test
    public void testSameInstance() {
        Object wanted = new Object();
        Same same = new Same(wanted);
        Object actual = wanted;

        Matcher<Object> matcher = same;
        boolean result = matcher.matches(actual);
        assertEquals(true, result);
    }

    @Test
    public void testDifferentInstances() {
        Object wanted = new Object();
        Object actual = new Object();

        Same same = new Same(wanted);
        Matcher<Object> matcher = same;
        boolean result = matcher.matches(actual);
        assertEquals(false, result);
    }

    @Test
    public void testSameClassAndSameValue() {
        String wanted = "test";
        Same same = new Same(wanted);
        String actual = "test";

        Matcher<String> matcher = same;
        boolean result = matcher.matches(actual);
        assertEquals(true, result);
    }

    @Test
    public void testSameClassAndDifferentValue() {
        String wanted = "test";
        String actual = "test1";

        Same same = new Same(wanted);
        Matcher<String> matcher = same;
        boolean result = matcher.matches(actual);
        assertEquals(false, result);
    }

    @Test
    public void testDifferentClassesButSameValue() {
        Integer wanted = 1;
        Object actual = 1;

        Same same = new Same(wanted);
        Matcher<Object> matcher = same;
        boolean result = matcher.matches(actual);
        assertEquals(true, result);
    }

    @Test
    public void testNullValue() {
        Same same = new Same(null);
        Object actual = null;

        Matcher<Object> matcher = same;
        boolean result = matcher.matches(actual);
        assertEquals(true, result);
    }

    @Test
    public void testToString() {
        Object wanted = new Object();
        Same same = new Same(wanted);

        String actual = same.toString();
        String expected = "org.mockito.internal.matchers.Same@1540e19";

        assertEquals(expected, actual);
    }

    @Test
    public void testDescribeTo() {
        Object wanted = new Object();
        Same same = new Same(wanted);

        Description description = mock(Description.class);
        same.describeTo(description);

        String actual = description.toString();
        String expected = "same(<object>test</object>)";

        assertEquals(expected, actual);
    }
}