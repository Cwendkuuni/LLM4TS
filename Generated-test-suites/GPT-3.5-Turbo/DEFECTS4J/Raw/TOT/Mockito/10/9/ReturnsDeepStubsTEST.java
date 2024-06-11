import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {
    
    @Test
    public void testAnswer_typicalUseCase_returnsMock() {
        // Arrange
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(MyClass.class));
        
        // Act
        Object result = answer.answer(invocation);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof MyClass);
    }
    
    @Test
    public void testAnswer_unmockableType_returnsDelegate() {
        // Arrange
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(UnmockableClass.class));
        
        // Act
        Object result = answer.answer(invocation);
        
        // Assert
        assertNotNull(result);
        assertTrue(result instanceof ReturnsEmptyValues);
    }
    
    @Test
    public void testAnswer_matchingInvocation_returnsStubbedAnswer() {
        // Arrange
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object mock = mock(Object.class);
        when(invocation.getMock()).thenReturn(mockitoMock);
        when(invocation.getMethod()).thenReturn(MyClass.class.getMethod("method"));
        
        // Act
        Object result = answer.answer(invocation);
        
        // Assert
        assertSame(mock, result);
    }
    
    @Test
    public void testAnswer_noMatchingInvocation_returnsDeepStubMock() {
        // Arrange
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object mock = mock(Object.class);
        when(invocation.getMock()).thenReturn(mockitoMock);
        when(invocation.getMethod()).thenReturn(MyClass.class.getMethod("method"));
        when(mockitoCore().isTypeMockable(Object.class)).thenReturn(true);
        when(container.getStubbedInvocations()).thenReturn(new ArrayList<>());
        
        // Act
        Object result = answer.answer(invocation);
        
        // Assert
        assertSame(mock, result);
    }
    
    @Test
    public void testNewDeepStubMock_validMetadata_returnsMock() {
        // Arrange
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        GenericMetadataSupport metadata = mock(GenericMetadataSupport.class);
        when(metadata.rawType()).thenReturn(MyClass.class);
        when(mockitoCore().mock(eq(MyClass.class), any())).thenReturn(mock(MyClass.class));

        // Act
        Object result = answer.newDeepStubMock(metadata);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof MyClass);
    }
    
    @Test
    public void testNewDeepStubMock_invalidMetadata_returnsNull() {
        // Arrange
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        GenericMetadataSupport metadata = mock(GenericMetadataSupport.class);
        when(metadata.rawType()).thenReturn(UnmockableClass.class);

        // Act
        Object result = answer.newDeepStubMock(metadata);

        // Assert
        assertNull(result);
    }
    
    @Test
    public void testWithSettingsUsing_rawExtraInterfaces_returnsMockSettingsWithExtraInterfaces() {
        // Arrange
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        GenericMetadataSupport metadata = mock(GenericMetadataSupport.class);
        when(metadata.hasRawExtraInterfaces()).thenReturn(true);
        Class[] extraInterfaces = new Class[]{ExtraInterface1.class, ExtraInterface2.class};
        when(metadata.rawExtraInterfaces()).thenReturn(extraInterfaces);

        // Act
        MockSettings mockSettings = answer.withSettingsUsing(metadata);

        // Assert
        assertNotNull(mockSettings);
        assertArrayEquals(extraInterfaces, mockSettings.getExtraInterfaces());
    }
    
    @Test
    public void testWithSettingsUsing_noRawExtraInterfaces_returnsMockSettings() {
        // Arrange
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        GenericMetadataSupport metadata = mock(GenericMetadataSupport.class);
        when(metadata.hasRawExtraInterfaces()).thenReturn(false);

        // Act
        MockSettings mockSettings = answer.withSettingsUsing(metadata);

        // Assert
        assertNotNull(mockSettings);
        assertNull(mockSettings.getExtraInterfaces());
    }
    
    @Test
    public void testReturnsDeepStubsAnswerUsing_validMetadata_returnsReturnsDeepStubsAnswer() {
        // Arrange
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        GenericMetadataSupport metadata = mock(GenericMetadataSupport.class);
        when(metadata.rawType()).thenReturn(MyClass.class);

        // Act
        ReturnsDeepStubs returnsDeepStubsAnswer = answer.returnsDeepStubsAnswerUsing(metadata);

        // Assert
        assertNotNull(returnsDeepStubsAnswer);
        assertSame(metadata, returnsDeepStubsAnswer.returnTypeGenericMetadata);
    }
    
    @Test
    public void testRecordDeepStubAnswer_validMock_returnsMock() {
        // Arrange
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(container.addAnswer(any(), anyBoolean())).thenReturn(0);
        
        // Act
        Object result = answer.recordDeepStubAnswer(mock, container);
        
        // Assert
        assertSame(mock, result);
        verify(container, times(1)).addAnswer(any(), anyBoolean());
    }
    
    @Test
    public void testActualParameterizedType_validMock_returnsGenericMetadata() {
        // Arrange
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(mockSettings.getTypeToMock()).thenReturn(MyClass.class);
        when(new MockUtil().getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        
        // Act
        GenericMetadataSupport result = answer.actualParameterizedType(mock);
        
        // Assert
        assertNotNull(result);
        assertEquals(Mockito.RETURNS_DEEP_STUBS, result);
    }
    
    @Test
    public void testReturnsDeepStubsSerializationFallback_validMetadata_returnsReturnsDeepStubsSerializationFallback() {
        // Arrange
        GenericMetadataSupport metadata = mock(GenericMetadataSupport.class);
        when(metadata.rawType()).thenReturn(MyClass.class);

        // Act
        ReturnsDeepStubsSerializationFallback result = new ReturnsDeepStubsSerializationFallback(metadata);

        // Assert
        assertNotNull(result);
        assertSame(metadata, result.returnTypeGenericMetadata);
    }
    
    @Test
    public void testDeeplyStubbedAnswer_validMock_returnsMock() {
        // Arrange
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        
        // Act
        DeeplyStubbedAnswer deeplyStubbedAnswer = answer.new DeeplyStubbedAnswer(mock);
        
        // Assert
        assertNotNull(deeplyStubbedAnswer);
        assertSame(mock, deeplyStubbedAnswer.mock);
    }
    
    @Test
    public void testMockitoCore_returnsMockitoCoreInstance() {
        // Act
        MockitoCore core = ReturnsDeepStubs.mockitoCore();
        
        // Assert
        assertNotNull(core);
        assertTrue(core instanceof MockitoCore);
    }
    
    @Test
    public void testDelegate_returnsReturnsEmptyValuesInstance() {
        // Act
        ReturnsEmptyValues delegate = ReturnsDeepStubs.delegate();
        
        // Assert
        assertNotNull(delegate);
        assertTrue(delegate instanceof ReturnsEmptyValues);
    }
    
}