package org.mockito.internal.stubbing.answers.test;

import org.junit.jupiter.api.Assertions;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.CallsRealMethods;

class CallsRealMethodsTest {

 // Testing constructor
 CallsRealMethods testInstance = new CallsRealMethods();

 // Testing answer method with void return type
 @org.junit.jupiter.api.Test
 void testAnswerWithVoidReturnType() {
   Object mock = Mockito.mock(Object.class);
   CallsRealMethods callsRealMethods = Mockito.spy(new CallsRealMethods());

   Runnable runnable = () -> {};

   callsRealMethods.answer(Mockito.argThat(invocation -> {
     Assertions.assertEquals(mock.getClass(), invocation.getTarget().getClass());
     Assertions.assertEquals(runnable.getClass(), invocation.getMethod().getDeclaringClass());
     Assertions.assertEquals("run", invocation.getMethod().getName());
     invocation.callRealMethod();
     return null;
   }));

   callsRealMethods.answer(new InvocationOnMock(mock, runnable));
   mock.run();
 }

 // Testing answer method with Object return type
 @org.junit.jupiter.api.Test
 Object testAnswerWithObjectReturnType() {
   Object mock = Mockito.mock(String.class);
   CallsRealMethods callsRealMethods = Mockito.spy(new CallsRealMethods());

   String result = "test";

   Object returnedObject = callsRealMethods.answer(Mockito.argThat(invocation -> {
     Assertions.assertEquals(mock.getClass(), invocation.getTarget().getClass());
     Assertions.assertEquals(String.class, invocation.getMethod().getReturnType());
     Assertions.assertEquals("toString", invocation.getMethod().getName());
     return invocation.callRealMethod();
   }));

   Mockito.when(mock.toString()).thenReturn(result);
   Mockito.doReturn(callsRealMethods).when(mock).answerOnCallsToRealMethodsWith(ArgumentMatchers.any());

   Assertions.assertEquals(result, (String) callsRealMethods.answer(new InvocationOnMock(mock, null)));
 }

 // Testing answer method with throwable return type
 @org.junit.jupiter.api.Test
 void testAnswerWithThrowableReturnType() {
   Object mock = Mockito.mock(Exception.class);
   CallsRealMethods callsRealMethods = Mockito.spy(new CallsRealMethods());

   RuntimeException exception = new RuntimeException("Test Exception");

   Assertions.assertThrows(RuntimeException.class, () -> {
     callsRealMethods.answer(Mockito.argThat(invocation -> {
       Assertions.assertEquals(mock.getClass(), invocation.getTarget().getClass());
       Assertions.assertEquals(RuntimeException.class, invocation.getMethod().getReturnType());
       Assertions.assertEquals("printStackTrace", invocation.getMethod().getName());
       throw (RuntimeException) invocation.callRealMethod();
     }));

     callsRealMethods.answer(new InvocationOnMock(mock, null));
     mock.printStackTrace();
   });
 }
}