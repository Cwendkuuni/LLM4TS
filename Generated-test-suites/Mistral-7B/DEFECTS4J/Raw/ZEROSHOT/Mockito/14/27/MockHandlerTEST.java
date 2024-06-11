package org.mockito.internal.test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.MockSettingsImpl;
import org.mockito.internal.MockHandlerInterface;
import org.mockito.internal.MockitoInvocationHandler;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationMatcher;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.VoidMethodStubbable;
import org.mockito.internal.stubbing.VoidMethodStubbableImpl;
import org.mockito.internal.progress.MockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress;
import org.mockito.internal.progress.ThreadSafeMockingProgress.ArgumentMatcherStorage;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.OngoingStubbingImpl;
import org.mockito.internal.verification.VerificationDataImpl;
import org.mockito.stubbing.AnswerImpl;
import org.mockito.internal.verification.VerificationMode;

import java.util.ArrayList;
import java.util.List;

public class MockHandlerTest {

 private MockHandler<SomeMockType> mockHandler;
 private SomeMockType mock;
 private Invocation invocation;
 private InvocationMatcher invocationMatcher;
 private StubbedInvocationMatcher stubbedInvocation;
 private OngoingStubbingImpl<SomeMockType> ongoingStubbing;
 private ArgumentMatcherStorage argumentMatcherStorage;

 @Before
 public void setUp() {
 mock = mock(SomeMockType.class);
 mockHandler = new MockHandler<>();
 invocation = new Invocation(null, null, new Object[0], new Class[0], null);
 invocationMatcher = ArgumentMatchers.any();
 stubbedInvocation = new StubbedInvocationMatcher(invocationMatcher);
 argumentMatcherStorage = new ThreadSafeMockingProgress().new ArgumentMatcherStorage();
 }

 @Test
 public void testHandleNullInvocation() {
 assertNull(mockHandler.handle(null));
 }

 @Test
 public void testHandleDefaultAnswer() {
 when(mockHandler.getMockSettings()).thenReturn(new MockSettingsImpl());
 when(mockHandler.getMockSettings().getDefaultAnswer()).thenReturn(new AnswerImpl(null));

 Object result = mockHandler.handle(invocation);
 assertNull(result);
 }

 @Test
 public void testHandleStubbingVoidMethods() {
 mockHandler.voidMethodStubbable(mock).stubVoid();
 when(mockHandler.getMockSettings()).thenReturn(new MockSettingsImpl());
 when(mockHandler.mockingProgress.getArgumentMatcherStorage()).thenReturn(argumentMatcherStorage);

 mockHandler.handle(invocation);
 assertEquals(ArgumentMatchers.isNull(), invocationContainer.getMethodForStubbing());
 }

 @Test
 public void testHandleStubbingMethodsWithAnswer() {
 List<Answer> answers = new ArrayList<>();
 answers.add(new AnswerImpl(new Object() {
 @Override
 public Object answer(Invocation invocation) {
 return "Stubbed response";
 }
 }));

 when(mockHandler.getMockSettings()).thenReturn(new MockSettingsImpl());
 when(mockHandler.getMockSettings().getDefaultAnswer()).thenReturn(new AnswerImpl(null));
 when(mockHandler.mockingProgress.getArgumentMatcherStorage()).thenReturn(argumentMatcherStorage);
 when(mockHandler.matchersBinder.bindMatchers(argumentMatcherStorage, invocation)).thenReturn(invocationMatcher);
 when(mockHandler.invocationContainerImpl.setAnswersForStubbing(answers)).thenReturn(true);
 when(mockHandler.invocationContainerImpl.findAnswerFor(invocation)).thenReturn(stubbedInvocation);

 Object result = mockHandler.handle(invocation);
 assertEquals("Stubbed response", result);
 }

 @Test
 public void testHandleVerificationMode() {
 when(mockHandler.getMockSettings()).thenReturn(new MockSettingsImpl());
 when(mockHandler.getMockSettings().getDefaultAnswer()).thenReturn(new AnswerImpl(null));
 when(mockHandler.mockingProgress.getArgumentMatcherStorage()).thenReturn(argumentMatcherStorage);
 when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(times(1));

 mockHandler.handle(invocation);
 verify(mockHandler.invocationContainerImpl).validateState();
 }

 @Test(expected = IllegalStateException.class)
 public void testHandleVerificationModeDifferentMock() {
 SomeOtherMockType otherMock = mock(SomeOtherMockType.class);
 when(mockHandler.getMockSettings()).thenReturn(new MockSettingsImpl());
 when(mockHandler.getMockSettings().getDefaultAnswer()).thenReturn(new AnswerImpl(null));
 when(mockHandler.mockingProgress.getArgumentMatcherStorage()).thenReturn(argumentMatcherStorage);
 when(mockHandler.mockingProgress.pullVerificationMode()).thenReturn(times(1));

 mockHandler.handle(new Invocation(null, otherMock, new Object[0], new Class[0], null));
 }
}