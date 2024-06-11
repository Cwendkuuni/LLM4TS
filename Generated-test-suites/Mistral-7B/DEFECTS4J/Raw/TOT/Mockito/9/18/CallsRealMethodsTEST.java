package org.mockito.internal.stubbing.answers.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*; // for given, then, and when

public class CallsRealMethodsTest {

 private CallsRealMethods callRealMethods;
 private Object realObject;

 @Before
 public void setup() {
 realObject = new Object() {
 public String methodWithReturnTypeString() {
 return "Real Method";
 }
 };
 callRealMethods = new CallsRealMethods();
 }

 @Test
 public void testCallsRealMethodWithStringReturnType() {
 given(callRealMethods).answers(callRealMethods); // Set up callRealMethods as a mock with this answer

 when(callRealMethods.answer(ArgumentMatchers.<InvocationOnMock>any())).thenReturn(realObject);

 then(callRealMethods).should().answer(Mockito.any(InvocationOnMock.class)); // Verify that callRealMethods.answer was called

 assertEquals("Real Method", callRealMethods.answer(Mockito.<InvocationOnMock>any())); // Test the return value of the callRealMethods.answer
 }

 @Test(expected = Throwable.class)
 public void testCallsRealMethodWithException() {
 realObject = new Object() {
 public String methodWithException() throws Exception {
 throw new RuntimeException("Expected Exception");
 }
 };

 given(callRealMethods).answers(callRealMethods);

 when(callRealMethods.answer(ArgumentMatchers.<InvocationOnMock>any())).thenReturn(realObject);

 try {
 callRealMethods.answer(Mockito.<InvocationOnMock>any());
 fail("Expected Exception not thrown");
 } catch (RuntimeException e) {
 assertEquals("Expected Exception", e.getMessage()); // Test that the expected exception is thrown
 }
 }

 @Test
 public void testCallsRealMethodWithNullArgument() {
 given(callRealMethods).answers(callRealMethods);

 when(callRealMethods.answer(Mockito.<InvocationOnMock>any())).thenReturn(null);

 assertNull(callRealMethods.answer(Mockito.<InvocationOnMock>any())); // Test that null is returned
 }
}