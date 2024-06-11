import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswerWithDefaultReturnValue() throws Throwable {
        // Arrange
        ReturnsMoreEmptyValues delegateMock = mock(ReturnsMoreEmptyValues.class);
        when(delegateMock.answer(any(InvocationOnMock.class))).thenReturn(null);
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls(delegateMock);
        
        // Act
        Object result = smartNulls.answer(mock(InvocationOnMock.class));
        
        // Assert
        assertNull(result);
    }
    
    @Test
    public void testAnswerWithMockableReturnType() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
        ClassImposterizer classImposterizerMock = mock(ClassImposterizer.class);
        when(classImposterizerMock.canImposterise(any(Class.class))).thenReturn(true);
        ClassImposterizer.setMock(classImposterizerMock);
        ThrowingInterceptor throwingInterceptorMock = mock(ThrowingInterceptor.class);
        when(classImposterizerMock.imposterise(throwingInterceptorMock, Object.class)).thenReturn(mock(Object.class));
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
        
        // Act
        Object result = smartNulls.answer(invocation);
        
        // Assert
        assertNotNull(result);
        verify(classImposterizerMock).canImposterise(Object.class);
        verify(classImposterizerMock).imposterise(throwingInterceptorMock, Object.class);
    }
    
    @Test
    public void testAnswerWithUnmockableReturnType() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().getReturnType()).thenReturn(Void.class);
        ClassImposterizer classImposterizerMock = mock(ClassImposterizer.class);
        when(classImposterizerMock.canImposterise(any(Class.class))).thenReturn(false);
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
        
        // Act
        Object result = smartNulls.answer(invocation);
        
        // Assert
        assertNull(result);
        verify(classImposterizerMock).canImposterise(Void.class);
    }
    
    @Test
    public void testAnswerWithToStringMethod() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(invocation.getMethod().getName()).thenReturn("toString");
        Reporter reporterMock = mock(Reporter.class);
        doNothing().when(reporterMock).smartNullPointerException(any(Location.class));
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
        
        // Act
        Object result = smartNulls.answer(invocation);
        
        // Assert
        assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
        verify(reporterMock).smartNullPointerException(any(Location.class));
    }
}