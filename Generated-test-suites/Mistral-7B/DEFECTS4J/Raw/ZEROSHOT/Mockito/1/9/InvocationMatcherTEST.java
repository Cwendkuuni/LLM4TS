import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    private Object mock;

    @Before
    public void setUp() {
        mock = mock(Object.class);
    }

    @Test
    public void testInvocationMatcherWithEmptyMatcherList() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        List<Matcher> matchers = invocationMatcher.getMatchers();
        assertThat(matchers, is(Collections.emptyList()));
    }

    @Test
    public void testInvocationMatcherWithMatchersList() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Object[] arguments = new Object[]{1, 2, 3};
        Method method = Mockito.mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(method.getParameterTypes()).thenReturn(new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE});
        when(invocation.getArguments()).thenReturn(arguments);

        Matcher<Integer> intMatcher = ArgumentMatchers.eq(1);
        Matcher<Integer> intMatcher2 = ArgumentMatchers.eq(2);

        List<Matcher> matchers = Arrays.asList(intMatcher, intMatcher2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        List<Matcher> matchersFromInvocationMatcher = invocationMatcher.getMatchers();

        assertThat(matchersFromInvocationMatcher, is(matchers));
    }

    @Test
    public void testToString() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Object[] arguments = new Object[]{1, 2, 3};
        Method method = Mockito.mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(method.getParameterTypes()).thenReturn(new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE});
        when(invocation.getArguments()).thenReturn(arguments);

        Matcher<Integer> intMatcher = ArgumentMatchers.eq(1);
        Matcher<Integer> intMatcher2 = ArgumentMatchers.eq(2);

        List<Matcher> matchers = Arrays.asList(intMatcher, intMatcher2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String invocationMatcherString = invocationMatcher.toString();

        String expectedString = "InvocationMatcher[method=MockitoMethod[public java.lang.Object org.mockito.internal.test.InvocationMatcherTest$TestClass.testMethod(java.lang.Object,java.lang.Object)], arguments=[1, 2], matchers=[Matcher[is 1], Matcher[is 2]]]";
        assertThat(invocationMatcherString, is(expectedString));
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Object[] arguments1 = new Object[]{1, 2, 3};
        Method method1 = Mockito.mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(method1.getParameterTypes()).thenReturn(new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE});
        when(invocation1.getArguments()).thenReturn(arguments1);

        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        Object[] arguments2 = new Object[]{1, 2, 3};
        Method method2 = Mockito.mock(Method.class);
        when(invocation2.getMethod()).thenReturn(method2);
        when(method2.getParameterTypes()).thenReturn(new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE});
        when(invocation2.getArguments()).thenReturn(arguments2);

        Matcher<Integer> intMatcher = ArgumentMatchers.eq(1);
        Matcher<Integer> intMatcher2 = ArgumentMatchers.eq(2);

        List<Matcher> matchers = Arrays.asList(intMatcher, intMatcher2);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        boolean result = invocationMatcher1.matches(invocationMatcher2);

        boolean expectedResult = true;
        assertThat(result, is(expectedResult));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Object[] arguments = new Object[]{1, 2, 3};
        Method method = Mockito.mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(method.getParameterTypes()).thenReturn(new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE});
        when(invocation.getArguments()).thenReturn(arguments);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        boolean result = invocationMatcher.safelyArgumentsMatch(arguments);

        boolean expectedResult = true;
        assertThat(result, is(expectedResult));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Object[] arguments1 = new Object[]{1, 2, 3};
        Method method1 = Mockito.mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(method1.getParameterTypes()).thenReturn(new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE});
        when(invocation1.getArguments()).thenReturn(arguments1);

        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        Object[] arguments2 = new Object[]{1, 2, 3};
        Method method2 = Mockito.mock(Method.class);
        when(invocation2.getMethod()).thenReturn(method2);
        when(method2.getParameterTypes()).thenReturn(new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE});
        when(invocation2.getArguments()).thenReturn(arguments2);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        boolean result = invocationMatcher1.hasSimilarMethod(invocationMatcher2);

        boolean expectedResult = true;
        assertThat(result, is(expectedResult));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Object[] arguments1 = new Object[]{1, 2, 3};
        Method method1 = Mockito.mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        when(method1.getParameterTypes()).thenReturn(new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE});
        when(invocation1.getArguments()).thenReturn(arguments1);

        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        Object[] arguments2 = new Object[]{1, 2, 3};
        Method method2 = Mockito.mock(Method.class);
        when(invocation2.getMethod()).thenReturn(method2);
        when(method2.getParameterTypes()).thenReturn(new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE});
        when(invocation2.getArguments()).thenReturn(arguments2);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        boolean result = invocationMatcher1.hasSameMethod(invocationMatcher2);

        boolean expectedResult = true;
        assertThat(result, is(expectedResult));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Object[] arguments = new Object[]{1, 2, 3};
        Method method = Mockito.mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(method.getParameterTypes()).thenReturn(new Class[]{Integer.TYPE, Integer.TYPE, Integer.TYPE});
        when(invocation.getArguments()).thenReturn(arguments);

        Matcher<Integer> intMatcher = ArgumentMatchers.argThat(new CapturesArguments<Integer>() {
            private Integer capturedValue;

            @Override
            public boolean matches(Object argument) {
                this.capturedValue = (Integer) argument;
                return true;
            }

            @Override
            public void captureFrom(Invocation invocation) {
                int expectedCapturedValue = 2;
                int capturedValue = this.capturedValue;
                assertThat(capturedValue, is(expectedCapturedValue));
            }
        });

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.captureArgumentsFrom(invocation);

        verify(invocation).getRawArguments();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFromWithVarargs() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Object[] arguments = new Object[]{1, 2, new Object[]{3, 4}};
        Method method = Mockito.mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(method.getParameterTypes()).thenReturn(new Class[]{Integer.TYPE, Integer.TYPE, Object[].class});
        when(invocation.getArguments()).thenReturn(arguments);

        Matcher<Object[]> varargMatcher = ArgumentMatchers.argThat(new CapturesArguments<Object[]>() {
            private Object[] capturedValue;

            @Override
            public boolean matches(Object argument) {
                this.capturedValue = (Object[]) argument;
                return true;
            }

            @Override
            public void captureFrom(Invocation invocation) {
                Object[] expectedCapturedValue = new Object[]{3, 4};
                Object[] capturedValue = this.capturedValue;
                assertThat(capturedValue, is(expectedCapturedValue));
            }
        });

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.captureArgumentsFrom(invocation);

        // UnsupportedOperationException should be thrown
    }
}