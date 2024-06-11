import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationOnMockImpl;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.util.reflection.Reflections;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndEmptyMatchers() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Method method = Reflections.getMethod(InvocationMatcher.class, "getMethod");
        assertThat(invocationMatcher.getMethod(), is(method.getDeclaringClass()));
        assertThat(invocationMatcher.getInvocation(), is(invocation));
        assertThat(invocationMatcher.getMatchers(), is(Collections.emptyList()));
    }

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndMatchers() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Collections.singletonList(Mockito.mock(ArgumentsMatcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Method method = Reflections.getMethod(InvocationMatcher.class, "getMethod");
        assertThat(invocationMatcher.getMethod(), is(method.getDeclaringClass()));
        assertThat(invocationMatcher.getInvocation(), is(invocation));
        assertThat(invocationMatcher.getMatchers(), is(matchers));
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertThat(invocationMatcher.getInvocation(), is(invocation));
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        List<Matcher> matchers = Collections.singletonList(Mockito.mock(ArgumentsMatcher.class));
        InvocationMatcher invocationMatcherWithMatchers = new InvocationMatcher(invocation, matchers);
        assertThat(invocationMatcher.getMatchers(), is(Collections.emptyList()));
        assertThat(invocationMatcherWithMatchers.getMatchers(), is(matchers));
    }

    @Test
    public void testToString() {
        Invocation invocation = new InvocationOnMockImpl(new PrintableInvocation() {
            @Override
            public String toString() {
                return "mock.toString()";
            }
        }, new Object[0]);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        String printSettingsString = "PrintSettings{suppressDefaultErrors(true), suppressExceptions(false), suppressArguments(false), printStacktrace(true)}";
        String expectedToString = "InvocationMatcher[this=InvocationMatcher@785d3282, invocation=mock.toString(), matchers=[]]";
        assertThat(invocationMatcher.toString(), is(expectedToString));
        assertThat(invocationMatcher.toString(new PrintSettings()), is(expectedToString + " " + printSettingsString));
    }

    @Test(expected = IllegalStateException.class)
    public void testMatchesWithDifferentMock() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);
        invocationMatcher.matches(new InvocationOnMockImpl(invocation2.getMock(), invocation2.getArguments()));
    }

    @Test
    public void testHasSameMethod() {
        Method method1 = Reflections.getMethod(InvocationOnMock.class, "toString", new Class[0]);
        Method method2 = Reflections.getMethod(InvocationOnMock.class, "toString", new Class[0]);
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertTrue(invocationMatcher.hasSameMethod(new InvocationMatcher(invocation, Collections.emptyList(), method1)));
        assertTrue(invocationMatcher.hasSameMethod(new InvocationMatcher(invocation, Collections.emptyList(), method2)));
        assertFalse(invocationMatcher.hasSameMethod(new InvocationMatcher(invocation, Collections.emptyList(), Reflections.getMethod(String.class, "toString", new Class[0]))));
    }

    @Test
    public void testHasSimilarMethod() {
        Method method1 = Reflections.getMethod(InvocationOnMock.class, "toString", new Class[0]);
        Method method2 = Reflections.getMethod(String.class, "toString", new Class[0]);
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        assertFalse(invocationMatcher.hasSimilarMethod(new InvocationMatcher(invocation, Collections.emptyList(), method1)));
        assertFalse(invocationMatcher.hasSimilarMethod(new InvocationMatcher(invocation, Collections.emptyList(), method2)));
        InvocationMatcher methodMatcher1 = new InvocationMatcher(invocation, Collections.singletonList(new ArgumentsMatcher()));
        InvocationMatcher methodMatcher2 = new InvocationMatcher(invocation, Collections.singletonList(new ArgumentsMatcher()));
        assertTrue(invocationMatcher.hasSimilarMethod(methodMatcher1));
        assertTrue(invocationMatcher.hasSimilarMethod(methodMatcher2));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] actualArgs = new Object[0];
        InvocationMatcher invocationMatcher = new InvocationMatcher(Mockito.mock(InvocationOnMock.class), Collections.emptyList());
        assertTrue(invocationMatcher.safelyArgumentsMatch(actualArgs));
    }

    @Test(expected = AssertionError.class)
    public void testSafelyArgumentsMatchWithException() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(Mockito.mock(InvocationOnMock.class), Collections.emptyList());
        Object[] actualArgs = {new Object() {
            @Override
            public void foo() {
                throw new RuntimeException("Exception");
            }
        }};
        invocationMatcher.safelyArgumentsMatch(actualArgs);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        List<Matcher> matchers = Collections.singletonList(Mockito.mock(ArgumentsMatcher.class));
        invocationMatcher.captureArgumentsFrom(Mockito.mock(InvocationOnMock.class));
        invocationMatcher.captureArgumentsFrom(new InvocationOnMockImpl(invocation, matchers.toArray()));
    }
}