import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setUp() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswer() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        Object result = returnsDeepStubs.answer(invocation);

        assertNotNull(result);
    }

    @Test
    public void testDeepStub() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        Object result = returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata);

        assertNotNull(result);
    }

    @Test
    public void testNewDeepStubMock() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        Object result = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);

        assertNotNull(result);
    }

    @Test
    public void testWithSettingsUsing() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        Object result = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);

        assertNotNull(result);
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        Object result = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);

        assertNotNull(result);
    }

    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        Object mock = mock(Object.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        Object result = returnsDeepStubs.recordDeepStubAnswer(mock, container);

        assertNotNull(result);
    }

    @Test
    public void testActualParameterizedType() {
        Object mock = mock(Object.class);

        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);

        assertNotNull(result);
    }

    @Test
    public void testReturnsDeepStubsSerializationFallback() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        ReturnsDeepStubsSerializationFallback result = new ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata);

        assertNotNull(result);
    }

    @Test
    public void testDeeplyStubbedAnswer() {
        Object mock = mock(Object.class);

        DeeplyStubbedAnswer result = new ReturnsDeepStubs.DeeplyStubbedAnswer(mock);

        assertNotNull(result);
    }

    @Test
    public void testMockitoCore() {
        MockitoCore result = ReturnsDeepStubs.mockitoCore();

        assertNotNull(result);
    }

    @Test
    public void testDelegate() {
        ReturnsEmptyValues result = ReturnsDeepStubs.delegate();

        assertNotNull(result);
    }
}