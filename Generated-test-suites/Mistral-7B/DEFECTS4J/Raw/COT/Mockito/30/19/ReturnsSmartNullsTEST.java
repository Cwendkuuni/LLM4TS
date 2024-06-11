package org.mockito.internal.stubbing.defaultanswers.test;

import static org.junit.Assert.*;

import org.mockito.Answer;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls;

import java.lang.reflect.Method;
import java.util.Arrays;

public class ReturnsSmartNullsTest {

 private ReturnsSmartNulls returnsSmartNulls;

 @Before
 public void setUp() {
 returnsSmartNulls = new ReturnsSmartNulls();
 }

 // Testing the constructor
 @Test
 public void testReturnsSmartNullsInstanceIsCreated() {
 assertNotNull(returnsSmartNulls);
 }

 // Testing answer method with null argument
 @Test
 public void testAnswerNullArgument() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 try {
 returnsSmartNulls.answer(invocation);
 fail("An exception should be thrown when answer method is called with null argument");
 } catch (NullPointerException e) {
 // Expected exception
 }
 }

 // Testing answer method with method that returns String
 @Test
 public void testAnswerStringMethod() {
 Method method = Mockito.method(String.class, "toString");
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 when(invocation.getMethod()).thenReturn(method);
 when(method.getReturnType()).thenReturn(String.class);
 Object result = returnsSmartNulls.answer(invocation);
 assertEquals("SmartNull returned by unstubbed method on mock", result.toString());
 }

 // Testing answer method with method that returns null
 @Test
 public void testAnswerNullMethod() {
 Method method = Mockito.method(Object.class, "getNullMethod", new Class[]{}, new Class[]{Object.class});
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 when(invocation.getMethod()).thenReturn(method);
 when(method.getName()).thenReturn("getNullMethod");
 when(method.getReturnType()).thenReturn(Object.class);
 Object result = returnsSmartNulls.answer(invocation);
 assertNull(result);
 }

 // Testing answer method with method that returns non-mockable object
 @Test
 public void testAnswerNonMockableObjectMethod() {
 Method method = Mockito.method(int[].class, "getClass", new Class[0]);
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 when(invocation.getMethod()).thenReturn(method);
 when(method.getName()).thenReturn("getClass");
 when(method.getReturnType()).thenReturn(int[].class);
 try {
 returnsSmartNulls.answer(invocation);
 fail("An exception should be thrown when answer method is called with non-mockable object method");
 } catch (UnsupportedOperationException e) {
 // Expected exception
 }
 }

 // Testing answer method with method that throws checked exception
 @Test
 public void testAnswerCheckedExceptionMethod() throws Exception {
 Method method = Mockito.method(String.class, "getCheckedExceptionMethod", new Class[]{}, new Class[]{});
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 when(invocation.getMethod()).thenReturn(method);
 when(method.getName()).thenReturn("getCheckedExceptionMethod");
 when(method.getReturnType()).thenReturn(String.class);
 when(method.getDeclaringClass()).thenReturn(String.class);
 try {
 ClassImposterizer.INSTANCE.imposterise(new ThrowingInterceptor(invocation), method.getReturnType());
 fail("An exception should be thrown when trying to imposterise method that throws checked exception");
 } catch (RuntimeException e) {
 // Expected exception
 }
 }

 // Testing answer method with method that throws unchecked exception
 @Test
 public void testAnswerUncheckedExceptionMethod() throws Exception {
 Method method = Mockito.method(String.class, "getUncheckedExceptionMethod", new Class[]{}, new Class[]{});
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 when(invocation.getMethod()).thenReturn(method);
 when(method.getName()).thenReturn("getUncheckedExceptionMethod");
 when(method.getReturnType()).thenReturn(String.class);
 when(method.getDeclaringClass()).thenReturn(String.class);
 try {
 ClassImposterizer.INSTANCE.imposterise(new ThrowingInterceptor(invocation), method.getReturnType());
 Object result = returnsSmartNulls.answer(invocation);
 assertEquals("SmartNull returned by unstubbed method on mock", result.toString());
 } catch (Exception e) {
 fail("An exception should not be thrown when answer method is called with method that throws unchecked exception");
 }
 }

 // Testing answer method with method that returns array
 @Test
 public void testAnswerArrayMethod() {
 Method method = Mockito.method(int[].class, "getValue", new Class[]{int.class});
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 when(invocation.getMethod()).thenReturn(method);
 when(method.getName()).thenReturn("getValue");
 when(method.getReturnType()).thenReturn(int[].class);
 when(method.getParameterTypes()[0]).thenReturn(int.class);
 Object[] result = returnsSmartNulls.answer(invocation);
 assertArrayEquals(new int[0], result);
 }

 // Testing answer method with method that returns primitive type
 @Test
 public void testAnswerPrimitiveTypeMethod() {
 Method method = Mockito.method(int.class, "getValue", new Class[]{}, new Class[]{});
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 when(invocation.getMethod()).thenReturn(method);
 when(method.getName()).thenReturn("getValue");
 when(method.getReturnType()).thenReturn(int.class);
 int result = (int) returnsSmartNulls.answer(invocation);
 assertEquals(0, result);
 }
}