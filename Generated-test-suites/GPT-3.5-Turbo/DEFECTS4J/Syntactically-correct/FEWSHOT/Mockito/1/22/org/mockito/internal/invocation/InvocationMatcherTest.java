package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class InvocationMatcherTest {
    private Invocation invocation;
    private Method method;
    private List<Matcher> matchers;

    @Before
    public void setUp() throws NoSuchMethodException {
        method = DummyClass.class.getDeclaredMethod("dummyMethod", String.class, int.class);
        invocation = new Invocation(DummyClass.class, method, new Object[]{"arg1", 10});
        matchers = new ArrayList<>();
        matchers.add(new EqualsMatcher("arg1"));
        matchers.add(new EqualsMatcher(10));
    }

    @Test
    public void testGetMethod() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String expected = "[EqualsMatcher: \"arg1\", EqualsMatcher: 10] - dummyMethod(\"arg1\", 10)";
        assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testMatches() throws NoSuchMethodException {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Create a matching invocation
        Invocation matchingInvocation = new Invocation(DummyClass.class, method, new Object[]{"arg1", 10});
        assertTrue(invocationMatcher.matches(matchingInvocation));

        // Create a non-matching invocation
        Invocation nonMatchingInvocation = new Invocation(DummyClass.class, method, new Object[]{"arg2", 20});
        assertFalse(invocationMatcher.matches(nonMatchingInvocation));

        // Create an invocation with different arguments
        Invocation differentArgumentsInvocation = new Invocation(DummyClass.class, method, new Object[]{"arg1", 20});
        assertFalse(invocationMatcher.matches(differentArgumentsInvocation));

        // Create an invocation with a different method
        Method differentMethod = DummyClass.class.getDeclaredMethod("dummyMethod", String.class, double.class);
        Invocation differentMethodInvocation = new Invocation(DummyClass.class, differentMethod, new Object[]{"arg1", 10});
        assertFalse(invocationMatcher.matches(differentMethodInvocation));

        // Create an invocation with a different mock
        Invocation differentMockInvocation = new Invocation(DummyInterface.class, method, new Object[]{"arg1", 10});
        assertFalse(invocationMatcher.matches(differentMockInvocation));
    }

    @Test
    public void testHasSimilarMethod() throws NoSuchMethodException {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Create a matching invocation
        Invocation matchingInvocation = new Invocation(DummyClass.class, method, new Object[]{"arg1", 10});
        assertTrue(invocationMatcher.hasSimilarMethod(matchingInvocation));

        // Create a non-matching invocation
        Invocation nonMatchingInvocation = new Invocation(DummyClass.class, method, new Object[]{"arg2", 20});
        assertFalse(invocationMatcher.hasSimilarMethod(nonMatchingInvocation));

        // Create an invocation with different arguments
        Invocation differentArgumentsInvocation = new Invocation(DummyClass.class, method, new Object[]{"arg1", 20});
        assertFalse(invocationMatcher.hasSimilarMethod(differentArgumentsInvocation));

        // Create an invocation with a different method
        Method differentMethod = DummyClass.class.getDeclaredMethod("dummyMethod", String.class, double.class);
        Invocation differentMethodInvocation = new Invocation(DummyClass.class, differentMethod, new Object[]{"arg1", 10});
        assertFalse(invocationMatcher.hasSimilarMethod(differentMethodInvocation));

        // Create an invocation with a different mock
        Invocation differentMockInvocation = new Invocation(DummyInterface.class, method, new Object[]{"arg1", 10});
        assertFalse(invocationMatcher.hasSimilarMethod(differentMockInvocation));

        // Create an invocation with the same method name but overloaded
        Method overloadedMethod = DummyClass.class.getDeclaredMethod("dummyMethod", String.class, int.class, boolean.class);
        Invocation overloadedMethodInvocation = new Invocation(DummyClass.class, overloadedMethod, new Object[]{"arg1", 10, true});
        assertFalse(invocationMatcher.hasSimilarMethod(overloadedMethodInvocation));
    }

    @Test
    public void testHasSameMethod() throws NoSuchMethodException {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Create an invocation with the same method
        Method sameMethod = DummyClass.class.getDeclaredMethod("dummyMethod", String.class, int.class);
        Invocation sameMethodInvocation = new Invocation(DummyClass.class, sameMethod, new Object[]{"arg1", 10});
        assertTrue(invocationMatcher.hasSameMethod(sameMethodInvocation));

        // Create an invocation with a different method
        Method differentMethod = DummyClass.class.getDeclaredMethod("dummyMethod", String.class, double.class);
        Invocation differentMethodInvocation = new Invocation(DummyClass.class, differentMethod, new Object[]{"arg1", 10});
        assertFalse(invocationMatcher.hasSameMethod(differentMethodInvocation));

        // Create an invocation with the same method name but different parameters
        Method differentParamsMethod = DummyClass.class.getDeclaredMethod("dummyMethod", String.class, Integer.class);
        Invocation differentParamsMethodInvocation = new Invocation(DummyClass.class, differentParamsMethod, new Object[]{"arg1", 10});
        assertFalse(invocationMatcher.hasSameMethod(differentParamsMethodInvocation));
    }

    @Test
    public void testGetLocation() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        Location location = invocation.getLocation();
        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test
    public void testCaptureArgumentsFrom() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Create an invocation with arguments that can be captured
        Invocation captureInvocation = new Invocation(DummyClass.class, method, new Object[]{"captureArg", 20});
        invocationMatcher.captureArgumentsFrom(captureInvocation);

        // Verify that the captured arguments match the expected values
        assertEquals("captureArg", matchers.get(0).getArgument());
        assertEquals(20, matchers.get(1).getArgument());
    }
}

class DummyClass {
    public void dummyMethod(String arg1, int arg2) {}
}

interface DummyInterface {
    void dummyMethod(String arg1, int arg2);
}

class EqualsMatcher implements Matcher {
    private Object argument;

    public EqualsMatcher(Object argument) {
        this.argument = argument;
    }

    public Object getArgument() {
        return argument;
    }
}