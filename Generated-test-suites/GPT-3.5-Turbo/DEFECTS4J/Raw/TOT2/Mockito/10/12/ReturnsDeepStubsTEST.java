import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer_WhenReturnTypeMockable_ReturnsDeepStub() throws Throwable {
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object result = deepStubs.answer(invocation);
        assertNotNull(result);
    }

    @Test
    public void testAnswer_WhenReturnTypeNotMockable_ReturnsDelegateValue() throws Throwable {
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(null);
        Object result = deepStubs.answer(invocation);
        assertNotNull(result);
    }

    @Test
    public void testNewDeepStubMock_WithGenericMetadata_CreatesMock() {
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport metadata = new GenericMetadataSupport();
        Object mock = deepStubs.newDeepStubMock(metadata);
        assertNotNull(mock);
    }

    @Test
    public void testWithSettingsUsing_WithGenericMetadata_ReturnsMockSettings() {
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport metadata = new GenericMetadataSupport();
        assertNotNull(deepStubs.withSettingsUsing(metadata));
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing_WithGenericMetadata_ReturnsDeepStubs() {
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport metadata = new GenericMetadataSupport();
        assertNotNull(deepStubs.returnsDeepStubsAnswerUsing(metadata));
    }

    @Test
    public void testRecordDeepStubAnswer_AddsAnswerToContainer() throws Throwable {
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        Object mock = new Object();
        Object result = deepStubs.recordDeepStubAnswer(mock, container);
        assertEquals(mock, result);
    }

    @Test
    public void testActualParameterizedType_WithTypeMock_ReturnsGenericMetadata() {
        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        GenericMetadataSupport metadata = deepStubs.actualParameterizedType(mock);
        assertNotNull(metadata);
    }

    @Test
    public void testDeeplyStubbedAnswer_ReturnsMockAnswer() {
        ReturnsDeepStubs.DeeplyStubbedAnswer answer = new ReturnsDeepStubs.DeeplyStubbedAnswer(mock(Object.class));
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object result = answer.answer(invocation);
        assertNotNull(result);
    }
}