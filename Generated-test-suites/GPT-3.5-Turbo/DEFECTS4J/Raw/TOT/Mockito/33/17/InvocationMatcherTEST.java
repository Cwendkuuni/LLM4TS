import static org.junit.Assert.*;
import org.junit.Test;

public class InvocationMatcherTest {

    // Test case for constructor with matchers
    @Test
    public void testConstructorWithMatchers() {
        // Create mock objects and method
        Object mock = new Object();
        Method method = mock.getClass().getMethod("methodName");

        // Create mock Invocation and Matchers
        Invocation invocation = new Invocation(mock, method);
        List<Matcher> matchers = new ArrayList<Matcher>();
        matchers.add(new Matcher());

        // Create InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Test that invocationMatcher has correct invocation and matchers
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    // Test case for constructor without matchers
    @Test
    public void testConstructorWithoutMatchers() {
        // Create mock object and method
        Object mock = new Object();
        Method method = mock.getClass().getMethod("methodName");

        // Create mock Invocation
        Invocation invocation = new Invocation(mock, method);

        // Create InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Test that invocationMatcher has correct invocation and empty matchers list
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(Collections.emptyList(), invocationMatcher.getMatchers());
    }

    // Test case for getMethod
    @Test
    public void testGetMethod() {
        // Create mock object and method
        Object mock = new Object();
        Method method = mock.getClass().getMethod("methodName");

        // Create mock Invocation
        Invocation invocation = new Invocation(mock, method);

        // Create InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Test that invocationMatcher returns correct method
        assertEquals(method, invocationMatcher.getMethod());
    }
    
    // Test case for toString
    @Test
    public void testToString() {
        // Create mock objects and method
        Object mock = new Object();
        Method method = mock.getClass().getMethod("methodName");

        // Create mock Invocation
        Invocation invocation = new Invocation(mock, method);

        // Create InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Test that toString returns correct string representation
        String expectedString = invocation.toString(invocationMatcher.getMatchers(), new PrintSettings());
        assertEquals(expectedString, invocationMatcher.toString());
    }
    
    // Test case for matches
    @Test
    public void testMatches() {
        // Create mock objects and methods
        Object mock1 = new Object();
        Method method1 = mock1.getClass().getMethod("methodName");
        Object mock2 = new Object();
        Method method2 = mock1.getClass().getMethod("methodName");

        // Create mock Invocations
        Invocation invocation1 = new Invocation(mock1, method1);
        Invocation invocation2 = new Invocation(mock2, method2);

        // Create InvocationMatcher objects
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // Test that matches returns true for the same invocation and false for different invocation
        assertTrue(invocationMatcher1.matches(invocation1));
        assertFalse(invocationMatcher1.matches(invocation2));
        assertFalse(invocationMatcher2.matches(invocation1));
        assertTrue(invocationMatcher2.matches(invocation2));
    }
    
    // Test case for hasSimilarMethod
    @Test
    public void testHasSimilarMethod() {
        // Create mock objects and methods
        Object mock1 = new Object();
        Method method1 = mock1.getClass().getMethod("methodName1");
        Method method2 = mock1.getClass().getMethod("methodName2");

        // Create mock Invocations
        Invocation invocation1 = new Invocation(mock1, method1);
        Invocation invocation2 = new Invocation(mock1, method1);
        Invocation invocation3 = new Invocation(mock1, method2);

        // Create InvocationMatcher objects
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);
        InvocationMatcher invocationMatcher3 = new InvocationMatcher(invocation3);

        // Test that hasSimilarMethod returns true for the same method invocation and false for different method invocation
        assertTrue(invocationMatcher1.hasSimilarMethod(invocation2));
        assertFalse(invocationMatcher1.hasSimilarMethod(invocation3));
        assertFalse(invocationMatcher2.hasSimilarMethod(invocation3));
    }
    
    // Test case for hasSameMethod
    @Test
    public void testHasSameMethod() {
        // Create mock objects and methods
        Object mock1 = new Object();
        Object mock2 = new Object();
        Method method1 = mock1.getClass().getMethod("methodName");
        Method method2 = mock2.getClass().getMethod("methodName");

        // Create mock Invocations
        Invocation invocation1 = new Invocation(mock1, method1);
        Invocation invocation2 = new Invocation(mock2, method2);

        // Create InvocationMatcher objects
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // Test that hasSameMethod returns true for the same method and false for different method
        assertTrue(invocationMatcher1.hasSameMethod(invocation1));
        assertFalse(invocationMatcher1.hasSameMethod(invocation2));
        assertFalse(invocationMatcher2.hasSameMethod(invocation1));
        assertTrue(invocationMatcher2.hasSameMethod(invocation2));
    }
    
