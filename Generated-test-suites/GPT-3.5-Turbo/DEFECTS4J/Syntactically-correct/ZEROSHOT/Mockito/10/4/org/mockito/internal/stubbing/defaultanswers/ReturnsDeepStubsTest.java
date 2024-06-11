import org.junit.Test;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.io.Serializable;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        // Arrange
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        
        // Act
        Object result = returnsDeepStubs.answer(invocation);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void testDeepStub() throws Throwable {
        // Arrange
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getInvocationForStubbing()).thenReturn(mock(InvocationOnMock.class));
        
        // Act
        Object result = returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void testNewDeepStubMock() {
        // Arrange
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        
        // Act
        Object result = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);
        
        // Assert
        assertNotNull(result);
    }

    @Test
    public void testWithSettingsUsing() {
        // Arrange
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        
        when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(true);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class<?>[] {Serializable.class});
        
        // Act
        MockSettings mockSettings = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);
        
        // Assert
        assertNotNull(mockSettings);
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        // Arrange
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        
        // Act
        ReturnsDeepStubs deepStubsAnswer = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
        
        // Assert
        assertNotNull(deepStubsAnswer);
    }

    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        // Arrange
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        
        // Act
        Object result = returnsDeepStubs.recordDeepStubAnswer(mock, container);
        
        // Assert
        assertEquals(mock, result);
    }

    @Test
    public void testActualParameterizedType() {
        // Arrange
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        
        when(new MockUtil().getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(Object.class);
        
        // Act
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);
        
        // Assert
        assertEquals(returnTypeGenericMetadata, result);
    }
}