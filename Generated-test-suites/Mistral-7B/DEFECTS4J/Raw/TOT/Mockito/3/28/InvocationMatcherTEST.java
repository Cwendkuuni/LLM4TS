import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.isInstanceOf;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testSimpleMatcher() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        List<Matcher> matchers = Arrays.asList(Mockito.argThat(isA(String.class)));
        InvocationMatcher im = new InvocationMatcher(invocation, matchers);

        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(method.getName()).thenReturn("testMethod");

        assertThat(im.matches(new InvocationMatcher(Mockito.mock(Invocation.class), Collections.singletonList(Mockito.argThat(isA(String.class)))))).isTrue();
        assertThat(im.matches(new InvocationMatcher(Mockito.mock(Invocation.class), Collections.singletonList(Mockito.argThat(isA(Integer.class)))))).isFalse();
    }

    @Test
    public void testVarargsMatcher() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        List<Matcher> matchers = Arrays.asList(Mockito.argThat(isInstanceOf(String[].class)), Mockito.argThat(isA(String[].class)));
        InvocationMatcher im = new InvocationMatcher(invocation, matchers);

        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(method.getName()).thenReturn("testMethod");
        Mockito.when(method.isVarArgs()).thenReturn(true);

        Object[] args = new Object[]{"arg1", "arg2"};
        Mockito.when(invocation.getArguments()).thenReturn(args);
        Mockito.when(invocation.getRawArguments()).thenReturn(new Object[]{args});

        InvocationMatcher expectedMatcher = new InvocationMatcher(Mockito.mock(Invocation.class), Arrays.asList(Mockito.argThat(isA(String[].class)), Mockito.argThat(ArraysAsList.contains("arg2"))));

        im.captureArgumentsFrom(invocation);

        assertThat(im).isEqualTo(expectedMatcher);
    }

    @Test
    public void testSameMethodWithDifferentArguments() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        List<Matcher> matchers1 = Collections.singletonList(Mockito.argThat(isA(String.class)));
        List<Matcher> matchers2 = Collections.singletonList(Mockito.argThat(isA(Integer.class)));
        InvocationMatcher im1 = new InvocationMatcher(invocation1, matchers1);
        InvocationMatcher im2 = new InvocationMatcher(invocation2, matchers2);

        Mockito.when(invocation1.getMethod()).thenReturn(method);
        Mockito.when(invocation2.getMethod()).thenReturn(method);

        Mockito.when(method.getName()).thenReturn("testMethod");

        assertThat(im1.matches(im2)).isFalse();
    }

    @Test
    public void testSameMethodWithSameArguments() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method = Mockito.mock(Method.class);
        List<Matcher> matchers1 = Collections.singletonList(Mockito.argThat(isA(String.class)));
        List<Matcher> matchers2 = Collections.singletonList(Mockito.argThat(isA(String.class)));
        InvocationMatcher im1 = new InvocationMatcher(invocation1, matchers1);
        InvocationMatcher im2 = new InvocationMatcher(invocation2, matchers2);

        Mockito.when(invocation1.getMethod()).thenReturn(method);
        Mockito.when(invocation2.getMethod()).thenReturn(method);

        Mockito.when(method.getName()).thenReturn("testMethod");

        Object[] args = new Object[]{"testArg"};
        Mockito.when(invocation1.getArguments()).thenReturn(args);
        Mockito.when(invocation2.getArguments()).thenReturn(args);

        InvocationMatcher expectedMatcher = new InvocationMatcher(Mockito.mock(Invocation.class), matchers1);

        im1.captureArgumentsFrom(invocation1);
        im2.captureArgumentsFrom(invocation2);

        assertThat(im1).isEqualTo(expectedMatcher);
        assertThat(im1).isEqualTo(im2);
    }

    @Test(expected = AssertionError.class)
    public void testDifferentMethods() {
        Object mock = Mockito.mock(Object.class);
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);
        List<Matcher> matchers1 = Collections.singletonList(Mockito.argThat(isA(String.class)));
        List<Matcher> matchers2 = Collections.singletonList(Mockito.argThat(isA(Integer.class)));
        InvocationMatcher im1 = new InvocationMatcher(invocation1, matchers1);
        InvocationMatcher im2 = new InvocationMatcher(invocation2, matchers2);

        Mockito.when(invocation1.getMethod()).thenReturn(method1);
        Mockito.when(invocation2.getMethod()).thenReturn(method2);

        Mockito.when(method1.getName()).thenReturn("testMethod1");
        Mockito.when(method2.getName()).thenReturn("testMethod2");

        im1.matches(im2);
    }
}