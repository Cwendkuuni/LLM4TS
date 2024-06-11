import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyList() {
        Invocation invocation = new MockitoInvocation("mock", new Method(Object.class, new Class[0]));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = invocationMatcher.getMethod();
        Assert.assertEquals(invocation.getMethod(), method);
        List<Matcher> matchers = invocationMatcher.getMatchers();
        Assert.assertEquals(Collections.emptyList(), matchers);
    }

    @Test
    public void testConstructorWithInvocationAndListOfMatchers() {
        Invocation invocation = new MockitoInvocation("mock", new Method(String.class, String.class));
        Matcher<String> argMatcher = Arguments.is("arg");
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.singletonList(argMatcher));

        Method method = invocationMatcher.getMethod();
        Assert.assertEquals(invocation.getMethod(), method);
        List<Matcher> matchers = invocationMatcher.getMatchers();
        Assert.assertEquals(Collections.singletonList(argMatcher), matchers);
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = new MockitoInvocation("mock", new Method(Object.class, new Class[0]));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation actualInvocation = invocationMatcher.getInvocation();
        Assert.assertEquals(invocation, actualInvocation);
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = new MockitoInvocation("mock", new Method(Object.class, new Class[0]));
        Matcher<String> argMatcher = Arguments.is("arg");
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.singletonList(argMatcher));

        List<Matcher> matchers = invocationMatcher.getMatchers();
        Assert.assertEquals(Collections.singletonList(argMatcher), matchers);
    }

    @Test
    public void testToString() {
        Invocation invocation = new MockitoInvocation("mock", new Method(Object.class, new Class[0]));
        Matcher<String> argMatcher = Arguments.is("arg");
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, Collections.singletonList(argMatcher));

        String toString = invocationMatcher.toString();
        String expectedToString = "InvocationMatcher[this = Invocation[this = org.mockito.internal.mocking.mocking_invocation.MockitoInvocation[this = org.mockito.internal.invocation.Invocation[this = [Ljava.lang.Object;@79e8d3a3, mock, org.mockito.internal.invocation.MethodInvocation[this = java.lang.reflect.Method[this = public java.lang.String java.lang.String.toString(),[Ljava.lang.String;]@17d5e1b7]]], invocation = Invocation[this = org.mockito.internal.invocation.Invocation[this = [Ljava.lang.Object;@79e8d3a3, mock, org.mockito.internal.invocation.MethodInvocation[this = java.lang.reflect.Method[this = public java.lang.String java.lang.String.toString(),[Ljava.lang.String;]@17d5e1b7]], arguments = [], matchers = [ArgumentsMatcher[this = org.mockito.internal.matchers.Arguments$1@16a62d57]]]]";
        Assert.assertEquals(expectedToString, toString);
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = new MockitoInvocation("mock1", new Method(Object.class, new Class[0]));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        Invocation invocation2 = new MockitoInvocation("mock2", new Method(Object.class, new Class[0]));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        boolean matches = invocationMatcher1.matches(invocationMatcher2);
        Assert.assertFalse(matches);

        invocationMatcher1 = new InvocationMatcher(invocation1, Collections.singletonList(Arguments.is(1)));
        matches = invocationMatcher1.matches(invocationMatcher2);
        Assert.assertFalse(matches);

        invocationMatcher2 = new InvocationMatcher(invocation2, Collections.singletonList(Arguments.is(1)));
        matches = invocationMatcher1.matches(invocationMatcher2);
        Assert.assertTrue(matches);
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = new MockitoInvocation("mock", new Method(Object.class, String.class));
        Object[] arguments = new Object[]{"arg"};
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        boolean safelyArgumentsMatch = invocationMatcher.safelyArgumentsMatch(arguments);
        ArgumentsComparator argumentsComparator = new ArgumentsComparator();
        Matcher<Object[]> matcher = argumentsComparator.argumentsMatch(invocationMatcher, arguments);
        Assert.assertTrue(matcher.matches(arguments));
        Assert.assertTrue(safelyArgumentsMatch);

        invocationMatcher = new InvocationMatcher(invocation, Collections.singletonList(Arguments.not(Arguments.is("arg"))));
        Assert.assertFalse(invocationMatcher.safelyArgumentsMatch(arguments));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = new MockitoInvocation("mock1", new Method(Object.class, new Class[0]));
        Invocation invocation2 = new MockitoInvocation("mock2", new Method(Object.class, new Class[0]));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        boolean hasSimilarMethod = invocationMatcher1.hasSimilarMethod(invocationMatcher2);
        Assert.assertFalse(hasSimilarMethod);

        invocationMatcher1 = new InvocationMatcher(invocation1, Collections.singletonList(Arguments.is(1)));
        invocationMatcher2 = new InvocationMatcher(invocation2, Collections.singletonList(Arguments.is(1)));
        hasSimilarMethod = invocationMatcher1.hasSimilarMethod(invocationMatcher2);
        Assert.assertTrue(hasSimilarMethod);

        invocationMatcher1 = new InvocationMatcher(invocation1, Collections.singletonList(Arguments.is(1)));
        invocationMatcher2 = new InvocationMatcher(invocation2, Collections.singletonList(Arguments.not(Arguments.is(1))));
        hasSimilarMethod = invocationMatcher1.hasSimilarMethod(invocationMatcher2);
        Assert.assertFalse(hasSimilarMethod);
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = new MockitoInvocation("mock1", new Method(Object.class, new Class[0]));
        Invocation invocation2 = new MockitoInvocation("mock2", new Method(Object.class, new Class[0]));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        boolean hasSameMethod = invocationMatcher1.hasSameMethod(invocationMatcher2);
        Assert.assertFalse(hasSameMethod);

        Method method1 = invocationMatcher1.getMethod();
        Method method2 = invocationMatcher2.getMethod();
        InvocationMatcher invocationMatcher3 = new InvocationMatcher(new MockitoInvocation("mock", method1));
        InvocationMatcher invocationMatcher4 = new InvocationMatcher(new MockitoInvocation("mock", method2));
        hasSameMethod = invocationMatcher3.hasSameMethod(invocationMatcher4);
        Assert.assertTrue(hasSameMethod);
    }

    @Test(expected = RuntimeException.class)
    public void testSafelyArgumentsMatchWithException() {
        Invocation invocation = new MockitoInvocation("mock", new Method(Object.class, String.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Object[] arguments = new Object[]{"arg", new Object()};

        invocationMatcher.safelyArgumentsMatch(arguments);
    }
}