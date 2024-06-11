import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.runners.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

@RunWith(MockitoJUnitRunner.class)
public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyMatcherList() {
        Invocation mockInvocation = Mockito.mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);

        Method method = mockInvocation.getMethod();
        assertTrue(invocationMatcher.hasSameMethod(Mockito.argThat(i -> i.getMethod().equals(method))));
    }

    @Test
    public void testConstructorWithInvocationAndMatcherList() {
        Invocation mockInvocation = Mockito.mock(Invocation.class);
        Method mockMethod = mock(Method.class);
        List<Matcher> matchers = Arrays.asList(eq("expected1"), eq("expected2"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, matchers);

        assertTrue(invocationMatcher.hasSameMethod(Mockito.argThat(i -> i.getMethod().equals(mockMethod))));
    }

    @Test
    public void testGetMethod() {
        Invocation mockInvocation = Mockito.mock(Invocation.class);
        Method mockMethod = mock(Method.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);
        InvocationMatcher invocationMatcherWithMethod = new InvocationMatcher(mockInvocation, Collections.emptyList());

        mockInvocation.setMethod(mockMethod);
        invocationMatcher.setInvocation(mockInvocation);

        InvocationMatcher invocationMatcherWithSetMethod = new InvocationMatcher(mockInvocation);
        invocationMatcherWithSetMethod.setMethod(mockMethod);

        assertTrue(invocationMatcher.getMethod().equals(mockMethod));
        assertTrue(invocationMatcherWithMethod.getMethod().equals(mockMethod));
        assertTrue(invocationMatcherWithSetMethod.getMethod().equals(mockMethod));
    }

    @Test
    public void testGetInvocation() {
        Invocation mockInvocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);

        assertTrue(invocationMatcher.getInvocation().equals(mockInvocation));
    }

    @Test
    public void testGetMatchers() {
        Invocation mockInvocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(eq("expected1"), eq("expected2"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, matchers);

        assertTrue(invocationMatcher.getMatchers().equals(matchers));
    }

    @Test
    public void testToString() {
        Invocation mockInvocation = mock(Invocation.class);
        List<Matcher> matchers = Arrays.asList(eq("expected1"), eq("expected2"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, matchers);

        String printSettings = "PrintSettings";
        String expected = String.format("%s(%s)", invocationMatcher.getInvocation().toString(), invocationMatcher.getMatchers().toString());
        String result = invocationMatcher.toString(new PrintSettings(printSettings));

        assertTrue(expected.equals(result));
    }

    @Test
    public void testMatchesWithSameInvocation() {
        Invocation mockInvocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);

        assertTrue(invocationMatcher.matches(mockInvocation));
    }

    @Test
    public void testMatchesWithDifferentInvocationMock() {
        Invocation mockInvocation1 = mock(Invocation.class);
        Invocation mockInvocation2 = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation1);

        assertFalse(invocationMatcher.matches(mockInvocation2));
    }

    @Test
    public void testMatchesWithDifferentMethod() {
        Invocation mockInvocation = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);

        invocationMatcher.setMethod(method1);

        assertFalse(invocationMatcher.matches(Mockito.argThat(i -> i.getMethod().equals(method2))));
    }

    @Test(expected = AssertionError.class)
    public void testMatchesWithDifferentArguments() {
        Invocation mockInvocation = mock(Invocation.class);
        Object[] arguments1 = new Object[]{"expected1"};
        Object[] arguments2 = new Object[]{"expected2"};
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);

        invocationMatcher.setArguments(arguments1);

        invocationMatcher.matches(Mockito.argThat(i -> i.getArguments()[0].equals(arguments2[0])));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation mockInvocation = mock(Invocation.class);
        Object[] arguments = new Object[]{"expected"};
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);

        assertTrue(invocationMatcher.safelyArgumentsMatch(arguments));
    }

    @Test
    public void testHasSameMethod() {
        Invocation mockInvocation1 = mock(Invocation.class);
        Invocation mockInvocation2 = mock(Invocation.class);
        Method method = mock(Method.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation1);

        invocationMatcher.setMethod(method);
        invocationMatcher.setInvocation(mockInvocation1);

        assertTrue(invocationMatcher.hasSameMethod(new InvocationMatcher(mockInvocation2, Collections.emptyList()).setMethod(method).setInvocation(mockInvocation2)));
    }

    @Test
    public void testHasSimilarMethodWithSameMethod() {
        Invocation mockInvocation1 = mock(Invocation.class);
        Invocation mockInvocation2 = mock(Invocation.class);
        Method method = mock(Method.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation1);

        invocationMatcher.setMethod(method);
        invocationMatcher.setInvocation(mockInvocation1);

        assertTrue(invocationMatcher.hasSimilarMethod(new InvocationMatcher(mockInvocation2, Collections.emptyList()).setMethod(method).setInvocation(mockInvocation2)));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethod() {
        Invocation mockInvocation1 = mock(Invocation.class);
        Invocation mockInvocation2 = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation1);

        invocationMatcher.setMethod(method1);
        invocationMatcher.setInvocation(mockInvocation1);

        assertFalse(invocationMatcher.hasSimilarMethod(new InvocationMatcher(mockInvocation2, Collections.emptyList()).setMethod(method2).setInvocation(mockInvocation2)));
    }

    @Test
    public void testHasSimilarMethodWithSameArguments() {
        Invocation mockInvocation1 = mock(Invocation.class);
        Invocation mockInvocation2 = mock(Invocation.class);
        Method method = mock(Method.class);
        Object[] arguments = new Object[]{"expected"};
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation1);

        invocationMatcher.setMethod(method);
        invocationMatcher.setInvocation(mockInvocation1);
        invocationMatcher.setArguments(arguments);

        assertTrue(invocationMatcher.hasSimilarMethod(new InvocationMatcher(mockInvocation2, Collections.emptyList()).setMethod(method).setInvocation(mockInvocation2).setArguments(arguments)));
    }

    @Test
    public void testHasSimilarMethodWithDifferentArguments() {
        Invocation mockInvocation1 = mock(Invocation.class);
        Invocation mockInvocation2 = mock(Invocation.class);
        Method method = mock(Method.class);
        Object[] arguments1 = new Object[]{"expected1"};
        Object[] arguments2 = new Object[]{"expected2"};
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation1);

        invocationMatcher.setMethod(method);
        invocationMatcher.setInvocation(mockInvocation1);
        invocationMatcher.setArguments(arguments1);

        assertFalse(invocationMatcher.hasSimilarMethod(new InvocationMatcher(mockInvocation2, Collections.emptyList()).setMethod(method).setInvocation(mockInvocation2).setArguments(arguments2)));
    }
}