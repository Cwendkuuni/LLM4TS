import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

 @Test
 public void testAnswerToStringMethod() {
 Mockito.when(mock.toString()).thenAnswer(Answers.delegatesTo(returnsEmptyValues));
 String expected = "Mock for [class test.ReturnsEmptyValuesTest], hashCode: 123456";
 String actual = Mockito.mock(String.class).toString();
 assertEquals(expected, actual);
 }

 @Test(expected = AssertionError.class)
 public void testAnswerCompareToMethod() {
 Mockito.when(mock.compareTo(any())).thenAnswer(Answers.delegatesTo(returnsEmptyValues));
 Mockito.verify(mock).compareTo(Mockito.any());
 fail("Expected comparison to throw an exception");
 }

 @Test
 public void testReturnValueForPrimitiveOrWrapperTypes() {
 Class<?> intType = int.class;
 Object intValue = returnsEmptyValues.returnValueFor(intType);
 assertEquals(0, intValue.hashCode());
 assertTrue(intValue instanceof Integer);
 assertEquals(0, ((Integer) intValue).intValue());

 Class<?> booleanType = boolean.class;
 Object booleanValue = returnsEmptyValues.returnValueFor(booleanType);
 assertEquals(false, booleanValue);
 }

 @Test
 public void testReturnValueForCollectionTypes() {
 Class<?> collectionType = Collection.class;
 Collection<?> collectionValue = (Collection<?>) returnsEmptyValues.returnValueFor(collectionType);
 assertTrue(collectionValue.isEmpty());
 }

 @Test
 public void testReturnValueForSetTypes() {
 Class<?> setType = Set.class;
 Set<?> setValue = (Set<?>) returnsEmptyValues.returnValueFor(setType);
 assertTrue(setValue.isEmpty());
 }

 @Test
 public void testReturnValueForHashSetTypes() {
 Class<?> hashSetType = HashSet.class;
 Set<?> hashSetValue = (Set<?>) returnsEmptyValues.returnValueFor(hashSetType);
 assertTrue(hashSetValue.isEmpty());
 }

 @Test
 public void testReturnValueForSortedSetTypes() {
 Class<?> sortedSetType = SortedSet.class;
 Set<?> sortedSetValue = (Set<?>) returnsEmptyValues.returnValueFor(sortedSetType);
 assertTrue(sortedSetValue.isEmpty());
 }

 @Test
 public void testReturnValueForTreeSetTypes() {
 Class<?> treeSetType = TreeSet.class;
 Set<?> treeSetValue = (Set<?>) returnsEmptyValues.returnValueFor(treeSetType);
 assertTrue(treeSetValue.isEmpty());
 }

 @Test
 public void testReturnValueForLinkedHashSetTypes() {
 Class<?> linkedHashSetType = LinkedHashSet.class;
 Set<?> linkedHashSetValue = (Set<?>) returnsEmptyValues.returnValueFor(linkedHashSetType);
 assertTrue(linkedHashSetValue.isEmpty());
 }

 @Test
 public void testReturnValueForListTypes() {
 Class<?> listType = List.class;
 List<?> listValue = (List<?>) returnsEmptyValues.returnValueFor(listType);
 assertTrue(listValue.isEmpty());
 }

 @Test
 public void testReturnValueForLinkedListTypes() {
 Class<?> linkedListType = LinkedList.class;
 List<?> linkedListValue = (List<?>) returnsEmptyValues.returnValueFor(linkedListType);
 assertTrue(linkedListValue.isEmpty());
 }

 @Test
 public void testReturnValueForArrayListTypes() {
 Class<?> arrayListType = ArrayList.class;
 List<?> arrayListValue = (List<?>) returnsEmptyValues.returnValueFor(arrayListType);
 assertTrue(arrayListValue.isEmpty());
 }

 @Test
 public void testReturnValueForMapTypes() {
 Class<?> mapType = Map.class;
 Map<?> mapValue = (Map<?, ?>) returnsEmptyValues.returnValueFor(mapType);
 assertTrue(mapValue.isEmpty());
 }

 @Test
 public void testReturnValueForHashMapTypes() {
 Class<?> hashMapType = HashMap.class;
 Map<String, String> hashMapValue = (Map<String, String>) returnsEmptyValues.returnValueFor(hashMapType);
 assertTrue(hashMapValue.isEmpty());
 }

 @Test
 public void testReturnValueForSortedMapTypes() {
 Class<?> sortedMapType = SortedMap.class;
 Map<String, String> sortedMapValue = (Map<String, String>) returnsEmptyValues.returnValueFor(sortedMapType);
 assertTrue(sortedMapValue.isEmpty());
 }

 @Test
 public void testReturnValueForTreeMapTypes() {
 Class<?> treeMapType = TreeMap.class;
 Map<String, String> treeMapValue = (Map<String, String>) returnsEmptyValues.returnValueFor(treeMapType);
 assertTrue(treeMapValue.isEmpty());
 }

 @Test
 public void testReturnValueForLinkedHashMapTypes() {
 Class<?> linkedHashMapType = LinkedHashMap.class;
 Map<String, String> linkedHashMapValue = (Map<String, String>) returnsEmptyValues.returnValueFor(linkedHashMapType);
 assertTrue(linkedHashMapValue.isEmpty());
 }

 @Test(expected = AssertionError.class)
 public void testReturnValueForNullType() {
 Class<?> nullType = null;
 Object nullValue = returnsEmptyValues.returnValueFor(nullType);
 fail("Expected NullPointerException");
 }

}