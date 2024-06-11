import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues returnsEmptyValues;

 @Before
 public void setup() {
 returnsEmptyValues = new ReturnsEmptyValues();
 }

 @Test
 public void testAnswerToStringMethod() {
 InvocationOnMock invocation = mock(Object.class).invocationOnMethod("toString", (Object[]) null);
 assertEquals("Mock for <class name of mocked object>, hashCode: <some hash code>", returnsEmptyValues.answer(invocation));
 }

 @Test
 public void testAnswerCompareToMethod() {
 Object mock = mock(Comparable.class);
 Object anotherMock = mock(Comparable.class);
 InvocationOnMock invocation = mock(Object.class).invocationOnMethod("compareTo", mock, anotherMock);
 assertEquals(0, returnsEmptyValues.answer(invocation).intValue());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAnswerEmptyCollectionMethods_ModifyReturnedCollection() {
 Collection<Object> collection = (Collection<Object>) returnsEmptyValues.answer(mock(MethodWithCollectionReturnType.class).invocationOnMethod("getCollection", (Object[]) null));
 collection.add("test");
 }

 @Test
 public void testAnswerEmptySetMethods() {
 Class<? extends Set<Object>> setType = HashSet.class;
 Set<Object> set = (Set<Object>) returnsEmptyValues.answer(mock(MethodWithSetReturnType.class).invocationOnMethod("getSet", (Object[]) null));
 assertEquals(0, set.size());
 }

 @Test
 public void testAnswerEmptySortedSetMethods() {
 Class<? extends SortedSet<Object>> sortedSetType = TreeSet.class;
 SortedSet<Object> sortedSet = (SortedSet<Object>) returnsEmptyValues.answer(mock(MethodWithSortedSetReturnType.class).invocationOnMethod("getSortedSet", (Object[]) null));
 assertEquals(0, sortedSet.size());
 }

 @Test
 public void testAnswerEmptyListMethods() {
 Class<? extends List<Object>> listType = LinkedList.class;
 List<Object> list = (List<Object>) returnsEmptyValues.answer(mock(MethodWithListReturnType.class).invocationOnMethod("getList", (Object[]) null));
 assertEquals(0, list.size());
 }

 @Test
 public void testAnswerEmptyMapMethods() {
 Class<? extends Map<Object, Object>> mapType = HashMap.class;
 Map<Object, Object> map = (Map<Object, Object>) returnsEmptyValues.answer(mock(MethodWithMapReturnType.class).invocationOnMethod("getMap", (Object[]) null));
 assertEquals(0, map.size());
 }

 @Test
 public void testAnswerNullableType() {
 InvocationOnMock invocation = mock(Object.class).invocationOnMethod("getNull", (Object[]) null);
 assertEquals(null, returnsEmptyValues.answer(invocation));
 }

 @Test
 public void testAnswerCustomClass() {
 Class<?> customClass = MyCustomClass.class;
 MyCustomClass customInstance = mock(MyCustomClass.class);
 InvocationOnMock invocation = mock(Object.class).invocationOnMethod("getCustomClassInstance", customInstance);
 Answer<Object> customAnswer = Answers.returns(customInstance);
 Object result = returnsEmptyValues.answer(invocation);
 assertEquals(customInstance, result);
 }

 @Test(expected = NullPointerException.class)
 public void testAnswerNullMethod() {
 InvocationOnMock invocation = mock(Object.class).invocationOnMethod("getNullMethod", (Object[]) null);
 returnsEmptyValues.answer(invocation); //This should throw a NullPointerException
 }

}