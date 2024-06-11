import org.junit.Assert;
import org.junit.Test;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        Object result = returnsDeepStubs.answer(invocation);

        // Add assertions here

    }

    @Test
    public void testDeepStub() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);

        Object result = returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata);

        // Add assertions here

    }

    @Test
    public void testNewDeepStubMock() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        Object parentMock = Mockito.mock(Object.class);

        Object result = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata, parentMock);

        // Add assertions here

    }

    @Test
    public void testWithSettingsUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);

        MockSettings mockSettings = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);

        // Add assertions here

    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);

        ReturnsDeepStubs result = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);

        // Add assertions here

    }

    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        Object mock = Mockito.mock(Object.class);
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);

        Object result = returnsDeepStubs.recordDeepStubAnswer(mock, container);

        // Add assertions here

    }

    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        Object mock = Mockito.mock(Object.class);

        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);

        // Add assertions here

    }

    @Test
    public void testReturnsDeepStubsSerializationFallback() {
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);

        ReturnsDeepStubsSerializationFallback returnsDeepStubsSerializationFallback = new ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata);

        GenericMetadataSupport result = returnsDeepStubsSerializationFallback.actualParameterizedType(Mockito.mock(Object.class));

        // Add assertions here

    }

    @Test
    public void testWriteReplace() throws Throwable {
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);

        ReturnsDeepStubsSerializationFallback returnsDeepStubsSerializationFallback = new ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata);

        Object result = returnsDeepStubsSerializationFallback.writeReplace();

        // Add assertions here

    }

    @Test
    public void testDeeplyStubbedAnswer() throws Throwable {
        Object mock = Mockito.mock(Object.class);

        DeeplyStubbedAnswer deeplyStubbedAnswer = new DeeplyStubbedAnswer(mock);

        Object result = deeplyStubbedAnswer.answer(Mockito.mock(InvocationOnMock.class));

        // Add assertions here

    }

    @Test
    public void testMockitoCore() {
        MockitoCore mockitoCore = LazyHolder.MOCKITO_CORE;

        // Add assertions here

    }

    @Test
    public void testDelegate() {
        ReturnsEmptyValues delegate = LazyHolder.DELEGATE;

        // Add assertions here

    }
}