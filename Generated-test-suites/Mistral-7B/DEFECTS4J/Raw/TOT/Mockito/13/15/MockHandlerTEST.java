package org.mockito.internal.test.handler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentsMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.internal.invocation.MethodType.method;

public class MockHandlerTest {

 private MockHandler<MockitoTestDummy> mockHandler;
 private MockSettingsImpl mockSettings;
 private MockitoTestDummy mockitoTestDummy;

 @Before
 public void setup() {
 mockSettings = new MockSettingsImpl();
 mockHandler = new MockHandler<>(mockSettings);
 mockitoTestDummy = Mockito.mock(MockitoTestDummy.class, mockSettings);
 }

 @Test
 public void testStubbing() {
 // Given
 when(mockitoTestDummy.method1(1)).thenReturn("expectedValue");

 // When
 String result = mockHandler.handle(new InvocationOnMock(mockitoTestDummy, method(MockitoTestDummy.class, "method1", int.class, String.class), 1));

 // Then
 assertEquals("expectedValue", result);
 }

 @Test
 public void testStubbingVoid() {
 // Given
 mockHandler.voidMethodStubbable(mockitoTestDummy).stub(() -> System.out.println("Printed when void method called"));

 // When
 mockHandler.handle(new InvocationOnMock(mockitoTestDummy, method(MockitoTestDummy.class, "voidMethod"), null));

 // Then
 verify(mockitoTestDummy).voidMethod();
 System.out.println("Printed when void method called");
 }

 @Test(expected = AssertionError.class)
 public void testVerification() {
 // Given
 // No verification expected

 // When
 mockHandler.handle(new InvocationOnMock(mockitoTestDummy, method(MockitoTestDummy.class, "method1", int.class, String.class), 1));

 // Then - exception should be thrown
 verify(mockitoTestDummy).method1(ArgumentsMatchers.isA(Integer.class));
 }

 @Test(expected = AssertionError.class)
 public void testVerificationOnIncorrectMock() {
 // Given
 MockitoTestDummy incorrectMock = Mockito.mock(AnotherClass.class, mockSettings);

 // When & Then - verification should fail because it's not on the correct mock
 verify(incorrectMock).method1(1);
 }

 @Test
 public void testSettingAnswersForStubbing() {
 // Given
 List<Answer> answers = new ArrayList<>();
 Answer answer1 = invocation -> "answer1";
 Answer answer2 = invocation -> "answer2";
 answers.add(answer1);
 answers.add(answer2);

 // When
 mockHandler.setAnswersForStubbing(answers);

 // Then
 InvocationOnMock invocation = new InvocationOnMock(mockitoTestDummy, method(MockitoTestDummy.class, "method1", int.class, String.class), 1);
 doAnswer(answer1).when(mockitoTestDummy).method1(anyInt());
 doAnswer(answer2).when(mockitoTestDummy).method1(anyInt());

 String result1 = mockHandler.handle(invocation);
 assertEquals("answer1", result1);

 String result2 = mockHandler.handle(invocation);
 assertEquals("answer2", result2);
 }

 @Test
 public void testResetInvocationForPotentialStubbing() {
 // Given
 when(mockitoTestDummy.method1(1)).thenReturn("expectedValue1");
 when(mockitoTestDummy.method1(2)).thenReturn("expectedValue2");

 // When
 String result1 = mockHandler.handle(new InvocationOnMock(mockitoTestDummy, method(MockitoTestDummy.class, "method1", int.class, String.class), 1));
 assertEquals("expectedValue1", result1);

 // Then - reset invocation for potential stubbing
 mockHandler.resetInvocationForPotentialStubbing(any(InvocationMatcher.class));

 // When
 String result2 = mockHandler.handle(new InvocationOnMock(mockitoTestDummy, method(MockitoTestDummy.class, "method1", int.class, String.class), 2));
 assertEquals("expectedValue2", result2);
 }
}

class MockitoTestDummy {
 public String method1(int arg) {
 return "default value";
 }

 public void voidMethod() {
 //void method
 }
}