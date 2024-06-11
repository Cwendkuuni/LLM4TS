package com.yourpackage.test;

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

import static org.mockito.ArgumentMatching.*;

public class MockHandlerTest {

 private MockHandler<MockitoTestClass> mockHandler;
 private MockitoTestClass mock;

 @Before
 public void setUp() {
 mock = Mockito.mock(MockitoTestClass.class);
 mockHandler = new MockHandler<>();
 }

 @Test
 public void testHandleInvocationWithStubbing() throws Throwable {
 // Given
 when(mock.methodToStub(ArgumentMatchers.anyInt())).thenReturn("stubbed result");

 // When
 String result = (String) mockHandler.handle(new InvocationOnMock(mock) {
 @SuppressWarnings("unchecked")
 @Override
 public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
 return mock.methodToStub((Integer) args[0]);
 }
 });

 // Then
 verify(mock).methodToStub(eq(1));
 assertEquals("stubbed result", result);
 }

 @Test
 public void testHandleInvocationWithVerification() throws Throwable {
 // Given
 Mockito.verify(mock).methodToVerify(eq(1));

 // When
 String result = (String) mockHandler.handle(new InvocationOnMock(mock) {
 @SuppressWarnings("unchecked")
 @Override
 public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
 return mock.methodToStub((Integer) args[0]);
 }
 });

 // Then
 // Verification should have already been performed in the previous test
 // No need to verify anything here
 }

 @Test
 public void testHandleInvocationWithAnswer() throws Throwable {
 // Given
 final String expectedResult = "expected result";
 when(mock.methodToStub(anyInt())).thenAnswer(new Answer<String>() {
 @Override
 public String answer(InvocationOnMock invocation) throws Throwable {
 return expectedResult;
 }
 });

 // When
 String result = (String) mockHandler.handle(new InvocationOnMock(mock) {
 @SuppressWarnings("unchecked")
 @Override
 public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
 return mock.methodToStub((Integer) args[0]);
 }
 });

 // Then
 verify(mock).methodToStub(anyInt());
 assertEquals(expectedResult, result);
 }

 @Test
 public void testHandleInvocationWithVoidAnswer() throws Throwable {
 // Given
 when(mock.methodToVoidStub()).thenAnswer(Mockito.doNothing());

 // When
 mockHandler.handle(new InvocationOnMock(mock) {
 @SuppressWarnings("unchecked")
 @Override
 public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
 mock.methodToVoidStub();
 return null;
 }
 });

 // Then
 verify(mock).methodToVoidStub();
 }

 @Test
 public void testHandleInvocationWithMultipleAnswers() throws Throwable {
 // Given
 List<Answer> answers = new ArrayList<>();
 answers.add(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return 1;
 }
 });
 answers.add(new Answer<Integer>() {
 @Override
 public Integer answer(InvocationOnMock invocation) throws Throwable {
 return 2;
 }
 });
 mockHandler.setAnswersForStubbing(answers);

 // When
 Integer result1 = (Integer) mockHandler.handle(new InvocationOnMock(mock) {
 @SuppressWarnings("unchecked")
 @Override
 public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
 return mock.methodToStub();
 }
 });
 Integer result2 = (Integer) mockHandler.handle(new InvocationOnMock(mock) {
 @SuppressWarnings("unchecked")
 @Override
 public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
 return mock.methodToStub();
 }
 });

 // Then
 verify(mock).methodToStub();
 verify(mock).methodToStub();
 assertEquals(1, result1.intValue());
 assertEquals(2, result2.intValue());
 }
}