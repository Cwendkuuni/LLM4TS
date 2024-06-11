package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import org.mockito.exceptions.Reporter;
import org.mockito.internal.creation.jmock.ClassImposterizer;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsSmartNullsTest {
    
    @Before
    public void setUp() {
        when(mockInvocation.getMethod()).thenReturn(mockMethod);
        when(mockMethod.getReturnType()).thenReturn(String.class);
        when(mockMethod.getName()).thenReturn("testMethod");
    }
    
    @Mock
    InvocationOnMock mockInvocation;
    
    @Mock
    Method mockMethod;
    
    @Test
    public void testAnswer_OrdinaryReturnValue() throws Throwable {
        // Arrange
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        when(delegate.answer(mockInvocation)).thenReturn("returnValue");
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls(delegate);
        
        // Act
        Object result = returnsSmartNulls.answer(mockInvocation);
        
        // Assert
        assertEquals("returnValue", result);
        verify(mockInvocation).getMethod();
        verify(mockMethod).getReturnType();
        verify(mockMethod).getName();
        verifyZeroInteractions(ClassImposterizer.INSTANCE);
    }
    
    @Test
    public void testAnswer_NullReturnValue() throws Throwable {
        // Arrange
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        when(delegate.answer(mockInvocation)).thenReturn(null);
        when(mockInvocation.getMethod()).thenReturn(mockMethod);
        when(mockMethod.getReturnType()).thenReturn(String.class);
        ClassImposterizer imposterizer = mock(ClassImposterizer.class);
        when(imposterizer.canImposterise(String.class)).thenReturn(true);
        when(imposterizer.imposterise(any(MethodInterceptor.class), eq(String.class))).thenReturn("SmartNull");
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls(delegate);
        
        // Act
        Object result = returnsSmartNulls.answer(mockInvocation);
        
        // Assert
        assertEquals("SmartNull", result);
        verify(mockInvocation).getMethod();
        verify(mockMethod, times(2)).getReturnType();
        verify(mockMethod).getName();
        verify(imposterizer).canImposterise(String.class);
        verify(imposterizer).imposterise(any(MethodInterceptor.class), eq(String.class));
    }
    
    @Test
    public void testAnswer_NullReturnValue_UnableImposterize() throws Throwable {
        // Arrange
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        when(delegate.answer(mockInvocation)).thenReturn(null);
        when(mockInvocation.getMethod()).thenReturn(mockMethod);
        when(mockMethod.getReturnType()).thenReturn(Integer.class);
        ClassImposterizer imposterizer = mock(ClassImposterizer.class);
        when(imposterizer.canImposterise(Integer.class)).thenReturn(false);
        when(imposterizer.imposterise(any(MethodInterceptor.class), eq(Integer.class))).thenReturn(null);
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls(delegate);
        
        // Act
        Object result = returnsSmartNulls.answer(mockInvocation);
        
        // Assert
        assertNull(result);
        verify(mockInvocation).getMethod();
        verify(mockMethod, times(2)).getReturnType();
        verify(mockMethod).getName();
        verify(imposterizer).canImposterise(Integer.class);
        verifyZeroInteractions(imposterizer);
    }
    
    @Test
    public void testAnswer_NullReturnValue_ThrowException() throws Throwable {
        // Arrange
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        when(delegate.answer(mockInvocation)).thenReturn(null);
        when(mockInvocation.getMethod()).thenReturn(mockMethod);
        when(mockMethod.getReturnType()).thenReturn(String.class);
        ClassImposterizer imposterizer = mock(ClassImposterizer.class);
        when(imposterizer.canImposterise(String.class)).thenReturn(false);
        when(imposterizer.imposterise(any(MethodInterceptor.class), eq(String.class))).thenReturn(null);
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls(delegate);
        
        // Act & Assert
        assertThrows(Reporter.SmartNullPointerException.class, () -> {
            returnsSmartNulls.answer(mockInvocation);
        });
        verify(mockInvocation).getMethod();
        verify(mockMethod, times(3)).getReturnType();
        verify(mockMethod).getName();
        verify(imposterizer).canImposterise(String.class);
        verifyZeroInteractions(imposterizer);
    }
}