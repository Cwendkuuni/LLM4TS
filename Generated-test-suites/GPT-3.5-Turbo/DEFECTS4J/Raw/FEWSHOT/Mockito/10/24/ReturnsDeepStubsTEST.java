
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsDeepStubsTest {
    
    @Mock
    private InvocationOnMock invocation;
    
    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswerWhenMockIsNotTypeMockable() throws Throwable {
        when(invocation.getMock()).thenReturn("mock");
        when(MockUtil.isTypeMockable(String.class)).thenReturn(false);

        Object actual = returnsDeepStubs.answer(invocation);

        verify(MockUtil.isTypeMockable(String.class));
        verify(invocation, never()).getMethod();
        verify(invocation, never()).getMock();
        verifyNoMoreInteractions(invocation);

        assertSame("delegateReturnValue", actual);
    }

    @Test
    public void testAnswerWhenReturnTypeIsMockable() throws Throwable {
        when(invocation.getMock()).thenReturn("mock");
        when(MockUtil.isTypeMockable(String.class)).thenReturn(true);
        when(MockUtil.getMockHandler("mock")).thenReturn(mockHandler);
        when(mockHandler.getInvocationContainer()).thenReturn(invocationContainer);
        when(invocationContainer.getStubbedInvocations()).thenReturn(stubbedInvocations);
        when(invocationContainer.getInvocationForStubbing()).thenReturn(invocationForStubbing);

        when(invocationForStubbing.matches(stubbedInvocationMatcher.getInvocation())).thenReturn(true);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn("expected");

        Object actual = returnsDeepStubs.answer(invocation);

        verify(MockUtil.isTypeMockable(String.class));
        verify(invocation).getMock();
        verify(mockHandler).getInvocationContainer();
        verify(invocationContainer).getStubbedInvocations();
        verify(invocationContainer).getInvocationForStubbing();
        verify(stubbedInvocationMatcher).getInvocation();
        verify(invocationForStubbing).matches(stubbedInvocationMatcher.getInvocation());
        verify(stubbedInvocationMatcher).answer(invocation);
        verifyNoMoreInteractions(invocation);

        assertSame("expected", actual);
    }

    @Test
    public void testAnswerWhenReturnTypeIsNotMockable() throws Throwable {
        when(invocation.getMock()).thenReturn("mock");
        when(MockUtil.isTypeMockable(String.class)).thenReturn(true);
        when(MockUtil.getMockHandler("mock")).thenReturn(mockHandler);
        when(mockHandler.getInvocationContainer()).thenReturn(invocationContainer);
        when(invocationContainer.getStubbedInvocations()).thenReturn(stubbedInvocations);
        when(invocationContainer.getInvocationForStubbing()).thenReturn(invocationForStubbing);

        when(invocationForStubbing.matches(stubbedInvocationMatcher.getInvocation())).thenReturn(false);
        when(mockitoCore.isTypeMockable(String.class)).thenReturn(false);
        when(delegate.returnValueFor(String.class)).thenReturn("expected");

        Object actual = returnsDeepStubs.answer(invocation);

        verify(MockUtil.isTypeMockable(String.class));
        verify(invocation).getMock();
        verify(mockHandler).getInvocationContainer();
        verify(invocationContainer).getStubbedInvocations();
        verify(invocationContainer).getInvocationForStubbing();
        verify(stubbedInvocationMatcher).getInvocation();
        verify(invocationForStubbing).matches(stubbedInvocationMatcher.getInvocation());
        verify(mockitoCore).isTypeMockable(String.class);
        verify(delegate).returnValueFor(String.class);
        verifyNoMoreInteractions(invocation);

        assertSame("expected", actual);
    }
}
