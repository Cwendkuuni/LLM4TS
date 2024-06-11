import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        // Test invocation with non-mockable raw type
        // should return value based on delegate
        InvocationOnMock invocationNonMockable = mock(InvocationOnMock.class);
        when(invocationNonMockable.getMock()).thenReturn(new Object());
        Object resultNonMockable = returnsDeepStubs.answer(invocationNonMockable);
        assertNotNull(resultNonMockable);
        
        // Test deep stub creation when no matching invocation found
        // should create new deep stub mock based on generic metadata
        InvocationOnMock invocationNoMatch = mock(InvocationOnMock.class);
        when(invocationNoMatch.getMock()).thenReturn(mock(Object.class));
        Object resultNoMatch = returnsDeepStubs.answer(invocationNoMatch);
        assertNotNull(resultNoMatch);
    }
    
    @Test
    public void testGetMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        // Test getMock method when matching invocation found
        // should return answer from stubbed invocation matcher
        InvocationOnMock invocationMatch = mock(InvocationOnMock.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getInvocationForStubbing()).thenReturn(mock(InvocationOnMock.class));
        when(container.getStubbedInvocations()).thenReturn(java.util.Collections.singletonList(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(true);
        when(stubbedInvocationMatcher.answer(any())).thenReturn("Stubbed Answer");
        when(new MockUtil().getMockHandler(any())).thenReturn(handler);
        Object resultMatch = returnsDeepStubs.getMock(invocationMatch, mock(GenericMetadataSupport.class));
        assertEquals("Stubbed Answer", resultMatch);
        
        // Test getMock method when no matching invocation found
        // should create new deep stub mock based on generic metadata
        InvocationOnMock invocationNoMatch = mock(InvocationOnMock.class);
        Object resultNoMatch = returnsDeepStubs.getMock(invocationNoMatch, mock(GenericMetadataSupport.class));
        assertNotNull(resultNoMatch);
    }
    
    @Test
    public void testCreateNewDeepStubMock() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        
        // Test creating new deep stub mock
        // should return a newly created mock based on generic metadata
        Object result = returnsDeepStubs.createNewDeepStubMock(genericMetadataSupport);
        assertNotNull(result);
    }

    @Test
    public void testWithSettingsUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        
        // Test withSettingsUsing method
        // should return mock settings with extra interfaces and custom answer
        MockSettings mockSettings = returnsDeepStubs.withSettingsUsing(genericMetadataSupport);
        assertNotNull(mockSettings);
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        
        // Test recording deep stub mock
        // should add the answer to the container and return the mock
        Object mock = mock(Object.class);
        Object result = returnsDeepStubs.recordDeepStubMock(mock, container);
        assertNotNull(result);
    }

    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        CreationSettings creationSettings = mock(CreationSettings.class);
        
        // Test actualParameterizedType method
        // should return generic metadata from creation settings
        when(new MockUtil().getMockHandler(any()).getMockSettings()).thenReturn(creationSettings);
        when(creationSettings.getTypeToMock()).thenReturn(mock(Class.class));
        GenericMetadataSupport genericMetadataSupport = returnsDeepStubs.actualParameterizedType(mock);
        assertNotNull(genericMetadataSupport);
    }
}