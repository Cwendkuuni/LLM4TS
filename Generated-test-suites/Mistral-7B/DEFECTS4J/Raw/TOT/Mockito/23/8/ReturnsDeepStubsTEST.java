package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.annotations.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 @Mock
 private GenericsNest<Number> genericNestMock;
 @Mock
 private GenericsNest<String> stringGenericNestMock;

 @Before
 public void setup() {
 MockitoAnswer mockitoAnswer = mock(Answer.class);
 ReturnsDeepStubs returnsDeepStubsAnswer = new ReturnsDeepStubs();
 when(mockitoAnswer.answer(any(InvocationOnMock.class))).then(returnsDeepStubsAnswer);
 this.returnsDeepStubs = new ReturnsDeepStubs();
 }

 @Test
 public void testReturnDeepStubForGenericsTypeWithNestedGenerics() {
 // given
 GenericsNest<Map<Number, Set<Number>>> genericNest = mock(GenericsNest.class, with(returnsDeepStubs));

 // when
 Map<Number, Set<Number>> map = genericNest.entrySet().iterator().next().getValue();
 Set<Number> set = map.iterator().next().iterator().next();
 Number number = set.iterator().next();

 // then
 verify(genericNest).entrySet();
 verify(genericNest.entrySet()).iterator();
 verify(map).iterator();
 verify(map.iterator()).next();
 verify(map.iterator()).next().iterator();
 verify(map.iterator().next().iterator()).next();

 // verify that the returned mocks are the same instances
 Assert.assertEquals(number, genericNest.entrySet().iterator().next().getValue().iterator().next().iterator().next());
 Assert.assertEquals(number, map.iterator().next().iterator().next());
 }

 @Test
 public void testReturnEmptyValuesForNonMockableType() {
 // given
 when(returnsDeepStubs.answer(any(InvocationOnMock.class))).then(new ReturnsEmptyValues());

 // when
 Object value = returnsDeepStubs.answer(mock(NonMockableType.class).invoking(anyMethod()));

 // then
 Assert.assertNull(value);
 }

 @Test(expected = Throwable.class)
 public void testThrowExceptionWhenTypeCannotBeMocked() {
 // given
 when(returnsDeepStubs.answer(any(InvocationOnMock.class))).then(new ReturnsDeepStubs());

 // when, then
 doThrow(new RuntimeException("Test error")).when(genericNestMock).entrySet();
 returnsDeepStubs.answer(genericNestMock.entrySet());

 // verify error is thrown
 }

 @Test
 public void testReturnSameInstanceForStubbedInvocation() {
 // given
 when(genericNestMock.entrySet().iterator().next().getValue().iterator().next()).thenReturn(1);
 when(genericNestMock.entrySet()).thenReturn(Collections.singletonMap(1, new LinkedHashSet<Number>(){{add(2);}}));

 // when
 Object value1 = returnsDeepStubs.answer(genericNestMock.entrySet().iterator().next().getValue().iterator().next());
 Object value2 = returnsDeepStubs.answer(genericNestMock.entrySet().iterator().next().getValue().iterator().next());

 // then
 Assert.assertEquals(value1, value2);
 }

 @Test
 public void testReturnDifferentInstanceForDifferentInvocations() {
 // given
 when(genericNestMock.entrySet().iterator().next().getValue().iterator().next()).thenReturn(1);
 when(stringGenericNestMock.entrySet().iterator().next().getValue().iterator().next()).thenReturn(3);

 // when
 Object value1 = returnsDeepStubs.answer(genericNestMock.entrySet().iterator().next().getValue().iterator().next());
 Object value2 = returnsDeepStubs.answer(stringGenericNestMock.entrySet().iterator().next().getValue().iterator().next());
 // then
 Assert.assertNotSame(value1, value2);
 }
}

class NonMockableType {
 // A non-mockable type
}