import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;
import org.mockito.stub.Stubber;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InvocationMatcherTest {

    @Rule
    public TestRule mockitoRule = MockitoJUnit.rule();

    private Stubber stubber;
    private InvocationMatcher invocationMatcher;
    private Object mock;

    @Before
    public void setup() {
        mock = mock(Object.class);
        stubber = mockitoRule.createStubber();
    }

    @org.junit.Test
    public void testInvocationMatcherWithEmptyListOfMatchers() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getArguments()).thenReturn(new Object[0]);
        invocationMatcher = new InvocationMatcher(invocation);

        Invocation testInvocation = Mockito.mock(InvocationOnMock.class);
        InvocationMatcher testMatcher = new InvocationMatcher(testInvocation);

        List<Matcher> matchers = invocationMatcher.getMatchers();
        List<Matcher> emptyList = Collections.emptyList();
        assertThat(matchers, is(emptyList));

        assertThat(invocationMatcher.matches(testMatcher), is(true));
    }

    @org.junit.Test
    public void testInvocationMatcherWithListOfMatchers() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Object[] arguments = new Object[]{1, "test"};
        when(invocation.getArguments()).thenReturn(arguments);

        Matcher matcher1 = ArgumentMatchers.is(1);
        Matcher matcher2 = ArgumentMatchers.is("test");
        List<Matcher> matchers = Arrays.asList(matcher1, matcher2);

        invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation testInvocation = Mockito.mock(InvocationOnMock.class);
        Object[] testArguments = new Object[]{1, "test"};
        when(testInvocation.getArguments()).thenReturn(testArguments);

        InvocationMatcher testMatcher = new InvocationMatcher(testInvocation);

        assertThat(invocationMatcher.matches(testMatcher), is(true));
    }

    @org.junit.Test
    public void testInvocationMatcherWithDifferentListOfMatchers() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        Object[] arguments = new Object[]{1, "test"};
        when(invocation.getArguments()).thenReturn(arguments);

        Matcher matcher1 = ArgumentMatchers.is(2);
        Matcher matcher2 = ArgumentMatchers.is("test1");
        List<Matcher> matchers = Arrays.asList(matcher1, matcher2);

        invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation testInvocation = Mockito.mock(InvocationOnMock.class);
        Object[] testArguments = new Object[]{1, "test"};
        when(testInvocation.getArguments()).thenReturn(testArguments);

        InvocationMatcher testMatcher = new InvocationMatcher(testInvocation);

        assertThat(invocationMatcher.matches(testMatcher), is(false));
    }

    @org.junit.Test
    public void testInvocationMatcherWithSameMethod() {
        Method method = Mockito.method(Object.class, "testMethod", new Class[]{String.class, int.class});
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(method);

        Object[] arguments = new Object[]{"testString", 1};
        when(invocation.getArguments()).thenReturn(arguments);

        invocationMatcher = new InvocationMatcher(invocation);

        Invocation testInvocation = Mockito.mock(InvocationOnMock.class);
        when(testInvocation.getMethod()).thenReturn(method);
        Object[] testArguments = new Object[]{"testString", 1};
        when(testInvocation.getArguments()).thenReturn(testArguments);

        InvocationMatcher testMatcher = new InvocationMatcher(testInvocation);

        stubber.doReturn(true).when(mock).equals(testInvocation.getMock());
        stubber.doReturn(true).when(mock).isAnnotationPresent(Mockito.Retries.class);

        assertThat(invocationMatcher.matches(testMatcher), is(true));
    }

    @org.junit.Test
    public void testInvocationMatcherWithDifferentMethods() {
        Method method1 = Mockito.method(Object.class, "testMethod1", new Class[]{String.class, int.class});
        Method method2 = Mockito.method(Object.class, "testMethod2", new Class[]{String.class, int.class});

        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(method1);

        Object[] arguments = new Object[]{"testString", 1};
        when(invocation.getArguments()).thenReturn(arguments);

        invocationMatcher = new InvocationMatcher(invocation);

        Invocation testInvocation = Mockito.mock(InvocationOnMock.class);
        when(testInvocation.getMethod()).thenReturn(method2);
        Object[] testArguments = new Object[]{"testString", 1};
        when(testInvocation.getArguments()).thenReturn(testArguments);

        InvocationMatcher testMatcher = new InvocationMatcher(testInvocation);

        assertThat(invocationMatcher.matches(testMatcher), is(false));
    }

    @org.junit.Test
    public void testInvocationMatcherWithSameArgumentsButDifferentInvocations() {
        Method method = Mockito.method(Object.class, "testMethod", new Class[]{String.class, int.class});
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(method);

        Object[] arguments = new Object[]{"testString", 1};
        when(invocation.getArguments()).thenReturn(arguments);

        invocationMatcher = new InvocationMatcher(invocation);

        Invocation testInvocation = Mockito.mock(InvocationOnMock.class);
        when(testInvocation.getMethod()).thenReturn(method);
        when(testInvocation.getIndex()).thenReturn(1);

        Object[] testArguments = new Object[]{null, 1};
        when(testInvocation.getArguments()).thenReturn(testArguments);

        InvocationMatcher testMatcher = new InvocationMatcher(testInvocation);

        stubber.doReturn(false).when(mock).equals(testInvocation.getMock());

        assertThat(invocationMatcher.matches(testMatcher), is(false));
    }

    @org.junit.Test
    public void testInvocationMatcherWithDifferentArguments() {
        Method method = Mockito.method(Object.class, "testMethod", new Class[]{String.class, int.class});
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(method);

        Object[] arguments = new Object[]{"testString", 1};
        when(invocation.getArguments()).thenReturn(arguments);

        invocationMatcher = new InvocationMatcher(invocation);

        Invocation testInvocation = Mockito.mock(InvocationOnMock.class);
        when(testInvocation.getMethod()).thenReturn(method);
        Object[] testArguments = new Object[]{null, 2};
        when(testInvocation.getArguments()).thenReturn(testArguments);

        InvocationMatcher testMatcher = new InvocationMatcher(testInvocation);

        assertThat(invocationMatcher.matches(testMatcher), is(false));
    }

    @org.junit.Test
    public void testInvocationMatcherWithVarArgs() {
        Method method = Mockito.method(Object.class, "testMethod", new Class[]{String[].class});
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(method);

        Object[] arguments = new Object[]{new String[]{"test1", "test2"}};
        when(invocation.getArguments()).thenReturn(arguments);

        invocationMatcher = new InvocationMatcher(invocation);

        Invocation testInvocation = Mockito.mock(InvocationOnMock.class);
        when(testInvocation.getMethod()).thenReturn(method);
        Object[] testArguments = new Object[]{new String[]{"test1", "test3"}};
        when(testInvocation.getArguments()).thenReturn(testArguments);

        InvocationMatcher testMatcher = new InvocationMatcher(testInvocation);

        assertThat(invocationMatcher.matches(testMatcher), is(false));
    }

    @org.junit.Test
    public void testInvocationMatcherWithCapturingMatchers() {
        Method method = Mockito.method(Object.class, "testMethod", new Class[]{String[].class});
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(method);

        Object[] arguments = new Object[]{new String[]{"test1", "test2"}};
        when(invocation.getArguments()).thenReturn(arguments);

        Matcher capturingMatcher = Mockito.argumentCaptor(String[].class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.is("test1"), capturingMatcher);

        invocationMatcher = new InvocationMatcher(invocation, matchers);

        Invocation testInvocation = Mockito.mock(InvocationOnMock.class);
        when(testInvocation.getMethod()).thenReturn(method);
        Object[] testArguments = new Object[]{new String[]{"test1", "test3"}};
        when(testInvocation.getArguments()).thenReturn(testArguments);

        InvocationMatcher testMatcher = new InvocationMatcher(testInvocation);

        testMatcher.captureArgumentsFrom(testInvocation);

        String[] capturedArguments = capturingMatcher.getValue();

        assertThat(capturedArguments, is(new String[]{"test2"}));
    }

    @org.junit.Test
    public void testCreateFromListOfInvocations() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);

        Method method = Mockito.method(Object.class, "testMethod", new Class[]{String[].class});
        when(invocation1.getMethod()).thenReturn(method);
        when(invocation2.getMethod()).thenReturn(method);

        Object[] arguments1 = new Object[]{new String[]{"test1", "test2"}};
        when(invocation1.getArguments()).thenReturn(arguments1);

        Object[] arguments2 = new Object[]{new String[]{"test3", "test4"}};
        when(invocation2.getArguments()).thenReturn(arguments2);

        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(Arrays.asList(invocationMatcher1, invocationMatcher2));

        Invocation testInvocation = Mockito.mock(InvocationOnMock.class);
        when(testInvocation.getMethod()).thenReturn(method);
        Object[] testArguments = new Object[]{new String[]{"test1", "test3"}};
        when(testInvocation.getArguments()).thenReturn(testArguments);

        InvocationMatcher testMatcher = new InvocationMatcher(testInvocation);

        assertThat(invocationMatchers.size(), is(2));
        assertThat(invocationMatchers.get(0).matches(testMatcher), is(true));
        assertThat(invocationMatchers.get(1).matches(testMatcher), is(true));
    }
}