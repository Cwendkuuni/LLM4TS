import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {

    private ReturnsEmptyValues delegate;
    private ReturnsDeepStubs returnsDeepStubs;

    public ReturnsDeepStubsTest() {
        delegate = mock(ReturnsEmptyValues.class);
        returnsDeepStubs = new ReturnsDeepStubs();
        returnsDeepStubs.delegate = delegate;
    }

    @Test
    public void testAnswer_DelegateReturnValue() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(delegate.returnValueFor(String.class)).thenReturn("mockString");
        Object result = returnsDeepStubs.answer(invocation);
        assertEquals("mockString", result);
        verify(delegate).returnValueFor(String.class);
    }

    @Test
    public void testAnswer_ReturnDeepStubMock() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(delegate.returnValueFor(String.class)).thenReturn(null);
        when(invocation.getMethod()).thenReturn(String.class.getMethod("length"));
        when(invocation.getMock()).thenReturn(new Object());
        Object result = returnsDeepStubs.answer(invocation);
        assertNotNull(result);
        verify(delegate).returnValueFor(String.class);
    }

    @Test
    public void testGetMock_MatchingInvocation() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        when(invocation.getMock()).thenReturn(new Object());
        when(handler.getInvocationContainer()).thenReturn(mock(InvocationContainerImpl.class));
        when(new MockUtil().getMockHandler(invocation.getMock())).thenReturn(handler);
        when(handler.getInvocationContainer().getStubbedInvocations())
                .thenReturn(singletonList(mock(StubbedInvocationMatcher.class)));
        when(handler.getInvocationContainer().getInvocationForStubbing().matches(any())).thenReturn(true);
        Object result = returnsDeepStubs.getMock(invocation);
        assertNotNull(result);
        verify(handler.getInvocationContainer().getInvocationForStubbing()).matches(any());
    }

    @Test
    public void testGetMock_NoMatchingInvocation() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        when(invocation.getMock()).thenReturn(new Object());
        when(handler.getInvocationContainer()).thenReturn(mock(InvocationContainerImpl.class));
        when(new MockUtil().getMockHandler(invocation.getMock())).thenReturn(handler);
        when(handler.getInvocationContainer().getStubbedInvocations())
                .thenReturn(singletonList(mock(StubbedInvocationMatcher.class)));
        when(handler.getInvocationContainer().getInvocationForStubbing().matches(any())).thenReturn(false);
        Object result = returnsDeepStubs.getMock(invocation);
        assertNull(result);
        verify(handler.getInvocationContainer().getInvocationForStubbing()).matches(any());
    }

    @Test
    public void testRecordDeepStubMock_AnswerReturnsMock() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(invocation.getMethod()).thenReturn(String.class.getMethod("length"));
        when(container.addAnswer(any(Answer.class), anyBoolean())).thenReturn(true);
        Object result = returnsDeepStubs.recordDeepStubMock(invocation, container);
        assertNotNull(result);
        verify(container).addAnswer(any(Answer.class), anyBoolean());
    }

    @Test
    public void testActualParameterizedType() {
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(new MockUtil().getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(String.class);
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);
        assertNotNull(result);
        assertEquals(String.class, result.rawType());
    }
}