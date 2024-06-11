import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Mock
    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAnswer() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object result = returnsDeepStubs.answer(invocation);
        Assert.assertNotNull(result);
    }

    @Test
    public void testDeepStub() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata)).thenReturn(new Object());
        Object result = returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata);
        Assert.assertNotNull(result);
        verify(returnsDeepStubs, times(1)).newDeepStubMock(eq(returnTypeGenericMetadata));
    }

    @Test
    public void testNewDeepStubMock() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        Object result = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);
        Assert.assertNotNull(result);
    }

    @Test
    public void testWithSettingsUsing() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        MockSettings mockSettings = mock(MockSettings.class);
        when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(true);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class<?>[]{Serializable.class});
        when(mockSettings.serializable()).thenReturn(mockSettings);
        when(mockSettings.defaultAnswer(any(ReturnsDeepStubs.class))).thenReturn(mockSettings);
        MockSettings result = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);
        Assert.assertEquals(mockSettings, result);
        verify(mockSettings, times(1)).extraInterfaces(eq(returnTypeGenericMetadata.rawExtraInterfaces()));
        verify(mockSettings, times(1)).serializable();
        verify(mockSettings, times(1)).defaultAnswer(any(ReturnsDeepStubs.class));
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        ReturnsDeepStubsSerializationFallback result = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
        Assert.assertNotNull(result);
        Assert.assertEquals(returnTypeGenericMetadata, result.returnTypeGenericMetadata);
    }

    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        Object mockObj = mock(Object.class);
        when(container.addAnswer(any(DeeplyStubbedAnswer.class), eq(false))).thenReturn(mockObj);

        Object result = returnsDeepStubs.recordDeepStubAnswer(mockObj, container);

        Assert.assertNotNull(result);
        Assert.assertEquals(mockObj, result);
        verify(container, times(1)).addAnswer(any(DeeplyStubbedAnswer.class), eq(false));
    }

    @Test
    public void testActualParameterizedType() {
        Object mockObj = mock(Object.class);
        CreationSettings creationSettings = mock(CreationSettings.class);
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        when(new MockUtil().getMockHandler(mockObj).getMockSettings()).thenReturn(creationSettings);
        when(creationSettings.getTypeToMock()).thenReturn(mockObj);
        when(returnsDeepStubs.actualParameterizedType(mockObj)).thenReturn(genericMetadataSupport);

        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mockObj);
        Assert.assertEquals(genericMetadataSupport, result);
        verify(creationSettings, times(1)).getTypeToMock();
    }

    @Test
    public void testReturnsDeepStubsSerializationFallback() throws IOException {
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        ReturnsDeepStubsSerializationFallback result = new ReturnsDeepStubsSerializationFallback(genericMetadataSupport);
        Assert.assertEquals(genericMetadataSupport, result.returnTypeGenericMetadata);
        Object replaceObj = result.writeReplace();
        Assert.assertEquals(Mockito.RETURNS_DEEP_STUBS, replaceObj);
    }

    @Test
    public void testDeeplyStubbedAnswer() throws Throwable {
        Object mockObj = mock(Object.class);
        DeeplyStubbedAnswer deeplyStubbedAnswer = new DeeplyStubbedAnswer(mockObj);
        Object result = deeplyStubbedAnswer.answer(mock(InvocationOnMock.class));
        Assert.assertEquals(mockObj, result);
    }

    @Test
    public void testMockitoCore() {
        MockitoCore result = ReturnsDeepStubs.mockitoCore();
        Assert.assertNotNull(result);
    }

    @Test
    public void testDelegate() {
        ReturnsEmptyValues result = ReturnsDeepStubs.delegate();
        Assert.assertNotNull(result);
    }

    private static class DeeplyStubbedAnswer implements Answer<Object>, Serializable {
        private final Object mock;

        DeeplyStubbedAnswer(Object mock) {
            this.mock = mock;
        }

        public Object answer(InvocationOnMock invocation) throws Throwable {
            return mock;
        }
    }
}