import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.hamcrest.Matcher;
import org.mockito.invocation.Invocation;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.invocation.Location;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.invocation.ArgumentsComparator;
import org.mockito.internal.invocation.ArgumentsProcessor;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.internal.reporting.PrintSettings;
import org.hamcrest.Matcher;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithoutMatchers() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        PrintSettings printSettings = mock(PrintSettings.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(mock(Matcher.class));
        when(invocation.getPrintSettings()).thenReturn(printSettings);
        String expected = "test string";
        when(printSettings.print(matchers, invocation)).thenReturn(expected);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Object[] arguments1 = new Object[]{"arg1", "arg2"};
        Object[] arguments2 = new Object[]{"arg3", "arg4"};
        when(invocation1.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getMock()).thenReturn(mock(Object.class));
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        when(invocation1.getArguments()).thenReturn(arguments1);
        when(invocation2.getArguments()).thenReturn(arguments2);
        when(new ArgumentsComparator().argumentsMatch(invocation1, invocation2)).thenReturn(true);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertTrue(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method);
        when(invocation2.getMethod()).thenReturn(method);
        when(invocation1.isVerified()).thenReturn(false);
        when(new ArgumentsComparator().argumentsMatch(invocation1, invocation2)).thenReturn(true);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertTrue(invocationMatcher.hasSimilarMethod(invocation2));
    }

    @Test
    public void testHasNotSimilarMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);
        when(invocation1.isVerified()).thenReturn(false);
        when(new ArgumentsComparator().argumentsMatch(invocation1, invocation2)).thenReturn(false);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertFalse(invocationMatcher.hasSimilarMethod(invocation2));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method);
        when(invocation2.getMethod()).thenReturn(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertTrue(invocationMatcher.hasSameMethod(invocation2));
    }

    @Test
    public void testHasNotSameMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertFalse(invocationMatcher.hasSameMethod(invocation2));
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        Object argument1 = "arg1";
        Object argument2 = "arg2";
        Object[] arguments = new Object[]{argument1, argument2};
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation1.isVarArgs()).thenReturn(false);
        when(invocation1.getRawArguments()).thenReturn(arguments);
        when(matcher1 instanceof CapturesArguments).thenReturn(true);
        when(matcher2 instanceof CapturesArguments).thenReturn(false);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(matcher1);
        matchers.add(matcher2);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, matchers);

        invocationMatcher.captureArgumentsFrom(invocation2);

        verify((CapturesArguments)matcher1).captureFrom(argument1);
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(invocation1);
        invocations.add(invocation2);
        InvocationMatcher invocationMatcher1 = mock(InvocationMatcher.class);
        InvocationMatcher invocationMatcher2 = mock(InvocationMatcher.class);
        whenNew(InvocationMatcher.class).withArguments(invocation1).thenReturn(invocationMatcher1);
        whenNew(InvocationMatcher.class).withArguments(invocation2).thenReturn(invocationMatcher2);

        List<InvocationMatcher> result = InvocationMatcher.createFrom(invocations);

        assertEquals(2, result.size());
        assertTrue(result.contains(invocationMatcher1));
        assertTrue(result.contains(invocationMatcher2));
    }

}