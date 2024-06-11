import org.junit.Test;
import org.mockito.Mockito;

public class InvocationMatcherTest {

    @Test
    public void testConstructorWithMatchers() {
        // Arrange
        Invocation invocation = Mockito.mock(Invocation.class);
        Matcher matcher = Mockito.mock(Matcher.class);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(matcher);
        
        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        // Assert
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testConstructorWithoutMatchers() {
        // Arrange
        Invocation invocation = Mockito.mock(Invocation.class);
        
        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Assert
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertTrue(invocationMatcher.getMatchers().isEmpty());
    }
    
    @Test
    public void testGetMethod() {
        // Arrange
        Invocation invocation = Mockito.mock(Invocation.class);
        Method expectedMethod = invocation.getClass().getMethod("getMethod");
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Act
        Method actualMethod = invocationMatcher.getMethod();
        
        // Assert
        assertEquals(expectedMethod, actualMethod);
    }
    
    @Test
    public void testMatches_WithMatchingInvocation() {
        // Arrange
        Invocation invocation = Mockito.mock(Invocation.class);
        Invocation actualInvocation = Mockito.mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(actualInvocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(actualInvocation.getMethod()).thenReturn(mock(Method.class));
        ArgumentsComparator argumentsComparator = Mockito.mock(ArgumentsComparator.class);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        when(argumentsComparator.argumentsMatch(any(InvocationMatcher.class), any(Invocation.class))).thenReturn(true);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Act
        boolean result = invocationMatcher.matches(actualInvocation);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testMatches_WithNonMatchingInvocation() {
        // Arrange
        Invocation invocation = Mockito.mock(Invocation.class);
        Invocation nonMatchingInvocation = Mockito.mock(Invocation.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(nonMatchingInvocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(nonMatchingInvocation.getMethod()).thenReturn(mock(Method.class));
        ArgumentsComparator argumentsComparator = Mockito.mock(ArgumentsComparator.class);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        when(argumentsComparator.argumentsMatch(any(InvocationMatcher.class), any(Invocation.class))).thenReturn(false);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Act
        boolean result = invocationMatcher.matches(nonMatchingInvocation);
        
        // Assert
        assertFalse(result);
    }

    @Test
    public void testHasSimilarMethod_WithSimilarMethod() {
        // Arrange
        Invocation invocation = Mockito.mock(Invocation.class);
        Invocation similarInvocation = Mockito.mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(similarInvocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(similarInvocation.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(similarInvocation.getMock()).thenReturn(mock(Object.class));
        ArgumentsComparator argumentsComparator = Mockito.mock(ArgumentsComparator.class);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        when(argumentsComparator.argumentsMatch(any(InvocationMatcher.class), any(Object[].class))).thenReturn(true);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Act
        boolean result = invocationMatcher.hasSimilarMethod(similarInvocation);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testHasSimilarMethod_WithOverloadedButSameArgs() {
        // Arrange
        Invocation invocation = Mockito.mock(Invocation.class);
        Invocation overloadedInvocation = Mockito.mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(overloadedInvocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(overloadedInvocation.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(overloadedInvocation.getMock()).thenReturn(mock(Object.class));
        ArgumentsComparator argumentsComparator = Mockito.mock(ArgumentsComparator.class);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        when(argumentsComparator.argumentsMatch(any(InvocationMatcher.class), any(Object[].class))).thenReturn(true);
        when(argumentsComparator.argumentsMatch(any(InvocationMatcher.class), any(Invocation.class))).thenReturn(true);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Act
        boolean result = invocationMatcher.hasSimilarMethod(overloadedInvocation);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testHasSimilarMethod_WithNonSimilarMethod() {
        // Arrange
        Invocation invocation = Mockito.mock(Invocation.class);
        Invocation nonSimilarInvocation = Mockito.mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(nonSimilarInvocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.isVerified()).thenReturn(false);
        when(nonSimilarInvocation.isVerified()).thenReturn(false);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(nonSimilarInvocation.getMock()).thenReturn(mock(Object.class));
        ArgumentsComparator argumentsComparator = Mockito.mock(ArgumentsComparator.class);
        whenNew(ArgumentsComparator.class).withNoArguments().thenReturn(argumentsComparator);
        when(argumentsComparator.argumentsMatch(any(InvocationMatcher.class), any(Object[].class))).thenReturn(false);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Act
        boolean result = invocationMatcher.hasSimilarMethod(nonSimilarInvocation);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testHasSameMethod_WithEqualMethods() throws NoSuchMethodException {
        // Arrange
        Invocation invocation = Mockito.mock(Invocation.class);
        Method method = InvocationMatcher.class.getDeclaredMethod("hasSameMethod", Invocation.class);
        when(invocation.getMethod()).thenReturn(method);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Act
        boolean result = invocationMatcher.hasSameMethod(invocation);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testHasSameMethod_WithDifferentMethods() throws NoSuchMethodException {
        // Arrange
        Invocation invocation = Mockito.mock(Invocation.class);
        Invocation differentInvocation = Mockito.mock(Invocation.class);
        Method method = InvocationMatcher.class.getDeclaredMethod("hasSameMethod", Invocation.class);
        Method differentMethod = InvocationMatcher.class.getDeclaredMethod("toString");
        when(invocation.getMethod()).thenReturn(method);
        when(differentInvocation.getMethod()).thenReturn(differentMethod);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Act
        boolean result = invocationMatcher.hasSameMethod(differentInvocation);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testToString() {
        // Arrange
        Invocation invocation = Mockito.mock(Invocation.class);
        PrintSettings printSettings = Mockito.mock(PrintSettings.class);
        when(invocation.toString(anyList(), any(PrintSettings.class))).thenReturn("toString result");
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Act
        String result = invocationMatcher.toString(printSettings);
        
        // Assert
        assertEquals("toString result", result);
    }
    
    @Test
    public void testCaptureArgumentsFrom() {
        // Arrange
        Invocation invocation = Mockito.mock(Invocation.class);
        Invocation i = Mockito.mock(Invocation.class);
        Matcher matcher = Mockito.mock(Matcher.class);
        CapturesArguments capturesArguments = Mockito.mock(CapturesArguments.class);
        when(i.getArguments()).thenReturn(new Object[] { "arg1", "arg2" });
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(matcher);
        matchers.add(capturesArguments);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        // Act
        invocationMatcher.captureArgumentsFrom(i);
        
        // Assert
        verify(capturesArguments, times(1)).captureFrom("arg1");
        verify(capturesArguments, times(1)).captureFrom("arg2");
    }
    
    @Test
    public void testCreateFrom() {
        // Arrange
        Invocation invocation1 = Mockito.mock(Invocation.class);
        Invocation invocation2 = Mockito.mock(Invocation.class);
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