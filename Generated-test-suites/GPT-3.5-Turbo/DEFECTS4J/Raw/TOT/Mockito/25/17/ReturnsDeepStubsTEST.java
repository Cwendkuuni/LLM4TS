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

import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer_GenericNonMockableType_ReturnsEmptyValues() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        mock(InternalMockHandler.class);
        MockCreationValidator mockCreationValidator = mock(MockCreationValidator.class);
        when(invocation.getMock()).thenReturn(mock(CreationSettings.class));
        when(mockCreationValidator.isTypeMockable(any(Class.class))).thenReturn(false);
        when(invocation.getMock()).thenReturn(mockCreationValidator);

        Object result = returnsDeepStubs.answer(invocation);

        verify(mockCreationValidator).isTypeMockable(any(Class.class));
        verify(invocation, never()).getMock();
        assertSame(result.getClass(), Object.class);
    }

    @Test
    public void testAnswer_GenericMockableType_ReturnsMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        mock(InternalMockHandler.class);
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        MockUtil mockUtil = mock(MockUtil.class);
        when(invocation.getMock()).thenReturn(mock(CreationSettings.class));
        when(mockUtil.getMockHandler(anyObject())).thenReturn(mock(InternalMockHandler.class));
        when(genericMetadataSupport.rawType()).thenReturn(Object.class);
        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethod("getClass"));

        Object result = returnsDeepStubs.answer(invocation);

        verify(mockUtil).getMockHandler(anyObject());
        verify(invocation, times(2)).getMock();
        verify(genericMetadataSupport).rawType();

        assertSame(result.getClass(), Object.class);
    }

    @Test
    public void testGetMock_MatchingInvocation_ReturnsMatchedAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InternalMockHandler mockHandler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(invocation.getMock()).thenReturn(mock(CreationSettings.class));
        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethod("getClass"));
        when(mockHandler.getInvocationContainer()).thenReturn(container);
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[]{stubbedInvocationMatcher});
        when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(true);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn(new Object());

        Object result = returnsDeepStubs.getMock(invocation);

        verify(mockHandler).getMock();
        verify(container).addAnswer(any(Answer.class), anyBoolean());
        verify(container, never()).addAnswer(any(Answer.class), eq(true));
        assertSame(result.getClass(), Object.class);
    }

    @Test
    public void testGetMock_NonMatchingInvocation_ReturnsDeepStubMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InternalMockHandler mockHandler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(invocation.getMock()).thenReturn(mock(CreationSettings.class));
        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethod("getClass"));
        when(mockHandler.getInvocationContainer()).thenReturn(container);
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[]{stubbedInvocationMatcher});
        when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(false);

        Object result = returnsDeepStubs.getMock(invocation);

        verify(mockHandler).getMock();
        verify(container, never()).addAnswer(any(Answer.class), anyBoolean());
        assertSame(result.getClass(), Object.class);
    }

    @Test
    public void testRecordDeepStubMock_ReturnsMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InternalMockHandler mockHandler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethod("getClass"));
        when(mockHandler.getInvocationContainer()).thenReturn(container);
        when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
        when(org.mockito.Mockito.mock(any(Class.class), any(Answer.class))).thenReturn(mock(Object.class));

        Object result = returnsDeepStubs.recordDeepStubMock(invocation, container);

        verify(mockHandler, times(2)).getMock();
        verify(container).addAnswer(any(Answer.class), eq(false));
        assertSame(result.getClass(), Object.class);
    }

    @Test
    public void testActualParameterizedType_ReturnsGenericMetadataSupport() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        MockUtil mockUtil = mock(MockUtil.class);
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        when(mockUtil.getMockHandler(anyObject())).thenReturn(mock(InternalMockHandler.class));
        when(mockUtil.getMockHandler(anyObject()).getMockSettings()).thenReturn(mock(CreationSettings.class));
        when(mockUtil.getMockHandler(anyObject()).getMockSettings().getTypeToMock()).thenReturn(Object.class);
        when(GenericMetadataSupport.inferFrom(anyObject())).thenReturn(genericMetadataSupport);

        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(new Object());

        verify(mockUtil).getMockHandler(anyObject());
        verify(mockUtil.getMockHandler(anyObject())).getMockSettings();
        verify(mockUtil.getMockHandler(anyObject()).getMockSettings()).getTypeToMock();
        verify(GenericMetadataSupport).inferFrom(anyObject());
        assertSame(result, genericMetadataSupport);
    }

}