import org.junit.Test;
import org.mockito.internal.invocation.InvocationBuilder;
import org.mockito.invocation.InvocationOnMock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer_InvocationMatchesStubbedInvocation_ReturnsAnswerFromStubbedInvocation() throws Throwable {
        // Arrange
        ReturnsDeepStubs stubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = new InvocationBuilder().toInvocation();
        
        // create a stubbed invocation with matching invocation
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        when(stubbedInvocation.getInvocation()).thenReturn(invocation);
        when(stubbedInvocation.answer(invocation)).thenReturn("answer");
        
        // set up the container with the stubbed invocation
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocation));
        when(handler.getInvocationContainer()).thenReturn(container);
        when(handler.getInvocationForStubbing()).thenReturn(invocation);
        when(handler.getInvocationContainer()).thenReturn(container);
        
        // Act
        Object result = stubs.answer(invocation);
        
        // Assert
        assertEquals("answer", result);
    }
    
    @Test
    public void testAnswer_InvocationDoesNotMatchStubbedInvocation_CreatesAndReturnsDeepStubMock() throws Throwable {
        // Arrange
        ReturnsDeepStubs stubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = new InvocationBuilder().toInvocation();
        
        // create a stubbed invocation with non-matching invocation
        StubbedInvocationMatcher stubbedInvocation = mock(StubbedInvocationMatcher.class);
        when(stubbedInvocation.getInvocation()).thenReturn(mock(InvocationOnMock.class));
        when(stubbedInvocation.answer(invocation)).thenReturn("answer");
        
        // set up the container with the stubbed invocation
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocation));
        when(handler.getInvocationContainer()).thenReturn(container);
        when(handler.getInvocationForStubbing()).thenReturn(invocation);
        when(handler.getInvocationContainer()).thenReturn(container);
        
        // Act
        Object result = stubs.answer(invocation);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof ReturnsDeepStubs);
    }
}