import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.Answers;
import org.mockito.stubbing.Stubber;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;

import java.util.*;

import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setup() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void answer_WithNonMockableReturnType_ReturnsEmptyValues() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        
        when(invocation.getMock()).thenReturn(new Object());
        when(returnTypeGenericMetadata.rawType()).thenReturn(List.class);
        
        Object result = returnsDeepStubs.answer(invocation);
        
        verify(invocation, times(1)).getMock();
        verify(returnTypeGenericMetadata, times(1)).rawType();
        
        assertInstanceOf(ReturnsEmptyValues.class, result);
    }

    @Test
    public void answer_WithMockableReturnType_ReturnsMock() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);

        when(invocation.getMock()).thenReturn(new Object());
        when(returnTypeGenericMetadata.rawType()).thenReturn(List.class);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(handler.getInvocationContainer().getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing().matches(any(InvocationOnMock.class))).thenReturn(true);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn("mockResult");
        
        Object result = returnsDeepStubs.answer(invocation);
        
        verify(invocation, times(1)).getMock();
        verify(returnTypeGenericMetadata, times(1)).rawType();
        verify(handler, times(1)).getInvocationContainer();
        verify(container, times(1)).getStubbedInvocations();
        verify(stubbedInvocationMatcher, times(1)).answer(invocation);
        
        assertEquals("mockResult", result);
    }

    @Test
    public void getMock_ReturnsNewMock() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        when(invocation.getMock()).thenReturn(new Object());
        when(new MockUtil().getMockHandler(invocation.getMock())).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        
        Object result = returnsDeepStubs.getMock(invocation);
        
        verify(invocation, times(1)).getMock();
        verify(new MockUtil(), times(1)).getMockHandler(invocation.getMock());
        verify(handler, times(1)).getInvocationContainer();
        verify(container, times(1)).getInvocationForStubbing();

        assertInstanceOf(Object.class, result);
    }

    @Test
    public void getMock_WithMatchingInvocation_ReturnsStubbedAnswer() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);

        when(invocation.getMock()).thenReturn(new Object());
        when(new MockUtil().getMockHandler(invocation.getMock())).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        when(stubbedInvocationMatcher.getInvocation()).thenReturn(invocation);
        when(container.getInvocationForStubbing().matches(invocation)).thenReturn(true);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn("stubbedResult");
        
        Object result = returnsDeepStubs.getMock(invocation);
        
        verify(invocation, times(2)).getMock();
        verify(new MockUtil(), times(1)).getMockHandler(invocation.getMock());
        verify(handler, times(1)).getInvocationContainer();
        verify(container, times(1)).getStubbedInvocations();
        verify(invocation, times(1)).getMethod();
        verify(invocation, times(2)).getInvocation();
        verify(container.getInvocationForStubbing(), times(1)).matches(invocation);
        verify(stubbedInvocationMatcher, times(1)).answer(invocation);

        assertEquals("stubbedResult", result);
    }

    @Test
    public void getMock_WithNoMatchingInvocation_ReturnsDeepStubMock() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        Stubber stubber = mock(Stubber.class);

        when(invocation.getMock()).thenReturn(new Object());
        when(new MockUtil().getMockHandler(invocation.getMock())).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(mock(StubbedInvocationMatcher.class))); // no matching invocation
        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        when(container.addAnswer(any(Answer.class), anyBoolean())).thenReturn(stubber);
        when(stubber.when(any(InvocationOnMock.class))).thenReturn(stubber);
        when(stubber.thenReturn(anyObject())).thenReturn(stubber);
        
        Object result = returnsDeepStubs.getMock(invocation);
        
        verify(invocation, times(2)).getMock();
        verify(new MockUtil(), times(1)).getMockHandler(invocation.getMock());
        verify(handler, times(1)).getInvocationContainer();
        verify(container, times(1)).getStubbedInvocations();
        verify(invocation, times(1)).getMethod();
        verify(container, times(1)).addAnswer(any(Answer.class), anyBoolean());
        verify(stubber, times(1)).when(any(InvocationOnMock.class));
        verify(stubber, times(1)).thenReturn(anyObject());

        assertInstanceOf(Object.class, result);
    }

    @Test
    public void recordDeepStubMock_AddsAnswerToContainer() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        Object mock = mock(Object.class);

        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);
        when(invocation.getMock()).thenReturn(mock);
        when(container.addAnswer(any(Answer.class), anyBoolean())).thenReturn(mock);
        
        Object result = returnsDeepStubs.recordDeepStubMock(invocation, container);
        
        verify(container, times(1)).addAnswer(any(Answer.class), anyBoolean());

        assertInstanceOf(Object.class, result);
    }

    @Test
    public void actualParameterizedType_ReturnsGenericMetadataSupport() {
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        MockUtil mockUtil = mock(MockUtil.class);
        
        when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);
        when(mockHandler.getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(mockType);
        when(GenericMetadataSupport.inferFrom(mockType)).thenReturn(genericMetadataSupport);
        
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);
        
        verify(mockUtil, times(1)).getMockHandler(mock);
        verify(mockHandler, times(1)).getMockSettings();
        verify(mockSettings, times(1)).getTypeToMock();
        verify(GenericMetadataSupport, times(1)).inferFrom(mockType);

        assertEquals(genericMetadataSupport, result);
    }
}