
import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.matchers.CapturesArguments;
import org.mockito.internal.reporting.PrintSettings;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.internal.invocation.InvocationBuilder.anInvocation;

public class InvocationMatcherTest {

    @Test
    public void invocationMatcher_ConstructorWithInvocationAndMatchers_SetsInvocationAndMatchersCorrectly() {
        // Create a mockInvocation object
        Invocation mockInvocation = mock(Invocation.class);
        // Create a list of mockMatchers
        List<CapturesArguments> mockMatchers = Arrays.asList(mock(CapturesArguments.class), mock(CapturesArguments.class));
        // Create an InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, mockMatchers);
        
        // Check that the invocationMatcher object has the correct invocation and matchers
        assertThat(invocationMatcher.getInvocation(), is(mockInvocation));
        assertThat(invocationMatcher.getMatchers(), is(mockMatchers));
    }
    
    @Test
    public void invocationMatcher_ConstructorWithInvocationOnly_SetsInvocationAndMatchersCorrectly() {
        // Create a mockInvocation object
        Invocation mockInvocation = mock(Invocation.class);
        // Create an InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);
        
        // Check that the invocationMatcher object has the correct invocation and empty matchers
        assertThat(invocationMatcher.getInvocation(), is(mockInvocation));
        assertThat(invocationMatcher.getMatchers(), is(Collections.emptyList()));
    }
    
    @Test
    public void invocationMatcher_GetMethod_ReturnsMethod() {
        // Create a mockInvocation object
        Invocation mockInvocation = mock(Invocation.class);
        // Create a Method object
        Method mockMethod = mock(Method.class);
        // Define behavior for getMethod() method of mockInvocation
        when(mockInvocation.getMethod()).thenReturn(mockMethod);
        // Create an InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);
        
        // Check that the getMethod() method returns the correct method
        assertThat(invocationMatcher.getMethod(), is(mockMethod));
    }
    
    @Test
    public void invocationMatcher_ToString_ReturnsInvocationStringRepresentation() {
        // Create a mockInvocation object
        Invocation mockInvocation = mock(Invocation.class);
        // Create a PrintSettings object
        PrintSettings mockPrintSettings = mock(PrintSettings.class);
        // Define behavior for toString() method of mockInvocation
        when(mockInvocation.toString(anyList(), any(PrintSettings.class))).thenReturn("InvocationString");
        // Create an InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);
        
        // Check that the toString() method returns the correct string representation of the invocation
        assertThat(invocationMatcher.toString(), is("InvocationString"));
    }
    
    @Test
    public void invocationMatcher_Matches_WithMatchingInvocation_ReturnsTrue() {
        // Create a mockInvocation object
        Invocation mockInvocation = mock(Invocation.class);
        // Create a mockArgumentsComparator object
        ArgumentsComparator mockArgumentsComparator = mock(ArgumentsComparator.class);
        // Create an InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);
        // Create an actualInvocation object
        Invocation actualInvocation = anInvocation().withMethod("methodName").build();
        // Define behavior for matches() method of mockArgumentsComparator
        when(mockArgumentsComparator.argumentsMatch(invocationMatcher, actualInvocation)).thenReturn(true);
        
        // Check that the matches() method returns true when the invocation matches the actual invocation
        assertThat(invocationMatcher.matches(actualInvocation), is(true));
    }
    
    @Test
    public void invocationMatcher_Matches_WithNonMatchingInvocation_ReturnsFalse() {
        // Create a mockInvocation object
        Invocation mockInvocation = mock(Invocation.class);
        // Create a mockArgumentsComparator object
        ArgumentsComparator mockArgumentsComparator = mock(ArgumentsComparator.class);
        // Create an InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);
        // Create an actualInvocation object
        Invocation actualInvocation = anInvocation().withMethod("methodName").build();
        // Define behavior for matches() method of mockArgumentsComparator
        when(mockArgumentsComparator.argumentsMatch(invocationMatcher, actualInvocation)).thenReturn(false);
        
        // Check that the matches() method returns false when the invocation does not match the actual invocation
        assertThat(invocationMatcher.matches(actualInvocation), is(false));
    }
    
    @Test
    public void invocationMatcher_HasSimilarMethod_WithNonMatchingMethodName_ReturnsFalse() {
        // Create a mockInvocation object
        Invocation mockInvocation = mock(Invocation.class);
        // Create an InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);
        // Create a candidateInvocation object with a different method name
        Invocation candidateInvocation = anInvocation().withMethod("differentMethodName").build();
        
        // Check that the hasSimilarMethod() method returns false when the method names do not match
        assertThat(invocationMatcher.hasSimilarMethod(candidateInvocation), is(false));
    }
    
    @Test
    public void invocationMatcher_HasSimilarMethod_WithVerifiedInvocation_ReturnsFalse() {
        // Create a mockInvocation object
        Invocation mockInvocation = mock(Invocation.class);
        // Create an InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);
        // Create a candidateInvocation object that is verified
        Invocation candidateInvocation = anInvocation().verified().build();
        
        // Check that the hasSimilarMethod() method returns false when the candidate invocation is verified
        assertThat(invocationMatcher.hasSimilarMethod(candidateInvocation), is(false));
    }
    
    @Test
    public void invocationMatcher_HasSimilarMethod_WithDifferentMock_ReturnsFalse() {
        // Create a mockInvocation object
        Invocation mockInvocation = mock(Invocation.class);
        // Create another mockInvocation object
        Invocation anotherMockInvocation = mock(Invocation.class);
        // Create an InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);
        // Create a candidateInvocation object with a different mock
        Invocation candidateInvocation = anInvocation().onMock(anotherMockInvocation).build();
        
        // Check that the hasSimilarMethod() method returns false when the mocks do not match
        assertThat(invocationMatcher.hasSimilarMethod(candidateInvocation), is(false));
    }
    
