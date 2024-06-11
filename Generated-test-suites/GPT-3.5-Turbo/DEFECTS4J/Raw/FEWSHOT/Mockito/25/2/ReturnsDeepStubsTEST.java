import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class ReturnsDeepStubsTest {
    @Test
    public void testAnswer() throws Throwable {
        ReturnsEmptyValues delegate = mock(ReturnsEmptyValues.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs(delegate);

        // Test when returnTypeGenericMetadata is not mockable
        when(delegate.returnValueFor(Object.class)).thenReturn(null);
        assertEquals(null, returnsDeepStubs.answer(mock(InvocationOnMock.class)));

        // Test when returnTypeGenericMetadata is mockable
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(invocation.getMock()).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(container);

        // Test when container has a matching stubbed invocation
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(true);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn("mocked result");
        assertEquals("mocked result", returnsDeepStubs.answer(invocation));

        // Test when container does not have a matching stubbed invocation
        when(container.getStubbedInvocations()).thenReturn(Collections.emptyList());
        Object mock = mock(Object.class);
        when(mock(clz, returnsDeepStubs)).thenReturn(mock);
        when(container.addAnswer(any(Answer.class), eq(false))).thenReturn(true);
        assertEquals(mock, returnsDeepStubs.answer(invocation));
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = mock(Method.class);
        when(invocation.getMethod()).thenReturn(method);
        when(method.getReturnType()).thenReturn(Object.class);

        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        ReturnsDeepStubs spyReturnsDeepStubs = spy(returnsDeepStubs);
        doReturn(mock).when(spyReturnsDeepStubs).mock(clz, returnsDeepStubs);
        when(invocation.getMock()).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(container);

        assertEquals(mock, spyReturnsDeepStubs.recordDeepStubMock(invocation, container));
        verify(container, times(1)).addAnswer(any(Answer.class), eq(false));
    }

    @Test
    public void testActualParameterizedType() {
        Object mock = mock(Object.class);
        MockUtil mockUtil = mock(MockUtil.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(mockUtil.getMockHandler(mock)).thenReturn(handler);
        when(handler.getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(Object.class);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        ReturnsDeepStubs spyReturnsDeepStubs = spy(returnsDeepStubs);
        doReturn(mockUtil).when(spyReturnsDeepStubs).getMockUtil();
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        when(GenericMetadataSupport.inferFrom(Object.class)).thenReturn(genericMetadataSupport);

        assertEquals(genericMetadataSupport, spyReturnsDeepStubs.actualParameterizedType(mock));
    }
}