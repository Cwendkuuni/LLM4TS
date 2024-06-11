import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.Serializable;

public class ReturnsDeepStubsTest {
    @SuppressWarnings("unchecked")
    @Test
    public void testAnswer() throws Throwable {
        ReturnsEmptyValues delegate = mock(ReturnsEmptyValues.class);
        Object mock = mock(Object.class);
        Object invocationMock = mock(InvocationOnMock.class);
        GenericMetadataSupport genericMetadata = mock(GenericMetadataSupport.class);
        Class<Object> rawType = mock(Class.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);

        when(delegate.returnValueFor(rawType)).thenReturn(mock);
        when(actualParameterizedType(invocationMock)).thenReturn(genericMetadata);
        when(genericMetadata.rawType()).thenReturn(rawType);
        when(new MockCreationValidator().isTypeMockable(rawType)).thenReturn(true);
        when(new MockUtil().getMockHandler(invocationMock)).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[]{stubbedInvocationMatcher});
        when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(true);
        when(stubbedInvocationMatcher.answer(invocationMock)).thenReturn(mock);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object actual = returnsDeepStubs.answer(invocationMock);

        verify(delegate).returnValueFor(rawType);
        verify(actualParameterizedType(invocationMock)).resolveGenericReturnType(invocationMock.getMethod());
        verify(new MockUtil().getMockHandler(invocationMock)).getMockSettings();
        verifyMockCreationValidator(actualType);
        verify(handler).getInvocationContainer();
        verify(container).addAnswer(any(Answer.class), anyBoolean());

        assertEquals(mock, actual);
    }

    @Test
    public void testGetMock() throws Throwable {
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);

        when(new MockUtil().getMockHandler(invocationMock)).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[]{stubbedInvocationMatcher});
        when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(true);
        when(stubbedInvocationMatcher.answer(invocationMock)).thenReturn(mock);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object actual = returnsDeepStubs.getMock(invocationMock);

        verify(new MockUtil().getMockHandler(invocationMock)).getMockSettings();
        verify(handler).getInvocationContainer();

        assertEquals(mock, actual);
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        Class<Object> clz = mock(Class.class);
        Object mock = mock(Object.class);

        when(invocationMock.getMethod().getReturnType()).thenReturn(clz);
        when(org.mockito.Mockito.mock(clz, returnsDeepStubs)).thenReturn(mock);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object actual = returnsDeepStubs.recordDeepStubMock(invocationMock, container);

        verify(invocationMock.getMethod()).getReturnType();
        verify(container).addAnswer(any(Answer.class), eq(false));

        assertEquals(mock, actual);
    }

    @Test
    public void testActualParameterizedType() {
        CreationSettings mockSettings = mock(CreationSettings.class);
        Object mock = mock(Object.class);

        when(new MockUtil().getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(mock);
        when(GenericMetadataSupport.inferFrom(mock)).thenReturn(mock(GenericMetadataSupport.class));

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport actual = returnsDeepStubs.actualParameterizedType(mock);

        verify(new MockUtil().getMockHandler(mock)).getMockSettings();
        verify(mockSettings).getTypeToMock();
        verify(GenericMetadataSupport).inferFrom(mock);

        assertEquals(mock(GenericMetadataSupport.class), actual);
    }
}