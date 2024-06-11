import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        // Setup
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        // Test 1
        when(invocation.getMethod()).thenReturn(null);
        assertNull(returnsDeepStubs.answer(invocation));

        // Test 2
        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethod("toString"));
        assertEquals("", returnsDeepStubs.answer(invocation));

        // Test 3
        when(invocation.getMethod()).thenReturn(ReturnsDeepStubs.class.getDeclaredMethod("deepStub", InvocationOnMock.class, GenericMetadataSupport.class));
        assertEquals("", returnsDeepStubs.answer(invocation));
        
        // Test 4
        when(invocation.getMethod()).thenReturn(ReturnsDeepStubs.class.getDeclaredMethod("actualParameterizedType", Object.class));
        assertEquals(new GenericMetadataSupport(), returnsDeepStubs.answer(invocation));
    }

    @Test
    public void testDeepStub() throws Throwable {
        // Setup
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        // Test 1
        when(invocation.getMock()).thenReturn(null);
        assertEquals(null, returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata));

        // Test 2
        when(invocation.getMock()).thenReturn(ReturnsDeepStubs.class.getDeclaredConstructor().newInstance());
        assertEquals(null, returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata));

        // Test 3
        when(invocation.getMock()).thenReturn(new Object());
        assertEquals(null, returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata));
    }

    @Test
    public void testNewDeepStubMock() throws Throwable {
        // Setup
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        Object parentMock = mock(Object.class);

        // Test 1
        when(returnTypeGenericMetadata.rawType()).thenReturn(null);
        assertEquals(null, returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata, parentMock));

        // Test 2
        when(returnTypeGenericMetadata.rawType()).thenReturn(ReturnsDeepStubs.class);
        assertEquals(null, returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata, parentMock));

        // Test 3
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        assertEquals(null, returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata, parentMock));
    }

    @Test
    public void testWithSettingsUsing() {
        // Setup
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        MockSettings mockSettings = mock(MockSettings.class);

        // Test 1
        when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(false);
        assertEquals(mockSettings.serializable().defaultAnswer(returnsDeepStubs), returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata));

        // Test 2
        when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(true);
        assertEquals(mockSettings.extraInterfaces(returnTypeGenericMetadata.rawExtraInterfaces()).serializable().defaultAnswer(returnsDeepStubs), returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata));
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        // Setup
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        ReturnsDeepStubsSerializationFallback returnsDeepStubsSerializationFallback = mock(ReturnsDeepStubsSerializationFallback.class);

        // Test 1
        assertEquals(returnsDeepStubsSerializationFallback, returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata));
    }

    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        // Setup
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        // Test 1
        assertEquals(mock, returnsDeepStubs.recordDeepStubAnswer(mock, container));
    }

    @Test
    public void testActualParameterizedType() {
        // Setup
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);

        // Test 1
        assertEquals(new GenericMetadataSupport(), returnsDeepStubs.actualParameterizedType(mock));
    }
}