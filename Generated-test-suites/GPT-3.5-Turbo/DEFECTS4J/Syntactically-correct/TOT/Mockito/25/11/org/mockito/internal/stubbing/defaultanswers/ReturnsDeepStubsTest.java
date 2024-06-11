import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

    @Test
    public void testAnswer_typicalUseCase() throws Throwable {
        // Prepare
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        // Perform
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Object);
    }

    @Test
    public void testAnswer_nonMockableType() throws Throwable {
        // Prepare
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object mock = mock(Object.class);
        when(invocation.getMock()).thenReturn(mock);

        // Perform
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        assertNotNull(result);
        assertSame(mock, result);
    }

    @Test
    public void testGetMock_matchInvocationForVerification() throws Throwable {
        // Prepare
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));

        // Perform
        Object result = returnsDeepStubs.getMock(invocation);

        // Assert
        assertNotNull(result);
        assertNotNull(handler);
        verify(container).getStubbedInvocations();
        verify(container).getInvocationForStubbing();
        verify(stubbedInvocationMatcher).getInvocation();
        verify(stubbedInvocationMatcher).answer(invocation);
    }

    @Test
    public void testGetMock_noMatchingInvocationForVerification() throws Throwable {
        // Prepare
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(Collections.emptyList());

        // Perform
        Object result = returnsDeepStubs.getMock(invocation);

        // Assert
        assertNotNull(result);
        assertNotNull(handler);
        verify(container).getStubbedInvocations();
        verify(container, never()).getInvocationForStubbing();
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        // Prepare
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));

        // Perform
        Object result = returnsDeepStubs.recordDeepStubMock(invocation, container);

        // Assert
        assertNotNull(result);
        verify(container).addAnswer(any(Answer.class), eq(false));
    }

    @Test
    public void testActualParameterizedType() throws Throwable {
        // Prepare
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(new MockUtil().getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(Object.class);

        // Perform
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);

        // Assert
        assertNotNull(result);
        verify(mockSettings).getTypeToMock();
    }
}