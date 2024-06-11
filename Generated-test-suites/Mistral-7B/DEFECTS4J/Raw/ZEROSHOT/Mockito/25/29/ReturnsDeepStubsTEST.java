package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.MockitoCore;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.io.Serializable;
import java.lang.reflect.Type;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private InternalMockHandler<Object> handler;
 private InvocationContainerImpl container;
 private Object mock;
 private Object deepMock;

 @Before
 public void setUp() {
 MockitoCore.initMocks(this);
 returnsDeepStubs = new ReturnsDeepStubs();
 handler = new MockUtil().getMockHandler(mock);
 container = (InvocationContainerImpl) handler.getInvocationContainer();
 }

 @Test
 public void testAnswer_WhenInvocationMatchesStubbedInvocation_ThenReturnStubbedAnswer() throws Exception {
 // given
 StubbedInvocationMatcher stubbedInvocationMatcher = new StubbedInvocationMatcher(ArgumentMatchers.any());
 container.addAnswer(stubbedInvocationMatcher, false);
 Object stubbedAnswer = "stubbed answer";
 stubbedInvocationMatcher.answer(ArgumentMatchers.any()) = stubbedAnswer;

 // when
 InvocationOnMock invocation = new InvocationOnMock(mock, new MethodTestDouble(), null);

 // then
 Object answer = returnsDeepStubs.answer(invocation);

 // verify that stubbed answer was returned
 verify(stubbedInvocationMatcher).answer(invocation);
 assertEquals(stubbedAnswer, answer);
 }

 @Test
 public void testAnswer_WhenInvocationDoesNotMatchAnyStubbedInvocation_ThenReturnEmptyValue() throws Exception {
 // given
 InvocationOnMock invocation = new InvocationOnMock(mock, new MethodTestDouble(), null);

 // when
 Object answer = returnsDeepStubs.answer(invocation);

 // then
 verifyZeroInteractions(handler);
 assertEquals(Answers.RETURNS_DEEP_STUBS.getEmptyValue(), answer);
 }

 @Test
 public void testRecordDeepStubMock() throws Exception {
 // given
 Class<?> clazz = ArgumentTypes.getClass();
 mock = mock(clazz, returnsDeepStubs);

 // when
 DeepMock deepMock = (DeepMock) spy(mock);
 deepMock.method();

 // then
 verify(handler).getInvocationContainer().addAnswer(ArgumentMatchers.isA(Answer.class), ArgumentMatchers.eq(false));
 verifyNoMoreInteractions(handler);

 // when deep stub is requested
 InvocationOnMock deepStubInvocation = new InvocationOnMock(deepMock, new MethodTestDouble(), null);
 Object deepStubAnswer = returnsDeepStubs.answer(deepStubInvocation);

 // then
 assertNotNull(deepStubAnswer);
 assertEquals(deepMock, deepStubAnswer);
 }

 @Test(expected = IllegalStateException.class)
 public void testRecordDeepStubMock_WhenSameMockIsAlreadyRecordedAsDeepStub_ThenThrowException() throws Exception {
 // given
 Class<?> clazz = ArgumentTypes.getClass();
 mock = mock(clazz, returnsDeepStubs);

 // when
 DeepMock deepMock = (DeepMock) spy(mock);
 deepMock.method();
 invokeDeepStubRecording(deepMock, handler); // recording deep stub for the first time

 // when trying to record deep stub for the second time
 invokeDeepStubRecording(deepMock, handler); // should throw exception
 }

 private void invokeDeepStubRecording(Object deepMock, InternalMockHandler<Object> handler) throws Throwable {
 InvocationOnMock deepStubInvocation = new InvocationOnMock(deepMock, new MethodTestDouble(), null);
 returnsDeepStubs.recordDeepStubMock(deepStubInvocation, container);
 }
}

class MethodTestDouble {
 public void method() {
}
}

class DeepMock extends MockitoTestSupport {
}