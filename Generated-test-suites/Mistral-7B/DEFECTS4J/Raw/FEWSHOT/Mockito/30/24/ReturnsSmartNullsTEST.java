package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;

public class ReturnsSmartNullsTest {

 private static final Class<?> MOCKABLE_TYPE = Object.class;
 private static final Class<?> FINAL_TYPE = String.class;

 @Test
 public void testAnswerWithDefaultReturnValue() {
 Mockito.<Object>when(Mockito.mock(MOCKABLE_TYPE)).thenAnswer(new ReturnsSmartNulls());
 Object mock = Mockito.mock(MOCKABLE_TYPE);
 assertNotNull(ReturnsSmartNulls.class.cast(Mockito.mock(MOCKABLE_TYPE, Answer.class)).answer(ArgumentMatchers.any(InvocationOnMock.class)));
 }

 @Test
 public void testAnswerWithNullReturnValue() {
 Mockito.<Object>when(Mockito.mock(MOCKABLE_TYPE)).thenAnswer(new ReturnsSmartNulls());
 Object mock = Mockito.mock(MOCKABLE_TYPE);
 assertNull(ReturnsSmartNulls.class.cast(Mockito.mock(MOCKABLE_TYPE, Answer.class)).answer(ArgumentMatchers.any(InvocationOnMock.class)));
 }

 @Test
 public void testAnswerWithSmartNullForStringReturnType() {
 Mockito.<Object>when(Mockito.mock(String.class)).thenAnswer(new ReturnsSmartNulls());
 String mock = Mockito.mock(String.class);
 assertNotNull(ReturnsSmartNulls.class.cast(Mockito.mock(String.class, Answer.class)).answer(ArgumentMatchers.any(InvocationOnMock.class)));
 }

 @Test(expected = org.mockito.exceptions.SmartNullPointerException.class)
 public void testAnswerWithSmartNullForUnstubbedStringMethod() {
 String expected = "SmartNull returned by unstubbed method";
 Mockito.<Object>when(Mockito.mock(String.class)).thenAnswer(new ReturnsSmartNulls());
 String mock = Mockito.mock(String.class);
 String result = (String) ReturnsSmartNulls.class.cast(Mockito.mock(String.class, Answer.class)).answer(new InvocationOnMock(Mockito.mock(Object.class), new MethodStub("toString"), new Object[0], null));
 assertEquals(expected, result);
 }

 @Test(expected = org.mockito.exceptions.SmartNullPointerException.class)
 public void testAnswerWithSmartNullForUnstubbedMethodWithArguments() {
 String expected = "SmartNull returned by unstubbed method with arguments [1, 2, 3]";
 Mockito.<Object>when(Mockito.mock(String.class)).thenAnswer(new ReturnsSmartNulls());
 String mock = Mockito.mock(String.class);
 String result = (String) ReturnsSmartNulls.class.cast(Mockito.mock(String.class, Answer.class)).answer(new InvocationOnMock(Mockito.mock(Object.class), new MethodStub("methodWithArguments"), new Object[]{1, 2, 3}, null));
 assertEquals(expected, result);
 }

 @Test
 public void testAnswerWithNullForFinalType() {
 Mockito.<Object>when(Mockito.mock(FINAL_TYPE)).thenAnswer(new ReturnsSmartNulls());
 Object mock = Mockito.mock(FINAL_TYPE);
 assertNull(ReturnsSmartNulls.class.cast(Mockito.mock(FINAL_TYPE, Answer.class)).answer(ArgumentMatchers.any(InvocationOnMock.class)));
 }

}

 class MethodStub implements MethodInterceptor {
 private final Method method;
 private final Object[] arguments;

 MethodStub(Method method, Object[] arguments) {
 this.method = method;
 this.arguments = arguments;
 }

 public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
 throw new UnsupportedOperationException();
 }

 public Method getMethod() {
 return this.method;
 }
 }