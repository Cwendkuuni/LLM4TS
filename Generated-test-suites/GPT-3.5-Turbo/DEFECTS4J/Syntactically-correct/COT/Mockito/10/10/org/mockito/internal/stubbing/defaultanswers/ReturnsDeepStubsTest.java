
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        Object result = returnsDeepStubs.answer(invocation);
        
        Assert.assertNotNull(result);
    }

    @Test
    public void testDeepStub() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        
        GenericMetadataSupport genericMetadata = Mockito.mock(GenericMetadataSupport.class);
        Mockito.when(invocation.getMock()).thenReturn(genericMetadata);

        Object result = returnsDeepStubs.deepStub(invocation, genericMetadata);
        
        Assert.assertNotNull(result);
    }

    @Test
    public void testNewDeepStubMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport genericMetadata = Mockito.mock(GenericMetadataSupport.class);

        Object result = returnsDeepStubs.newDeepStubMock(genericMetadata);
        
        Assert.assertNotNull(result);
    }

    @Test
    public void testWithSettingsUsing() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport genericMetadata = Mockito.mock(GenericMetadataSupport.class);

        Object result = returnsDeepStubs.withSettingsUsing(genericMetadata);
        
        Assert.assertNotNull(result);
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport genericMetadata = Mockito.mock(GenericMetadataSupport.class);

        Object result = returnsDeepStubs.returnsDeepStubsAnswerUsing(genericMetadata);
        
        Assert.assertNotNull(result);
    }

    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = Mockito.mock(Object.class);
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);

        Object result = returnsDeepStubs.recordDeepStubAnswer(mock, container);
        
        Assert.assertNotNull(result);
    }

    @Test
    public void testActualParameterizedType() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = Mockito.mock(Object.class);

        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);
        
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testReturnsDeepStubsSerializationFallback() throws Throwable {
        GenericMetadataSupport genericMetadata = Mockito.mock(GenericMetadataSupport.class);
        
        ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback returnsDeepStubsSerializationFallback = 
                new ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback(genericMetadata);
        
        Assert.assertNotNull(returnsDeepStubsSerializationFallback);
    }
    
    @Test
    public void testDeeplyStubbedAnswer() throws Throwable {
        Object mock = Mockito.mock(Object.class);
        
        ReturnsDeepStubs.DeeplyStubbedAnswer deeplyStubbedAnswer = new ReturnsDeepStubs.DeeplyStubbedAnswer(mock);
        
        Assert.assertNotNull(deeplyStubbedAnswer);
    }
    
    @Test
    public void testMockitoCore() {
        MockitoCore mockitoCore = ReturnsDeepStubs.mockitoCore();
        
        Assert.assertNotNull(mockitoCore);
    }
    
    @Test
    public void testDelegate() {
        ReturnsEmptyValues delegate = ReturnsDeepStubs.delegate();
        
        Assert.assertNotNull(delegate);
    }
}

