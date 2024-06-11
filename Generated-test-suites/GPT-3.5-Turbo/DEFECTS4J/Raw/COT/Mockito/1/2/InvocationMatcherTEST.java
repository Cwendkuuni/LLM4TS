import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.mockito.invocation.DescribedInvocation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.LinkedList;
import org.hamcrest.Matcher;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.internal.invocation.ArgumentsProcessor;
import org.mockito.internal.invocation.ArgumentsComparator;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    private Invocation createInvocationMock() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(methodMock());
        return invocation;
    }

    private Method methodMock() {
        Method method = mock(Method.class);
        when(method.getName()).thenReturn("methodName");
        when(method.getParameterTypes()).thenReturn(new Class[]{});
        return method;
    }

    @Test
    public void testConstructorWithInvocationAndMatchers() {
        Invocation invocation = createInvocationMock();
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithInvocation() {
        Invocation invocation = createInvocationMock();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = createInvocationMock();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        Invocation invocation = createInvocationMock();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        PrintSettings printSettingsMock = mock(PrintSettings.class);
        when(printSettingsMock.print(any(), any())).thenReturn("toStringResult");
        assertEquals("toStringResult", invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation invocation = createInvocationMock();
        Invocation actual = createInvocationMock();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        ArgumentsComparator argumentsComparatorMock = mock(ArgumentsComparator.class);
        when(argumentsComparatorMock.argumentsMatch(any(), any())).thenReturn(true);
        assertTrue(invocationMatcher.matches(actual));
        when(argumentsComparatorMock.argumentsMatch(any(), any())).thenReturn(false);
        assertFalse(invocationMatcher.matches(actual));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = createInvocationMock();
        Invocation candidate = createInvocationMock();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        ArgumentsComparator argumentsComparatorMock = mock(ArgumentsComparator.class);
        when(argumentsComparatorMock.argumentsMatch(any(), any())).thenReturn(true);
        candidate.getMethod().getName();
        candidate.isVerified();
        candidate.getMock();
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
        when(candidate.getMethod().getName()).thenReturn("differentName");
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
        when(candidate.getMethod().getName()).thenReturn("methodName");
        when(candidate.isVerified()).thenReturn(true);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(mock(Object.class));
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
        when(argumentsComparatorMock.argumentsMatch(any(), any())).thenReturn(false);
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation = createInvocationMock();
        Invocation candidate = createInvocationMock();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method m1 = invocation.getMethod();
        Method m2 = candidate.getMethod();
        when(m1.getName()).thenReturn("name");
        when(m2.getName()).thenReturn("name");
        when(m1.getParameterTypes()).thenReturn(new Class[]{});
        when(m2.getParameterTypes()).thenReturn(new Class[]{});
        assertTrue(invocationMatcher.hasSameMethod(candidate));
        when(m2.getName()).thenReturn("differentName");
        assertFalse(invocationMatcher.hasSameMethod(candidate));
        when(m2.getName()).thenReturn("name");
        when(m2.getParameterTypes()).thenReturn(new Class[]{mock(Class.class)});
        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = createInvocationMock();
        Location locationMock = mock(Location.class);
        when(invocation.getLocation()).thenReturn(locationMock);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertEquals(locationMock, invocationMatcher.getLocation());
    }

    @Test
    public void testCaptureArgumentsFrom_withoutVarargs() {
        Invocation invocation = createInvocationMock();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Matcher matcherMock = mock(Matcher.class);
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(matcherMock);
        invocationMatcher.captureArgumentsFrom(invocation);
        verify(matcherMock).captureFrom(any());
    }

    @Test
    public void testCaptureArgumentsFrom_withVarargs_notSupported() {
        Invocation invocation = createInvocationMock();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        when(invocation.getMethod().isVarArgs()).thenReturn(true);
        assertThrows(UnsupportedOperationException.class, () -> invocationMatcher.captureArgumentsFrom(invocation));
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = createInvocationMock();
        Invocation invocation2 = createInvocationMock();
        List<Invocation> invocations = new LinkedList<>();
        invocations.add(invocation1);
        invocations.add(invocation2);
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        assertEquals(2, invocationMatchers.size());
        assertEquals(invocation1, invocationMatchers.get(0).getInvocation());
        assertEquals(invocation2, invocationMatchers.get(1).getInvocation());
    }
}



// Test cases for InvocationMatcher class
public class InvocationMatcherTest {

    // Test constructor with invocation and matchers
    @Test
    public void testConstructorWithInvocationAndMatchers() {
        Invocation invocation = createInvocationMock();
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(mock(Matcher.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    // Test constructor with invocation
    @Test
    public void testConstructorWithInvocation() {
        Invocation invocation = createInvocationMock();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    // Test getMethod() method
    @Test
    public void testGetMethod() {
        Invocation invocation = createInvocationMock();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
    }

    // Test toString() method
    @Test
    public void testToString() {
        Invocation invocation = createInvocationMock();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        PrintSettings printSettingsMock = mock(PrintSettings.class);
        when(printSettingsMock.print(any(), any())).thenReturn("toStringResult");

        assertEquals("toStringResult", invocationMatcher.toString());
    }

    // Test matches() method
    @Test
    public void testMatches() {
        Invocation invocation = createInvocationMock();
        Invocation actual = createInvocationMock();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        ArgumentsComparator argumentsComparatorMock = mock(ArgumentsComparator.class);
        when(argumentsComparatorMock.argumentsMatch(any(), any())).thenReturn(true);
        assertTrue(invocationMatcher.matches(actual));

        when(argumentsComparatorMock.argumentsMatch(any(), any())).thenReturn(false);
        assertFalse(invocationMatcher.matches(actual));
    }

    // Test hasSimilarMethod() method
    @Test
    public void testHasSimilarMethod() {
        Invocation invocation = createInvocationMock();
        Invocation candidate = createInvocationMock();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        ArgumentsComparator argumentsComparatorMock = mock(ArgumentsComparator.class);
        when(argumentsComparatorMock.argumentsMatch(any(), any())).thenReturn(true);

        candidate.getMethod().getName();
        candidate.isVerified();
        candidate.getMock();
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));

        when(candidate.getMethod().getName()).thenReturn("differentName");
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));

        when(candidate.getMethod().getName()).thenReturn("methodName");
        when(candidate.isVerified()).thenReturn(true);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));

        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(mock(Object.class));
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));

        when(argumentsComparatorMock.argumentsMatch()).thenReturn(false);
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));

    }

    // Test hasSameMethod() method
    @Test
    public void testHasSameMethod() {
        Invocation invocation = createInvocationMock();
        Invocation candidate = createInvocationMock();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method m1 = invocation.getMethod();
        Method m2 = candidate.getMethod();

        when(m1.getName()).thenReturn("name");
        when(m2.getName()).thenReturn("name");
        when(m1.getParameterTypes()).thenReturn(new Class[]{});
        when(m2.getParameterTypes()).thenReturn(new Class[]{});

        assertTrue(invocationMatcher.hasSameMethod(candidate));

        when(m2.getName()).thenReturn("differentName");
        assertFalse(invocationMatcher.hasSameMethod(candidate));

        when(m2.getName()).thenReturn("name");
        when(m2.getParameterTypes()).thenReturn(new Class[]{mock(Class.class)});
        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }

    // Test getLocation() method
    @Test
    public void testGetLocation() {
        Invocation invocation = createInvocationMock();
        Location locationMock = mock(Location.class);
        when(invocation.getLocation()).thenReturn(locationMock);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(locationMock, invocationMatcher.getLocation());
    }

    // Test captureArgumentsFrom() method without varargs
    @Test
    public void testCaptureArgumentsFrom_withoutVarargs() {
        Invocation invocation = createInvocationMock();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Matcher matcherMock = mock(Matcher.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(matcherMock);
        invocationMatcher.captureArgumentsFrom(invocation);
        verify(matcherMock).captureFrom(any());
    }

    // Test captureArgumentsFrom() method with varargs (unsupported)
    @Test
    public void testCaptureArgumentsFrom_withVarargs_notSupported() {
        Invocation invocation = createInvocationMock();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        when(invocation.getMethod().isVarArgs()).thenReturn(true);
        assertThrows(UnsupportedOperationException.class, () -> invocationMatcher.captureArgumentsFrom(invocation));
    }

    // Test createFrom() method
    @Test
    public void testCreateFrom() {
        Invocation invocation1 = createInvocationMock();
        Invocation invocation2 = createInvocationMock();
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(invocation1);
        invocations.add(invocation2);
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        assertEquals(2, invocationMatchers.size());
        assertEquals(invocation1, invocationMatchers.get(0).getInvocation());
        assertEquals(invocation2, invocationMatchers.get(1).getInvocation());
    }

    private Invocation createInvocationMock() {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(methodMock());
        return invocation;
    }

    private Method methodMock() {
        Method method = mock(Method.class);
        when(method.getName()).thenReturn("methodName");
        when(method.getParameterTypes()).thenReturn(new Class[]{});
        return method;
    }
}
