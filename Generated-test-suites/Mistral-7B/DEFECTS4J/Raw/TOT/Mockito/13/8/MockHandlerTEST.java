package org.mockito.internal.test.mockhandler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.internal.matchers.ArgumentMatchersExt.*;
import static org.mockito.internal.verification.VerificationMode.*;

public class MockHandlerTest {

 private MockHandler<DummyClass> mockHandler;
 private DummyClass mock;

 @Before
 public void setup() {
 mock = mock(DummyClass.class);
 mockHandler = new MockHandler<DummyClass>(mock);
 }

 @Test
 public void testHandle_voidMethod() {
 // given
 when(mock.voidMethod()).thenReturn(true);

 // when & then
 mockHandler.handle(new InvocationOnMock(mock, "voidMethod"));
 verify(mock, times(1)).voidMethod();
 }

 @Test
 public void testHandle_methodWithArguments() {
 // given
 when(mock.methodWithArguments(1, 2, "three")).thenReturn("result");

 // when & then
 Object result = mockHandler.handle(new InvocationOnMock(mock, "methodWithArguments", 1, 2, "three"));
 assertEquals("result", result);
 verify(mock, times(1)).methodWithArguments(eq(1), eq(2), eq("three"));
 }

 @Test
 public void testHandle_verification() {
 // given
 verify(mock).methodWithArguments(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString());

 // when
 mockHandler.handle(new InvocationOnMock(mock, "methodWithArguments", 1, "test"));

 // then
 verify(mock, times(1)).methodWithArguments(eq(1), eq("test"));
 }

 @Test(expected = RuntimeException.class)
 public void testHandle_verificationOnDifferentMock() {
 // given
 MockHandler<DummyClass2> otherMockHandler = new MockHandler<DummyClass2>(mock(DummyClass2.class));
 verify(mock).methodWithArguments(ArgumentMatchers.anyInt(), ArgumentMatchers.anyString());

 // when & then (should throw exception)
 otherMockHandler.handle(new InvocationOnMock(mock, "methodWithArguments", 1, "test"));
 }

 @Test
 public void testHandle_answersForStubbing() {
 // given
 List<Answer> answers = Arrays.asList(answers(1), answers(2), answers("result"));
 mockHandler.setAnswersForStubbing(answers);

 // when
 Object result1 = mockHandler.handle(new InvocationOnMock(mock, "methodWithArguments", 1, 2, "three"));
 Object result2 = mockHandler.handle(new InvocationOnMock(mock, "methodWithArguments", 4, "test"));

 // then
 assertEquals(1, result1);
 assertEquals(2, result2);
 }
}

class DummyClass {
 void voidMethod() {
 }

 String methodWithArguments(int arg1, String arg2) {
 return "";
 }
}

class DummyClass2 {
 String methodWithArguments(int arg1, String arg2) {
 return "";
 }
}