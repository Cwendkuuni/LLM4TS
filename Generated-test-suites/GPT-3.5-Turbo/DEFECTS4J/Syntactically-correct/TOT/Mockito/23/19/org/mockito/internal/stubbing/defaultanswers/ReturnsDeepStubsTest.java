import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {
    
    private ReturnsDeepStubs returnsDeepStubs;
    
    @Before
    public void setUp() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }
    
    @Test
    public void testAnswer_WhenInvocationMatches_ReturnsStubbedValue() throws Throwable {
        // Mock objects for the test
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        MockitoCore mockitoCore = mock(MockitoCore.class);
        ReturnsEmptyValues delegate = mock(ReturnsEmptyValues.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        
        // Setting up the mock objects
        when(invocation.getMock()).thenReturn(returnTypeGenericMetadata);
        when(returnTypeGenericMetadata.rawType()).thenReturn(TestClass.class);
        when(mockitoCore.isTypeMockable(TestClass.class)).thenReturn(true);
        when(delegate.returnValueFor(TestClass.class)).thenReturn(new TestClass());
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(List.of(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(stubbedInvocationMatcher.getInvocation()).thenReturn(invocation);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn("MockedValue");
        
        // Calling the method to be tested
        Object result = returnsDeepStubs.answer(invocation);
        
        // Verifying the interactions and assertions
        verify(invocation).getMock();
        verify(invocation, atLeastOnce()).getMethod();
        verify(returnTypeGenericMetadata).rawType();
        verify(mockitoCore).isTypeMockable(TestClass.class);
        verify(delegate).returnValueFor(TestClass.class);
        verify(handler).getInvocationContainer();
        verify(container).getStubbedInvocations();
        verify(container).getInvocationForStubbing();
        verify(stubbedInvocationMatcher).getInvocation();
        verify(stubbedInvocationMatcher).answer(invocation);
        
        verifyNoMoreInteractions(invocation, returnTypeGenericMetadata, mockitoCore,
            delegate, handler, container, stubbedInvocationMatcher);
        
        assertEquals("MockedValue", result);
    }
    
    @Test
    public void testAnswer_WhenInvocationDoesNotMatch_ReturnsDeepStub() throws Throwable {
        // Mock objects for the test
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        MockitoCore mockitoCore = mock(MockitoCore.class);
        ReturnsEmptyValues delegate = mock(ReturnsEmptyValues.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        
        // Setting up the mock objects
        when(invocation.getMock()).thenReturn(returnTypeGenericMetadata);
        when(returnTypeGenericMetadata.rawType()).thenReturn(TestClass.class);
        when(mockitoCore.isTypeMockable(TestClass.class)).thenReturn(false);
        
        // Calling the method to be tested
        Object result = returnsDeepStubs.answer(invocation);
        
        // Verifying the interactions and assertions
        verify(invocation).getMock();
        verify(invocation, atLeastOnce()).getMethod();
        verify(returnTypeGenericMetadata).rawType();
        verify(mockitoCore).isTypeMockable(TestClass.class);
        
        verifyNoMoreInteractions(invocation, returnTypeGenericMetadata, mockitoCore,
            delegate, handler, container);
        
        assertNotNull(result);
        assertTrue(result instanceof TestClass);
    }
    
    private static class TestClass {
        // Class implementation for testing
    }
}