import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setUp() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswer_withValidInvocation_returnsMockObject() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Object);
        verifyZeroInteractions(invocation);
    }

    @Test(expected = Throwable.class)
    public void testAnswer_withInvalidInvocation_throwsException() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        doThrow(new Throwable()).when(invocation).getMock();

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        assertNull(result);
        verify(invocation).getMock();
    }

    @Test
    public void testDeepStub_withMatchingInvocation_returnsAnswer() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(invocation.getMock()).thenReturn(returnTypeGenericMetadata);

        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing().matches(invocation)).thenReturn(true);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn("Answer");

        // Act
        Object result = returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata);

        // Assert
        assertNotNull(result);
        assertEquals("Answer", result);
        verify(handler).getInvocationContainer();
        verify(container).getStubbedInvocations();
        verify(container).getInvocationForStubbing();
        verify(stubbedInvocationMatcher).answer(invocation);
    }

    @Test
    public void testDeepStub_withNonMatchingInvocation_returnsRecordedDeepStubAnswer() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(invocation.getMock()).thenReturn(returnTypeGenericMetadata);

        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(mock(StubbedInvocationMatcher.class)));
        when(container.getInvocationForStubbing().matches(invocation)).thenReturn(false);

        // Act
        Object result = returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata);

        // Assert
        assertNotNull(result);
        verify(handler).getInvocationContainer();
        verify(container).getStubbedInvocations();
        verify(container).getInvocationForStubbing();
    }

    @Test
    public void testNewDeepStubMock_withGenericMetadata_returnsMockObject() {
        // Arrange
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);

        // Act
        Object result = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Object);
    }

    @Test
    public void testWithSettingsUsing_withRawExtraInterfaces_returnsMockSettings() {
        // Arrange
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(true);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class[]{Serializable.class});

        // Act
        MockSettings mockSettings = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);

        // Assert
        assertNotNull(mockSettings);
        assertTrue(mockSettings.isSerializable());
        assertTrue(Arrays.deepEquals(new Class[]{Serializable.class}, mockSettings.getExtraInterfaces()));
    }

    @Test
    public void testWithSettingsUsing_withoutRawExtraInterfaces_returnsMockSettings() {
        // Arrange
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(false);

        // Act
        MockSettings mockSettings = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);

        // Assert
        assertNotNull(mockSettings);
        assertTrue(mockSettings.isSerializable());
        assertNull(mockSettings.getExtraInterfaces());
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing_withGenericMetadata_returnsReturnsDeepStubsAnswer() {
        // Arrange
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        // Act
        ReturnsDeepStubs result = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);

        // Assert
        assertNotNull(result);
        assertEquals(ReturnsDeepStubsSerializationFallback.class, result.getClass());
        assertSame(returnTypeGenericMetadata, ((ReturnsDeepStubsSerializationFallback) result).returnTypeGenericMetadata);
    }

    @Test
    public void testRecordDeepStubAnswer_withMockAndContainer_returnsMockObject() throws Throwable {
        // Arrange
        Object mock = mock(Object.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        // Act
        Object result = returnsDeepStubs.recordDeepStubAnswer(mock, container);

        // Assert
        assertNotNull(result);
        assertSame(mock, result);
        verify(container).addAnswer(any(DeeplyStubbedAnswer.class), eq(false));
    }

    @Test
    public void testActualParameterizedType_withMockSettings_returnsGenericMetadataSupport() {
        // Arrange
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(mockSettings.getTypeToMock()).thenReturn(mock);
        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockHandler(mock)).thenReturn(mock(InternalMockHandler.class));
        returnsDeepStubs = spy(returnsDeepStubs);
        doReturn(mockUtil).when(returnsDeepStubs).getMockUtil();

        // Act
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testReturnsDeepStubsSerializationFallback_withGenericMetadata_returnsReturnsDeepStubsSerializationFallback() {
        // Arrange
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        // Act
        ReturnsDeepStubsSerializationFallback result = new ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata);

        // Assert
        assertNotNull(result);
        assertEquals(ReturnsDeepStubsSerializationFallback.class, result.getClass());
        assertSame(returnTypeGenericMetadata, result.returnTypeGenericMetadata);
    }

    @Test
    public void testReturnsDeepStubsSerializationFallback_writeReplace_returnsMockitoReturnsDeepStubs() throws IOException {
        // Arrange
        ReturnsDeepStubsSerializationFallback serializationFallback = new ReturnsDeepStubsSerializationFallback(mock(GenericMetadataSupport.class));

        // Act
        Object result = serializationFallback.writeReplace();

        // Assert
        assertNotNull(result);
        assertEquals(Mockito.RETURNS_DEEP_STUBS, result);
    }

    @Test
    public void testDeeplyStubbedAnswer_withMock_returnsMockObject() {
        // Arrange
        Object mock = mock(Object.class);
        DeeplyStubbedAnswer deeplyStubbedAnswer = new DeeplyStubbedAnswer(mock);

        // Act
        Object result = deeplyStubbedAnswer.answer(mock(InvocationOnMock.class));

        // Assert
        assertNotNull(result);
        assertSame(mock, result);
    }

    private class DeeplyStubbedAnswerTest extends ReturnsDeepStubs.DeeplyStubbedAnswer {
        public DeeplyStubbedAnswerTest(Object mock) {
            super(mock);
        }
    }

}