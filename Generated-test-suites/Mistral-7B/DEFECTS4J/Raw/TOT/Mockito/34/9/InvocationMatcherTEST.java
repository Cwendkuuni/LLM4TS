import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.Arguments;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private InvocationMatcher emptyMatcher;
    private Object mock;

    @Before
    public void setup() {
        mock = mock(SomeClass.class);
        matcher = new InvocationMatcher(Mockito.mock(Invocation.class), Arrays.asList(new CapturesArguments("arg1"), new CapturesArguments("arg2")));
        emptyMatcher = new InvocationMatcher(Mockito.mock(Invocation.class));
    }

    @Test
    public void testMatches() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(new Method("someMethod", String.class, int.class));
        Mockito.when(invocation.getMock()).thenReturn(mock);
        Mockito.when(invocation.getArguments()).thenReturn(new Object[]{new String("arg1"), new Integer(1)});

        Matcher expectedMatcher = new CapturesArguments("arg1");
        InvocationMatcher expectedMatcherInstance = new InvocationMatcher(invocation, Arrays.asList(expectedMatcher));
        Matcher actualMatcher = matcher;

        boolean result = actualMatcher.matches(expectedMatcherInstance);
        assertThat(result, is(true));
    }

    @Test
    public void testMatchesWithEmptyMatcher() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(new Method("someMethod", String.class, int.class));
        Mockito.when(invocation.getMock()).thenReturn(mock);
        Mockito.when(invocation.getArguments()).thenReturn(new Object[]{new String("arg1"), new Integer(1)});

        boolean result = emptyMatcher.matches(invocation);
        assertThat(result, is(false));
    }

    @Test
    public void testHasSameMethod() {
        InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
        InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);

        Mockito.when(invocation1.getMethod()).thenReturn(new Method("someMethod", String.class, int.class));
        Mockito.when(invocation2.getMethod()).thenReturn(new Method("someMethod", String.class, int.class));

        boolean result = matcher.hasSameMethod(invocation1);
        assertThat(result, is(true));

        invocation2.getMock().equals(invocation1.getMock());
        result = matcher.hasSameMethod(invocation2);
        assertThat(result, is(true));

        Object wrongMock = mock(WrongClass.class);
        result = matcher.hasSameMethod(new InvocationOnMock() {
            @Override
            public Method getMethod() {
                return new Method("someMethod", String.class, int.class);
            }

            @Override
            public Object[] getArguments() {
                return new Object[]{new String("arg1"), new Integer(1)};
            }

            @Override
            public Object getMock() {
                return wrongMock;
            }
        });
        assertThat(result, is(false));
    }

    @Test(expected = AssertionError.class)
    public void testSafelyArgumentsMatchThrowsException() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(new Method("someMethod", String.class, int.class));
        Mockito.when(invocation.getMock()).thenReturn(mock);
        Mockito.when(invocation.getArguments()).thenReturn(new Object[]{null, new Integer(1)});

        matcher.safelyArgumentsMatch(invocation.getArguments());
    }

    @Test
    public void testCaptureArgumentsFrom() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        ArgumentCaptor<String> stringArgumentCaptor = ArgumentCaptor.forClass(String.class);

        matcher.captureArgumentsFrom(invocation);

        verify(invocation).getArguments()[0];
        String capturedArgument = stringArgumentCaptor.getValue();
        assertThat(capturedArgument, is(sameInstance(matcher.getMatchers().get(0).match(invocation.getArguments()[0]))));
    }
}

class SomeClass {
    public SomeMethod someMethod(String arg1, int arg2) {
        // some implementation
    }
}

class WrongClass {
    // some other implementation
}