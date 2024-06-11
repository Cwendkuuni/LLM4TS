import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.DescribedInvocation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    private Invocation invocation;
    private List<Matcher> matchers;

    @Before
    public void setup() {
        invocation = mock(Invocation.class);
        matchers = new ArrayList<>();
    }

    @Test
    public void testConstructor_withMatchers() {
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        matchers.add(matcher1);
        matchers.add(matcher2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructor_noMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testGetMethod() {
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        PrintSettings printSettings = mock(PrintSettings.class);
        when(invocation.getArguments()).thenReturn(new Object[]{});
        when(new PrintSettings().print(matchers, invocation)).thenReturn("toString value");

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals("toString value", invocationMatcher.toString());

        verify(new PrintSettings(), times(1)).print(matchers, invocation);
    }

    @Test
    public void testMatches_allConditionsTrue() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn("mock");
        when(actual.getMock()).thenReturn("mock");
        when(invocation.getMethod()).thenReturn(method("testMethod", void.class));
        when(actual.getMethod()).thenReturn(method("testMethod", void.class));
        when(new ArgumentsComparator().argumentsMatch(invocation, actual)).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertTrue(invocationMatcher.matches(actual));

        verify(invocation, times(1)).getMock();
        verify(actual, times(1)).getMock();
        verify(invocation, times(1)).getMethod();
        verify(actual, times(1)).getMethod();
        verify(new ArgumentsComparator(), times(1)).argumentsMatch(invocation, actual);
    }

    @Test
    public void testMatches_notMock() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn("mock");
        when(actual.getMock()).thenReturn("not-mock");

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertFalse(invocationMatcher.matches(actual));

        verify(invocation, times(1)).getMock();
        verify(actual, times(1)).getMock();
    }

    @Test
    public void testMatches_differentMethods() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method("testMethod1", void.class));
        when(actual.getMethod()).thenReturn(method("testMethod2", void.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertFalse(invocationMatcher.matches(actual));

        verify(invocation, times(1)).getMethod();
        verify(actual, times(1)).getMethod();
    }

    @Test
    public void testMatches_argumentsMismatch() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn("mock");
        when(actual.getMock()).thenReturn("mock");
        when(invocation.getMethod()).thenReturn(method("testMethod", void.class));
        when(actual.getMethod()).thenReturn(method("testMethod", void.class));
        when(new ArgumentsComparator().argumentsMatch(invocation, actual)).thenReturn(false);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertFalse(invocationMatcher.matches(actual));

        verify(invocation, times(1)).getMock();
        verify(actual, times(1)).getMock();
        verify(invocation, times(1)).getMethod();
        verify(actual, times(1)).getMethod();
        verify(new ArgumentsComparator(), times(1)).argumentsMatch(invocation, actual);
    }

    @Test
    public void testHasSimilarMethod_allConditionsTrue() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method("testMethod", void.class));
        when(candidate.getMethod()).thenReturn(method("testMethod", void.class));
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn("mock");
        when(candidate.getMock()).thenReturn("mock");
        when(new ArgumentsComparator().argumentsMatch(invocation, candidate)).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertTrue(invocationMatcher.hasSimilarMethod(candidate));

        verify(invocation, times(1)).getMethod();
        verify(candidate, times(1)).getMethod();
        verify(candidate, times(1)).isVerified();
        verify(invocation, times(2)).getMock();
        verify(candidate, times(2)).getMock();
        verify(new ArgumentsComparator(), times(1)).argumentsMatch(invocation, candidate);
    }

    @Test
    public void testHasSimilarMethod_methodNameNotEqual() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method("testMethod1", void.class));
        when(candidate.getMethod()).thenReturn(method("testMethod2", void.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));

        verify(invocation, times(1)).getMethod();
        verify(candidate, times(1)).getMethod();
    }

    @Test
    public void testHasSimilarMethod_isVerifiedTrue() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.isVerified()).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));

        verify(candidate, times(1)).isVerified();
    }

    @Test
    public void testHasSimilarMethod_mockNotEqual() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMock()).thenReturn("mock1");
        when(candidate.getMock()).thenReturn("mock2");

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));

        verify(invocation, times(1)).getMock();
        verify(candidate, times(1)).getMock();
    }

    @Test
    public void testHasSimilarMethod_argumentsMatch() {
        Invocation candidate = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method("testMethod", void.class));
        when(candidate.getMethod()).thenReturn(method("testMethod", void.class));
        when(invocation.getMock()).thenReturn("mock");
        when(candidate.getMock()).thenReturn("mock");
        when(new ArgumentsComparator().argumentsMatch(invocation, candidate)).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));

        verify(invocation, times(1)).getMethod();
        verify(candidate, times(1)).getMethod();
        verify(invocation, times(2)).getMock();
        verify(candidate, times(2)).getMock();
        verify(new ArgumentsComparator(), times(1)).argumentsMatch(invocation, candidate);
    }

    @Test
    public void testHasSameMethod_allConditionsTrue() {
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(method.getName()).thenReturn("testMethod");

        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(method);
        when(method.getName()).thenReturn("testMethod");

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertTrue(invocationMatcher.hasSameMethod(candidate));

        verify(invocation, times(1)).getMethod();
        verify(candidate, times(1)).getMethod();
        verify(method, times(2)).getName();
    }

    @Test
    public void testHasSameMethod_methodNameNotEqual() {
        Method method1 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(method1.getName()).thenReturn("testMethod1");

        Method method2 = mock(Method.class);
        when(candidate.getMethod()).thenReturn(method2);
        when(method2.getName()).thenReturn("testMethod2");

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertFalse(invocationMatcher.hasSameMethod(candidate));

        verify(invocation, times(1)).getMethod();
        verify(candidate, times(1)).getMethod();
        verify(method1, times(2)).getName();
        verify(method2, times(2)).getName();
    }

    @Test
    public void testHasSameMethod_parametersNotEqual() {
        Method method1 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(method1.getName()).thenReturn("testMethod");
        when(method1.getParameterTypes()).thenReturn(new Class[]{int.class});

        Method method2 = mock(Method.class);
        when(candidate.getMethod()).thenReturn(method2);
        when(method2.getName()).thenReturn("testMethod");
        when(method2.getParameterTypes()).thenReturn(new Class[]{String.class});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertFalse(invocationMatcher.hasSameMethod(candidate));

        verify(invocation, times(1)).getMethod();
        verify(candidate, times(1)).getMethod();
        verify(method1, times(2)).getName();
        verify(method1, times(1)).getParameterTypes();
        verify(method2, times(2)).getName();
        verify(method2, times(1)).getParameterTypes();
    }

    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(location, invocationMatcher.getLocation());

        verify(invocation, times(1)).getLocation();
    }

    @Test
    public void testCaptureArgumentsFrom_varargs() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(method("testMethod", Object[].class));
        when(invocation.getRawArguments()).thenReturn(new Object[]{1, 2, 3});

        Matcher matcher = mock(Matcher.class);
        matchers.add(matcher);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        try {
            invocationMatcher.captureArgumentsFrom(invocation);
            fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            // exception caught, test passed
        }

        verify(invocation, times(1)).getMethod();
    }

    @Test
    public void testCaptureArgumentsFrom_noMatchers() {
        Object argument = new Object();
        Object[] arguments = new Object[]{argument};

        when(invocation.getArguments()).thenReturn(arguments);
        when(invocation.getArgumentAt(0, Object.class)).thenReturn(argument);

        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        matchers.add(capturesArguments);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        invocationMatcher.captureArgumentsFrom(invocation);

        verify(invocation, times(1)).getArguments();
        verify(invocation, times(1)).getArgumentAt(0, Object.class);
        verify(capturesArguments, times(1)).captureFrom(argument);
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(2, invocationMatchers.size());
        assertEquals(invocation1, invocationMatchers.get(0).getInvocation());
        assertEquals(invocation2, invocationMatchers.get(1).getInvocation());
    }

    private Method method(String name, Class<?> returnType) {
        try {
            return getClass().getDeclaredMethod(name);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

}