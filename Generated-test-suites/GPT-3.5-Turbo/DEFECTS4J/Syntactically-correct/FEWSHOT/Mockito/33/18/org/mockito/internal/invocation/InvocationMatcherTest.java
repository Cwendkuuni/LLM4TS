import org.hamcrest.Matcher;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {
    @Test
    public void testConstructorWithMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = mock(List.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithoutMatchers() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
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
        List<Matcher> matchers = mock(List.class);
        PrintSettings printSettings = mock(PrintSettings.class);

        when(invocation.toString(matchers, printSettings)).thenReturn("InvocationToString");

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals("InvocationToString", invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));

        List<Matcher> matchers1 = mock(List.class);
        when(invocation1.argumentsToMatchers()).thenReturn(matchers1);
        List<Matcher> matchers2 = mock(List.class);
        when(invocation2.argumentsToMatchers()).thenReturn(matchers2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, matchers1);

        assertTrue(invocationMatcher.matches(invocation2));
        verify(invocationMatcher).getMethod();
    }

    @Test
    public void testMatchesWithDifferentMethodNames() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        when(invocation1.getMock()).thenReturn(new Object());
        when(invocation2.getMock()).thenReturn(new Object());

        List<Matcher> matchers = mock(List.class);
        when(invocation1.argumentsToMatchers()).thenReturn(matchers);
        when(invocation2.argumentsToMatchers()).thenReturn(matchers);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, matchers);

        assertFalse(invocationMatcher.matches(invocation2));
        verify(invocationMatcher).getMethod();
        verify(invocationMatcher, times(2)).getMock();
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        Mockito.when(invocation1.isVerified()).thenReturn(false);
        Mockito.when(invocation2.isVerified()).thenReturn(false);
        Mockito.when(invocation1.getMock()).thenReturn(new Object());
        Mockito.when(invocation2.getMock()).thenReturn(new Object());

        Mockito.when(invocation2.getMethod().getName()).thenReturn("methodName");

        ArgumentCaptor<Object[]> argumentCaptor = ArgumentCaptor.forClass(Object[].class);
        Mockito.when(invocationMatcher.safelyArgumentsMatch(argumentCaptor.capture()))
                .thenAnswer(
                        new Answer<Boolean>() {
                            @Override
                            public Boolean answer(InvocationOnMock invocationOnMock) throws Throwable {
                                return invocationOnMock.getArgument(0) != null;
                            }
                        }
                );

        assertTrue(invocationMatcher.hasSimilarMethod(invocation2));
        verify(invocationMatcher).getMethod();
        verify(invocationMatcher).getMock();
        assertArrayEquals(new Object[]{null}, argumentCaptor.getValue());
    }

    @Test
    public void testHasSimilarMethodWithOverloadedArguments() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        Mockito.when(invocation1.isVerified()).thenReturn(false);
        Mockito.when(invocation2.isVerified()).thenReturn(false);
        Mockito.when(invocation1.getMock()).thenReturn(new Object());
        Mockito.when(invocation2.getMock()).thenReturn(new Object());

        Mockito.when(invocation2.getMethod().getName()).thenReturn("methodName");

        ArgumentCaptor<Object[]> argumentCaptor = ArgumentCaptor.forClass(Object[].class);
        Mockito.when(invocationMatcher.safelyArgumentsMatch(argumentCaptor.capture()))
                .thenAnswer(
                        new Answer<Boolean>() {
                            @Override
                            public Boolean answer(InvocationOnMock invocationOnMock) throws Throwable {
                                return invocationOnMock.getArgument(0) != null;
                            }
                        }
                );

        assertFalse(invocationMatcher.hasSimilarMethod(invocation2));
        verify(invocationMatcher).getMethod();
        verify(invocationMatcher).getMock();
        assertArrayEquals(new Object[]{null}, argumentCaptor.getValue());
    }

    @Test
    public void testHasSimilarMethodWithOverridesArguments() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        Mockito.when(invocation1.isVerified()).thenReturn(false);
        Mockito.when(invocation2.isVerified()).thenReturn(false);
        Mockito.when(invocation1.getMock()).thenReturn(new Object());
        Mockito.when(invocation2.getMock()).thenReturn(new Object());

        Mockito.when(invocation2.getMethod().getName()).thenReturn("methodName");

        ArgumentCaptor<Object[]> argumentCaptor = ArgumentCaptor.forClass(Object[].class);
        Mockito.when(invocationMatcher.safelyArgumentsMatch(argumentCaptor.capture()))
                .thenReturn(false);

        assertTrue(invocationMatcher.hasSimilarMethod(invocation2));
        verify(invocationMatcher).getMethod();
        verify(invocationMatcher).getMock();
        assertArrayEquals(new Object[]{null}, argumentCaptor.getValue());
    }

    @Test
    public void testHasSameMethod() throws NoSuchMethodException {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(getClass().getDeclaredMethod("testHasSameMethod"));
        when(invocation2.getMethod()).thenReturn(getClass().getDeclaredMethod("testHasSameMethod"));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertTrue(invocationMatcher.hasSameMethod(invocation2));
        verify(invocationMatcher).getMethod();
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
    public void testToStringWithPrintSettings() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = mock(List.class);
        PrintSettings printSettings = mock(PrintSettings.class);

        when(invocation.toString(matchers, printSettings)).thenReturn("InvocationToString");

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals("InvocationToString", invocationMatcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        Object[] arguments = new Object[]{1, 2, 3};

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        List<Matcher> matchers = List.of(matcher1, matcher2);
        when(invocationMatcher.getMatchers()).thenReturn(matchers);

        CapturesArguments capturesArguments1 = mock(CapturesArguments.class);
        CapturesArguments capturesArguments2 = mock(CapturesArguments.class);

        when(matcher1 instanceof CapturesArguments).thenReturn(true);
        when(matcher2 instanceof CapturesArguments).thenReturn(true);

        when(invocation.getArguments()).thenReturn(arguments);

        invocationMatcher.captureArgumentsFrom(invocation);

        verify(capturesArguments1).captureFrom(1);
        verify(capturesArguments2).captureFrom(2);
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        Invocation invocation3 = mock(Invocation.class);
        List<Invocation> invocations = List.of(invocation1, invocation2, invocation3);

        LinkedList<InvocationMatcher> expectedInvocationMatchers = new LinkedList<>();
        expectedInvocationMatchers.add(new InvocationMatcher(invocation1));
        expectedInvocationMatchers.add(new InvocationMatcher(invocation2));
        expectedInvocationMatchers.add(new InvocationMatcher(invocation3));

        LinkedList<InvocationMatcher> invocationMatchers = (LinkedList<InvocationMatcher>) InvocationMatcher.createFrom(invocations);

        for (int i = 0; i < invocations.size(); i++) {
            assertEquals(expectedInvocationMatchers.get(i), invocationMatchers.get(i));
        }
    }
}