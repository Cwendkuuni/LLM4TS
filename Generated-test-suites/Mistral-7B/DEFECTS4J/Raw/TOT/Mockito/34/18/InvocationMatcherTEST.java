import org.junit.Before;
import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationMatcher;
import org.mockito.invocation.InvocationOnMockImpl;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.matchers.CaptureArguments;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private InvocationOnMock mock;

    @Before
    public void setUp() {
        mock = mock(SomeMockClass.class);
        matcher = new InvocationMatcher(new InvocationOnMockImpl(mock, new Method("someMethod", int.class), new Object[0]));
    }

    @Test
    public void testMatchSameMethodAndArguments() {
        InvocationMatcher expectedMatcher = new InvocationMatcher(new InvocationOnMockImpl(mock, new Method("someMethod", int.class), new Object[]{1}));
        List<Matcher> matchers = Arrays.asList(is(1));
        InvocationMatcher actualMatcher = new InvocationMatcher(new InvocationOnMockImpl(mock, new Method("someMethod", int.class), new Object[]{1}), matchers);

        assertThat(actualMatcher, is(equalTo(expectedMatcher)));
    }

    @Test
    public void testMatchDifferentMethod() {
        InvocationMatcher expectedMatcher = new InvocationMatcher(new InvocationOnMockImpl(mock, new Method("someOtherMethod", String.class), new Object[]{null}));
        List<Matcher> matchers = Collections.emptyList();

        InvocationMatcher actualMatcher = new InvocationMatcher(new InvocationOnMockImpl(mock, new Method("someMethod", int.class), new Object[]{1}));

        assertThat(actualMatcher, not(is(expectedMatcher)));
    }

    @Test
    public void testMatchDifferentMock() {
        InvocationMatcher expectedMatcher = new InvocationMatcher(new InvocationOnMockImpl(mock1, new Method("someMethod", int.class), new Object[]{1}));
        List<Matcher> matchers = Collections.emptyList();
        InvocationMatcher mock2 = mock(SomeMockClass.class);

        InvocationMatcher actualMatcher = new InvocationMatcher(new InvocationOnMockImpl(mock2, new Method("someMethod", int.class), new Object[]{1}));

        assertThat(actualMatcher, not(is(expectedMatcher)));
    }

    @Test
    public void testMatchWithCustomMatcher() {
        ArgumentMatcher customMatcher = argThat(new ArgumentsMatcher() {
            @Override
            public boolean matches(Object argument) {
                return argument instanceof Integer && ((Integer) argument) > 5;
            }
        });

        List<Matcher> matchers = Arrays.asList(customMatcher);
        InvocationMatcher expectedMatcher = new InvocationMatcher(new InvocationOnMockImpl(mock, new Method("someMethod", int.class), new Object[]{6}));

        InvocationMatcher actualMatcher = new InvocationMatcher(new InvocationOnMockImpl(mock, new Method("someMethod", int.class), new Object[]{6}), matchers);

        assertThat(actualMatcher, is(equalTo(expectedMatcher)));
    }

    @Test(expected = AssertionError.class)
    public void testMismatchedArguments() {
        List<Matcher> matchers = Arrays.asList(is(1), is("abc"));
        InvocationMatcher matcher = new InvocationMatcher(new InvocationOnMockImpl(mock, new Method("someMethod", int.class), new Object[]{1}), matchers);

        InvocationOnMock mock2 = mock(SomeMockClass.class);
        Object[] arguments = new Object[]{2};
        InvocationMatcher otherMatcher = new InvocationMatcher(new InvocationOnMockImpl(mock2, new Method("someMethod", int.class), arguments), matchers);

        matcher.captureArgumentsFrom(new InvocationOnMockImpl(mock2, new Method("someOtherMethod", String.class), arguments));
        verify(mock2).someOtherMethod("abc");
    }

    @Test
    public void testCaptureArguments() {
        List<Matcher> matchers = Arrays.asList(new CaptureArguments());
        InvocationMatcher matcher = new InvocationMatcher(new InvocationOnMockImpl(mock, new Method("someMethod", int.class), new Object[]{1}), matchers);

        matcher.captureArgumentsFrom(new InvocationOnMockImpl(mock, new Method("someMethod", int.class), new Object[]{2}));
        Object capturedArgument = matchers.get(0).getCapturedValue();

        assertThat(capturedArgument, is(2));
    }
}

class SomeMockClass {
    public void someMethod(int arg) {}
    public void someOtherMethod(String arg) {}
}