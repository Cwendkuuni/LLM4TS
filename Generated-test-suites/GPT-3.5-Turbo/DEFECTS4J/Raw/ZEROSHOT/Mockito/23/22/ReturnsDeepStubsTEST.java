
package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setUp() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswer() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        
        Object returnValue = returnsDeepStubs.answer(invocation);
        
        assertNotNull(returnValue);
    }
    
    @Test
    public void testGetMock() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        
        Object returnValue = returnsDeepStubs.getMock(invocation, returnTypeGenericMetadata);
        
        assertNotNull(returnValue);
    }
    
    @Test
    public void testCreateNewDeepStubMock() throws Throwable {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        
        Object returnValue = returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);
        
        assertNotNull(returnValue);
    }
    
    @Test
    public void testWithSettingsUsing() throws Throwable {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        
        Object returnValue = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);
        
        assertNotNull(returnValue);
    }
    
    @Test
    public void testReturnsDeepStubsAnswerUsing() throws Throwable {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        
        Object returnValue = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
        
        assertNotNull(returnValue);
    }
    
    @Test
    public void testRecordDeepStubMock() throws Throwable {
        Object mock = mock(Object.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        
        Object returnValue = returnsDeepStubs.recordDeepStubMock(mock, container);
        
        assertNotNull(returnValue);
    }
    
    @Test
    public void testActualParameterizedType() {
        Object mock = mock(Object.class);
        
        ReturnsDeepStubs returnsDeepStubsSpy = spy(returnsDeepStubs);
        GenericMetadataSupport returnValue = returnsDeepStubsSpy.actualParameterizedType(mock);
        
        assertNotNull(returnValue);
    }

}