    // Test case for getLocation
    @Test
    public void testGetLocation() {
        // Create mock objects and method
        Object mock = new Object();
        Method method = mock.getClass().getMethod("methodName");

        // Create mock Invocation
        Invocation invocation = new Invocation(mock, method);
        
        // Create mock Location
        Location location = new Location();

        // Set Location in Invocation
        invocation.setLocation(location);
        
        // Create InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Test that getLocation returns correct location
        assertEquals(location, invocationMatcher.getLocation());
    }
    
    // Test case for toString with printSettings
    @Test
    public void testToStringWithPrintSettings() {
        // Create mock objects and method
        Object mock = new Object();
        Method method = mock.getClass().getMethod("methodName");

        // Create mock Invocation
        Invocation invocation = new Invocation(mock, method);

        // Create InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Create PrintSettings object
        PrintSettings printSettings = new PrintSettings();

        // Test that toString with printSettings returns correct string representation
        String expectedString = invocation.toString(invocationMatcher.getMatchers(), printSettings);
        assertEquals(expectedString, invocationMatcher.toString(printSettings));
    }
    
    // Test case for captureArgumentsFrom
    @Test
    public void testCaptureArgumentsFrom() {
        // Create mock objects and methods
        Object mock1 = new Object();
        Method method1 = mock1.getClass().getMethod("methodName1");
        Object mock2 = new Object();
        Method method2 = mock1.getClass().getMethod("methodName2");

        // Create mock Invocations
        Invocation invocation1 = new Invocation(mock1, method1);
        Invocation invocation2 = new Invocation(mock2, method2);

        // Create mock Matchers
        Matcher matcher1 = new Matcher();
        Matcher matcher2 = new Matcher();

        // Create mock CapturesArguments
        CapturesArguments capturesArguments1 = new CapturesArguments();
        CapturesArguments capturesArguments2 = new CapturesArguments();

        // Set CapturesArguments in Matchers
        matcher1.setCapturedArguments(capturesArguments1);
        matcher2.setCapturedArguments(capturesArguments2);

        // Create InvocationMatcher objects
        InvocationMatcher invocationMatcher1 = new InvocationMatcher(invocation1);
        InvocationMatcher invocationMatcher2 = new InvocationMatcher(invocation2);

        // Create list of Matchers for invocationMatcher1
        List<Matcher> matchers1 = new ArrayList<Matcher>();
        matchers1.add(matcher1);
        matchers1.add(matcher2);

        // Set matchers1 in invocationMatcher1
        invocationMatcher1.setMatchers(matchers1);

        // Capture arguments from invocation2 using invocationMatcher1
        invocationMatcher1.captureArgumentsFrom(invocation2);

        // Test that capturedArguments1 and capturedArguments2 contain correct arguments
        assertEquals(invocation2.getArguments()[0], capturesArguments1.getCapturedArguments()[0]);
        assertEquals(invocation2.getArguments()[1], capturesArguments2.getCapturedArguments()[1]);
    }
    
    // Test case for createFrom
    @Test
    public void testCreateFrom() {
        // Create mock objects and methods
        Object mock1 = new Object();
        Method method1 = mock1.getClass().getMethod("methodName1");
        Object mock2 = new Object();
        Method method2 = mock1.getClass().getMethod("methodName2");

        // Create mock Invocations
        Invocation invocation1 = new Invocation(mock1, method1);
        Invocation invocation2 = new Invocation(mock2, method2);

        // Create list of Invocations
        List<Invocation> invocations = new ArrayList<Invocation>();
        invocations.add(invocation1);
        invocations.add(invocation2);

        // Create list of InvocationMatchers using createFrom
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        // Test that invocationMatchers have correct invocations
        assertEquals(invocation1, invocationMatchers.get(0).getInvocation());
        assertEquals(invocation2, invocationMatchers.get(1).getInvocation());
    }
}