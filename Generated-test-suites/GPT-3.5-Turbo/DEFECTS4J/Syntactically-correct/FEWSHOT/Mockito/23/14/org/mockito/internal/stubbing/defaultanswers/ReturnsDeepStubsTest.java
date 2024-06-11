import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsEmptyValues delegate = mock(ReturnsEmptyValues.class);
        Object mock = mock(Object.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Verify if returnTypeGenericMetadata.rawType() is not mockable, then delegate.returnValueFor(rawType) is returned
        when(mockitoCore.isTypeMockable(any(Class.class))).thenReturn(false);
        when(delegate.returnValueFor(any(Class.class))).thenReturn(null);
        
        Object result = new ReturnsDeepStubs(delegate, mockitoCore).answer(invocation);
        
        assertNull(result);
        verify(mockitoCore).isTypeMockable(any(Class.class));
        verify(delegate).returnValueFor(any(Class.class));
        
        // Verify if returnTypeGenericMetadata.rawType() is mockable, then getMock method is called
        when(mockitoCore.isTypeMockable(any(Class.class))).thenReturn(true);
        when(invocation.getMock()).thenReturn(mock);
        
        result = new ReturnsDeepStubs(delegate, mockitoCore).answer(invocation);

        assertNotNull(result);
        verify(mockitoCore).isTypeMockable(any(Class.class));
        verify(invocation).getMock();
        verify(mockitoCore).mock(any(Class.class), any(MockSettings.class));
        verify(invocation).getMethod();
        verify(actualParameterizedType(invocation.getMock())).resolveGenericReturnType(invocation.getMethod());
        
        // Verify if invocation matches stubbedInvocationMatcher.getInvocation(), then stubbedInvocationMatcher.answer(invocation) is returned
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));

        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(invocation.matches(any(InvocationMatcher.class))).thenReturn(true);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn(mockitoCore);

        result = new ReturnsDeepStubs(delegate, mockitoCore).answer(invocation);

        assertNotNull(result);
        verify(container).getStubbedInvocations();
        verify(container).getInvocationForStubbing();
        verify(invocation).matches(any(InvocationMatcher.class));
        verify(stubbedInvocationMatcher).answer(invocation);
        
        // Verify if no stubbedInvocationMatcher matches the invocation, then recordDeepStubMock method is called
        when(container.getStubbedInvocations()).thenReturn(Collections.emptyList());

        result = new ReturnsDeepStubs(delegate, mockitoCore).answer(invocation);

        assertNotNull(result);
        verify(container, times(2)).getStubbedInvocations();
        verify(container, times(2)).getInvocationForStubbing();
        verify(invocation, times(2)).matches(any(InvocationMatcher.class));
        verify(stubbedInvocationMatcher, never()).answer(invocation);
        verify(mockitoCore).mock(any(Class.class), any(MockSettings.class));
        verify(invocation, times(2)).getMock();
    }
}