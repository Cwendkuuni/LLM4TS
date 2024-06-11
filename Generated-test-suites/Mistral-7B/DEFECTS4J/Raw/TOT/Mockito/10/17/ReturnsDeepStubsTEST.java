import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReturnsDeepStubsTest {

 private ReturnsDeepStubs returnsDeepStubs;
 private Object mock1;
 private Object mock2;
 private Object mock3;

 @Before
 public void setup() {
 mock1 = Mockito.mock(Object.class, Mockito.withSettings().defaultAnswer(ReturnsDeepStubs.RETURNS_DEEP_STUBS));
 mock2 = Mockito.mock(List.class, Mockito.withSettings().defaultAnswer(ReturnsDeepStubs.RETURNS_DEEP_STUBS));
 mock3 = Mockito.mock(Object.class, Mockito.withSettings().defaultAnswer(ReturnsDeepStubs.RETURNS_DEEP_STUBS));
 returnsDeepStubs = Mockito.spy(ReturnsDeepStubs.class);
 }

 @Test
 public void testDeepStubAnswerForObject() {
 Object value = returnsDeepStubs.answer(Mockito.argThat(invocation -> {
 throw new AssertionError("Unexpected invocation");
 }));
 verify(mock1).answer(ArgumentMatchers.eq(value));
 }

 @Test
 public void testDeepStubAnswerForList() {
 List<Object> list = (List<Object>) returnsDeepStubs.answer(Mockito.argThat(invocation -> {
 throw new AssertionError("Unexpected invocation");
 }));
 verify(mock2).answer(ArgumentMatchers.eq(list));
 }

 @Test
 public void testDeepStubForSameMock() {
 Object mock = mock1;
 InvocationOnMock invocation = Mockito.mock(Object.class, Mockito.withSettings().defaultAnswer(ReturnsDeepStubs.RETURNS_DEEP_STUBS));
 Object deepStub = returnsDeepStubs.deepStub(invocation, Mockito.type(mock.getClass()));
 verify(mock).answer(ArgumentMatchers.eq(deepStub));
 }

 @Test
 public void testDeepStubForDifferentMock() {
 Object mock1 = Mockito.mock(Object.class, Mockito.withSettings().defaultAnswer(ReturnsDeepStubs.RETURNS_DEEP_STUBS));
 Object mock2 = Mockito.mock(Object.class, Mockito.withSettings().defaultAnswer(ReturnsDeepStubs.RETURNS_DEEP_STUBS));
 Object deepStub1 = returnsDeepStubs.deepStub(Mockito.mock(Object.class), Mockito.type(mock1.getClass()));
 Object deepStub2 = returnsDeepStubs.deepStub(Mockito.mock(Object.class), Mockito.type(mock2.getClass()));
 assertNotSame(deepStub1, deepStub2);
 }

 @Test(expected = Throwable.class)
 public void testExceptionWhenMockIsNotMockable() {
 Object nonMockableType = new Object() {
 };
 returnsDeepStubs.answer(Mockito.argThat(invocation -> {
 throw new AssertionError("Unexpected invocation");
 }));
 // this should throw an exception as Object is not mockable
 }

 @Test
 public void testSerialization() {
 Object serializedMock1 = ReturnsDeepStubsTest.serializer(mock1);
 Object deserializedMock1 = ReturnsDeepStubsTest.deserializer(serializedMock1);
 assertEquals(mock1, deserializedMock1);
 }

 private static Object serializer(Object object) {
 try {
 return Mockito.serialize(object);
 } catch (Exception e) {
 throw new RuntimeException("Failed to serialize mock object", e);
 }
 }

 private static Object deserializer(Object object) {
 try {
 return Mockito.deserialize(object);
 } catch (Exception e) {
 throw new RuntimeException("Failed to deserialize mock object", e);
 }
 }

 @Test
 public void testDeepStubsAnswerForNestedGenerics() {
 interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
 }
 Object mockGenericsNest = Mockito.mock(GenericsNest.class, Mockito.withSettings().defaultAnswer(ReturnsDeepStubs.RETURNS_DEEP_STUBS));
 Number number = mockGenericsNest.entrySet().iterator().next().getValue().iterator().next();
 Object deepStubNumber = returnsDeepStubs.deepStub(Mockito.mock(Number.class), Mockito.type(Number.class));
 assertEquals(number, deepStubNumber);
 }

 @Test
 public void testDeepStubsAnswerForNestedGenericsWithExtraInterfaces() {
 interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>>, Serializable {
 }
 Object mockGenericsNest = Mockito.mock(GenericsNest.class, Mockito.withSettings().defaultAnswer(ReturnsDeepStubs.RETURNS_DEEP_STUBS));
 Number number = mockGenericsNest.entrySet().iterator().next().getValue().iterator().next();
 Object deepStubNumber = returnsDeepStubs.deepStub(Mockito.mock(Number.class), Mockito.type(Number.class));
 assertEquals(number, deepStubNumber);
 }

 @Test
 public void testDeepStubsAnswerForNestedGenericsWithMultipleTypes() {
 interface GenericsNest<K extends Comparable<K> & Cloneable, L extends Comparable<L>> extends Map<K, Set<L>> {
 }
 Object mockGenericsNest = Mockito.mock(GenericsNest.class, Mockito.withSettings().defaultAnswer(ReturnsDeepStubs.RETURNS_DEEP_STUBS));
 Number number = mockGenericsNest.entrySet().iterator().next().getValue().iterator().next();
 Number deepStubNumber = returnsDeepStubs.deepStub(Mockito.mock(Number.class), Mockito.type(Number.class));
 assertEquals(number, deepStubNumber);
 L list = mockGenericsNest.entrySet().iterator().next().getValue();
 Set<Number> set = list.iterator().next();
 Number deepStubNumberInSet = returnsDeepStubs.deepStub(Mockito.mock(Number.class), Mockito.type(Number.class));
 assertEquals(deepStubNumberInSet, set.iterator().next());
 }

 @Test
 public void testDeepStubsAnswerForNestedGenericsWithCollections() {
 interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Collection<Number>> {
 }
 Object mockGenericsNest = Mockito.mock(GenericsNest.class, Mockito.withSettings().defaultAnswer(ReturnsDeepStubs.RETURNS_DEEP_STUBS));
 Number number = mockGenericsNest.entrySet().iterator().next().getValue().iterator().next();
 Collection<Number> collection = mockGenericsNest.entrySet().iterator().next().getValue();
 Number deepStubNumber = returnsDeepStubs.deepStub(Mockito.mock(Number.class), Mockito.type(Number.class));
 assertEquals(number, deepStubNumber);
 assertEquals(Collections.singleton(deepStubNumber), collection);
 }

 @Test
 public void testDeepStubsAnswerForNestedGenericsWithArrays() {
 interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Object[]> {
 }
 Object mockGenericsNest = Mockito.mock(GenericsNest.class, Mockito.withSettings().defaultAnswer(ReturnsDeepStubs.RETURNS_DEEP_STUBS));
 Number number = mockGenericsNest.entrySet().iterator().next().getValue().iterator().next();
 Object[] array = mockGenericsNest.entrySet().iterator().next().getValue();
 Number deepStubNumber = returnsDeepStubs.deepStub(Mockito.mock(Number.class), Mockito.type(Number.class));
 assertEquals(number, deepStubNumber);
 assertArrayEquals(new Object[]{deepStubNumber}, array);
 }

 @Test
 public void testDeepStubsAnswerForNestedGenericsWithArraysOfPrimitives() {
 interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, int[]> {
 }
 Object mockGenericsNest = Mockito.mock(GenericsNest.class, Mockito.withSettings().defaultAnswer(ReturnsDeepStubs.RETURNS_DEEP_STUBS));
 Number number = mockGenericsNest.entrySet().iterator().next().getValue().iterator().next();
 int[] array = mockGenericsNest.entrySet().iterator().next().getValue();
 int deepStubNumber = (int) returnsDeepStubs.deepStub(Mockito.mock(Number.class), Mockito.type(Number.class));
 assertEquals(number, deepStubNumber);
 assertArrayEquals(new int[]{deepStubNumber}, array);
 }
}