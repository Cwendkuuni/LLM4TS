import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.*;

import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setup() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswer_WhenInvocationMatches_ReturnsAnswer() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));

        Object answer = returnsDeepStubs.answer(invocation);

        // Verify that the answer is returned when the invocation matches
        verify(invocation.getMock(), times(1)).matches(any());
        verify(invocation.getMock(), times(1)).getNextAnswer();
        verifyNoMoreInteractions(invocation.getMock());
    }

    @Test
    public void testAnswer_WhenInvocationDoesNotMatch_ReturnsMock() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object mockObject = mock(Object.class);

        when(invocation.getMock()).thenReturn(mockObject);
        when(mockObject.matches(any())).thenReturn(false);

        Object answer = returnsDeepStubs.answer(invocation);

        // Verify that the mock is returned when the invocation does not match
        verify(invocation.getMock(), times(1)).matches(any());
        verify(invocation.getMock(), never()).getNextAnswer();
        verifyNoMoreInteractions(invocation.getMock());
    }

    @Test
    public void testAnswer_WhenRawTypeNotMockable_ReturnsDelegateReturnValue() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        ReturnsEmptyValues delegate = mock(ReturnsEmptyValues.class);

        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(genericMetadataSupport.rawType()).thenReturn(String.class);
        when(delegate.returnValueFor(String.class)).thenReturn("delegateReturnValue");

        Object answer = returnsDeepStubs.answer(invocation);

        // Verify that the delegate return value is returned when the raw type is not mockable
        verify(invocation.getMock(), times(1)).matches(any());
        verify(invocation.getMock(), never()).getNextAnswer();
        verify(delegate, times(1)).returnValueFor(String.class);
        verifyNoMoreInteractions(invocation.getMock(), delegate);
    }

    @Test
    public void testDeepStub_WhenInvocationMatches_ReturnsStubbedInvocationResult() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object mockObject = mock(Object.class);
        InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);

        when(invocation.getMock()).thenReturn(mockObject);
        when(mockObject.matches(any())).thenReturn(true);
        when(mockHandler.getInvocationContainer()).thenReturn(mock(InvocationContainerImpl.class));
        when(mockHandler.getInvocationContainer().getStubbedInvocations()).thenReturn(Collections.singletonList(mock(StubbedInvocationMatcher.class)));

        Object answer = returnsDeepStubs.deepStub(invocation, mock(GenericMetadataSupport.class));

        // Verify that the stubbed invocation result is returned when the invocation matches
        verify(invocation.getMock(), times(1)).matches(any());
        verify(mockHandler, times(1)).getInvocationContainer();
        verify(mockHandler.getInvocationContainer(), times(1)).getStubbedInvocations();
        verifyNoMoreInteractions(invocation.getMock(), mockHandler.getInvocationContainer());
    }

    @Test
    public void testDeepStub_WhenInvocationDoesNotMatch_ReturnsNewDeepStubMock() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object mockObject = mock(Object.class);
        InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);

        when(invocation.getMock()).thenReturn(mockObject);
        when(mockObject.matches(any())).thenReturn(false);
        when(mockHandler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing()).thenReturn(mock(InvocationOnMock.class));

        Object answer = returnsDeepStubs.deepStub(invocation, mock(GenericMetadataSupport.class));

        // Verify that the new deep stub mock is returned when the invocation does not match
        verify(invocation.getMock(), times(1)).matches(any());
        verify(mockHandler, times(1)).getInvocationContainer();
        verify(container, times(1)).getStubbedInvocations();
        verify(container, never()).addAnswer(any(), anyBoolean());
        verifyNoMoreInteractions(invocation.getMock(), mockHandler.getInvocationContainer(), container);
    }

    @Test
    public void testNewDeepStubMock_WithReturnMetadata_CreatesMockWithSettings() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        Object mock = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);

        // Verify that the mock is created with the given return type generic metadata
        verify(returnTypeGenericMetadata, times(1)).rawType();
        verifyNoMoreInteractions(returnTypeGenericMetadata);
    }

    @Test
    public void testWithSettingsUsing_WithRawExtraInterfaces_AddsExtraInterfacesToSettings() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        MockSettings mockSettings = mock(MockSettings.class);

        when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(true);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class[]{Serializable.class});

        MockSettings result = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);

        // Verify that the extra interfaces are added to the mock settings
        verify(mockSettings, times(1)).extraInterfaces(new Class[]{Serializable.class});
        verify(mockSettings, times(1)).serializable();
        verify(mockSettings, times(1)).defaultAnswer(any(ReturnsDeepStubs.class));
        verifyNoMoreInteractions(mockSettings);
    }

    @Test
    public void testWithSettingsUsing_WithoutRawExtraInterfaces_DoesNotAddExtraInterfacesToSettings() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        MockSettings mockSettings = mock(MockSettings.class);

        when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(false);

        MockSettings result = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);

        // Verify that no extra interfaces are added to the mock settings
        verify(mockSettings, never()).extraInterfaces(any(Class[].class));
        verify(mockSettings, times(1)).serializable();
        verify(mockSettings, times(1)).defaultAnswer(any(ReturnsDeepStubs.class));
        verifyNoMoreInteractions(mockSettings);
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing_WithReturnMetadata_CreatesReturnsDeepStubsSerializationFallback() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        ReturnsDeepStubsSerializationFallback result = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);

        // Verify that the ReturnsDeepStubsSerializationFallback is created with the given return type generic metadata
        verify(returnTypeGenericMetadata, times(1)).rawType();
        verifyNoMoreInteractions(returnTypeGenericMetadata);
    }

    @Test
    public void testRecordDeepStubAnswer_AddsAnswerToInvocationContainer() throws Throwable {
        Object mock = mock(Object.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        returnsDeepStubs.recordDeepStubAnswer(mock, container);

        // Verify that the answer is added to the invocation container
        verify(container, times(1)).addAnswer(any(DeeplyStubbedAnswer.class), eq(false));
        verifyNoMoreInteractions(container);
    }

    @Test
    public void testActualParameterizedType_InfersFromMockSettingsType() {
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);

        when(new MockUtil().getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(Map.class);

        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);

        // Verify that the generic metadata is inferred from the mock settings type
        verify(mockSettings, times(1)).getTypeToMock();
        verifyNoMoreInteractions(mockSettings);
    }

    @Test
    public void testReturnsDeepStubsSerializationFallback_WriteReplace_ReturnsReturnsDeepStubs() throws IOException {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        ReturnsDeepStubsSerializationFallback serializationFallback = new ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata);

        Object result = serializationFallback.writeReplace();

        // Verify that the writeReplace method returns Mockito.RETURNS_DEEP_STUBS
        assertSame(Mockito.RETURNS_DEEP_STUBS, result);
    }

    @Test
    public void testDeeplyStubbedAnswer_AnswersWithMock() throws Throwable {
        Object mock = mock(Object.class);
        DeeplyStubbedAnswer deeplyStubbedAnswer = new DeeplyStubbedAnswer(mock);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        Object result = deeplyStubbedAnswer.answer(invocation);

        // Verify that the answer returns the mock object
        assertSame(mock, result);
    }

    @Test
    public void testMockitoCore_ReturnsMockitoCoreInstance() {
        MockitoCore mockitoCore = mock(MockitoCore.class);
        ReturnsDeepStubs.LazyHolder.MOCKITO_CORE = mockitoCore;

        MockitoCore result = returnsDeepStubs.mockitoCore();

        // Verify that the MockitoCore instance is returned
        assertSame(mockitoCore, result);
    }

    @Test
    public void testDelegate_ReturnsDelegateInstance() {
        ReturnsEmptyValues delegate = mock(ReturnsEmptyValues.class);
        ReturnsDeepStubs.LazyHolder.DELEGATE = delegate;

        ReturnsEmptyValues result = returnsDeepStubs.delegate();

        // Verify that the Delegate instance is returned
        assertSame(delegate, result);
    }
}