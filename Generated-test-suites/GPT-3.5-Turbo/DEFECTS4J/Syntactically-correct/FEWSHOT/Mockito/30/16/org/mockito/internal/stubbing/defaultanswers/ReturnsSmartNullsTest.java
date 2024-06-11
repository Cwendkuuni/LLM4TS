import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class ReturnsSmartNullsTest {
    
    @Test
    public void testAnswer() throws Throwable {
        ReturnsMoreEmptyValues delegateMock = mock(ReturnsMoreEmptyValues.class);
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        
        when(delegateMock.answer(invocationMock)).thenReturn(null);
        
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls(delegateMock);
        
        Object result = returnsSmartNulls.answer(invocationMock);
        
        assertNull(result);
        verify(delegateMock).answer(invocationMock);
    }
    
    @Test
    public void testAnswerWithDefaultReturnValue() throws Throwable {
        ReturnsMoreEmptyValues delegateMock = mock(ReturnsMoreEmptyValues.class);
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        
        when(delegateMock.answer(invocationMock)).thenReturn("Default");
        
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls(delegateMock);
        
        Object result = returnsSmartNulls.answer(invocationMock);
        
        assertEquals("Default", result);
        verify(delegateMock).answer(invocationMock);
    }
    
    @Test
    public void testAnswerWithImposterisableType() throws Throwable {
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        
        Method methodMock = mock(Method.class);
        when(invocationMock.getMethod()).thenReturn(methodMock);
        
        Class<?> returnTypeMock = mock(Class.class);
        when(methodMock.getReturnType()).thenReturn(returnTypeMock);
        
        ClassImposterizer imposterizerMock = mock(ClassImposterizer.class);
        when(ClassImposterizer.INSTANCE).thenReturn(imposterizerMock);
        
        when(imposterizerMock.canImposterise(returnTypeMock)).thenReturn(true);
        
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        
        Object result = returnsSmartNulls.answer(invocationMock);
        
        assertNull(result);
        verify(invocationMock).getMethod();
        verify(methodMock).getReturnType();
        verify(methodMock).getName();
        verify(invocationMock).getArguments();
        verify(returnTypeMock).getName();
        verify(imposterizerMock).canImposterise(returnTypeMock);
        verify(imposterizerMock).imposterise(any(ReturnsSmartNulls.ThrowingInterceptor.class), eq(returnTypeMock));
    }
    
    @Test
    public void testAnswerWithNonImposterisableType() throws Throwable {
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        
        Method methodMock = mock(Method.class);
        when(invocationMock.getMethod()).thenReturn(methodMock);
        
        Class<?> returnTypeMock = mock(Class.class);
        when(methodMock.getReturnType()).thenReturn(returnTypeMock);
        
        ClassImposterizer imposterizerMock = mock(ClassImposterizer.class);
        when(ClassImposterizer.INSTANCE).thenReturn(imposterizerMock);
        
        when(imposterizerMock.canImposterise(returnTypeMock)).thenReturn(false);
        
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        
        Object result = returnsSmartNulls.answer(invocationMock);
        
        assertNull(result);
        verify(invocationMock).getMethod();
        verify(methodMock).getReturnType();
        verify(methodMock).getName();
        verify(invocationMock).getArguments();
        verify(returnTypeMock).getName();
        verify(imposterizerMock).canImposterise(returnTypeMock);
    }
}