import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.mock.MockCreationSettings;
import org.mockito.mock.MockName;
import org.mockito.stubbing.Answer;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private Invocation invocation;

    @Before
    public void setUp() {
        invocation = mock(Invocation.class);
        invocationMatcher = new InvocationMatcher(invocation);
    }

    @Test
    public void testConstructorWithMatchers_emptyMatchers() {
        List<Matcher> matchers = new ArrayList<>();
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    @Test
    public void testConstructorWithMatchers_nonEmptyMatchers() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(mock(Matcher.class));
        invocationMatcher = new InvocationMatcher(invocation, matchers);

        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testConstructorWithoutMatchers() {
        List<Matcher> expectedMatchers = ArgumentsProcessor.argumentsToMatchers(invocation.getArguments());
        invocationMatcher = new InvocationMatcher(invocation);

        assertEquals(expectedMatchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testGetMethod() {
        Method expectedMethod = invocation.getMethod();

        assertEquals(expectedMethod, invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> expectedMatchers = invocationMatcher.getMatchers();

        assertEquals(expectedMatchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        invocationMatcher.toString(); // Verify no exception is thrown
    }

    @Test
    public void testMatches() {
        Invocation actual = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getArguments()).thenReturn(mock(Object[].class));

        assertFalse(invocationMatcher.matches(actual));

        verify(invocation).getMock();
        verify(invocation).getMethod();
        verify(invocation).getArguments();
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] actualArgs = new Object[0];

        assertFalse(invocationMatcher.safelyArgumentsMatch(actualArgs));
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(mock(Object.class));
        when(candidate.getArguments()).thenReturn(mock(Object[].class));

        assertFalse(invocationMatcher.hasSimilarMethod(candidate));

        verify(invocation, times(2)).getMethod();
        verify(candidate).getMethod();
        verify(candidate).isVerified();
        verify(candidate).getMock();
        verify(candidate).getArguments();
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = mock(Invocation.class);
        when(candidate.getMethod()).thenReturn(mock(Method.class));

        assertFalse(invocationMatcher.hasSameMethod(candidate));

        verify(invocation, times(2)).getMethod();
        verify(candidate).getMethod();
    }

    @Test
    public void testGetLocation() {
        Location expectedLocation = mock(Location.class);
        when(invocation.getLocation()).thenReturn(expectedLocation);

        assertEquals(expectedLocation, invocationMatcher.getLocation());

        verify(invocation).getLocation();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFrom_VarArgs() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        Object[] rawArguments = new Object[]{};
        when(invocation.getMethod()).thenReturn(method);
        when(method.isVarArgs()).thenReturn(true);
        when(invocation.getRawArguments()).thenReturn(rawArguments);

        invocationMatcher.captureArgumentsFrom(invocation);
    }

    @Test
    public void testCaptureArgumentsFrom_NoVarArgs() {
        Invocation invocation = mock(Invocation.class);
        Method method = mock(Method.class);
        Object[] rawArguments = new Object[]{};
        MockCreationSettings<Object> mockSettings = mock(MockCreationSettings.class);
        MockName mockName = mock(MockName.class);
        Answer<Object> defaultAnswer = mock(Answer.class);
        when(invocation.getMethod()).thenReturn(method);
        when(method.isVarArgs()).thenReturn(false);
        when(invocation.getRawArguments()).thenReturn(rawArguments);
        when(invocation.getArgumentAt(anyInt(), eq(Object.class))).thenReturn(mock(Object.class));

        invocationMatcher.captureArgumentsFrom(invocation);

        verify(invocation).getMethod();
        verify(method).isVarArgs();
        verify(invocation).getRawArguments();
        verify(invocation).getArgumentAt(anyInt(), eq(Object.class));
    }
    
    @Test
    public void testCreateFrom() {
        LinkedList<InvocationMatcher> expectedInvocationMatchers = new LinkedList<>();
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(mock(Invocation.class));

        List<InvocationMatcher> actualInvocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(expectedInvocationMatchers, actualInvocationMatchers);
    }

}