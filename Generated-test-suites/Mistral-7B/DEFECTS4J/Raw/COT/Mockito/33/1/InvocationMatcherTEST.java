import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.Invocations;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.matchers.CapturesArguments;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class InvocationMatcherTest {

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndEmptyList() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        Method method = matcher.getMethod();
        assertEquals("testMethod", method.getName());
        List<Matcher> matchers = matcher.getMatchers();
        assertTrue(matchers.isEmpty());
    }

    @Test
    public void testInvocationMatcherConstructorWithInvocationAndList() {
        Invocation invocation = createMockInvocation();
        List<Matcher> matchers = Arrays.asList(new ArgumentMatcher("arg1"), new ArgumentMatcher("arg2"));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Method method = matcher.getMethod();
        assertEquals("testMethod", method.getName());
        List<Matcher> matchersList = matcher.getMatchers();
        assertEquals(2, matchersList.size());
        assertTrue(matchersList.contains(new ArgumentMatcher("arg1")));
        assertTrue(matchersList.contains(new ArgumentMatcher("arg2")));
    }

    @Test
    public void testGetInvocation() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        Invocation actualInvocation = matcher.getInvocation();
        assertEquals(invocation, actualInvocation);
    }

    @Test
    public void testGetMatchers() {
        Invocation invocation = createMockInvocation();
        List<Matcher> matchers = Arrays.asList(new ArgumentMatcher("arg1"), new ArgumentMatcher("arg2"));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        List<Matcher> actualMatchers = matcher.getMatchers();
        assertEquals(2, actualMatchers.size());
        assertTrue(actualMatchers.contains(new ArgumentMatcher("arg1")));
        assertTrue(actualMatchers.contains(new ArgumentMatcher("arg2")));
    }

    @Test
    public void testToString() {
        Invocation invocation = createMockInvocation();
        List<Matcher> matchers = Arrays.asList(new ArgumentMatcher("arg1"), new ArgumentMatcher("arg2"));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        String actualToString = matcher.toString();
        String expectedToString = "InvocationMatcher [invocation=Invocation [method=public void testMethod(java.lang.String arg1, java.lang.String arg2)], matchers=[ArgumentMatcher [description=arg1 = " +
                "'arg1']], [ArgumentMatcher [description=arg2 = " +
                "'arg2']]]";
        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testMatches() {
        Invocation invocation1 = createMockInvocation();
        Invocation invocation2 = createMockInvocation();
        List<Matcher> matchers = Arrays.asList(new ArgumentMatcher("arg1"), new ArgumentMatcher("arg2"));
        InvocationMatcher matcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        assertTrue(matcher1.matches(matcher1));
        assertFalse(matcher1.matches(matcher2));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Invocation invocation = createMockInvocation();
        List<Matcher> matchers = Arrays.asList(new ArgumentMatcher("arg1"), new ArgumentMatcher("arg2"));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Object[] actualArgs = new Object[]{1, "test"};
        assertTrue(matcher.safelyArgumentsMatch(actualArgs));
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatchThrowsException() {
        Invocation invocation = createMockInvocation();
        List<Matcher> matchers = Arrays.asList(new ArgumentMatcher("arg1"), new ArgumentMatcher("arg2"));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Object[] actualArgs = new Object[]{new Exception("TestException")};
        matcher.safelyArgumentsMatch(actualArgs);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation invocation1 = createMockInvocation();
        Invocation invocation2 = createMockInvocation();

        InvocationMatcher matcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher matcher2 = new InvocationMatcher(invocation2);

        Method method1 = invocation1.getMethod();
        Method method2 = invocation2.getMethod();

        assertTrue(matcher1.hasSimilarMethod(matcher1));
        assertTrue(matcher1.hasSimilarMethod(matcher2));
        assertFalse(matcher2.hasSimilarMethod(matcher1));

        Method differentMethod = createDifferentMethod();
        Invocation differentInvocation = createMockInvocation(differentMethod);
        InvocationMatcher differentMatcher = new InvocationMatcher(differentInvocation);

        assertFalse(matcher1.hasSimilarMethod(differentMatcher));
    }

    @Test
    public void testHasSameMethod() {
        Invocation invocation1 = createMockInvocation();
        Invocation invocation2 = createMockInvocation();

        Method method1 = invocation1.getMethod();
        Method method2 = invocation2.getMethod();

        assertTrue(matcher1.hasSameMethod(matcher1));
        assertFalse(matcher1.hasSameMethod(matcher2));
        assertTrue(matcher1.hasSameMethod(new InvocationMatcher(invocation1)));
        assertTrue(matcher1.hasSameMethod(new InvocationMatcher(invocation1, Arrays.asList(new ArgumentMatcher("arg1")))));

        assertTrue(matcher2.hasSameMethod(matcher2));
        assertFalse(matcher2.hasSameMethod(matcher1));
        assertTrue(matcher2.hasSameMethod(new InvocationMatcher(invocation2)));
        assertTrue(matcher2.hasSameMethod(new InvocationMatcher(invocation2, Arrays.asList(new ArgumentMatcher("arg1")))));

        Method differentMethod = createDifferentMethod();
        Invocation differentInvocation = createMockInvocation(differentMethod);
        InvocationMatcher differentMatcher = new InvocationMatcher(differentInvocation);

        assertFalse(matcher1.hasSameMethod(differentMatcher));
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = createMockInvocation();
        InvocationMatcher matcher = new InvocationMatcher(invocation);

        Location actualLocation = matcher.getLocation();
        Location expectedLocation = new Location("testClass.java", 1, 1);
        assertEquals(expectedLocation.getClassName(), actualLocation.getClassName());
        assertEquals(expectedLocation.getLineNumber(), actualLocation.getLineNumber());
        assertEquals(expectedLocation.getColumnNumber(), actualLocation.getColumnNumber());
    }

    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = createMockInvocation();
        List<Matcher> matchers = Arrays.asList(new ArgumentMatcher("arg1"), new ArgumentMatcher("arg2"));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        PrintSettings printSettings = new PrintSettings();
        String actualToString = matcher.toString(printSettings);
        String expectedToString = "InvocationMatcher [invocation=Invocation [method=public void testMethod(java.lang.String arg1, java.lang.String arg2)], matchers=[ArgumentMatcher [description=arg1 = " +
                "'arg1']], [ArgumentMatcher [description=arg2 = " +
                "'arg2']]]";
        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = createMockInvocation();
        List<Matcher> matchers = Arrays.asList(new CapturesArguments("arg"), new ArgumentMatcher("arg2"));
        InvocationMatcher matcher = new InvocationMatcher(invocation, matchers);

        Object[] invocationArguments = new Object[]{1, "test", new Object[]{1, 2, 3}};
        matcher.captureArgumentsFrom(new Invocation(new Method("testMethod", new Class[]{Integer.class, String.class, int[].class}), invocationArguments));

        CapturesArguments actualCapturesArguments = (CapturesArguments) matchers.get(0);
        Object actualCapturedArgument = actualCapturesArguments.getCapturedArguments()[0];
        assertEquals(1, actualCapturedArgument);
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = Arrays.asList(createMockInvocation(), createMockInvocation());
        List<InvocationMatcher> matchers = InvocationMatcher.createFrom(invocations);

        InvocationMatcher matcher1 = matchers.get(0);
        InvocationMatcher matcher2 = matchers.get(1);

        assertEquals(2, matchers.size());
        assertTrue(matcher1 instanceof InvocationMatcher);
        assertTrue(matcher2 instanceof InvocationMatcher);

        Method method1 = matcher1.getMethod();
        Method method2 = matcher2.getMethod();

        assertNotSame(method1, method2);
    }

    @Test(expected = AssertionError.class)
    public void testCreateFromWithNullInvocation() {
        List<Invocation> invocations = Arrays.asList(null);
        InvocationMatcher.createFrom(invocations);
    }

    private Invocation createMockInvocation() {
        Method method = createMethod("testMethod", new Class[]{String.class, String.class});
        return new Invocation(method, new Object[]{1, "test"});
    }

    private Method createMethod(String name, Class<?>... parameterTypes) {
        try {
            return Class.forName("testClass").getMethod(name, parameterTypes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Method createDifferentMethod() {
        return new Method("differentMethod", new Class[0]);
    }
}