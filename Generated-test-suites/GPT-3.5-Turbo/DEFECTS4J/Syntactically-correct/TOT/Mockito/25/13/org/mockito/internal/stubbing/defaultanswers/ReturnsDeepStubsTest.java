import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer_invokeMethodMatchingInvocation_returnsStubbedValue() throws Throwable {
        // Arrange
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn("MockObject");
        when(invocation.getMethod()).thenReturn(String.class.getMethod("toString"));

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        assertEquals("MockObject", result);
    }

    @Test
    public void testAnswer_invokeMethodNotMatchingInvocation_returnsEmptyValue() throws Throwable {
        // Arrange
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn("MockObject");
        when(invocation.getMethod()).thenReturn(String.class.getMethod("hashCode"));

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        assertEquals("", result);
    }

    @Test
    public void testAnswer_invokeMethodWithNonMockableReturnType_returnsEmptyValue() throws Throwable {
        // Arrange
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn("MockObject");
        when(invocation.getMethod()).thenReturn(String.class.getMethod("getBytes"));

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        assertEquals("", result);
    }

    @Test
    public void testGetMock_matchingInvocationForVerification_returnsMockObject() throws Throwable {
        // Arrange
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(invocation.getMock()).thenReturn("MockObject");
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[] {});

        // Act
        Object result = returnsDeepStubs.getMock(invocation);

        // Assert
        assertEquals("MockObject", result);
    }

    @Test
    public void testGetMock_matchingInvocationForVerification_returnsStubbedValue() throws Throwable {
        // Arrange
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(invocation.getMock()).thenReturn("MockObject");
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[]{stubbedInvocationMatcher});
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn("StubbedValue");

        // Act
        Object result = returnsDeepStubs.getMock(invocation);

        // Assert
        assertEquals("StubbedValue", result);
        verify(stubbedInvocationMatcher).answer(invocation);
    }

    @Test
    public void testGetMock_nonMatchingInvocationForVerification_returnsDeepStubMock() throws Throwable {
        // Arrange
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(invocation.getMock()).thenReturn("MockObject");
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getInvocationForStubbing()).thenReturn(mock(InvocationOnMock.class));
        when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[]{stubbedInvocationMatcher});
        when(stubbedInvocationMatcher.getInvocation()).thenReturn(invocation);

        // Act
        Object result = returnsDeepStubs.getMock(invocation);

        // Assert
        assertNotNull(result);
        verify(container).addAnswer(any(Answer.class), eq(false));
    }

    @Test
    public void testRecordDeepStubMock_returnsMockObject() throws Throwable {
        // Arrange
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(invocation.getMethod()).thenReturn(String.class.getMethod("toString"));

        // Act
        Object result = returnsDeepStubs.recordDeepStubMock(invocation, container);

        // Assert
        assertEquals(String.class, result.getClass());
    }

    @Test
    public void testActualParameterizedType_returnsGenericMetadataSupport() {
        // Arrange
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);

        // Act
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);

        // Assert
        assertNotNull(result);
    }
}