import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        // Create a ReturnsDeepStubs object
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // Create a mock InvocationOnMock object
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        // Test the answer method
        Object result = returnsDeepStubs.answer(invocation);

        // Verify the result
        assertNotNull(result);
    }

    @Test
    public void testDeepStub() throws Throwable {
        // Create a ReturnsDeepStubs object
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // Create a mock InvocationOnMock object
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        // Create a GenericMetadataSupport object
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        // Test the deepStub method
        Object result = returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata);

        // Verify the result
        assertNotNull(result);
    }

    @Test
    public void testNewDeepStubMock() {
        // Create a ReturnsDeepStubs object
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // Create a GenericMetadataSupport object
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        // Test the newDeepStubMock method
        Object result = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);

        // Verify the result
        assertNotNull(result);
    }

    @Test
    public void testWithSettingsUsing() {
        // Create a ReturnsDeepStubs object
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // Create a GenericMetadataSupport object
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        // Test the withSettingsUsing method
        Object result = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);

        // Verify the result
        assertNotNull(result);
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        // Create a ReturnsDeepStubs object
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // Create a GenericMetadataSupport object
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        // Test the returnsDeepStubsAnswerUsing method
        Object result = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);

        // Verify the result
        assertNotNull(result);
    }

    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        // Create a ReturnsDeepStubs object
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // Create a mock InvocationContainerImpl object
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        // Create a mock object
        Object mock = mock(Object.class);

        // Test the recordDeepStubAnswer method
        Object result = returnsDeepStubs.recordDeepStubAnswer(mock, container);

        // Verify the result
        assertNotNull(result);
    }

    @Test
    public void testActualParameterizedType() {
        // Create a ReturnsDeepStubs object
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        // Create a mock object
        Object mock = mock(Object.class);

        // Test the actualParameterizedType method
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);

        // Verify the result
        assertNotNull(result);
    }

    @Test
    public void testReturnsDeepStubsSerializationFallback() {
        // Create a GenericMetadataSupport object
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        // Create a ReturnsDeepStubsSerializationFallback object
        ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback serializationFallback = new ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata);

        // Test the ReturnsDeepStubsSerializationFallback class
        assertNotNull(serializationFallback);
    }

    @Test
    public void testDeeplyStubbedAnswer() {
        // Create a mock object
        Object mock = mock(Object.class);

        // Create a DeeplyStubbedAnswer object
        ReturnsDeepStubs.DeeplyStubbedAnswer deeplyStubbedAnswer = new ReturnsDeepStubs.DeeplyStubbedAnswer(mock);

        // Test the DeeplyStubbedAnswer class
        assertNotNull(deeplyStubbedAnswer);
    }

    @Test
    public void testMockitoCore() {
        // Test the mockitoCore method
        assertNotNull(ReturnsDeepStubs.mockitoCore());
    }

    @Test
    public void testDelegate() {
        // Test the delegate method
        assertNotNull(ReturnsDeepStubs.delegate());
    }
}