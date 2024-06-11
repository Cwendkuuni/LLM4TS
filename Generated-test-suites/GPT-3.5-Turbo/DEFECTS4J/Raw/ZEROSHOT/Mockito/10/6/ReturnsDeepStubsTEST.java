import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Stubber;
import java.util.Map;
import java.util.Set;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {
    
    @Test
    public void testAnswer() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        try {
            returnsDeepStubs.answer(invocation);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        
        verify(invocation).getMock();
        verify(invocation).getMethod();
    }
    
    @Test
    public void testDeepStub() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        try {
            GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
            when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
            when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(false);
            when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(null);
            when(returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata)).thenReturn(null);
            when(returnsDeepStubs.recordDeepStubAnswer(null, null)).thenReturn(null);
            returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    
    @Test
    public void testNewDeepStubMock() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata)).thenReturn(mock(MockSettings.class));
        when(returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata)).thenCallRealMethod();
        
        Object mock = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);
        
        assertNotNull(mock);
        verify(returnTypeGenericMetadata).rawType();
    }
    
    @Test
    public void testWithSettingsUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(false);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(null);
        when(returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata)).thenReturn(mock(ReturnsDeepStubs.class));
        when(returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata)).thenCallRealMethod();
        
        MockSettings mockSettings = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);
        
        assertNotNull(mockSettings);
        verify(mockSettings).serializable();
        verify(mockSettings).defaultAnswer(any());
    }
    
    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata)).thenCallRealMethod();
        
        ReturnsDeepStubsSerializationFallback serializationFallback = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
        
        assertNotNull(serializationFallback);
        verify(serializationFallback).ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata);
    }
    
    @Test
    public void testRecordDeepStubAnswer() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(returnsDeepStubs.recordDeepStubAnswer(any(), any())).thenCallRealMethod();
        
        try {
            returnsDeepStubs.recordDeepStubAnswer(null, container);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        
        verify(container).addAnswer(any(), eq(false));
    }
    
    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        MockUtil mockUtil = mock(MockUtil.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(mockUtil.getMockHandler(any())).thenReturn(mock(InternalMockHandler.class));
        when(mockUtil.getMockHandler(any()).getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(Object.class);
        when(returnsDeepStubs.actualParameterizedType(any())).thenCallRealMethod();
        GenericMetadataSupport metadataSupport = mock(GenericMetadataSupport.class);
        when(GenericMetadataSupport.inferFrom(any())).thenReturn(metadataSupport);
        
        returnsDeepStubs.actualParameterizedType(null);
        
        verify(mockUtil).getMockHandler(null);
        verify(mockUtil.getMockHandler(null)).getMockSettings();
        verify(mockSettings).getTypeToMock();
        verify(GenericMetadataSupport).inferFrom(Object.class);
    }
    
    @Test
    public void testReturnsDeepStubsSerializationFallback() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(false);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(null);
        ReturnsDeepStubsSerializationFallback serializationFallback = new ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata);
        serializationFallback.writeReplace();
    }
    
    @Test
    public void testDeeplyStubbedAnswer() {
        Object mock = mock(Object.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        DeeplyStubbedAnswer answer = new DeeplyStubbedAnswer(mock);
        
        try {
            answer.answer(invocation);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        
        verify(answer).answer(invocation);
    }
    
    @Test
    public void testMockitoCore() {
        LazyHolder.MOCKITO_CORE.instance();
    }
    
    @Test
    public void testDelegate() {
        LazyHolder.DELEGATE.instance();
    }
    
}