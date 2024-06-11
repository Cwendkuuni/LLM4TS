package org.mockito.internal.test;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*; // for importing various stubbing methods like doAnswer, stubVoid etc.

import java.util.Arrays;
import java.util.List;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.Answer2;
import org.mockito.stubbing.Answer3;
import org.mockito.stubbing.Stub;
import org.mockito.stubbing.Stubber;
import org.mockito.verification.VerificationMode;

import org.mockito.internal.MockHandler;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.InvocationContainerImpl;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.invocation.StubbedInvocationMatcher;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.VoidMethodStubbableImpl;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbable;

public class MockHandlerTest {

 // List all the public methods with their signatures

 // 1. handle(Invocation) throws Throwable
 public void testHandle_NormalInvocation() {
 MockHandler<MockitoTestClass> mockHandler = mock(MockHandler.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(mockHandler.handle(invocation)).thenReturn("returnValue");
 assertEquals("returnValue", mockHandler.handle(invocation));
 verify(mockHandler, times(1)).handle(invocation);
 }

 public void testHandle_StubbingVoidMethod() {
 MockHandler<MockitoTestClass> mockHandler = mock(MockHandler.class);
 Stubber stubber = mock(Stubber.class);
 mockHandler.voidMethodStubbable(mock(MockitoTestClass.class));
 stubber.stub(mockHandler).handle(any());
 verify(mockHandler, times(1)).handle((InvocationOnMock) isA(InvocationOnMock.class));
 }

 public void testHandle_Verification() {
 MockHandler<MockitoTestClass> mockHandler = mock(MockHandler.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 InvocationMatcher invocationMatcher = argThat(isA(InvocationOnMock.class));
 when(mockHandler.handle(invocation)).then(answer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "returnValue";
 }
 }));
 verify(mockHandler, times(1)).handle(invocationMatcher);
 }

 public void testHandle_VerificationWithAnswer() {
 MockHandler<MockitoTestClass> mockHandler = mock(MockHandler.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 InvocationMatcher invocationMatcher = argThat(isA(InvocationOnMock.class));
 when(mockHandler.handle(invocation)).thenAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "returnValue";
 }
 });
 verify(mockHandler, times(1)).handle(invocationMatcher).thenReturn("verifiedValue");
 assertEquals("verifiedValue", mockHandler.handle(invocationMatcher.verify()));
 }

 public void testHandle_VerificationWithAnswer2() {
 MockHandler<MockitoTestClass> mockHandler = mock(MockHandler.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 InvocationMatcher invocationMatcher = argThat(isA(InvocationOnMock.class));
 when(mockHandler.handle(invocation)).thenAnswer(new Answer2<InvocationOnMock, Integer>() {
 @Override
 public Object answer(InvocationOnMock invocation, Integer integer) throws Throwable {
 return "returnValue";
 }
 });
 verify(mockHandler, times(1)).handle(invocationMatcher).thenReturn("verifiedValue");
 verify(mockHandler, times(1)).handle(invocationMatcher).thenAnswer(new Answer2<InvocationOnMock, Integer>() {
 @Override
 public Object answer(InvocationOnMock invocation, Integer integer) throws Throwable {
 return "verifiedAnswer";
 }
 }).thenReturn("verifiedValue2");
 assertEquals("verifiedValue2", mockHandler.handle(invocationMatcher.verify()));
 }

 public void testHandle_VerificationWithAnswer3() {
 MockHandler<MockitoTestClass> mockHandler = mock(MockHandler.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 InvocationMatcher invocationMatcher = argThat(isA(InvocationOnMock.class));
 when(mockHandler.handle(invocation)).thenAnswer(new Answer3<InvocationOnMock, Integer, String>() {
 @Override
 public Object answer(InvocationOnMock invocation, Integer integer, String string) throws Throwable {
 return "returnValue";
 }
 });
 verify(mockHandler, times(1)).handle(invocationMatcher).thenReturn("verifiedValue");
 verify(mockHandler, times(1)).handle(invocationMatcher).thenAnswer(new Answer3<InvocationOnMock, Integer, String>() {
 @Override
 public Object answer(InvocationOnMock invocation, Integer integer, String string) throws Throwable {
 return "verifiedAnswer";
 }
 }).thenAnswer(new Answer3<InvocationOnMock, Integer, String>() {
 @Override
 public Object answer(InvocationOnMock invocation, Integer integer, String string) throws Throwable {
 return "verifiedAnswer2";
 }
 });
 assertEquals("verifiedAnswer2", mockHandler.handle(invocationMatcher.verify()));
 }

 // 2. voidMethodStubbable(T mock)
 public void testVoidMethodStubbable() {
 MockHandler<MockitoTestClass> mockHandler = mock(MockHandler.class);
 VoidMethodStubbable<MockitoTestClass> voidMethodStubbable = mockHandler.voidMethodStubbable(mock(MockitoTestClass.class));
 voidMethodStubbable.stubVoid();
 verify(mockHandler, times(1)).handle((InvocationOnMock) isA(InvocationOnMock.class));
 }

 // 3. getMockSettings()
 public void testGetMockSettings() {
 MockHandler<MockitoTestClass> mockHandler = mock(MockHandler.class);
 MockSettingsImpl mockSettings = mockHandler.getMockSettings();
 assertNotNull(mockSettings);
 }

 // 4. setAnswersForStubbing(List<Answer> answers)
 public void testSetAnswersForStubbing() {
 MockHandler<MockitoTestClass> mockHandler = mock(MockHandler.class);
 List<Answer> answers = Arrays.asList(answers(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "answer1";
 }
 }, answers(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return "answer2";
 }
 })));
 mockHandler.setAnswersForStubbing(answers);
 verify(mockHandler, times(1)).setAnswersForStubbing(isA(List.class));
 }

 // 5. getInvocationContainer()
 public void testGetInvocationContainer() {
 MockHandler<MockitoTestClass> mockHandler = mock(MockHandler.class);
 InvocationContainer invocationContainer = mockHandler.getInvocationContainer();
 assertNotNull(invocationContainer);
 }

 // Edge cases and exception handling test cases

 public void testHandle_NullInvocation() {
 MockHandler<MockitoTestClass> mockHandler = mock(MockHandler.class);
 assertThrows(NullPointerException.class, () -> mockHandler.handle(null));
 }

 public void testHandle_NullVerificationMode() {
 MockHandler<MockitoTestClass> mockHandler = mock(MockHandler.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 InvocationMatcher invocationMatcher = argThat(isA(InvocationOnMock.class));
 when(mockHandler.handle(invocation)).thenAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 throw new NullPointerException();
 }
 });
 assertThrows(NullPointerException.class, () -> mockHandler.handle(invocationMatcher).thenReturn("verifiedValue"));
 }

 public void testHandle_NullInvocationMatcher() {
 MockHandler<MockitoTestClass> mockHandler = mock(MockHandler.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(mockHandler.handle(invocation)).thenAnswer(new Answer<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 throw new NullPointerException();
 }
 });
 assertThrows(NullPointerException.class, () -> mockHandler.handle(null).thenReturn("verifiedValue"));
 }

 public void testHandle_NullAnswer() {
 MockHandler<MockitoTestClass> mockHandler = mock(MockHandler.class);
 InvocationOnMock invocation = mock(InvocationOnMock.class);
 when(mockHandler.handle(invocation)).thenAnswer(null);
 assertThrows(NullPointerException.class, () -> mockHandler.handle(invocation).thenReturn("verifiedValue"));
 }
}