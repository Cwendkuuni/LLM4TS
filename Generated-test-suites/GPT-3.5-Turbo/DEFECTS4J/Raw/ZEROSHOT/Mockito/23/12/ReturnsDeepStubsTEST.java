import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setup() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswer_invocationMatches_ReturnsStubbedInvocation() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        MockitoCore mockitoCore = mock(MockitoCore.class);
        ReturnsEmptyValues delegate = mock(ReturnsEmptyValues.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);

        when(invocation.getMock()).thenReturn(returnTypeGenericMetadata);
        when(returnTypeGenericMetadata.resolveGenericReturnType(invocation.getMethod())).thenReturn(returnTypeGenericMetadata);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(mockitoCore.isTypeMockable(Object.class)).thenReturn(true);
        when(delegate.returnValueFor(Object.class)).thenReturn(null);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(stubbedInvocationMatcher.getInvocation()).thenReturn(invocation);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn(null);

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        assertNull(result);
        verify(invocation, times(1)).getMock();
        verify(returnTypeGenericMetadata, times(2)).resolveGenericReturnType(invocation.getMethod());
        verify(returnTypeGenericMetadata, times(1)).rawType();
        verify(mockitoCore, times(1)).isTypeMockable(Object.class);
        verify(delegate, times(1)).returnValueFor(Object.class);
        verify(handler, times(1)).getInvocationContainer();
        verify(container, times(1)).getStubbedInvocations();
        verify(container, times(1)).getInvocationForStubbing();
        verify(stubbedInvocationMatcher, times(1)).getInvocation();
        verify(stubbedInvocationMatcher, times(1)).answer(invocation);
    }

    @Test
    public void testAnswer_invocationDoesNotMatch_ReturnsDeepStubMock() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        MockitoCore mockitoCore = mock(MockitoCore.class);
        ReturnsEmptyValues delegate = mock(ReturnsEmptyValues.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        when(invocation.getMock()).thenReturn(returnTypeGenericMetadata);
        when(returnTypeGenericMetadata.resolveGenericReturnType(invocation.getMethod())).thenReturn(returnTypeGenericMetadata);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(mockitoCore.isTypeMockable(Object.class)).thenReturn(true);
        when(handler.getInvocationContainer()).thenReturn(container);

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        assertNotNull(result);
        verify(invocation, times(1)).getMock();
        verify(returnTypeGenericMetadata, times(2)).resolveGenericReturnType(invocation.getMethod());
        verify(returnTypeGenericMetadata, times(1)).rawType();
        verify(mockitoCore, times(1)).isTypeMockable(Object.class);
        verify(handler, times(1)).getInvocationContainer();
    }

    @Test
    public void testGetMock_invocationMatches_ReturnsStubbedInvocation() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        MockitoCore mockitoCore = mock(MockitoCore.class);
        ReturnsEmptyValues delegate = mock(ReturnsEmptyValues.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);

        when(invocation.getMock()).thenReturn(returnTypeGenericMetadata);
        when(returnTypeGenericMetadata.resolveGenericReturnType(invocation.getMethod())).thenReturn(returnTypeGenericMetadata);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(mockitoCore.isTypeMockable(Object.class)).thenReturn(true);
        when(delegate.returnValueFor(Object.class)).thenReturn(null);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(stubbedInvocationMatcher.getInvocation()).thenReturn(invocation);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn(null);

        // Act
        Object result = returnsDeepStubs.getMock(invocation, returnTypeGenericMetadata);

        // Assert
        assertNull(result);
        verify(invocation, times(1)).getMock();
        verify(returnTypeGenericMetadata, times(2)).resolveGenericReturnType(invocation.getMethod());
        verify(returnTypeGenericMetadata, times(1)).rawType();
        verify(mockitoCore, times(1)).isTypeMockable(Object.class);
        verify(delegate, times(1)).returnValueFor(Object.class);
        verify(handler, times(1)).getInvocationContainer();
        verify(container, times(1)).getStubbedInvocations();
        verify(container, times(1)).getInvocationForStubbing();
        verify(stubbedInvocationMatcher, times(1)).getInvocation();
        verify(stubbedInvocationMatcher, times(1)).answer(invocation);
    }

    @Test
    public void testGetMock_invocationDoesNotMatch_ReturnsDeepStubMock() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        MockitoCore mockitoCore = mock(MockitoCore.class);
        ReturnsEmptyValues delegate = mock(ReturnsEmptyValues.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        when(invocation.getMock()).thenReturn(returnTypeGenericMetadata);
        when(returnTypeGenericMetadata.resolveGenericReturnType(invocation.getMethod())).thenReturn(returnTypeGenericMetadata);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(mockitoCore.isTypeMockable(Object.class)).thenReturn(true);
        when(handler.getInvocationContainer()).thenReturn(container);

        // Act
        Object result = returnsDeepStubs.getMock(invocation, returnTypeGenericMetadata);

        // Assert
        assertNotNull(result);
        verify(invocation, times(1)).getMock();
        verify(returnTypeGenericMetadata, times(2)).resolveGenericReturnType(invocation.getMethod());
        verify(returnTypeGenericMetadata, times(1)).rawType();
        verify(mockitoCore, times(1)).isTypeMockable(Object.class);
        verify(handler, times(1)).getInvocationContainer();
    }

    @Test
    public void testCreateNewDeepStubMock() {
        // Arrange
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        MockitoCore mockitoCore = mock(MockitoCore.class);
        ReturnsDeepStubs returnsDeepStubs = mock(ReturnsDeepStubs.class);

        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(mockitoCore.mock(returnTypeGenericMetadata.rawType(), null)).thenReturn(null);

        // Act
        Object result = returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);

        // Assert
        assertNull(result);
        verify(returnTypeGenericMetadata, times(1)).rawType();
        verify(mockitoCore, times(1)).mock(Object.class, null);
    }

    @Test
    public void testWithSettingsUsing() {
        // Arrange
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        MockSettings mockSettings = mock(MockSettings.class);

        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class<?>[]{Serializable.class});
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(mockSettings.extraInterfaces(returnTypeGenericMetadata.rawExtraInterfaces())).thenReturn(mockSettings);
        when(mockSettings.defaultAnswer(null)).thenReturn(mockSettings);

        // Act
        MockSettings result = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);

        // Assert
        assertNotNull(result);
        verify(returnTypeGenericMetadata, times(1)).rawExtraInterfaces();
        verify(returnTypeGenericMetadata, times(1)).rawType();
        verify(mockSettings, times(1)).extraInterfaces(returnTypeGenericMetadata.rawExtraInterfaces());
        verify(mockSettings, times(1)).defaultAnswer(null);
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        // Arrange
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        ReturnsDeepStubs returnsDeepStubs = mock(ReturnsDeepStubs.class);

        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);

        // Act
        ReturnsDeepStubs result = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);

        // Assert
        assertNotNull(result);
        verify(returnTypeGenericMetadata, times(1)).rawType();
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        // Arrange
        Object mock = mock(Object.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        when(handler.getInvocationContainer()).thenReturn(container);

        // Act
        Object result = returnsDeepStubs.recordDeepStubMock(mock, container);

        // Assert
        assertNotNull(result);
        verify(handler, times(1)).getInvocationContainer();
        verify(container, times(1)).addAnswer(any(), eq(false));
    }

    @Test
    public void testActualParameterizedType() {
        // Arrange
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);

        when(new MockUtil().getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(null);

        // Act
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);

        // Assert
        assertNotNull(result);
        verify(new MockUtil(), times(1)).getMockHandler(mock);
        verify(mockSettings, times(1)).getTypeToMock();
    }

}