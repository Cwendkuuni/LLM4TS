import static org.junit.Assert.*;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsEmptyValues delegateMock = mock(ReturnsEmptyValues.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object result = new Object();

        when(delegateMock.returnValueFor(any(Class.class))).thenReturn(result);

        Object actual = returnsDeepStubs.answer(invocation);

        assertEquals(result, actual);
        verify(delegateMock).returnValueFor(any(Class.class));
        verifyNoMoreInteractions(delegateMock);
    }

    @Test
    public void testGetMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InternalMockHandler<Object> handlerMock = mock(InternalMockHandler.class);
        MockitoUtil mockitoUtilMock = mock(MockitoUtil.class);
        InvocationContainerImpl containerMock = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcherMock = mock(StubbedInvocationMatcher.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Class clz = Object.class;
        Object mock = new Object();

        when(handlerMock.getInvocationContainer()).thenReturn(containerMock);
        when(mockitoUtilMock.getMockHandler(invocation.getMock())).thenReturn(handlerMock);
        when(containerMock.getStubbedInvocations()).thenReturn(Arrays.asList(stubbedInvocationMatcherMock));
        when(containerMock.getInvocationForStubbing().matches(stubbedInvocationMatcherMock.getInvocation())).thenReturn(true);
        when(stubbedInvocationMatcherMock.answer(invocation)).thenReturn(mock);

        Object actual = returnsDeepStubs.getMock(invocation);

        assertEquals(mock, actual);
        verify(handlerMock).getInvocationContainer();
        verify(mockitoUtilMock).getMockHandler(invocation.getMock());
        verify(containerMock).getStubbedInvocations();
        verify(containerMock).getInvocationForStubbing();
        verify(stubbedInvocationMatcherMock).getInvocation();
        verify(stubbedInvocationMatcherMock).answer(invocation);
        verifyNoMoreInteractions(handlerMock, mockitoUtilMock, containerMock, stubbedInvocationMatcherMock);
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InternalMockHandler<Object> handlerMock = mock(InternalMockHandler.class);
        MockitoUtil mockitoUtilMock = mock(MockitoUtil.class);
        InvocationContainerImpl containerMock = mock(InvocationContainerImpl.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Class clz = Object.class;
        Object mock = new Object();

        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().getReturnType()).thenReturn(clz);
        when(mockitoUtilMock.getMockHandler(invocation.getMock())).thenReturn(handlerMock);
        doAnswer(inv -> {
            Answer<Object> answer = inv.getArgument(0);
            answer.answer(invocation);
            return null;
        }).when(containerMock).addAnswer(any(Answer.class), eq(false));
        when(handlerMock.getInvocationContainer()).thenReturn(containerMock);
        when(mockitoUtilMock.mock(clz, returnsDeepStubs)).thenReturn(mock);

        Object actual = returnsDeepStubs.recordDeepStubMock(invocation, containerMock);

        assertEquals(mock, actual);
        verify(invocation).getMethod();
        verify(invocation.getMethod()).getReturnType();
        verify(mockitoUtilMock).getMockHandler(invocation.getMock());
        verify(containerMock).addAnswer(any(Answer.class), eq(false));
        verify(handlerMock).getInvocationContainer();
        verify(mockitoUtilMock).mock(clz, returnsDeepStubs);
        verifyNoMoreInteractions(invocation, handlerMock, mockitoUtilMock, containerMock);
    }

    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);

        when(mockitoUtilMock.getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(clz);

        GenericMetadataSupport actual = returnsDeepStubs.actualParameterizedType(mock);

        assertNotNull(actual);
        verify(mockitoUtilMock).getMockHandler(mock);
        verify(mockitoUtilMock.getMockHandler(mock)).getMockSettings();
        verify(mockSettings).getTypeToMock();
        verifyNoMoreInteractions(mockitoUtilMock, mockSettings);
    }
}