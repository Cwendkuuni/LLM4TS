import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    private Object mock;
    private Stubber stubber;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setup() {
        mock = mock(Object.class);
        stubber = Mockito.stub(mock);
    }

    @Test
    public void testInvocationMatcher_methodEquals_sameMethod() {
        Method method = new Method("testMethod", new Class[0]);
        Invocation invocation1 = new Invocation(mock, method, new Object[0]);
        Invocation invocation2 = new Invocation(mock, method, new Object[0]);

        List<Matcher> matchers = Arrays.asList(is(mock), is(1));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertThat(invocationMatcher1, is(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcher_methodEquals_differentMethods() {
        Method method1 = new Method("testMethod1", new Class[0]);
        Method method2 = new Method("testMethod2", new Class[0]);

        Invocation invocation1 = new Invocation(mock, method1, new Object[0]);
        Invocation invocation2 = new Invocation(mock, method2, new Object[0]);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertThat(invocationMatcher1, is(notNullValue()));
        assertThat(invocationMatcher2, is(notNullValue()));
        assertThat(invocationMatcher1, is(not(sameInstance(invocationMatcher2))));
    }

    @Test
    public void testInvocationMatcher_argumentsMatch_sameArguments() {
        Object[] arguments = new Object[0];
        Invocation invocation = new Invocation(mock, new Method("testMethod", new Class[0]), arguments);
        List<Matcher> matchers = Arrays.asList(is(mock), is(1));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Object[] argumentsToTest = new Object[0];
        InvocationMatcher invocationMatcherToTest = new InvocationMatcher(new Invocation(mock, invocation.getMethod(), argumentsToTest));

        assertThat(invocationMatcher, invocationMatcherToTest);
    }

    @Test
    public void testInvocationMatcher_argumentsMatch_differentArguments() {
        Object[] arguments = new Object[0];
        Object[] argumentsToTest = new Object[1];
        Invocation invocation = new Invocation(mock, new Method("testMethod", new Class[0]), arguments);
        List<Matcher> matchers = Arrays.asList(is(mock), is(1));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        InvocationMatcher invocationMatcherToTest = new InvocationMatcher(new Invocation(mock, invocation.getMethod(), argumentsToTest));

        assertThat(invocationMatcher, is(not(invocationMatcherToTest)));
    }

    @Test
    public void testInvocationMatcher_argumentsMatch_errorCase() {
        Object[] arguments = new Object[0];
        Object[] argumentsToTest = new Object[2];
        Invocation invocation = new Invocation(mock, new Method("testMethod", new Class[0]), arguments);
        List<Matcher> matchers = Arrays.asList(is(mock), is(1));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        InvocationMatcher invocationMatcherToTest = new InvocationMatcher(new Invocation(mock, invocation.getMethod(), argumentsToTest));

        assertThat(invocationMatcher, is(not(invocationMatcherToTest)));
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        Invocation invocation = new Invocation(mock, new Method("testMethod", new Class[0]), new Object[0]);
        List<Matcher> matchers = Arrays.asList(new ArgumentCaptor<Integer>()
                .thenCapture("integerCaptor"));

        invocationMatcher = new InvocationMatcher(invocation, matchers);

        stubber.doAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                invocationMatcher.captureArgumentsFrom(invocation);
                return null;
            }
        }).when(mock).testMethod();

        mock.testMethod();

        verify(mock).testMethod();

        int capturedArgument = invocationMatcher.getMatchers().get(0).getValue();
        assertThat(capturedArgument, is(1));
    }

    @Test(expected = RuntimeException.class)
    public void testInvocationMatcher_argumentsMatch_exceptionCase() {
        Object[] arguments = new Object[0];
        Object[] argumentsToTest = new Object[1];
        Invocation invocation = new Invocation(mock, new Method("testMethod", new Class[0]), arguments);
        List<Matcher> matchers = Arrays.asList(is(mock), is(1));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        InvocationMatcher invocationMatcherToTest = new InvocationMatcher(new Invocation(mock, invocation.getMethod(), argumentsToTest));

        try {
            invocationMatcher.matches(invocationMatcherToTest);
        } catch (RuntimeException e) {
            // Expected
        }
    }
}