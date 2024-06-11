import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setUp() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void answer_ValidInvocation_ReturnsMock() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Object);
    }

    @Test
    public void answer_InvalidInvocation_ReturnsDelegateValue() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(invocation.getMethod()).thenReturn(returnTypeGenericMetadata);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Void.class);

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        assertNull(result);
    }

    @Test
    public void getMock_MatchingInvocation_ReturnsStubbedAnswer() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(mockHandler.getInvocationContainer()).thenReturn(container);
        when(new MockUtil().getMockHandler(invocation.getMock())).thenReturn(mockHandler);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[]{stubbedInvocationMatcher});
        when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(true);
        Object expectedResult = new Object();
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn(expectedResult);

        // Act
        Object result = returnsDeepStubs.getMock(invocation);

        // Assert
        assertEquals(expectedResult, result);
    }

    @Test
    public void getMock_NonMatchingInvocation_ReturnsRecordedMock() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(mockHandler.getInvocationContainer()).thenReturn(container);
        when(new MockUtil().getMockHandler(invocation.getMock())).thenReturn(mockHandler);
        when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[]{});
        Class<?> returnType = Object.class;
        when(invocation.getMethod().getReturnType()).thenReturn(returnType);
        Object expectedResult = new Object();
        returnsDeepStubs = spy(returnsDeepStubs);
        doReturn(expectedResult).when(returnsDeepStubs).recordDeepStubMock(any(), any());

        // Act
        Object result = returnsDeepStubs.getMock(invocation);

        // Assert
        assertEquals(expectedResult, result);
    }

    @Test
    public void recordDeepStubMock_ValidInvocation_ReturnsMock() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(new MockUtil().getMockHandler(invocation.getMock())).thenReturn(mock(InternalMockHandler.class));
        when(new MockUtil().getMockHandler(invocation.getMock()).getInvocationContainer()).thenReturn(container);
        Class<?> returnType = Object.class;
        when(invocation.getMethod().getReturnType()).thenReturn(returnType);
        Object expectedResult = new Object();
        Answer<Object> answer = mock(Answer.class);
        when(answer.answer(invocation)).thenReturn(expectedResult);
        doReturn(answer).when(returnsDeepStubs).createAnswer();
        when(invocation.getMock()).thenReturn(mock(Object.class));
        GenericMetadataSupport mockSettings = mock(GenericMetadataSupport.class);
        when(new MockUtil().getMockHandler(invocation.getMock()).getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(returnType);

        // Act
        Object result = returnsDeepStubs.recordDeepStubMock(invocation, container);

        // Assert
        assertEquals(expectedResult, result);
        verify(container).addAnswer(answer, false);
        verify(container, times(1)).addAnswer(any(Answer.class), eq(false));
    }

    @Test
    public void actualParameterizedType_ValidMock_ReturnsGenericMetadata() {
        // Arrange
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(new MockUtil().getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        GenericMetadataSupport expectedResult = mock(GenericMetadataSupport.class);
        when(mockSettings.getTypeToMock()).thenReturn(mock(Class.class));
        when(GenericMetadataSupport.inferFrom(mockSettings.getTypeToMock())).thenReturn(expectedResult);

        // Act
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);

        // Assert
        assertEquals(expectedResult, result);
    }
}