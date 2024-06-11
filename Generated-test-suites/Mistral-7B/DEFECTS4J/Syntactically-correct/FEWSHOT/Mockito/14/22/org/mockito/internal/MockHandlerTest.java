package org.mockito.internal;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.internal.stubbing.Answer.returnValue;
import static org.mockito.internal.verification.VerificationMode.times;
import static org.mockito.internal.verification.VerificationMode.verifyNoMoreInteractions;
import static org.mockito.internal.verification.VerificationMode.verifyZeroInteractions;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.creation.MockSettingsImpl;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.invocation.MatchersBinder;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.Answer;
import org.mockito.stubbing.VoidMethodStubbableImpl;
import org.mockito.verification.VerificationMode;

public class MockHandlerTest {

 private MockHandler<Object> mockHandler;
 private Object mock;
 private Invocation invocation;
 private InvocationMatcher invocationMatcher;

 @Before
 public void setUp() {
 mock = Mockito.mock(Object.class);
 mockHandler = new MockHandler<>();
 invocation = Mockito.mock(Invocation.class);
 }

 @Test
 public void testHandle_noAnswersForStubbing() throws Throwable {
 Object result = mockHandler.handle(invocation);
 assertNull(result);
 }

 @Test
 public void testHandle_answersForStubbing() throws Throwable {
 StubbedInvocationMatcher stubbedInvocation = Mockito.mock(StubbedInvocationMatcher.class);
 mockHandler.setAnswersForStubbing(new ArrayList<Answer>() {{
 add(returnValue("result"));
 add(stubbedInvocation);
 }});
 result = mockHandler.handle(invocation);
 assertNull(result);
 }

 @Test
 public void testHandle_invocationForPotentialStubbing() throws Throwable {
 mockHandler.invocationContainerImpl.setInvocationForPotentialStubbing(invocationMatcher = Mockito.mock(InvocationMatcher.class));
 Object result = mockHandler.handle(invocation);
 assertSame(mock, result);
 }

 @Test
 public void testHandle_nullResult() throws Throwable {
 mockHandler.mockSettings.getDefaultAnswer().answer(invocation) == null;
 result = mockHandler.handle(invocation);
 assertNull(result);
 }

 @Test
 public void testHandle_defaultAnswer() throws Throwable {
 Object answer = new Object();
 mockHandler.mockSettings.getDefaultAnswer().answer(invocation) = answer;
 result = mockHandler.handle(invocation);
 assertSame(answer, result);
 }

 @Test
 public void testHandle_voidMethodStubbable() throws Throwable {
 VoidMethodStubbable<Object> voidMethodStubbable = mockHandler.voidMethodStubbable(mock);
 voidMethodStubbable.when(mock).myMethod();
 result = mockHandler.handle(invocationOn(mock).myMethod());
 assertNull(result);
 }

 @Test
 public void testHandle_verifyInteractions() throws Throwable {
 mockHandler.invocationContainerImpl.getInvocations().add(invocation);
 mockHandler.mockingProgress.pushVerificationMode(times(1));
 result = mockHandler.handle(invocation);
 assertNull(result);
 Mockito.verify(mock).myMethod();
 }

 @Test
 public void testHandle_verifyNoMoreInteractions() throws Throwable {
 mockHandler.invocationContainerImpl.getInvocations().add(invocation);
 mockHandler.mockingProgress.pushVerificationMode(times(1));
 result = mockHandler.handle(invocation);
 assertNull(result);
 Mockito.verifyNoMoreInteractions(mock);
 }

 @Test
 public void testHandle_verifyZeroInteractions() throws Throwable {
 result = mockHandler.handle(invocation);
 assertNull(result);
 Mockito.verifyZeroInteractions(mock);
 }

 @Test
 public void testHandle_multipleVerificationModes() throws Throwable {
 mockHandler.invocationContainerImpl.getInvocations().add(invocation);
 mockHandler.mockingProgress.pushVerificationMode(times(1));
 mockHandler.mockingProgress.pushVerificationMode(verifyNoMoreInteractions());
 result = mockHandler.handle(invocation);
 assertNull(result);
 }
}