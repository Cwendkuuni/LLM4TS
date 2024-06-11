import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));

        Object result = returnsDeepStubs.answer(invocation);

        assertNotNull(result);
    }

    @Test
    public void testMockitoCoreIsTypeMockable() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        MockitoCore mockitoCore = spy(MockitoCore.class);
        when(mockitoCore.isTypeMockable(Object.class)).thenReturn(false);

        returnsDeepStubs.mockitoCore = mockitoCore;

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));

        Object result = returnsDeepStubs.answer(invocation);

        assertNotNull(result);
    }

    @Test
    public void testDelegateReturnValueFor() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        ReturnsEmptyValues delegate = mock(ReturnsEmptyValues.class);
        when(delegate.returnValueFor(Object.class)).thenReturn(new Object());

        returnsDeepStubs.delegate = delegate;

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));

        Object result = returnsDeepStubs.answer(invocation);

        assertNotNull(result);
    }

    @Test
    public void testGetMockForVerification() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        MockUtil mockUtil = mock(MockUtil.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        InvocationOnMock invocationForStubbing = mock(InvocationOnMock.class);

        when(mockUtil.getMockHandler(any())).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[]{stubbedInvocationMatcher});
        when(container.getInvocationForStubbing()).thenReturn(invocationForStubbing);
        when(invocationForStubbing.matches(any())).thenReturn(true);
        when(stubbedInvocationMatcher.answer(any())).thenReturn(new Object());

        returnsDeepStubs.mockitoCore = mockitoCore;
        returnsDeepStubs.delegate = delegate;
        returnsDeepStubs.createDeepStubMock = createDeepStubMock;

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));

        Object result = returnsDeepStubs.answer(invocation);

        assertNotNull(result);
    }

    @Test
    public void testGetMockForStubbing() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        MockUtil mockUtil = mock(MockUtil.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);

        when(mockUtil.getMockHandler(any())).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[]{stubbedInvocationMatcher});
        when(container.getInvocationForStubbing()).thenReturn(mock(InvocationOnMock.class));
        when(stubbedInvocationMatcher.getInvocation()).thenReturn(mock(InvocationOnMock.class));
        when(stubbedInvocationMatcher.getInvocation().matches(any())).thenReturn(false);

        returnsDeepStubs.mockitoCore = mockitoCore;
        returnsDeepStubs.delegate = delegate;
        returnsDeepStubs.createDeepStubMock = createDeepStubMock;

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));

        Object result = returnsDeepStubs.answer(invocation);

        assertNotNull(result);
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        returnsDeepStubs.mockitoCore = mockitoCore;
        returnsDeepStubs.delegate = delegate;
        returnsDeepStubs.createDeepStubMock = createDeepStubMock;

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));

        Object result = returnsDeepStubs.answer(invocation);

        assertNotNull(result);
    }

}