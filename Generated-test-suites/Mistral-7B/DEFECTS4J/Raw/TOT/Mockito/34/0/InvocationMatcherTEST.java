import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.invocation.InvocationOnMockImpl;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.Arguments;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class InvocationMatcherTest {

    private Object mock;
    private InvocationMatcher matcher;

    @Before
    public void setup() {
        mock = Mockito.mock(String.class);
    }

    @Test
    public void testBasicMatch() {
        // Given
        Invocation invocation = new InvocationOnMockImpl(mock, new MethodTestClass(), "testMethod", new Object[]{1, "test"});
        List<Matcher> matchers = Arrays.asList(is(1), is("test"));

        // When
        matcher = new InvocationMatcher(invocation, matchers);

        // Then
        assertThat(matcher.matches(new InvocationOnMockImpl(mock, new MethodTestClass(), "testMethod", new Object[]{1, "test"}))).is(true);
    }

    @Test
    public void testEmptyMatchers() {
        // Given
        Invocation invocation = new InvocationOnMockImpl(mock, new MethodTestClass(), "testMethod", new Object[]{1, "test"});

        // When
        matcher = new InvocationMatcher(invocation);

        // Then
        assertThat(matcher.matches(new InvocationOnMockImpl(mock, new MethodTestClass(), "testMethod", new Object[]{1, "test"}))).is(true);
    }

    @Test
    public void testSingleMatcher() {
        // Given
        Invocation invocation = new InvocationOnMockImpl(mock, new MethodTestClass(), "testMethod", new Object[]{1, "test"});
        Matcher<Object> matcher = is("test");

        // When
        InvocationMatcher matchersMatcher = new InvocationMatcher(invocation, Collections.singletonList(matcher));

        // Then
        assertThat(matchersMatcher.matches(new InvocationOnMockImpl(mock, new MethodTestClass(), "testMethod", new Object[]{1, "test"}))).is(true);
    }

    @Test
    public void testNoMatch() {
        // Given
        Invocation invocation = new InvocationOnMockImpl(mock, new MethodTestClass(), "testMethod", new Object[]{1, "test"});
        List<Matcher> matchers = Arrays.asList(is(2), is("test"));

        // When
        matcher = new InvocationMatcher(invocation, matchers);

        // Then
        assertThat(matcher.matches(new InvocationOnMockImpl(mock, new MethodTestClass(), "testMethod", new Object[]{1, "test"}))).is(false);
    }

    @Test
    public void testCapturingMatcher() {
        // Given
        Invocation invocation = new InvocationOnMockImpl(mock, new MethodTestClass(), "testMethod", new Object[]{1, "test"});
        List<Matcher> matchers = Arrays.asList(new CapturesArguments());

        // When
        matcher = new InvocationMatcher(invocation, matchers);

        // Then
        ArgumentCaptor<Object> argumentCaptor = ArgumentCaptor.forClass(Object.class);
        InvocationOnMock invocationWithCaptor = Mockito.verify(mock).testMethod(argumentCaptor.capture());
        Object argument = argumentCaptor.getValue();

        // Then
        assertThat(matcher.matches(invocationWithCaptor)).is(true);
        assertThat(argument).is(notNullValue());
        // You can check the captured value here
    }

    @Test
    public void testPrint() {
        // Given
        Invocation invocation = new InvocationOnMockImpl(mock, new MethodTestClass(), "testMethod", new Object[]{1, "test"});
        List<Matcher> matchers = Arrays.asList(is(1), is("test"));

        // When
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        // Then
        String print = matcher.toString(new PrintSettings());
        assertThat(print, is("InvocationMatcher:\n" +
                "\tinvoked: testMethod(java.lang.Object test, java.lang.Integer arg1)\n" +
                "\targuments:\n" +
                "\t\t0: test\n" +
                "\t\t1: 1\n" +
                "\tmatchers:\n" +
                "\t\t0: <is test>\n" +
                "\t\t1: <is 1>\n" +
                "\tlocation:\n" +
                "\t\tline: 0\n" +
                "\t\tmethod: testMethod\n" +
                "\t\tclass: MethodTestClass\n"));
    }
}

class MethodTestClass {
    public void testMethod(String arg1, Integer arg2) {
    }
}