
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    // Test case for the answer() method
    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(mockitoCore().isTypeMockable(Object.class)).thenReturn(false);

        Object result = returnsDeepStubs.answer(invocation);

        verify(invocation, times(1)).getMock();
        verify(returnTypeGenericMetadata, times(1)).rawType();
        verify(mockitoCore(), times(1)).isTypeMockable(Object.class);
        verify(delegate(), times(1)).returnValueFor(Object.class);
        verifyNoMoreInteractions(invocation, returnTypeGenericMetadata, mockitoCore(), delegate());
    }

    // Test case for the deepStub() method with matching invocation
    @Test
    public void testDeepStubWithMatchingInvocation() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object mock = mock(Object.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(invocation.getMock()).thenReturn(mock);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(true);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn(mock);

        Object result = returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata);

        verify(invocation, times(1)).getMock();
        verify(handler, times(1)).getInvocationContainer();
        verify(container, times(1)).getStubbedInvocations();
        verify(container, times(1)).getInvocationForStubbing();
        verify(container.getInvocationForStubbing(), times(1)).matches(stubbedInvocationMatcher.getInvocation());
        verify(stubbedInvocationMatcher, times(1)).answer(invocation);
        verifyNoMoreInteractions(invocation, handler, container, stubbedInvocationMatcher);
    }

    // Test case for the deepStub() method without matching invocation
    @Test
    public void testDeepStubWithoutMatchingInvocation() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object mock = mock(Object.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(invocation.getMock()).thenReturn(mock);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(false);
        when(mockitoCore().mock(any(Class.class), any(MockSettings.class))).thenReturn(mock);

        Object result = returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata);

        verify(invocation, times(1)).getMock();
        verify(handler, times(1)).getInvocationContainer();
        verify(container, times(1)).getStubbedInvocations();
        verify(container, times(1)).getInvocationForStubbing();
        verify(container.getInvocationForStubbing(), times(1)).matches(stubbedInvocationMatcher.getInvocation());
        verify(mockitoCore(), times(1)).mock(any(Class.class), any(MockSettings.class));
        verifyNoMoreInteractions(invocation, handler, container, stubbedInvocationMatcher);
    }

    // Test case for the newDeepStubMock() method with raw extra interfaces
    @Test
    public void testNewDeepStubMockWithRawExtraInterfaces() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(true);
        Class<?>[] rawExtraInterfaces = new Class[]{Serializable.class};
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(rawExtraInterfaces);
        when(mockitoCore().mock(
                returnTypeGenericMetadata.rawType(),
                withSettings().extraInterfaces(rawExtraInterfaces)
        )).thenReturn(mock(Object.class));

        Object result = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);

        verify(returnTypeGenericMetadata, times(1)).rawType();
        verify(returnTypeGenericMetadata, times(1)).hasRawExtraInterfaces();
        verify(returnTypeGenericMetadata, times(1)).rawExtraInterfaces();
        verify(mockitoCore(), times(1)).mock(
                returnTypeGenericMetadata.rawType(),
                withSettings().extraInterfaces(rawExtraInterfaces)
        );
        verifyNoMoreInteractions(returnTypeGenericMetadata, mockitoCore());
    }

    // Test case for the newDeepStubMock() method without raw extra interfaces
    @Test
    public void testNewDeepStubMockWithoutRawExtraInterfaces() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(false);
        when(mockitoCore().mock(
                returnTypeGenericMetadata.rawType(),
                withSettings()
        )).thenReturn(mock(Object.class));

        Object result = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);

        verify(returnTypeGenericMetadata, times(1)).rawType();
        verify(returnTypeGenericMetadata, times(1)).hasRawExtraInterfaces();
        verify(mockitoCore(), times(1)).mock(
                returnTypeGenericMetadata.rawType(),
                withSettings()
        );
        verifyNoMoreInteractions(returnTypeGenericMetadata, mockitoCore());
    }

    // Test case for the withSettingsUsing() method with raw extra interfaces
    @Test
    public void testWithSettingsUsingWithRawExtraInterfaces() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(true);
        Class<?>[] rawExtraInterfaces = new Class[]{Serializable.class};
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(rawExtraInterfaces);
        MockSettings mockSettings = mock(MockSettings.class);
        when(withSettings().extraInterfaces(rawExtraInterfaces)).thenReturn(mockSettings);
        when(mockSettings.serializable()).thenReturn(mockSettings);
        when(mockSettings.defaultAnswer(returnsDeepStubsAnswerUsing(returnTypeGenericMetadata))).thenReturn(mockSettings);

        MockSettings result = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);

        verify(returnTypeGenericMetadata, times(1)).hasRawExtraInterfaces();
        verify(returnTypeGenericMetadata, times(1)).rawExtraInterfaces();
        verify(withSettings(), times(1)).extraInterfaces(rawExtraInterfaces);
        verify(mockSettings, times(1)).serializable();
        verify(mockSettings, times(1)).defaultAnswer(returnsDeepStubsAnswerUsing(returnTypeGenericMetadata));
        verifyNoMoreInteractions(returnTypeGenericMetadata, withSettings(), mockSettings);
    }

    // Test case for the withSettingsUsing() method without raw extra interfaces
    @Test
    public void testWithSettingsUsingWithoutRawExtraInterfaces() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(false);
        MockSettings mockSettings = mock(MockSettings.class);
        when(withSettings()).thenReturn(mockSettings);
        when(mockSettings.serializable()).thenReturn(mockSettings);
        when(mockSettings.defaultAnswer(returnsDeepStubsAnswerUsing(returnTypeGenericMetadata))).thenReturn(mockSettings);

        MockSettings result = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);

        verify(returnTypeGenericMetadata, times(1)).hasRawExtraInterfaces();
        verify(withSettings(), times(1)).serializable();
        verify(withSettings(), times(1)).defaultAnswer(returnsDeepStubsAnswerUsing(returnTypeGenericMetadata));
        verifyNoMoreInteractions(returnTypeGenericMetadata, withSettings(), mockSettings);
    }

    // Test case for the returnsDeepStubsAnswerUsing() method
    @Test
    public void testReturnsDeepStubsAnswerUsing() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        ReturnsDeepStubsSerializationFallback returnsDeepStubsSerializationFallback = mock(ReturnsDeepStubsSerializationFallback.class);
        whenNew(ReturnsDeepStubsSerializationFallback.class)
                .withArguments(returnTypeGenericMetadata)
                .thenReturn(returnsDeepStubsSerializationFallback);

        ReturnsDeepStubs result = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);

        verifyNew(ReturnsDeepStubsSerializationFallback.class, times(1)).withArguments(returnTypeGenericMetadata);
        verifyNoMoreInteractions();
    }

    // Test case for the recordDeepStubAnswer() method
    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        Object mock = mock(Object.class);
        DeeplyStubbedAnswer deeplyStubbedAnswer = mock(DeeplyStubbedAnswer.class);
        whenNew(DeeplyStubbedAnswer.class)
                .withArguments(mock)
                .thenReturn(deeplyStubbedAnswer);

        Object result = returnsDeepStubs.recordDeepStubAnswer(mock, container);

        verify(container, times(1)).addAnswer(deeplyStubbedAnswer, false);
        verifyNoMoreInteractions(container);
    }

    // Test case for the actualParameterizedType() method
    @Test
    public void testActualParameterizedType() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(new MockUtil().getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(Object.class);
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        when(GenericMetadataSupport.inferFrom(mockSettings.getTypeToMock())).thenReturn(genericMetadataSupport);

        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);

        verifyNew(MockUtil.class, times(1)).withNoArguments();
        verifyNew(MockUtil.class, times(1)).getMockHandler(mock);
        verify(mockSettings, times(1)).getTypeToMock();
        verify(GenericMetadataSupport.class, times(1)).inferFrom(mockSettings.getTypeToMock());
        verifyNoMoreInteractions();
    }

    // Test case for ReturnsDeepStubsSerializationFallback.writeReplace() method
    @Test
    public void testReturnsDeepStubsSerializationFallbackWriteReplace() throws Throwable {
        ReturnsDeepStubsSerializationFallback returnsDeepStubsSerializationFallback = new ReturnsDeepStubsSerializationFallback(null);

        Object result = returnsDeepStubsSerializationFallback.writeReplace();

        assertNotNull(result);
        assertTrue(result instanceof Mockito.RETURNS_DEEP_STUBS);
        verifyNoMoreInteractions();
    }

    // Test case for the DeeplyStubbedAnswer.answer() method
    @Test
    public void testDeeplyStubbedAnswer() throws Throwable {
        DeeplyStubbedAnswer deeplyStubbedAnswer = new DeeplyStubbedAnswer(null);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object mock = mock(Object.class);

        Object result = deeplyStubbedAnswer.answer(invocation);

        assertEquals(mock, result);
        verifyNoMoreInteractions();
    }

    // Helper method to mock the static mockitoCore() method
    private MockitoCore mockitoCore() {
        return LazyHolder.MOCKITO_CORE;
    }

    // Helper method to mock the static delegate() method
    private ReturnsEmptyValues delegate() {
        return LazyHolder.DELEGATE;
    }
}
