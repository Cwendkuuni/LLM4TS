package org.mockito.internal.tests;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<DummyMock> mockHandler;
 private DummyMock dummyMock;

 @Before
 public void setUp() {
 dummyMock = Mockito.mock(DummyMock.class);
 mockHandler = new MockHandler<>();
 }

 @Test
 public void testHandleVoidMethodStubbing() {
 when(dummyMock.voidMethod(ArgumentMatchers.anyInt())).thenCallRealMethod();

 mockHandler.voidMethodStubbable(dummyMock).stubVoid();

 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setTarget(dummyMock);
 invocation.setMethod(DummyMock.class.getMethod("voidMethod", int.class));
 invocation.setArguments(new Object[]{42});

 mockHandler.handle(invocation);

 // verify no interaction with the mocked method
 verify(dummyMock).voidMethod(ArgumentMatchers.anyInt());
 }

 @Test
 public void testHandleAnswerStubbing() {
 final String expectedResult = "hello";

 when(dummyMock.stringMethod(ArgumentMatchers.anyString())).thenAnswer(new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return expectedResult;
 }
 });

 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setTarget(dummyMock);
 invocation.setMethod(DummyMock.class.getMethod("stringMethod", String.class));
 invocation.setArguments(new Object[]{"world"});

 Object result = mockHandler.handle(invocation);

 // verify the answer was returned
 verify(dummyMock).stringMethod(ArgumentMatchers.eq("world"));
 assertEquals(expectedResult, result);
 }

 @Test(expected = RuntimeException.class)
 public void testHandleVerificationMode() {
 mockHandler.mockingProgress.pushVerificationMode(new MockAwareVerificationMode() {
 @Override
 public void verify(VerificationDataImpl verificationData) {
 throw new RuntimeException("VerificationMode should not be triggered in handle() method");
 }

 @Override
 public MockHandlerInterface<?> getMock() {
 return dummyMock;
 }
 });

 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setTarget(dummyMock);
 invocation.setMethod(DummyMock.class.getMethod("voidMethod", int.class));
 invocation.setArguments(new Object[]{42});

 mockHandler.handle(invocation);
 }

 @Test(expected = RuntimeException.class)
 public void testHandleWrongMock() {
 Mockito.mock(OtherDummyMock.class);

 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setTarget(dummyMock);
 invocation.setMethod(DummyMock.class.getMethod("voidMethod", int.class));
 invocation.setArguments(new Object[]{42});

 mockHandler.handle(invocation);
 }

 @Test
 public void testHandleDefaultAnswer() {
 when(dummyMock.intMethod(ArgumentMatchers.anyInt())).thenCallRealMethod();

 Object result = mockHandler.handle(Mockito.mock(InvocationOnMock.class).setTarget(dummyMock)
 .setMethod(DummyMock.class.getMethod("intMethod", int.class))
 .setArguments(new Object[]{42}));

 // verify the real method was called
 verify(dummyMock).intMethod(ArgumentMatchers.eq(42));

 // verify the default answer was returned
 assertEquals(42, result);
 }

 @Test
 public void testHandleSetAnswersForStubbing() {
 List<Answer> answers = new ArrayList<>();
 Answer answer = new Answer() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "answer";
 }
 };
 answers.add(answer);

 mockHandler.setAnswersForStubbing(answers);

 when(dummyMock.stringMethod(ArgumentMatchers.anyString())).thenCallRealMethod();

 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.setTarget(dummyMock);
 invocation.setMethod(DummyMock.class.getMethod("stringMethod", String.class));
 invocation.setArguments(new Object[]{"world"});

 Object result = mockHandler.handle(invocation);

 // verify the answer was returned
 verify(dummyMock).stringMethod(ArgumentMatchers.eq("world"));
 assertEquals("answer", result);
 }
}

class DummyMock {
 void voidMethod(int i) {
 }

 String stringMethod(String s) {
 return "result";
 }

 int intMethod(int i) {
 return i * 2;
 }
}