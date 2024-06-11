package org.mockito.internal.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Answer2;
import org.mockito.stubbing.Stub;
import org.mockito.verification.VerificationMode;

import java.lang.reflect.Method;
import java.util.List;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.JUnit4.class)
public class MockHandlerTest {

 private MockHandler<MockHandlerTestClass> mockHandler;
 private MockHandler<MockHandlerTestClass> spyMockHandler;
 private MockitoInvocationHandler mockMockHandler;
 private MockitoInvocationHandler spyMockitoInvocationHandler;
 private T mock;
 private T spy;
 private List<Answer> answers;
 private Answer answer;
 private Answer2<Object, Integer> answer2;
 private Method method;
 private InvocationOnMock invocation;
 private Throwable throwable;

 @Before
 public void setUp() {
 mockSettings = new MockSettingsImpl();
 mockHandler = new MockHandler<>(mockSettings);
 spyMockHandler = spy(new MockHandler<>(mockSettings));
 mockMockHandler = mock(MockitoInvocationHandler.class);
 spyMockitoInvocationHandler = spy(mockMockHandler);
 mock = mock(MockHandlerTestClass.class);
 spy = spy(mock);
 answers = new java.util.ArrayList<>();
 answer = new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
 return "answer";
 }
 };
 answer2 = new Answer2<Object, Integer>() {
 @Override
 public Object answer(InvocationOnMock invocationOnMock, Integer integer) throws Throwable {
 return "answer" + integer;
 }
 };
 }

 @Test
 public void testHandle_stubbing() throws Throwable {
 invocation = Mockito.mock(InvocationOnMock.class);
 when(mock.someMethod(eq("arg1"), eq("arg2"))).thenReturn("stubbedValue");
 invokeMethodAndCheckResult(invocation, "someMethod", new Object[]{"arg1", "arg2"}, "stubbedValue");
 }

 @Test
 public void testHandle_doAnswer() throws Throwable {
 invocation = Mockito.mock(InvocationOnMock.class);
 when(mock.someMethod(eq("arg1"), eq("arg2"))).doAnswer(answer);
 invokeMethodAndCheckResult(invocation, "someMethod", new Object[]{"arg1", "arg2"}, "answer");
 }

 @Test
 public void testHandle_doAnswer_withArguments() throws Throwable {
 invocation = Mockito.mock(InvocationOnMock.class);
 when(mock.someMethod(eq("arg1"), eq("arg2"))).doAnswer(answer2);
 invokeMethodAndCheckResult(invocation, "someMethod", new Object[]{"arg1", 1}, "answer1");
 }

 @Test
 public void testHandle_doReturn() throws Throwable {
 invocation = Mockito.mock(InvocationOnMock.class);
 when(mock.someMethod(eq("arg1"), eq("arg2"))).thenReturn("returnValue");
 invokeMethodAndCheckResult(invocation, "someMethod", new Object[]{"arg1", "arg2"}, "returnValue");
 }

 @Test
 public void testHandle_doThrow() throws Throwable {
 invocation = Mockito.mock(InvocationOnMock.class);
 when(mock.someMethod(eq("arg1"), eq("arg2"))).thenThrow(new Exception("exceptionMessage"));
 invokeMethodAndCheckResult(invocation, "someMethod", new Object[]{"arg1", "arg2"}, null, new Exception("exceptionMessage"));
 }

 @Test
 public void testHandle_findAnswerFor() throws Throwable {
 invocation = Mockito.mock(InvocationOnMock.class);
 when(mock.someMethod(eq("arg1"), eq("arg2"))).thenReturn("returnValue1");
 when(mock.someOtherMethod(eq("arg1"), eq("arg2"))).thenReturn("returnValue2");
 stubbedInvocation = spyMockHandler.findAnswerFor(invocation);
 assertEquals("returnValue1", stubbedInvocation.answer(invocation));
 }

 @Test(expected = RuntimeException.class)
 public void testHandle_findAnswerFor_noAnswerFound() throws Throwable {
 invocation = Mockito.mock(InvocationOnMock.class);
 stubbedInvocation = spyMockHandler.findAnswerFor(invocation);
 assertNull(stubbedInvocation);
 }

 @Test
 public void testHandle_getMockSettings() throws Throwable {
 assertEquals(mockSettings, mockHandler.getMockSettings());
 }

 @Test
 public void testHandle_setAnswersForStubbing() throws Throwable {
 answers.add(answer);
 mockHandler.setAnswersForStubbing(answers);
 verify(mockMockHandler).setAnswersForStubbing(answers);
 }

 @Test
 public void testHandle_getInvocationContainer() throws Throwable {
 assertEquals(mockHandler.getInvocationContainer(), mockHandler.invocationContainerImpl);
 }

 @Test
 public void testHandle_voidMethodStubbable() throws Throwable {
 VoidMethodStubbable<MockHandlerTestClass> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);
 assertEquals(mockHandler, voidMethodStubbable.getMockHandler());
 assertEquals(mock, voidMethodStubbable.getMock());
 }

 @Test
 public void testHandle_handle_verificationModeIsNull() throws Throwable {
 invocation = Mockito.mock(InvocationOnMock.class);
 invokeMethodAndNoVerification(invocation, "someMethod", new Object[]{"arg1", "arg2"});
 }

 @Test
 public void testHandle_handle_verificationModeNotNull() throws Throwable {
 invocation = Mockito.mock(InvocationOnMock.class);
 verificationMode = Mockito.verify(spyMockitoInvocationHandler, Mockito.times(1)).handle(Mockito.any());
 verify(spyMockitoInvocationHandler).handle(invocation);
 invokeMethodAndVerify(invocation, "someMethod", new Object[]{"arg1", "arg2"}, verificationMode);
 }

 @Test
 public void testHandle_handle_multipleInvocations() throws Throwable {
 invocation = Mockito.mock(InvocationOnMock.class);
 verificationMode = Mockito.verify(spyMockitoInvocationHandler, Mockito.times(2)).handle(Mockito.any());
 invokeMethodAndVerify(invocation, "someMethod", new Object[]{"arg1", "arg2"}, verificationMode);
 invokeMethodAndVerify(invocation, "someMethod", new Object[]{"arg1", "arg2"}, verificationMode);
 }

 @Test
 public void testHandle_handle_multipleVerifications() throws Throwable {
 invocation = Mockito.mock(InvocationOnMock.class);
 VerificationDataImpl data1 = new VerificationDataImpl(new java.util.ArrayList<>(), eq(invocationMatcher1));
 VerificationDataImpl data2 = new VerificationDataImpl(new java.util.ArrayList<>(), eq(invocationMatcher2));
 VerificationMode verificationMode = Mockito.verify(spyMockitoInvocationHandler, Mockito.times(1)).handle(Mockito.any());
 verify(spyMockitoInvocationHandler).handle(invocation);
 invokeMethodAndVerify(invocation, "someMethod", new Object[]{"arg1", "arg2"}, verificationMode, data1);
 invokeMethodAndVerify(invocation, "someOtherMethod", new Object[]{"arg1", "arg2"}, verificationMode, data2);
 }

 @Test
 public void testHandle_handle_multipleVerifications_differentMock() throws Throwable {
 mockHandler2 = new MockHandler<>(mockSettings);
 invocation = Mockito.mock(InvocationOnMock.class);
 VerificationDataImpl data1 = new VerificationDataImpl(new java.util.ArrayList<>(), eq(invocationMatcher1));
 VerificationDataImpl data2 = new VerificationDataImpl(new java.util.ArrayList<>(), eq(invocationMatcher2));
 VerificationMode verificationMode = Mockito.verify(spyMockitoInvocationHandler, Mockito.times(1)).handle(Mockito.any());
 verify(spyMockitoInvocationHandler).handle(invocation);
 invokeMethodAndVerify(invocation, "someMethod", new Object[]{"arg1", "arg2"}, verificationMode, data1);
 invokeMethodAndVerify(mock, "someOtherMethod", new Object[]{"arg1", "arg2"}, verificationMode, data2);
 }

 @Test
 public void testHandle_handle_resetInvocationForPotentialStubbing() throws Throwable {
 invocation = Mockito.mock(InvocationOnMock.class);
 mockHandler.handle(invocation);
 mockHandler.resetInvocationForPotentialStubbing(invocationMatcher);
 verifyZeroInteractions(mockHandler);
 }

 @Test
 public void testHandle_handle_resetInvocationForPotentialStubbing_differentMock() throws Throwable {
 mockHandler2 = new MockHandler<>(mockSettings);
 invocation = Mockito.mock(InvocationOnMock.class);
 mockHandler2.handle(invocation);
 mockHandler2.resetInvocationForPotentialStubbing(invocationMatcher);
 verifyZeroInteractions(mockHandler2);
 }

 private void invokeMethodAndCheckResult(InvocationOnMock invocation, String methodName, Object[] arguments, Object expectedResult) throws Throwable {
 method = mock.getClass().getMethod(methodName, arguments.getClass());
 Object result = mockHandler.handle(invocation);
 assertEquals(expectedResult, result);
 }

 private void invokeMethodAndVerify(InvocationOnMock invocation, String methodName, Object[] arguments, VerificationMode verificationMode) throws Throwable {
 method = mock.getClass().getMethod(methodName, arguments.getClass());
 Object result = mockHandler.handle(invocation);
 verify(mock).handle(invocation);
 verify(mock).handle(invocation);
 if (verificationMode != null) {
 verify(spyMockitoInvocationHandler).handle(invocation);
 verify(spyMockitoInvocationHandler).handle(invocation);
 }
 }

 private void invokeMethodAndVerify(InvocationOnMock invocation, String methodName, Object[] arguments, VerificationMode verificationMode, VerificationDataImpl verificationData) throws Throwable {
 method = mock.getClass().getMethod(methodName, arguments.getClass());
 Object result = mockHandler.handle(invocation);
 verify(mock).handle(invocation);
 verify(mock).handle(invocation);
 if (verificationMode != null) {
 verify(spyMockitoInvocationHandler).handle(invocation);
 verify(spyMockitoInvocationHandler).handle(invocation);
 verify(spyMockitoInvocationHandler).verify(verificationData.getVerificationData());
 }
 }

 private void invokeMethodAndNoVerification(InvocationOnMock invocation, String methodName, Object[] arguments) throws Throwable {
 method = mock.getClass().getMethod(methodName, arguments.getClass());
 mockHandler.handle(invocation);
 }
}

class MockHandlerTestClass {
 void someMethod(String arg1, String arg2) {}
}