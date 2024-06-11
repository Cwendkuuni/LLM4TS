import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    private Invocation invocation;
    private List<Matcher> matchers;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setUp() {
        invocation = mock(Invocation.class);
        matchers = Collections.singletonList(Matchers.any());
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testGetMethod() {
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);

        assertSame(method, invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        assertSame(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        assertSame(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        String expected = "toString";
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getArguments()).thenReturn(new Object[0]);
        when(invocationMatcher.getMatchers()).thenReturn(Collections.singletonList(Matchers.any()));
        when(invocation.getMethod().getName()).thenReturn(expected);

        assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        Invocation actual = mock(Invocation.class);
        Object[] arguments = new Object[0];
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getArguments()).thenReturn(arguments);
        when(actual.getMock()).thenReturn(mock(Object.class));
        when(actual.getMethod()).thenReturn(mock(Method.class));
        when(actual.getArguments()).thenReturn(arguments);
        when(invocationMatcher.hasSameMethod(actual)).thenReturn(true);

        assertTrue(invocationMatcher.matches(actual));

        verify(invocation, times(2)).getMock();
        verify(invocation).getMethod();
        verify(invocation).getArguments();
        verify(actual).getMock();
        verify(actual).getMethod();
        verify(actual).getArguments();
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(candidate.getMethod()).thenReturn(method);
        when(method.getName()).thenReturn("methodName");
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));

        assertTrue(invocationMatcher.hasSimilarMethod(candidate));

        verify(invocation, times(2)).getMethod();
        verify(candidate, times(2)).getMethod();
        verify(method).getName();
        verify(candidate).isVerified();
        verify(invocation).getMock();
        verify(candidate).getMock();
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = mock(Invocation.class);
        Method m1 = mock(Method.class);
        Method m2 = mock(Method.class);
        when(invocation.getMethod()).thenReturn(m1);
        when(candidate.getMethod()).thenReturn(m2);
        when(m1.getName()).thenReturn("methodName");
        when(m2.getName()).thenReturn("methodName");
        when(m1.getParameterTypes()).thenReturn(new Class[0]);
        when(m2.getParameterTypes()).thenReturn(new Class[0]);

        assertTrue(invocationMatcher.hasSameMethod(candidate));

        verify(invocation).getMethod();
        verify(candidate).getMethod();
        verify(m1).getName();
        verify(m2).getName();
        verify(m1).getParameterTypes();
        verify(m2).getParameterTypes();
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        Matcher matcher = mock(Matcher.class);
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getRawArguments()).thenReturn(new Object[0]);
        when(matchers.get(0)).thenReturn(matcher);
        when(matcher instanceof CapturesArguments).thenReturn(true);
        when(invocation.getArgumentAt(0, Object.class)).thenReturn(mock(Object.class));
        when(invocation.getRawArguments()).thenReturn(new Object[1]);
        when(resultMatcher instanceof CapturesArguments).thenReturn(true);
        when(invocation.getArgumentAt(0, Object.class)).thenReturn(mock(Object.class));

        invocationMatcher.captureArgumentsFrom(invocation);

        verify(invocation, times(2)).getMethod();
        verify(matchers).get(0);
        verify(matcher).captureFrom(invocation.getArgumentAt(0, Object.class));
        verify(invocation).getRawArguments();
        verify(matcher).captureFrom(invocation.getRawArguments()[0]);
        verify(capturesArguments).captureFrom(invocation.getArgumentAt(0, Object.class));
        verify(capturesArguments).captureFrom(invocation.getRawArguments()[0]);
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);
        InvocationMatcher invocationMatcher1 = mock(InvocationMatcher.class);
        InvocationMatcher invocationMatcher2 = mock(InvocationMatcher.class);
        whenNew(InvocationMatcher.class).withArguments(invocation1).thenReturn(invocationMatcher1);
        whenNew(InvocationMatcher.class).withArguments(invocation2).thenReturn(invocationMatcher2);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(2, invocationMatchers.size());
        assertSame(invocationMatcher1, invocationMatchers.get(0));
        assertSame(invocationMatcher2, invocationMatchers.get(1));
        verifyNew(InvocationMatcher.class).withArguments(invocation1);
        verifyNew(InvocationMatcher.class).withArguments(invocation2);
    }
}