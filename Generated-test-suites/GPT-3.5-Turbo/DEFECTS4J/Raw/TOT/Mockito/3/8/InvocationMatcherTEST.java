import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private Invocation invocation;
    private List<Matcher> matchers;

    @Before
    public void setUp() {
        invocation = mock(Invocation.class);
        matchers = new ArrayList<>();
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testConstructor_withInvocationsAndMatchers() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructor_withInvocationsAndEmptyMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testGetMethod() {
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        PrintSettings printSettings = mock(PrintSettings.class);
        when(printSettings.print(matchers, invocation)).thenReturn("InvocationMatcher");

        assertEquals("InvocationMatcher", invocationMatcher.toString());
    }

    @Test
    public void testMatches_sameMocksAndMethods() {
        Invocation actualInvocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actualInvocation.getMock()).thenReturn(mock(Object.class));
        when(actualInvocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(new ArgumentsComparator().argumentsMatch(invocationMatcher, actualInvocation)).thenReturn(true);

        assertTrue(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testMatches_differentMocks() {
        Invocation actualInvocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actualInvocation.getMock()).thenReturn(null);

        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testMatches_sameMocksDifferentMethods() {
        Invocation actualInvocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actualInvocation.getMock()).thenReturn(mock(Object.class));
        when(actualInvocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(hasSameMethod(actualInvocation)).thenReturn(false);
        when(safelyArgumentsMatch(actualInvocation.getArguments())).thenReturn(true);

        assertFalse(invocationMatcher.matches(actualInvocation));
    }

    @Test
    public void testHasSimilarMethod_sameMethodName() throws NoSuchMethodException {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(this.getClass().getDeclaredMethod("testHasSimilarMethod_sameMethodName"));

        when(invocation.getMethod()).thenReturn(this.getClass().getDeclaredMethod("testHasSimilarMethod_sameMethodName"));
        when(candidate.isVerified()).thenReturn(false);
        when(getInvocation().getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));
        when(hasSameMethod(candidate)).thenReturn(true);
        when(safelyArgumentsMatch(candidate.getArguments())).thenReturn(false);

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_differentMethodName() throws NoSuchMethodException {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(this.getClass().getDeclaredMethod("testHasSimilarMethod_differentMethodName"));

        when(invocation.getMethod()).thenReturn(this.getClass().getDeclaredMethod("testHasSimilarMethod_sameMethodName"));
        when(candidate.isVerified()).thenReturn(false);
        when(getInvocation().getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_differentMock() throws NoSuchMethodException {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(this.getClass().getDeclaredMethod("testHasSimilarMethod_differentMock"));

        when(invocation.getMethod()).thenReturn(this.getClass().getDeclaredMethod("testHasSimilarMethod_differentMock"));
        when(candidate.isVerified()).thenReturn(false);
        when(getInvocation().getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(null);

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod_sameMethodNameAndParameters() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(method2.getName()).thenReturn("testHasSameMethod_sameMethodNameAndParameters");
        when(method1.getName()).thenReturn("testHasSameMethod_sameMethodNameAndParameters");
        when(method1.getParameterTypes()).thenReturn(new Class[]{Integer.class});
        when(method2.getParameterTypes()).thenReturn(new Class[]{Integer.class});

        assertTrue(invocationMatcher.hasSameMethod(invocation));
    }

    @Test
    public void testHasSameMethod_differentMethodName() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(method2.getName()).thenReturn("testHasSameMethod_differentMethodName");
        when(method1.getName()).thenReturn("testHasSameMethod_sameMethodNameAndParameters");

        assertFalse(invocationMatcher.hasSameMethod(invocation));
    }

    @Test
    public void testHasSameMethod_differentNumberOfParameters() {
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method1);
        when(method2.getName()).thenReturn("testHasSameMethod_differentNumberOfParameters");
        when(method1.getName()).thenReturn("testHasSameMethod_differentNumberOfParameters");
        when(method1.getParameterTypes()).thenReturn(new Class[]{Integer.class});
        when(method2.getParameterTypes()).thenReturn(new Class[]{Integer.class, String.class});

        assertFalse(invocationMatcher.hasSameMethod(invocation));
    }

    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);

        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test
    public void testCaptureArgumentsFrom_varArgs() throws NoSuchMethodException {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(this.getClass().getDeclaredMethod("testCaptureArgumentsFrom_varArgs"));
        Object[] arguments = {1, 2, 3};
        when(invocation.getRawArguments()).thenReturn(arguments);

        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        matchers.add(mock(Matcher.class));
        matchers.add(capturesArguments);
        matchers.add(mock(Matcher.class));

        invocationMatcher.captureArgumentsFrom(invocation);

        verify(capturesArguments).captureFrom(2);
        verify(capturesArguments).captureFrom(3);
    }

    @Test
    public void testCaptureArgumentsFrom_nonVarArgs() throws NoSuchMethodException {
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(this.getClass().getDeclaredMethod("testCaptureArgumentsFrom_nonVarArgs"));
        Object[] arguments = {1, 2, 3};
        when(invocation.getRawArguments()).thenReturn(arguments);

        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        matchers.add(mock(Matcher.class));
        matchers.add(capturesArguments);

        invocationMatcher.captureArgumentsFrom(invocation);

        verify(capturesArguments).captureFrom(2);
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(invocation);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(1, invocationMatchers.size());
        assertEquals(invocation, invocationMatchers.get(0).getInvocation());
    }

}