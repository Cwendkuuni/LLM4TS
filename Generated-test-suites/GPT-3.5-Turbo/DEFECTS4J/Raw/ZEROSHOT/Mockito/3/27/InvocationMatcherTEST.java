import org.junit.*;
import static org.junit.Assert.*;
import org.mockito.*;
import org.hamcrest.*;

public class InvocationMatcherTest {

    @Test
    public void testConstructor() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Mockito.mock(List.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertNotNull(invocationMatcher);
        assertSame(invocation, invocationMatcher.getInvocation());
        assertSame(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithEmptyMatchers() {
        Invocation invocation = Mockito.mock(Invocation.class);
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertNotNull(invocationMatcher);
        assertSame(invocation, invocationMatcher.getInvocation());
        assertNotNull(invocationMatcher.getMatchers());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method result = invocationMatcher.getMethod();

        assertNotNull(result);
        assertSame(invocation.getMethod(), result);
    }

    @Test
    public void testMatches() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Invocation actual = Mockito.mock(Invocation.class);
        ArgumentsComparator argumentsComparator = Mockito.mock(ArgumentsComparator.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.setArgumentsComparator(argumentsComparator);

        Mockito.when(invocation.getMock()).thenReturn("mock");
        Mockito.when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(actual.getMock()).thenReturn("mock");
        Mockito.when(actual.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(argumentsComparator.argumentsMatch(invocationMatcher, actual)).thenReturn(true);

        boolean result = invocationMatcher.matches(actual);

        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethodWithMatchingMethod() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Invocation candidate = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Mockito.when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(invocation.isVerified()).thenReturn(false);
        Mockito.when(invocation.getMock()).thenReturn("mock");
        Mockito.when(candidate.getMethod()).thenReturn(invocation.getMethod());
        Mockito.when(candidate.isVerified()).thenReturn(false);
        Mockito.when(candidate.getMock()).thenReturn("mock");
        Mockito.when(invocationMatcher.hasSameMethod(candidate)).thenReturn(true);
        Mockito.when(invocationMatcher.safelyArgumentsMatch(candidate.getArguments())).thenReturn(false);

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethodWithMethodNameNotEquals() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Invocation candidate = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Mockito.when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(invocation.isVerified()).thenReturn(false);
        Mockito.when(invocation.getMock()).thenReturn("mock");
        Mockito.when(candidate.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(invocationMatcher.hasSameMethod(candidate)).thenReturn(true);
        Mockito.when(invocationMatcher.safelyArgumentsMatch(candidate.getArguments())).thenReturn(false);

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethodWithIsVerified() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Invocation candidate = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Mockito.when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(invocation.isVerified()).thenReturn(true);
        Mockito.when(invocation.getMock()).thenReturn("mock");
        Mockito.when(candidate.getMethod()).thenReturn(invocation.getMethod());
        Mockito.when(candidate.isVerified()).thenReturn(false);
        Mockito.when(candidate.getMock()).thenReturn("mock");
        Mockito.when(invocationMatcher.hasSameMethod(candidate)).thenReturn(true);
        Mockito.when(invocationMatcher.safelyArgumentsMatch(candidate.getArguments())).thenReturn(false);

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethodWithMockIsNotSame() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Invocation candidate = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Mockito.when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(invocation.isVerified()).thenReturn(false);
        Mockito.when(invocation.getMock()).thenReturn("mock");
        Mockito.when(candidate.getMethod()).thenReturn(invocation.getMethod());
        Mockito.when(candidate.isVerified()).thenReturn(false);
        Mockito.when(candidate.getMock()).thenReturn("otherMock");
        Mockito.when(invocationMatcher.hasSameMethod(candidate)).thenReturn(true);
        Mockito.when(invocationMatcher.safelyArgumentsMatch(candidate.getArguments())).thenReturn(false);

        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        assertFalse(result);
    }

    @Test
    public void testHasSameMethodWithEqualMethodName() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Invocation candidate = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Mockito.when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(candidate.getMethod()).thenReturn(invocation.getMethod());
        Mockito.when(invocationMatcher.safelyArgumentsMatch(candidate.getArguments())).thenReturn(false);

        boolean result = invocationMatcher.hasSameMethod(candidate);

        assertTrue(result);
    }

    @Test
    public void testHasSameMethodWithMethodNameNotEquals() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Invocation candidate = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Mockito.when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(candidate.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(invocationMatcher.safelyArgumentsMatch(candidate.getArguments())).thenReturn(false);

        boolean result = invocationMatcher.hasSameMethod(candidate);

        assertFalse(result);
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Location location = Mockito.mock(Location.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Mockito.when(invocation.getLocation()).thenReturn(location);

        Location result = invocationMatcher.getLocation();

        assertNotNull(result);
        assertSame(location, result);
    }

    @Test
    public void testCaptureArgumentsFromWithVarArgs() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Matcher matcher = Mockito.mock(Matcher.class);
        CapturesArguments capturesArguments = Mockito.mock(CapturesArguments.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.setMatchers(Arrays.asList(matcher, matcher, capturesArguments));

        Mockito.when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(invocation.getRawArguments()).thenReturn(new Object[] {1, 2, 3});
        Mockito.when(capturesArguments.captureFrom(1)).thenReturn(true);
        Mockito.when(capturesArguments.captureFrom(2)).thenReturn(true);
        Mockito.when(capturesArguments.captureFrom(3)).thenReturn(false);

        invocationMatcher.captureArgumentsFrom(invocation);

        Mockito.verify(capturesArguments).captureFrom(1);
        Mockito.verify(capturesArguments).captureFrom(2);
        Mockito.verify(capturesArguments, Mockito.never()).captureFrom(3);
    }

    @Test
    public void testCaptureArgumentsFromWithoutVarArgs() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Matcher matcher = Mockito.mock(Matcher.class);
        CapturesArguments capturesArguments = Mockito.mock(CapturesArguments.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.setMatchers(Arrays.asList(matcher, matcher, capturesArguments));

        Mockito.when(invocation.getMethod()).thenReturn(Mockito.mock(Method.class));
        Mockito.when(invocation.getArgumentAt(0, Object.class)).thenReturn(1);
        Mockito.when(invocation.getArgumentAt(1, Object.class)).thenReturn(2);
        Mockito.when(invocation.getArgumentAt(2, Object.class)).thenReturn(3);
        Mockito.when(capturesArguments.captureFrom(1)).thenReturn(true);
        Mockito.when(capturesArguments.captureFrom(2)).thenReturn(true);
        Mockito.when(capturesArguments.captureFrom(3)).thenReturn(false);

        invocationMatcher.captureArgumentsFrom(invocation);

        Mockito.verify(capturesArguments).captureFrom(1);
        Mockito.verify(capturesArguments).captureFrom(2);
        Mockito.verify(capturesArguments, Mockito.never()).captureFrom(3);
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);

        List<InvocationMatcher> result = InvocationMatcher.createFrom(invocations);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertSame(invocation1, result.get(0).getInvocation());
        assertSame(invocation2, result.get(1).getInvocation());
    }
}