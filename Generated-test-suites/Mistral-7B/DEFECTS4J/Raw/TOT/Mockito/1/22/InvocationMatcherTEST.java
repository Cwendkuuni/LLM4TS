import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stub.Answer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InvocationMatcherTest {

    private InvocationMatcher matcher;
    private Object mock;

    @Before
    public void setup() {
        mock = Mockito.mock(Object.class);
    }

    @Test
    public void testMatchersWithNoArguments() {
        Invocation invocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.anyInt(), ArgumentMatchers.eq(mock));

        matcher = new InvocationMatcher(invocationMatcher, matchers);
        Method method = Mockito.mock(Method.class);

        assertTrue(matcher.matches(new InvocationOnMock(mock, method)));
    }

    @Test
    public void testMatchersWithArguments() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Object[] arguments = new Object[]{1, "test"};
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq("test")));

        matcher = new InvocationMatcher(invocationMatcher);
        Method method = Mockito.mock(Method.class);
        Mockito.when(method.getParameters()).thenReturn(new Class[0]);
        Mockito.when(invocation.getArguments()).thenReturn(arguments);

        assertTrue(matcher.matches(new InvocationOnMock(mock, method)));
    }

    @Test
    public void testDifferentMethods() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        List<Matcher> matchers1 = Arrays.asList(ArgumentMatchers.anyInt(), ArgumentMatchers.eq(mock));
        matcher = new InvocationMatcher(invocationMatcher1, matchers1);

        List<Matcher> matchers2 = Arrays.asList(ArgumentMatchers.anyInt(), ArgumentMatchers.eq(1));
        MatcherMatcher matcherMatcher2 = new MatcherMatcher(invocationMatcher2, matchers2);

        Method method1 = Mockito.mock(Method.class);
        Method method2 = Mockito.mock(Method.class);

        assertFalse(matcherMatcher2.matches(matcher));
        assertFalse(matcher.matches(matcherMatcher2));
    }

    @Test
    public void testSameMethodDifferentInvocations() {
        Invocation invocation1 = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        List<Matcher> matchers1 = Arrays.asList(ArgumentMatchers.anyInt());
        matcher = new InvocationMatcher(invocationMatcher1, matchers1);

        List<Matcher> matchers2 = Collections.singletonList(ArgumentMatchers.eq(mock));
        MatcherMatcher matcherMatcher2 = new MatcherMatcher(invocationMatcher2, matchers2);

        Method method = Mockito.mock(Method.class);
        Mockito.when(method.getName()).thenReturn("testMethod");
        Mockito.when(invocation1.getMethod()).thenReturn(method);
        Mockito.when(invocation2.getMethod()).thenReturn(method);

        assertTrue(matcher.matches(matcherMatcher2));
        assertTrue(matcherMatcher2.matches(matcher));
    }

    @Test
    public void testArgumentsMatching() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Object[] arguments = new Object[]{1, "test"};
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.eq(1), ArgumentMatchers.eq("test")));

        matcher = new InvocationMatcher(invocationMatcher);
        Method method = Mockito.mock(Method.class);
        Mockito.when(method.getParameters()).thenReturn(new Class[0]);
        Mockito.when(invocation.getArguments()).thenReturn(arguments);

        Answer<Object> answer = Mockito.answer(Mockito.mock(Object.class));
        Mockito.when(mock.toString()).thenAnswer(answer);

        Object result = matcher.matches(new InvocationOnMock(mock, method));
        Object expectedResult = true;

        assertTrue(result.equals(expectedResult));
    }

    @Test
    public void testArgumentsDoesNotMatch() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Object[] arguments = new Object[]{1, "test"};
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.eq(2), ArgumentMatchers.eq("test")));

        matcher = new InvocationMatcher(invocationMatcher);
        Method method = Mockito.mock(Method.class);
        Mockito.when(method.getParameters()).thenReturn(new Class[0]);
        Mockito.when(invocation.getArguments()).thenReturn(arguments);

        Object result = matcher.matches(new InvocationOnMock(mock, method));
        Object expectedResult = false;

        assertFalse(result.equals(expectedResult));
    }

    @Test
    public void testVarargs() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Object[] arguments = new Object[]{1, 2, 3, "test"};
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.anyInt()));

        matcher = new InvocationMatcher(invocationMatcher);
        Method method = Mockito.mock(Method.class);
        Mockito.when(method.getParameters()).thenReturn(new Class[]{Integer[].class});
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(invocation.getArguments()).thenReturn(arguments);

        assertTrue(matcher.matches(new InvocationOnMock(mock, method)));
    }

    @Test
    public void testVarargsDifferentSize() {
        Invocation invocation = Mockito.mock(Invocation.class);
        Object[] arguments1 = new Object[]{1, 2, 3};
        Object[] arguments2 = new Object[]{1, 2, 3, 4};
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Arrays.asList(ArgumentMatchers.anyInt()));

        matcher = new InvocationMatcher(invocationMatcher);
        Method method = Mockito.mock(Method.class);
        Mockito.when(method.getParameters()).thenReturn(new Class[]{Integer[].class});
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(invocation.getArguments()).thenReturn(arguments1);

        assertFalse(matcher.matches(new InvocationOnMock(mock, method, arguments2)));
        assertFalse(matcher.matches(new InvocationOnMock(mock, method, arguments1)));
    }
}

class MatcherMatcher implements Serializable {

    private static final long serialVersionUID = -3047126096857467610L;
    private final InvocationMatcher invocationMatcher;
    private final List<Matcher> matchers;

    public MatcherMatcher(InvocationMatcher invocationMatcher, List<Matcher> matchers) {
        this.invocationMatcher = invocationMatcher;
        this.matchers = matchers;
    }

    public boolean matches(InvocationMatcher other) {
        return invocationMatcher.matches(other);
    }

    public List<Matcher> getMatchers() {
        return matchers;
    }
}