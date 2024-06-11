import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import org.mockito.invocation.Invocation;
import org.mockito.invocation.Location;
import org.hamcrest.Matcher;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class InvocationMatcherTest {

    private Invocation invocation;
    private InvocationMatcher invocationMatcher;
    private List<Matcher> matchers;

    @Before
    public void setup() {
        invocation = mock(Invocation.class);
        matchers = new ArrayList<>();
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testConstructorWithMatchers() {
        List<Matcher> mockMatchers = mock(List.class);
        invocationMatcher = new InvocationMatcher(invocation, mockMatchers);

        verify(mockMatchers).isEmpty();
    }

    @Test
    public void testConstructorWithEmptyMatchers() {
        List<Matcher> emptyMatchers = new ArrayList<>();

        invocationMatcher = new InvocationMatcher(invocation, emptyMatchers);
        List<Matcher> argumentsMatchers = invocationMatcher.getMatchers();

        verify(invocation, times(1)).getArguments();
        assertNotEquals(emptyMatchers, argumentsMatchers);
    }

    @Test
    public void testGetInvocation() {
        Invocation actualInvocation = invocationMatcher.getInvocation();

        assertEquals(invocation, actualInvocation);
    }

    @Test
    public void testGetMethod() {
        Method method = mock(Method.class);

        when(invocation.getMethod()).thenReturn(method);

        Method actualMethod = invocationMatcher.getMethod();

        assertEquals(method, actualMethod);
    }

    @Test
    public void testGetMatchers() {
        List<Matcher> actualMatchers = invocationMatcher.getMatchers();

        assertEquals(matchers, actualMatchers);
    }

    @Test
    public void testToString() {
        PrintSettings printSettingsMock = mock(PrintSettings.class);
        String expectedOutput = "Expected Output";

        when(printSettingsMock.print(matchers, invocation)).thenReturn(expectedOutput);

        invocationMatcher.toString();

        verify(printSettingsMock).print(matchers, invocation);
    }

    @Test
    public void testMatches() {
        Invocation actualInvocation = mock(Invocation.class);

        when(invocation.getMock()).thenReturn("Mock");
        when(actualInvocation.getMock()).thenReturn("Mock");
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actualInvocation.getMethod()).thenReturn(mock(Method.class));

        boolean matches = invocationMatcher.matches(actualInvocation);

        assertTrue(matches);
    }

    @Test
    public void testSafelyArgumentsMatch() {
        Object[] actualArgs = new Object[0];

        boolean argumentsMatch = invocationMatcher.safelyArgumentsMatch(actualArgs);

        assertFalse(argumentsMatch);
    }

    @Test
    public void testHasSimilarMethod() {
        Invocation candidate = mock(Invocation.class);

        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(candidate.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(candidate.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod().getName()).thenReturn("method1");
        when(candidate.getMethod().getName()).thenReturn("method1");
        when(invocation.equals(candidate)).thenReturn(true);

        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(candidate);

        assertTrue(hasSimilarMethod);
    }

    @Test
    public void testHasSameMethod() {
        Invocation candidate = mock(Invocation.class);

        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(candidate.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().getName()).thenReturn("method1");
        when(candidate.getMethod().getName()).thenReturn("method1");

        boolean hasSameMethod = invocationMatcher.hasSameMethod(candidate);

        assertTrue(hasSameMethod);
    }

    @Test
    public void testGetLocation() {
        Location location = mock(Location.class);

        when(invocation.getLocation()).thenReturn(location);

        Location actualLocation = invocationMatcher.getLocation();

        assertEquals(location, actualLocation);
    }

    @Test
    public void testCaptureArgumentsFrom() {
        Invocation invocation = mock(Invocation.class);

        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().isVarArgs()).thenReturn(true);
        when(invocation.getArguments()).thenReturn(new Object[0]);
        when(invocation.getRawArguments()).thenReturn(new Object[0]);

        invocationMatcher.captureArgumentsFrom(invocation);

        verify(invocation, times(2)).getMethod();
        verify(invocation, times(2)).getRawArguments();
        verify(invocation, times(2)).getArguments();
        verify(matchers, times(2)).get(anyInt());
    }

    @Test
    public void testCreateFrom() {
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(mock(Invocation.class));

        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        assertEquals(invocations.size(), invocationMatchers.size());
    }
}