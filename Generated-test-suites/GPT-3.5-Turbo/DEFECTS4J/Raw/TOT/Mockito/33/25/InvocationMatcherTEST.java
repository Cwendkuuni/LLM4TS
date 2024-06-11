import org.junit.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class InvocationMatcherTest {

    // Test initialization
    private Invocation invocation;
    private List<Matcher> matchers;

    @Test
    public void testInvocationMatcher_ConstructorWithMatchArgument() {
        // Setup
        invocation = Mockito.mock(Invocation.class);
        matchers = Arrays.asList(Mockito.mock(Matcher.class), Mockito.mock(Matcher.class));

        // Execute
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Verify
        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testInvocationMatcher_ConstructorWithoutMatchArgument() {
        // Setup
        invocation = Mockito.mock(Invocation.class);

        // Execute
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Verify
        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(invocation.argumentsToMatchers(), invocationMatcher.getMatchers());
    }

    @Test
    public void testInvocationMatcher_GetMethod() throws NoSuchMethodException {
        // Setup
        Method method = Invocation.class.getDeclaredMethod("getMethod");
        invocation = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Execute
        Method returnedMethod = invocationMatcher.getMethod();

        // Verify
        assertNotNull(returnedMethod);
        assertEquals(method, returnedMethod);
    }

    @Test
    public void testInvocationMatcher_Matches_ReturnsTrue() {
        // Create mock objects
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);
        Method method = Invocation.class.getDeclaredMethods()[0];
        Invocation invocation1 = new Invocation(mock1, method, null, null);
        Invocation invocation2 = new Invocation(mock1, method, null, null);

        // Setup
        invocation = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMock()).thenReturn(mock1);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(invocation.getArguments()).thenReturn(new Object[]{"arg1", "arg2"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        // Execute
        boolean matches = invocationMatcher.matches(invocation2);

        // Verify
        assertTrue(matches);
    }

    @Test
    public void testInvocationMatcher_Matches_ReturnsFalse_MockNotEqual() {
        // Create mock objects
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);
        Method method = Invocation.class.getDeclaredMethods()[0];
        Invocation invocation1 = new Invocation(mock1, method, null, null);
        Invocation invocation2 = new Invocation(mock2, method, null, null);

        // Setup
        invocation = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMock()).thenReturn(mock1);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(invocation.getArguments()).thenReturn(new Object[]{"arg1", "arg2"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        // Execute
        boolean matches = invocationMatcher.matches(invocation2);

        // Verify
        assertFalse(matches);
    }

    @Test
    public void testInvocationMatcher_Matches_ReturnsFalse_MethodNameNotEqual() {
        // Create mock objects
        Object mock1 = Mockito.mock(Object.class);
        Method method1 = Invocation.class.getDeclaredMethods()[0];
        Method method2 = Invocation.class.getDeclaredMethods()[1];
        Invocation invocation1 = new Invocation(mock1, method1, null, null);
        Invocation invocation2 = new Invocation(mock1, method2, null, null);

        // Setup
        invocation = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMock()).thenReturn(mock1);
        Mockito.when(invocation.getMethod()).thenReturn(method1);
        Mockito.when(invocation.getArguments()).thenReturn(new Object[]{"arg1", "arg2"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        // Execute
        boolean matches = invocationMatcher.matches(invocation2);

        // Verify
        assertFalse(matches);
    }

    @Test
    public void testInvocationMatcher_Matches_ReturnsFalse_ArgumentsNotEqual() {
        // Create mock objects
        Object mock1 = Mockito.mock(Object.class);
        Method method = Invocation.class.getDeclaredMethods()[0];
        Invocation invocation1 = new Invocation(mock1, method, null, null);
        Invocation invocation2 = new Invocation(mock1, method, null, null);

        // Setup
        invocation = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMock()).thenReturn(mock1);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(invocation.getArguments()).thenReturn(new Object[]{"arg1", "arg2"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        // Execute
        boolean matches = invocationMatcher.matches(invocation2);

        // Verify
        assertFalse(matches);
    }

    @Test
    public void testInvocationMatcher_HasSimilarMethod_ReturnsTrue() {
        // Create mock objects
        Object mock1 = Mockito.mock(Object.class);
        Method method = Invocation.class.getDeclaredMethods()[0];
        Invocation invocation1 = new Invocation(mock1, method, null, null);
        Invocation invocation2 = new Invocation(mock1, method, null, null);

        // Setup
        invocation = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMock()).thenReturn(mock1);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        // Execute
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(invocation2);

        // Verify
        assertTrue(hasSimilarMethod);
    }

    @Test
    public void testInvocationMatcher_HasSimilarMethod_ReturnsFalse_MethodNameNotEqual() {
        // Create mock objects
        Object mock1 = Mockito.mock(Object.class);
        Method method1 = Invocation.class.getDeclaredMethods()[0];
        Method method2 = Invocation.class.getDeclaredMethods()[1];
        Invocation invocation1 = new Invocation(mock1, method1, null, null);
        Invocation invocation2 = new Invocation(mock1, method2, null, null);

        // Setup
        invocation = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMock()).thenReturn(mock1);
        Mockito.when(invocation.getMethod()).thenReturn(method1);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        // Execute
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(invocation2);

        // Verify
        assertFalse(hasSimilarMethod);
    }

    @Test
    public void testInvocationMatcher_HasSimilarMethod_ReturnsFalse_MethodNameEqual_ArgumentsEqual() {
        // Create mock objects
        Object mock1 = Mockito.mock(Object.class);
        Method method = Invocation.class.getDeclaredMethods()[0];
        Invocation invocation1 = new Invocation(mock1, method, null, null);
        Invocation invocation2 = new Invocation(mock1, method, null, null);

        // Setup
        invocation = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMock()).thenReturn(mock1);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(invocation.getArguments()).thenReturn(new Object[]{"arg1", "arg2"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        // Execute
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(invocation2);

        // Verify
        assertFalse(hasSimilarMethod);
    }

    @Test
    public void testInvocationMatcher_HasSimilarMethod_ReturnsFalse_MethodNameEqual_ArgumentsNotEqual() {
        // Create mock objects
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);
        Method method = Invocation.class.getDeclaredMethods()[0];
        Invocation invocation1 = new Invocation(mock1, method, null, null);
        Invocation invocation2 = new Invocation(mock2, method, null, null);

        // Setup
        invocation = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMock()).thenReturn(mock1);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(invocation.getArguments()).thenReturn(new Object[]{"arg1", "arg2"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        // Execute
        boolean hasSimilarMethod = invocationMatcher.hasSimilarMethod(invocation2);

        // Verify
        assertFalse(hasSimilarMethod);
    }

    @Test
    public void testInvocationMatcher_HasSameMethod_ReturnsTrue() throws NoSuchMethodException {
        // Setup
        Method method1 = Invocation.class.getDeclaredMethod("getMethod");
        Method method2 = Invocation.class.getDeclaredMethod("getMethod");
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Execute
        boolean hasSameMethod = invocationMatcher.hasSameMethod(method1, method2);

        // Verify
        assertTrue(hasSameMethod);
    }

    @Test
    public void testInvocationMatcher_HasSameMethod_ReturnsFalse() throws NoSuchMethodException {
        // Set up
        Method method1 = Invocation.class.getDeclaredMethod("getMethod");
        Method method2 = InvocationMatcher.class.getDeclaredMethod("getMatchers");
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Execute
        boolean hasSameMethod = invocationMatcher.hasSameMethod(method1, method2);

        // Verify
        assertFalse(hasSameMethod);
    }

    @Test
    public void testInvocationMatcher_GetLocation() {
        // Set up
        Location location = Mockito.mock(Location.class);
        invocation = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getLocation()).thenReturn(location);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Execute
        Location returnedLocation = invocationMatcher.getLocation();

        // Verify
        assertNotNull(returnedLocation);
        assertEquals(location, returnedLocation);
    }

    @Test
    public void testInvocationMatcher_ToString() {
        // Set up
        Method method = Invocation.class.getDeclaredMethods()[0];
        invocation = Mockito.mock(Invocation.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Mockito.when(invocation.toString(matchers, new PrintSettings())).thenReturn("Invocation string");
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Execute
        String stringRepresentation = invocationMatcher.toString();

        // Verify
        assertNotNull(stringRepresentation);
        assertEquals("Invocation string", stringRepresentation);
    }

    @Test
    public void testInvocationMatcher_CaptureArgumentsFrom() {
        // Set up
        Matcher matcher1 = Mockito.mock(Matcher.class);
        Matcher matcher2 = Mockito.mock(Matcher.class);
        Invocation invocation = Mockito.mock(Invocation.class);
        Invocation i = Mockito.mock(Invocation.class);
        Object[] arguments = new Object[]{"arg1", "arg2"};
        Mockito.when(invocation.getArguments()).thenReturn(arguments);
        Mockito.when(invocation.getArguments()[0]).thenReturn("arg1");
        Mockito.when(matcher1 instanceof CapturesArguments).thenReturn(true);
        Mockito.when(matcher2 instanceof CapturesArguments).thenReturn(false);
        Mockito.when(matchers.get(0)).thenReturn(matcher1);
        Mockito.when(matchers.get(1)).thenReturn(matcher2);
        
        List<InvocationMatcher> invocations = new ArrayList<>();
        InvocationMatcher.invocations.add(invocation);

        // Execute
        invocationMatcher.captureArgumentsFrom(i);

        // Verify
        Mockito.verify((CapturesArguments) matcher1, Mockito.times(1)).captureFrom("arg1");
        Mockito.verify((CapturesArguments) matcher2, Mockito.times(0)).captureFrom(Mockito.any());
    }

    @Test
    public void testInvocationMatcher_CreateFrom() {
        // Set up
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
        List<Invocation> invocations = Arrays.asList(invocation1, invocation2);

        // Execute
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        // Verify
        assertNotNull(invocationMatchers);
        assertEquals(invocations.size(), invocationMatchers.size());
        assertEquals(new InvocationMatcher(invocation1), invocationMatchers.get(0));
        assertEquals(new InvocationMatcher(invocation2), invocationMatchers.get(1));
    }

}