import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;


public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;
    private InvocationOnMock invocation;
    private GenericMetadataSupport returnTypeGenericMetadata;

    @Before
    public void setup() {
        returnsDeepStubs = new ReturnsDeepStubs();
        invocation = mock(InvocationOnMock.class);
        returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
    }

    @Test
    public void whenReturnTypeIsNotMockable_shouldReturnDelegateReturnValue() throws Throwable {
        // Arrange
        when(returnTypeGenericMetadata.rawType()).thenReturn(String.class);
        when(returnsDeepStubs.actualParameterizedType(invocation.getMock())).thenReturn(returnTypeGenericMetadata);
        ReturnsEmptyValues delegate = mock(ReturnsEmptyValues.class);
        when(delegate.returnValueFor(String.class)).thenReturn("DelegateValue");

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        verify(delegate).returnValueFor(String.class);
        assertEquals("DelegateValue", result);
    }

    @Test
    public void whenReturnTypeIsMockable_shouldReturnMock() throws Throwable {
        // Arrange
        when(returnTypeGenericMetadata.rawType()).thenReturn(TestMock.class);
        when(returnsDeepStubs.actualParameterizedType(invocation.getMock())).thenReturn(returnTypeGenericMetadata);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(handler.getInvocationContainer()).thenReturn(container);

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        verify(container, never()).getStubbedInvocations();
        verifyNoMoreInteractions(container);
        verify(handler, never()).getMock();
        assertNotNull(result);
        assertTrue(result instanceof TestMock);
    }

    @Test
    public void whenInvocationMatchesStubbedInvocation_shouldReturnAnswerValue() throws Throwable {
        // Arrange
        when(returnTypeGenericMetadata.rawType()).thenReturn(TestMock.class);
        when(returnsDeepStubs.actualParameterizedType(invocation.getMock())).thenReturn(returnTypeGenericMetadata);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(handler.getInvocationContainer()).thenReturn(container);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(container.getStubbedInvocations()).thenReturn(List.of(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(true);
        Object answerValue = new Object();
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn(answerValue);

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        verify(container).getStubbedInvocations();
        verify(stubbedInvocationMatcher).getInvocation();
        verify(container).getInvocationForStubbing();
        verify(container.getInvocationForStubbing()).matches(stubbedInvocationMatcher.getInvocation());
        assertEquals(answerValue, result);
    }

    @Test
    public void whenInvocationDoesNotMatchStubbedInvocation_shouldCreateNewDeepStubMock() throws Throwable {
        // Arrange
        when(returnTypeGenericMetadata.rawType()).thenReturn(TestMock.class);
        when(returnsDeepStubs.actualParameterizedType(invocation.getMock())).thenReturn(returnTypeGenericMetadata);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(handler.getInvocationContainer()).thenReturn(container);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(container.getStubbedInvocations()).thenReturn(List.of(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(false);

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        verify(container).addAnswer(any(Answer.class), eq(false));
        assertNull(result);
    }

    @Test
    public void createNewDeepStubMock_shouldCreateMockWithReturnTypeGenericMetadata() throws Throwable {
        // Arrange
        when(returnTypeGenericMetadata.rawType()).thenReturn(TestMock.class);
        when(returnsDeepStubs.actualParameterizedType(invocation.getMock())).thenReturn(returnTypeGenericMetadata);
        MockitoCore mockitoCore = mock(MockitoCore.class);
        when(mockitoCore.mock(eq(TestMock.class), any(MockSettings.class))).thenReturn(new TestMock());

        // Act
        Object result = returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);

        // Assert
        verify(mockitoCore).mock(eq(TestMock.class), any(MockSettings.class));
        assertNotNull(result);
        assertTrue(result instanceof TestMock);
    }

    @Test
    public void withSettingsUsing_shouldReturnMockSettingsWithExtraInterfacesAndDefaultAnswer() {
        // Arrange
        when(returnTypeGenericMetadata.rawType()).thenReturn(TestMock.class);
        when(returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata)).thenCallRealMethod();

        // Act
        MockSettings result = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);

        // Assert
        assertNotNull(result);
        assertArrayEquals(returnTypeGenericMetadata.rawExtraInterfaces(), result.getExtraInterfaces());
        assertNotNull(result.getDefaultAnswer());
    }

    @Test
    public void returnsDeepStubsAnswerUsing_shouldReturnInstanceOfReturnsDeepStubs() {
        // Arrange
        when(returnTypeGenericMetadata.rawType()).thenReturn(TestMock.class);
        when(returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata)).thenCallRealMethod();

        // Act
        ReturnsDeepStubs result = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);

        // Assert
        assertNotNull(result);
        assertSame(ReturnsDeepStubs.class, result.getClass());
    }

    @Test
    public void recordDeepStubMock_shouldAddAnswerToContainerAndReturnMock() throws Throwable {
        // Arrange
        Object mock = mock(Object.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(container.addAnswer(any(Answer.class), anyBoolean())).thenReturn(container);

        // Act
        Object result = returnsDeepStubs.recordDeepStubMock(mock, container);

        // Assert
        verify(container).addAnswer(any(Answer.class), eq(false));
        assertSame(mock, result);
    }

    @Test
    public void actualParameterizedType_shouldReturnGenericMetadataFromMockSettings() {
        // Arrange
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(mockitoCore.getMockSettings(mock)).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(TestMock.class);
        when(returnsDeepStubs.actualParameterizedType(mock)).thenCallRealMethod();

        // Act
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);

        // Assert
        assertNotNull(result);
        assertEquals(GenericMetadataSupport.class, result.getClass());
    }

    private static class TestMock {
    }
}