import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import java.io.Serializable;
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

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues returnsEmptyValues;

 @Before
 public void setUp() {
 returnsEmptyValues = new ReturnsEmptyValues();
 }

 @Test
 public void testAnswer_toStringMethod() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Mockito.when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("toString"));
 Object mock = mock(String.class);
 Object result = returnsEmptyValues.answer(invocation);

 assertEquals("Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode(), result.toString());
 }

 @Test
 public void testAnswer_compareToMethod() {
 InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
 Mockito.when(invocation.getMethod()).thenReturn(ArgumentMatchers.any(Comparable.class).getClass().getMethod("compareTo", ArgumentMatchers.any(Comparable.class.getType())));
 Object mock = mock(Comparable.class);
 Object arg = mock(Comparable.class);
 Object result = returnsEmptyValues.answer(invocation);

 assertEquals(1, result.getClass().cast(result).compareTo(arg));
 }

 @Test
 public void testReturnValueFor_PrimitiveTypes() {
 Class<?> intType = int.class;
 Object result = returnsEmptyValues.returnValueFor(intType);
 assertEquals(0, result);
 }

 @Test
 public void testReturnValueFor_CollectionTypes_ArrayList() {
 Class<?> collectionType = ArrayList.class;
 Object result = returnsEmptyValues.returnValueFor(collectionType);
 assertTrue(result instanceof ArrayList);
 assertEquals(0, ((List<?>) result).size());
 }

 @Test
 public void testReturnValueFor_CollectionTypes_LinkedList() {
 Class<?> collectionType = LinkedList.class;
 Object result = returnsEmptyValues.returnValueFor(collectionType);
 assertTrue(result instanceof LinkedList);
 assertEquals(0, ((List<?>) result).size());
 }

 @Test
 public void testReturnValueFor_CollectionTypes_HashSet() {
 Class<?> collectionType = HashSet.class;
 Object result = returnsEmptyValues.returnValueFor(collectionType);
 assertTrue(result instanceof HashSet);
 assertEquals(0, ((Set<?>) result).size());
 }

 @Test
 public void testReturnValueFor_CollectionTypes_LinkedHashSet() {
 Class<?> collectionType = LinkedHashSet.class;
 Object result = returnsEmptyValues.returnValueFor(collectionType);
 assertTrue(result instanceof LinkedHashSet);
 assertEquals(0, ((Set<?>) result).size());
 }

 @Test
 public void testReturnValueFor_CollectionTypes_TreeSet() {
 Class<?> collectionType = TreeSet.class;
 Object result = returnsEmptyValues.returnValueFor(collectionType);
 assertTrue(result instanceof TreeSet);
 assertEquals(0, ((Set<?>) result).size());
 }

 @Test
 public void testReturnValueFor_CollectionTypes_TreeMap() {
 Class<?> collectionType = TreeMap.class;
 Object result = returnsEmptyValues.returnValueFor(collectionType);
 assertTrue(result instanceof TreeMap);
 assertEquals(0, ((Map<?, ?>) result).size());
 }

 @Test
 public void testReturnValueFor_CollectionTypes_LinkedHashMap() {
 Class<?> collectionType = LinkedHashMap.class;
 Object result = returnsEmptyValues.returnValueFor(collectionType);
 assertTrue(result instanceof LinkedHashMap);
 assertEquals(0, ((Map<?, ?>) result).size());
 }

 @Test(expected = NullPointerException.class)
 public void testReturnValueFor_NullType() {
 Class<?> nullType = null;
 Object result = returnsEmptyValues.returnValueFor(nullType);
 assertNull(result);
 }
}