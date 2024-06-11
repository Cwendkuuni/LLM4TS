
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {
    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        doReturn(Object.class).when(genericMetadataSupport).rawType();
        doReturn(genericMetadataSupport).when(returnsDeepStubs).actualParameterizedType(anyObject());

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object result1 = returnsDeepStubs.answer(invocation);
        Object result2 = returnsDeepStubs.answer(invocation);

        assertNotNull(result1);
        assertSame(result1, result2);
        verify(invocation, times(1)).getMock();
        verify(invocation, times(1)).getMethod();
        verify(genericMetadataSupport, times(2)).rawType();
    }

    @Test
    public void testDeepStub() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        doReturn(handler).when(returnsDeepStubs).getMockHandler(anyObject());
        doReturn(container).when(handler).getInvocationContainer();
        doReturn(true).when(returnTypeGenericMetadata).hasRawExtraInterfaces();
        doReturn(new Class[]{Serializable.class}).when(returnTypeGenericMetadata).rawExtraInterfaces();
        doReturn(true).when(container.getInvocationForStubbing()).matches(anyObject());
        doReturn(stubbedInvocationMatcher).when(container.getStubbedInvocations()).get(0);

        Object result = returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata);

        assertNotNull(result);
        assertEquals(stubbedInvocationMatcher.answer(invocation), result);
        verify(handler, times(1)).getInvocationContainer();
        verify(container, times(1)).getStubbedInvocations();
        verify(container, times(1)).getInvocationForStubbing();
        verify(returnTypeGenericMetadata, times(1)).hasRawExtraInterfaces();
        verify(returnTypeGenericMetadata, times(1)).rawExtraInterfaces();
    }

    @Test
    public void testNewDeepStubMock() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        MockitoCore mockitoCore = mock(MockitoCore.class);

        doReturn(Object.class).when(returnTypeGenericMetadata).rawType();
        doReturn(mockitoCore).when(returnsDeepStubs).mockitoCore();
        doReturn(returnsDeepStubs).when(returnsDeepStubs).returnsDeepStubsAnswerUsing(anyObject());

        Object result = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);

        assertNotNull(result);
        verify(returnTypeGenericMetadata, times(1)).rawType();
        verify(mockitoCore, times(1)).mock(any(Class.class), any(MockSettings.class));
        verify(returnsDeepStubs, times(1)).returnsDeepStubsAnswerUsing(anyObject());
    }

    @Test
    public void testWithSettingsUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        MockSettings mockSettings = mock(MockSettings.class);

        doReturn(true).when(returnTypeGenericMetadata).hasRawExtraInterfaces();
        doReturn(new Class[]{Serializable.class}).when(returnTypeGenericMetadata).rawExtraInterfaces();
        doReturn(mockSettings).when(mockSettings).serializable();
        doReturn(returnsDeepStubs).when(returnsDeepStubs).returnsDeepStubsAnswerUsing(anyObject());

        MockSettings result = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);

        assertNotNull(result);
        assertSame(mockSettings, result);
        verify(returnTypeGenericMetadata, times(1)).hasRawExtraInterfaces();
        verify(returnTypeGenericMetadata, times(1)).rawExtraInterfaces();
        verify(mockSettings, times(1)).serializable();
        verify(returnsDeepStubs, times(1)).returnsDeepStubsAnswerUsing(anyObject());
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        ReturnsDeepStubsSerializationFallback returnsDeepStubsSerializationFallback = mock(ReturnsDeepStubsSerializationFallback.class);

        doReturn(returnsDeepStubsSerializationFallback).when(returnsDeepStubsSerializationFallback).ReturnsDeepStubsSerializationFallback(anyObject());

        ReturnsDeepStubs result = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);

        assertNotNull(result);
        verify(returnsDeepStubsSerializationFallback, times(1)).ReturnsDeepStubsSerializationFallback(anyObject());
    }

    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        Object mock = mock(Object.class, RETURNS_DEEP_STUBS);

        Object result = returnsDeepStubs.recordDeepStubAnswer(mock, container);

        assertNotNull(result);
        assertSame(mock, result);
        verify(container, times(1)).addAnswer(any(DeeplyStubbedAnswer.class), anyBoolean());
    }

    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        CreationSettings mockSettings = mock(CreationSettings.class);
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        MockUtil mockUtil = mock(MockUtil.class);

        doReturn(mockSettings).when(mockUtil).getMockSettings(anyObject());
        doReturn(genericMetadataSupport).when(genericMetadataSupport).inferFrom(anyObject());

        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock(Object.class));

        assertNotNull(result);
        verify(mockUtil, times(1)).getMockSettings(anyObject());
        verify(genericMetadataSupport, times(1)).inferFrom(anyObject());
    }

    @Test
    public void testReturnsDeepStubsSerializationFallback() throws Throwable {
        ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback returnsDeepStubsSerializationFallback = new ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback(mock(GenericMetadataSupport.class));

        Object result = returnsDeepStubsSerializationFallback.writeReplace();

        assertNotNull(result);
        assertEquals(Mockito.RETURNS_DEEP_STUBS, result);
    }

    @Test
    public void testDeeplyStubbedAnswer() throws Throwable {
        ReturnsDeepStubs.DeeplyStubbedAnswer deeplyStubbedAnswer = new ReturnsDeepStubs.DeeplyStubbedAnswer(mock(Object.class));

        Object result = deeplyStubbedAnswer.answer(mock(InvocationOnMock.class));

        assertNotNull(result);
    }
}
