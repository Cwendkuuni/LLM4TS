import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setup() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void answer_ValidInvocation_ReturnsExpectedValue() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof String);
        assertEquals("mockObject", result);
    }

    @Test(expected = Throwable.class)
    public void answer_InvalidInvocation_ThrowsThrowable() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("hashCode"));

        // Act
        returnsDeepStubs.answer(invocation);
    }

    @Test
    public void deepStub_ValidInvocation_ReturnsExpectedValue() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));

        InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
        when(mockHandler.getInvocationContainer()).thenReturn(mock(InvocationContainerImpl.class));
        when(mockHandler.getInvocationContainer().getStubbedInvocations()).thenReturn(Collections.emptyList());

        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockHandler(any())).thenReturn(mockHandler);
        doReturn(mock(Object.class)).when(mockitoCore()).mock(any(Class.class), any(MockSettings.class));

        GenericMetadataSupport genericMetadata = mock(GenericMetadataSupport.class);
        when(genericMetadata.rawType()).thenReturn(Object.class);
        when(actualParameterizedType(any())).thenReturn(genericMetadata);

        // Act
        Object result = returnsDeepStubs.deepStub(invocation, genericMetadata);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof String);
        assertEquals("mockObject", result);
    }

    @Test
    public void deepStub_MatchingStubbedInvocation_ReturnsExpectedValue() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));

        InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
        when(mockHandler.getInvocationContainer()).thenReturn(mock(InvocationContainerImpl.class));

        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(stubbedInvocationMatcher.getInvocation()).thenReturn(invocation);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));

        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockHandler(any())).thenReturn(mockHandler);
        doReturn(mock(Object.class)).when(mockitoCore()).mock(any(Class.class), any(MockSettings.class));

        GenericMetadataSupport genericMetadata = mock(GenericMetadataSupport.class);
        when(genericMetadata.rawType()).thenReturn(Object.class);
        when(actualParameterizedType(any())).thenReturn(genericMetadata);

        // Act
        Object result = returnsDeepStubs.deepStub(invocation, genericMetadata);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof String);
        assertEquals("mockObject", result);
    }

    @Test
    public void deepStub_NoMatchingStubbedInvocation_ReturnsExpectedMock() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));

        InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
        when(mockHandler.getInvocationContainer()).thenReturn(mock(InvocationContainerImpl.class));

        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(stubbedInvocationMatcher.getInvocation()).thenReturn(mock(Invocation.class));
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));

        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockHandler(any())).thenReturn(mockHandler);
        doReturn(mock(Object.class)).when(mockitoCore()).mock(any(Class.class), any(MockSettings.class));

        GenericMetadataSupport genericMetadata = mock(GenericMetadataSupport.class);
        when(genericMetadata.rawType()).thenReturn(Object.class);
        when(actualParameterizedType(any())).thenReturn(genericMetadata);

        // Act
        Object result = returnsDeepStubs.deepStub(invocation, genericMetadata);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Object);
        assertNotEquals(mock(invocation.getMock()), result);
    }

    @Test
    public void newDeepStubMock_ValidMetadata_ReturnsExpectedMock() {
        // Arrange
        GenericMetadataSupport genericMetadata = mock(GenericMetadataSupport.class);
        when(genericMetadata.rawType()).thenReturn(Object.class);

        when(mockitoCore().mock(any(Class.class), any(MockSettings.class)))
                .thenReturn(mock(Object.class));

        // Act
        Object result = returnsDeepStubs.newDeepStubMock(genericMetadata);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Object);
    }

    @Test
    public void withSettingsUsing_HasRawExtraInterfaces_ReturnsExpectedMockSettings() {
        // Arrange
        GenericMetadataSupport genericMetadata = mock(GenericMetadataSupport.class);
        when(genericMetadata.hasRawExtraInterfaces()).thenReturn(true);
        when(genericMetadata.rawExtraInterfaces()).thenReturn(new Class<?>[]{Comparable.class});

        // Act
        MockSettings result = returnsDeepStubs.withSettingsUsing(genericMetadata);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof MockSettings);
        assertTrue(result.getExtraInterfaces().contains(Comparable.class));
        assertTrue(result.getDefaultAnswer() instanceof ReturnsDeepStubs);
    }

    @Test
    public void withSettingsUsing_NoRawExtraInterfaces_ReturnsExpectedMockSettings() {
        // Arrange
        GenericMetadataSupport genericMetadata = mock(GenericMetadataSupport.class);
        when(genericMetadata.hasRawExtraInterfaces()).thenReturn(false);

        // Act
        MockSettings result = returnsDeepStubs.withSettingsUsing(genericMetadata);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof MockSettings);
        assertNull(result.getExtraInterfaces());
        assertTrue(result.getDefaultAnswer() instanceof ReturnsDeepStubs);
    }

    @Test
    public void returnsDeepStubsAnswerUsing_ValidMetadata_ReturnsExpectedAnswer() {
        // Arrange
        GenericMetadataSupport genericMetadata = mock(GenericMetadataSupport.class);
        when(genericMetadata.rawType()).thenReturn(Object.class);

        // Act
        ReturnsDeepStubs result = returnsDeepStubs.returnsDeepStubsAnswerUsing(genericMetadata);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof ReturnsDeepStubsSerializationFallback);
        assertSame(genericMetadata, ((ReturnsDeepStubsSerializationFallback) result).returnTypeGenericMetadata);
    }

    @Test
    public void recordDeepStubAnswer_ValidMock_AddsAnswerToContainer() throws Throwable {
        // Arrange
        Object mock = mock(Object.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        // Act
        returnsDeepStubs.recordDeepStubAnswer(mock, container);

        // Assert
        verify(container, times(1)).addAnswer(any(DeeplyStubbedAnswer.class), anyBoolean());
    }

    @Test
    public void actualParameterizedType_ValidObject_ReturnsExpectedMetadata() {
        // Arrange
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(new MockUtil().getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);

        GenericMetadataSupport genericMetadata = mock(GenericMetadataSupport.class);
        when(GenericMetadataSupport.inferFrom(mockSettings.getTypeToMock())).thenReturn(genericMetadata);

        // Act
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);

        // Assert
        assertNotNull(result);
        assertSame(genericMetadata, result);
    }
}