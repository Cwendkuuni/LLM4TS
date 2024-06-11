
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.MockSettings;
import org.mockito.Mockito;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object result = returnsDeepStubs.answer(invocation);
        assertNull(result);
    }

    @Test
    public void testDeepStub() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object result = returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata);
        assertNull(result);
    }

    @Test
    public void testNewDeepStubMock() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object result = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);
        assertNull(result);
    }

    @Test
    public void testWithSettingsUsing() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        MockSettings mockSettings = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);
        assertNotNull(mockSettings);
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        ReturnsDeepStubs actual = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
        assertNotNull(actual);
    }

    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        Object mock = mock(Object.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object result = returnsDeepStubs.recordDeepStubAnswer(mock, container);
        assertNotNull(result);
    }

    @Test
    public void testActualParameterizedType() {
        Object mock = mock(Object.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport actual = returnsDeepStubs.actualParameterizedType(mock);
        assertNull(actual);
    }

    @Test
    public void testReturnsDeepStubsSerializationFallback() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        ReturnsDeepStubsSerializationFallback returnsDeepStubsSerializationFallback = new ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata);
        assertNotNull(returnsDeepStubsSerializationFallback);
    }

    @Test
    public void testDeeplyStubbedAnswer() {
        Object mock = mock(Object.class);
        DeeplyStubbedAnswer deeplyStubbedAnswer = new DeeplyStubbedAnswer(mock);
        assertNotNull(deeplyStubbedAnswer);
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

