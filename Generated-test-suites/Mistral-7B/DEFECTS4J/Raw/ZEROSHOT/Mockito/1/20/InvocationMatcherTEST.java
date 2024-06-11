import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertNotNull;

public class InvocationMatcherTest {

    private static final String CLASS_NAME = InvocationMatcher.class.getName();

    private InvocationMatcher invocationMatcher;
    private Object mock;

    @Before
    public void setup() {
        mock = Mockito.mock(Object.class);
    }

    @Test
    public void testInvocationMatcherWithEmptyMatchers() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Collections.emptyList();

        invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
        assertEquals(invocation.toString(), invocationMatcher.toString());
        assertTrue(invocationMatcher.matches(invocation));
    }

    @Test
    public void testInvocationMatcherWithCustomMatchers() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.isA(String.class), ArgumentMatchers.isNull());

        invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
        assertEquals(invocation.toString(), invocationMatcher.toString());

        Method method = invocation.getMethod();
        Object[] arguments = new Object[]{new String("test"), null};

        Answer<Object> answer = invocation -> "Matched";
        Mockito.when(mock.methodCall(method, ArgumentMatchers.is("test"), ArgumentMatchers.isNull()))
                .thenAnswer(answer);

        Object result = invocationMatcher.matches(new InvocationOnMock(mock, method, invocation.getArguments(), null));

        assertEquals("Matched", result);
    }

    @Test
    public void testInvocationMatcherWithSameMethod() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Collections.emptyList();

        Method method = Mockito.mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method);
        when(invocation2.getMethod()).thenReturn(method);

        invocationMatcher = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2, matchers);

        assertTrue(invocationMatcher.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcherWithDifferentMethods() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Collections.emptyList();

        Method method1 = Mockito.mock(Method.class);
        when(invocation1.getMethod()).thenReturn(method1);
        Method method2 = Mockito.mock(Method.class);
        when(invocation2.getMethod()).thenReturn(method2);

        assertFalse(invocationMatcher.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcherWithDifferentMocks() {
        Invocation invocation1 = Mockito.mock(InvocationOnMock.class);
        Invocation invocation2 = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Collections.emptyList();

        assertFalse(invocationMatcher.matches(invocationMatcher2));
    }

    @Test
    public void testInvocationMatcherWithNullInvocation() {
        List<Matcher> matchers = Collections.emptyList();

        InvocationMatcher invocationMatcher = new InvocationMatcher(null, matchers);

        assertNull(invocationMatcher);
    }

    @Test
    public void testInvocationMatcherWithNullMatchers() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, null);

        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
        assertEquals(invocation.toString(), invocationMatcher.toString());
        assertTrue(invocationMatcher.matches(invocation));
    }

    @Test
    public void testCaptureArgumentsFromInvocation() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.isA(String.class));

        Object argument = "test";

        invocationMatcher = new InvocationMatcher(invocation, matchers);
        invocationMatcher.captureArgumentsFrom(invocation);

        Matcher m = matchers.get(0);
        assertNotNull(m);
        assertEquals(argument, m.getRealValue());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFromInvocationWithVarargs() {
        Invocation invocation = Mockito.mock(InvocationOnMock.class);
        List<Matcher> matchers = Collections.singletonList(ArgumentMatchers.isA(String[].class));

        Object[] arguments = new Object[]{new String[]{"test1", "test2"}};

        invocationMatcher = new InvocationMatcher(invocation);
        invocationMatcher.captureArgumentsFrom(invocation);

        Matcher m = matchers.get(0);
        m.captureFrom(invocation.getRawArguments()[0]);
    }
}