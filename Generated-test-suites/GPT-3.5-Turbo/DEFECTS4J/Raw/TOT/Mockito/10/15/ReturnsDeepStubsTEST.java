import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer_NormalCase() throws Throwable {
        // Arrange
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));
        GenericMetadataSupport metadataSupport = new GenericMetadataSupport();
        metadataSupport.setRawType(Object.class);

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testAnswer_NonMockableRawType() throws Throwable {
        // Arrange
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));
        GenericMetadataSupport metadataSupport = new GenericMetadataSupport();
        metadataSupport.setRawType(IOException.class);

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testDeepStub_MatchingInvocation() throws Throwable {
        // Arrange
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));

        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(stubbedInvocationMatcher.getInvocation()).thenReturn(invocation);

        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(container.getInvocationForStubbing().matches(invocation)).thenReturn(true);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));

        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        when(handler.getInvocationContainer()).thenReturn(container);

        GenericMetadataSupport metadataSupport = new GenericMetadataSupport();
        metadataSupport.setRawType(Object.class);

        // Act
        Object result = returnsDeepStubs.deepStub(invocation, metadataSupport);

        // Assert
        assertNotNull(result);
        verify(stubbedInvocationMatcher, times(1)).answer(invocation);
    }

    @Test
    public void testDeepStub_NoMatchingInvocation() throws Throwable {
        // Arrange
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));

        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(stubbedInvocationMatcher.getInvocation()).thenReturn(invocation);

        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(container.getInvocationForStubbing().matches(invocation)).thenReturn(false);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));

        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        when(handler.getInvocationContainer()).thenReturn(container);

        GenericMetadataSupport metadataSupport = new GenericMetadataSupport();
        metadataSupport.setRawType(Object.class);

        // Act
        Object result = returnsDeepStubs.deepStub(invocation, metadataSupport);

        // Assert
        assertNotNull(result);
        verify(stubbedInvocationMatcher, never()).answer(invocation);
    }

    @Test
    public void testNewDeepStubMock_WithExtraInterfaces() {
        // Arrange
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport metadataSupport = new GenericMetadataSupport();
        metadataSupport.setRawType(Object.class);
        metadataSupport.setRawExtraInterfaces(new Class<?>[]{Serializable.class});

        // Act
        Object result = returnsDeepStubs.newDeepStubMock(metadataSupport);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Serializable);
    }

    @Test
    public void testNewDeepStubMock_WithoutExtraInterfaces() {
        // Arrange
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport metadataSupport = new GenericMetadataSupport();
        metadataSupport.setRawType(Object.class);
        metadataSupport.setRawExtraInterfaces(null);

        // Act
        Object result = returnsDeepStubs.newDeepStubMock(metadataSupport);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testWithSettingsUsing_WithExtraInterfaces() {
        // Arrange
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport metadataSupport = new GenericMetadataSupport();
        metadataSupport.setRawType(Object.class);
        metadataSupport.setRawExtraInterfaces(new Class<?>[]{Serializable.class});

        // Act
        MockSettings mockSettings = returnsDeepStubs.withSettingsUsing(metadataSupport);

        // Assert
        assertNotNull(mockSettings);
        assertTrue(mockSettings.getExtraInterfaces().contains(Serializable.class));
    }

    @Test
    public void testWithSettingsUsing_WithoutExtraInterfaces() {
        // Arrange
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport metadataSupport = new GenericMetadataSupport();
        metadataSupport.setRawType(Object.class);
        metadataSupport.setRawExtraInterfaces(null);

        // Act
        MockSettings mockSettings = returnsDeepStubs.withSettingsUsing(metadataSupport);

        // Assert
        assertNotNull(mockSettings);
        assertNull(mockSettings.getExtraInterfaces());
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        // Arrange
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport metadataSupport = new GenericMetadataSupport();
        metadataSupport.setRawType(Object.class);

        // Act
        ReturnsDeepStubs result = returnsDeepStubs.returnsDeepStubsAnswerUsing(metadataSupport);

        // Assert
        assertNotNull(result);
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
        assertNotNull(result);
        verify(container, times(1)).addAnswer(any(DeeplyStubbedAnswer.class), eq(false));
    }

    @Test
    public void testActualParameterizedType() {
        // Arrange
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(mockSettings.getTypeToMock()).thenReturn(Object.class);
        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockHandler(mock)).thenReturn(mock(InternalMockHandler.class));
        when(mockUtil.getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);

        // Act
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testReturnsDeepStubsSerializationFallback() {
        // Arrange
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport();
        ReturnsDeepStubsSerializationFallback returnsDeepStubs = new ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata);

        // Act
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock(Object.class));

        // Assert
        assertEquals(returnTypeGenericMetadata, result);
    }

    @Test
    public void testDeeplyStubbedAnswer() throws Throwable {
        // Arrange
        Object mock = mock(Object.class);
        DeeplyStubbedAnswer deeplyStubbedAnswer = new DeeplyStubbedAnswer(mock);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        // Act
        Object result = deeplyStubbedAnswer.answer(invocation);

        // Assert
        assertEquals(mock, result);
    }
}