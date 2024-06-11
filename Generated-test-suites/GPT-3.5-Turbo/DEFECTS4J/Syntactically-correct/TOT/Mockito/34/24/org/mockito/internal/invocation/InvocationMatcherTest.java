
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.exceptions.util.Formatting;
import org.mockito.internal.invocation.ArgumentsComparator;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.mock.MockCreationSettings;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(CoreMatchers.any(String.class));
        matchers.add(CoreMatchers.any(Integer.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithInvocationEmptyMatchers() {
        Invocation invocation = mock(Invocation.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(invocation.argumentsToMatchers(), invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(CoreMatchers.any(String.class));
        matchers.add(CoreMatchers.any(Integer.class));
        String expectedToString = "InvocationMatcher.toString";

        Mockito.when(invocation.toString(matchers, new PrintSettings())).thenReturn(expectedToString);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(expectedToString, invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation invocation = mock(Invocation.class);
        Invocation actual = mock(Invocation.class);

        Mockito.when(invocation.getMock()).thenReturn("mock1");
        Mockito.when(actual.getMock()).thenReturn("mock1");

        Mockito.when(invocation.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(actual.getMethod()).thenReturn(mock(Method.class));

        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        Mockito.when(argumentsComparator.argumentsMatch(invocation, actual)).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.matches(actual);

        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);

        Mockito.when(invocation.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(invocation.getLocation()).thenReturn(mock(Location.class));
        Mockito.when(candidate.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(candidate.getLocation()).thenReturn(mock(Location.class));

        Mockito.when(candidate.isVerified()).thenReturn(false);
        Mockito.when(candidate.getMock()).thenReturn(invocation.getMock());
        Mockito.when(candidate.getArguments()).thenReturn(invocation.getArguments());

        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        Mockito.when(argumentsComparator.argumentsMatch(invocation, candidate.getArguments())).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethod_MethodNameNotEqual() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);

        Mockito.when(invocation.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(invocation.getLocation()).thenReturn(mock(Location.class));
        Mockito.when(candidate.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(candidate.getLocation()).thenReturn(mock(Location.class));

        Mockito.when(invocation.getMethod().getName()).thenReturn("method1");
        Mockito.when(candidate.getMethod().getName()).thenReturn("method2");

        Mockito.when(candidate.isVerified()).thenReturn(false);
        Mockito.when(candidate.getMock()).thenReturn(invocation.getMock());
        Mockito.when(candidate.getArguments()).thenReturn(invocation.getArguments());

        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        Mockito.when(argumentsComparator.argumentsMatch(invocation, candidate.getArguments())).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_Verified() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);

        Mockito.when(invocation.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(invocation.getLocation()).thenReturn(mock(Location.class));
        Mockito.when(candidate.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(candidate.getLocation()).thenReturn(mock(Location.class));

        Mockito.when(invocation.getMethod().getName()).thenReturn("method1");
        Mockito.when(candidate.getMethod().getName()).thenReturn("method1");

        Mockito.when(candidate.isVerified()).thenReturn(true);
        Mockito.when(candidate.getMock()).thenReturn(invocation.getMock());
        Mockito.when(candidate.getArguments()).thenReturn(invocation.getArguments());

        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        Mockito.when(argumentsComparator.argumentsMatch(invocation, candidate.getArguments())).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_MockNotEqual() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);

        Mockito.when(invocation.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(invocation.getLocation()).thenReturn(mock(Location.class));
        Mockito.when(candidate.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(candidate.getLocation()).thenReturn(mock(Location.class));

        Mockito.when(invocation.getMethod().getName()).thenReturn("method1");
        Mockito.when(candidate.getMethod().getName()).thenReturn("method1");

        Mockito.when(candidate.isVerified()).thenReturn(false);
        Mockito.when(candidate.getMock()).thenReturn("mock1");
        Mockito.when(invocation.getMock()).thenReturn("mock2");
        Mockito.when(candidate.getArguments()).thenReturn(invocation.getArguments());

        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        Mockito.when(argumentsComparator.argumentsMatch(invocation, candidate.getArguments())).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_ArgumentsNotEqual() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);

        Mockito.when(invocation.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(invocation.getLocation()).thenReturn(mock(Location.class));
        Mockito.when(candidate.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(candidate.getLocation()).thenReturn(mock(Location.class));

        Mockito.when(invocation.getMethod().getName()).thenReturn("method1");
        Mockito.when(candidate.getMethod().getName()).thenReturn("method1");

        Mockito.when(candidate.isVerified()).thenReturn(false);
        Mockito.when(candidate.getMock()).thenReturn(invocation.getMock());

        Object[] arguments = {"arg1"};
        Mockito.when(candidate.getArguments()).thenReturn(arguments);

        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        Mockito.when(argumentsComparator.argumentsMatch(invocation, arguments)).thenReturn(false);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(result);
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = mock(Invocation.class);
        Invocation candidate = mock(Invocation.class);

        Mockito.when(invocation.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(candidate.getMethod()).thenReturn(mock(Method.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        boolean result = invocationMatcher.hasSameMethod(candidate);

        assertTrue(result);
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);

        Mockito.when(invocation.getLocation()).thenReturn(location);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Location result = invocationMatcher.getLocation();

        assertEquals(location, result);
    }

    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(CoreMatchers.any(String.class));
        matchers.add(CoreMatchers.any(Integer.class));
        String expectedToString = "InvocationMatcher.toString";

        Mockito.when(invocation.toString(matchers, new PrintSettings())).thenReturn(expectedToString);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(expectedToString, invocationMatcher.toString(new PrintSettings()));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        Invocation i = mock(Invocation.class);
        Object[] arguments = {"arg1"};
        Mockito.when(i.getArguments()).thenReturn(arguments);

        List<Matcher> matchers = new ArrayList<>();
        Matcher capturesArguments = mock(Matcher.class);
        matchers.add(capturesArguments);

        CapturesArguments capturesArgumentsMatcher = mock(CapturesArguments.class);
        Mockito.when(capturesArgumentsMatcher.captureFrom(arguments[0])).thenReturn(true);

        matchers.add(capturesArgumentsMatcher);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(i);

        Mockito.verify(capturesArgumentsMatcher, Mockito.times(1)).captureFrom(arguments[0]);
    }

}
