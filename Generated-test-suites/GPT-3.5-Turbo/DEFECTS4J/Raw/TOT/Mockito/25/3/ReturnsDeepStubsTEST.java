import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setUp() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswer_InvokeNonMockableType_ReturnsEmptyValue() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn("non-mockable type");

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        assertTrue(result instanceof String);
        assertEquals("", result);
    }

    @Test
    public void testAnswer_InvokeMockableType_ReturnsMock() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(CreationSettings.class));

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        assertTrue(result instanceof CreationSettings);
        assertEquals(0, ((CreationSettings) result).getTypeToMock());
    }

    @Test
    public void testGetMock_InvocationMatchesStubbedInvocation_ReturnsStubbedAnswer() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(invocation.getMock()).thenReturn(mock(CreationSettings.class));
        when(mockHandler.getInvocationContainer()).thenReturn(container);
        when(container.getInvocationForStubbing().matches(invocation)).thenReturn(true);
        when(container.getStubbedInvocations()).thenReturn(Arrays.asList(stubbedInvocationMatcher));

        // Act
        Object result = returnsDeepStubs.getMock(invocation);

        // Assert
        assertTrue(result instanceof Object);
        assertEquals(mockedObject, result);
    }

    @Test
    public void testGetMock_InvocationDoesNotMatchStubbedInvocation_ReturnsRecordedDeepStubMock() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(invocation.getMock()).thenReturn(mock(CreationSettings.class));
        when(mockHandler.getInvocationContainer()).thenReturn(container);
        when(container.getInvocationForStubbing().matches(invocation)).thenReturn(false);
        when(container.getStubbedInvocations()).thenReturn(Arrays.asList(stubbedInvocationMatcher));

        // Act
        Object result = returnsDeepStubs.getMock(invocation);

        // Assert
        assertTrue(result instanceof Object);
        assertEquals(mockedObject, result);
    }

    @Test
    public void testRecordDeepStubMock_AddAnswer_CallsAddAnswerOnContainer() {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
        when(mockHandler.getInvocationContainer()).thenReturn(container);
        when(container.addAnswer(any(Answer.class), anyBoolean())).thenReturn(true);

        // Act
        Object result = returnsDeepStubs.recordDeepStubMock(invocation, container);

        // Assert
        assertTrue(result instanceof Object);
        assertEquals(mockedObject, result);
        verify(container).addAnswer(any(Answer.class), anyBoolean());
    }

    @Test
    public void testActualParameterizedType_MockHandlerHasCreationSettings_ReturnsGenericMetadata() {
        // Arrange
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(mockHandler.getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(Object.class);

        // Act
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);

        // Assert
        assertNotNull(result);
        assertEquals(Object.class, result.rawType());
    }

}