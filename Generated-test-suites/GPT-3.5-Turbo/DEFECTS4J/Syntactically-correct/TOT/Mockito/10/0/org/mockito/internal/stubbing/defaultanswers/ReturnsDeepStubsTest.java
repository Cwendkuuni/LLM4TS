import org.junit.Test;
import org.junit.Assert;

public class ReturnsDeepStubsTest {

    // Tests for answer() method
    @Test
    public void testAnswer_typicalCase() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object result = returnsDeepStubs.answer(null);
        Assert.assertNotNull(result);
    }
    
    // Tests for deepStub() method
    @Test
    public void testDeepStub_invocationMatches() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = new Object();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        
        Mockito.when(invocation.getMock()).thenReturn(mock);
        
        Object result = returnsDeepStubs.deepStub(invocation, null);
        
        Assert.assertEquals(mock, result);
    }
    
    @Test
    public void testDeepStub_invocationNotMatches() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = new Object();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        
        Mockito.when(invocation.getMock()).thenReturn(mock);
        
        Object result = returnsDeepStubs.deepStub(invocation, null);
        
        Assert.assertNotEquals(mock, result);
    }
    
    // Tests for newDeepStubMock() method
    @Test
    public void testNewDeepStubMock_rawTypeMockable() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        Object mock = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);
        
        Assert.assertNotNull(mock);
    }
    
    @Test
    public void testNewDeepStubMock_rawTypeNotMockable() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        
        Mockito.when(returnTypeGenericMetadata.rawType()).thenReturn(Void.class);
        
        Object mock = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);
        
        Assert.assertNull(mock);
    }
    
    // Tests for withSettingsUsing() method
    @Test
    public void testWithSettingsUsing_hasRawExtraInterfaces() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        
        Mockito.when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(true);
        Mockito.when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class<?>[] {Serializable.class});
        
        Object result = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);
        
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testWithSettingsUsing_noRawExtraInterfaces() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        
        Mockito.when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(false);
        
        Object result = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);
        
        Assert.assertNotNull(result);
    }
    
    // Tests for returnsDeepStubsAnswerUsing() method
    @Test
    public void testReturnsDeepStubsAnswerUsing_typicalCase() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        Object result = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
        
        Assert.assertNotNull(result);
    }
    
    // Tests for recordDeepStubAnswer() method
    @Test
    public void testRecordDeepStubAnswer_typicalCase() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
        Object mock = new Object();
        
        Mockito.when(container.addAnswer(Mockito.any(), Mockito.anyBoolean())).thenReturn(null);
        
        Object result = returnsDeepStubs.recordDeepStubAnswer(mock, container);
        
        Assert.assertNotNull(result);
    }
    
    // Tests for actualParameterizedType() method
    @Test
    public void testActualParameterizedType_typicalCase() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = new Object();
        CreationSettings mockSettings = Mockito.mock(CreationSettings.class);
        GenericMetadataSupport genericMetadataSupport = Mockito.mock(GenericMetadataSupport.class);
        
        Mockito.when(new MockUtil().getMockHandler(mock)).thenReturn(mockSettings);
        Mockito.when(mockSettings.getTypeToMock()).thenReturn(Mockito.anyObject());
        Mockito.when(GenericMetadataSupport.inferFrom(Mockito.anyObject())).thenReturn(genericMetadataSupport);
        
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);
        
        Assert.assertNotNull(result);
    }
    
    // Tests for ReturnsDeepStubsSerializationFallback class
    @Test
    public void testReturnsDeepStubsSerializationFallback_writeReplace() throws IOException {
        ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback fallback = 
            new ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback(Mockito.mock(GenericMetadataSupport.class));
        
        Assert.assertNotNull(fallback.writeReplace());
    }
    
    // Tests for DeeplyStubbedAnswer class
    @Test
    public void testDeeplyStubbedAnswer_answer() throws Throwable {
        Object mock = new Object();
        ReturnsDeepStubs.DeeplyStubbedAnswer answer = new ReturnsDeepStubs.DeeplyStubbedAnswer(mock);
        Object result = answer.answer(Mockito.mock(InvocationOnMock.class));
        
        Assert.assertEquals(mock, result);
    }

}