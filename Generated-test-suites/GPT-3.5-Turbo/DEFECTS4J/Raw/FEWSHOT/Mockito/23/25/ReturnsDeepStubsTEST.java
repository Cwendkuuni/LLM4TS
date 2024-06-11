
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsEmptyValues delegate = mock(ReturnsEmptyValues.class);
        MockitoCore mockitoCore = mock(MockitoCore.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        returnsDeepStubs.delegate = delegate;
        returnsDeepStubs.mockitoCore = mockitoCore;

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        Class<?> rawType = mock(Class.class);

        when(returnTypeGenericMetadata.rawType()).thenReturn(rawType);
        when(mockitoCore.isTypeMockable(rawType)).thenReturn(true);
        when(invocation.getMock()).thenReturn(mock(Object.class));

        Object result = returnsDeepStubs.answer(invocation);
        assertEquals(delegate.returnValueFor(rawType), result);

        when(mockitoCore.isTypeMockable(rawType)).thenReturn(false);

        result = returnsDeepStubs.answer(invocation);
        assertNull(result);

        Object mock = mock(Object.class);
        when(invocation.getMock()).thenReturn(mock);
        when(invocation.getMethod()).thenReturn(mock(Method.class));

        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(true);
        when(container.getStubbedInvocations()).thenReturn(Arrays.asList(stubbedInvocationMatcher));
        when(mockitoCore.get(MockUtil.getMockHandler(mock))).thenReturn(container);

        result = returnsDeepStubs.answer(invocation);
        verify(stubbedInvocationMatcher).answer(invocation);

        when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(false);

        result = returnsDeepStubs.answer(invocation);
        verify(container).addAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return mock;
            }
        }, false);
        assertEquals(mock, result);
    }
}
