import org.junit.Test;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.invocation.Invocation;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherWithInvocationAndMatchers() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(new ArgumentsMatcher("expected"), new CapturesArguments());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Test method calls
        verify(invocationMatcher).getMethod();
        verify(invocationMatcher).getInvocation();
        verify(invocationMatcher).getMatchers();

        // Test toString method
        String invocationMatcherString = invocationMatcher.toString();
        String expectedToString = "InvocationMatcher:invocation=[Invocation@" + invocation.hashCode() + "], matchers=[ArgumentsMatcher{arg1='expected'}, CapturesArguments]";
        assertEquals(expectedToString, invocationMatcherString);

        // Test matches method
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getMock()).thenReturn(invocation.getMock());
        when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());
        when(actualInvocation.getArguments()).thenReturn(invocation.getArguments());
        assertTrue(invocationMatcher.matches(actualInvocation));

        // Test hasSimilarMethod method
        Invocation candidateInvocation = mock(Invocation.class);
        when(candidateInvocation.getMock()).thenReturn(invocation.getMock());
        when(candidateInvocation.getMethod()).thenReturn(new Method("anotherMethod", new Class[0]));
        assertFalse(invocationMatcher.hasSimilarMethod(candidateInvocation));

        // Test hasSameMethod method
        Invocation sameInvocation = mock(Invocation.class);
        when(sameInvocation.getMock()).thenReturn(invocation.getMock());
        when(sameInvocation.getMethod()).thenReturn(invocation.getMethod());
        assertTrue(invocationMatcher.hasSameMethod(sameInvocation));
    }
}