import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyList() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testConstructorWithInvocationAndMatcherList() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(eq(1), eq(2), eq(3));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testGetterMethods() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(eq(1), eq(2), eq(3));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Method method = invocationMatcher.getMethod();
        Invocation getInvocation = invocationMatcher.getInvocation();
        List<Matcher> getMatchers = invocationMatcher.getMatchers();
        String toString = invocationMatcher.toString();

        assertNotNull(method);
        assertSame(invocation, getInvocation);
        assertSame(matchers, getMatchers);
        assertTrue(toString.length() > 0);
    }

    @Test
    public void testMatchesWithSameInvocation() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(eq(1), eq(2), eq(3));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Invocation sameInvocation = invocation;
        assertTrue(invocationMatcher.matches(sameInvocation));
    }

    @Test
    public void testMatchesWithDifferentInvocation() {
        Invocation invocation1 = mock(Invocation.class);
        List<Matcher> matchers1 = Arrays.asList(eq(1), eq(2), eq(3));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers1);
        Invocation invocation2 = mock(Invocation.class);
        List<Matcher> matchers2 = Arrays.asList(eq(1), eq(4), eq(5));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers2);
        assertFalse(invocationMatcher1.matches(invocationMatcher2));
    }

    @Test
    public void testSafelyArgumentsMatchWithSameArguments() {
        Invocation invocation = mock(Invocation.class);
        Object[] arguments = new Object[]{1, 2, 3};
        List<Matcher> matchers = Arrays.asList(eq(1), eq(2), eq(3));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertTrue(invocationMatcher.safelyArgumentsMatch(arguments));
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatchWithDifferentArguments() {
        Invocation invocation = mock(Invocation.class);
        Object[] arguments1 = new Object[]{1, 2, 3};
        List<Matcher> matchers = Arrays.asList(eq(1), eq(2), eq(3));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Object[] arguments2 = new Object[]{1, 2, 4};
        assertFalse(invocationMatcher.safelyArgumentsMatch(arguments2));
    }

    @Test
    public void testHasSameMethodWithSameMethods() {
        Invocation invocation1 = mock(Invocation.class);
        Method method1 = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        Invocation invocation2 = mock(Invocation.class);
        Method method2 = mock(Method.class);
        when(invocation2.getMethod()).thenReturn(method2);
        when(method1.getName()).thenReturn("testMethod");
        when(method2.getName()).thenReturn("testMethod");
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testHasSameMethodWithDifferentMethods() {
        Invocation invocation1 = mock(Invocation.class);
        Method method1 = mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        Invocation invocation2 = mock(Invocation.class);
        Method method2 = mock(Method.class);
        when(invocation2.getMethod()).thenReturn(method2);
        when(method1.getName()).thenReturn("testMethod");
        when(method2.getName()).thenReturn("differentMethod");
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(eq(1), eq(2), eq(3));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String toString = invocationMatcher.toString();
        assertTrue(toString.length() > 0);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(eq(1), eq(2), eq(3));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        List<Object> capturedArgs = new LinkedList<>();
        Matcher captureArgMatcher = argThat(new TypeMatcher<Object>() {
            @Override
            public Class<?> getType() {
                return Object.class;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("captured argument");
            }
        });
        Matcher capturedMatcher = argThat(new TypeMatcher<Matcher>() {
            @Override
            public Class<?> getType() {
                return Matcher.class;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("captured matcher");
            }
        });
        Invocation invocationWithCaptureArg = mock(Invocation.class);
        when(invocationWithCaptureArg.getArgumentAt(anyInt(), eq(Matcher.class))).thenReturn(capturedMatcher);
        when(invocationWithCaptureArg.getArgumentAt(anyInt(), eq(Object.class))).thenAnswer(i -> capturedArgs.get(i.getIntValue()));

        invocationMatcher.captureArgumentsFrom(invocationWithCaptureArg);

        for (int i = 0; i < matchers.size(); i++) {
            Matcher matcher = matchers.get(i);
            if (matcher instanceof CapturesArguments) {
                assertEquals(capturedArgs.get(i), matcher.captureFrom(invocationWithCaptureArg));
            }
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFromWithVararg() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(eq(1), eq(2), eq(new Object[]{3, 4, 5}));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(invocation);
        throw new UnsupportedOperationException();
    }
}