import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InvocationMatcherTest {

    private Invocation invocation;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setUp() {
        invocation = mock(Invocation.class);
        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testGetMethod() {
        assertSame(invocation.getMethod(), invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        assertSame(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        assertNotNull(invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        assertNotNull(invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation actual = mock(Invocation.class);
        when(actual.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actual.getMethod()).thenReturn(invocation.getMethod());
        when(actual.getArguments()).thenReturn(new Object[invocation.getArguments().length]);
        assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testMatches_WithDifferentMock() {
        Invocation actual = mock(Invocation.class);
        when(actual.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actual.getMethod()).thenReturn(invocation.getMethod());
        when(actual.getArguments()).thenReturn(invocation.getArguments());
        assertFalse(invocationMatcher.matches(actual));
    }

    @Test
    public void testMatches_WithDifferentMethod() {
        Invocation actual = mock(Invocation.class);
        when(actual.getMock()).thenReturn(invocation.getMock());
        when(actual.getMethod()).thenReturn(mock(Method.class));
        assertFalse(invocationMatcher.matches(actual));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(invocation.getMethod());
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(invocation.getMock());
        when(candidate.getArguments()).thenReturn(new Object[invocation.getArguments().length]);
        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_WithDifferentMethodName() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(candidate.getMethod()).thenReturn(method);
        when(method.getName()).thenReturn("differentMethod");
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_WithVerifiedCandidate() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(invocation.getMethod());
        when(candidate.isVerified()).thenReturn(true);
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_WithDifferentMock() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(invocation.getMethod());
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(mock(Object.class));
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethod_WithOverloadedButSameArgs() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(invocation.getMethod());
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(invocation.getMock());
        when(candidate.getArguments()).thenReturn(invocation.getArguments());
        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(invocation.getMethod());
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testHasSameMethod_WhenMethodEquals() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(candidate.getMethod()).thenReturn(method);
        when(invocation.getMethod()).thenReturn(method);
        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testHasSameMethod_WhenMethodNotEquals() {
        Invocation candidate = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        when(candidate.getMethod()).thenReturn(method1);
        when(invocation.getMethod()).thenReturn(method2);
        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testGetLocation() {
        assertNotNull(invocationMatcher.getLocation());
    }

    @Test
    public void testToString_WithPrintSettings() {
        assertNotNull(invocationMatcher.toString(new PrintSettings()));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation i = mock(Invocation.class);
        Object[] arguments = new Object[invocationMatcher.getMatchers().size()];
        when(i.getArguments()).thenReturn(arguments);

        for (int k = 0; k < invocationMatcher.getMatchers().size(); k++) {
            Matcher matcher = mock(Matcher.class);
            if (matcher instanceof CapturesArguments) {
                when(i.getArguments()[k]).thenReturn(mock(Object.class));
                invocationMatcher.captureArgumentsFrom(i);
            }
        }
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