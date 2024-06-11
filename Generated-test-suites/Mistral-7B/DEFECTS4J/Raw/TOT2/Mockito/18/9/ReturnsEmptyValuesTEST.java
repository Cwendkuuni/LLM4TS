package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ReturnsEmptyValuesTest {
 private ReturnsEmptyValues returnsEmptyValues;

 @Before
 public void setup() {
 returnsEmptyValues = new ReturnsEmptyValues();
 }

 @Test
 public void testAnswerMethod() {
 InvocationOnMock invocationMock = Mockito.mock(InvocationOnMock.class);
 Object result = returnsEmptyValues.answer(invocationMock);

 assertEquals("Mock for org.mockito.invocation.InvocationOnMock, hashCode: 123456", result.toString());

 Mockito.when(invocationMock.getMock()).thenReturn(new Object());
 Mockito.when(invocationMock.getArguments()[0]).thenReturn(new Object());
 assertEquals(1, returnsEmptyValues.answer(invocationMock).compareTo(invocationMock.getArguments()[0]));
 }

 @Test
 public void testReturnValueForMethod() {
 Class<?> types[] = { Integer.TYPE, Integer.class, Collection.class, Set.class, HashSet.class, SortedSet.class, TreeSet.class, LinkedHashSet.class, List.class, LinkedList.class, ArrayList.class, Map.class, HashMap.class, SortedMap.class, TreeMap.class, LinkedHashMap.class };
 Object result = null;

 for (Class<?> type : types) {
 result = returnsEmptyValues.returnValueFor(type);

 if (type == Collection.class || type == Set.class || type == HashSet.class || type == SortedSet.class || type == TreeSet.class || type == LinkedHashSet.class || type == List.class || type == LinkedList.class || type == ArrayList.class) {
 Assert.assertNotNull(result);
 assertTrue(result instanceof Collection);
 } else if (type == Map.class || type == HashMap.class || type == SortedMap.class || type == TreeMap.class || type == LinkedHashMap.class) {
 Assert.assertNotNull(result);
 assertTrue(result instanceof Map);
 } else {
 Assert.assertNull(result);
 }
 }
 }

 @Test
 public void testNullInvocation() {
 try {
 returnsEmptyValues.answer(null);
 } catch (NullPointerException e) {
 // Expected NullPointerException
 }
 }

 @Test
 public void testCompareToMethodWithNullComparator() {
 try {
 returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class)).compareTo(null);
 } catch (NullPointerException e) {
 // Expected NullPointerException
 }
 }
}