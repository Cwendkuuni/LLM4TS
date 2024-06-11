package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {
    private Invocation invocation;
    private List<Matcher> matchers;
    private List<Invocation> invocations;

    @Before
    public void setUp() {
        invocation = new Invocation();
        matchers = new ArrayList<>();
        invocations = new ArrayList<>();
    }

    @Test
    public void testConstructorWithMatchers() {
        invocation.setMethod(new Method("methodName", null));
        matchers.add(new Matcher());
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testConstructorWithoutMatchers() {
        invocation.setMethod(new Method("methodName", null));
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertFalse(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testGetMethod() {
        Method method = new Method("methodName", null);
        invocation.setMethod(method);
        
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testMatches_sameMock_sameMethod_sameArguments() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        invocation1.setMock("mock");
        invocation2.setMock("mock");
        invocation1.setMethod(new Method("methodName", null));
        invocation2.setMethod(new Method("methodName", null));
        invocation1.setArguments(new Object[] {1, 2});
        invocation2.setArguments(new Object[] {1, 2});
        invocations.add(invocation2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertTrue(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testMatches_sameMock_sameMethod_differentArguments() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        invocation1.setMock("mock");
        invocation2.setMock("mock");
        invocation1.setMethod(new Method("methodName", null));
        invocation2.setMethod(new Method("methodName", null));
        invocation1.setArguments(new Object[] {1});
        invocation2.setArguments(new Object[] {2});
        invocations.add(invocation2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertFalse(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testMatches_differentMock_sameMethod_sameArguments() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        invocation1.setMock("mock1");
        invocation2.setMock("mock2");
        invocation1.setMethod(new Method("methodName", null));
        invocation2.setMethod(new Method("methodName", null));
        invocation1.setArguments(new Object[] {1, 2});
        invocation2.setArguments(new Object[] {1, 2});
        invocations.add(invocation2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertFalse(invocationMatcher.matches(invocation2));
    }

    @Test
    public void testHasSimilarMethod_sameMethodName_differentMock_sameArguments() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        invocation1.setMock("mock1");
        invocation2.setMock("mock2");
        invocation1.setMethod(new Method("methodName", null));
        invocation2.setMethod(new Method("methodName", null));
        invocation1.setArguments(new Object[] {1, 2});
        invocation2.setArguments(new Object[] {1, 2});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertTrue(invocationMatcher.hasSimilarMethod(invocation2));
    }

    @Test
    public void testHasSimilarMethod_differentMethodName() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        invocation1.setMock("mock1");
        invocation2.setMock("mock2");
        invocation1.setMethod(new Method("methodName1", null));
        invocation2.setMethod(new Method("methodName2", null));
        invocation1.setArguments(new Object[] {1, 2});
        invocation2.setArguments(new Object[] {1, 2});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertFalse(invocationMatcher.hasSimilarMethod(invocation2));
    }

    @Test
    public void testHasSimilarMethod_verifiedInvocation() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        invocation1.setMock("mock1");
        invocation2.setMock("mock2");
        invocation1.setMethod(new Method("methodName", null));
        invocation2.setMethod(new Method("methodName", null));
        invocation1.setArguments(new Object[] {1, 2});
        invocation2.setArguments(new Object[] {1, 2});
        invocation2.setVerified(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertFalse(invocationMatcher.hasSimilarMethod(invocation2));
    }

    @Test
    public void testHasSimilarMethod_sameMock_sameMethod_sameArguments() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        invocation1.setMock("mock");
        invocation2.setMock("mock");
        invocation1.setMethod(new Method("methodName", null));
        invocation2.setMethod(new Method("methodName", null));
        invocation1.setArguments(new Object[] {1, 2});
        invocation2.setArguments(new Object[] {1, 2});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertFalse(invocationMatcher.hasSimilarMethod(invocation2));
    }

    @Test
    public void testHasSameMethod_sameMethod_sameMock_sameArguments() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        invocation1.setMock("mock");
        invocation2.setMock("mock");
        invocation1.setMethod(new Method("methodName", null));
        invocation2.setMethod(new Method("methodName", null));
        invocation1.setArguments(new Object[] {1, 2});
        invocation2.setArguments(new Object[] {1, 2});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertTrue(invocationMatcher.hasSameMethod(invocation2));
    }

    @Test
    public void testHasSameMethod_sameMethod_sameMock_differentArguments() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        invocation1.setMock("mock");
        invocation2.setMock("mock");
        invocation1.setMethod(new Method("methodName", null));
        invocation2.setMethod(new Method("methodName", null));
        invocation1.setArguments(new Object[] {1});
        invocation2.setArguments(new Object[] {2});

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        assertTrue(invocationMatcher.hasSameMethod(invocation2));
    }

    @Test
    public void testToString() {
        Invocation invocation1 = new Invocation();
        invocation1.setMethod(new Method("methodName", null));
        matchers.add(new Matcher());
        PrintSettings printSettings = new PrintSettings();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, matchers);

        assertEquals(invocation1.toString(matchers, printSettings), invocationMatcher.toString());
    }

    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation1 = new Invocation();
        invocation1.setMethod(new Method("methodName", null));
        matchers.add(new Matcher());
        PrintSettings printSettings = new PrintSettings();

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1, matchers);

        assertEquals(invocation1.toString(matchers, printSettings), invocationMatcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        invocation1.setArguments(new Object[] {1});
        invocation2.setArguments(new Object[] {2});
        matchers.add(new CapturesArguments());
        matchers.add(new Matcher());
        invocations.add(invocation1);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation2, matchers);
        invocationMatcher.captureArgumentsFrom(invocation1);

        assertEquals(1, ((CapturesArguments)invocationMatcher.getMatchers().get(0)).getCapturedArguments().size());
        assertEquals(1, ((CaptureArguments)invocationMatcher.getMatchers().get(0)).getCapturedArguments().get(0));
    }

    @Test
    public void testCreateFrom() {
        Invocation invocation1 = new Invocation();
        Invocation invocation2 = new Invocation();
        invocations.addAll(Arrays.asList(invocation1, invocation2));

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(invocations.size(), invocationMatchers.size());
        assertTrue(invocationMatchers.contains(new InvocationMatcher(invocation1)));
        assertTrue(invocationMatchers.contains(new InvocationMatcher(invocation2)));
    }
}