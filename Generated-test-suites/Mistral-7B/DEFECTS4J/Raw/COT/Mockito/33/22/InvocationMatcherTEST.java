import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationMatcherImpl;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstructionWithArguments() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(mock(Matcher.class));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        assertMatchers(matcher, matchers);
        assertMethod(matcher, invocation.getMethod());
    }

    @Test
    public void testInvocationMatcherConstructionWithoutArguments() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        assertMatchers(matcher, Collections.emptyList());
        assertMethod(matcher, invocation.getMethod());
    }

    @Test
    public void testMatchesWithSameInvocation() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(mock(Matcher.class));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Invocation sameInvocation = mock(Invocation.class);
        setupSameInvocation(sameInvocation, invocation);

        assertTrue(matcher.matches(sameInvocation));
    }

    @Test
    public void testMatchesWithDifferentInvocationSameMethod() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(mock(Matcher.class));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Invocation differentInvocation = mock(Invocation.class);
        setupDifferentInvocation(differentInvocation, invocation);

        assertFalse(matcher.matches(differentInvocation));
    }

    @Test
    public void testMatchesWithDifferentInvocationSameMethodDifferentArguments() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(mock(Matcher.class));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Invocation differentInvocation = mock(Invocation.class);
        setupDifferentInvocation(differentInvocation, invocation);
        Object[] differentArguments = new Object[]{1L, "test"};
        setupArguments(differentInvocation, differentArguments);

        assertFalse(matcher.matches(differentInvocation));
    }

    @Test(expected = AssertionError.class)
    public void testMatchesWithDifferentInvocationOverloadedMethod() {
        Invocation invocation1 = mock(Invocation.class);
        List<Matcher> matchers1 = Collections.singletonList(mock(Matcher.class));
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1, matchers1);

        Invocation invocation2 = mock(Invocation.class);
        List<Matcher> matchers2 = Collections.singletonList(mock(Matcher.class));
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2, matchers2);

        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        setupMethodsWithSameNameButDifferentParameters(method1, method2);
        setupInvocationsWithMethods(invocation1, method1);
        setupInvocationsWithMethods(invocation2, method2);

        assertTrue(matcher1.hasSimilarMethod(matcher2));
        assertTrue(matcher1.matches(matcher2));
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(mock(Matcher.class));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        String expected = "InvocationMatcher{invocation=Invocation{creationDetails=CreationDetails{method=public java.lang.String java.lang.String.valueOf(java.lang.Object)java.lang.String valueOf(java.lang.Object)}, arguments=[], target=MockForMockito{target=TestClass$MockTest$_MockForMockito_0}}";
        assertEquals(expected, matcher.toString());
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        Object[] arguments = new Object[]{1, "test"};
        matcher.captureArgumentsFrom(mock(Invocation.class).withArguments(arguments));

        Matcher capturedMatcher = matcher.getMatchers().get(0);
        assertTrue(capturedMatcher instanceof CapturesArguments);
        assertEquals(arguments[0], ((CapturesArguments) capturedMatcher).getValue());
        assertEquals(arguments[1], ((CapturesArguments) capturedMatcher).getParameters()[0].getValue());
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(Collections.singletonList(invocation1));

        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);
        matchers.add(matcher2);

        assertTrue(matchers.contains(matcher1));
        assertTrue(matchers.contains(matcher2));
    }

    private void assertMatchers(InvocationMatcher matcher, List<Matcher> matchers) {
        assertEquals(matchers, matcher.getMatchers());
    }

    private void assertMethod(InvocationMatcher matcher, Method method) {
        assertEquals(method, matcher.getMethod());
    }

    private void setupInvocationsWithMethods(Invocation invocation, Method method) {
        setupInvocation(invocation, method, null);
    }

    private void setupInvocation(Invocation invocation, Method method, Object[] arguments) {
        when(invocation.getMethod()).thenReturn(method);
        if (arguments != null) {
            when(invocation.getArguments()).thenReturn(arguments);
        }
    }

    private void setupSameInvocation(Invocation invocation1, Invocation invocation2) {
        setupInvocationsWithMethods(invocation1, invocation1.getMethod());
        setupInvocationsWithMethods(invocation2, invocation1.getMethod());
        when(invocation1.getMock()).thenReturn(invocation2.getMock());
    }

    private void setupDifferentInvocation(Invocation invocation1, Invocation invocation2) {
        setupInvocationsWithMethods(invocation1, invocation1.getMethod());
        setupInvocationsWithMethods(invocation2, invocation2.getMethod());
        when(invocation1.getMock()).thenReturn(invocation2.getMock());
        doAnswer(i -> true).when(invocation2).isVerified();
    }

    private void setupMethodsWithSameNameButDifferentParameters(Method method1, Method method2) {
        setupMethod(method1, "methodName", new Class[0]);
        setupMethod(method2, "methodName", new Class[]{Long.TYPE, String.class});
    }

    private void setupMethod(Method method, String name, Class[] parameterTypes) {
        when(method.getName()).thenReturn(name);
        when(method.getParameterTypes()).thenReturn(parameterTypes);
    }

    private void setupArguments(Invocation invocation, Object[] arguments) {
        when(invocation.getArguments()).thenReturn(arguments);
    }
}