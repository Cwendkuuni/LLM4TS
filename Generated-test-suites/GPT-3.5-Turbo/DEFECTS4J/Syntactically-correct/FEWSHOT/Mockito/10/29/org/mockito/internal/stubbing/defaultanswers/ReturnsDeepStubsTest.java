import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        when(invocation.getMock()).thenReturn("mock");
        assertEquals("mock", returnsDeepStubs.answer(invocation));

        when(invocation.getMock()).thenReturn(123);
        assertEquals(123, returnsDeepStubs.answer(invocation));

        when(invocation.getMock()).thenReturn(null);
        assertNull(returnsDeepStubs.answer(invocation));        
    }

    @Test
    public void testDeepStub() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);

        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(true);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn("mock");

        when(returnTypeGenericMetadata.rawType()).thenReturn(String.class);
        when(invocation.getMock()).thenReturn("mock");
        when(returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata)).thenReturn("mock");
        when(returnsDeepStubs.recordDeepStubAnswer("mock", container)).thenReturn("mock");

        assertEquals("mock", returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata));

        when(returnTypeGenericMetadata.rawType()).thenReturn(Integer.class);
        when(invocation.getMock()).thenReturn("mock");
        when(returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata)).thenReturn("mock");
        when(returnsDeepStubs.recordDeepStubAnswer("mock", container)).thenReturn("mock");

        assertEquals("mock", returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata));

        when(returnTypeGenericMetadata.rawType()).thenReturn(null);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(false);

        assertNull(returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata));        
    }

    @Test
    public void testNewDeepStubMock() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        when(returnTypeGenericMetadata.rawType()).thenReturn(String.class);
        when(returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata)).thenReturn(mock(MockSettings.class));
        when(returnsDeepStubs.mockitoCore().mock(returnTypeGenericMetadata.rawType(), mock(MockSettings.class))).thenReturn("mock");

        assertEquals("mock", returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata));
    }
    
    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        when(returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata)).thenReturn(mock(ReturnsDeepStubs.class));
        
        assertNotNull(returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata));
    }

    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        when(container.addAnswer(any(DeeplyStubbedAnswer.class), anyBoolean())).thenReturn("mock");
        
        assertEquals("mock", returnsDeepStubs.recordDeepStubAnswer("mock", container));
    }

    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);

        CreationSettings mockSettings = mock(CreationSettings.class);
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);

        when(new MockUtil().getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(Object.class);
        when(new MockUtil().getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        when(genericMetadataSupport.inferFrom(Object.class)).thenReturn(genericMetadataSupport);

        assertEquals(genericMetadataSupport, returnsDeepStubs.actualParameterizedType(mock));
    }

    @Test
    public void testReturnsDeepStubsSerializationFallback() throws IOException {
        ReturnsDeepStubsSerializationFallback returnsDeepStubsSerializationFallback = 
            new ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback(mock(GenericMetadataSupport.class));

        assertNotNull(returnsDeepStubsSerializationFallback);

        assertNotNull(returnsDeepStubsSerializationFallback.writeReplace());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDeeplyStubbedAnswer() throws Throwable {
        ReturnsDeepStubs.DeeplyStubbedAnswer deeplyStubbedAnswer = new ReturnsDeepStubs.DeeplyStubbedAnswer("mock");

        assertNotNull(deeplyStubbedAnswer);

        assertEquals("mock", deeplyStubbedAnswer.answer(mock(InvocationOnMock.class)));        
    }
}