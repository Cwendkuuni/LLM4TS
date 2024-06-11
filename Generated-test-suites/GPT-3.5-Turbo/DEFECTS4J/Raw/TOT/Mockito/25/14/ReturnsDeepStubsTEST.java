import org.junit.Test;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer_withNonMockableReturnType_shouldReturnEmptyValue() throws Throwable {
        // Arrange
        Object mock = mock(Object.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock);
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        assertTrue(result instanceof String);
        assertEquals("", result.toString());
    }

    @Test
    public void testAnswer_withMockableReturnType_shouldReturnMock() throws Throwable {
        // Arrange
        Object mock = mock(Object.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock);
        when(invocation.getMethod()).thenReturn(Map.class.getMethod("entrySet"));

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        assertTrue(result instanceof Map);
        assertNotNull(result);
    }

    @Test
    public void testGetMock_withMatchingInvocation_shouldReturnStubbedAnswer() throws Throwable {
        // Arrange
        Object mock = mock(Object.class);
        InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        when(invocation.getMock()).thenReturn(mock);
        when(mockHandler.getInvocationContainer()).thenReturn(container);
        when(container.getInvocationForStubbing().matches(invocation)).thenReturn(true);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);

        // Act
        Object result = returnsDeepStubs.getMock(invocation);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testGetMock_withoutMatchingInvocation_shouldReturnDeepStubMock() throws Throwable {
        // Arrange
        Object mock = mock(Object.class);
        InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);
        when(mockHandler.getInvocationContainer()).thenReturn(container);

        // Act
        Object result = returnsDeepStubs.getMock(invocation);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testRecordDeepStubMock_withMockType_shouldReturnMock() throws Throwable {
        // Arrange
        Object mock = mock(Object.class);
        InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock);
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);
        when(mockHandler.getInvocationContainer()).thenReturn(container);

        // Act
        Object result = returnsDeepStubs.recordDeepStubMock(invocation, container);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testActualParameterizedType_withMockType_shouldReturnGenericMetadataSupport() throws Throwable {
        // Arrange
        Object mock = mock(Object.class);
        InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);
        when(mockHandler.getInvocationContainer()).thenReturn(container);
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(mockSettings.getTypeToMock()).thenReturn(Map.class);
        when(mockHandler.getMockSettings()).thenReturn(mockSettings);

        // Act
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);

        // Assert
        assertNotNull(result);
    }
}