package org.mockito.internal.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class MockHandlerTest {

 private MockHandler<MockitoTestMock> mockHandler;
 private MockitoTestMock mock;

 @Before
 public void setUp() {
 mock = mock(MockitoTestMock.class);
 mockHandler = new MockHandler<>(mock.getClass());
 }

 @Test
 public void testHandleVoidMethodInvocation() {
 // Given - stubbing void method with stubVoid()
 mockHandler.voidMethodStubbable(mock).stubVoid("methodName");

 // When - invoking the void method
 mockHandler.voidMethodStubbable(mock).methodName();

 // Then - no error is thrown and verification mode is null
 verify(mock, never()).methodName(anyInt());
 }

 @Test
 public void testHandleMethodWithAnswer() {
 // Given - setting an answer for method invocation using doAnswer()
 Answer<String> answer = arguments -> " answer ";
 mockHandler.setAnswersForStubbing(new ArrayList<Answer<?>>() {{
 add(answer);
 }});

 // When - invoking the method
 String result = mockHandler.handle(new InvocationOnMock(mock, "methodName", new Object[0]));

 // Then - the answer is returned and verification mode is null
 assertEquals(" answer ", result);
 verify(mock, never()).methodName(anyInt());
 }

 @Test(expected = IllegalStateException.class)
 public void testHandleMethodWithNoAnswerAndNoVerificationMode() {
 // Given - no answers or verification mode
 mockHandler.handle(new InvocationOnMock(mock, "methodName", new Object[0]));

 // When - invoking the method
 // Then - an IllegalStateException is thrown with a message indicating no answers or verification mode
 }

 @Test
 public void testHandleMethodWithVerificationMode() {
 // Given - setting a verification mode
 verify(mock, times(2)).methodName(anyInt());

 // When - invoking the method
 mockHandler.handle(new InvocationOnMock(mock, "methodName", new Object[0]));

 // Then - the method invocation is verified
 verify(mock, times(2)).methodName(anyInt());
 }
}