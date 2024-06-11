
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {
    
    private ReturnsDeepStubs returnsDeepStubs;
    
    @Before
    public void setUp() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }
    
    @Test
    public void answer_shouldReturnDelegateValueWhenRawTypeNotMockable() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        
        // Act
        Object result = returnsDeepStubs.answer(invocation);
        
        // Assert
        assertTrue(result instanceof DelegateValue);
    }
    
    @Test
    public void answer_shouldReturnMockWhenMatchingInvocationForVerificationFound() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InvocationsContainer container = mock(InvocationsContainer.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        
        // Act
        Object result = returnsDeepStubs.answer(invocation);
        
        // Assert
        assertTrue(result instanceof MockedObject);
    }
    
    @Test
    public void answer_shouldReturnMockWhenNoMatchingInvocationForVerificationFound() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InvocationsContainer container = mock(InvocationsContainer.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(container.getStubbedInvocations()).thenReturn(Collections.emptyList());
        
        // Act
        Object result = returnsDeepStubs.answer(invocation);
        
        // Assert
        assertTrue(result instanceof MockedObject);
    }
    
    @Test
    public void getMock_shouldReturnMockWithAnswerWhenNoMatchingInvocationForVerificationFound() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InvocationsContainer container = mock(InvocationsContainer.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.addAnswer(any(), anyBoolean())).thenReturn(true);
        
        // Act
        Object result = returnsDeepStubs.getMock(invocation);
        
        // Assert
        assertTrue(result instanceof MockedObject);
    }
    
    @Test
    public void recordDeepStubMock_shouldReturnMockWithAnswer() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InvocationsContainer container = mock(InvocationsContainer.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMock()).thenReturn(mock(Object.class));
        
        // Act
        Object result = returnsDeepStubs.recordDeepStubMock(invocation, container);
        
        // Assert
        assertTrue(result instanceof MockedObject);
    }
    
    @Test
    public void actualParameterizedType_shouldReturnGenericMetadataSupportObject() {
        // Arrange
        Object mock = mock(Object.class);
        
        // Act
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);
        
        // Assert
        assertNotNull(result);
    }
}
