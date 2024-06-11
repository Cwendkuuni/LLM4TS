import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.matchers.MatcherDecorator;
import org.mockito.internal.matchers.VarargMatcher;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Location;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithInvocationAndEmptyList() {
        Invocation invocation = new MockitoInvocation("mock", new Method(Object.class, new Class[0]), new Object[0], new Location(1, 1));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Test basic functionality
        Assert.assertEquals(invocation, invocationMatcher.getInvocation());
        Assert.assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
        Assert.assertNotNull(invocationMatcher.getMatchers());
        Assert.assertEquals(0, invocationMatcher.getMatchers().size());

        // Test edge cases and exceptions
        // Test null invocation
        try {
            new InvocationMatcher(null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test null method in invocation
        try {
            Invocation invocationNullMethod = new MockitoInvocation("mock", null, new Object[0], new Location(1, 1));
            new InvocationMatcher(invocationNullMethod);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test null matchers list
        try {
            Invocation invocation = new MockitoInvocation("mock", new Method(Object.class, new Class[0]), new Object[0], new Location(1, 1));
            InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test empty list vs non-empty list
        InvocationMatcher invocationMatcherWithEmptyList = new InvocationMatcher(invocation);
        InvocationMatcher invocationMatcherWithMatchers = new InvocationMatcher(invocation, Collections.singletonList(new CapturesArguments()));

        // Test toString method
        Assert.assertEquals(invocation.toString(), invocationMatcherWithEmptyList.toString());
        Assert.assertEquals(invocation.toString(), invocationMatcherWithMatchers.toString());

        // Test matches method with same invocation
        Assert.assertTrue(invocationMatcherWithEmptyList.matches(invocationMatcherWithEmptyList));
        Assert.assertTrue(invocationMatcherWithMatchers.matches(invocationMatcherWithMatchers));

        // Test matches method with different invocations
        Invocation differentInvocation = new MockitoInvocation("mock2", new Method(Object.class, new Class[0]), new Object[0], new Location(1, 1));
        Assert.assertFalse(invocationMatcherWithEmptyList.matches(differentInvocation));
        Assert.assertFalse(invocationMatcherWithMatchers.matches(differentInvocation));

        // Test hasSameMethod method with same method
        Method sameMethod = new Method(Object.class, new Class[0]);
        Invocation sameMethodInvocation = new MockitoInvocation("mock", sameMethod, new Object[0], new Location(1, 1));
        Invocation differentMethodInvocation = new MockitoInvocation("mock", new Method(Integer.class, new Class[0]), new Object[0], new Location(1, 1));

        Assert.assertTrue(invocationMatcherWithEmptyList.hasSameMethod(new InvocationMatcher(sameMethodInvocation)));
        Assert.assertTrue(invocationMatcherWithMatchers.hasSameMethod(new InvocationMatcher(sameMethodInvocation)));
        Assert.assertFalse(invocationMatcherWithEmptyList.hasSameMethod(new InvocationMatcher(differentMethodInvocation)));
        Assert.assertFalse(invocationMatcherWithMatchers.hasSameMethod(new InvocationMatcher(differentMethodInvocation)));

        // Test hasSimilarMethod method
        Invocation similarMethodInvocation = new MockitoInvocation("mock", new Method("similarName", Object.class, new Class[0]), new Object[0], new Location(1, 1));
        Assert.assertFalse(invocationMatcherWithEmptyList.hasSimilarMethod(new InvocationMatcher(similarMethodInvocation)));
        Assert.assertFalse(invocationMatcherWithMatchers.hasSimilarMethod(new InvocationMatcher(similarMethodInvocation)));

        // Test safelyArgumentsMatch method with same arguments
        Object[] sameArguments = new Object[0];
        Assert.assertTrue(invocationMatcherWithEmptyList.safelyArgumentsMatch(sameArguments));
        Assert.assertTrue(invocationMatcherWithMatchers.safelyArgumentsMatch(sameArguments));

        // Test safelyArgumentsMatch method with different arguments
        Object[] differentArguments = new Object[]{1};
        Assert.assertFalse(invocationMatcherWithEmptyList.safelyArgumentsMatch(differentArguments));
        Assert.assertFalse(invocationMatcherWithMatchers.safelyArgumentsMatch(differentArguments));
    }

    @Test
    public void testConstructorWithInvocationAndListOfMatchers() {
        Invocation invocation = new MockitoInvocation("mock", new Method(Object.class, new Class[0]), new Object[0], new Location(1, 1));
        List<Matcher> matchers = new LinkedList<>();
        matchers.add(new CapturesArguments());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Test basic functionality
        Assert.assertEquals(invocation, invocationMatcher.getInvocation());
        Assert.assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
        Assert.assertEquals(matchers, invocationMatcher.getMatchers());

        // Test edge cases and exceptions
        // Test null invocation
        try {
            new InvocationMatcher(null, matchers);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test null method in invocation
        try {
            Invocation invocationNullMethod = new MockitoInvocation("mock", null, new Object[0], new Location(1, 1));
            new InvocationMatcher(invocationNullMethod, matchers);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test null matchers list
        try {
            Invocation invocation = new MockitoInvocation("mock", new Method(Object.class, new Class[0]), new Object[0], new Location(1, 1));
            new InvocationMatcher(invocation, null);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test empty list vs non-empty list
        InvocationMatcher invocationMatcherWithEmptyList = new InvocationMatcher(invocation);
        InvocationMatcher invocationMatcherWithMatchers = new InvocationMatcher(invocation, matchers);

        // Test captureArgumentsFrom method with empty list and non-varargs
        invocationMatcherWithEmptyList.captureArgumentsFrom(invocation);
        for (int i = 0; i < invocation.getArguments().length; i++) {
            Object captured = ((CapturesArguments) invocationMatcherWithEmptyList.getMatchers().get(i)).getCapturedValue();
            Assert.assertNull(captured);
        }

        invocationMatcherWithMatchers.captureArgumentsFrom(invocation);
        for (int i = 0; i < invocation.getArguments().length; i++) {
            Object captured = ((CapturesArguments) invocationMatcherWithMatchers.getMatchers().get(i)).getCapturedValue();
            Assert.assertNull(captured);
        }

        // Test captureArgumentsFrom method with non-empty list and non-varargs
        Matcher matcher = new CapturesArguments();
        invocationMatcherWithMatchers.getMatchers().set(0, matcher);
        matcher.captureFrom(invocation.getArgumentAt(0, Object.class));
        Object captured = matcher.getCapturedValue();
        Assert.assertNotNull(captured);
        Assert.assertEquals(invocation.getArgumentAt(0, Object.class), captured);

        // Test captureArgumentsFrom method with empty list and varargs
        try {
            invocationMatcherWithEmptyList.captureArgumentsFrom(invocation);
            Assert.fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected
        }

        // Test captureArgumentsFrom method with non-empty list and varargs
        Object[] varargs = new Object[]{1, 2, 3};
        invocationMatcherWithMatchers.getMatchers().add(new VarargMatcher(Object[].class));
        ((VarargMatcher) invocationMatcherWithMatchers.getMatchers().get(invocation.getRawArguments().length - 1)).captureFrom(invocation.getRawArguments());
        Object[] capturedVarargs = ((VarargMatcher) invocationMatcherWithMatchers.getMatchers().get(invocation.getRawArguments().length - 1)).getCapturedValues();
        for (int i = 0; i < varargs.length; i++) {
            Assert.assertEquals(varargs[i], capturedVarargs[i]);
        }
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new LinkedList<>();
        invocations.add(new MockitoInvocation("mock", new Method(Object.class, new Class[0]), new Object[0], new Location(1, 1)));
        invocations.add(new MockitoInvocation("mock", new Method(Object.class, new Class[0]), new Object[0], new Location(1, 1)));

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        // Test basic functionality
        Assert.assertEquals(2, invocationMatchers.size());
        for (InvocationMatcher invocationMatcher : invocationMatchers) {
            Invocation invocation = invocationMatcher.getInvocation();
            Assert.assertNotNull(invocation);
            Assert.assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
            Assert.assertNotNull(invocationMatcher.getMatchers());
        }

        // Test edge cases and exceptions
        // Test null invocations list
        try {
            InvocationMatcher.createFrom(null);
            Assert.fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }

        // Test empty list
        List<InvocationMatcher> invocationMatchersEmptyList = InvocationMatcher.createFrom(Collections.emptyList());
        Assert.assertEquals(0, invocationMatchersEmptyList.size());

        // Test invocations with different methods
        List<Invocation> invocationsWithDifferentMethods = new LinkedList<>();
        invocationsWithDifferentMethods.add(new MockitoInvocation("mock", new Method(Object.class, new Class[0]), new Object[0], new Location(1, 1)));
        invocationsWithDifferentMethods.add(new MockitoInvocation("mock", new Method(Integer.class, new Class[0]), new Object[0], new Location(1, 1)));

        try {
            InvocationMatcher.createFrom(invocationsWithDifferentMethods);
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}