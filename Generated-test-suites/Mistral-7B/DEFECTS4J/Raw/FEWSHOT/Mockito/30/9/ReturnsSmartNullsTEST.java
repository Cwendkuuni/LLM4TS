package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls returnsSmartNulls;

 @Test
 public void testSmartNullReturnedWhenMethodIsNotToString() {
 returnsSmartNulls = Mockito.spy(new ReturnsSmartNulls());
 InvocationOnMock invocation = Mockito.mock(Object.class);
 when(invocation.getMethod()).thenReturn(new MethodStub("toString"));

 assertNull(returnsSmartNulls.answer(invocation));
 }

 @Test
 public void testSmartNullPointerExceptionIsThrownWhenMethodIsToString() {
 returnsSmartNulls = Mockito.spy(new ReturnsSmartNulls());
 InvocationOnMock invocation = Mockito.mock(Object.class);
 when(invocation.getMethod()).thenReturn(new MethodStub("toString"));

 assertNull(returnsSmartNulls.answer(invocation));
 // Test that smart null pointer exception is thrown
 try {
 returnsSmartNulls.answer(invocation);
 } catch (Throwable e) {
 assertEquals("Expected 'SmartNull returned by unstubbed toString method on mock'", e.getMessage());
 }
 }

 @Test
 public void testSmartNullIsReturnedWhenMethodIsNotMockable() {
 returnsSmartNulls = new ReturnsSmartNulls();
 InvocationOnMock invocation = Mockito.mock(Object.class);
 when(invocation.getMethod()).thenReturn(new MethodStub(String[].class.getName()));
 assertNull(returnsSmartNulls.answer(invocation));
 }

 @Test
 public void testOrdinaryReturnValueIsReturnedWhenAvailable() {
 returnsSmartNulls = new ReturnsSmartNulls();
 InvocationOnMock invocation = Mockito.mock(Object.class);
 when(invocation.getMethod()).thenReturn(new MethodStub(Integer.class.getName()));
 when(returnsSmartNulls.delegate.answer(invocation)).thenReturn(1);
 assertEquals(1, returnsSmartNulls.answer(invocation));
 }

 @Test
 public void testSmartNullIsReturnedWhenMethodReturnTypeIsMockable() {
 returnsSmartNulls = new ReturnsSmartNulls();
 InvocationOnMock invocation = Mockito.mock(Object.class);
 when(invocation.getMethod()).thenReturn(new MethodStub(String.class.getName()));
 String smartNull = returnsSmartNulls.answer(invocation);
 assertNotNull(smartNull);
 assertEquals("SmartNull returned by unstubbed method on mock", smartNull.toString());
 }

 private static class MethodStub implements Method {
 private final String name;

 public MethodStub(String name) {
 this.name = name;
 }

 @Override
 public Class<?> getReturnType() {
 return String.class;
 }

 @Override
 public Class<?>[] getParameterTypes() {
 return new Class[0];
 }

 @Override
 public String getName() {
 return name;
 }
 }
}