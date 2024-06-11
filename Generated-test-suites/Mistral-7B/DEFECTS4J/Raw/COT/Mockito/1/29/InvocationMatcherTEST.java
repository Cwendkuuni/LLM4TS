import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.Runners;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationOnMockImpl;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.matchers.VarargMatcher;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(Runners.JUnit4.class)
public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyMatchers() {
        Invocation mockInvocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);
        Method method = mockInvocation.getMethod();

        assertTrue(invocationMatcher.matches(new InvocationMatcher(mockInvocation)));
        assertTrue(invocationMatcher.hasSameMethod(new InvocationMatcher(mockInvocation)));
        assertEquals(method, invocationMatcher.getMethod());
        assertEquals(method, invocationMatcher.getInvocation().getMethod());
    }

    @Test
    public void testConstructorWithInvocationAndCustomMatchers() {
        Invocation mockInvocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(Mockito.eq("test"), Mockito.anyInt());
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, matchers);

        Method method = mockInvocation.getMethod();
        assertTrue(invocationMatcher.matches(new InvocationMatcher(mockInvocation, matchers)));
        assertTrue(invocationMatcher.hasSameMethod(new InvocationMatcher(mockInvocation, matchers)));
        assertEquals(method, invocationMatcher.getMethod());
        assertEquals(method, invocationMatcher.getInvocation().getMethod());
    }

    @Test
    public void testMatchesWithSameInvocation() {
        Invocation mockInvocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);

        assertTrue(invocationMatcher.matches(new InvocationMatcher(mockInvocation)));
    }

    @Test
    public void testMatchesWithDifferentInvocationsSameMethod() {
        Invocation mockInvocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation mockInvocation2 = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(mockInvocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(mockInvocation2);

        Method method = mockInvocation1.getMethod();

        assertTrue(invocationMatcher1.matches(invocationMatcher2));
        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
        assertEquals(method, invocationMatcher1.getMethod());
        assertEquals(method, invocationMatcher2.getMethod());
    }

    @Test
    public void testMatchesWithDifferentInvocationsDifferentMethod() {
        Invocation mockInvocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation mockInvocation2 = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(mockInvocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(mockInvocation2);

        Method method1 = mockInvocation1.getMethod();
        Method method2 = mockInvocation2.getMethod();

        assertFalse(invocationMatcher1.matches(invocationMatcher2));
        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
        assertFalse(invocationMatcher1.hasSameMethod(invocationMatcher2));
        assertNotSame(method1, method2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFromVarargs() {
        Invocation mockInvocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);
        invocationMatcher.captureArgumentsFrom(mockInvocation);
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(Mockito.mock(InvocationOnMock.class), Mockito.mock(InvocationOnMock.class));
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        InvocationMatcher firstInvocationMatcher = invocationMatchers.get(0);
        InvocationMatcher secondInvocationMatcher = invocationMatchers.get(1);

        assertTrue(firstInvocationMatcher.matches(new InvocationMatcher(invocations.get(0))));
        assertTrue(firstInvocationMatcher.hasSameMethod(new InvocationMatcher(invocations.get(0))));
        assertEquals(firstInvocationMatcher.getMethod(), invocations.get(0).getMethod());

        assertTrue(secondInvocationMatcher.matches(new InvocationMatcher(invocations.get(1))));
        assertTrue(secondInvocationMatcher.hasSameMethod(new InvocationMatcher(invocations.get(1))));
        assertEquals(secondInvocationMatcher.getMethod(), invocations.get(1).getMethod());
    }

    @Test
    public void testCaptureArgumentsFromNonVarargs() {
        Invocation mockInvocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(Mockito.eq("test"), Mockito.anyInt());
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, matchers);

        Object[] invocationArguments = new Object[]{1, "test"};
        Object[] matcherArguments = new Object[]{"test", 1};

        invocationMatcher.captureArgumentsFrom(new InvocationOnMockImpl(mockInvocation.getMock(), mockInvocation.getLocation(), mockInvocation.getMethod(), invocationArguments));

        for (int i = 0; i < matchers.size(); i++) {
            Matcher m = matchers.get(i);
            if (m instanceof CapturesArguments) {
                assertEquals(matcherArguments[i], ((CapturesArguments) m).getValue());
            }
        }
    }

    @Test
    public void testCaptureArgumentsFromVarargs() {
        Invocation mockInvocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(Mockito.argThat(new ArgumentsMatcher("test")), Mockito.anyInt());
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, matchers);

        Object[] invocationArguments = new Object[]{1, new Object[]{1, 2, 3}};

        invocationMatcher.captureArgumentsFrom(new InvocationOnMockImpl(mockInvocation.getMock(), mockInvocation.getLocation(), mockInvocation.getMethod(), invocationArguments));

        for (int i = 0; i < matchers.size(); i++) {
            Matcher m = matchers.get(i);
            if (m instanceof CapturesArguments) {
                if (i == matchers.size() - 1 && m instanceof VarargMatcher) {
                    Object[] varargs = ((VarargMatcher) m).getArguments();
                    for (int j = 0; j < varargs.length; j++) {
                        assertEquals(1 + j, varargs[j].intValue());
                    }
                } else {
                    assertEquals(1, ((CapturesArguments) m).getValue().intValue());
                }
            }
        }
    }
}