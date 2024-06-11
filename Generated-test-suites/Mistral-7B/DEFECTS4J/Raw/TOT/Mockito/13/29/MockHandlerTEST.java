package org.mockito.internal.test.mockhandler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<MockitoTestMock> mockHandler;
 private MockitoTestMock mock;

 @Before
 public void setup() {
 mock = mock(MockitoTestMock.class);
 mockHandler = new MockHandler<>(mock);
 }

 @Test
 public void testHandle_voidMethodStubbing() {
 // given
 when(mock.voidMethod()).thenReturn(null);

 // when
 mockHandler.handle(new InvocationOnMock(mock, "voidMethod"));

 // then
 // no verification or answer is expected in this test case
 }

 @Test
 public void testHandle_answerMethodStubbing() {
 // given
 when(mock.answerMethod()).thenAnswer(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocationOnMock) throws Throwable {
 return 42;
 }
 });

 // when
 int result = (int) mockHandler.handle(new InvocationOnMock(mock, "answerMethod"));

 // then
 verify(mock).answerMethod();
 assertEquals(42, result);
 }

 @Test(expected = AssertionError.class)
 public void testHandle_verificationMode_wrongMock() {
 // given
 VerificationMode verificationMode = Mockito.verify(mock2, Mockito.times(1)); // mock2 is different from mock

 // when
 mockHandler.handle(new InvocationOnMock(mock, "answerMethod"));

 // then
 // AssertionError should be thrown as verification mode is not associated with the mock object passed to handle() method
 }

 @Test
 public void testHandle_verificationMode_correctMock() {
 // given
 VerificationMode verificationMode = Mockito.verify(mock, Mockito.times(1));

 // when
 mockHandler.handle(new InvocationOnMock(mock, "answerMethod"));

 // then
 // no verification or answer is expected in this test case as verificationMode is already processed in the MockHandler
 }

 @Test
 public void testHandle_multipleAnswerMethods() {
 // given
 when(mock.answerMethod1()).thenAnswer(new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocationOnMock) throws Throwable {
 return "answer1";
 }
 });
 when(mock.answerMethod2()).thenAnswer(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocationOnMock) throws Throwable {
 return 42;
 }
 });

 // when
 String stringResult = (String) mockHandler.handle(new InvocationOnMock(mock, "answerMethod1"));
 Integer integerResult = (Integer) mockHandler.handle(new InvocationOnMock(mock, "answerMethod2"));

 // then
 assertEquals("answer1", stringResult);
 assertEquals(42, integerResult);
 }

 @Test
 public void testHandle_multipleVerificationModes() {
 // given
 VerificationMode verificationMode1 = Mockito.verify(mock, Mockito.times(1));
 VerificationMode verificationMode2 = Mockito.verify(mock, Mockito.times(2));

 // when
 mockHandler.handle(new InvocationOnMock(mock, "answerMethod"));
 mockHandler.handle(new InvocationOnMock(mock, "answerMethod"));

 // then
 // verificationMode1 should have been processed before handle() call
 // verificationMode2 should be processed after handle() calls
 verify(mock, verificationMode1);
 verify(mock, verificationMode2);
 }

 @Test
 public void testHandle_answersForStubbing() {
 // given
 List<Answer> answers = Arrays.asList(new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocationOnMock) throws Throwable {
 return "answer1";
 }
 }, new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocationOnMock) throws Throwable {
 return 42;
 }
 });
 mockHandler.setAnswersForStubbing(answers);

 // when
 String stringResult1 = (String) mockHandler.handle(new InvocationOnMock(mock, "answerMethod1"));
 Integer integerResult1 = (Integer) mockHandler.handle(new InvocationOnMock(mock, "answerMethod2"));
 String stringResult2 = (String) mockHandler.handle(new InvocationOnMock(mock, "answerMethod1"));
 Integer integerResult2 = (Integer) mockHandler.handle(new InvocationOnMock(mock, "answerMethod2"));

 // then
 assertEquals("answer1", stringResult1);
 assertEquals(42, integerResult1);
 assertEquals("answer1", stringResult2);
 assertEquals(42, integerResult2);
 }

 @Test
 public void testHandle_voidMethodStubbing_noAnswers() {
 // given
 // no answers are set

 // when
 mockHandler.handle(new InvocationOnMock(mock, "voidMethod"));

 // then
 // no verification or answer is expected in this test case
 }
}