package org.mockito.internal.tests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMockException;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.BDDMockito.*; // for thenAnswer

public class MockHandlerTest {

 private MockHandler<MyMockType> mockHandler;
 private MyMockType mock;

 @Before
 public void setup() {
 mockHandler = new MockHandler<>();
 mock = Mockito.mock(MyMockType.class);
 }

 @Test
 public void testStubbingVoidMethod() {
 // Given
 when(mock.voidMethod()).thenCallRealMethod();
 doThrow(new RuntimeException("Test exception")).when(mock).voidMethod();

 // When
 // This should not throw any exception as we're stubbing a void method
 mockHandler.voidMethodStubbable(mock).voidMethod();

 // Then
 // No verification is needed as we're stubbing a void method
 // We can check the side effect of the stub (throwing an exception in this case)
 assertEquals(1, mockingProgress().getNumberOfThrownExceptions());
 }

 @Test
 public void testStubbingNonVoidMethod() {
 // Given
 final int expectedReturnValue = 42;
 when(mock.intMethod(anyInt())).thenReturn(expectedReturnValue);

 // When - Then
 then(mock).intMethod(ArgumentMatchers.eq(5)).shouldReturn(expectedReturnValue);
 // Now we can call the real method and check its return value
 assertEquals(expectedReturnValue, mock.intMethod(5));
 }

 @Test(expected = InvocationOnMockException.class)
 public void testVerificationOnDifferentMock() {
 // Given
 MyMockType mock2 = Mockito.mock(MyMockType.class);
 doThrow(new RuntimeException("Test exception")).when(mock).intMethod(ArgumentMatchers.eq(5));

 // When - Then
 // This should throw InvocationOnMockException as the verification is for a different mock
 verify(mock2).intMethod(ArgumentMatchers.eq(5));
 }

 @Test
 public void testVerificationOnSameMock() {
 // Given
 doReturn(new String("Test value")).when(mock).methodWithStringArgument(ArgumentMatchers.eq("Test arg"));

 // When - Then
 verify(mock).methodWithStringArgument(ArgumentMatchers.eq("Test arg"));
 then(mock).methodWithStringArgument(ArgumentMatchers.eq("Test arg")).shouldHaveBeenCalled();
 }

 @Test
 public void testAnswerWithArgumentsCapture() {
 // Given
 final List<String> capturedList = new ArrayList<>();
 Answer<String> answer = arguments -> capturedList.add(arguments[0]);

 // When - Then
 doAnswer(answer).when(mock).methodWithStringArgument(ArgumentMatchers.anyString());

 // Now we can call the method and check the captured list
 mock.methodWithStringArgument("Test arg");
 assertEquals("Test arg", capturedList.get(0));
 }

 @Test
 public void testDefaultAnswer() {
 // Given
 when(mock.defaultMethod()).thenCallRealMethod();

 // When
 Object result = mockHandler.handle(new InvocationOnMock(mock, "defaultMethod", new Object[0]));

 // Then
 assertEquals(mockSettings().getDefaultAnswer().answer(new InvocationOnMock(mock, "defaultMethod", new Object[0])), result);
 }
}