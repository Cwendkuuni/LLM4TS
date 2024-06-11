import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.InvocationOnMockImpl;
import org.mockito.internal.matchers.Arguments;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private Object mock;

    @Captor
    private ArgumentCaptor<String> stringCaptor;

    @Before
    public void setup() {
        mock = Mockito.mock(String.class);
        invocationMatcher = new InvocationMatcher(new InvocationOnMockImpl(mock, new Method( "testMethod", String.class ), new Location(0, 0)), Collections.emptyList());
    }

    @Test
    public void testMatchesSameInvocation() {
        // Given
        Invocation invocation = new InvocationOnMockImpl(mock, new Method("testMethod", String.class), new Location(0, 0), new Object[]{"test"});

        // When
        InvocationMatcher test = new InvocationMatcher(invocation);

        // Then
        assertThat(test.matches(invocationMatcher), is(true));
    }

    @Test
    public void testMatchesDifferentMethod() {
        // Given
        Invocation invocation = new InvocationOnMockImpl(mock, new Method("testAnotherMethod", Integer.class), new Location(0, 0), new Object[]{1});
        InvocationMatcher test = new InvocationMatcher(new InvocationOnMockImpl(mock, new Method("testMethod", String.class), new Location(0, 0)), Collections.emptyList());

        // Then
        assertThat(test.matches(invocationMatcher), is(false));
    }

    @Test
    public void testMatchesSameMethodDifferentMock() {
        // Given
        Object mock2 = Mockito.mock(String.class);
        Invocation invocation = new InvocationOnMockImpl(mock, new Method("testMethod", String.class), new Location(0, 0), new Object[]{"test"});
        InvocationMatcher test = new InvocationMatcher(new InvocationOnMockImpl(mock2, new Method("testMethod", String.class), new Location(0, 0)), Collections.emptyList());

        // Then
        assertThat(test.matches(invocationMatcher), is(false));
    }

    @Test
    public void testMatchesOverloadedMethod() {
        // Given
        Object mock = Mockito.mock(Object.class);
        InvocationMatcher test1 = new InvocationMatcher(new InvocationOnMockImpl(mock, new Method("testMethod", String.class), new Location(0, 0), new Object[]{"test"}), Collections.emptyList());
        InvocationMatcher test2 = new InvocationMatcher(new InvocationOnMockImpl(mock, new Method("testMethod", Integer.class), new Location(0, 0), new Object[]{1}), Collections.emptyList());

        // Then
        assertThat(test1.matches(test2), is(false));
    }

    @Test
    public void testMatchesSameMethodWithArguments() {
        // Given
        String arg = "test";
        Invocation invocation = new InvocationOnMockImpl(mock, new Method("testMethod", String.class), new Location(0, 0), new Object[]{arg});
        InvocationMatcher test = new InvocationMatcher(invocation, Arrays.asList(Arguments.is(arg)));

        // When & Then
        Mockito.verify(mock).testMethod(arg);
    }

    @Test
    public void testMatchesWithCustomMatcher() {
        // Given
        Invocation invocation = new InvocationOnMockImpl(mock, new Method("testMethod", String.class), new Location(0, 0), new Object[]{"test"});
        InvocationMatcher test = new InvocationMatcher(invocation, Arrays.asList(new CustomMatcher()));

        // Then
        assertThat(test.matches(invocationMatcher), is(true));
    }

    @Test(expected = AssertionError.class)
    public void testMismatch() {
        // Given
        Invocation invocation = new InvocationOnMockImpl(mock, new Method("testMethod", String.class), new Location(0, 0), new Object[]{"test"});
        InvocationMatcher test = new InvocationMatcher(new InvocationOnMockImpl(mock, new Method("testMethod", Integer.class), new Location(0, 0), new Object[]{1}), Arrays.asList(Arguments.is("test")));

        // Then
        // This test should fail with an AssertionError
    }
}

class CustomMatcher implements Matcher<Object> {
    @Override
    public boolean matches(Object argument) {
        return "test".equals(argument);
    }
}