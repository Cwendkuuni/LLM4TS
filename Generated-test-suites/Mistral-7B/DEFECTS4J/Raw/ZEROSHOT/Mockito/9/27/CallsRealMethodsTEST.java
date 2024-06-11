package org.mockito.internal.stubbing.answers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CallsRealMethodsTest {

 // Testing the default constructor
 @Test
 public void testCallsRealMethodsDefaultConstructor() {
     CallsRealMethods callsRealMethods = new CallsRealMethods();
 }

 // Testing the answer method with a null invocation
 @Test(expected = IllegalArgumentException.class)
 public void testCallsRealMethodsNullInvocation(InvocationOnMock invocation) {
     CallsRealMethods callsRealMethods = new CallsRealMethods();
     callsRealMethods.answer(null);
 }

 // Testing the answer method with a valid invocation and checking if the real method is called
 @Test
 public void testCallsRealMethodsRealMethodCalled(InvocationOnMock invocation) throws Throwable {
     Object mockObject = mock(Object.class);
     CallsRealMethods callsRealMethods = new CallsRealMethods();
     Object result = callsRealMethods.answer(invocation);
     verify(mockObject).callRealMethod();
     assertNotNull(result);
 }

 // Testing the answer method with a valid invocation and checking if the return value is the same as the result of the real method call
 @Test
 public void testCallsRealMethodsSameReturnValue(InvocationOnMock invocation) throws Throwable {
     Object mockObject = mock(Object.class);
     CallsRealMethods callsRealMethods = new CallsRealMethods();
     Object expectedResult = new Object();
     when(mockObject.someMethod(any())).thenReturn(expectedResult);
     Object result = callsRealMethods.answer(invocationOn(mockObject.someMethod(any()))).get();
     assertEquals(expectedResult, result);
 }

 // Testing the answer method with a valid invocation and checking if an exception thrown by the real method is propagated
 @Test(expected = RuntimeException.class)
 public void testCallsRealMethodsThrowsException(InvocationOnMock invocation) throws Throwable {
     Object mockObject = mock(Object.class);
     CallsRealMethods callsRealMethods = new CallsRealMethods();
     doThrow(new RuntimeException("Test exception")).when(mockObject).someMethod(any());
     callsRealMethods.answer(invocationOn(mockObject.someMethod(any())));
 }
}