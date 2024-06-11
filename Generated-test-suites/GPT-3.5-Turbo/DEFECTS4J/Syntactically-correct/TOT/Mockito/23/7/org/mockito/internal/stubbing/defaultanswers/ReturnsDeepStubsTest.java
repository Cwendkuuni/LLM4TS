import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        Object answer = returnsDeepStubs.answer(invocation);
        
        assertNotNull(answer);
        assertTrue(answer instanceof Object);
    }
    
    @Test
    public void testGetMockShouldReturnMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        Object mock = returnsDeepStubs.getMock(invocation, null);
        
        assertNotNull(mock);
        assertTrue(mock instanceof Object);
    }
    
    @Test
    public void testCreateNewDeepStubMockShouldReturnMock() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        
        Object mock = returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);
        
        assertNotNull(mock);
        assertTrue(mock instanceof Object);
    }
    
    @Test
    public void testWithSettingsUsingShouldReturnMockSettings() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        
        ReturnsDeepStubs.MockSettings mockSettings = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);
        
        assertNotNull(mockSettings);
        assertTrue(mockSettings instanceof ReturnsDeepStubs.MockSettings);
    }
    
    @Test
    public void testReturnsDeepStubsAnswerUsingShouldReturnReturnsDeepStubsObject() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        
        ReturnsDeepStubs innerReturnsDeepStubs = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
        
        assertNotNull(innerReturnsDeepStubs);
        assertTrue(innerReturnsDeepStubs instanceof ReturnsDeepStubs);
    }
    
    @Test
    public void testRecordDeepStubMockShouldReturnMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        
        Object recordedMock = returnsDeepStubs.recordDeepStubMock(mock, null);
        
        assertNotNull(recordedMock);
        assertTrue(recordedMock instanceof Object);
    }
    
    @Test
    public void testActualParameterizedTypeShouldReturnGenericMetadataSupport() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        
        GenericMetadataSupport genericMetadataSupport = returnsDeepStubs.actualParameterizedType(mock);
        
        assertNotNull(genericMetadataSupport);
        assertTrue(genericMetadataSupport instanceof GenericMetadataSupport);
    }
}