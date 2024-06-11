import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.reporting.PrintSettings;

public class InvocationMatcherTest {

    // Test for constructor with matchers argument
    @Test
    public void testInvocationMatcherWithMatchers() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Matcher matcher1 = mock(Matcher.class);
        Matcher matcher2 = mock(Matcher.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(matcher1);
        matchers.add(matcher2);

        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Assert
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }

    // Test for constructor without matchers argument
    @Test
    public void testInvocationMatcherWithoutMatchers() {
        // Arrange
        Invocation invocation = mock(Invocation.class);

        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Assert
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }

    // Test for getMethod
    @Test
    public void testGetMethod() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Method expectedMethod = mock(Method.class);
        when(invocation.getMethod()).thenReturn(expectedMethod);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        Method actualMethod = invocationMatcher.getMethod();

        // Assert
        assertEquals(expectedMethod, actualMethod);
    }

    // Test for toString
    @Test
    public void testToString() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(mock(Matcher.class));
        matchers.add(mock(Matcher.class));
        matchers.add(mock(Matcher.class));
        when(invocation.getArguments()).thenReturn(new Object[] {"arg1", "arg2", "arg3"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        String expectedString = "<InvocationMatcher> arg1, arg2, arg3";

        // Act
        String actualString = invocationMatcher.toString();

        // Assert
        assertEquals(expectedString, actualString);
    }

    // Test for matches
    @Test
    public void testMatches() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Invocation actualInvocation = mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getArguments()).thenReturn(new Object[] {"arg1", "arg2"});
        when(actualInvocation.getMock()).thenReturn(invocation.getMock());
        when(actualInvocation.getMethod()).thenReturn(invocation.getMethod());
        when(actualInvocation.getArguments()).thenReturn(new Object[] {"arg1", "arg2"});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        boolean actualResult = invocationMatcher.matches(actualInvocation);

        // Assert
        assertTrue(actualResult);
    }

    // Test for hasSimilarMethod with same method name, same mock, unverified
    @Test
    public void testHasSimilarMethodWithUnverified() {
        // Arrange
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation1.getMethod().getName()).thenReturn("method1");
        when(invocation1.isVerified()).thenReturn(false);
        when(invocation1.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod().getName()).thenReturn("method1");
        when(invocation2.isVerified()).thenReturn(false);
        when(invocation2.getMock()).thenReturn(invocation1.getMock());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        // Act
        boolean actualResult = invocationMatcher.hasSimilarMethod(invocation2);

        // Assert
        assertTrue(actualResult);
    }

    // Test for hasSimilarMethod with same method name, same mock, verified
    @Test
    public void testHasSimilarMethodWithVerified() {
        // Arrange
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation1.getMethod().getName()).thenReturn("method1");
        when(invocation1.isVerified()).thenReturn(false);
        when(invocation1.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod().getName()).thenReturn("method1");
        when(invocation2.isVerified()).thenReturn(true);
        when(invocation2.getMock()).thenReturn(invocation1.getMock());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        // Act
        boolean actualResult = invocationMatcher.hasSimilarMethod(invocation2);

        // Assert
        assertFalse(actualResult);
    }

    // Test for hasSimilarMethod with different method name
    @Test
    public void testHasSimilarMethodWithDifferentMethodName() {
        // Arrange
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation1.getMethod().getName()).thenReturn("method1");
        when(invocation1.isVerified()).thenReturn(false);
        when(invocation1.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod().getName()).thenReturn("method2");
        when(invocation2.isVerified()).thenReturn(false);
        when(invocation2.getMock()).thenReturn(invocation1.getMock());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        // Act
        boolean actualResult = invocationMatcher.hasSimilarMethod(invocation2);

        // Assert
        assertFalse(actualResult);
    }

    // Test for hasSimilarMethod with different mock
    @Test
    public void testHasSimilarMethodWithDifferentMock() {
        // Arrange
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation1.getMethod().getName()).thenReturn("method1");
        when(invocation1.isVerified()).thenReturn(false);
        when(invocation1.getMock()).thenReturn(mock(Object.class));
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod().getName()).thenReturn("method1");
        when(invocation2.isVerified()).thenReturn(false);
        when(invocation2.getMock()).thenReturn(mock(Object.class));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        // Act
        boolean actualResult = invocationMatcher.hasSimilarMethod(invocation2);

        // Assert
        assertFalse(actualResult);
    }

    // Test for hasSameMethod with same method name, same parameter types
    @Test
    public void testHasSameMethodWithSameMethodNameAndTypes() {
        // Arrange
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation1.getMethod().getName()).thenReturn("method1");
        when(invocation1.getMethod().getParameterTypes()).thenReturn(new Class[] {String.class, Integer.class});
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod().getName()).thenReturn("method1");
        when(invocation2.getMethod().getParameterTypes()).thenReturn(new Class[] {String.class, Integer.class});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        // Act
        boolean actualResult = invocationMatcher.hasSameMethod(invocation2);

        // Assert
        assertTrue(actualResult);
    }

    // Test for hasSameMethod with different method name
    @Test
    public void testHasSameMethodWithDifferentMethodName() {
        // Arrange
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation1.getMethod().getName()).thenReturn("method1");
        when(invocation1.getMethod().getParameterTypes()).thenReturn(new Class[] {String.class, Integer.class});
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod().getName()).thenReturn("method2");
        when(invocation2.getMethod().getParameterTypes()).thenReturn(new Class[] {String.class, Integer.class});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        // Act
        boolean actualResult = invocationMatcher.hasSameMethod(invocation2);

        // Assert
        assertFalse(actualResult);
    }

    // Test for hasSameMethod with different parameter types
    @Test
    public void testHasSameMethodWithDifferentParameterTypes() {
        // Arrange
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        when(invocation1.getMethod()).thenReturn(mock(Method.class));
        when(invocation1.getMethod().getName()).thenReturn("method1");
        when(invocation1.getMethod().getParameterTypes()).thenReturn(new Class[] {String.class, Integer.class});
        when(invocation2.getMethod()).thenReturn(mock(Method.class));
        when(invocation2.getMethod().getName()).thenReturn("method1");
        when(invocation2.getMethod().getParameterTypes()).thenReturn(new Class[] {Integer.class, String.class});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation1);

        // Act
        boolean actualResult = invocationMatcher.hasSameMethod(invocation2);

        // Assert
        assertFalse(actualResult);
    }

    // Test for getLocation
    @Test
    public void testGetLocation() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Location expectedLocation = mock(Location.class);
        when(invocation.getLocation()).thenReturn(expectedLocation);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);

        // Act
        Location actualLocation = invocationMatcher.getLocation();

        // Assert
        assertEquals(expectedLocation, actualLocation);
    }

    // Test for captureArgumentsFrom when non-varargs method
    @Test
    public void testCaptureArgumentsFromNonVarargsMethod() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Matcher matcher1 = mock(CapturesArguments.class);
        Matcher matcher2 = mock(Matcher.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(matcher1);
        matchers.add(matcher2);
        when(invocation.getArguments()).thenReturn(new Object[] {"arg1", "arg2"});
        when(matcher1 instanceof CapturesArguments).thenReturn(true);
        when(matcher2 instanceof CapturesArguments).thenReturn(false);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Act
        invocationMatcher.captureArgumentsFrom(invocation);

        // Assert
        verify((CapturesArguments) matcher1, times(1)).captureFrom("arg1");
        verify((CapturesArguments) matcher1, times(1)).captureFrom("arg2");
        verify((CapturesArguments) matcher2, never()).captureFrom(any());
    }

    // Test for captureArgumentsFrom when varargs method
    @Test(expected = UnsupportedOperationException.class)
    public void testCaptureArgumentsFromVarargsMethod() {
        // Arrange
        Invocation invocation = mock(Invocation.class);
        Matcher matcher = mock(CapturesArguments.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(matcher);
        when(invocation.getArguments()).thenReturn(new Object[] {"arg1", "arg2"});
        when(invocation.getMethod().isVarArgs()).thenReturn(true);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);

        // Act
        invocationMatcher.captureArgumentsFrom(invocation);

        // Assert
        verify((CapturesArguments) matcher, never()).captureFrom(any());
    }

    // Test for createFrom
    @Test
    public void testCreateFrom() {
        // Arrange
        Invocation invocation1 = mock(Invocation.class);
        Invocation invocation2 = mock(Invocation.class);
        List<Invocation> invocations = new ArrayList<>();
        invocations.add(invocation1);
        invocations.add(invocation2);

        // Act
        List<InvocationMatcher> invocationMatchers = InvocationMatcher.createFrom(invocations);

        // Assert
        assertEquals(2, invocationMatchers.size());
        assertEquals(invocation1, invocationMatchers.get(0).getInvocation());
        assertEquals(invocation2, invocationMatchers.get(1).getInvocation());
    }
}