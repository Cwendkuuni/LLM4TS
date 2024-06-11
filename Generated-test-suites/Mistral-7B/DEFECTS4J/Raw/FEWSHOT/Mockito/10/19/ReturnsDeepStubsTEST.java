package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.withSettings;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.internal.invocation.StubbedInvocationMatcher;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher.AnswerType;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.AnswerWith;
import org.mockito.stubbing.OngoingStubbing;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs answers;
 private InvocationHandler handler;
 private Object mock;
 private InvocationOnMock invocation;

 @Before
 public void setup() throws Exception {
 answers = new ReturnsDeepStubs();
 handler = Mockito.mock(InvocationHandler.class);
 mock = mock(Object.class, withSettings().serializable().defaultAnswer(answers));
 invocation = Mockito.mock(InvocationOnMock.class);
 }

 @Test
 public void testAnswerReturnTypeIsMockable() throws Exception {
 final Class<List<String>> expectedType = List.class.getTypeName().asSubType(List.class, String.class);
 final Invocation invocation = Mockito.mock(Invocation.class);
 final Method method = getClass().getMethod("testMethod", InvocationOnMock.class);
 final Object returnValue = answers.answer(invocation);
 final InvocationOnMock invocationOnMock = Mockito.mock(InvocationOnMock.class);
 final InvocationOnMock invocationMock = (InvocationOnMock) handler.invoke(mock, invocationOnMock, method, new Object[]{invocationOnMock});
 final GenericMetadataSupport metadata = new GenericMetadataSupport(expectedType, method);
 final Object deepStub = answers.newDeepStubMock(metadata);
 final Object answer = answers.deepStub(invocationOnMock, metadata);

 assertSame(deepStub, answer);
 }

 @Test
 public void testAnswerReturnTypeIsNotMockable() throws Exception {
 final Class<IOException> expectedType = IOException.class;
 final Invocation invocation = Mockito.mock(Invocation.class);
 final Method method = getClass().getMethod("testMethod", InvocationOnMock.class);
 final Object returnValue = answers.answer(invocation);
 final InvocationOnMock invocationOnMock = Mockito.mock(InvocationOnMock.class);
 final InvocationOnMock invocationMock = (InvocationOnMock) handler.invoke(mock, invocationOnMock, method, new Object[]{invocationOnMock});
 final Object answer = returnValue;

 assertSame(answer, invocationMock.getMock());
 }

 @Test
 public void testMultipleMocksForSameInvocation() throws Exception {
 final Class<Map<Number, Set<Number>>> expectedType = Map.class.getTypeName().asSubType(Map.class, Number.class, Set.class, Number.class);
 final Invocation invocation = Mockito.mock(Invocation.class);
 final Method method = getClass().getMethod("testMethod", InvocationOnMock.class);
 final InvocationOnMock invocationOnMock1 = Mockito.mock(InvocationOnMock.class);
 final InvocationOnMock invocationOnMock2 = Mockito.mock(InvocationOnMock.class);
 final Object mock1 = mock(Map.class, withSettings().serializable().defaultAnswer(answers));
 final Object mock2 = mock(Map.class, withSettings().serializable().defaultAnswer(answers));
 final Object returnValue1 = answers.answer(invocationOnMock1);
 final Object returnValue2 = answers.answer(invocationOnMock2);
 final Object answer1 = answers.deepStub(invocationOnMock1, new GenericMetadataSupport(expectedType, method));
 final Object answer2 = answers.deepStub(invocationOnMock2, new GenericMetadataSupport(expectedType, method));

 assertSame(mock1, answer1);
 assertSame(mock2, answer2);
 }

 @Test
 public void testStubbedInvocationMatcherAnswer() throws Exception {
 final Class<List<Integer>> expectedType = List.class.getTypeName().asSubType(List.class, Integer.class);
 final Invocation invocation = Mockito.mock(Invocation.class);
 final Method method = getClass().getMethod("testMethod", InvocationOnMock.class);
 final Object mockList = mock(List.class);
 final StubbedInvocationMatcher stubbedInvocationMatcher = Mockito.mock(StubbedInvocationMatcher.class);
 final Object answer = answers.recordDeepStubAnswer(mockList, Mockito.mock(InvocationContainerImpl.class));
 final AnswerType answerType = new AnswerType(stubbedInvocationMatcher, answer);
 final Object invocationMock = Mockito.mock(InvocationOnMock.class);
 final Object invocationContainer = Mockito.mock(InvocationContainerImpl.class);
 final InvocationContainerImpl invocationContainerImpl = (InvocationContainerImpl) invocationContainer;
 final Set<Answer<?>> answersSet = new HashSet<>();
 invocationContainerImpl.setAnswers(answersSet);
 final StubbedInvocationMatcher stubbedInvocationMatcherSpy = spy(stubbedInvocationMatcher);
 final Object result = invocationContainerImpl.call(stubbedInvocationMatcher, invocationMock, method);

 assertSame(answer, result);
 }

 @Test
 public void testReturnsDeepStubsAnswerUsing() throws Exception {
 final Class<List<Integer>> expectedType = List.class.getTypeName().asSubType(List.class, Integer.class);
 final Invocation invocation = Mockito.mock(Invocation.class);
 final Method method = getClass().getMethod("testMethod", InvocationOnMock.class);
 final Object mockList = mock(List.class, withSettings().serializable().defaultAnswer(answers));
 final ReturnsDeepStubs returnsDeepStubsAnswer = answers.returnsDeepStubsAnswerUsing(new GenericMetadataSupport(expectedType, method));
 final Object answer = returnsDeepStubsAnswer.answer(invocation);

 assertSame(mockList, answer);
 }

 @Test
 public void testDeeplyStubbedAnswer() throws Exception {
 final Class<List<Integer>> expectedType = List.class.getTypeName().asSubType(List.class, Integer.class);
 final Invocation invocation = Mockito.mock(Invocation.class);
 final Method method = getClass().getMethod("testMethod", InvocationOnMock.class);
 final Object mockList = mock(List.class, withSettings().serializable().defaultAnswer(answers));
 final DeeplyStubbedAnswer deeplyStubbedAnswer = new DeeplyStubbedAnswer(mockList);
 final Object answer = deeplyStubbedAnswer.answer(invocation);

 assertSame(mockList, answer);
 }

 @Test
 public void testRecordDeepStubAnswer() throws Exception {
 final Class<List<Integer>> expectedType = List.class.getTypeName().asSubType(List.class, Integer.class);
 final Invocation invocation = Mockito.mock(Invocation.class);
 final Method method = getClass().getMethod("testMethod", InvocationOnMock.class);
 final Object mockList = mock(List.class, withSettings().serializable().defaultAnswer(answers));
 final InvocationContainerImpl invocationContainer = Mockito.mock(InvocationContainerImpl.class);
 final List<Object> answersList = new ArrayList<>();
 answersList.add(mockList);
 final OngoingStubbing<InvocationContainerImpl, Object> setAnswers = invocationContainer.answers(answersList.iterator());
 final Object answer = answers.recordDeepStubAnswer(mockList, invocationContainer);

 assertSame(mockList, answer);
 }

 @Test
 public void testActualParameterizedType() throws Exception {
 final Class<List<Integer>> expectedType = List.class.getTypeName().asSubType(List.class, Integer.class);
 final Object mock = mock(List.class, withSettings().serializable());
 final CreationSettings creationSettings = new MockUtil().getMockHandler(mock).getMockSettings();
 final GenericMetadataSupport metadata = answers.actualParameterizedType(mock);

 assertSame(expectedType, metadata.getTypeToMock());
 }

 @Test
 public void testActualParameterizedType_withExtraInterfaces() throws Exception {
 final Class<Map<Number, Set<Number>>> expectedType = Map.class.getTypeName().asSubType(Map.class, Number.class, Set.class, Number.class);
 final Object mock = mock(Map.class, withSettings().serializable().extraInterfaces(Collections.singleton(HashSet.class)));
 final CreationSettings creationSettings = new MockUtil().getMockHandler(mock).getMockSettings();
 final GenericMetadataSupport metadata = answers.actualParameterizedType(mock);

 assertSame(expectedType, metadata.getTypeToMock());
 }

 @Test(expected = IllegalStateException.class)
 public void testActualParameterizedType_notSerializable() throws Exception {
 final Object mock = mock(NonSerializable.class, withSettings().defaultAnswer(answers));
 final CreationSettings creationSettings = new MockUtil().getMockHandler(mock).getMockSettings();
 answers.actualParameterizedType(mock);
 }

 @Test
 public void testDeepStubsSerializationFallback() throws Exception {
 final Class<List<Integer>> expectedType = List.class.getTypeName().asSubType(List.class, Integer.class);
 final Invocation invocation = Mockito.mock(Invocation.class);
 final Method method = getClass().getMethod("testMethod", InvocationOnMock.class);
 final Object mockList = mock(List.class, withSettings().serializable().defaultAnswer(answers));
 final Object returnValue = answers.answer(invocation);
 final Object mockDeepStubsSerializationFallback = mock(ReturnsDeepStubsSerializationFallback.class);
 final Object answer = answers.deepStub(invocation, new GenericMetadataSupport(expectedType, method));
 final Object answerWithSerializationFallback = ((ReturnsDeepStubsSerializationFallback) answer).answer(invocation);

 assertSame(mockList, answerWithSerializationFallback);
 }
}

class NonSerializable {
}