package org.mockito.internal.stubbing.defaultanswers;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class ReturnsDeepStubsTest {
    
    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setup() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswer() throws Throwable {
        // Test case for when returnTypeGenericMetadata is not type mockable
        Object returnValue = returnsDeepStubs.answer(mock(InvocationOnMock.class));
        assertNull(returnValue);
        
        // Test case for when returnTypeGenericMetadata is type mockable
        // Simulating situation where invocation matches stubbed invocation
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        Object returnValue2 = returnsDeepStubs.answer(invocation);
        assertNotNull(returnValue2);
    }
    
    @Test
    public void testGetMock() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        
        // Test case for when matching stubbed invocation exists
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(container.getStubbedInvocations()).thenReturn(Arrays.asList(stubbedInvocationMatcher));
        when(invocation.matches(stubbedInvocationMatcher.getInvocation())).thenReturn(true);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn(mock(Object.class));
        
        Object returnValue = returnsDeepStubs.getMock(invocation);
        assertNotNull(returnValue);
        
        // Test case for when matching stubbed invocation does not exist
        when(invocation.matches(stubbedInvocationMatcher.getInvocation())).thenReturn(false);
        Object returnValue2 = returnsDeepStubs.getMock(invocation);
        assertNotNull(returnValue2);
    }
    
    @Test
    public void testRecordDeepStubMock() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
        when(container.addAnswer(any(Answer.class), eq(false))).thenReturn(mock(Object.class));
        
        Object returnValue = returnsDeepStubs.recordDeepStubMock(invocation, container);
        assertNotNull(returnValue);
    }
    
    @Test
    public void testActualParameterizedType() {
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(new MockUtil().getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(mock(Type.class));
        
        GenericMetadataSupport returnValue = returnsDeepStubs.actualParameterizedType(mock);
        assertNotNull(returnValue);
    }
}