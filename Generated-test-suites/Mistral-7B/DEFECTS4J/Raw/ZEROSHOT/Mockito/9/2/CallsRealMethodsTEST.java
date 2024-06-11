package org.mockito.internal.stubbing.answers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(org.mockito.runners.JUnit4.class)
public class CallsRealMethodsTest {

 private CallsRealMethods callRealMethods;
 private Object realObject;

 @Before
 public void setUp() {
 realObject = mock(Object.class);
 callRealMethods = new CallsRealMethods();
 }

 @Test
 public void testCallRealMethod() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setTarget(realObject);
 invocation.setMethod(ArgumentMatchers.anyMethod());
 invocation.setArguments(new Object[0]);

 Object result = callRealMethods.answer(invocation);

 // Assert that the real method was called
 verify(realObject).methodCall();

 // Assert that the answer returned the result of the real method call
 assertEquals(result, realObject.methodCall());
 }

 @Test(expected = Throwable.class)
 public void testCallRealMethodWithException() throws Throwable {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setTarget(realObject);
 invocation.setMethod(ArgumentMatchers.anyMethod());
 invocation.setArguments(new Object[0]);

 doThrow(new RuntimeException("Test Exception")).when(realObject).methodCall();

 callRealMethods.answer(invocation);
 }
}