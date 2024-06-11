package org.mockito.internal.stubbing.defaultanswers;

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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.invocation;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues returnsEmptyValues;
 private ObjectMethodsGuru methodsGuru;
 private MockUtil mockUtil;

 @Before
 public void setup() {
 this.returnsEmptyValues = new ReturnsEmptyValues();
 this.methodsGuru = new ObjectMethodsGuru();
 this.mockUtil = new MockUtil();
 }

 @Test
 public void testToString() {
 MockitoMock mock = mock(Object.class);
 when(mock.toString()).thenAnswer(returnsEmptyValues);
 assertEquals("Mock for Object, hashCode: 1211527553", mock.toString());
 }

 @Test
 public void testCompareToMethod() {
 Object mock1 = mock(Object.class);
 Object mock2 = mock(Object.class);
 when(mock1.compareTo(mock2)).thenAnswer(returnsEmptyValues);
 assertEquals(1, mock1.compareTo(mock2));
 }

 @Test
 public void testPrimitivesAndWrappers() {
 Byte byteMock = mock(Byte.class);
 Short shortMock = mock(Short.class);
 Integer integerMock = mock(Integer.class);
 Long longMock = mock(Long.class);
 Float floatMock = mock(Float.class);
 Double doubleMock = mock(Double.class);
 Character charMock = mock(Character.class);

 when(byteMock.byteValue()).thenAnswer(returnsEmptyValues);
 when(shortMock.shortValue()).thenAnswer(returnsEmptyValues);
 when(integerMock.intValue()).thenAnswer(returnsEmptyValues);
 when(longMock.longValue()).thenAnswer(returnsEmptyValues);
 when(floatMock.floatValue()).thenAnswer(returnsEmptyValues);
 when(doubleMock.doubleValue()).thenAnswer(returnsEmptyValues);
 when(charMock.charValue()).thenAnswer(returnsEmptyValues);

 assertEquals((byte) 0, byteMock.byteValue());
 assertEquals((short) 0, shortMock.shortValue());
 assertEquals(0, integerMock.intValue());
 assertEquals(0L, longMock.longValue());
 assertEquals(0.0f, floatMock.floatValue(), 0.001f);
 assertEquals(0.0d, doubleMock.doubleValue(), 0.001d);
 assertEquals('\u0000', charMock.charValue());
 }

 @Test
 public void testEmptyCollections() {
 List<Object> listMock = mock(List.class);
 Set<Object> setMock = mock(Set.class);
 Collection<Object> collectionMock = mock(Collection.class);

 when(listMock.isEmpty()).thenReturn(true);
 when(listMock.size()).thenReturn(0);
 when(listMock.toString()).thenAnswer(returnsEmptyValues);
 when(listMock.iterator()).thenReturn(new LinkedList<Object>().iterator());
 when(listMock.get(0)).thenThrow(IndexOutOfBoundsException.class);

 when(setMock.isEmpty()).thenReturn(true);
 when(setMock.size()).thenReturn(0);
 when(setMock.toString()).thenAnswer(returnsEmptyValues);
 when(setMock.iterator()).thenReturn(new HashSet<Object>().iterator());
 when(setMock.add(any())).thenReturn(true);
 when(setMock.contains(any())).thenReturn(false);

 when(collectionMock.isEmpty()).thenReturn(true);
 when(collectionMock.size()).thenReturn(0);
 when(collectionMock.toString()).thenAnswer(returnsEmptyValues);
 when(collectionMock.iterator()).thenReturn(new ArrayList<Object>().iterator());
 when(collectionMock.add(any())).thenReturn(true);

 // Testing List methods
 verify(listMock).isEmpty();
 verify(listMock).size();
 verify(listMock).toString();
 verify(listMock).iterator().hasNext();
 verify(listMock).iterator().next();
 verify(listMock).get(0).equals(null);

 // Testing Set methods
 verify(setMock).isEmpty();
 verify(setMock).size();
 verify(setMock).toString();
 verify(setMock).iterator().hasNext();
 verify(setMock).iterator().next().equals(null);
 verify(setMock).contains(any(Object.class));
 verify(setMock).add(any(Object.class));
 }

 @Test
 public void testEmptyMaps() {
 Map<Object, Object> mapMock = mock(Map.class);
 SortedMap<Object, Object> sortedMapMock = mock(SortedMap.class);

 when(mapMock.isEmpty()).thenReturn(true);
 when(mapMock.size()).thenReturn(0);
 when(mapMock.toString()).thenAnswer(returnsEmptyValues);
 when(mapMock.keySet()).thenReturn(new HashSet<Object>());
 when(mapMock.values()).thenReturn(new ArrayList<Object>());
 when(mapMock.entrySet()).thenReturn(new HashSet<Map.Entry<Object, Object>>());
 when(mapMock.get(any())).thenReturn(null);
 when(mapMock.put(any(), any())).thenReturn(null);

 when(sortedMapMock.isEmpty()).thenReturn(true);
 when(sortedMapMock.size()).thenReturn(0);
 when(sortedMapMock.toString()).thenAnswer(returnsEmptyValues);
 when(sortedMapMock.firstKey()).thenReturn(null);
 when(sortedMapMock.lastKey()).thenReturn(null);
 when(sortedMapMock.headMap(any())).thenReturn(mapMock);
 when(sortedMapMock.tailMap(any())).thenReturn(mapMock);
 when(sortedMapMock.subMap(any(), any())).thenReturn(mapMock);

 // Testing Map methods
 verify(mapMock).isEmpty();
 verify(mapMock).size();
 verify(mapMock).toString();
 verify(mapMock.keySet()).isEmpty();
 verify(mapMock.values()).isEmpty();
 verify(mapMock.entrySet()).isEmpty();
 verify(mapMock.get(any())).equals(null);
 verify(mapMock.put(any(), any())).isNull();

 // Testing SortedMap methods
 verify(sortedMapMock).isEmpty();
 verify(sortedMapMock).size();
 verify(sortedMapMock).toString();
 verify(sortedMapMock.firstKey()).equals(null);
 verify(sortedMapMock.lastKey()).equals(null);
 verify(sortedMapMock.headMap(any())).isInstanceOf(Map.class);
 verify(sortedMapMock.tailMap(any())).isInstanceOf(Map.class);
 verify(sortedMapMock.subMap(any(), any())).isInstanceOf(Map.class);
 }
}