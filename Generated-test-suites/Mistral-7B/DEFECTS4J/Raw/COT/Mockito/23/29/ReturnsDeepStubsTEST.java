package org.mockito.internal.stubbing.defaultanswers.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.invocation.MockitoMethod;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.reflection.GenericMetadataSupport;

import org.mockito.test.internal.stubbing.answers.ReturnsDeepStubs;

public class ReturnsDeepStubsTest {

 private static final Class<?> GENERICS_NEST_INTERFACE = getGenericNestInterface();

 @Test
 public void testReturnsStubWhenNotMockableType() {
 // given
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Object notMockableType = new Object() {};

 // when
 Object answer = returnsDeepStubs.answer(mock(notMockableType.getClass()));

 // then
 Assert.assertNotNull(answer);
 }

 @Test
 public void testReturnsEmptyValuesWhenNotGenericType() {
 // given
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Object notGenericType = new Object() {};

 // when
 Object answer = returnsDeepStubs.answer(mock(notGenericType.getClass()));

 // then
 Assert.assertEquals(Answers.RETURNS_DEEP_STUBS, answer);
 }

 @Test
 public void testReturnsSameStubForSameInvocation() {
 // given
 Object genericType = getGenericNestInstance();
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

 // when
 Object stub1 = returnsDeepStubs.answer(mock(genericType.getClass()));
 Object stub2 = returnsDeepStubs.answer(mock(genericType.getClass()));

 // then
 Assert.assertSame(stub1, stub2);
 }

 @Test
 public void testReturnsNewStubForDifferentInvocations() {
 // given
 Object genericType1 = getGenericNestInstance();
 Object genericType2 = getGenericNestInstance();
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

 // when
 Object stub1 = returnsDeepStubs.answer(mock(genericType1.getClass()));
 Object stub2 = returnsDeepStubs.answer(mock(genericType2.getClass()));

 // then
 Assert.assertNotSame(stub1, stub2);
 }

 @Test
 public void testGetMockWithMatchingStubbedInvocation() throws Throwable {
 // given
 Object genericType = getGenericNestInstance();
 Object number = 1;
 InvocationContainerImpl container = mock(InvocationContainerImpl.class);
 StubbedInvocationMatcher stubbedInvocationMatcher = stub(genericType).method("entrySet").returnValue(Collections.singletonMap(any(), any()));
 stubbedInvocationMatcher.answer(container);
 stub(genericType.getClass().getDeclaredField("iterator").get(genericType)).method("next").returnValue(number);
 stub(number).method("iterator").returnValue(Arrays.asList(1.0).iterator());
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

 // when
 Object answer = returnsDeepStubs.answer(mock(genericType.getClass()));

 // then
 Object deepStub = container.getInvocationForStubbing().getMock();
 Assert.assertSame(answer, deepStub);
 }

 @Test(expected = Throwable.class)
 public void testGetMockWithNoMatchingStubbedInvocation() throws Throwable {
 // given
 Object genericType = getGenericNestInstance();
 Object container = mock(InvocationContainerImpl.class);
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

 // when, then
 returnsDeepStubs.answer(mock(genericType.getClass()));
 verify(container).addAnswer(any(Answer.class), eq(false));
 }

 private static Class<?> getGenericNestInterface() {
 try {
 return Class.forName("org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs$GenericsNest");
 } catch (ClassNotFoundException e) {
 throw new RuntimeException(e);
 }
 }

 private static Object getGenericNestInstance() {
 try {
 return Class.forName("org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs$GenericsNest").newInstance();
 } catch (InstantiationException | IllegalAccessException e) {
 throw new RuntimeException(e);
 }
 }
}