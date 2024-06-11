package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatcher.argumentsAreEqual;
import static org.mockito.internal.matchers.ArgumentsMatcher.argumentsAreSame;
import static org.mockito.internal.matchers.ArgumentsMatcher.argumentsWithSameClassTypes;
import static org.mockito.internal.matchers.ArgumentsMatcher.instanceOf;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.InvocationOnMockImpl;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.util.reflection.FieldAccessible;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {
    @Test
    public void testConstructorWithInvocationAndEmptyMatcherList() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher im = new InvocationMatcher(invocation);

        assertFalse(im.hasMatchers());
        assertEquals(invocation, im.getInvocation());
        assertEquals(Collections.<Matcher>emptyList(), im.getMatchers());
    }

    @Test
    public void testConstructorWithInvocationAndMatcherList() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(instanceOf(String.class), argumentsAreEqual("foo"));
        InvocationMatcher im = new InvocationMatcher(invocation, matchers);

        assertTrue(im.hasMatchers());
        assertEquals(invocation, im.getInvocation());
        assertEquals(matchers, im.getMatchers());
    }

    @Test
    public void testToString() {
        List<Matcher> matchers = Arrays.asList(instanceOf(String.class), argumentsAreEqual("foo"));
        InvocationMatcher im = new InvocationMatcher(Mockito.mock(InvocationOnMock.class), matchers);
        String expected = String.format("InvocationMatcher[%s%n%s]", im.getInvocation().toString(), Arrays.toString(matchers.toArray()));
        String actual = im.toString();

        assertTrue(actual.startsWith(expected));
    }

    @Test
    public void testMatches() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(instanceOf(String.class), argumentsAreEqual("foo"));
        InvocationMatcher im = new InvocationMatcher(invocation, matchers);
        InvocationOnMockImpl actual = Mockito.mock(InvocationOnMockImpl.class);

        Method method = Mockito.mock(Method.class);
        FieldAccessible fieldAccessible = new FieldAccessible();
        fieldAccessible.setAccessible(true);
        try {
            fieldAccessible.setValue(actual, new Method("foo", String[].class));
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        assertTrue(im.matches(actual));

        String actualArg1 = Mockito.mock(String.class);
        String[] actualArgs = new String[]{actualArg1};
        InvocationOnMockImpl invocationWithArguments = new InvocationOnMockImpl(method, new Object[0], actualArgs);

        assertTrue(im.matches(invocationWithArguments));

        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(invocation).invoke(stringCaptor.capture());
        assertEquals("foo", stringCaptor.getValue());

        InvocationOnMockImpl invocationWithDifferentMethod = new InvocationOnMockImpl(Mockito.mock(Method.class), new Object[0], actualArgs);
        assertFalse(im.matches(invocationWithDifferentMethod));

        InvocationOnMockImpl invocationWithSameMethodButDifferentArguments = new InvocationOnMockImpl(method, new Object[0], Arrays.asList("bar", "baz").toArray());
        assertFalse(im.matches(invocationWithSameMethodButDifferentArguments));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        List<Matcher> matchers = Arrays.asList(instanceOf(String.class), argumentsAreEqual("foo"));
        InvocationMatcher im = new InvocationMatcher(Mockito.mock(InvocationOnMock.class), matchers);
        String[] arguments = new String[]{"foo"};

        assertTrue(im.safelyArgumentsMatch(arguments));

        InvocationMatcher im2 = new InvocationMatcher(Mockito.mock(InvocationOnMock.class), matchers);
        assertFalse(im.safelyArgumentsMatch(im2.getInvocation().getArguments()));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);

        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);

        InvocationMatcher im1 = new InvocationMatcher(invocation1, Collections.<Matcher>emptyList());
        InvocationMatcher im2 = new InvocationMatcher(invocation2, Collections.<Matcher>emptyList());

        try {
            FieldAccessible fieldAccessible = new FieldAccessible();
            fieldAccessible.setAccessible(true);
            fieldAccessible.setValue(invocation1, method1);
            fieldAccessible.setValue(invocation2, method2);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        assertTrue(im1.hasSimilarMethod(im2));

        assertFalse(im1.hasSimilarMethod(new InvocationMatcher(Mockito.mock(InvocationOnMock.class), Collections.<Matcher>emptyList())));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);

        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);

        InvocationMatcher im1 = new InvocationMatcher(invocation1, Collections.<Matcher>emptyList());
        InvocationMatcher im2 = new InvocationMatcher(invocation2, Collections.<Matcher>emptyList());

        try {
            FieldAccessible fieldAccessible = new FieldAccessible();
            fieldAccessible.setAccessible(true);
            fieldAccessible.setValue(invocation1, method1);
            fieldAccessible.setValue(invocation2, method2);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        assertTrue(im1.hasSameMethod(im2));

        assertFalse(im1.hasSameMethod(new InvocationMatcher(Mockito.mock(InvocationOnMock.class), Collections.<Matcher>emptyList())));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(instanceOf(String.class), argumentsAreEqual("foo"));
        InvocationMatcher im = new InvocationMatcher(invocation, matchers);
        String arg = "foo";

        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(invocation).invoke(stringCaptor.capture());

        im.captureArgumentsFrom(invocation);

        assertEquals(arg, stringCaptor.getValue());
    }

    @Test
    public void testHasMatchers() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(instanceOf(String.class), argumentsAreEqual("foo"));
        InvocationMatcher im = new InvocationMatcher(invocation, matchers);

        assertTrue(im.hasMatchers());

        List<Matcher> emptyList = Collections.emptyList();
        InvocationMatcher im2 = new InvocationMatcher(invocation, emptyList);

        assertFalse(im2.hasMatchers());
    }

    @Test
    public void testToStringWithPrintSettings() {
        List<Matcher> matchers = Arrays.asList(instanceOf(String.class), argumentsAreEqual("foo"));
        InvocationMatcher im = new InvocationMatcher(Mockito.mock(InvocationOnMock.class), matchers);
        PrintSettings printSettings = new PrintSettings();
        printSettings.setPrintClassNames(true);

        String expected = String.format("InvocationMatcher[%s%n%s]", im.getInvocation().toString(printSettings), Arrays.toString(matchers.toArray()));
        String actual = im.toString(printSettings);

        assertTrue(actual.startsWith(expected));
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Location location = Mockito.mock(Location.class);
        InvocationMatcher im = new InvocationMatcher(invocation, Collections.<Matcher>emptyList());

        try {
            FieldAccessible fieldAccessible = new FieldAccessible();
            fieldAccessible.setAccessible(true);
            fieldAccessible.setValue(invocation, location);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        assertEquals(location, im.getLocation());
    }
}