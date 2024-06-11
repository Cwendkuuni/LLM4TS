import org.junit.Test;
import static org.mockito.Mockito.*;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs obj = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        obj.answer(invocation);
    }
    
    @Test
    public void testDeepStub() throws Throwable {
        ReturnsDeepStubs obj = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        obj.deepStub(invocation, returnTypeGenericMetadata);
    }
    
    @Test
    public void testNewDeepStubMock() throws Throwable {
        ReturnsDeepStubs obj = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        Object parentMock = mock(Object.class);
        obj.newDeepStubMock(returnTypeGenericMetadata, parentMock);
    }
    
    @Test
    public void testWithSettingsUsing() throws Throwable {
        ReturnsDeepStubs obj = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        obj.withSettingsUsing(returnTypeGenericMetadata);
    }
    
    @Test
    public void testReturnsDeepStubsAnswerUsing() throws Throwable {
        ReturnsDeepStubs obj = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        obj.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
    }
    
    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        ReturnsDeepStubs obj = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        obj.recordDeepStubAnswer(mock, container);
    }
    
    @Test
    public void testActualParameterizedType() throws Throwable {
        ReturnsDeepStubs obj = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        obj.actualParameterizedType(mock);
    }
    
    @Test
    public void testReturnsDeepStubsSerializationFallback() throws Throwable {
        ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback obj = 
            new ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback(new GenericMetadataSupport());
        obj.writeReplace();
    }
    
    @Test
    public void testDeeplyStubbedAnswer() throws Throwable {
        ReturnsDeepStubs.DeeplyStubbedAnswer obj = 
            new ReturnsDeepStubs.DeeplyStubbedAnswer(new Object());
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        obj.answer(invocation);
    }
}