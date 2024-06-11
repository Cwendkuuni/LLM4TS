package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.internal.stubbing.Answer;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.AnswerWhoAnswers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class ReturnsDeepStubsTest {

 private static final Class<ReturnsDeepStubs> RETURNS_DEEP_STUBS_CLASS = ReturnsDeepStubs.class;

 @Test
 public void testReturnsDeepStubsForNonMockableTypes() throws Exception {
 Object nonMockableTypeMock = mock(String.class);
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

 Answer<Object> answer = returnsDeepStubs.answer(ArgumentMatchers.any(InvocationOnMock.class));
 Object nonMockableType = answer.answer(Mockito.mock(Object.class));

 assertEquals(nonMockableType, nonMockableTypeMock);
 }

 @Test
 public void testReturnsShallowStubForSimpleTypes() throws Exception {
 Object simpleTypeMock = mock(Integer.class);
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

 Answer<Object> answer = returnsDeepStubs.answer(ArgumentMatchers.any(InvocationOnMock.class));
 Object simpleType = answer.answer(Mockito.mock(Object.class));

 assertEquals(simpleTypeMock, simpleType);
 }

 @Test
 public void testReturnsStubOfSameTypeWithDifferentInvocations() throws Exception {
 Object genericTypeMock = mock(List.class);
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

 Answer<Object> answer = returnsDeepStubs.answer(ArgumentMatchers.any(InvocationOnMock.class));
 Object list1 = answer.answer(Mockito.mock(Object.class));
 Object list2 = answer.answer(Mockito.mock(Object.class));

 assertEquals(genericTypeMock, list1);
 assertEquals(genericTypeMock, list2);
 }

 @Test
 public void testReturnsNewStubForDifferentTypes() throws Exception {
 Object listMock = mock(List.class);
 Object mapMock = mock(Map.class);
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

 Answer<Object> answer = returnsDeepStubs.answer(ArgumentMatchers.any(InvocationOnMock.class));
 Object list = answer.answer(Mockito.mock(Object.class));
 Object map = answer.answer(Mockito.mock(Object.class));

 assertEquals(listMock, list);
 assertEquals(mapMock, map);
 }

 @Test
 public void testReturnsNewStubForNestedTypes() throws Exception {
 Object genericNestMock = mock(GenericsNest.class);
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

 Answer<Object> answer = returnsDeepStubs.answer(ArgumentMatchers.any(InvocationOnMock.class));
 Object genericNest = answer.answer(Mockito.mock(Object.class));
 Number number = ((GenericsNest<?>) genericNest).entrySet().iterator().next().getValue().iterator().next();

 assertEquals(genericNestMock, genericNest);
 assertEquals(number.getClass(), Number.class);
 }

 @Test
 public void testReturnsNewStubWhenInvocationMatchesStubbedInvocation() throws Exception {
 Object genericNestMock = mock(GenericsNest.class);
 Object genericNestDeepStub = mock(GenericsNest.class);

 when(genericNestMock.entrySet().iterator().next()).thenReturn(ArgumentMatchers.argThat(new AnswerWhoAnswers<Object>() {
 @Override
 public Object answer(InvocationOnMock invocation) throws Throwable {
 return genericNestDeepStub;
 }
 }));

 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Answer<Object> answer = returnsDeepStubs.answer(ArgumentMatchers.any(InvocationOnMock.class));
 Object genericNest = answer.answer(Mockito.mock(Object.class));
 Number number = ((GenericsNest<?>) genericNest).entrySet().iterator().next().getValue().iterator().next();

 assertEquals(genericNestDeepStub, genericNest);
 assertEquals(number.getClass(), Number.class);
 }

 @Test(expected = IllegalStateException.class)
 public void testThrowsExceptionIfTypeCannotBeMocked() throws Exception {
 Object illegalTypeMock = mock(IllegalType.class);
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

 Answer<Object> answer = returnsDeepStubs.answer(ArgumentMatchers.any(InvocationOnMock.class));
 answer.answer(Mockito.mock(Object.class));
 }
}

class IllegalType {}