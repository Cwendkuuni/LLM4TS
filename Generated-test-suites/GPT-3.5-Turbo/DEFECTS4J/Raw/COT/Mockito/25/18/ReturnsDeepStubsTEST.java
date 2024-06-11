import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        
        // Create mock InvocationOnMock object
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Test when returnType cannot be mocked
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        when(genericMetadataSupport.rawType()).thenReturn(String.class);
        when(invocation.getMock()).thenReturn(genericMetadataSupport);
        Object result1 = deepStubs.answer(invocation);
        assertNull(result1);
        
        // Test when returnType can be mocked
        GenericMetadataSupport genericMetadataSupport2 = mock(GenericMetadataSupport.class);
        when(genericMetadataSupport2.rawType()).thenReturn(List.class);
        when(invocation.getMock()).thenReturn(genericMetadataSupport2);
        Object result2 = deepStubs.answer(invocation);
        assertNotNull(result2);
    }
}