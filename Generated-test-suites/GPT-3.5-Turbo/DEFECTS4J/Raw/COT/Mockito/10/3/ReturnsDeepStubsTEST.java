
import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        assertNull(answer.answer(invocation));
    }

    @Test
    public void testDeepStub() throws Throwable {
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        assertNull(answer.deepStub(invocation, returnTypeGenericMetadata));
    }

    @Test
    public void testNewDeepStubMock() {
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        assertNotNull(answer.newDeepStubMock(returnTypeGenericMetadata));
    }

    @Test
    public void testWithSettingsUsing() {
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        assertNotNull(answer.withSettingsUsing(returnTypeGenericMetadata));
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        assertNotNull(answer.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata));
    }

    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        Object mock = mock(Object.class);

        assertNotNull(answer.recordDeepStubAnswer(mock, container));
    }

    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        Object mock = mock(Object.class);

        assertNotNull(answer.actualParameterizedType(mock));
    }

    @Test
    public void testReturnsDeepStubsSerializationFallback() {
        ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback fallback = new ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback(mock(GenericMetadataSupport.class));

        assertNotNull(fallback.writeReplace());
    }

    @Test
    public void testDeeplyStubbedAnswer() {
        ReturnsDeepStubs.DeeplyStubbedAnswer answer = new ReturnsDeepStubs.DeeplyStubbedAnswer(mock(Object.class));
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        assertNotNull(answer.answer(invocation));
    }

    @Test
    public void testMockitoCore() {
        assertNotNull(ReturnsDeepStubs.mockitoCore());
    }

    @Test
    public void testDelegate() {
        assertNotNull(ReturnsDeepStubs.delegate());
    }
}
