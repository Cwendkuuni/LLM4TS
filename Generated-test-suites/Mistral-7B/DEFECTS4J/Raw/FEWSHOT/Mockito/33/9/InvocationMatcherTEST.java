package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentMatchers.argThat;
import static org.mockito.internal.matchers.is;
import static org.mockito.internal.matchers.not;
import static org.mockito.internal.reporting.PrintSettings.printSetting;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.util.reflection.FieldAccessUtils;
import org.mockito.internal.util.reflection.MethodInvocationUtils;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockComponentImplementation;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = createInvocation(mockitoTestClass(), "testMethod", new Object[0]);
        List<Matcher> matchers = Collections.singletonList(is(1));

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertTrue(matcher.matches(invocation));
    }

    @Test
    public void testConstructorWithoutMatchers() {
        Invocation invocation = createInvocation(mockitoTestClass(), "testMethod", new Object[0]);

        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertTrue(matcher.matches(invocation));
    }

    @Test
    public void testMatchesWithSameInvocation() {
        Invocation invocation = createInvocation(mockitoTestClass(), "testMethod", new Object[0]);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertTrue(matcher.matches(invocation));
    }

    @Test
    public void testMatchesWithDifferentInvocationButSameMethod() {
        Invocation invocation1 = createInvocation(mockitoTestClass(), "testMethod", new Object[]{1});
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = createInvocation(mockitoTestClass(), "testMethod", new Object[]{2});
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        assertTrue(matcher1.hasSimilarMethod(matcher2));
        assertTrue(matcher1.matches(invocation2));
    }

    @Test
    public void testMatchesWithDifferentInvocationButDifferentMethod() {
        Invocation invocation1 = createInvocation(mockitoTestClass(), "testMethod1", new Object[]{1});
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = createInvocation(mockitoTestClass(), "testMethod2", new Object[]{2});
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        assertFalse(matcher1.matches(invocation2));
        assertFalse(matcher1.hasSimilarMethod(matcher2));
    }

    @Test
    public void testMatchesWithNullInvocation() {
        InvocationMatcher matcher = new InvocationMatcher(null);
        assertFalse(matcher.matches(null));
    }

    @Test
    public void testToString() {
        Invocation invocation = createInvocation(mockitoTestClass(), "testMethod", new Object[0]);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        String expected = String.format("InvocationMatcher%n%s%nargs %s", matcher.getLocation(), matcher.getMethod().getName());
        String actual = matcher.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = createInvocation(mockitoTestClass(), "testMethod", new Object[0]);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        String expected = String.format("InvocationMatcher%n%s%nargs : %s%n%s%n",
                matcher.getLocation(), printArgValues(matcher.getMethod().getParameters()),
                printSetting("toString", matcher.getMatchers()));
        String actual = matcher.toString(new PrintSettings(true, true));

        assertEquals(expected, actual);
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        Invocation invocation = createInvocation(mockitoTestClass(), "testMethod", new Object[]{1, "foo"});
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        CapturesArguments captureArguments = mock(CapturesArguments.class);

        matcher.captureArgumentsFrom(invocation);

        verify(captureArguments).captureFrom(eq(1));
        verify(captureArguments).captureFrom("foo");
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = createInvocation(mockitoTestClass(), "testMethod", new Object[]{1});
        Invocation invocation2 = createInvocation(mockitoTestClass(), "testMethod", new Object[]{2});

        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(Arrays.asList(invocation1, invocation2));

        InvocationMatcher matcher1 = matchers.get(0);
        InvocationMatcher matcher2 = matchers.get(1);

        assertTrue(matcher1.matches(invocation1));
        assertTrue(matcher2.matches(invocation2));
        assertFalse(matcher1.matches(invocation2));
        assertFalse(matcher2.matches(invocation1));
    }

    private Invocation createInvocation(Class<?> clazz, String methodName, Object[] arguments) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, arguments.getClass());
            method.setAccessible(true);

            Object mock = mock(clazz, new Answer() {
                @Override
                public Object answer(InvocationOnMock invocation) throws Throwable {
                    return null;
                }
            });

            Stubber stubber = mock(Stubber.class);
            MethodInvocationUtils.invokeMethod(stubber, "doReturn", mock);

            return new InvocationOnMock(method, mock, arguments);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object mockitoTestClass() {
        return mock(MockComponentImplementation.class);
    }

    private static String printArgValues(Method method) {
        String args = "";

        for (Object parameter : method.getParameters()) {
            if (!args.isEmpty()) {
                args += ", ";
            }
            args += printValue(parameter);
        }

        return args;
    }

    private static String printValue(Object value) {
        if (value instanceof String) {
            String strValue = (String) value;
            return "\"" + strValue.replace("\"", "\\\"") + "\"";
        } else {
            return FieldAccessUtils.getShortClassName(value.getClass());
        }
    }
}