package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class ReturnsDeepStubsTest {
    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Mocks that are not type mockable should return default mock value
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport();
        returnTypeGenericMetadata.rawType(String.class);
        Object result1 = deepStubs.answer(invocation);
        assertEquals(null, result1);
        
        // Mocks that are type mockable should return deep stub mock
        GenericMetadataSupport returnTypeGenericMetadata2 = new GenericMetadataSupport();
        returnTypeGenericMetadata2.rawType(MyClass.class);
        Object result2 = deepStubs.answer(invocation);
        assertEquals(MyClass.class, result2.getClass());
    }
    
    @Test
    public void testDeepStub() throws Throwable {
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Invocation matches stub, should return answer
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(container.getInvocationForStubbing().matches(invocation)).thenReturn(true);
        when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[] {});
        Object result1 = deepStubs.deepStub(invocation, null);
        assertEquals(null, result1);
        
        // Invocation does not match any stub, should create new deep stub mock
        when(container.getInvocationForStubbing().matches(invocation)).thenReturn(false);
        Object result2 = deepStubs.deepStub(invocation, null);
        assertEquals(MyClass.class, result2.getClass());
    }
    
    @Test
    public void testNewDeepStubMock() throws Throwable {
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        
        // Mock with raw type and generic metadata
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport();
        returnTypeGenericMetadata.rawType(MyClass.class);
        Object result1 = deepStubs.newDeepStubMock(returnTypeGenericMetadata);
        assertEquals(MyClass.class, result1.getClass());
        
        // Mock with raw type and without generic metadata
        GenericMetadataSupport returnTypeGenericMetadata2 = new GenericMetadataSupport();
        returnTypeGenericMetadata2.rawType(String.class);
        Object result2 = deepStubs.newDeepStubMock(returnTypeGenericMetadata2);
        assertEquals(String.class, result2.getClass());
    }
    
    @Test
    public void testWithSettingsUsing() {
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        
        // Mock with raw extra interfaces
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport();
        returnTypeGenericMetadata.rawExtraInterfaces(new Class<?>[] { MyInterface.class });
        MockSettings mockSettings = deepStubs.withSettingsUsing(returnTypeGenericMetadata);
        assertEquals(true, mockSettings.extraInterfaces().contains(MyInterface.class));
        
        // Mock without raw extra interfaces
        GenericMetadataSupport returnTypeGenericMetadata2 = new GenericMetadataSupport();
        MockSettings mockSettings2 = deepStubs.withSettingsUsing(returnTypeGenericMetadata2);
        assertEquals(false, mockSettings2.isSerializable());
    }
    
    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        
        // Return new instance of ReturnsDeepStubsSerializationFallback
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport();
        ReturnsDeepStubsSerializationFallback result = deepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
        assertEquals(ReturnsDeepStubsSerializationFallback.class, result.getClass());
        assertEquals(returnTypeGenericMetadata, result.returnTypeGenericMetadata);
    }
    
    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        
        // Add answer to container and return mock
        Object mock = new Object();
        Object result = deepStubs.recordDeepStubAnswer(mock, container);
        assertEquals(mock, result);
    }
    
    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        
        // Mock with mock settings and generic type
        Object mock = mock(MyClass.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(((MockUtil) deepStubs).getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(MyClass.class);
        GenericMetadataSupport result1 = deepStubs.actualParameterizedType(mock);
        assertEquals(MyClass.class, result1.rawType());
        
        // Mock without mock settings and generic type
        Object mock2 = mock(AnotherClass.class);
        CreationSettings mockSettings2 = mock(CreationSettings.class);
        when(((MockUtil) deepStubs).getMockHandler(mock2).getMockSettings()).thenReturn(mockSettings2);
        when(mockSettings2.getTypeToMock()).thenReturn(null);
        GenericMetadataSupport result2 = deepStubs.actualParameterizedType(mock2);
        assertEquals(AnotherClass.class, result2.rawType());
    }
    
    @Test
    public void testReturnsDeepStubsSerializationFallback() throws IOException {
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport();
        returnTypeGenericMetadata.rawType(MyClass.class);
        ReturnsDeepStubsSerializationFallback fallback = new ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata);
        
        // Return generic metadata for actual types
        GenericMetadataSupport result1 = fallback.actualParameterizedType(null);
        assertEquals(returnTypeGenericMetadata, result1);
        
        // Test writeReplace method
        Object result2 = fallback.writeReplace();
        assertEquals(RETURNS_DEEP_STUBS, result2);
    }
    
    @Test
    public void testDeeplyStubbedAnswer() throws Throwable {
        Object mock = mock(MyClass.class);
        DeeplyStubbedAnswer answer = new DeeplyStubbedAnswer(mock);
        
        // Return mock for any invocation
        Object result = answer.answer(null);
        assertEquals(mock, result);
    }
    
    @Test
    public void testLazyHolder() {
        MockitoCore result1 = LazyHolder.MOCKITO_CORE;
        assertEquals(MockitoCore.class, result1.getClass());
        
        ReturnsEmptyValues result2 = LazyHolder.DELEGATE;
        assertEquals(ReturnsEmptyValues.class, result2.getClass());
    }
    
    private interface MyInterface {}
    
    private class MyClass implements MyInterface {}
    
    private class AnotherClass {}
}