import org.junit.Test;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsEmptyValues delegateMock = mock(ReturnsEmptyValues.class);
        ReturnsDeepStubs stubs = new ReturnsDeepStubs();
        stubs.delegate = delegateMock;

        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        GenericMetadataSupport genericMetadataSupportMock = mock(GenericMetadataSupport.class);
        when(invocationMock.getMock()).thenReturn(mock(Object.class));
        when(invocationMock.getMethod()).thenReturn(Object.class.getMethod("toString"));
        when(delegateMock.returnValueFor(Object.class)).thenReturn(new Object());
        when(stubs.actualParameterizedType(any())).thenReturn(genericMetadataSupportMock);
        when(genericMetadataSupportMock.rawType()).thenReturn(Object.class);

        stubs.answer(invocationMock);

        verify(delegateMock).returnValueFor(Object.class);
    }

    @Test
    public void testGetMock() throws Throwable {
        ReturnsEmptyValues delegateMock = mock(ReturnsEmptyValues.class);
        ReturnsDeepStubs stubs = new ReturnsDeepStubs();
        stubs.delegate = delegateMock;

        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        InternalMockHandler<Object> handlerMock = mock(InternalMockHandler.class);
        InvocationContainerImpl containerMock = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcherMock = mock(StubbedInvocationMatcher.class);
        when(invocationMock.getMock()).thenReturn(mock(Object.class));
        when(new MockUtil().getMockHandler(any())).thenReturn(handlerMock);
        when(handlerMock.getInvocationContainer()).thenReturn(containerMock);
        when(containerMock.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcherMock));

        stubs.getMock(invocationMock);

        verify(containerMock).getInvocationForStubbing();
        verify(stubbedInvocationMatcherMock).getInvocation();
        verify(stubbedInvocationMatcherMock).answer(invocationMock);
    }

    @Test
    public void testRecordDeepStubMock() {
        ReturnsDeepStubs stubs = new ReturnsDeepStubs();
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        InvocationContainerImpl containerMock = mock(InvocationContainerImpl.class);
        Answer<Object> answer = mock(Answer.class);
        when(invocationMock.getMethod()).thenReturn(Object.class.getMethod("toString"));
        when(invocationMock.getMethod().getReturnType()).thenReturn(Object.class);
        when(new MockUtil().getMockHandler(any())).thenReturn(mock(InternalMockHandler.class));
        when(new MockUtil().getMockHandler(any()).getInvocationContainer()).thenReturn(containerMock);
        when(new MockUtil().getMockHandler(any()).getInvocationContainer().addAnswer(any(), anyBoolean())).thenReturn(mock(Answer.class));

        stubs.recordDeepStubMock(invocationMock, containerMock);

        verify(containerMock).addAnswer(answer, false);
    }

    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs stubs = new ReturnsDeepStubs();
        CreationSettings mockSettingsMock = mock(CreationSettings.class);
        when(new MockUtil().getMockHandler(any()).getMockSettings()).thenReturn(mockSettingsMock);
        when(mockSettingsMock.getTypeToMock()).thenReturn(mock(Class.class));

        stubs.actualParameterizedType(mock(Object.class));

        verify(mockSettingsMock).getTypeToMock();
    }
}