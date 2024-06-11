import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private Invocation invocation;
    private Object mock;

    @Before
    public void setup() {
        mock = mock(Object.class);
        invocation = new Invocation(mock, new Method(getClass(), "testMethod"), new Object[0]);
        matcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testSimpleMatcher() {
        when(mock.testMethod(ArgumentMatchers.anyString())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        });

        InvocationMatcher simpleMatcher = new InvocationMatcher(invocation, ArgumentMatchers.argThat(m -> m.equals("test")));
        assertTrue(simpleMatcher.matches(invocation));
        verify(mock).testMethod("test");
    }

    @Test
    public void testEmptyMatcher() {
        InvocationMatcher emptyMatcher = new InvocationMatcher(invocation);
        assertFalse(emptyMatcher.matches(invocation));
    }

    @Test
    public void testDifferentMethods() {
        Object mock2 = mock(Object.class);
        Invocation invocation2 = new Invocation(mock2, new Method(getClass(), "testMethod2"), new Object[0]);

        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);
        assertFalse(matcher.matches(matcher2));
    }

    @Test
    public void testDifferentMocks() {
        Object mock3 = mock(Object.class);
        Invocation invocation3 = new Invocation(mock3, invocation.getMethod(), new Object[0]);

        InvocationMatcher matcher3 = new InvocationMatcher(invocation3);
        assertFalse(matcher.matches(matcher3));
    }

    @Test
    public void testUnverifiedInvocation() {
        Invocation unverifiedInvocation = new Invocation(mock, invocation.getMethod(), new Object[0], false);
        InvocationMatcher matcherUnverified = new InvocationMatcher(unverifiedInvocation);
        assertTrue(matcher.matches(matcherUnverified));
    }

    @Test
    public void testSameMethodDifferentArguments() {
        when(mock.testMethod(ArgumentMatchers.anyString(), ArgumentMatchers.anyInt())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        });

        InvocationMatcher sameMethodDifferentArguments = new InvocationMatcher(invocation, ArgumentMatchers.argThat(m -> m.toString().equals("test"))) {
            @Override
            public boolean matches(Invocation actual) {
                return super.matches(actual) && actual.getArguments()[0].toString().equals("error");
            }
        };

        verifyZeroInteractions(mock);
        Invocation invocationWithError = new Invocation(mock, invocation.getMethod(), new Object[]{"test", 10});
        assertFalse(sameMethodDifferentArguments.matches(invocationWithError));
    }

    @Test
    public void testSameMethodSameArguments() {
        when(mock.testMethod(ArgumentMatchers.anyString())).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return null;
            }
        });

        InvocationMatcher sameMethodSameArguments = new InvocationMatcher(invocation, ArgumentMatchers.argThat(m -> m.equals("test")));
        assertTrue(sameMethodSameArguments.matches(invocation));
    }
}