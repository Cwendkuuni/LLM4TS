package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;

@SuppressWarnings("unchecked")
public class InvocationMatcherTest {

    private Invocation invocation;
    private List<Matcher> matchers;
    private InvocationMatcher invocationMatcher;

    @Before
    public void setup() {
        invocation = mock(Invocation.class);
        matchers = new ArrayList<>();
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testGetMethod() {
        Method method = mock(Method.class);
        invocation.setMethod(method);

        assertEquals(method, invocationMatcher.getMethod());
    }
    
    @Test
    public void testGetInvocation() {
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testToString() {
        assertEquals("", invocationMatcher.toString());
    }
    
    @Test
    public void testMatches() {
        Invocation actual = mock(Invocation.class);

        assertFalse(invocationMatcher.matches(actual));
    }

    @Test
    public void testMatchesWithSameMockAndMethod() {
        Invocation actual = mock(Invocation.class);
        invocation.setMock(actual.getMock());
        invocation.setMethod(actual.getMethod());

        assertTrue(invocationMatcher.matches(actual));
    }

    @Test
    public void testMatchesWithDifferentMock() {
        Invocation actual = mock(Invocation.class);
        invocation.setMock(mock(Object.class));

        assertFalse(invocationMatcher.matches(actual));
    }

    @Test
    public void testMatchesWithDifferentMethod() {
        Invocation actual = mock(Invocation.class);
        invocation.setMethod(mock(Method.class));

        assertFalse(invocationMatcher.matches(actual));
    }

    @Test
    public void testHasSimilarMethodWithSameMethodName() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        invocation.setMethod(method);
        candidate.setMethod(method);
        candidate.setVerified(false);
        candidate.setMock(invocation.getMock());

        assertTrue(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMethodName() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        invocation.setMethod(method);
        candidate.setMethod(mock(Method.class));
        candidate.setVerified(false);
        candidate.setMock(invocation.getMock());

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithVerifiedCandidate() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        invocation.setMethod(method);
        candidate.setMethod(method);
        candidate.setVerified(true);
        candidate.setMock(invocation.getMock());

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithDifferentMock() {
        Invocation candidate = mock(Invocation.class);
        invocation.setMock(mock(Object.class));
        candidate.setVerified(false);

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSimilarMethodWithOverloadedButSameArguments() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        invocation.setMethod(method);
        candidate.setMethod(method);
        candidate.setVerified(false);
        candidate.setMock(invocation.getMock());
        candidate.setArguments(new Object[]{"arg1", "arg2"});

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        invocation.setMethod(method);
        candidate.setMethod(method);

        assertTrue(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testHasSameMethodWithDifferentMethodName() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        invocation.setMethod(method);
        candidate.setMethod(mock(Method.class));

        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }

    @Test
    public void testHasSameMethodWithDifferentParameterTypes() {
        Invocation candidate = mock(Invocation.class);
        Method method = mock(Method.class);
        invocation.setMethod(method);
        candidate.setMethod(mock(Method.class));

        assertFalse(invocationMatcher.hasSameMethod(candidate));
    }
    
    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);
        invocation.setLocation(location);
        
        assertEquals(location, invocationMatcher.getLocation());
    }
    
    @Test
    public void testCaptureArgumentsFromWithVarArgs() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Object[] args = { "arg1", "arg2" };
        invocation.setMethod(mock(Method.class, args));
        
        invocationMatcher.captureArgumentsFrom(invocation);
        // Verify captured arguments
    }

    @Test
    public void testCaptureArgumentsFromWithoutVarArgs() {
        Invocation invocation = mock(Invocation.class);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Object[] args = { "arg1", "arg2" };
        invocation.setMethod(mock(Method.class, args));
        
        invocationMatcher.captureArgumentsFrom(invocation);
        // Verify captured arguments
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new ArrayList<>();
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        invocations.add(invocation1);
        invocations.add(invocation2);
        
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);
        
        assertEquals(2, invocationMatchers.size());
        assertEquals(invocation1, invocationMatchers.get(0).getInvocation());
        assertEquals(invocation2, invocationMatchers.get(1).getInvocation());
    }
}