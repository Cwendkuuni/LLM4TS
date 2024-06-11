import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {
    
    private ReturnsDeepStubs returnsDeepStubs;

    @Test
    public void testAnswer_validInvocation_returnsMock() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Act
        Object result = returnsDeepStubs.answer(invocation);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Object);
    }
    
    @Test
    public void testAnswer_nonMockableReturnType_returnsDelegateValue() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));
        when(invocation.getMock()).thenReturn(mock(Object.class));
        
        // Act
        Object result = returnsDeepStubs.answer(invocation);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof String);
        assertEquals("", result);
    }
    
    @Test
    public void testGetMock_matchingInvocation_returnsStubbedAnswer() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        
        // Act
        Object result = returnsDeepStubs.getMock(invocation, returnTypeGenericMetadata);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Object);
    }
    
    @Test
    public void testGetMock_nonMatchingInvocation_returnsDeepStub() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getInvocationForStubbing()).thenReturn(null);
        
        // Act
        Object result = returnsDeepStubs.getMock(invocation, returnTypeGenericMetadata);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Object);
    }
    
    @Test
    public void testCreateNewDeepStubMock_validReturnType_returnsMock() throws Throwable {
        // Arrange
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        
        // Act
        Object result = returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Object);
    }
    
    @Test
    public void testWithSettingsUsing_extraInterfacesExist_returnsMockSettingsWithExtraInterfaces() throws Throwable {
        // Arrange
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class<?>[] {Serializable.class});
        
        // Act
        MockSettings mockSettings = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);
        
        // Assert
        assertNotNull(mockSettings);
        assertTrue(mockSettings.getExtraInterfaces().contains(Serializable.class));
    }
    
    @Test
    public void testWithSettingsUsing_noExtraInterfacesExist_returnsMockSettings() throws Throwable {
        // Arrange
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class<?>[0]);
        
        // Act
        MockSettings mockSettings = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);
        
        // Assert
        assertNotNull(mockSettings);
        assertNull(mockSettings.getExtraInterfaces());
    }
    
    @Test
    public void testReturnsDeepStubsAnswerUsing_validReturnType_returnsReturnsDeepStubs() throws Throwable {
        // Arrange
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        
        // Act
        ReturnsDeepStubs answer = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
        
        // Assert
        assertNotNull(answer);
    }
    
    @Test
    public void testRecordDeepStubMock_validMock_returnsMock() throws Throwable {
        // Arrange
        Object mock = mock(Object.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        
        // Act
        Object result = returnsDeepStubs.recordDeepStubMock(mock, container);
        
        // Assert
        assertNotNull(result);
        assertSame(mock, result);
    }
    
    @Test
    public void testActualParameterizedType_validMock_returnsGenericMetadataSupport() {
        // Arrange
        Object mock = mock(Object.class);
        
        // Act
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof GenericMetadataSupport);
    }
}