import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setUp() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswer_WithMockableReturnType_ReturnsMockObject() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(new Object());

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        assertTrue(result instanceof ReturnsEmptyValues);
    }

    @Test
    public void testAnswer_WithNonMockableReturnType_ReturnsDelegateReturnValue() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(new NonMockableObject());

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        assertEquals(new NonMockableObject(), result);
    }

    @Test
    public void testGetMock_WithMatchingInvocation_ReturnsStubbedInvocationResult() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        MockUtil mockUtil = mock(MockUtil.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        
        when(mockUtil.getMockHandler(any())).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(invocation.matches(any())).thenReturn(true);
        when(stubbedInvocationMatcher.answer(any())).thenReturn("stubbedResult");

        // Act
        Object result = returnsDeepStubs.getMock(invocation, new GenericMetadataSupport());

        // Assert
        assertEquals("stubbedResult", result);
    }

    @Test
    public void testGetMock_WithNoMatchingInvocation_CreatesNewDeepStubMock() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        MockUtil mockUtil = mock(MockUtil.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        ReturnsDeepStubs delegateReturnsDeepStubs = mock(ReturnsDeepStubs.class);
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport();

        when(mockUtil.getMockHandler(any())).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(Collections.emptyList());
        when(delegateReturnsDeepStubs.answer(any())).thenReturn("delegateResult");
        when(returnsDeepStubs.createNewDeepStubMock(any())).thenReturn("newMock");
        when(returnsDeepStubs.recordDeepStubMock(any(), any())).thenReturn("stubbedResult");

        // Act
        Object result = returnsDeepStubs.getMock(invocation, returnTypeGenericMetadata);

        // Assert
        assertEquals("stubbedResult", result);
        verify(delegateReturnsDeepStubs).answer(any());
        verify(returnsDeepStubs).createNewDeepStubMock(returnTypeGenericMetadata);
        verify(returnsDeepStubs).recordDeepStubMock("newMock", container);
    }

    @Test
    public void testCreateNewDeepStubMock_WithGenericMetadata_ReturnsMock() throws Throwable {
        // Arrange
        ReturnsDeepStubs returnsDeepStubs = mock(ReturnsDeepStubs.class);
        MockitoCore mockitoCore = mock(MockitoCore.class);
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport();

        when(returnsDeepStubs.createNewDeepStubMock(any())).thenCallRealMethod();
        when(returnsDeepStubs.getMockitoCore()).thenReturn(mockitoCore);
        when(mockitoCore.mock(any(), any())).thenReturn("mock");

        // Act
        Object result = returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);

        // Assert
        assertEquals("mock", result);
        verify(returnsDeepStubs).getMockitoCore();
        verify(mockitoCore).mock(any(), any());
    }

    @Test
    public void testWithSettingsUsing_WithGenericMetadata_ReturnsMockSettings() {
        // Arrange
        ReturnsDeepStubs returnsDeepStubs = mock(ReturnsDeepStubs.class);
        DefaultAnswer defaultAnswer = mock(DefaultAnswer.class);
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport();

        when(returnsDeepStubs.withSettingsUsing(any())).thenCallRealMethod();
        when(returnsDeepStubs.returnsDeepStubsAnswerUsing(any())).thenReturn(defaultAnswer);

        // Act
        MockSettings mockSettings = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);

        // Assert
        assertSame(defaultAnswer, mockSettings.getDefaultAnswer());
        verify(returnsDeepStubs).returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing_WithGenericMetadata_ReturnsReturnsDeepStubs() {
        // Arrange
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport();

        // Act
        ReturnsDeepStubs nestedReturnsDeepStubs = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);

        // Assert
        assertNotNull(nestedReturnsDeepStubs);
        assertSame(returnTypeGenericMetadata, nestedReturnsDeepStubs.actualParameterizedType(null));
    }

    @Test
    public void testRecordDeepStubMock_WithAnswer_AddsAnswerToContainer() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        when(handler.getInvocationContainer()).thenReturn(container);

        // Act
        Object result = returnsDeepStubs.recordDeepStubMock("mock", container);

        // Assert
        assertEquals("mock", result);
        verify(container).addAnswer(any(), eq(false));
    }

    @Test
    public void testActualParameterizedType_WithMock_ReturnsGenericMetadata() {
        // Arrange
        MockUtil mockUtil = mock(MockUtil.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        CreationSettings mockSettings = mock(CreationSettings.class);
        GenericMetadataSupport genericMetadataSupport = new GenericMetadataSupport();

        when(mockUtil.getMockHandler(any()).getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(Object.class);
        when(returnsDeepStubs.getMockUtil()).thenReturn(mockUtil);
        when(returnsDeepStubs.inferFrom(any())).thenReturn(genericMetadataSupport);

        // Act
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(null);

        // Assert
        assertEquals(genericMetadataSupport, result);
        verify(returnsDeepStubs).getMockUtil();
        verify(mockUtil).getMockHandler(null);
        verify(mockSettings).getTypeToMock();
        verify(returnsDeepStubs).inferFrom(Object.class);
    }

    private class NonMockableObject {
    }
}