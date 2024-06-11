import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {
    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Test for raw type that cannot be mocked
        when(invocation.getMock()).thenReturn(new Object());
        assertNull(returnsDeepStubs.answer(invocation));
        
        // Test for raw type that can be mocked
        when(invocation.getMock()).thenReturn(new ReturnsEmptyValues());
        assertEquals(new ReturnsEmptyValues().returnValueFor(Object.class), returnsDeepStubs.answer(invocation));
        
        // Test for generic type that can be mocked
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(invocation.getMock()).thenReturn(newDeepStubMock(returnTypeGenericMetadata));
        assertEquals(mock(returnTypeGenericMetadata.rawType()), returnsDeepStubs.answer(invocation));
    }
    
    @Test
    public void testNewDeepStubMock() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        assertEquals(mock(returnTypeGenericMetadata.rawType()), returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata));
    }
    
    @Test
    public void testWithSettingsUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        assertEquals(withSettings().serializable().defaultAnswer(returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata)), 
                     returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata));
    }
    
    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        ReturnsDeepStubsSerializationFallback expected = new ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata);
        assertEquals(expected, returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata));
    }
    
    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        Object mock = new Object();
        returnsDeepStubs.recordDeepStubAnswer(mock, container);
        assertEquals(mock, container.getAnswerForStubbing().get().getAnswer().answer(null));
    }
    
    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        MockUtil mockUtil = mock(MockUtil.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        
        when(mockUtil.getMockHandler(mockSettings)).thenReturn(mock(InternalMockHandler.class));
        when(mockSettings.getTypeToMock()).thenReturn(Object.class);
        when(genericMetadataSupport.inferFrom(Object.class)).thenReturn(genericMetadataSupport);
        assertEquals(genericMetadataSupport, returnsDeepStubs.actualParameterizedType(mockSettings));
    }
    
    private Object newDeepStubMock(GenericMetadataSupport returnTypeGenericMetadata) {
        return mock(returnTypeGenericMetadata.rawType());
    }
}