import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.invocation.DescribedInvocation;
import org.mockito.invocation.Location;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Matchers.isInstanceOf;
import static org.mockito.Matchers.isNull;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    Invocation invocation;
    InvocationMatcher invocationMatcher;
    List<Matcher> matchers;
    List<Matcher> emptyMatchersList;
    InvocationMatcher emptyMatcher;
    List<Invocation> invocations;
    InvocationMatcher invocationMatcherFromList;

    @Before
    public void setUp() {
        // Create a test invocation
        Object[] arguments = new Object[]{1, "two", 3.0};
        Method method = mock(Method.class);
        when(method.getName()).thenReturn("testMethod");
        invocation = mock(Invocation.class, args, method, null);

        // Create an InvocationMatcher with matchers
        matchers = Arrays.asList(is(1), is("two"), is(3.0));
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Create an InvocationMatcher without matchers
        emptyMatchersList = Collections.emptyList();
        emptyMatcher = new InvocationMatcher(invocation, emptyMatchersList);

        // Create a list of invocations
        invocations = Arrays.asList(invocation, invocation);

        // Create an InvocationMatcher from a list of invocations
        invocationMatcherFromList = InvocationMatcher.createFrom(invocations);
    }

    @Test
    public void testConstructorWithInvocationAndMatchers() {
        // Test constructor with invocation and matchers
        assertThat(invocationMatcher, notNullValue());
        assertThat(invocationMatcher.getInvocation(), same(invocation));
        assertThat(invocationMatcher.getMatchers(), is(matchers));
    }

    @Test
    public void testConstructorWithInvocationOnly() {
        // Test constructor with invocation only
        assertThat(emptyMatcher, notNullValue());
        assertThat(emptyMatcher.getInvocation(), same(invocation));
        assertThat(emptyMatcher.getMatchers(), is(Collections.emptyList()));
    }

    @Test
    public void testGetMethod() {
        // Test getMethod()
        Method method = invocationMatcher.getMethod();
        assertThat(method, notNullValue());
        assertThat(method.getName(), eq(invocation.getMethod().getName()));
    }

    @Test
    public void testGetInvocation() {
        // Test getInvocation()
        Invocation actualInvocation = mock(Invocation.class);
        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation);
        when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());

        // Test that the current InvocationMatcher matches the given Invocation
        assertTrue(expectedMatcher.matches(actualInvocation));
        
        // Test that a different Invocation does not match
        Invocation differentInvocation = mock(Invocation.class);
        assertFalse(expectedMatcher.matches(differentInvocation));
    }

    @Test
    public void testGetMatchers() {
        // Test getMatchers()
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();
        assertThat(actualMatchers, is(matchers));
    }

    @Test
    public void testToString() {
        // Test toString()
        String expectedToString = "InvocationMatcher [this=InvocationMatcher@7958e892, invocation=InvocationMock@1540e106, matchers=[ArgumentMatcher [this=ArgumentMatcher@392408b1, description=<not described>], ArgumentMatcher [this=ArgumentMatcher@1142e12, description=<not described>], ArgumentMatcher [this=ArgumentMatcher@2107ab13, description=<not described>]]]";
        String actualToString = invocationMatcher.toString();
        assertThat(actualToString, is(expectedToString));
    }

    @Test
    public void testMatches() {
        // Test matches()
        Invocation actualInvocation = mock(Invocation.class);
        InvocationMatcher expectedMatcher = new InvocationMatcher(invocation);

        // Test that the current InvocationMatcher matches the given Invocation
        assertTrue(expectedMatcher.matches(actualInvocation));

        // Test that a different Invocation does not match
        Invocation differentInvocation = mock(Invocation.class);
        when(differentInvocation.getMethod()).thenReturn(new Method("testMethod", String[].class));
        assertFalse(expectedMatcher.matches(differentInvocation));
    }

    @Test
    public void testSafelyArgumentsMatch() {
        // Test safelyArgumentsMatch()
        Invocation actualInvocation = mock(Invocation.class);
        Object[] actualArguments = new Object[]{"one", "two", 3.0};
        when(actualInvocation.getArguments()).thenReturn(actualArguments);

        // Test that arguments match
        assertTrue(invocationMatcher.safelyArgumentsMatch(actualArguments));

        // Test that arguments do not match
        Object[] differentArguments = new Object[]{1, "two", 4.0};
        assertFalse(invocationMatcher.safelyArgumentsMatch(differentArguments));
    }

    @Test(expected = Throwable.class)
    public void testSafelyArgumentsMatchThrowsException() {
        // Test that safelyArgumentsMatch() throws an exception
        Invocation actualInvocation = mock(Invocation.class);
        when(actualInvocation.getArguments()).thenThrow(new RuntimeException("Test Exception"));

        invocationMatcher.safelyArgumentsMatch(actualInvocation.getArguments());
    }

    @Test
    public void testHasSimilarMethod() {
        // Test hasSimilarMethod()
        Invocation candidateInvocation = mock(Invocation.class);
        Method candidateMethod = mock(Method.class);
        when(candidateInvocation.getMethod()).thenReturn(candidateMethod);

        // Test that the current InvocationMatcher has a similar method to the given Invocation
        assertTrue(invocationMatcher.hasSimilarMethod(candidateInvocation));

        // Test that the current InvocationMatcher does not have a similar method to a different Invocation
        Method differentMethod = mock(Method.class);
        when(candidateInvocation.getMethod()).thenReturn(differentMethod);
        assertFalse(invocationMatcher.hasSimilarMethod(candidateInvocation));
    }

    @Test
    public void testHasSameMethod() {
        // Test hasSameMethod()
        Invocation candidateInvocation = mock(Invocation.class);
        Method candidateMethod = mock(Method.class);
        when(candidateInvocation.getMethod()).thenReturn(candidateMethod);

        // Test that the current InvocationMatcher has the same method as the given Invocation
        assertTrue(invocationMatcher.hasSameMethod(candidateInvocation));

        // Test that the current InvocationMatcher does not have the same method as a different Invocation
        Method differentMethod = mock(Method.class);
        when(candidateInvocation.getMethod()).thenReturn(differentMethod);
        assertFalse(invocationMatcher.hasSameMethod(candidateInvocation));
    }

    @Test
    public void testGetLocation() {
        // Test getLocation()
        Location expectedLocation = mock(Location.class);
        when(invocation.getLocation()).thenReturn(expectedLocation);

        // Test that the location of the InvocationMatcher is the same as the location of the Invocation
        assertThat(invocationMatcher.getLocation(), same(expectedLocation));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        // Test captureArgumentsFrom()
        Object arg1 = "one";
        Object arg2 = 2;
        Object[] varargs = new Object[]{"three", "four"};
        Invocation testInvocation = mock(Invocation.class);
        when(testInvocation.getArguments()).thenReturn(new Object[]{arg1, arg2, varargs});

        // Test that arguments are captured correctly
        Matcher argMatcher1 = argThat(isA(String.class));
        Matcher argMatcher2 = argThat(isA(Integer.class));
        Matcher varargMatcher = argThat(isA(String[].class));

        invocationMatcher.captureArgumentsFrom(testInvocation);

        // Test that the captured arguments match the expected values
        assertEquals(arg1, invocationMatcher.getMatchers().get(0).matches(argMatcher1.getValue()));
        assertEquals(arg2, invocationMatcher.getMatchers().get(1).matches(argMatcher2.getValue()));
        assertEquals(Arrays.asList("three", "four"), invocationMatcher.getMatchers().get(2).matches(varargMatcher.getValue()));
    }

    @Test
    public void testCreateFrom() {
        // Test createFrom()
        List<Matcher> matchersFromInvocations = new LinkedList<>();
        for (InvocationMatcher invocationMatcher : invocationMatcherFromList) {
            Matcher matcher = argThat(isA(String.class));
            matchersFromInvocations.add(matcher);
        }

        // Test that the matchers from the list of InvocationMatchers match the expected values
        assertEquals(matchers, matchersFromInvocations);
    }
}