import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.MockSettings;
import org.mockito.stubbing.Answer;
import org.mockito.Mockito;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;

import java.io.IOException;
import java.io.Serializable;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);

        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(genericMetadataSupport.rawType()).thenReturn(Object.class);
        when(new MockUtil().getMockHandler(invocation.getMock())).thenReturn(mock(InternalMockHandler.class));

        Object result = returnsDeepStubs.answer(invocation);

        assertNotNull(result);
    }

    @Test
    public void testDeepStub() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);

        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(genericMetadataSupport.rawType()).thenReturn(Object.class);
        when(new MockUtil().getMockHandler(invocation.getMock())).thenReturn(mockHandler);
        when(mockHandler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(List.of(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing()).thenReturn(mock(InvocationOnMock.class));
        when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(true);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn(mock(Object.class));

        Object result = returnsDeepStubs.deepStub(invocation, genericMetadataSupport);

        assertNotNull(result);
    }

    @Test
    public void testNewDeepStubMock() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        MockitoCore mockitoCore = mock(MockitoCore.class);

        when(genericMetadataSupport.rawType()).thenReturn(Object.class);
        when(mockitoCore.mock(any(), any())).thenReturn(mock(Object.class));

        Object result = returnsDeepStubs.newDeepStubMock(genericMetadataSupport);

        assertNotNull(result);
    }

    @Test
    public void testWithSettingsUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        MockSettings mockSettings = mock(MockSettings.class);

        when(genericMetadataSupport.hasRawExtraInterfaces()).thenReturn(true);
        when(genericMetadataSupport.rawExtraInterfaces()).thenReturn(new Class<?>[]{Serializable.class});
        when(mockSettings.extraInterfaces(any())).thenReturn(mockSettings);
        when(mockSettings.serializable()).thenReturn(mockSettings);
        when(mockSettings.defaultAnswer(any())).thenReturn(mockSettings);

        MockSettings result = returnsDeepStubs.withSettingsUsing(genericMetadataSupport);

        assertNotNull(result);
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);

        ReturnsDeepStubsSerializationFallback result = returnsDeepStubs.returnsDeepStubsAnswerUsing(genericMetadataSupport);

        assertNotNull(result);
    }

    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        Object mock = mock(Object.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        when(container.addAnswer(any(), anyBoolean())).thenReturn(true);

        Object result = returnsDeepStubs.recordDeepStubAnswer(mock, container);

        assertNotNull(result);
    }

    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        GenericMetadataSupport genericMetadataSupport = new GenericMetadataSupport();

        when(new MockUtil().getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(Object.class);
        when(GenericMetadataSupport.inferFrom(Object.class)).thenReturn(genericMetadataSupport);

        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);

        assertNotNull(result);
    }

    @Test
    public void testReturnsDeepStubsSerializationFallback() throws IOException {
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        ReturnsDeepStubsSerializationFallback returnsDeepStubsSerializationFallback = new ReturnsDeepStubsSerializationFallback(genericMetadataSupport);

        Object result = returnsDeepStubsSerializationFallback.writeReplace();

        assertNotNull(result);
    }

    @Test
    public void testDeeplyStubbedAnswer() throws Throwable {
        Object mock = mock(Object.class);
        DeeplyStubbedAnswer deeplyStubbedAnswer = new DeeplyStubbedAnswer(mock);

        Object result = deeplyStubbedAnswer.answer(mock(InvocationOnMock.class));

        assertNotNull(result);
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