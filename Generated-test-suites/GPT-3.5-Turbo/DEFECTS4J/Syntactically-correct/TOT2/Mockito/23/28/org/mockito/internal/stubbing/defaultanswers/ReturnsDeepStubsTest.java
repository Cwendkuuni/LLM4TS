import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswerMethod() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        Object result = returnsDeepStubs.answer(mock(InvocationOnMock.class));
        
        assertNotNull(result);
    }

    @Test
    public void testGetMockMethod() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        Object result = returnsDeepStubs.getMock(mock(InvocationOnMock.class), new GenericMetadataSupport());
        
        assertNotNull(result);
    }

    @Test
    public void testCreateNewDeepStubMockMethod() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        Object result = returnsDeepStubs.createNewDeepStubMock(new GenericMetadataSupport());
        
        assertNotNull(result);
    }

    @Test
    public void testWithSettingsUsingMethod() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        MockSettings mockSettings = returnsDeepStubs.withSettingsUsing(new GenericMetadataSupport());
        
        assertNotNull(mockSettings);
    }

    @Test
    public void testReturnsDeepStubsAnswerUsingMethod() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        ReturnsDeepStubs result = returnsDeepStubs.returnsDeepStubsAnswerUsing(new GenericMetadataSupport());
        
        assertNotNull(result);
    }

    @Test
    public void testRecordDeepStubMockMethod() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        Object result = returnsDeepStubs.recordDeepStubMock(new Object(), new InvocationContainerImpl());
        
        assertNotNull(result);
    }

    @Test
    public void testActualParameterizedTypeMethod() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(new Object());
        
        assertNotNull(result);
    }
}