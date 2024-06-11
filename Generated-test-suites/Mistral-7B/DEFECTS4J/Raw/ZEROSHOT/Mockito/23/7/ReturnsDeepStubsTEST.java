package org.mockito.internal.stubbing.defaultanswers.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.MockSettings;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.invocation.InvocationOnMockInvocationHandler;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;

import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 private ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 @org.junit.Test public void testReturnsDeepStubsForSimpleClass() {
 // given
 GenericMetadataSupport stringMeta = new GenericMetadataSupport(String.class);
 Object stringMock = mock(String.class, new ReturnsDeepStubs());

 // when
 Object answer = returnsDeepStubs.answer(new InvocationOnMock(stringMock, String.class.getMethod("length"), (Object[]) null));

 // then
 assertEquals(stringMock, answer);
 }

 @org.junit.Test public void testReturnsDeepStubsForGenericClass() {
 // given
 Class<Map> mapInterface = Map.class;
 Class<Set<Number>> setClass = Set.class.getTypeParameters()[1];
 Class<Number> numberClass = Number.class;
 GenericMetadataSupport mapMeta = new GenericMetadataSupport(mapInterface, setClass, numberClass);
 Object mapMock = mock(mapInterface, new ReturnsDeepStubs());

 // when
 Object answer = returnsDeepStubs.answer(new InvocationOnMock(mapMock, mapInterface.getMethod("entrySet"), (Object[]) null));

 // then
 assertEquals(mapMock, answer);

 // and when checking the iterator and the number
 Iterator<Entry<Number, Set<Number>>> mapIterator = ((Iterator<?>) answer);
 Entry<Number, Set<Number>> mapEntry = mapIterator.next();
 Number number = mapEntry.getValue().iterator().next();

 assertEquals(mapMock, number);
 }

 @org.junit.Test public void testReturnsEmptyValueForNonMockableClass() {
 // given
 Object nonMockableClassMock = mock(String[].class, new ReturnsEmptyValues());

 // when
 Object answer = returnsDeepStubs.answer(new InvocationOnMock(nonMockableClassMock, String[].class.getMethod("length"), (Object[]) null));

 // then
 assertNull(answer);
 }

 @org.junit.Test public void testVerificationMatchesReturnedStub() {
 // given
 GenericMetadataSupport mapMeta = new GenericMetadataSupport(Map.class, Set.class, Number.class);
 Object mapMock = mock(Map.class, new ReturnsDeepStubs());

 // when stubbing
 Object deepStub = mock(Object.class, new ReturnsDeepStubs());
 when(mapMock.entrySet()).thenReturn((Set<Entry<Number, Set<Number>>>) deepStub);

 // and when invoking the method
 InvocationOnMockInvocationHandler handler = (InvocationOnMockInvocationHandler) mock(InvocationOnMock.class);
 handler.setMock(mapMock);
 handler.setInvocation(new InvocationOnMock(mapMock, Map.class.getMethod("entrySet"), (Object[]) null));
 InvocationContainer invocationContainer = new InvocationContainerImpl(handler, new Object[] {});

 // then verifying the answer
 ArgumentCaptor<Set<Entry<Number, Set<Number>>>> deepStubCaptor = ArgumentCaptor.forClass(Set.class);
 verify(mapMock).entrySet();
 verify(deepStubCaptor.getValue()).iterator().next().getValue().iterator().next();
 assertEquals(deepStub, deepStubCaptor.getValue().iterator().next().getValue().iterator().next());
 }

 @org.junit.Test public void testCreationOfNewStubWithGenericMetadata() {
 // given
 GenericMetadataSupport listMeta = new GenericMetadataSupport(List.class, Integer.class);

 // when
 Object listMock = returnsDeepStubs.createNewDeepStubMock(listMeta);

 // then
 assertEquals(List.class, listMock.getClass());
 assertNotNull(listMock);
 }

 @org.junit.Test public void testCreationOfNewStubWithMultipleExtraInterfaces() {
 // given
 Class<Map<String, Set<Number>>> mapInterface = Map.class.getTypeParameterTypes()[0].getGenericComponentType();
 Class<Set<Number>> setClass = Set.class.getTypeParameters()[0];
 Class<Number> numberClass = Number.class;
 GenericMetadataSupport mapMeta = new GenericMetadataSupport(mapInterface, setClass, numberClass);
 mapInterface.getInterfaces();

 // when
 MockSettings mockSettings = new MockitoCore().mockSettings().defaultAnswer(returnsDeepStubsAnswerUsing(mapMeta));
 Object mapMock = mockitoCore.mock(mapInterface, mockSettings);

 // then
 assertEquals(mapInterface, mapMock.getClass().getInterfaces()[0]);
 assertEquals(mapMeta.rawType(), mapMock.getClass().getSuperclass());
 }

 @org.junit.Test public void testAnswerMethodCallsCreateNewDeepStubMock() {
 // given
 GenericMetadataSupport mapMeta = new GenericMetadataSupport(Map.class, Set.class, Number.class);
 Object mapMock = mockitoCore.mock(Map.class, new ReturnsDeepStubs());

 // when
 Object answer = returnsDeepStubs.answer(new InvocationOnMock(mapMock, Map.class.getMethod("entrySet"), (Object[]) null));

 // then
 MockSettings mockSettings = MockUtil.getMockSettings(answer);
 ReturnsDeepStubs deepStubs = (ReturnsDeepStubs) mockSettings.getDefaultAnswer();
 Object deepStub = deepStubs.createNewDeepStubMock(mapMeta);
 assertEquals(mapMeta.rawType(), deepStub.getClass().getSuperclass());
 }

 @org.junit.Test public void testAnswerMethodReturnsSameInstance() {
 // given
 GenericMetadataSupport listMeta = new GenericMetadataSupport(List.class, Integer.class);
 Object listMock1 = mockitoCore.mock(List.class, new ReturnsDeepStubs());
 Object listMock2 = mockitoCore.mock(List.class, new ReturnsDeepStubs());

 // when
 Object answer1 = returnsDeepStubs.answer(new InvocationOnMock(listMock1, List.class.getMethod("get", int.class), new Object[] { 0 }));
 Object answer2 = returnsDeepStubs.answer(new InvocationOnMock(listMock2, List.class.getMethod("get", int.class), new Object[] { 0 }));

 // then
 assertEquals(listMock1, answer1);
 assertEquals(listMock2, answer2);
 }

 @org.junit.Test public void testAnswerMethodReturnsSameInstanceWhenInvocationMatchesStubbedInvocation() {
 // given
 GenericMetadataSupport mapMeta = new GenericMetadataSupport(Map.class, Set.class, Number.class);
 Object mapMock = mockitoCore.mock(Map.class, new ReturnsDeepStubs());
 Object setMock = mockitoCore.mock(Set.class, new ReturnsDeepStubs());
 Object numberMock = mockitoCore.mock(Number.class, new ReturnsDeepStubs());
 InvocationContainerImpl container = new InvocationContainerImpl(mockitoCore.mock(InvocationOnMockInvocationHandler.class), new Object[] { mapMock, setMock, numberMock });
 container.addAnswer(new Answer<Set<Number>>() {
 @Override public Set<Number> answer(InvocationOnMock invocation) throws Throwable {
 return setMock;
 }
 }, false);

 // when
 Object mapAnswer = returnsDeepStubs.answer(new InvocationOnMock(mapMock, Map.class.getMethod("entrySet"), (Object[]) null));

 // then
 assertEquals(setMock, mapAnswer);
 }

 @org.junit.Test public void testAnswerMethodReturnsDeepStubbedMockWhenInvocationDoesNotMatchAnyStubbedInvocation() {
 // given
 GenericMetadataSupport mapMeta = new GenericMetadataSupport(Map.class, Set.class, Number.class);
 Object mapMock = mockitoCore.mock(Map.class, new ReturnsDeepStubs());
 Object setMock = mockitoCore.mock(Set.class, new ReturnsDeepStubs());
 Object numberMock = mockitoCore.mock(Number.class, new ReturnsDeepStubs());
 InvocationContainerImpl container = new InvocationContainerImpl(mockitoCore.mock(InvocationOnMockInvocationHandler.class), new Object[] { mapMock, setMock, numberMock });

 // when
 Object mapAnswer = returnsDeepStubs.answer(new InvocationOnMock(mapMock, Map.class.getMethod("get", Integer.class), new Object[] { 10 }));

 // then
 assertNotSame(setMock, mapAnswer);
 }

 @org.junit.Test public void testAnswerMethodRecordsAnswerForStubbedInvocation() {
 // given
 GenericMetadataSupport mapMeta = new GenericMetadataSupport(Map.class, Set.class, Number.class);
 Object mapMock = mockitoCore.mock(Map.class, new ReturnsDeepStubs());
 Object setMock = mockitoCore.mock(Set.class, new ReturnsDeepStubs());
 Object numberMock = mockitoCore.mock(Number.class, new ReturnsDeepStubs());
 InvocationContainerImpl container = new InvocationContainerImpl(mockitoCore.mock(InvocationOnMockInvocationHandler.class), new Object[] { mapMock, setMock, numberMock });
 container.addAnswer(new Answer<Set<Number>>() {
 @Override public Set<Number> answer(InvocationOnMock invocation) throws Throwable {
 return setMock;
 }
 }, false);

 // when
 Object mapAnswer = returnsDeepStubs.answer(new InvocationOnMock(mapMock, Map.class.getMethod("entrySet"), (Object[]) null));

 // then
 assertEquals(setMock, mapAnswer);

 // and when checking the stubbed invocation
 StubbedInvocationMatcher matcher = container.getStubbedInvocations()[0];
 assertEquals(mapMock, matcher.getInvocation().getMock());
 assertEquals("entrySet()", matcher.getMethodName());
 }

 @org.junit.Test public void testAnswerMethodRecordsAnswerForStubbedInvocationWithCustomMatcher() {
 // given
 GenericMetadataSupport mapMeta = new GenericMetadataSupport(Map.class, Set.class, Number.class);
 Object mapMock = mockitoCore.mock(Map.class, new ReturnsDeepStubs());
 Object setMock = mockitoCore.mock(Set.class, new ReturnsDeepStubs());
 Object numberMock = mockitoCore.mock(Number.class, new ReturnsDeepStubs());
 InvocationContainerImpl container = new InvocationContainerImpl(mockitoCore.mock(InvocationOnMockInvocationHandler.class), new Object[] { mapMock, setMock, numberMock });
 container.addAnswer(new Answer<Set<Number>>() {
 @Override public Set<Number> answer(InvocationOnMock invocation) throws Throwable {
 return setMock;
 }
 }, new StubbedInvocationMatcher() {
 @Override public boolean matches(InvocationOnMock invocation) {
 return Arrays.equals(invocation.getMethod().getParameterTypes(), new Class[] { Map.class });
 }
 });

 // when
 Object mapAnswer = returnsDeepStubs.answer(new InvocationOnMock(mapMock, Map.class.getMethod("get", Integer.class), new Object[] { 10 }));

 // then
 assertNull(mapAnswer);

 // and when checking the stubbed invocation
 StubbedInvocationMatcher matcher = container.getStubbedInvocations()[0];
 assertEquals(mapMock, matcher.getInvocation().getMock());
 assertEquals("get(Integer)", matcher.getMethodName());
 }

 @org.junit.Test public void testAnswerMethodRecordsAnswerForStubbedInvocationWithCustomMatcherAndCustomAnswer() {
 // given
 GenericMetadataSupport mapMeta = new GenericMetadataSupport(Map.class, Set.class, Number.class);
 Object mapMock = mockitoCore.mock(Map.class, new ReturnsDeepStubs());
 Object setMock = mockitoCore.mock(Set.class, new ReturnsDeepStubs());
 Object numberMock = mockitoCore.mock(Number.class, new ReturnsDeepStubs());
 InvocationContainerImpl container = new InvocationContainerImpl(mockitoCore.mock(InvocationOnMockInvocationHandler.class), new Object[] { mapMock, setMock, numberMock });
 container.addAnswer(new Answer<Integer>() {
 @Override public Integer answer(InvocationOnMock invocation) throws Throwable {
 return 100;
 }
 }, new StubbedInvocationMatcher() {
 @Override public boolean matches(InvocationOnMock invocation) {
 return Arrays.equals(invocation.getMethod().getParameterTypes(), new Class[] { Map.class, Integer.class });
 }
 });

 // when
 Object mapAnswer = returnsDeepStubs.answer(new InvocationOnMock(mapMock, Map.class.getMethod("get", Integer.class), new Object[] { 10 }));

 // then
 assertEquals(100, mapAnswer);
 }

 @org.junit.Test public void testAnswerMethodRecordsMultipleAnswerForStubbedInvocation() {
 // given
 GenericMetadataSupport mapMeta = new GenericMetadataSupport(Map.class, Set.class, Number.class);
 Object mapMock = mockitoCore.mock(Map.class, new ReturnsDeepStubs());
 Object setMock = mockitoCore.mock(Set.class, new ReturnsDeepStubs());
 Object numberMock = mockitoCore.mock(Number.class, new ReturnsDeepStubs());
 InvocationContainerImpl container = new InvocationContainerImpl(mockitoCore.mock(InvocationOnMockInvocationHandler.class), new Object[] { mapMock, setMock, numberMock });
 container.addAnswer(new Answer<Set<Number>>() {
 @Override public Set<Number> answer(InvocationOnMock invocation) throws Throwable {
 return setMock;
 }
 }, false);
 container.addAnswer(new Answer<Set<Number>>() {
 @Override public Set<Number> answer(InvocationOnMock invocation) throws Throwable {
 return new HashSet<>(Arrays.asList(1, 2, 3));
 }
 }, new StubbedInvocationMatcher() {
 @Override public boolean matches(InvocationOnMock invocation) {
 return Arrays.equals(invocation.getMethod().getParameterTypes(), new Class[] { Map.class });
 }
 });

 // when
 Object mapAnswer = returnsDeepStubs.answer(new InvocationOnMock(mapMock, Map.class.getMethod("entrySet"), (Object[]) null));

 // then
 assertNotNull(mapAnswer);
 assertEquals(1, mapAnswer.size());
 assertTrue(mapAnswer.contains(1));
 assertTrue(mapAnswer.contains(2));
 assertTrue(mapAnswer.contains(3));
 }
}