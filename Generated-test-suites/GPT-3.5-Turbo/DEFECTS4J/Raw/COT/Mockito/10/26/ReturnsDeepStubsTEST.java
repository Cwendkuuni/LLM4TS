
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ReturnsDeepStubsTest {
    
    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Test scenario 1: Raw type is not mockable
        when(invocation.getMock()).thenReturn("string mock");
        when(invocation.getMethod()).thenReturn(String.class.getMethod("length"));
        Object result1 = returnsDeepStubs.answer(invocation);
        assertEquals("string mock".length(), result1);
        
        // Test scenario 2: Raw type is mockable and invocation matches stubbed invocation
        when(invocation.getMock()).thenReturn(Mockito.mock(String.class));
        when(invocation.getMethod()).thenReturn(String.class.getMethod("length"));
        when(invocation.getInvocation()).thenReturn(invocation);
        Object result2 = returnsDeepStubs.answer(invocation);
        assertEquals(null, result2); // return null since the stubbed invocation is not found
        
        // Test scenario 3: Raw type is mockable and invocation does not match stubbed invocation
        when(invocation.getInvocation()).thenReturn(mock(InvocationOnMock.class));
        Object result3 = returnsDeepStubs.answer(invocation);
        assertEquals(null, result3); // return null since the stubbed invocation is not found
    }
    
    @Test
    public void testDeepStub() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher1 = mock(StubbedInvocationMatcher.class);
        when(invocation.getMock()).thenReturn(Mockito.mock(String.class));
        when(invocation.getMethod()).thenReturn(String.class.getMethod("length"));
        when(returnTypeGenericMetadata.rawType()).thenReturn(String.class);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(container.getStubbedInvocations()).thenReturn(Arrays.asList(stubbedInvocationMatcher1));
        when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher1.getInvocation())).thenReturn(true);
        when(stubbedInvocationMatcher1.answer(invocation)).thenReturn(10);
        
        Object result = returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata);
        assertEquals(10, result);
    }
    
    @Test
    public void testNewDeepStubMock() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(true);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class[]{Serializable.class});
        when(returnTypeGenericMetadata.rawType()).thenReturn(String.class);
        
        Object result = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);
        assertEquals(String.class, result.getClass());
    }
    
    @Test
    public void testWithSettingsUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(true);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class[]{Serializable.class});
        when(returnTypeGenericMetadata.rawType()).thenReturn(String.class);
        
        MockSettings mockSettings = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);
        
        assertTrue(mockSettings.isSerializable());
        assertTrue(mockSettings.hasExtraInterfaces());
    }
    
    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(String.class);
        
        ReturnsDeepStubs result = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
        assertEquals(returnTypeGenericMetadata, result.actualParameterizedType(null));
    }
    
    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(new ArrayList<>());
        Object mock = Mockito.mock(String.class);
        
        Object result = returnsDeepStubs.recordDeepStubAnswer(mock, container);
        assertEquals(mock, result);
    }
    
    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        MockUtil mockUtil = mock(MockUtil.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(mockUtil.getMockHandler(Mockito.any())).thenReturn(mock(InternalMockHandler.class));
        when(mockSettings.getTypeToMock()).thenReturn(String.class);
        when(returnsDeepStubs.actualParameterizedType(null)).thenReturn(mock(GenericMetadataSupport.class));
        ReturnsEmptyValues delegate = mock(ReturnsEmptyValues.class);
        LazyHolder.MOCKITO_CORE = mock(MockitoCore.class);
        LazyHolder.DELEGATE = delegate;
        when(mockSettings.getTypeToMock()).thenReturn(String.class);
        
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(null);
        assertNotNull(result);
    }
    
    @Test
    public void testReturnsDeepStubsSerializationFallback() throws IOException {
        ReturnsDeepStubs.GenericMetadataSupport returnTypeGenericMetadata = mock(ReturnsDeepStubs.GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(String.class);
        ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback returnsDeepStubsSerializationFallback = new ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata);
        
        Object result = returnsDeepStubsSerializationFallback.writeReplace();
        assertEquals(Mockito.RETURNS_DEEP_STUBS, result);
    }
    
    @Test
    public void testDeeplyStubbedAnswer() throws Throwable {
        ReturnsDeepStubs.DeeplyStubbedAnswer deeplyStubbedAnswer = new ReturnsDeepStubs.DeeplyStubbedAnswer(Mockito.mock(String.class));
        
        Object result = deeplyStubbedAnswer.answer(null);
        assertEquals(Mockito.mock(String.class), result);
    }
    
    @Test
    public void testMockitoCore() {
        MockitoCore result = ReturnsDeepStubs.mockitoCore();
        assertNotNull(result);
    }
    
    @Test
    public void testDelegate() {
        ReturnsEmptyValues result = ReturnsDeepStubs.delegate();
        assertNotNull(result);
    }
}
