package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatcher.argThat;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.exceptions.InvocationNotVerifiedException;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.verification.ArgumentsComparator;
import org.mockito.internal.verification.VerifiedInvocation;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockitoMock;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherWithEmptyMatcherList() {
        // Given
        Invocation invocation = new InvocationOnMock(new MockitoMock(Object.class), new Method(String.class.getName(), String[].class), new Location("TestClass.java", 1, 1), new Object[0]);

        // When
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.<Matcher>emptyList());

        // Then
        assertTrue(invocationMatcher.matches(invocation));
    }

    @Test
    public void testInvocationMatcherWithMatchers() {
        // Given
        Invocation invocation = new InvocationOnMock(new MockitoMock(Object.class), new Method("method", String.class, int.class), new Location("TestClass.java", 1, 1), new Object[]{"foo", 1});
        List<Matcher> matchers = Arrays.asList(argThat((Matcher<String>)(String s) -> s.equals("foo")), argThat((Matcher<Integer>)(Integer i) -> i.equals(1)));

        // When
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Then
        Invocation verificationInvocation = new InvocationOnMock(new MockitoMock(Object.class), new Method("method", String.class, int.class), new Location("TestClass.java", 2, 2), new Object[]{"bar", 2});
        assertFalse(invocationMatcher.matches(verificationInvocation));

        Invocation actualInvocation = new InvocationOnMock(new MockitoMock(Object.class), new Method("method", String.class, int.class), new Location("TestClass.java", 3, 3), new Object[]{"foo", 1});
        assertTrue(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testToString() {
        // Given
        Invocation invocation = new InvocationOnMock(new MockitoMock(Object.class), new Method("method", String.class, int.class), new Location("TestClass.java", 1, 1), new Object[]{"foo", 1});
        List<Matcher> matchers = Arrays.asList(argThat((Matcher<String>)(String s) -> s.equals("foo")), argThat((Matcher<Integer>)(Integer i) -> i.equals(1)));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // When
        String actual = invocationMatcher.toString();

        // Then
        String expected = "Invocation [method = method(java.lang.String, int), arguments = [foo, 1], mocks = [MockMockMethodInvocation: mock: 2006682642], location = TestClass.java:1:1]";
        assertEquals(expected, actual);
    }

    @Test
    public void testToStringWithPrintSettings() {
        // Given
        Invocation invocation = new InvocationOnMock(new MockitoMock(Object.class), new Method("method", String.class, int.class), new Location("TestClass.java", 1, 1), new Object[]{"foo", 1});
        List<Matcher> matchers = Arrays.asList(argThat((Matcher<String>)(String s) -> s.equals("foo")), argThat((Matcher<Integer>)(Integer i) -> i.equals(1)));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        PrintSettings printSettings = new PrintSettings();

        // When
        String actual = invocationMatcher.toString(printSettings);

        // Then
        String expected = "Invocation:\n" +
                "  method:\n" +
                "    methodName: method\n" +
                "    parameterTypes: [class java.lang.String, class int]\n" +
                "  arguments:\n" +
                "    [0]: foo\n" +
                "    [1]: 1\n" +
                "  mocks:\n" +
                "    [0]: MockMockMethodInvocation: mock: 2006682642\n" +
                "  location:\n" +
                "    classFile: TestClass.java\n" +
                "    lineNumber: 1\n" +
                "    columnNumber: 1";
        assertEquals(expected, actual);
    }

    @Test
    public void testHasSameMethod() {
        // Given
        Invocation invocation1 = new InvocationOnMock(new MockitoMock(Object.class), new Method("method1", String.class), new Location("TestClass.java", 1, 1), new Object[]{"foo"});
        Invocation invocation2 = new InvocationOnMock(new MockitoMock(Object.class), new Method("method2", String.class), new Location("TestClass.java", 2, 2), new Object[]{"foo"});

        // When
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        // Then
        assertTrue(invocationMatcher.hasSameMethod(invocation1));
        assertFalse(invocationMatcher.hasSameMethod(invocation2));
    }

    @Test
    public void testHasSimilarMethod() {
        // Given
        Invocation invocation1 = new InvocationOnMock(new MockitoMock(Object.class), new Method("method1", String.class), new Location("TestClass.java", 1, 1), new Object[]{"foo"});
        Invocation invocation2 = new InvocationOnMock(new MockitoMock(Object.class), new Method("method2", String[].class), new Location("TestClass.java", 2, 2), new Object[]{new String[]{"foo"}});

        // When
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        // Then
        assertFalse(invocationMatcher.hasSimilarMethod(invocation2));

        Invocation invocation3 = new InvocationOnMock(new MockitoMock(Object.class), new Method("method1", String.class), new Location("TestClass.java", 3, 3), new Object[]{"foo"});

        // Then
        assertTrue(invocationMatcher.hasSimilarMethod(invocation3));
    }

    @Test
    public void testMatches() {
        // Given
        Invocation invocation = new InvocationOnMock(new MockitoMock(Object.class), new Method("method", String.class, int.class), new Location("TestClass.java", 1, 1), new Object[]{"foo", 1});
        List<Matcher> matchers = Arrays.asList(argThat((Matcher<String>)(String s) -> s.equals("foo")), argThat((Matcher<Integer>)(Integer i) -> i.equals(1)));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // When & Then
        Invocation actualInvocation = new InvocationOnMock(new MockitoMock(Object.class), new Method("method", String.class, int.class), new Location("TestClass.java", 2, 2), new Object[]{"foo", 1});
        assertTrue(invocationMatcher.matches(actualInvocation));

        Invocation notMatchingInvocation = new InvocationOnMock(new MockitoMock(Object.class), new Method("method", String.class, int.class), new Location("TestClass.java", 3, 3), new Object[]{"bar", 2});
        assertFalse(invocationMatcher.matches(notMatchingInvocation));

        Invocation nullInvocation = null;
        assertFalse(invocationMatcher.matches(nullInvocation));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        // Given
        Invocation invocation = new InvocationOnMock(new MockitoMock(Object.class), new Method("method", String.class, int.class), new Location("TestClass.java", 1, 1), new Object[]{"foo", 1});
        List<Matcher> matchers = Arrays.asList(new CapturesArguments());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // When
        invocationMatcher.captureArgumentsFrom(invocation);

        // Then
        Object[] capturedArguments = invocationMatcher.getMatchers().get(0).getActual();
        assertEquals("foo", capturedArguments[0]);
        assertEquals(1, capturedArguments[1].intValue());
    }

    @Test(expected = InvocationNotVerifiedException.class)
    public void testMatchesFailsWhenInvocationNotVerified() {
        // Given
        Invocation invocation = new InvocationOnMock(new MockitoMock(Object.class), new Method("method", String.class, int.class), new Location("TestClass.java", 1, 1), new Object[]{"foo", 1});
        List<Matcher> matchers = Arrays.asList(argThat((Matcher<String>)(String s) -> s.equals("foo")));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // When
        Invocation notVerifiedInvocation = new InvocationOnMock(new MockitoMock(Object.class), new Method("method", String.class, int.class), new Location("TestClass.java", 2, 2), new Object[]{"bar", 2});

        // Then
        assertFalse(invocationMatcher.matches(notVerifiedInvocation));

        // Verify
        invocation.proceed();
    }

    @Test
    public void testSafelyArgumentsMatch() {
        // Given
        Invocation invocation = new InvocationOnMock(new MockitoMock(Object.class), new Method("method", String[].class, int.class), new Location("TestClass.java", 1, 1), new Object[]{new String[]{"foo", "bar"}});

        // When
        boolean result = new InvocationMatcher(invocation, Collections.<Matcher>emptyList()).safelyArgumentsMatch(new Object[]{"foo", "bar"});

        // Then
        assertTrue(result);
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatchThrowsException() {
        // Given
        Invocation invocation = new InvocationOnMock(new MockitoMock(Object.class), new Method("method", String[].class, int.class), new Location("TestClass.java", 1, 1), new Object[]{new String[]{"foo", "bar"}});

        // When & Then
        new InvocationMatcher(invocation, Collections.<Matcher>emptyList()).safelyArgumentsMatch(new Object[]{"foo", "baz"});
    }
}