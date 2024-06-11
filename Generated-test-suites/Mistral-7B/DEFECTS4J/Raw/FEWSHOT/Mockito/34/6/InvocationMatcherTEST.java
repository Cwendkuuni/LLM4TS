package org.mockito.internal.invocation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgThat.argThat;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.exceptions.verification.InvocationNotProperlySetException;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockCreationSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

public class InvocationMatcherTest {

    @Test
    public void testNewInvocationMatcher() {
        Invocation invocation = new InvocationMock(new MockCreationSettings().defaultValue(), new Object[]{}, new Location(1, 1), "someMethod");
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertNotNull(matcher);
        assertTrue(matcher instanceof PrintableInvocation);
        assertTrue(matcher instanceof PrintingFriendlyInvocation);
        assertTrue(matcher instanceof CapturesArgumensFromInvocation);
        assertTrue(matcher instanceof Serializable);
        assertEquals(invocation.getMethod(), matcher.getMethod());
        assertTrue(matcher.matches(invocation));
    }

    @Test
    public void testNewInvocationMatcherWithMatchers() {
        Invocation invocation = new InvocationMock(new MockCreationSettings().defaultValue(), new Object[]{"arg1", "arg2"}, new Location(1, 1), "someMethod");
        List<Matcher> matchers = Collections.singletonList(argThat(new ArgumentMatcher<Object>() {
            @Override
            public boolean matches(Object argument) {
                return "arg1".equals(argument);
            }
        }));

        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertNotNull(matcher);
        assertTrue(matcher.getMatchers().size() == 1);
        assertTrue(matcher.matches(invocation));
        assertFalse(matcher.matches(new InvocationMock(new MockCreationSettings().defaultValue(), new Object[]{"arg1", "arg3"}, new Location(1, 1), "someMethod")));
    }

    @Test(expected = InvocationNotProperlySetException.class)
    public void testNewInvocationMatcherWithNullInvocation() {
        List<Matcher> matchers = Collections.singletonList(Matchers.is("someValue"));
        new InvocationMatcher(null, matchers);
    }

    @Test
    public void testNewInvocationMatcherWithEmptyMatchersList() {
        Invocation invocation = new InvocationMock(new MockCreationSettings().defaultValue(), new Object[]{}, new Location(1, 1), "someMethod");
        InvocationMatcher matcher = new InvocationMatcher(invocation, Collections.emptyList());

        assertNotNull(matcher);
        assertTrue(matcher.getMatchers().isEmpty());
        assertTrue(matcher.matches(invocation));
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = new InvocationMock(new MockCreationSettings().defaultValue(), new Object[]{"arg1", "arg2"}, new Location(1, 1), "someMethod");
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertNotNull(matcher.getInvocation());
        assertEquals(invocation, matcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = new InvocationMock(new MockCreationSettings().defaultValue(), new Object[]{"arg1", "arg2"}, new Location(1, 1), "someMethod");
        List<Matcher> matchers = Collections.singletonList(argThat(new ArgumentMatcher<Object>() {
            @Override
            public boolean matches(Object argument) {
                return "arg1".equals(argument);
            }
        }));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertEquals(matchers, matcher.getMatchers());
    }

    @Test
    public void testToString() {
        Invocation invocation = new InvocationMock(new MockCreationSettings().defaultValue(), new Object[]{"arg1", "arg2"}, new Location(1, 1), "someMethod");
        List<Matcher> matchers = Collections.singletonList(argThat(new ArgumentMatcher<Object>() {
            @Override
            public boolean matches(Object argument) {
                return "arg1".equals(argument);
            }
        }));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        String expected = "Invocation on someMock for someMethod() with arguments [arg1, arg2]";
        String actual = matcher.toString();
        assertTrue(expected.equals(actual));
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = new InvocationMock(new MockCreationSettings().defaultValue(), new Object[]{"arg1", "arg2"}, new Location(1, 1), "someMethod");
        Invocation invocation2 = new InvocationMock(new MockCreationSettings().defaultValue(), new Object[]{"arg1", "arg3"}, new Location(1, 1), "someMethod");
        InvocationMatcher matcher = new InvocationMatcher(invocation1);

        assertTrue(matcher.matches(invocation1));
        assertFalse(matcher.matches(invocation2));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = new InvocationMock(new MockCreationSettings().defaultValue(), new Object[]{"arg1", "arg2"}, new Location(1, 1), "someMethod");
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        Object[] actualArgs = new Object[]{"arg1", "arg3"};
        assertTrue(matcher.safelyArgumentsMatch(invocation.getArguments()));
        assertFalse(matcher.safelyArgumentsMatch(actualArgs));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = new InvocationMock(new MockCreationSettings().defaultValue(), new Object[]{"arg1", "arg2"}, new Location(1, 1), "someMethod");
        Invocation invocation2 = new InvocationMock(new MockCreationSettings().defaultValue(), new Object[]{"arg1", "arg3"}, new Location(1, 1), "someMethod");
        InvocationMatcher matcher = new InvocationMatcher(invocation1);

        assertTrue(matcher.hasSimilarMethod(invocation1));
        assertFalse(matcher.hasSimilarMethod(invocation2));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = new InvocationMock(new MockCreationSettings().defaultValue(), new Object[]{"arg1", "arg2"}, new Location(1, 1), "someMethod");
        Invocation invocation2 = new InvocationMock(new MockCreationSettings().defaultValue(), new Object[]{"arg1", "arg2"}, new Location(1, 1), "someMethod");
        InvocationMatcher matcher = new InvocationMatcher(invocation1);

        assertTrue(matcher.hasSameMethod(invocation1));
        assertTrue(matcher.hasSameMethod(invocation2));
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = new InvocationMock(new MockCreationSettings().defaultValue(), new Object[]{"arg1", "arg2"}, new Location(1, 1), "someMethod");
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertEquals(invocation.getLocation(), matcher.getLocation());
    }

    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = new InvocationMock(new MockCreationSettings().defaultValue(), new Object[]{"arg1", "arg2"}, new Location(1, 1), "someMethod");
        List<Matcher> matchers = Collections.singletonList(argThat(new ArgumentMatcher<Object>() {
            @Override
            public boolean matches(Object argument) {
                return "arg1".equals(argument);
            }
        }));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        PrintSettings printSettings = new PrintSettings();
        printSettings.setPrintClassNames(true);

        String expected = "Invocation on someMock for someMethod(Object arg1, Object arg2) arguments:\n" +
                "arg1 = arg1\n" +
                "arg2 = arg2";
        String actual = matcher.toString(printSettings);
        assertTrue(expected.equals(actual));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new InvocationMock(new MockCreationSettings().defaultValue(), new Object[]{"arg1", "arg2"}, new Location(1, 1), "someMethod");
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        CapturesArguments captor = argThat(new ArgumentMatcher<CapturesArguments>() {
            @Override
            public boolean matches(CapturesArguments argument) {
                return argument.getArgument(0).equals("arg1");
            }
        });

        matcher.captureArgumentsFrom(invocation);
        assertTrue(captor.getValue().size() == 1);
        assertEquals("arg1", captor.getValue().get(0));
    }
}