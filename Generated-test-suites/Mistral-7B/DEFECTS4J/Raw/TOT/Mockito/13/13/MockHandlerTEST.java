package org.mockito.internal.test. MockHandlerTest;

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

 private MockHandler<MyMockType> mockHandler;
 private MyMockType mock;

 @Before
 public void setup() {
 mock = Mockito.mock(MyMockType.class);
 mockHandler = new MockHandler<MyMockType>() {};
 }

 @Test
 public void testHandle_invocationWithAnswer() {
 // given
 when(mockHandler.getInvocationContainer()).thenReturn(new InvocationContainerImpl(new ThreadSafeMockingProgress()));
 when(mockHandler.handle(ArgumentMatchers.any())).thenReturn(new Object());

 // when
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 invocation.getMock(); // call the real method on the mock

 // then
 verify(mockHandler).handle(invocation);
 }

 @Test
 public void testHandle_invocationWithVerificationMode() {
 // given
 when(mockHandler.getInvocationContainer()).thenReturn(new InvocationContainerImpl(new ThreadSafeMockingProgress()));
 when(mockHandler.handle(ArgumentMatchers.any())).thenReturn(null);

 // when
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 InvocationMatcher invocationMatcher = ArgumentMatchers.eq("test");

 // then
 verify(mockHandler, Mockito.atLeastOnce()).handle(invocationMatcher);
 }

 @Test(expected = IllegalStateException.class)
 public void testHandle_invocationWithVerificationModeOnDifferentMock() {
 // given
 MyMockType differentMock = Mockito.mock(MyMockType.class);
 when(mockHandler.getInvocationContainer()).thenReturn(new InvocationContainerImpl(new ThreadSafeMockingProgress()));
 when(mockHandler.handle(ArgumentMatchers.any())).thenReturn(null);

 // when
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 InvocationMatcher invocationMatcher = ArgumentMatchers.eq(differentMock);

 // then
 verify(mockHandler, Mockito.atLeastOnce()).handle(invocationMatcher); // should throw an exception
 }

 @Test
 public void testHandle_invocationWithVoidMethodStubbable() {
 // given
 when(mockHandler.getInvocationContainer()).thenReturn(new InvocationContainerImpl(new ThreadSafeMockingProgress()));
 when(mockHandler.voidMethodStubbable(mock)).thenReturn(new VoidMethodStubbableImpl<>());

 // when
 mockHandler.voidMethodStubbable(mock).stubVoid();

 // then
 // Add a test case here to check if the void method is actually stubbed
 }

 @Test
 public void testHandle_invocationWithAnswerList() {
 // given
 List<Answer> answers = new ArrayList<>();
 answers.add((Answer) ArgumentMatchers.anyInt());
 answers.add((Answer) ArgumentMatchers.anyString());

 when(mockHandler.getInvocationContainer()).thenReturn(new InvocationContainerImpl(new ThreadSafeMockingProgress()));
 when(mockHandler.getMockSettings()).thenReturn(new MockSettingsImpl());
 mockHandler.setAnswersForStubbing(answers);

 // when
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 InvocationMatcher invocationMatcher = ArgumentMatchers.any();

 // then
 Object answer1 = mockHandler.handle(invocationMatcher);
 Object answer2 = mockHandler.handle(invocationMatcher);

 // Assert that the answers were returned in the correct order
 assertEquals(answer1, answers.get(0).answer(invocation));
 assertEquals(answer2, answers.get(1).answer(invocation));
 }
}