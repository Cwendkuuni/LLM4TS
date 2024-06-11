import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.Captor;
import org.mockito.ArgumentCaptor;

public class ReturnsDeepStubsTest {
    
    @Captor
    private ArgumentCaptor<Answer<Object>> answerCaptor;
    
    @Test
    public void testAnswer_ReturnsDeepStubs() throws Throwable {
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> returnValue = answer.answer(invocation);
        
        verify(invocation).getMock();
        verify(invocation).getMethod();
        
        verifyPrivate(answer, times(1)).invoke("getMock", invocation);
    }
    
    @Test
    public void testGetMock_MatchesInvocation() throws Throwable {
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object returnValue = answer.getMock(invocation);
        
        InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
        when(new MockUtil().getMockHandler(invocation.getMock())).thenReturn(mockHandler);
        
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(mockHandler.getInvocationContainer()).thenReturn(container);
        
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        
        when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(true);
        
        answer.mockHandler = mockHandler;
        
        verifyPrivate(answer, times(1)).invoke("getMock", invocation);
        verify(container, times(1)).getStubbedInvocations();
        verify(container, times(1)).getInvocationForStubbing();
        
        verifyPrivate(answer, times(0)).invoke("recordDeepStubMock", invocation, container);
    }
    
    @Test
    public void testGetMock_NoMatchingInvocation() throws Throwable {
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object returnValue = answer.getMock(invocation);
        
        InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
        when(new MockUtil().getMockHandler(invocation.getMock())).thenReturn(mockHandler);
        
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(mockHandler.getInvocationContainer()).thenReturn(container);
        
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        
        when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(false);
        
        answer.mockHandler = mockHandler;
        
        verifyPrivate(answer, times(1)).invoke("getMock", invocation);
        verify(container, times(1)).getStubbedInvocations();
        verify(container, times(1)).getInvocationForStubbing();
        
        verifyPrivate(answer, times(1)).invoke("recordDeepStubMock", invocation, container);
    }
    
    @Test
    public void testRecordDeepStubMock() throws Throwable {
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
        
        final Object mock = mock(Object.class);
        when(org.mockito.Mockito.mock(Object.class, answer)).thenReturn(mock);
        
        answer.recordDeepStubMock(invocation, container);
        
        verifyPrivate(answer, times(1)).invoke("recordDeepStubMock", invocation, container);
        verify(container, times(1)).addAnswer(answerCaptor.capture(), eq(false));
        
        Answer<Object> answerCallback = answerCaptor.getValue();
        InvocationOnMock invocation1 = mock(InvocationOnMock.class);
        Object returnValue1 = answerCallback.answer(invocation1);
        
        assertEquals(mock, returnValue1);
    }
    
    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(new MockUtil().getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        
        GenericMetadataSupport genericMetadata = mock(GenericMetadataSupport.class);
        when(GenericMetadataSupport.inferFrom(mockSettings.getTypeToMock())).thenReturn(genericMetadata);
        
        GenericMetadataSupport actualParameterizedType = answer.actualParameterizedType(mock);
        
        verifyPrivate(answer, times(1)).invoke("actualParameterizedType", mock);
        assertEquals(genericMetadata, actualParameterizedType);
    }
}