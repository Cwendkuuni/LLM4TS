import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        Object result = returnsDeepStubs.answer(invocation);

        assertNull(result);
    }

    @Test
    public void testDeepStub() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);

        Object result = returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata);

        assertNull(result);
    }

    @Test
    public void testNewDeepStubMock() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);

        Object result = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);

        assertNull(result);
    }

    @Test
    public void testWithSettingsUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);

        Object result = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);

        assertNull(result);
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);

        Object result = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);

        assertNull(result);
    }

    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        Object mock = Mockito.mock(Object.class);
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);

        Object result = returnsDeepStubs.recordDeepStubAnswer(mock, container);

        assertNull(result);
    }

    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        Object mock = Mockito.mock(Object.class);
        GenericMetadataSupport genericMetadataSupport = Mockito.mock(GenericMetadataSupport.class);

        Object result = returnsDeepStubs.actualParameterizedType(mock);

        assertNull(result);
    }

    @Test
    public void testReturnsDeepStubsSerializationFallback() {
        ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback serializationFallback = new ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback();

        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);

        serializationFallback.returnTypeGenericMetadata = returnTypeGenericMetadata;

        Object result = serializationFallback.actualParameterizedType(Mockito.mock(Object.class));

        assertNull(result);
    }

    @Test
    public void testDeeplyStubbedAnswer() throws Throwable {
        ReturnsDeepStubs.DeeplyStubbedAnswer deeplyStubbedAnswer = new ReturnsDeepStubs.DeeplyStubbedAnswer(Mockito.mock(Object.class));

        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        Object result = deeplyStubbedAnswer.answer(invocation);

        assertNull(result);
    }

    @Test
    public void testMockitoCore() {
        ReturnsDeepStubs mock = Mockito.mock(ReturnsDeepStubs.class);

        MockitoCore mockitoCore = ReturnsDeepStubs.mockitoCore();

        assertNotNull(mockitoCore);
    }

    @Test
    public void testDelegate() {
        ReturnsDeepStubs mock = Mockito.mock(ReturnsDeepStubs.class);

        ReturnsEmptyValues delegate = ReturnsDeepStubs.delegate();

        assertNotNull(delegate);
    }

}