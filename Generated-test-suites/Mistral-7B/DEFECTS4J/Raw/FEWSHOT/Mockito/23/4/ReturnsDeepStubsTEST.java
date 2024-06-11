package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {

 private static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
 private static final Type[] EMPTY_TYPE_ARRAY = new Type[0];

 @Test
 public void testAnswerEmptyTypes() {
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 assertSame(new ReturnsEmptyValues(), returnsDeepStubs.delegate);
 }

 @Test
 public void testAnswerPrimitiveTypes() {
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 assertSame(new ReturnsEmptyValues(), returnsDeepStubs.delegate);

 byte[] bytes = new byte[0];
 byte mockByte = mock(byte[].class, new ReturnsDeepStubs());
 verify(mockByte).getClass();
 assertEquals(bytes, mockByte);
 }

 @Test
 public void testAnswerObject() {
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 assertSame(new ReturnsEmptyValues(), returnsDeepStubs.delegate);

 Object mock = mock(Object.class, new ReturnsDeepStubs());
 assertSame(mockitoCore(), mockitoCore());
 assertSame(mockitoCore(), mock.getClass().getDeclaredField("mockitoCore").get(mock));
 }

 @Test
 public void testAnswerArray() {
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 assertSame(new ReturnsEmptyValues(), returnsDeepStubs.delegate);

 int[] ints = new int[0];
 int[] mockInts = mock(int[].class, new ReturnsDeepStubs());
 verify(mockInts).getClass();
 assertArrayEquals(ints, mockInts);
 }

 @Test
 public void testAnswerCollection() {
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 assertSame(new ReturnsEmptyValues(), returnsDeepStubs.delegate);

 Collection<String> strings = Collections.emptyList();
 Collection<String> mockStrings = mock(Collection.class, new ReturnsDeepStubs());
 verify(mockStrings).getClass();
 assertEquals(strings, mockStrings);
 }

 @Test
 public void testAnswerMap() {
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 assertSame(new ReturnsEmptyValues(), returnsDeepStubs.delegate);

 Map<String, Integer> map = new HashMap<>();
 Map<String, Integer> mockMap = mock(Map.class, new ReturnsDeepStubs());
 verify(mockMap).getClass();
 assertEquals(map, mockMap);
 }

 @Test
 public void testAnswerClassWithNestedGenerics() {
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 assertSame(new ReturnsEmptyValues(), returnsDeepStubs.delegate);

 interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
 }

 GenericsNest<String> mock = mock(GenericsNest.class, new ReturnsDeepStubs());
 verify(mock).getClass();
 assertEquals("class org.mockito.internal.stubbing.defaultanswers.GenericsNest", mock.getClass().getName());
 }

 @Test
 public void testAnswerClassWithExtraInterfaces() {
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 assertSame(new ReturnsEmptyValues(), returnsDeepStubs.delegate);

 interface GenericsWithExtraInterfaces<T> extends Map<String, Set<Number>>, Serializable {
 }

 GenericsWithExtraInterfaces<String> mock = mock(GenericsWithExtraInterfaces.class, new ReturnsDeepStubs());
 verify(mock).getClass();
 assertEquals("class org.mockito.internal.stubbing.defaultanswers.GenericsWithExtraInterfaces", mock.getClass().getName());
 assertArrayEquals(new Class[]{Map.class, Serializable.class}, mock.getClass().getInterfaces());
 }

 @Test
 public void testAnswerClassWithNestedGenericsAndExtraInterfaces() {
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 assertSame(new ReturnsEmptyValues(), returnsDeepStubs.delegate);

 interface GenericsNestWithExtraInterfaces<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>>, Serializable {
 }

 GenericsNestWithExtraInterfaces<String> mock = mock(GenericsNestWithExtraInterfaces.class, new ReturnsDeepStubs());
 verify(mock).getClass();
 assertEquals("class org.mockito.internal.stubbing.defaultanswers.GenericsNestWithExtraInterfaces", mock.getClass().getName());
 assertArrayEquals(new Class[]{Map.class, Serializable.class}, mock.getClass().getInterfaces());
 }

 @Test
 public void testAnswerClassWithNestedGenericsAndDeepStubbing() {
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 assertSame(new ReturnsEmptyValues(), returnsDeepStubs.delegate);

 interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
 }

 interface GenericsNestWithDeepStubbing<K extends Comparable<K> & Cloneable> extends GenericsNest<K> {
 }

 GenericsNestWithDeepStubbing<String> mock = mock(GenericsNestWithDeepStubbing.class, new ReturnsDeepStubs());
 GenericsNest<String> innerMock = mock(GenericsNest.class, new ReturnsDeepStubs());
 verify(mock).getClass();
 assertEquals("class org.mockito.internal.stubbing.defaultanswers.GenericsNestWithDeepStubbing", mock.getClass().getName());
 assertSame(innerMock, mock.getMap());
 }

 @Test
 public void testAnswerClassWithNestedGenericsAndDeepStubbingAndExtraInterfaces() {
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 assertSame(new ReturnsEmptyValues(), returnsDeepStubs.delegate);

 interface GenericsNestWithExtraInterfaces<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>>, Serializable {
 }

 interface GenericsNestWithDeepStubbing<K extends Comparable<K> & Cloneable> extends GenericsNestWithExtraInterfaces<K> {
 }

 GenericsNestWithDeepStubbing<String> mock = mock(GenericsNestWithDeepStubbing.class, new ReturnsDeepStubs());
 GenericsNestWithExtraInterfaces<String> innerMock = mock(GenericsNestWithExtraInterfaces.class, new ReturnsDeepStubs());
 verify(mock).getClass();
 assertEquals("class org.mockito.internal.stubbing.defaultanswers.GenericsNestWithDeepStubbing", mock.getClass().getName());
 assertSame(innerMock, mock.getMap());
 assertArrayEquals(new Class[]{Map.class, Serializable.class}, mock.getClass().getInterfaces());
 }

 @Test
 public void testDeepStubbing() throws Exception {
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Object mock = mock(Object.class, new ReturnsDeepStubs());
 Object deepStub = mock(Object.class, new ReturnsDeepStubs());

 verify(mock).getClass();
 verify(mock).equals(deepStub);
 }

 @Test
 public void testDeepStubbingWithAnswer() throws Exception {
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Answer<Object> answer = mock(Answer.class);
 Object mock = mock(Object.class, answer);
 Object deepStub = mock(Object.class, new ReturnsDeepStubs());
 answer.answers(deepStub);

 verify(mock).getClass();
 verify(mock).equals(deepStub);
 }

 @Test
 public void testDeepStubbingWithAnswerWithGenericType() throws Exception {
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Answer<Collection<String>> answer = mock(Answer.class, withSettings().defaultAnswer(Collections.<String>singletonList("test")));
 Collection<String> mock = mock(Collection.class, answer);
 Collection<String> deepStub = mock(Collection.class, new ReturnsDeepStubs());
 answer.answers(deepStub);

 verify(mock).getClass();
 assertSame(deepStub, mock.iterator().next());
 }

 @Test
 public void testDeepStubbingWithAnswerWithGenericTypeAndNestedGenerics() throws Exception {
 ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
 Answer<Map<String, Collection<Number>>> answer = mock(Answer.class, withSettings().defaultAnswer(new HashMap<String, Collection<Number>>()));
 Map<String, Collection<Number>> mock = mock(Map.class, answer);
 Map<String, Collection<Number>> deepStub = mock(Map.class, new ReturnsDeepStubs());
 answer.answers(deepStub);

 verify(mock).getClass();
 assertSame(deepStub, mock.get("test"));
 }
}