package org.mockito.internal.stubbing.defaultanswers;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doReturn;

import org.junit.Test;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        // Test case where mock is not type mockable
        Object invocationMock1 = mock(Object.class);
        GenericMetadataSupport genericMetadata1 = mock(GenericMetadataSupport.class);
        when(genericMetadata1.rawType()).thenReturn(Object.class);
        when(returnsDeepStubs.actualParameterizedType(invocationMock1)).thenReturn(genericMetadata1);
        Object result1 = returnsDeepStubs.answer(invocationMock1);
        assertNull(result1);

        // Test case where mock is type mockable and matches invocation for verification
        Object invocationMock2 = mock(Object.class);
        GenericMetadataSupport genericMetadata2 = mock(GenericMetadataSupport.class);
        when(genericMetadata2.rawType()).thenReturn(String.class);
        when(returnsDeepStubs.actualParameterizedType(invocationMock2)).thenReturn(genericMetadata2);
        InvocationOnMock invocation2 = mock(InvocationOnMock.class);
        when(invocation2.getMock()).thenReturn(invocationMock2);
        when(invocation2.getMethod()).thenReturn(String.class.getMethod("length"));
        Object stubbedInvocationMatcher2 = mock(StubbedInvocationMatcher.class);
        when(stubbedInvocationMatcher2.getInvocation()).thenReturn(invocation2.getMock());
        MockUtil mockUtil2 = mock(MockUtil.class);
        when(mockUtil2.getMockHandler(invocation2.getMock())).thenReturn(mock(InternalMockHandler.class));
        when(mockUtil2.getMockHandler(invocation2.getMock()).getInvocationContainer()).thenReturn(mock(InvocationContainerImpl.class));
        when(mockUtil2.getMockHandler(invocation2.getMock()).getInvocationContainer().getInvocationForStubbing()).thenReturn(invocation2.getMock());
        when(mockUtil2.getMockHandler(invocation2.getMock()).getInvocationContainer().getStubbedInvocations()).thenReturn(new ArrayList<StubbedInvocationMatcher>());
        when(mockUtil2.getMockHandler(invocation2.getMock()).getInvocationContainer().getStubbedInvocations().contains(stubbedInvocationMatcher2)).thenReturn(true);
        ResultCaptor<? extends Stubbing> resultCaptor2 = mock(ResultCaptor.class);
        when(resultCaptor2.capture()).thenReturn(stubbedInvocationMatcher2);
        ReturnsDeepStubs returnsDeepStubsMock2 = mock(ReturnsDeepStubs.class);
        when(returnsDeepStubsMock2.answer(invocation2)).thenReturn(resultCaptor2.capture().answer(invocation2));
        Object result2 = returnsDeepStubs.answer(invocationMock2);
        assertNotNull(result2);

        // Test case for deep stub
        Object invocationMock3 = mock(Object.class);
        GenericMetadataSupport genericMetadata3 = mock(GenericMetadataSupport.class);
        when(genericMetadata3.rawType()).thenReturn(String.class);
        when(returnsDeepStubs.actualParameterizedType(invocationMock3)).thenReturn(genericMetadata3);
        InvocationOnMock invocation3 = mock(InvocationOnMock.class);
        when(invocation3.getMock()).thenReturn(invocationMock3);
        when(invocation3.getMethod()).thenReturn(String.class.getMethod("length"));
        Object stubbedInvocationMatcher3 = mock(StubbedInvocationMatcher.class);
        when(stubbedInvocationMatcher3.getInvocation()).thenReturn(invocation3.getMock());
        MockUtil mockUtil3 = mock(MockUtil.class);
        when(mockUtil3.getMockHandler(invocation3.getMock())).thenReturn(mock(InternalMockHandler.class));
        when(mockUtil3.getMockHandler(invocation3.getMock()).getInvocationContainer()).thenReturn(mock(InvocationContainerImpl.class));
        when(mockUtil3.getMockHandler(invocation3.getMock()).getInvocationContainer().getInvocationForStubbing()).thenReturn(invocation3.getMock());
        when(mockUtil3.getMockHandler(invocation3.getMock()).getInvocationContainer().getStubbedInvocations()).thenReturn(new ArrayList<StubbedInvocationMatcher>());
        when(mockUtil3.getMockHandler(invocation3.getMock()).getInvocationContainer().getStubbedInvocations().contains(stubbedInvocationMatcher3)).thenReturn(false);
        ResultCaptor<? extends Stubbing> resultCaptor3 = mock(ResultCaptor.class);
        when(resultCaptor3.capture()).thenReturn(stubbedInvocationMatcher3);
        ReturnsDeepStubs returnsDeepStubsMock3 = mock(ReturnsDeepStubs.class);
        when(returnsDeepStubsMock3.answer(invocation3)).thenReturn(resultCaptor3.capture().answer(invocation3));
        Object mock3 = mock(Object.class);
        doReturn(mock3).when(invocationMock3).answer(invocation3);
        Object result3 = returnsDeepStubs.answer(invocationMock3);
        assertNotNull(result3);
    }
}