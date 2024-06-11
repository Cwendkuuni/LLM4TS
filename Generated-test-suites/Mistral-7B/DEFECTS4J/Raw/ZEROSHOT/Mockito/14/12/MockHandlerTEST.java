package org.mockito.internal.test;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class MockHandlerTest {

 private MockHandler<SomeMockType> mockHandler;
 private SomeMockType mock;
 private InvocationOnMock invocation;

 @Before
 public void setup() {
 mock = Mockito.mock(SomeMockType.class);
 mockHandler = new MockHandler<>();
 mockHandler.setAnswersForStubbing(new ArrayList<>());
 invocation = Mockito.mock(InvocationOnMock.class);
 }

 @Test
 public void testHandle_noAnswersForStubbing_returnsDefaultAnswer() throws Throwable {
 // Given
 when(mock.someMethod()).thenReturn("default answer");

 // When
 Object result = mockHandler.handle(invocation);

 // Then
 verify(mock).someMethod();
 assertEquals("default answer", result);
 }

 @Test
 public void testHandle_answersForStubbing_returnsAnswer() throws Throwable {
 List<Answer> answers = new ArrayList<>();
 answers.add(Answer.returnValue("answer"));
 when(mock.someMethod()).thenAnswer(Answer.callRealMethod());

 mockHandler.setAnswersForStubbing(answers);

 // When
 Object result = mockHandler.handle(invocation);

 // Then
 verifyZeroInteractions(mock);
 assertEquals("answer", result);
 }

 @Test
 public void testHandle_verificationModeNotnull_verifiesInvocation() throws Throwable {
 List<Answer> answers = new ArrayList<>();
 answers.add(Answer.callRealMethod());
 when(mock.someMethod()).thenAnswer(Answers.returnValue("answer"));

 mockHandler.setAnswersForStubbing(answers);

 verify(mock).someMethod();
 mockHandler.handle(invocation);
 verify(mock).someMethod();
 }

 @Test(expected = IllegalStateException.class)
 public void testHandle_verificationModeNotnull_verifiesTwiceOnDifferentMocks() throws Throwable {
 List<Answer> answers = new ArrayList<>();
 answers.add(Answer.callRealMethod());
 when(mock.someMethod()).thenAnswer(Answers.returnValue("answer"));
 when(AnotherMock.class).thenReturn(Mockito.mock(AnotherMock.class));

 mockHandler.setAnswersForStubbing(answers);

 verify(mock).someMethod();
 mockHandler.handle(invocation);
 verify(AnotherMock.class).someMethod();
 }
}

class SomeMockType {
 void someMethod();
}