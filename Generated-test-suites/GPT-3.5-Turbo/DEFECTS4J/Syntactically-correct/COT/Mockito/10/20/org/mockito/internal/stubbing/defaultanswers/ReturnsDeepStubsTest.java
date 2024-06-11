import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs answer = new ReturnsDeepStubs();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object result = answer.answer(invocation);

        // Verify that the answer returns an object
        assert result != null;
    }

    @Test
    public void testDeepStub() throws Throwable {
        ReturnsDeepStubs answer = new ReturnsDeepStubs();

        InvocationOnMock invocation = mock(InvocationOnMock.class);

        // Stub the mockitoCore().isTypeMockable method to return false
        MockitoCore mockitoCore = mock(MockitoCore.class);
        when(mockitoCore.isTypeMockable(Mockito.any(Class.class))).thenReturn(false);
        when(answer.mockitoCore()).thenReturn(mockitoCore);

        Object result = answer.deepStub(invocation, mock(GenericMetadataSupport.class));

        // Verify that the answer returns the delegate().returnValueFor result
        assert result != null;
    }

    @Test
    public void testNewDeepStubMock() {
        ReturnsDeepStubs answer = new ReturnsDeepStubs();

        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        // Stub the mockitoCore().mock method to return a mock
        MockitoCore mockitoCore = mock(MockitoCore.class);
        Object expectedMock = new Object();
        when(mockitoCore.mock(Mockito.any(Class.class), Mockito.any(MockSettings.class))).thenReturn(expectedMock);
        when(answer.mockitoCore()).thenReturn(mockitoCore);

        Object mock = answer.newDeepStubMock(returnTypeGenericMetadata);

        // Verify that the answer returns the mock created by mockitoCore().mock
        assert mock == expectedMock;
    }

    @Test
    public void testWithSettingsUsing() {
        ReturnsDeepStubs answer = new ReturnsDeepStubs();

        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        // Stub the mockSettings.serializable and mockSettings.defaultAnswer methods to return a mock settings
        MockSettings mockSettings = mock(MockSettings.class);
        when(mockSettings.serializable()).thenReturn(mockSettings);
        when(mockSettings.defaultAnswer(Mockito.any(ReturnsDeepStubs.class))).thenReturn(mockSettings);
        when(answer.withSettings()).thenReturn(mockSettings);

        MockSettings result = answer.withSettingsUsing(returnTypeGenericMetadata);

        // Verify that the answer returns the mock settings created by mockSettings.serializable and mockSettings.defaultAnswer
        assert result == mockSettings;
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        ReturnsDeepStubs answer = new ReturnsDeepStubs();

        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        // Stub the ReturnsDeepStubsSerializationFallback constructor to return a ReturnsDeepStubsSerializationFallback object
        ReturnsDeepStubsSerializationFallback serializationFallback = mock(ReturnsDeepStubsSerializationFallback.class);
        when(serializationFallback.returnTypeGenericMetadata).thenReturn(returnTypeGenericMetadata);
        when(answer.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata)).thenReturn(serializationFallback);

        ReturnsDeepStubsSerializationFallback result = answer.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);

        // Verify that the answer returns the ReturnsDeepStubsSerializationFallback object created by the constructor
        assert result == serializationFallback;
    }

    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        ReturnsDeepStubs answer = new ReturnsDeepStubs();

        Object mock = mock(Object.class);

        // Stub the container.addAnswer method to store the mock
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(answer.recordDeepStubAnswer(mock, container)).thenCallRealMethod();

        Object result = answer.recordDeepStubAnswer(mock, container);

        // Verify that the answer returns the stored mock
        assert result == mock;
        verify(container).addAnswer(Mockito.any(DeeplyStubbedAnswer.class), Mockito.eq(false));
    }

    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs answer = new ReturnsDeepStubs();

        Object mock = mock(Object.class);

        // Stub the getMockHandler, getMockSettings, and getTypeToMock methods to return a mock settings
        MockUtil mockUtil = mock(MockUtil.class);
        InternalMockHandler mockHandler = mock(InternalMockHandler.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        Class typeToMock = Object.class;
        when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);
        when(mockHandler.getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(typeToMock);
        when(answer.actualParameterizedType(mock)).thenCallRealMethod();

        GenericMetadataSupport result = answer.actualParameterizedType(mock);

        // Verify that the answer returns the generic metadata support inferred from the mock settings
        assert result != null;
        verify(mockUtil).getMockHandler(mock);
        verify(mockHandler).getMockSettings();
        verify(mockSettings).getTypeToMock();
    }
}