
package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.Set;

import org.junit.Test;

@SuppressWarnings("unchecked")
public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // testing mock that is not type mockable
        Class<?> nonMockableClass = NonMockableClass.class;
        Object returnValueForNonMockableClass = returnsDeepStubs.answer(mock(InvocationOnMock.class));
        assertEquals("expected return value for non mockable class", nonMockableClass, returnValueForNonMockableClass);

        // testing mock that matches invocation for verification
        InvocationOnMock invocationForVerification = mock(InvocationOnMock.class);
        InvocationOnMock stubbedInvocation = mock(InvocationOnMock.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(container.getInvocationForStubbing().matches(stubbedInvocation)).thenReturn(true);
        when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[]{new StubbedInvocationMatcher(stubbedInvocation)});
        when(invocationForVerification.getMock()).thenReturn(container);
        Object returnValueForVerification = returnsDeepStubs.answer(invocationForVerification);
        assertEquals("expected return value for invocation verification", stubbedInvocation, returnValueForVerification);

        // testing deep stub
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Map.class);
        when(invocationForStubbing.getMock()).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getInvocationForStubbing()).thenReturn(stubbedInvocation);
        Object returnValueForDeepStub = returnsDeepStubs.answer(invocationForStubbing);
        assertEquals("expected return value for deep stub", returnValueForDeepStub, returnValueForDeepStub);
    }
}

class NonMockableClass {}
