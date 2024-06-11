import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationMatcher;
import org.mockito.invocation.InvocationOnMockImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.isInstanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private Object mock;

    @Before
    public void setup() {
        mock = mock(SomeClass.class);
        Invocation invocation = new InvocationOnMockImpl(mock, null, new Object[0], getClass().getName(), 0);
        List<Matcher> matchers = Arrays.asList(isA(String.class), isInstanceOf(SomeInterface.class));
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testMatchesInvocationWithSameMethodAndArguments() {
        InvocationMatcher testInvocation = new InvocationMatcher(mock, getMethodWithOneStringArgumentAndSomeInterface(), Collections.emptyList());
        assertThat(invocationMatcher, testInvocation);
    }

    @Test
    public void testMatchesInvocationWithSameMethodButDifferentArguments() {
        InvocationMatcher testInvocation = new InvocationMatcher(mock, getMethodWithOneStringArgumentAndSomeInterface(), new Object[]{new Object()});
        assertFalse(invocationMatcher.matches(testInvocation));
    }

    @Test
    public void testMatchesInvocationWithDifferentMethod() {
        Object anotherMock = mock(AnotherClass.class);
        InvocationMatcher testInvocation = new InvocationMatcher(Mockito.mock(AnotherClass.class), getMethodWithNoArguments(), Collections.emptyList());
        assertFalse(invocationMatcher.matches(testInvocation));
    }

    @Test
    public void testMatchesInvocationWithOverloadedMethodButSameArguments() {
        Object anotherMock = mock(AnotherClass.class);
        InvocationMatcher testInvocation = new InvocationMatcher(Mockito.mock(AnotherClass.class), getMethodWithOneStringArgument(), new Object[]{new Object()});
        InvocationMatcher testInvocationOverloaded = new InvocationMatcher(Mockito.mock(AnotherClass.class), getMethodWithOneStringArgumentAndAnotherStringArgument(), new Object[]{new Object(), new Object()});
        assertThat(invocationMatcher, testInvocationOverloaded);
    }

    @Test
    public void testMatchesInvocationWithOverloadedMethodButDifferentArguments() {
        Object anotherMock = mock(AnotherClass.class);
        InvocationMatcher testInvocation = new InvocationMatcher(Mockito.mock(AnotherClass.class), getMethodWithOneStringArgument(), new Object[]{new Object()});
        InvocationMatcher testInvocationOverloaded = new InvocationMatcher(Mockito.mock(AnotherClass.class), getMethodWithOneStringArgumentAndAnotherStringArgument(), new Object[]{new Object(), new Object()});
        assertFalse(invocationMatcher.matches(testInvocationOverloaded));
    }

    @Test
    public void testArgumentsComparatorThrowsExceptionForDifferentTypes() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(mock, getMethodWithOneIntegerArgument(), new Object[]{new Integer(1)});
        InvocationMatcher anotherInvocationMatcher = new InvocationMatcher(mock, getMethodWithOneStringArgument(), new Object[]{"test"});
        try {
            new ArgumentsComparator().argumentsMatch(invocationMatcher, anotherInvocationMatcher.getArguments());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //Expected
        }
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        String expectedString = "test";
        ArgumentCaptor<String> stringCaptor = ArgumentCaptor.forClass(String.class);

        invocationMatcher.captureArgumentsFrom(Mockito.verify(mock).someMethod(stringCaptor.capture()));
        String actualString = stringCaptor.getValue();

        assertEquals(expectedString, actualString);
    }

    @Test
    public void testZeroInteractions() {
        verifyZeroInteractions(mock);
    }

    private Method getMethodWithOneStringArgument() {
        return getClass().getMethod("someMethod", String.class);
    }

    private Method getMethodWithOneStringArgumentAndAnotherStringArgument() {
        return getClass().getMethod("someMethodWithTwoStrings", String.class, String.class);
    }

    private Method getMethodWithOneStringArgumentAndSomeInterface() {
        return getClass().getMethod("someMethodWithInterface", String.class, SomeInterface.class);
    }

    private Method getMethodWithOneIntegerArgument() {
        return getClass().getMethod("someMethodWithInteger", Integer.class);
    }

    private Method getMethodWithNoArguments() {
        return getClass().getMethod("someMethodWithNoArguments", new Class[0]);
    }
}