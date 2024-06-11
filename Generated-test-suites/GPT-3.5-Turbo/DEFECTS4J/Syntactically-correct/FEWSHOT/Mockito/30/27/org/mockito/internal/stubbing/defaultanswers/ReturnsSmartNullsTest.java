package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.cglib.proxy.MethodInterceptor;
import org.mockito.cglib.proxy.MethodProxy;
import org.mockito.exceptions.Reporter;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {
  
  @Test
  public void testAnswer() throws Throwable {
    ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
    ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls(delegate);
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    Object defaultReturnValue = new Object();
    
    when(delegate.answer(invocation)).thenReturn(defaultReturnValue);
    
    Object result = returnsSmartNulls.answer(invocation);
    
    assertEquals(defaultReturnValue, result);
    verify(delegate).answer(invocation);
  }
  
  @Test
  public void testAnswerNullDefaultReturnValue() throws Throwable {
    ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
    ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls(delegate);
    InvocationOnMock invocation = mock(InvocationOnMock.class);

    when(delegate.answer(invocation)).thenReturn(null);
    
    Object result = returnsSmartNulls.answer(invocation);
    
    assertNull(result);
    verify(delegate).answer(invocation);
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void testAnswerClassImposterizer() throws Throwable {
    ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
    ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls(delegate);
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    MethodProxy methodProxy = mock(MethodProxy.class);
    Class<?> type = TestClass.class;
    
    when(delegate.answer(invocation)).thenReturn(null);
    when(invocation.getMethod()).thenReturn(TestClass.class.getDeclaredMethod("testMethod"));
    when(TestClass.class.getDeclaredMethod("testMethod")).thenReturn(TestClass.class.getDeclaredMethod("testMethod"));
    when(invocation.getMethod().getReturnType()).thenReturn(type);
    when(ClassImposterizer.INSTANCE.canImposterise(type)).thenReturn(true);
    when(ClassImposterizer.INSTANCE.imposterise(any(MethodInterceptor.class), eq(type))).thenReturn(mock(TestClass.class, RETURNS_DEFAULTS));
    
    Object result = returnsSmartNulls.answer(invocation);
    
    assertNotNull(result);
    assertTrue(result instanceof TestClass);
    verify(delegate).answer(invocation);
    verify(invocation).getMethod();
    verify(TestClass.class.getDeclaredMethod("testMethod"));
    verify(invocation.getMethod()).getReturnType();
    verify(ClassImposterizer.INSTANCE).canImposterise(type);
    verify(ClassImposterizer.INSTANCE).imposterise(any(MethodInterceptor.class), eq(type));
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void testAnswerClassImposterizerCannotImposterise() throws Throwable {
    ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
    ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls(delegate);
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    MethodProxy methodProxy = mock(MethodProxy.class);
    Class<?> type = TestClass.class;
    
    when(delegate.answer(invocation)).thenReturn(null);
    when(invocation.getMethod()).thenReturn(TestClass.class.getDeclaredMethod("testMethod"));
    when(TestClass.class.getDeclaredMethod("testMethod")).thenReturn(TestClass.class.getDeclaredMethod("testMethod"));
    when(invocation.getMethod().getReturnType()).thenReturn(type);
    when(ClassImposterizer.INSTANCE.canImposterise(type)).thenReturn(false);
    
    Object result = returnsSmartNulls.answer(invocation);
    
    assertNull(result);
    verify(delegate).answer(invocation);
    verify(invocation).getMethod();
    verify(TestClass.class.getDeclaredMethod("testMethod"));
    verify(invocation.getMethod()).getReturnType();
    verify(ClassImposterizer.INSTANCE).canImposterise(type);
    verifyZeroInteractions(ClassImposterizer.INSTANCE);
  }
  
  @SuppressWarnings("unchecked")
  @Test
  public void testAnswerToStringMethod() throws Throwable {
    ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
    ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls(delegate);
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    MethodProxy methodProxy = mock(MethodProxy.class);
    
    when(delegate.answer(invocation)).thenReturn(null);
    when(invocation.getMethod()).thenReturn(TestClass.class.getDeclaredMethod("toString"));
    when(invocation.getArguments()).thenReturn(new Object[0]);
    when(new ObjectMethodsGuru().isToString(invocation.getMethod())).thenReturn(true);
    
    Object result = returnsSmartNulls.answer(invocation);
    
    assertEquals("SmartNull returned by unstubbed toString method on mock", result);
    verify(delegate).answer(invocation);
    verify(invocation).getMethod();
    verify(invocation).getArguments();
    verify(new ObjectMethodsGuru()).isToString(invocation.getMethod());
    verifyZeroInteractions(ClassImposterizer.INSTANCE);
  }

  private static class TestClass {
    public void testMethod() {
    }
  }
}