import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.GenericMetadataSupport;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.invocation.InvocationOnMock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        // Prepare
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Execute
        Object result = returnsDeepStubs.answer(invocation);
        
        // Verify
        assertNull(result);
    }
    
    @Test
    public void testDeepStub() throws Throwable {
        // Prepare
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        
        // Execute
        Object result = returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata);
        
        // Verify
        assertNull(result);
    }
    
    @Test
    public void testNewDeepStubMock() throws Throwable {
        // Prepare
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        Object parentMock = mock(Object.class);
        
        // Execute
        Object result = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata, parentMock);
        
        // Verify
        assertNotNull(result);
    }
    
    @Test
    public void testWithSettingsUsing() {
        // Prepare
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        
        // Execute
        MockSettings result = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);
        
        // Verify
        assertNotNull(result);
    }
    
    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        // Prepare
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        
        // Execute
        ReturnsDeepStubs result = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
        
        // Verify
        assertNotNull(result);
    }
    
    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        // Prepare
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        
        // Execute
        Object result = returnsDeepStubs.recordDeepStubAnswer(mock, container);
        
        // Verify
        assertNotNull(result);
    }
    
    @Test
    public void testActualParameterizedType() {
        // Prepare
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        
        // Execute
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);
        
        // Verify
        assertNotNull(result);
    }
    
    @Test
    public void testReturnsDeepStubsSerializationFallback() throws Throwable {
        // Prepare
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback serializationFallback = new ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata);
        
        // Execute
        Object result = serializationFallback.writeReplace();
        
        // Verify
        assertNotNull(result);
    }
    
    @Test
    public void testDeeplyStubbedAnswer() throws Throwable {
        // Prepare
        Object mock = mock(Object.class);
        ReturnsDeepStubs.DeeplyStubbedAnswer deeplyStubbedAnswer = new ReturnsDeepStubs.DeeplyStubbedAnswer(mock);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Execute
        Object result = deeplyStubbedAnswer.answer(invocation);
        
        // Verify
        assertNotNull(result);
    }

}