package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;

public class ReturnsDeepStubsTest {
    
    private ReturnsDeepStubs returnsDeepStubs;
    private InvocationContainerImpl container;
    
    @Before
    public void setup() {
        returnsDeepStubs = new ReturnsDeepStubs();
        container = new InvocationContainerImpl();
    }
    
    @Test
    public void testAnswer() throws Throwable {
        assertNotNull(returnsDeepStubs.answer(mock(InvocationOnMock.class)));
    }
    
    @Test
    public void testGetMock() throws Throwable {
        container.addAnswer(() -> "mock", false);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Mockito.when(invocation.getMock()).thenReturn(mock(Object.class));
        
        Object mock = returnsDeepStubs.getMock(invocation);
        
        assertNotNull(mock);
        assertEquals("mock", mock);
    }
    
    @Test
    public void testRecordDeepStubMock() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(mock(Method.class));
        
        Object mock = returnsDeepStubs.recordDeepStubMock(invocation, container);
        
        assertNotNull(mock);
        assertEquals(mock(Method.class), mock);
    }
    
    @Test
    public void testActualParameterizedType() {
        Object mock = mock(Object.class);
        Mockito.when(mock.getMockHandler(mock)).thenReturn(mock(MockUtil.class));
        Mockito.when(mock.getMockSettings()).thenReturn(mock(CreationSettings.class));
        Mockito.when(mock.getTypeToMock()).thenReturn(mock(Type.class));
        
        GenericMetadataSupport metadata = returnsDeepStubs.actualParameterizedType(mock);
        
        assertNotNull(metadata);
        assertEquals(mock(Type.class), metadata.getTypeToMock());
    }
}