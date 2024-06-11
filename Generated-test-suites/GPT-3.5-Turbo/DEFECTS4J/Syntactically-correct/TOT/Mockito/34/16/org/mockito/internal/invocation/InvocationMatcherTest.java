
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;

public class InvocationMatcherTest {

    private Invocation invocation;
    private List<Matcher> matchers;

    @Before
    public void setUp() {
        invocation = mock(Invocation.class);
        matchers = new ArrayList<>();
    }

    @Test
    public void testConstructorWithMatchersNotProvided() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
        assertEquals(invocation.argumentsToMatchers(), invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithMatchersProvided() {
        List<Matcher<String>> argumentMatchers = Collections.singletonList(mock(Matcher.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, argumentMatchers);

        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(invocation.getMethod(), invocationMatcher.getMethod());
        assertEquals(argumentMatchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        String expected = invocation.toString(matchers, null);
        assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testMatches_HappyPath() {
        Invocation actualInvocation = mock(Invocation.class);
        Object[] actualArguments = new Object[] { "arg1", 2, true };

        Method method = invocation.getMethod();
        Object[] arguments = actualArguments.clone();

        Mockito.when(invocation.getMock()).thenReturn(actualInvocation.getMock());
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(actualInvocation.getMock()).thenReturn(invocation.getMock());
        Mockito.when(actualInvocation.getMethod()).thenReturn(method);
        Mockito.when(actualInvocation.getArguments()).thenReturn(actualArguments);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        boolean matches = invocationMatcher.matches(actualInvocation);

        // Assert
        assertTrue(matches);
        Mockito.verify(invocation, Mockito.times(1)).getMock();
        Mockito.verify(invocation, Mockito.times(2)).getMethod();
        Mockito.verify(actualInvocation, Mockito.times(1)).getMock();
        Mockito.verify(actualInvocation, Mockito.times(2)).getMethod();
        Mockito.verify(actualInvocation, Mockito.times(1)).getArguments();
        Mockito.verifyNoMoreInteractions(invocation, actualInvocation);
        assertArrayEquals(arguments, actualArguments);
    }

    @Test
    public void testMatches_MockNotEqual() {
        Invocation actualInvocation = mock(Invocation.class);

        Mockito.when(invocation.getMock()).thenReturn(mock(Object.class));
        Mockito.when(actualInvocation.getMock()).thenReturn(mock(Object.class));

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        boolean matches = invocationMatcher.matches(actualInvocation);

        // Assert
        assertFalse(matches);
        Mockito.verify(invocation, Mockito.times(1)).getMock();
        Mockito.verify(actualInvocation, Mockito.times(1)).getMock();
        Mockito.verifyNoMoreInteractions(invocation, actualInvocation);
    }

    @Test
    public void testMatches_MethodNotEqual() {
        Invocation actualInvocation = mock(Invocation.class);

        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);

        Mockito.when(invocation.getMethod()).thenReturn(method1);
        Mockito.when(actualInvocation.getMethod()).thenReturn(method2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        boolean matches = invocationMatcher.matches(actualInvocation);

        // Assert
        assertFalse(matches);
        Mockito.verify(invocation, Mockito.times(1)).getMethod();
        Mockito.verify(actualInvocation, Mockito.times(1)).getMethod();
        Mockito.verifyNoMoreInteractions(invocation, actualInvocation);
    }

    @Test
    public void testMatches_SameMethodButArgumentsDoNotMatch() {
        Invocation actualInvocation = mock(Invocation.class);
        Object[] actualArguments = new Object[] { "arg1", 2, true };

        Method method = invocation.getMethod();
        Method actualMethod = actualInvocation.getMethod();
        Object[] arguments = actualArguments.clone();

        Mockito.when(invocation.getMock()).thenReturn(actualInvocation.getMock());
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(actualInvocation.getMock()).thenReturn(invocation.getMock());
        Mockito.when(actualInvocation.getMethod()).thenReturn(actualMethod);
        Mockito.when(actualInvocation.getArguments()).thenReturn(actualArguments);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        boolean matches = invocationMatcher.matches(actualInvocation);

        // Assert
        assertFalse(matches);
        Mockito.verify(invocation, Mockito.times(1)).getMock();
        Mockito.verify(invocation, Mockito.times(2)).getMethod();
        Mockito.verify(actualInvocation, Mockito.times(1)).getMock();
        Mockito.verify(actualInvocation, Mockito.times(2)).getMethod();
        Mockito.verify(actualInvocation, Mockito.times(1)).getArguments();
        Mockito.verifyNoMoreInteractions(invocation, actualInvocation);
        assertArrayEquals(arguments, actualArguments);
    }

    @Test
    public void testHasSimilarMethod_SameMethodButNotUnverified() {
        Invocation candidateInvocation = mock(Invocation.class);
        Mockito.when(candidateInvocation.isVerified()).thenReturn(true);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidateInvocation);

        // Assert
        assertFalse(hasSimilarMethod);
        Mockito.verify(candidateInvocation, Mockito.times(1)).isVerified();
        Mockito.verifyNoMoreInteractions(candidateInvocation);
    }

    @Test
    public void testHasSimilarMethod_DifferentMethodName() {
        Invocation candidateInvocation = mock(Invocation.class);
        Method method1 = mock(Method.class);
        Method method2 = mock(Method.class);

        Mockito.when(invocation.getMethod()).thenReturn(method1);
        Mockito.when(candidateInvocation.getMethod()).thenReturn(method2);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidateInvocation);

        // Assert
        assertFalse(hasSimilarMethod);
        Mockito.verify(invocation, Mockito.times(1)).getMethod();
        Mockito.verify(candidateInvocation, Mockito.times(1)).getMethod();
        Mockito.verifyNoMoreInteractions(invocation, candidateInvocation);
    }

    @Test
    public void testHasSimilarMethod_SameMethodAndMockButOverloadedAndSameArguments() {
        Invocation candidateInvocation = mock(Invocation.class);
        Object[] arguments = new Object[] { "arg1", 2, true };
        Object[] argumentsCopy = arguments.clone();

        Mockito.when(invocation.getMethod()).thenReturn(mock(Method.class));
        Mockito.when(candidateInvocation.getMethod()).thenReturn(invocation.getMethod());
        Mockito.when(invocation.isVerified()).thenReturn(false);
        Mockito.when(candidateInvocation.isVerified()).thenReturn(false);
        Mockito.when(invocation.getMock()).thenReturn(mock(Object.class));
        Mockito.when(candidateInvocation.getMock()).thenReturn(invocation.getMock());
        Mockito.when(candidateInvocation.getArguments()).thenReturn(arguments);

        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidateInvocation);

        // Assert
        assertFalse(hasSimilarMethod);
        Mockito.verify(invocation, Mockito.times(1)).getMethod();
        Mockito.verify(invocation, Mockito.times(1)).isVerified();
        Mockito.verify(invocation, Mockito.times(1)).getMock();
        Mockito.verify(candidateInvocation, Mockito.times(1)).getMethod();
        Mockito.verify(candidateInvocation, Mockito.times(1)).isVerified();
        Mockito.verify(candidateInvocation, Mockito.times(1)).getMock();
        Mockito.verify(candidateInvocation, Mockito.atLeast(1)).getArguments();
        Mockito.verifyNoMoreInteractions(invocation, candidateInvocation);
        assertArrayEquals(argumentsCopy, arguments);
    }
}
