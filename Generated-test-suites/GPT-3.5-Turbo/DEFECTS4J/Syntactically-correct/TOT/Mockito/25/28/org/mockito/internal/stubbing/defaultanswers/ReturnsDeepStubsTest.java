import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer_InvocationIsMock_ReturnsDeepStubs() throws Throwable {
        // Arrange
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));

        // Act
        Object result = answer.answer(invocation);

        // Assert
        assertEquals("ReturnsDeepStubs answer should return deep stubs", mock(Object.class), result);
    }

    @Test
    public void testAnswer_InvocationIsNotMock_ReturnsEmptyValues() throws Throwable {
        // Arrange
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(new Object());

        // Act
        Object result = answer.answer(invocation);

        // Assert
        assertNotNull("ReturnsDeepStubs answer should return non-null object", result);
        assertFalse("ReturnsDeepStubs answer should return an object of type that can be mocked", result.getClass().equals(Object.class));
    }

    @Test
    public void testGetMock_MatchingInvocationExists_ReturnsMatchingInvocationAnswer() throws Throwable {
        // Arrange
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(answer.getMock(invocation)).thenCallRealMethod();
        when(answer.recordDeepStubMock(invocation, container)).thenReturn(mock(Object.class));
        when(container.getInvocationForStubbing().matches(any(InvocationOnMock.class))).thenReturn(true);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(mock(StubbedInvocationMatcher.class)));

        // Act
        Object result = answer.answer(invocation);

        // Assert
        assertNotNull("ReturnsDeepStubs getMock should return non-null object", result);
    }

    @Test
    public void testGetMock_NoMatchingInvocationExists_ReturnsRecordedDeepStubMock() throws Throwable {
        // Arrange
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(answer.getMock(invocation)).thenCallRealMethod();
        when(answer.recordDeepStubMock(invocation, container)).thenReturn(mock(Object.class));
        when(container.getInvocationForStubbing().matches(any(InvocationOnMock.class))).thenReturn(false);
        when(container.getStubbedInvocations()).thenReturn(Collections.emptyList());

        // Act
        Object result = answer.answer(invocation);

        // Assert
        assertNotNull("ReturnsDeepStubs getMock should return non-null object", result);
    }

    @Test
    public void testRecordDeepStubMock_CreatesMockUsingGenericsMetadata() throws Throwable {
        // Arrange
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("clone"));
        when(answer.recordDeepStubMock(invocation, container)).thenCallRealMethod();

        // Act
        Object result = answer.recordDeepStubMock(invocation, container);

        // Assert
        assertNotNull("ReturnsDeepStubs recordDeepStubMock should return non-null object", result);
        assertEquals("ReturnsDeepStubs recordDeepStubMock should create a mock using generics metadata", Object.class, result.getClass());
    }

    @Test
    public void testActualParameterizedType_ReturnsGenericMetadataSupport() throws Throwable {
        // Arrange
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(mockSettings.getTypeToMock()).thenReturn(Object.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        when(handler.getMockSettings()).thenReturn(mockSettings);
        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockHandler(mock)).thenReturn(handler);
        whenNew(MockUtil.class).withNoArguments().thenReturn(mockUtil);
        when(answer.actualParameterizedType(mock)).thenCallRealMethod();

        // Act
        GenericMetadataSupport result = answer.actualParameterizedType(mock);

        // Assert
        assertNotNull("ReturnsDeepStubs actualParameterizedType should return non-null object", result);
        assertEquals("ReturnsDeepStubs actualParameterizedType should return the actual generic metadata support", mockSettings.getTypeToMock(), result);
    }

}