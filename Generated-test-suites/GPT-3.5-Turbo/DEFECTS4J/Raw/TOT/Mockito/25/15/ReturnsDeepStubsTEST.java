import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setUp() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswer_withValidInvocation_returnsMock() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Object);
    }

    @Test
    public void testAnswer_withInvalidInvocation_returnsEmptyValue() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(String.class));
        when(invocation.getMethod()).thenReturn(String.class.getMethod("length"));

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        assertNotNull(result);
        assertEquals(0, result);
    }

    @Test
    public void testAnswer_withNonMockableReturnType_returnsEmptyValue() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("getClass"));

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        assertNotNull(result);
        assertEquals(null, result);
    }

    @Test
    public void testGetMock_withMatchingInvocation_returnsStubbedResult() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("hashCode"));
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(mock(StubbedInvocationMatcher.class)));
        ReturnsDeepStubs returnsDeepStubsSpy = spy(returnsDeepStubs);
        doReturn(null).when(returnsDeepStubsSpy).recordDeepStubMock(any(), any());

        // Act
        Object result = returnsDeepStubsSpy.getMock(invocation);

        // Assert
        assertNotNull(result);
        assertEquals(null, result);
        verify(container).getInvocationForStubbing();
        verify(container).getStubbedInvocations();
    }

    @Test
    public void testGetMock_withNonMatchingInvocation_returnsDeepStubMock() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("hashCode"));
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(container.getInvocationForStubbing()).thenReturn(mock(InvocationOnMock.class));
        when(container.getStubbedInvocations()).thenReturn(Collections.emptyList());
        ReturnsDeepStubs returnsDeepStubsSpy = spy(returnsDeepStubs);
        doReturn(null).when(returnsDeepStubsSpy).recordDeepStubMock(any(), any());

        // Act
        Object result = returnsDeepStubsSpy.getMock(invocation);

        // Assert
        assertNotNull(result);
        assertEquals(null, result);
        verify(container).getInvocationForStubbing();
        verify(container).getStubbedInvocations();
        verify(returnsDeepStubsSpy).recordDeepStubMock(invocation, container);
    }

    @Test
    public void testRecordDeepStubMock_withValidInvocation_returnsMock() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("hashCode"));
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(container.addAnswer(any(Answer.class), anyBoolean())).thenReturn(true);
        ReturnsDeepStubs returnsDeepStubsSpy = spy(returnsDeepStubs);

        // Act
        Object result = returnsDeepStubsSpy.recordDeepStubMock(invocation, container);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Object);
        verify(container).addAnswer(any(Answer.class), anyBoolean());
    }

    @Test
    public void testActualParameterizedType_withMock_returnsGenericMetadataSupport() {
        // Arrange
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(mockSettings.getTypeToMock()).thenReturn(mock.getClass());
        InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
        when(mockHandler.getMockSettings()).thenReturn(mockSettings);
        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);
        ReturnsDeepStubs returnsDeepStubsSpy = spy(returnsDeepStubs);
        doReturn(mockUtil).when(returnsDeepStubsSpy).getMockUtil();

        // Act
        GenericMetadataSupport result = returnsDeepStubsSpy.actualParameterizedType(mock);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof GenericMetadataSupport);
    }

    @Test
    public void testActualParameterizedType_withNonMock_returnsGenericMetadataSupport() {
        // Arrange
        Object nonMock = new Object();
        ReturnsDeepStubs returnsDeepStubsSpy = spy(returnsDeepStubs);

        // Act
        GenericMetadataSupport result = returnsDeepStubsSpy.actualParameterizedType(nonMock);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof GenericMetadataSupport);
    }
}