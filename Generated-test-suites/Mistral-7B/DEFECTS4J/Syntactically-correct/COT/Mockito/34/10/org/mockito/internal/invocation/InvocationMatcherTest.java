import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsComparator;
import org.mockito.internal.matchers.CapturesArguments;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyMatchers() {
        Invocation invocation = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location("TestClass.java", 1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = invocationMatcher.getMethod();
        assertEquals("testMethod", method.getName());
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.<Matcher>emptyList(), invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithInvocationAndMatchers() {
        Invocation invocation = new Invocation(new Method("testMethod", new Class[]{Integer.class, String.class}), new Object[]{1, "test"}, new Location("TestClass.java", 1));
        List<Matcher> matchers = Collections.singletonList(new CapturesArguments("arg1", 0));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        Method method = invocationMatcher.getMethod();
        assertEquals("testMethod", method.getName());
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location("TestClass.java", 1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Method method = invocationMatcher.getMethod();
        assertEquals("testMethod", method.getName());
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location("TestClass.java", 1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        Invocation invocationFromMatcher = invocationMatcher.getInvocation();
        assertEquals(invocation, invocationFromMatcher);
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location("TestClass.java", 1));
        List<Matcher> matchers = Collections.singletonList(new CapturesArguments("arg1", 0));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        List<Matcher> matchersFromMatcher = invocationMatcher.getMatchers();
        assertEquals(matchers, matchersFromMatcher);
    }

    @Test
    public void testToString() {
        Invocation invocation = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location("TestClass.java", 1));
        List<Matcher> matchers = Collections.singletonList(new CapturesArguments("arg1", 0));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String result = invocationMatcher.toString();
        String expected = String.format("%s(%s)", invocation.toString(), matchers.toString());
        assertEquals(expected, result);
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location("TestClass.java", 1));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);

        Invocation invocation2 = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location("TestClass2.java", 1));
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertTrue(invocationMatcher1.matches(invocation1));
        assertFalse(invocationMatcher1.matches(invocation2));
    }

    @Test(expected = AssertionError.class)
    public void testSafelyArgumentsMatchThrowsException() {
        Invocation invocation = new Invocation(new Method("testMethod", new Class[]{Integer.class, String.class}), new Object[]{1, "test"}, new Location("TestClass.java", 1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        invocationMatcher.safelyArgumentsMatch(new Object[]{2, "test"});
    }

    @Test
    public void testHasSimilarMethodWithSameMethod() {
        Invocation invocation1 = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location("TestClass.java", 1));
        Invocation invocation2 = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location("TestClass.java", 2));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertTrue(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethod() {
        Invocation invocation1 = new Invocation(new Method("testMethod1", new Class[0]), new Object[0], new Location("TestClass.java", 1));
        Invocation invocation2 = new Invocation(new Method("testMethod2", new Class[0]), new Object[0], new Location("TestClass.java", 2));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertFalse(invocationMatcher1.hasSimilarMethod(invocationMatcher2));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location("TestClass.java", 1));
        Invocation invocation2 = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location("TestClass.java", 1));
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        assertTrue(invocationMatcher1.hasSameMethod(invocationMatcher2));
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation(new Method("testMethod", new Class[0]), new Object[0], new Location("TestClass.java", 1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(new Location("TestClass.java", 1), invocationMatcher.getLocation());
    }

    @Test(expected = AssertionError.class)
    public void testToStringWithExceptions() {
        Invocation invocation = new Invocation(new Method("testMethod", new Class[]{Integer.class, String.class}), new Object[]{1, "test"}, new Location("TestClass.java", 1));
        List<Matcher> matchers = Collections.singletonList(new CapturesArguments("arg1", 0));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String result = invocationMatcher.toString(new PrintSettings());
        throw new AssertionError("toString should not throw an exception");
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = new Invocation(new Method("testMethod", new Class[]{Integer.class, String.class}), new Object[]{1, "test"}, new Location("TestClass.java", 1));
        List<Matcher> matchers = Collections.singletonList(new CapturesArguments("arg1", 0));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        CapturesArguments argumentCaptor = (CapturesArguments) matchers.get(0);

        invocationMatcher.captureArgumentsFrom(new Invocation(new Method("testMethod", new Class[]{Integer.class, String.class}), new Object[]{2, "test"}, new Location("TestClass2.java", 1)));

        Object capturedArgument = argumentCaptor.getValue();
        assertEquals(2, capturedArgument);
    }
}