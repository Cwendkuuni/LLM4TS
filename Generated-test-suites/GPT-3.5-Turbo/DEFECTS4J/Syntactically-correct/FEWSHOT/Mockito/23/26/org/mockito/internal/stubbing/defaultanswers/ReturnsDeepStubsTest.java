package org.mockito.internal.stubbing.defaultanswers;

import static org.mockito.Mockito.*;

import org.junit.Test;

public class ReturnsDeepStubsTest {
    
    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        
        Object result = returnsDeepStubs.answer(invocation);
        
        // Add assertions here
        
    }
    
    @Test
    public void testGetMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        
        Object result = returnsDeepStubs.getMock(invocation, returnTypeGenericMetadata);
        
        // Add assertions here
        
    }
    
    @Test
    public void testCreateNewDeepStubMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        
        Object result = returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);
        
        // Add assertions here
        
    }
    
    @Test
    public void testWithSettingsUsing() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        
        Object result = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);
        
        // Add assertions here
        
    }
    
    @Test
    public void testReturnsDeepStubsAnswerUsing() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        
        Object result = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
        
        // Add assertions here
        
    }
    
    @Test
    public void testRecordDeepStubMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        Object mock = mock(Object.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        
        Object result = returnsDeepStubs.recordDeepStubMock(mock, container);
        
        // Add assertions here
        
    }
    
    @Test
    public void testActualParameterizedType() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        
        Object result = returnsDeepStubs.actualParameterizedType(mock);
        
        // Add assertions here
        
    }
}