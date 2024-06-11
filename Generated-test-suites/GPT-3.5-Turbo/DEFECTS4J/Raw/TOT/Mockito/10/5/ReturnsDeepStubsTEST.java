import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Stubber;

import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswerMethod_WhenMockIsTypeMockable_ReturnsDeepStub() throws Throwable {
        // Arrange
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Foo.class));

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        assertSame(result, mock(Foo.class));
    }

    @Test
    public void testAnswerMethod_WhenMockIsNotTypeMockable_ReturnsDelegateReturnValue() throws Throwable {
        // Arrange
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Bar.class));
        when(returnsDeepStubs.delegate().returnValueFor(Bar.class)).thenReturn(new Bar());

        // Act
        Object result = returnsDeepStubs.answer(invocation);

        // Assert
        assertSame(result, new Bar());
    }

    @Test
    public void testDeepStub_WhenInvocationMatches_ReturnsStubbedInvocationMatcherAnswer() throws Throwable {
        // Arrange
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Foo.class));

        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(stubbedInvocationMatcher.getInvocation()).thenReturn(invocation);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(container.getInvocationForStubbing().matches(invocation)).thenReturn(true);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));

        // Act
        Object result = returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata);

        // Assert
        assertSame(result, stubbedInvocationMatcher.answer(invocation));
    }

    @Test
    public void testDeepStub_WhenInvocationDoesNotMatch_RecordDeepStubAnswerAndReturnNewDeepStubMock() throws Throwable {
        // Arrange
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Foo.class));

        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(stubbedInvocationMatcher.getInvocation()).thenReturn(mock(Invocation.class));
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(container.getInvocationForStubbing().matches(any())).thenReturn(false);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));

        when(returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata)).thenReturn(mock(Foo.class));
        when(returnsDeepStubs.recordDeepStubAnswer(any(), any())).thenReturn(mock(Foo.class));

        // Act
        Object result = returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata);

        // Assert
        assertSame(result, mock(Foo.class));
        verify(returnsDeepStubs, times(1)).newDeepStubMock(returnTypeGenericMetadata);
        verify(returnsDeepStubs, times(1)).recordDeepStubAnswer(mock(Foo.class), container);
    }

    @Test
    public void testNewDeepStubMock_ReturnsMockWithCorrectSettings() {
        // Arrange
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        CreationSettings creationSettings = mock(CreationSettings.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Foo.class);
        when(mockitoCore().mock(eq(Foo.class), any())).thenReturn(mock(Foo.class));

        // Act
        Object result = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);

        // Assert
        assertNotNull(result);
        verify(mockitoCore(), times(1)).mock(eq(Foo.class), any());
    }

    @Test
    public void testWithSettingsUsing_ReturnsMockSettingsWithExtraInterfacesAndDefaultAnswer() {
        // Arrange
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        MockSettings mockSettings = mock(MockSettings.class);
        when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(true);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class<?>[]{Foo.class});
        when(withSettings()).thenReturn(mockSettings);

        // Act
        MockSettings result = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);

        // Assert
        assertNotNull(result);
        verify(mockSettings, times(1)).extraInterfaces(eq(new Class<?>[]{Foo.class}));
        verify(mockSettings, times(1)).serializable();
        verify(mockSettings, times(1)).defaultAnswer(any());
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing_ReturnsReturnsDeepStubsSerializationFallback() {
        // Arrange
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata)).thenCallRealMethod();

        // Act
        ReturnsDeepStubsSerializationFallback result = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);

        // Assert
        assertNotNull(result);
    }

    @Test
    public void testRecordDeepStubAnswer_AddsDeeplyStubbedAnswerToContainer() throws Throwable {
        // Arrange
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Foo.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        // Act
        Object result = returnsDeepStubs.recordDeepStubAnswer(mock, container);

        // Assert
        assertSame(result, mock);
        verify(container, times(1)).addAnswer(any(DeeplyStubbedAnswer.class), eq(false));
    }

    @Test
    public void testActualParameterizedType_ReturnsGenericMetadataFromMockSettings() {
        // Arrange
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Foo.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(new MockUtil().getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(Foo.class);
        when(GenericMetadataSupport.inferFrom(Foo.class)).thenReturn(mock(GenericMetadataSupport.class));

        // Act
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);

        // Assert
        assertNotNull(result);
        verify(new MockUtil(), times(1)).getMockHandler(mock);
        verify(mockSettings, times(1)).getTypeToMock();
        verify(GenericMetadataSupport, times(1)).inferFrom(Foo.class);
    }

    @Test
    public void testReturnsDeepStubsSerializationFallback_WriteReplace_ReturnsMockitoReturnsDeepStubs() throws IOException {
        // Arrange
        ReturnsDeepStubsSerializationFallback returnsDeepStubsSerializationFallback = new ReturnsDeepStubsSerializationFallback(mock(GenericMetadataSupport.class));

        // Act
        Object result = returnsDeepStubsSerializationFallback.writeReplace();

        // Assert
        assertSame(result, Mockito.RETURNS_DEEP_STUBS);
    }

    @Test
    public void testDeeplyStubbedAnswer_AnswerMethod_ReturnsMock() throws Throwable {
        // Arrange
        DeeplyStubbedAnswer deeplyStubbedAnswer = new DeeplyStubbedAnswer(mock(Foo.class));
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        // Act
        Object result = deeplyStubbedAnswer.answer(invocation);

        // Assert
        assertSame(result, mock(Foo.class));
    }

    private static class Foo {}

    private static class Bar {}
}