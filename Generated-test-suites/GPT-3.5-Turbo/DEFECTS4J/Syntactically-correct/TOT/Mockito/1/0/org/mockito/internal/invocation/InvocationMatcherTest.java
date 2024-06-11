import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.mock.MockCreationSettings;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class InvocationMatcherTest {

    private InvocationMatcher invocationMatcher;
    private Invocation invocation;
    private Method method;
    private List<Matcher> matchers;

    @Before
    public void setUp() throws Exception {
        invocation = mock(Invocation.class);
        method = InvocationMatcher.class.getMethod("matches", Invocation.class);
        matchers = new ArrayList<>();
        invocationMatcher = new InvocationMatcher(invocation, matchers);
    }

    @Test
    public void testConstructor_withEmptyMatchersList_shouldCreateMatchersFromArguments() {
        // Arrange
        List<Object> arguments = new ArrayList<>();
        arguments.add("test");
        arguments.add(123);
        when(invocation.getArguments()).thenReturn(arguments);

        // Act
        InvocationMatcher invMatcher = new InvocationMatcher(invocation, Collections.emptyList());

        // Assert
        List<Matcher> matchers = invMatcher.getMatchers();
        assertEquals(arguments.size(), matchers.size());
        for (int i = 0; i < arguments.size(); i++) {
            assertTrue(matchers.get(i) instanceof ArgumentMatcher);
            assertEquals(arguments.get(i), ((ArgumentMatcher) matchers.get(i)).getExpected());
        }
    }

    @Test
    public void testConstructor_withNonEmptyMatchersList_shouldUseProvidedMatchers() {
        // Arrange
        ArgumentMatcher argMatcher = mock(ArgumentMatcher.class);
        matchers.add(argMatcher);

        // Act
        InvocationMatcher invMatcher = new InvocationMatcher(invocation, matchers);

        // Assert
        List<Matcher> responseMatchers = invMatcher.getMatchers();
        assertEquals(matchers.size(), responseMatchers.size());
        assertEquals(argMatcher, responseMatchers.get(0));
    }

    @Test
    public void testGetMethod_shouldReturnCorrectMethod() {
        // Arrange
        when(invocation.getMethod()).thenReturn(method);

        // Act
        Method responseMethod = invocationMatcher.getMethod();

        // Assert
        assertEquals(method, responseMethod);
    }

    @Test
    public void testGetInvocation_shouldReturnCorrectInvocation() {
        // Act
        Invocation responseInvocation = invocationMatcher.getInvocation();

        // Assert
        assertEquals(invocation, responseInvocation);
    }

    @Test
    public void testGetMatchers_shouldReturnCorrectMatchers() {
        // Act
        List<Matcher> responseMatchers = invocationMatcher.getMatchers();

        // Assert
        assertEquals(matchers, responseMatchers);
    }

    @Test
    public void testToString_shouldReturnCorrectString() {
        // Arrange
        PrintSettings printSettings = mock(PrintSettings.class);
        String expectedString = "Test String";
        when(printSettings.print(matchers, invocation)).thenReturn(expectedString);

        // Act
        String responseString = invocationMatcher.toString();

        // Assert
        assertEquals(expectedString, responseString);
    }

    @Test
    public void testMatches_withMatchingInvocation_shouldReturnTrue() {
        // Arrange
        Invocation actualInvocation = mock(Invocation.class);
        Object mock = mock(Object.class);
        when(invocation.getMock()).thenReturn(mock);
        when(actualInvocation.getMock()).thenReturn(mock);
        when(invocation.getMethod()).thenReturn(method);
        when(actualInvocation.getMethod()).thenReturn(method);
        when(invocation.getArguments()).thenReturn(new ArrayList<>());
        when(actualInvocation.getArguments()).thenReturn(new ArrayList<>());

        // Act
        boolean result = invocationMatcher.matches(actualInvocation);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testMatches_withNonMatchingInvocation_shouldReturnFalse() {
        // Arrange
        Invocation actualInvocation = mock(Invocation.class);
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        when(invocation.getMock()).thenReturn(mock1);
        when(actualInvocation.getMock()).thenReturn(mock2);
        when(invocation.getMethod()).thenReturn(method);
        when(actualInvocation.getMethod()).thenReturn(method);
        when(invocation.getArguments()).thenReturn(new ArrayList<>());
        when(actualInvocation.getArguments()).thenReturn(new ArrayList<>());

        // Act
        boolean result = invocationMatcher.matches(actualInvocation);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_withDifferentMethodName_shouldReturnFalse() {
        // Arrange
        Invocation candidate = mock(Invocation.class);
        Method candidateMethod = InvocationMatcher.class.getMethod("hasSimilarMethod", Invocation.class);
        when(candidate.getMethod()).thenReturn(candidateMethod);
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(invocation.getMock());

        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_withVerifiedCandidate_shouldReturnFalse() {
        // Arrange
        Invocation candidate = mock(Invocation.class);
        Method candidateMethod = InvocationMatcher.class.getMethod("hasSimilarMethod", Invocation.class);
        when(candidate.getMethod()).thenReturn(candidateMethod);
        when(candidate.isVerified()).thenReturn(true);
        when(candidate.getMock()).thenReturn(invocation.getMock());

        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_withDifferentMock_shouldReturnFalse() {
        // Arrange
        Invocation candidate = mock(Invocation.class);
        Method candidateMethod = InvocationMatcher.class.getMethod("hasSimilarMethod", Invocation.class);
        when(candidate.getMethod()).thenReturn(candidateMethod);
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(mock(Object.class));

        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_withOverloadedButSameArgs_shouldReturnFalse() {
        // Arrange
        Invocation candidate = mock(Invocation.class);
        Method candidateMethod = InvocationMatcher.class.getMethod("matches", Invocation.class);
        when(candidate.getMethod()).thenReturn(candidateMethod);
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(invocation.getMock());
        when(candidate.getArguments()).thenReturn(new ArrayList<>());

        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_withOverloadedButDifferentArgs_shouldReturnTrue() {
        // Arrange
        Invocation candidate = mock(Invocation.class);
        Method candidateMethod = InvocationMatcher.class.getMethod("hasSimilarMethod", Invocation.class);
        List<Object> arguments = new ArrayList<>();
        arguments.add("test");
        arguments.add(123);
        when(candidate.getMethod()).thenReturn(candidateMethod);
        when(candidate.isVerified()).thenReturn(false);
        when(candidate.getMock()).thenReturn(invocation.getMock());
        when(candidate.getArguments()).thenReturn(arguments);

        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testHasSameMethod_withMatchingMethods_shouldReturnTrue() {
        // Arrange
        Method m1 = InvocationMatcher.class.getMethod("hasSameMethod", Invocation.class);
        Method m2 = InvocationMatcher.class.getMethod("hasSameMethod", Invocation.class);
        when(invocation.getMethod()).thenReturn(m1);
        when(invocation.getMethod()).thenReturn(m2);

        // Act
        boolean result = invocationMatcher.hasSameMethod(invocation);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testHasSameMethod_withDifferentMethods_shouldReturnFalse() {
        // Arrange
        Method m1 = InvocationMatcher.class.getMethod("hasSameMethod", Invocation.class);
        Method m2 = InvocationMatcher.class.getMethod("matches", Invocation.class);
        when(invocation.getMethod()).thenReturn(m1);
        when(invocation.getMethod()).thenReturn(m2);

        // Act
        boolean result = invocationMatcher.hasSameMethod(invocation);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetLocation_shouldReturnCorrectLocation() {
        // Arrange
        Location location = mock(Location.class);
        when(invocation.getLocation()).thenReturn(location);

        // Act
        Location responseLocation = invocationMatcher.getLocation();

        // Assert
        assertEquals(location, responseLocation);
    }

    @Test
    public void testCaptureArgumentsFrom_withNonVarArgsMethod_shouldCaptureArguments() {
        // Arrange
        ArgumentMatcher argMatcher = mock(ArgumentMatcher.class);
        matchers.add(argMatcher);
        Object mock = mock(Object.class);
        List<Object> arguments = new ArrayList<>();
        arguments.add("test");
        when(invocation.getMock()).thenReturn(mock);
        when(invocation.getArgumentAt(0, Object.class)).thenReturn(arguments.get(0));
        when(invocation.getArguments()).thenReturn(arguments);
        when(argMatcher instanceof CapturesArguments).thenReturn(true);

        // Act
        invocationMatcher.captureArgumentsFrom(invocation);

        // Assert
        verify(argMatcher).captureFrom(arguments.get(0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentFrom_withVarArgsMethod_shouldThrowUnsupportedOperationException() {
        // Arrange
        Method varargsMethod = InvocationMatcher.class.getMethod("captureArgumentsFrom", Invocation.class);
        when(invocation.getMethod()).thenReturn(varargsMethod);
        when(invocation.getRawArguments()).thenReturn(new Object[]{"first", "second"});

        // Act
        invocationMatcher.captureArgumentsFrom(invocation);

        // Assert
        // Exception expected
    }

    @Test
    public void testCreateFrom_shouldCreateInvocationMatchersFromInvocations() {
        // Arrange
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(invocation);
        when(invocation.getMock()).thenReturn(mock(Object.class));

        // Act
        List<InvocationMatcher> responseMatchers = InvocationMatcher.createFrom(invocations);

        // Assert
        assertEquals(invocations.size(), responseMatchers.size());
        assertEquals(invocation.getMock(), responseMatchers.get(0).getInvocation().getMock());
    }
}