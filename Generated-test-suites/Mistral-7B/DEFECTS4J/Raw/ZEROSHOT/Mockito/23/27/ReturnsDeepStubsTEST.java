package org.mockito.internal.stubbing.defaultanswers.test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.answers.ReturnsDeepStubs;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;

 @Before
 public void setUp() {
 returnsDeepStubs = new ReturnsDeepStubs();
 }

 @Test
 public void testAnswerWithDeepStubs() {
 Object mock = mock(Object.class, new ReturnsDeepStubs());

 Object deepStub = mock(Object.class, new ReturnsDeepStubs());
 when(mock.getDeepStub()).thenReturn(deepStub);

 Object result = mock.answerWithDeepStubs();

 assertEquals(deepStub, result);
 }

 @Test
 public void testAnswerWithEmptyValues() {
 Object mock = mock(Object.class, new ReturnsDeepStubs());

 Object result = mock.answerWithDeepStubs();

 assertNull(result);
 }

 @Test
 public void testAnswerWithSameType() {
 Object mock = mock(Object.class, new ReturnsDeepStubs());
 Object deepStub = mock(Object.class, new ReturnsDeepStubs());

 when(mock.getDeepStub()).thenReturn(deepStub);

 ArgumentList argumentList = new ArgumentList(new Object[] { mock });
 Stubbing call = stub(mock.getClass()).answers(Answers.RETURNS_DEEP_STUBS).withArguments(argumentList).thenReturn(deepStub);

 Object result = mock.answerWithDeepStubs();

 assertEquals(deepStub, result);

 verify(mock, times(1)).answerWithDeepStubs();
 }

 @Test
 public void testAnswerWithDifferentTypes() {
 Object mockObject = mock(Object.class, new ReturnsDeepStubs());
 Object mockList = mock(List.class, new ReturnsDeepStubs());
 Object mockNumber = mock(Number.class, new ReturnsDeepStubs());

 when(mockObject.getDeepStub()).thenReturn(mockList);
 when(mockList.getDeepStub()).thenReturn(mockNumber);

 ArgumentList argumentList = new ArgumentList(new Object[] { mockObject });

 Object result = mockObject.answerWithDeepStubs();

 assertEquals(mockNumber, result);

 verify(mockObject, times(1)).answerWithDeepStubs();
 }

 @Test
 public void testAnswerWithNestedGenerics() {
 interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
 }

 GenericsNest<Object> mock = mock(GenericsNest.class, new ReturnsDeepStubs());
 Number number = mock.entrySet().iterator().next().getValue().iterator().next();

 ArgumentList argumentList = new ArgumentList(new Object[] { mock });

 Object result = mock.answerWithDeepStubs();

 assertEquals(number, result);

 verify(mock, times(1)).answerWithDeepStubs();
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAnswerWithUnsupportedType() {
 Object mock = mock(Number[].class, new ReturnsDeepStubs());

 ArgumentList argumentList = new ArgumentList(new Object[] { mock });

 mock.answerWithDeepStubs();
 }

 @Test
 public void testAnswerWithSerializableType() throws Exception {
 Object mock = mock(Serializable.class, new ReturnsDeepStubs());

 Object result = mock.answerWithDeepStubs();

 assertNotNull(result);
 assertInstanceOf(Serializable.class, result);
 }
}

class ArgumentList {
 private List<Object> arguments;

 ArgumentList(Object[] arguments) {
 this.arguments = new LinkedList<>(Arrays.asList(arguments));
 }
}