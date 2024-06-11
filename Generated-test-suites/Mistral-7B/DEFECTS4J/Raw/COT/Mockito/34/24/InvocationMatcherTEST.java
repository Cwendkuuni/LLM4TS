import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.matchers.CapturesArguments;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyMatchers() {
        Invocation invocation = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location(0, 0));
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        Method method = matcher.getMethod();
        assertEquals(method.getName(), "testMethod");
    }

    @Test
    public void testConstructorWithInvocationAndMatchers() {
        Invocation invocation = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location(0, 0));
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new CapturesArguments("arg1"));
        matchers.add(new ArgumentMatcher<Integer>() {
            @Override
            public boolean matches(Object argument) {
                return argument instanceof Integer && ((Integer) argument) > 5;
            }
        });
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);
        Method method = matcher.getMethod();
        List<Matcher> matchers1 = matcher.getMatchers();
        assertEquals(method.getName(), "testMethod");
        assertEquals(matchers1.size(), 2);
    }

    @Test
    public void testHasSimilarMethod_SameMethod() {
        Invocation invocation1 = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location(0, 0));
        Invocation invocation2 = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location(0, 0));
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);
        boolean result = matcher1.hasSimilarMethod(matcher2);
        assertTrue(result);
    }

    @Test
    public void testHasSimilarMethod_DifferentMethods() {
        Invocation invocation1 = new Invocation(new Method("testMethod1", new Class[0]), new Object[0], new Location(0, 0));
        Invocation invocation2 = new Invocation(new Method("testMethod2", new Class[0]), new Object[0], new Location(0, 0));
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);
        boolean result = matcher1.hasSimilarMethod(matcher2);
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_SameMethodDifferentArguments() {
        Invocation invocation1 = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location(0, 0));
        Invocation invocation2 = new Invocation(new Method("testMethod", new Class[]{Integer.TYPE}), new Object[]{1}, new Location(0, 0));
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);
        boolean result = matcher1.hasSimilarMethod(matcher2);
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_SameMethodSameArguments() {
        Invocation invocation1 = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location(0, 0));
        Invocation invocation2 = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location(0, 0));
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);
        Object[] arguments1 = new Object[0];
        Object[] arguments2 = new Object[0];
        boolean result = matcher1.safelyArgumentsMatch(arguments2);
        assertTrue(result);
        result = matcher1.hasSimilarMethod(matcher2);
        assertTrue(result);
    }

    @Test(expected = AssertionError.class)
    public void testHasSimilarMethod_DifferentMethodsDifferentArguments() {
        Invocation invocation1 = new Invocation(new Method("testMethod1", new Class[0]), new Object[0], new Location(0, 0));
        Invocation invocation2 = new Invocation(new Method("testMethod2", new Class[]{Integer.TYPE}), new Object[]{1}, new Location(0, 0));
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);
        boolean result = matcher1.hasSimilarMethod(matcher2);
        fail("Expected AssertionError");
    }

    @Test
    public void testMatches_SameInvocations() {
        Invocation invocation1 = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location(0, 0));
        Invocation invocation2 = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location(0, 0));
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);
        boolean result = matcher1.matches(matcher2);
        assertTrue(result);
    }

    @Test
    public void testMatches_DifferentInvocationsSameMethod() {
        Invocation invocation1 = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location(0, 0));
        Invocation invocation2 = new Invocation(new Method("testMethod", new Class[0]), new Object[]{1}, new Location(1, 1));
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);
        boolean result = matcher1.matches(matcher2);
        assertFalse(result);
    }

    @Test
    public void testMatches_DifferentInvocationsSameMethodSameArguments() {
        Invocation invocation1 = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location(0, 0));
        Invocation invocation2 = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location(1, 1));
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);
        Object[] arguments1 = new Object[0];
        Object[] arguments2 = new Object[0];
        boolean result = matcher1.matches(matcher2);
        assertTrue(result);
    }

    @Test
    public void testMatches_DifferentInvocationsDifferentMethods() {
        Invocation invocation1 = new Invocation(new Method("testMethod1", new Class[0]), new Object[0], new Location(0, 0));
        Invocation invocation2 = new Invocation(new Method("testMethod2", new Class[0]), new Object[0], new Location(1, 1));
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);
        boolean result = matcher1.matches(matcher2);
        assertFalse(result);
    }

    @Test
    public void testToString() {
        Invocation invocation = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location(0, 0));
        InvocationMatcher matcher = new InvocationMatcher(invocation);
        String result = matcher.toString();
        List<Matcher> matchers = matcher.getMatchers();
        if (matchers.isEmpty()) {
            String expected = "InvocationMatcher[invocation=Invocation[method=public java.lang.String testMethod()], argumentsToMatchers()]";
            assertEquals(expected, result);
        } else {
            String expected = "InvocationMatcher[invocation=Invocation[method=public java.lang.String testMethod()], arguments="
                    + Arrays.toString(matchers.toArray()) + "]";
            assertEquals(expected, result);
        }
    }
}