package org.mockito.internal.stubbing.answers.test;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;

import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willCallRealMethod;
import static org.mockito.Mockito.mock;

public class CallsRealMethodsTest {

 private Object realObject;
 private CallsRealMethods answer;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 realObject = mock(Object.class);
 answer = new CallsRealMethods();
 }

 @Test
 public void testCallsRealMethod_whenInvoked_thenReturnsResultOfRealMethod() throws Exception {
 given(realObject.toString()).willReturn("Mockito");

 Object result = answer.answer(Mockito.argThat(ArgumentMatchers.isInstance(realObject)));

 assertEquals("Mockito", result);

 // Verify that the real method was called
 then(realObject).should().toString();
 }

 @Test(expected = InvocationTargetException.class)
 public void testCallsRealMethod_whenInvokedWithException_thenThrowsException() throws Throwable {
 given(realObject.toString()).willThrow(new RuntimeException("Exception"));

 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setTarget(realObject);
 invocation.setMethod(Mockito.method(Object.class, "toString"));
 invocation.setArguments(new Object[0]);

 try {
 answer.answer(invocation);
 } catch (Throwable e) {
 throw new AssertionError("Should have thrown exception", e);
 }

 assertEquals("Exception", invocation.getInvocationTarget().getMessage());
 }

 @Test
 public void testCallsRealMethod_whenInvokedWithNull_thenNullIsPassedToRealMethod() {
 Object result = answer.answer(null);

 // Verify that the real method was called with null argument
 then(realObject).should().toString();

 assertEquals(null, result);
 }
}