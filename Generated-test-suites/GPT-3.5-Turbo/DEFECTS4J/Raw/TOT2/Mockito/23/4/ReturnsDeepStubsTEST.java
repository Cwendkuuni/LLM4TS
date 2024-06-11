import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        Object answer = returnsDeepStubs.answer(invocation);
        
        assertNull(answer);
    }

    @Test
    public void testGetMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        Object mock = returnsDeepStubs.getMock(invocation, null);
        
        assertNotNull(mock);
    }

    @Test
    public void testCreateNewDeepStubMock() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        
        Object mock = returnsDeepStubs.createNewDeepStubMock(genericMetadataSupport);
        
        assertNotNull(mock);
    }

    @Test
    public void testWithSettingsUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        
        MockSettings mockSettings = returnsDeepStubs.withSettingsUsing(genericMetadataSupport);
        
        assertNotNull(mockSettings);
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        
        ReturnsDeepStubs stubsAnswer = returnsDeepStubs.returnsDeepStubsAnswerUsing(genericMetadataSupport);
        
        assertNotNull(stubsAnswer);
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        
        Object result = returnsDeepStubs.recordDeepStubMock(mock, container);
        
        assertEquals(mock, result);
    }

    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        
        GenericMetadataSupport genericMetadataSupport = returnsDeepStubs.actualParameterizedType(mock);
        
        assertNotNull(genericMetadataSupport);
    }
}