    @Test
    public void invocationMatcher_HasSimilarMethod_WithOverloadedButDifferentArgs_ReturnsFalse() {
        // Create a mockInvocation object
        Invocation mockInvocation = mock(Invocation.class);
        // Create an ArgumentsComparator object
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        // Create an InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);
        // Create a candidateInvocation object with overloaded but different arguments
        Invocation candidateInvocation = anInvocation().withArguments(1).build();
        // Define behavior for safelyArgumentsMatch() method of invocationMatcher
        when(argumentsComparator.argumentsMatch(invocationMatcher, candidateInvocation.getArguments())).thenReturn(false);
        
        // Check that the hasSimilarMethod() method returns false when the arguments do not match
        assertThat(invocationMatcher.hasSimilarMethod(candidateInvocation), is(false));
    }
    
    @Test
    public void invocationMatcher_HasSimilarMethod_WithOverloadedAndSameArgs_ReturnsTrue() {
        // Create a mockInvocation object
        Invocation mockInvocation = mock(Invocation.class);
        // Create an ArgumentsComparator object
        ArgumentsComparator argumentsComparator = mock(ArgumentsComparator.class);
        // Create an InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);
        // Create a candidateInvocation object with overloaded but same arguments
        Invocation candidateInvocation = anInvocation().withArguments(1).build();
        // Define behavior for safelyArgumentsMatch() method of invocationMatcher
        when(argumentsComparator.argumentsMatch(invocationMatcher, candidateInvocation.getArguments())).thenReturn(true);
        
        // Check that the hasSimilarMethod() method returns true when the arguments match
        assertThat(invocationMatcher.hasSimilarMethod(candidateInvocation), is(true));
    }
    
    @Test
    public void invocationMatcher_HasSameMethod_WithSameMethod_ReturnsTrue() {
        // Create a mockInvocation object
        Invocation mockInvocation = mock(Invocation.class);
        // Create a Method object
        Method mockMethod = mock(Method.class);
        // Define behavior for getMethod() method of mockInvocation
        when(mockInvocation.getMethod()).thenReturn(mockMethod);
        // Create an InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);
        // Create a candidateInvocation object with the same method
        Invocation candidateInvocation = anInvocation().withMethod("methodName").build();
        
        // Check that the hasSameMethod() method returns true when the methods match
        assertThat(invocationMatcher.hasSameMethod(candidateInvocation), is(true));
    }
    
    @Test
    public void invocationMatcher_HasSameMethod_WithDifferentMethod_ReturnsFalse() {
        // Create a mockInvocation object
        Invocation mockInvocation = mock(Invocation.class);
        // Create a Method object
        Method mockMethod = mock(Method.class);
        // Define behavior for getMethod() method of mockInvocation
        when(mockInvocation.getMethod()).thenReturn(mockMethod);
        // Create an InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);
        // Create a candidateInvocation object with a different method
        Invocation candidateInvocation = anInvocation().withMethod("differentMethodName").build();
        
        // Check that the hasSameMethod() method returns false when the methods do not match
        assertThat(invocationMatcher.hasSameMethod(candidateInvocation), is(false));
    }
    
    @Test
    public void invocationMatcher_GetLocation_ReturnsLocation() {
        // Create a mockInvocation object
        Invocation mockInvocation = mock(Invocation.class);
        // Create a Location object
        Location mockLocation = mock(Location.class);
        // Create an InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);
        // Define behavior for getLocation() method of mockInvocation
        when(mockInvocation.getLocation()).thenReturn(mockLocation);
        
        // Check that the getLocation() method returns the correct location
        assertThat(invocationMatcher.getLocation(), is(mockLocation));
    }
    
    @Test
    public void invocationMatcher_ToStringWithPrintSettings_ReturnsInvocationStringRepresentationWithPrintSettings() {
        // Create a mockInvocation object
        Invocation mockInvocation = mock(Invocation.class);
        // Create a PrintSettings object
        PrintSettings mockPrintSettings = mock(PrintSettings.class);
        // Create an InvocationMatcher object
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation);
        // Define behavior for toString() method of mockInvocation
        when(mockInvocation.toString(anyList(), any(PrintSettings.class))).thenReturn("InvocationString");
        
        // Check that the toString(PrintSettings) method returns the correct string representation of the invocation with print settings
        assertThat(invocationMatcher.toString(mockPrintSettings), is("InvocationString"));
    }
    
    @Test
    public void invocationMatcher_CaptureArgumentsFrom_CapturesArgumentsFromInvocation() {
        // Create a mockInvocation object
        Invocation mockInvocation = mock(Invocation.class);
        // Create a matcher object that captures arguments
        CapturesArguments mockMatcher1 = mock(CapturesArguments.class);
        CapturesArguments mockMatcher2 = mock(CapturesArguments.class);
        // Create an InvocationMatcher object with matchers that capture arguments
        InvocationMatcher invocationMatcher = new InvocationMatcher(mockInvocation, Arrays.asList(mockMatcher1, mockMatcher2));
        // Create an invocation object with arguments
        Invocation i = anInvocation().withArguments(1, 2, 3).build();
        
        // Call the captureArgumentsFrom() method
        invocationMatcher.captureArgumentsFrom(i);
        
        // Verify that the arguments are captured by the matchers
        assertThat(mockMatcher1.capturedArg(), is(1));
        assertThat(mockMatcher2.capturedArg(), is(2));
    }
}
