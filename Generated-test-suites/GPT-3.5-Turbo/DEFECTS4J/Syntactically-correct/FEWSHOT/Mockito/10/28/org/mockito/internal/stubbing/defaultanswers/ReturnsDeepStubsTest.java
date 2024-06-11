import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {
    
    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Test case 1: Mock object is not mockable
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        when(invocation.getMock()).thenReturn(genericMetadataSupport);
        when(invocation.getMethod()).thenReturn(null);
        Object result1 = returnsDeepStubs.answer(invocation);
        assertNull(result1);
        
        // Test case 2: Mock object is mockable, stub exists for invocation
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        when(genericMetadataSupport.rawType()).thenReturn(Object.class);
        when(handler.getInvocationContainer()).thenReturn(new InvocationContainerImpl());
        when(handler.getInvocationContainer().getInvocationForStubbing().matches(Mockito.any())).thenReturn(true);
        when(handler.getInvocationContainer().getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(stubbedInvocationMatcher.answer(Mockito.any())).thenReturn(new Object());
        when(invocation.getMock()).thenReturn(handler);
        Object result2 = returnsDeepStubs.answer(invocation);
        assertNotNull(result2);
        assertEquals(Object.class, result2.getClass());
        
        // Test case 3: Mock object is mockable, no stub exists for invocation
        ReturnsDeepStubs deepStubs = Mockito.mock(ReturnsDeepStubs.class);
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        when(deepStubs.recordDeepStubAnswer(Mockito.any(), Mockito.any())).thenReturn(new Object());
        when(genericMetadataSupport.rawType()).thenReturn(Object.class);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(new ArrayList<>());
        when(deepStubs.newDeepStubMock(returnTypeGenericMetadata)).thenReturn(new Object());
        when(container.addAnswer(Mockito.any(), Mockito.anyBoolean())).thenReturn(null);
        when(invocation.getMock()).thenReturn(handler);
        Object result3 = deepStubs.answer(invocation);
        assertNotNull(result3);
        assertEquals(Object.class, result3.getClass());
    }
  
    @Test
    public void testNewDeepStubMock() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        when(genericMetadataSupport.rawType()).thenReturn(Object.class);
        Object result = returnsDeepStubs.newDeepStubMock(genericMetadataSupport);
        assertNotNull(result);
    }
  
    @Test
    public void testWithSettingsUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        when(genericMetadataSupport.hasRawExtraInterfaces()).thenReturn(false);
        when(genericMetadataSupport.rawExtraInterfaces()).thenReturn(null);
        MockSettings mockSettings = mock(MockSettings.class);
        when(mockSettings.serializable()).thenReturn(mockSettings);
        when(mockSettings.defaultAnswer(Mockito.any())).thenReturn(mockSettings);
        when(withSettings()).thenReturn(mockSettings);
        MockSettings settings = returnsDeepStubs.withSettingsUsing(genericMetadataSupport);
        assertNotNull(settings);
    }
  
    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        ReturnsDeepStubsSerializationFallback serializationFallback = Mockito.mock(ReturnsDeepStubsSerializationFallback.class);
        when(serializationFallback.returnsDeepStubsAnswerUsing(genericMetadataSupport)).thenReturn(serializationFallback);
        ReturnsDeepStubs result = returnsDeepStubs.returnsDeepStubsAnswerUsing(genericMetadataSupport);
        assertNotNull(result);
    }
  
    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        ReturnsDeepStubs deepStubs = Mockito.mock(ReturnsDeepStubs.class);
        Object mock = new Object();
        when(deepStubs.recordDeepStubAnswer(mock, container)).thenReturn(mock);
        Object result = returnsDeepStubs.recordDeepStubAnswer(mock, container);
        assertNotNull(result);
    }
  
    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(mockSettings.getTypeToMock()).thenReturn(Object.class);
        when(new MockUtil().getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        Object result = returnsDeepStubs.actualParameterizedType(mock);
        assertNotNull(result);
    }
  
    @Test
    public void testReturnsDeepStubsSerializationFallback() throws IOException {
        ReturnsDeepStubsSerializationFallback serializationFallback = new ReturnsDeepStubsSerializationFallback(mock(GenericMetadataSupport.class));
        Object result = serializationFallback.writeReplace();
        assertNotNull(result);
    }
  
    @Test
    public void testDeeplyStubbedAnswer() {
        ReturnsDeepStubs.DeeplyStubbedAnswer deeplyStubbedAnswer = new ReturnsDeepStubs.DeeplyStubbedAnswer(new Object());
        Object result = deeplyStubbedAnswer.answer(mock(InvocationOnMock.class));
        assertNotNull(result);
    }
  
    @Test
    public void testMockitoCore() {
        MockitoCore mockitoCore = ReturnsDeepStubs.mockitoCore();
        assertNotNull(mockitoCore);
    }
  
    @Test
    public void testDelegate() {
        ReturnsEmptyValues delegate = ReturnsDeepStubs.delegate();
        assertNotNull(delegate);
    }
}