import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.invocation.Invocation;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    @Test
    public void testConstructorOneParam() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertNotNull(invocationMatcher);
        assertSame(invocation, invocationMatcher.getInvocation());
        assertNotNull(invocationMatcher.getMatchers());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testConstructorTwoParams() {
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = Collections.singletonList(mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertNotNull(invocationMatcher);
        assertSame(invocation, invocationMatcher.getInvocation());
        assertSame(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        assertSame(method, invocationMatcher.getMethod());
    }

    @Test
    public void testToString() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        PrintSettings printSettings = mock(PrintSettings.class);
        List<Matcher> matchers = Collections.singletonList(mock(Matcher.class));
        when(invocation.toString()).thenReturn("Invocation");
        when(printSettings.print(matchers, invocation)).thenReturn("Printed Invocation");
        invocationMatcher.toString();
        verify(printSettings).print(matchers, invocation);
    }
    
    @Test
    public void testMatches() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        when(invocation1.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getArguments()).thenReturn(new Object[]{});
        when(invocation2.getRawArguments()).thenReturn(new Object[]{});
        when(invocation2.isVerified()).thenReturn(false);

        assertFalse(invocationMatcher.matches(invocation2));


        when(invocation2.getMock()).thenReturn(invocation1.getMock());
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getArguments()).thenReturn(new Object[]{});
        when(invocation2.getRawArguments()).thenReturn(new Object[]{});
        when(invocation2.isVerified()).thenReturn(false);

        assertTrue(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        when(invocation1.getMethod().getName()).thenReturn("method1");
        when(invocation2.getMethod().getName()).thenReturn("method2");
        when(invocation2.isVerified()).thenReturn(false);
        when(invocation1.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getMock()).thenReturn(invocation1.getMock());
        when(invocation2.getArguments()).thenReturn(new Object[]{});
        when(invocation2.getRawArguments()).thenReturn(new Object[]{});

        assertFalse(invocationMatcher.hasSimilarMethod(invocation2));


        when(invocation2.getMethod().getName()).thenReturn("method1");

        assertTrue(invocationMatcher.hasSimilarMethod(invocation2));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        Method method = mock(Method.class);

        when(invocation1.getMethod()).thenReturn(method);
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        when(invocation1.getMethod().getName()).thenReturn("method1");
        when(invocation2.getMethod().getName()).thenReturn("method2");

        assertFalse(invocationMatcher.hasSameMethod(invocation2));

        when(invocation1.getMethod().getName()).thenReturn("method1");
        when(method.getParameterTypes()).thenReturn(new Class<?>[]{});
        assertFalse(invocationMatcher.hasSameMethod(invocation2));

        when(method.getParameterTypes()).thenReturn(new Class<?>[]{int.class});
        assertFalse(invocationMatcher.hasSameMethod(invocation2));

        when(invocation2.getMethod().getName()).thenReturn("method1");
        assertFalse(invocationMatcher.hasSameMethod(invocation2));

        when(invocation2.getMethod().getName()).thenReturn("method1");
        when(invocation2.getMethod().getParameterTypes()).thenReturn(new Class<?>[]{});
        assertTrue(invocationMatcher.hasSameMethod(invocation2));

        when(method.getParameterTypes()).thenReturn(new Class<?>[]{int.class});
        assertFalse(invocationMatcher.hasSameMethod(invocation2));
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);
        assertSame(location, invocationMatcher.getLocation());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFromVarArgs() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        when(invocation.getMethod().isVarArgs()).thenReturn(true);
        invocationMatcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Matcher matcher = mock(Matcher.class);
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        when(invocationMatcher.getMatchers()).thenReturn(Collections.singletonList(matcher));
        when(invocationMatcher.getInvocation()).thenReturn(invocation);
        when(invocation.getRawArguments()).thenReturn(new Object[]{});
        when(matcher instanceof CapturesArguments).thenReturn(true);
        when(matcher).thenReturn(capturesArguments);
        when(invocation.getArgumentAt(0, Object.class)).thenReturn(mock(Object.class));
        invocationMatcher.captureArgumentsFrom(invocation);
        verify(capturesArguments).captureFrom(any());
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = Collections.singletonList(invocation1);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        LinkedList<InvocationMatcher> expected = new LinkedList<>();
        expected.add(invocationMatcher1);

        assertEquals(expected, InvocationMatcher.createFrom(invocations));
    }
}