import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.mock.MockCreationSettings;

public class InvocationMatcherTest {
    
    @Test
    public void testConstructor_invocationAndMatchers_initialized() {
        // Arrange
        Invocation invocation = new Invocation();
        List<Matcher> matchers = new ArrayList<>();
        
        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation, matchers);
        
        // Assert
        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(matchers, invocationMatcher.getMatchers());
    }
    
    @Test
    public void testConstructor_invocation_initializedWithoutMatchers() {
        // Arrange
        Invocation invocation = new Invocation();
        
        // Act
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Assert
        assertNotNull(invocationMatcher);
        assertEquals(invocation, invocationMatcher.getInvocation());
        assertEquals(invocation.argumentsToMatchers(), invocationMatcher.getMatchers());
    }
    
    @Test
    public void testGetMethod_returnsMethod() {
        // Arrange
        Invocation invocation = new Invocation();
        Method expectedMethod = new Method();
        invocation.setMethod(expectedMethod);
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Act
        Method actualMethod = invocationMatcher.getMethod();
        
        // Assert
        assertEquals(expectedMethod, actualMethod);
    }
    
    @Test
    public void testMatches_sameInvocation_returnsTrue() {
        // Arrange
        Invocation invocation = new Invocation();
        invocation.setMock("mock");
        invocation.setMethod(new Method());
        invocation.setArguments(new Object[] {1, 2, 3});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Act
        boolean result = invocationMatcher.matches(invocation);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testMatches_differentMock_returnsFalse() {
        // Arrange
        Invocation invocation = new Invocation();
        invocation.setMock("mock1");
        invocation.setMethod(new Method());
        invocation.setArguments(new Object[] {1, 2, 3});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Invocation actualInvocation = new Invocation();
        actualInvocation.setMock("mock2");
        actualInvocation.setMethod(new Method());
        actualInvocation.setArguments(new Object[] {1, 2, 3});
        
        // Act
        boolean result = invocationMatcher.matches(actualInvocation);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testMatches_differentMethod_returnsFalse() {
        // Arrange
        Invocation invocation = new Invocation();
        invocation.setMock("mock");
        invocation.setMethod(new Method("method1"));
        invocation.setArguments(new Object[] {1, 2, 3});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Invocation actualInvocation = new Invocation();
        actualInvocation.setMock("mock");
        actualInvocation.setMethod(new Method("method2"));
        actualInvocation.setArguments(new Object[] {1, 2, 3});
        
        // Act
        boolean result = invocationMatcher.matches(actualInvocation);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testMatches_differentArguments_returnsFalse() {
        // Arrange
        Invocation invocation = new Invocation();
        invocation.setMock("mock");
        invocation.setMethod(new Method());
        invocation.setArguments(new Object[] {1, 2, 3});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Invocation actualInvocation = new Invocation();
        actualInvocation.setMock("mock");
        actualInvocation.setMethod(new Method());
        actualInvocation.setArguments(new Object[] {4, 5, 6});
        
        // Act
        boolean result = invocationMatcher.matches(actualInvocation);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testHasSimilarMethod_sameMethod_sameMock_notVerified_returnsTrue() {
        // Arrange
        Invocation invocation = new Invocation();
        invocation.setMethod(new Method());
        invocation.setVerified(false);
        invocation.setMock("mock");
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Invocation candidate = new Invocation();
        candidate.setMethod(new Method());
        candidate.setVerified(false);
        candidate.setMock("mock");
        
        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testHasSimilarMethod_sameMethod_sameMock_verified_returnsFalse() {
        // Arrange
        Invocation invocation = new Invocation();
        invocation.setMethod(new Method());
        invocation.setVerified(true);
        invocation.setMock("mock");
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Invocation candidate = new Invocation();
        candidate.setMethod(new Method());
        candidate.setVerified(false);
        candidate.setMock("mock");
        
        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testHasSimilarMethod_differentMethod_sameMock_returnsFalse() {
        // Arrange
        Invocation invocation = new Invocation();
        invocation.setMethod(new Method("method1"));
        invocation.setVerified(false);
        invocation.setMock("mock");
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Invocation candidate = new Invocation();
        candidate.setMethod(new Method("method2"));
        candidate.setVerified(false);
        candidate.setMock("mock");
        
        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testHasSimilarMethod_sameMethod_differentMock_returnsFalse() {
        // Arrange
        Invocation invocation = new Invocation();
        invocation.setMethod(new Method());
        invocation.setVerified(false);
        invocation.setMock("mock1");
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Invocation candidate = new Invocation();
        candidate.setMethod(new Method());
        candidate.setVerified(false);
        candidate.setMock("mock2");
        
        // Act
        boolean result = invocationMatcher.hasSimilarMethod(candidate);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testHasSameMethod_sameMethod_returnsTrue() {
        // Arrange
        Invocation invocation = new Invocation();
        invocation.setMethod(new Method());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Invocation candidate = new Invocation();
        candidate.setMethod(new Method());
        
        // Act
        boolean result = invocationMatcher.hasSameMethod(candidate);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testHasSameMethod_differentMethod_returnsFalse() {
        // Arrange
        Invocation invocation = new Invocation();
        invocation.setMethod(new Method("method1"));
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        Invocation candidate = new Invocation();
        candidate.setMethod(new Method("method2"));
        
        // Act
        boolean result = invocationMatcher.hasSameMethod(candidate);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testToString_returnsInvocationString() {
        // Arrange
        Invocation invocation = new Invocation();
        invocation.setMethod(new Method());
        invocation.setArguments(new Object[] {1, 2, 3});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        
        // Act
        String result = invocationMatcher.toString();
        
        // Assert
        assertEquals(invocation.toString(), result);
    }
    
    @Test
    public void testToString_withPrintSettings_returnsInvocationStringWithPrintSettings() {
        // Arrange
        Invocation invocation = new Invocation();
        invocation.setMethod(new Method());
        invocation.setArguments(new Object[] {1, 2, 3});
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        PrintSettings printSettings = new PrintSettings();
        
        // Act
        String result = invocationMatcher.toString(printSettings);
        
        // Assert
        assertEquals(invocation.toString(invocationMatcher.getMatchers(), printSettings), result);
    }
    
    @Test
    public void testCaptureArgumentsFrom_withCapturesArgumentsMatchers_capturesArguments() {
        // Arrange
        Invocation invocation = new Invocation();
        invocation.setMethod(new Method());
        InvocationMatcher invocationMatcher = new InvocationMatcher(invocation);
        Object[] arguments = new Object[] {1, 2, 3};
        
        ArgumentMatchers.CapturesArgumentsMatcher<Integer> argumentMatcher = new ArgumentMatchers.CapturesArgumentsMatcher<>();
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(argumentMatcher);
        
        InvocationMatcher argumentInvocationMatcher = new InvocationMatcher(invocation, matchers);
        
        // Act
        argumentInvocationMatcher.captureArgumentsFrom(invocationMatcher);
        
        // Assert
        assertArrayEquals(arguments, argumentMatcher.getCapturedArguments().toArray());
    }
}