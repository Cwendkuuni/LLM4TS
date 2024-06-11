import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

import java.util.*;

public class ReturnsDeepStubsTest {

    @Mock
    InvocationOnMock invocationMock;

    @Mock
    InternalMockHandler<Object> handlerMock;
    
    @Mock
    InvocationContainerImpl containerMock;

    @Test
    public void testAnswer_withMockCreationValidator_notTypeMockable() throws Throwable {
        MockitoAnnotations.initMocks(this);
        
        when(invocationMock.getMock()).thenReturn(new ReturnsDeepStubs());
        
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        returnsDeepStubs.answer(invocationMock);

        verify(invocationMock).getMock();
    }

    @Test
    public void testAnswer_withNotMatchingInvocation() throws Throwable {
        MockitoAnnotations.initMocks(this);
        
        when(invocationMock.getMock()).thenReturn(new ReturnsDeepStubs());
        when(handlerMock.getInvocationContainer()).thenReturn(containerMock);
        
        StubbedInvocationMatcher stubbedInvocationMatcherMock = mock(StubbedInvocationMatcher.class);
        List<StubbedInvocationMatcher> stubbedInvocationsMock = new ArrayList<>();
        stubbedInvocationsMock.add(stubbedInvocationMatcherMock);
        
        when(containerMock.getStubbedInvocations()).thenReturn(stubbedInvocationsMock);
        when(containerMock.getInvocationForStubbing()).thenReturn(invocationMock);
        when(containerMock.getInvocationForStubbing().matches(stubbedInvocationMatcherMock.getInvocation())).thenReturn(false);
        
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        returnsDeepStubs.answer(invocationMock);
        
        verify(invocationMock).getMock();
        verify(handlerMock).getInvocationContainer();
        verify(containerMock).getStubbedInvocations();
        verify(containerMock).getInvocationForStubbing().matches(stubbedInvocationMatcherMock.getInvocation());
    }

    @Test
    public void testAnswer_withMatchingInvocation() throws Throwable {
        MockitoAnnotations.initMocks(this);
        
        when(invocationMock.getMock()).thenReturn(new ReturnsDeepStubs());
        when(handlerMock.getInvocationContainer()).thenReturn(containerMock);
        
        StubbedInvocationMatcher stubbedInvocationMatcherMock = mock(StubbedInvocationMatcher.class);
        when(containerMock.getStubbedInvocations()).thenReturn(Arrays.asList(stubbedInvocationMatcherMock));
        when(containerMock.getInvocationForStubbing()).thenReturn(invocationMock);
        when(containerMock.getInvocationForStubbing().matches(stubbedInvocationMatcherMock.getInvocation())).thenReturn(true);
        when(stubbedInvocationMatcherMock.answer(invocationMock)).thenReturn(new ReturnsDeepStubs());
        
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        returnsDeepStubs.answer(invocationMock);
        
        verify(invocationMock).getMock();
        verify(invocationMock, times(2)).getMethod();
        verify(handlerMock).getInvocationContainer();
        verify(containerMock, times(2)).getStubbedInvocations();
        verify(containerMock, times(2)).getInvocationForStubbing();
        verify(stubbedInvocationMatcherMock).answer(invocationMock);
    }

    @Test
    public void testGetMock_withRawTypeMockable() throws Throwable {
        MockitoAnnotations.initMocks(this);

        when(invocationMock.getMock()).thenReturn(new ReturnsDeepStubs());
        when(handlerMock.getInvocationContainer()).thenReturn(containerMock);
        
        when(containerMock.addAnswer(any(), eq(false))).thenReturn(null);
        
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        returnsDeepStubs.getMock(invocationMock);
        
        verify(invocationMock).getMock();
        verify(handlerMock).getInvocationContainer();
        verify(containerMock).addAnswer(any(), eq(false));
    }

    @Test
    public void testGetMock_withRawTypeNotMockable() throws Throwable {
        MockitoAnnotations.initMocks(this);
        
        when(invocationMock.getMock()).thenReturn(new ReturnsDeepStubs());
        when(handlerMock.getInvocationContainer()).thenReturn(containerMock);
        
        when(containerMock.addAnswer(any(), eq(false))).thenReturn(null);
        
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        returnsDeepStubs.getMock(invocationMock);
        
        verify(invocationMock, times(2)).getMock();
        verify(handlerMock, times(2)).getInvocationContainer();
        verify(containerMock, times(2)).addAnswer(any(), eq(false));
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        MockitoAnnotations.initMocks(this);
        
        when(invocationMock.getMethod()).thenReturn(null);
        
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        returnsDeepStubs.recordDeepStubMock(invocationMock, containerMock);
        
        verify(invocationMock).getMethod();
        verify(containerMock).addAnswer(any(), eq(false));
    }

    @Test
    public void testActualParameterizedType() {
        MockitoAnnotations.initMocks(this);
        
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(new ReturnsDeepStubs());
        
        assertNotNull(result);
    }

}