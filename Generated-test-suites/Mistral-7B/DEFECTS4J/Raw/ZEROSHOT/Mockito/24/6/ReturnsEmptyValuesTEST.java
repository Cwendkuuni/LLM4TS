package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class ReturnsEmptyValuesTest {

 private ReturnsEmptyValues returnsEmptyValues;

 @Before
 public void setUp() {
 returnsEmptyValues = new ReturnsEmptyValues();
 }

 @Test
 public void testToStringAnswer() {
 Mockito.<String>stub(String.class).toReturnValues(returnsEmptyValues);
 String mock = Mockito.mock(String.class);
 String actual = mock.toString();
 String expected = "Mock for class java.lang.String, hashCode: " + System.identityHashCode(mock);
 assertEquals(expected, actual);
 }

 @Test
 public void testCompareToAnswer() {
 Mockito.<Comparable>stub(Comparable.class).toReturnValues(returnsEmptyValues);
 Comparable mock = Mockito.mock(Comparable.class);
 int actual = mock.compareTo(Mockito.mock(Comparable.class));
 assertEquals(1, actual);
 }

 @Test
 public void testPrimitiveTypesAnswer() {
 Mockito.<Byte>stub(Byte.class).toReturnValues(returnsEmptyValues);
 Byte mockByte = Mockito.mock(Byte.class);
 byte expectedByte = 0;
 byte actualByte = mockByte.byteValue();
 assertEquals(expectedByte, actualByte);

 Mockito.<Integer>stub(Integer.class).toReturnValues(returnsEmptyValues);
 Integer mockInteger = Mockito.mock(Integer.class);
 int expectedInt = 0;
 int actualInt = mockInteger.intValue();
 assertEquals(expectedInt, actualInt);

 Mockito.<Long>stub(Long.class).toReturnValues(returnsEmptyValues);
 Long mockLong = Mockito.mock(Long.class);
 long expectedLong = 0L;
 long actualLong = mockLong.longValue();
 assertEquals(expectedLong, actualLong);

 Mockito.<Short>stub(Short.class).toReturnValues(returnsEmptyValues);
 Short mockShort = Mockito.mock(Short.class);
 short expectedShort = 0;
 short actualShort = mockShort.shortValue();
 assertEquals(expectedShort, actualShort);

 Mockito.<Character>stub(Character.class).toReturnValues(returnsEmptyValues);
 Character mockCharacter = Mockito.mock(Character.class);
 char expectedCharacter = '\u0000';
 char actualCharacter = mockCharacter.charValue();
 assertEquals(expectedCharacter, actualCharacter);

 Mockito.<Boolean>stub(Boolean.class).toReturnValues(returnsEmptyValues);
 Boolean mockBoolean = Mockito.mock(Boolean.class);
 boolean expectedBoolean = false;
 boolean actualBoolean = mockBoolean.booleanValue();
 assertEquals(expectedBoolean, actualBoolean);
 }

 @Test
 public void testEmptyCollectionAnswer() {
 Mockito.<Collection>stub(Collection.class).toReturnValues(returnsEmptyValues);
 Collection<String> mockCollection = Mockito.mock(Collection.class);
 assertNotNull(mockCollection);
 assertTrue(mockCollection.isEmpty());
 }

 @Test
 public void testEmptySetAnswer() {
 Mockito.<Set>stub(Set.class).toReturnValues(returnsEmptyValues);
 Set<String> mockSet = Mockito.mock(Set.class);
 assertNotNull(mockSet);
 assertTrue(mockSet.isEmpty());
 }

 @Test
 public void testEmptyHashSetAnswer() {
 Mockito.<HashSet>stub(HashSet.class).toReturnValues(returnsEmptyValues);
 HashSet<String> mockHashSet = Mockito.mock(HashSet.class);
 assertNotNull(mockHashSet);
 assertTrue(mockHashSet.isEmpty());
 }

 @Test
 public void testEmptyTreeSetAnswer() {
 Mockito.<TreeSet>stub(TreeSet.class).toReturnValues(returnsEmptyValues);
 TreeSet<String> mockTreeSet = Mockito.mock(TreeSet.class);
 assertNotNull(mockTreeSet);
 assertTrue(mockTreeSet.isEmpty());
 }

 @Test
 public void testEmptyLinkedHashSetAnswer() {
 Mockito.<LinkedHashSet>stub(LinkedHashSet.class).toReturnValues(returnsEmptyValues);
 LinkedHashSet<String> mockLinkedHashSet = Mockito.mock(LinkedHashSet.class);
 assertNotNull(mockLinkedHashSet);
 assertTrue(mockLinkedHashSet.isEmpty());
 }

 @Test
 public void testEmptyListAnswer() {
 Mockito.<List>stub(List.class).toReturnValues(returnsEmptyValues);
 List<String> mockList = Mockito.mock(List.class);
 assertNotNull(mockList);
 assertTrue(mockList.isEmpty());
 }

 @Test
 public void testEmptyLinkedListAnswer() {
 Mockito.<LinkedList>stub(LinkedList.class).toReturnValues(returnsEmptyValues);
 LinkedList<String> mockLinkedList = Mockito.mock(LinkedList.class);
 assertNotNull(mockLinkedList);
 assertTrue(mockLinkedList.isEmpty());
 }

 @Test
 public void testEmptyArrayListAnswer() {
 Mockito.<ArrayList>stub(ArrayList.class).toReturnValues(returnsEmptyValues);
 ArrayList<String> mockArrayList = Mockito.mock(ArrayList.class);
 assertNotNull(mockArrayList);
 assertTrue(mockArrayList.isEmpty());
 }

 @Test
 public void testEmptyMapAnswer() {
 Mockito.<Map>stub(Map.class).toReturnValues(returnsEmptyValues);
 Map<String, String> mockMap = Mockito.mock(Map.class);
 assertNotNull(mockMap);
 assertTrue(mockMap.isEmpty());
 }

 @Test
 public void testEmptyHashMapAnswer() {
 Mockito.<HashMap>stub(HashMap.class).toReturnValues(returnsEmptyValues);
 HashMap<String, String> mockHashMap = Mockito.mock(HashMap.class);
 assertNotNull(mockHashMap);
 assertTrue(mockHashMap.isEmpty());
 }

 @Test
 public void testEmptyTreeMapAnswer() {
 Mockito.<TreeMap>stub(TreeMap.class).toReturnValues(returnsEmptyValues);
 TreeMap<String, String> mockTreeMap = Mockito.mock(TreeMap.class);
 assertNotNull(mockTreeMap);
 assertTrue(mockTreeMap.isEmpty());
 }

 @Test
 public void testEmptyLinkedHashMapAnswer() {
 Mockito.<LinkedHashMap>stub(LinkedHashMap.class).toReturnValues(returnsEmptyValues);
 LinkedHashMap<String, String> mockLinkedHashMap = Mockito.mock(LinkedHashMap.class);
 assertNotNull(mockLinkedHashMap);
 assertTrue(mockLinkedHashMap.isEmpty());
 }
}