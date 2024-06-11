package org.mockito.internal.invocation;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.MethodInterceptor;
import org.mockito.stub.Stubber;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private Invocation invocation;
    private Stubber stubber;
    private Object mock;

    @Before
    public void setup() {
        mock = mock(Object.class);
        stubber = Mockito.stub(mock);
        invocation = new InvocationOnMock(stubber, new MethodInterceptor() {
            @Override
            public Object intercept(MethodInvocation invocation) throws Throwable {
                return null;
            }
        }, new Object[0], new Object[0]);
        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testConstructorWithInvocationAndEmptyMatchers() {
        InvocationMatcher emptyMatcherInvocation = new InvocationMatcher(invocation);
        List<Matcher> matchers = emptyMatcherInvocation.getMatchers();
        assertThat(matchers, is(Collections.emptyList()));
    }

    @Test
    public void testConstructorWithInvocationAndMatchers() {
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("test"), ArgumentMatchers.isA(String.class));
        InvocationMatcher matcherInvocation = new InvocationMatcher(invocation, matchers);
        List<Matcher> matchersFromInvocation = matcherInvocation.getMatchers();
        assertThat(matchersFromInvocation, is(matchers));
    }

    @Test
    public void testGetMethod() {
        Method method = invocation.getMethod();
        Method methodFromMatcher = invocationMatcher.getMethod();
        assertThat(method, is(methodFromMatcher));
    }

    @Test
    public void testGetInvocation() {
        Invocation invocationFromMatcher = invocationMatcher.getInvocation();
        assertThat(invocation, is(invocationFromMatcher));
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("test"), ArgumentMatchers.isA(String.class));
        InvocationMatcher matcherInvocation = new InvocationMatcher(invocation, matchers);
        List<Matcher> matchersFromMatcher = matcherInvocation.getMatchers();
        assertThat(matchersFromMatcher, is(matchers));
    }

    @Test
    public void testToString() {
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq("test"), ArgumentMatchers.isA(String.class));
        InvocationMatcher matcherInvocation = new InvocationMatcher(invocation, matchers);
        String toString = matcherInvocation.toString();
        //TODO: Implement a proper printing of matchers
        //The following line is a placeholder for now
        String expectedToString = "InvocationMatcher[this = InvocationMock[...], arguments = [], matchers = [" +
                "ArgThat(is equal to: test)), ArgThat(matches class: java.lang.String))]";
        assertThat(toString, is(expectedToString));
    }

    @Test
    public void testMatchesWithSameInvocation() {
        boolean matches = invocationMatcher.matches(invocationMatcher);
        assertThat(matches, is(true));
    }

    @Test
    public void testMatchesWithDifferentInvocation() {
        Invocation differentInvocation = spy(invocation);
        boolean matches = invocationMatcher.matches(differentInvocation);
        assertThat(matches, is(false));
    }

    @Test
    public void testHasSimilarMethodWithSameMethod() {
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(invocationMatcher);
        assertThat(hasSimilarMethod, is(true));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethod() {
        Method differentMethod = new Method("differentMethod", new Class[0]);
        Invocation differentInvocation = new InvocationOnMock(stubber, new MethodInterceptor() {
            @Override
            public Object intercept(MethodInvocation invocation) throws Throwable {
                return null;
            }
        }, new Object[0], new Object[0]) {
            @Override
            public Method getMethod() {
                return differentMethod;
            }
        };
        InvocationMatcher differentMatcherInvocation = new InvocationMatcher(differentInvocation);
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(differentMatcherInvocation);
        assertThat(hasSimilarMethod, is(false));
    }

    @Test
    public void testHasSameMethodWithSameMethod() {
        boolean hasSameMethod = invocationMatcher.hasSameMethod(invocationMatcher);
        assertThat(hasSameMethod, is(true));
    }

    @Test
    public void testHasSameMethodWithDifferentMethod() {
        Method differentMethod = new Method("differentMethod", new Class[0]);
        Invocation differentInvocation = new InvocationOnMock(stubber, new MethodInterceptor() {
            @Override
            public Object intercept(MethodInvocation invocation) throws Throwable {
                return null;
            }
        }, new Object[0], new Object[0]) {
            @Override
            public Method getMethod() {
                return differentMethod;
            }
        };
        InvocationMatcher differentMatcherInvocation = new InvocationMatcher(differentInvocation);
        boolean hasSameMethod = invocationMatcher.hasSameMethod(differentMatcherInvocation);
        assertThat(hasSameMethod, is(false));
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.argThat(new CapturesArguments<Integer>() {
            @Override
            public void captureFrom(Object argument) {
                int capturedArgument = ((Number) argument).intValue();
                System.out.println("Captured argument: " + capturedArgument);
            }
        }));
        InvocationMatcher matcherInvocation = new InvocationMatcher(invocation, matchers);
        matcherInvocation.captureArgumentsFrom(invocation);
        //TODO: Implement a proper testing of captured arguments
        //The following line is a placeholder for now
        verify(stubber).thenAnswer(invocationArgument -> null);
    }

    @Test
    public void testCreateFromInvocations() {
        List<Invocation> invocations = Arrays.asList(invocation, invocation);
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        InvocationMatcher firstInvocationMatcher = invocationMatchers.get(0);
        InvocationMatcher secondInvocationMatcher = invocationMatchers.get(1);
        Invocation invocationFromFirstMatcher = firstInvocationMatcher.getInvocation();
        Invocation invocationFromSecondMatcher = secondInvocationMatcher.getInvocation();
        assertThat(invocationFromFirstMatcher, is(invocation));
        assertThat(invocationFromSecondMatcher, is(invocation));
    }
}