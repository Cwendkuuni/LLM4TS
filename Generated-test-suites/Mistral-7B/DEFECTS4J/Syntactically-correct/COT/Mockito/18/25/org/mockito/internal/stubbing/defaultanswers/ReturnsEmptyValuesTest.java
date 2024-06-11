import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues returnsEmptyValues;

 @Before
 public void setup() {
 returnsEmptyValues = new ReturnsEmptyValues();
 }

 @Test
 public void testAnswer_toStringMethod() {
 InvocationOnMock invocation = mock(Object.class);
 when(invocation.getMock()).thenReturn(new Object());

 String result = (String) returnsEmptyValues.answer(invocation);

 assertEquals("Mock for Object, hashCode: [hashcode]", result);
 }

 @Test
 public void testAnswer_compareToMethod() {
 Object mock = new Object();
 Object other = new Object();

 int result = (Integer) returnsEmptyValues.answer(argThat(invocation -> methodsGuru.isCompareToMethod(invocation.getMethod())));

 assertEquals(mock == other ? 0 : 1, result);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAnswer_CollectionMethod_modification() {
 Collection<String> mockCollection = mock(Collection.class);
 when(mockCollection).thenReturn(returnsEmptyValues.answer(argThat(invocation -> invocation.getMethod().getReturnType() == Collection.class)));

 mockCollection.add("Test");
 }

 @Test
 public void testAnswer_nullForEverythingElse() {
 Class<?> type = null;
 Object result = returnsEmptyValues.returnValueFor(type);

 assertNotNull(result);
 assertEquals(null, result);
 }

 @Test
 public void testAnswer_PrimitiveTypes() {
 Class<?> intType = int.class;
 Object intResult = returnsEmptyValues.returnValueFor(intType);

 assertEquals(0, intResult);
 }

 @Test
 public void testAnswer_CollectionTypes() {
 Class<?> collectionType = Collection.class;
 Object collectionResult = returnsEmptyValues.returnValueFor(collectionType);

 assertNotNull(collectionResult);
 assertEquals(true, collectionResult instanceof Collection);
 }

 @Test
 public void testAnswer_SetTypes() {
 Class<?> setType = HashSet.class;
 Object setResult = returnsEmptyValues.returnValueFor(setType);

 assertNotNull(setResult);
 assertEquals(true, setResult instanceof Set);
 }

 @Test
 public void testAnswer_ListTypes() {
 Class<?> listType = ArrayList.class;
 Object listResult = returnsEmptyValues.returnValueFor(listType);

 assertNotNull(listResult);
 assertEquals(true, listResult instanceof List);
 }

 @Test
 public void testAnswer_MapTypes() {
 Class<?> mapType = HashMap.class;
 Object mapResult = returnsEmptyValues.returnValueFor(mapType);

 assertNotNull(mapResult);
 assertEquals(true, mapResult instanceof Map);
 }
}