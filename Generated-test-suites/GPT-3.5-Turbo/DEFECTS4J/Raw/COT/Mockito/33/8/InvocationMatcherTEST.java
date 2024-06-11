import org.junit.Test;
import static org.mockito.Mockito.*;

public class InvocationMatcherTest {

    @Test
    public void testGetMethod() {
        // Create Invocation object
        Invocation invocation = mock(Invocation.class);
        // Create InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, null);
        // Test getMethod() method
        Method method = invocation.getMethod();
        assertEquals(method, invocationMatcher.getMethod());
    }

    @Test
    public void testGetInvocation() {
        // Create Invocation object
        Invocation invocation = mock(Invocation.class);
        // Create InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, null);
        // Test getInvocation() method
        assertEquals(invocation, invocationMatcher.getInvocation());
    }

    @Test
    public void testGetMatchers() {
        // Create Invocation object
        Invocation invocation = mock(Invocation.class);
        // Create Matcher list
        List<Matcher> matchers = new ArrayList<Matcher>();
        // Create InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        // Test getMatchers() method
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    @Test
    public void testToString() {
        // Create Invocation object
        Invocation invocation = mock(Invocation.class);
        // Create InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, null);
        // Test toString() method
        String expected = invocation.toString(matchers, new PrintSettings());
        assertEquals(expected, invocationMatcher.toString());
    }

    @Test
    public void testMatches() {
        // Create Invocation objects
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        // Create Matcher list
        List<Matcher> matchers = new ArrayList<Matcher>();
        // Create InvocationMatcher objects
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher3 = new InvocationMatcher(invocation2, matchers);
        // Test matches() method
        assertTrue(invocationMatcher1.matches(invocation2));
        assertTrue(invocationMatcher2.matches(invocation1));
        assertFalse(invocationMatcher3.matches(invocation1));
    }

    @Test
    public void testHasSimilarMethod() {
        // Create Invocation objects
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        // Create Matcher list
        List<Matcher> matchers = new ArrayList<Matcher>();
        // Create InvocationMatcher objects
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher3 = new InvocationMatcher(invocation2, matchers);
        // Test hasSimilarMethod() method
        assertFalse(invocationMatcher1.hasSimilarMethod(invocation2));
        assertFalse(invocationMatcher2.hasSimilarMethod(invocation1));
        assertTrue(invocationMatcher3.hasSimilarMethod(invocation1));
    }

    @Test
    public void testHasSameMethod() {
        // Create Invocation objects
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        // Create Matcher list
        List<Matcher> matchers = new ArrayList<Matcher>();
        // Set the methods of the invocations
        when(invocation1.getMethod()).thenReturn(method1);
        when(invocation2.getMethod()).thenReturn(method2);
        // Create InvocationMatcher objects
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation1, matchers);
        InvocationMatcher invocationMatcher3 = new InvocationMatcher(invocation2, matchers);
        // Test hasSameMethod() method
        assertTrue(invocationMatcher1.hasSameMethod(invocation2));
        assertTrue(invocationMatcher2.hasSameMethod(invocation1));
        assertFalse(invocationMatcher3.hasSameMethod(invocation1));
    }

    @Test
    public void testGetLocation() {
        // Create Invocation object
        Invocation invocation = mock(Invocation.class);
        // Create InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, null);
        // Test getLocation() method
        Location location = invocation.getLocation();
        assertEquals(location, invocationMatcher.getLocation());
    }

    @Test
    public void testToStringWithPrintSettings() {
        // Create Invocation object
        Invocation invocation = mock(Invocation.class);
        // Create Matcher list
        List<Matcher> matchers = new ArrayList<Matcher>();
        // Create InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        // Create PrintSettings object
        PrintSettings printSettings = mock(PrintSettings.class);
        // Test toString() method with PrintSettings object
        String expected = invocation.toString(matchers, printSettings);
        assertEquals(expected, invocationMatcher.toString(printSettings));
    }

    @Test
    public void testCaptureArgumentsFrom() {
        // Create Invocation objects
        Invocation invocation = mock(Invocation.class);
        Invocation i = mock(Invocation.class);
        // Create Matcher list
        List<Matcher> matchers = new ArrayList<Matcher>();
        // Create InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        // Create CapturesArguments object
        CapturesArguments capturesArguments = mock(CapturesArguments.class);
        // Set the arguments of the Invocation object
        when(invocation.getArguments()).thenReturn(arguments);
        // Set the arguments of the i object
        when(i.getArguments()).thenReturn(args);
        // Set the matcher of the InvocationMatcher object
        matchers.add(capturesArguments);
        // Test captureArgumentsFrom() method
        invocationMatcher.captureArgumentsFrom(i);
        verify(capturesArguments, times(1)).captureFrom(args[0]);
    }

    @Test
    public void testCreateFrom() {
        // Create Invocation list
        List<Invocation> invocations = new ArrayList<Invocation>();
        // Create Invocation objects
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        // Add the Invocation objects to the list
        invocations.add(invocation1);
        invocations.add(invocation2);
        // Create InvocationMatcher list
        List<InvocationMatcher> expected = new ArrayList<InvocationMatcher>();
        // Add the InvocationMatcher objects to the list
        expected.add(new InvocationMatcher(invocation1));
        expected.add(new InvocationMatcher(invocation2));
        // Test createFrom() method
        List<InvocationMatcher> actual = InvocationMatcher.createFrom(invocations);
        assertEquals(expected, actual);
    }

}