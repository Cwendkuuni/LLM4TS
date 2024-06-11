package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.Primitives.isPrimitiveOrWrapper;
import static org.mockito.internal.util.Primitives.defaultValueForPrimitiveOrWrapper;

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
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerToStringMethod() {
        Object mock = Mockito.mock(String.class);
        ReturnsEmptyValues answer = Mockito.mock(ReturnsEmptyValues.class);
        InvocationOnMock invocation = Mockito.mockInvocation(answer, Mockito.method(String.class, "toString"));

        Mockito.when(answer.answer(invocation)).thenReturn(answer);
        Mockito.when(mock.toString()).thenReturn("mockito-test");

        String result = (String) answer.answer(invocation);
        assertEquals("Mock for " + String.class.getSimpleName() + ", hashCode: " + mock.hashCode(), result);
    }

    @Test
    public void testAnswerCompareToMethod() {
        Object mock1 = Mockito.mock(Comparable.class);
        Object mock2 = Mockito.mock(Comparable.class);
        ReturnsEmptyValues answer = Mockito.mock(ReturnsEmptyValues.class);
        InvocationOnMock invocation = Mockito.mockInvocation(answer, Mockito.method(Comparable.class, "compareTo", Object.class));

        Mockito.when(answer.answer(invocation)).thenReturn(answer);
        Mockito.when(mock1.compareTo(mock2)).thenReturn(0);

        int result = (Integer) answer.answer(invocation);
        assertEquals(0, result);

        // assert that the compareTo method is called once with the correct arguments
        Mockito.verify(mock1).compareTo(mock2);
    }

    @Test
    public void testReturnValueForPrimitives() {
        ReturnsEmptyValues answer = Mockito.mock(ReturnsEmptyValues.class);

        byte result1 = (Byte) answer.returnValueFor(Byte.TYPE);
        assertEquals((byte) 0, result1);

        short result2 = (Short) answer.returnValueFor(Short.TYPE);
        assertEquals((short) 0, result2);

        int result3 = (Integer) answer.returnValueFor(Integer.TYPE);
        assertEquals(0, result3);

        long result4 = (Long) answer.returnValueFor(Long.TYPE);
        assertEquals(0L, result4);

        float result5 = (Float) answer.returnValueFor(Float.TYPE);
        assertEquals((float) 0, result5);

        double result6 = (Double) answer.returnValueFor(Double.TYPE);
        assertEquals(0.0, result6);

        char result7 = (Character) answer.returnValueFor(Character.TYPE);
        assertEquals((char) 0, result7);

        boolean result8 = (Boolean) answer.returnValueFor(Boolean.TYPE);
        assertEquals(false, result8);

        byte result9 = (Byte) answer.returnValueFor(Byte.class);
        assertEquals((byte) 0, result9);

        short result10 = (Short) answer.returnValueFor(Short.class);
        assertEquals((short) 0, result10);

        int result11 = (Integer) answer.returnValueFor(Integer.class);
        assertEquals(0, result11);

        long result12 = (Long) answer.returnValueFor(Long.class);
        assertEquals(0L, result12);

        float result13 = (Float) answer.returnValueFor(Float.class);
        assertEquals((float) 0, result13);

        double result14 = (Double) answer.returnValueFor(Double.class);
        assertEquals(0.0, result14);

        char result15 = (Character) answer.returnValueFor(Character.class);
        assertEquals((char) 0, result15);

        boolean result16 = (Boolean) answer.returnValueFor(Boolean.class);
        assertEquals(false, result16);

        // test wrapper classes
        Byte result17 = (Byte) answer.returnValueFor(Byte.class);
        assertEquals((byte) 0, result17.byteValue());

        Short result18 = (Short) answer.returnValueFor(Short.class);
        assertEquals((short) 0, result18.shortValue());

        Integer result19 = (Integer) answer.returnValueFor(Integer.class);
        assertEquals(0, result19.intValue());

        Long result20 = (Long) answer.returnValueFor(Long.class);
        assertEquals(0L, result20.longValue());

        Float result21 = (Float) answer.returnValueFor(Float.class);
        assertEquals((float) 0, result21.floatValue());

        Double result22 = (Double) answer.returnValueFor(Double.class);
        assertEquals(0.0, result22.doubleValue());

        Character result23 = (Character) answer.returnValueFor(Character.class);
        assertEquals((char) 0, result23);

        Boolean result24 = (Boolean) answer.returnValueFor(Boolean.class);
        assertEquals(false, result24);
    }

    @Test
    public void testReturnValueForCollection() {
        ReturnsEmptyValues answer = Mockito.mock(ReturnsEmptyValues.class);

        Collection<String> result = (Collection<String>) answer.returnValueFor(Collection.class);
        assertNotNull(result);
        assertEquals(0, result.size());

        List<String> resultList = (List<String>) answer.returnValueFor(List.class);
        assertNotNull(resultList);
        assertEquals(0, resultList.size());

        Set<String> resultSet = (Set<String>) answer.returnValueFor(Set.class);
        assertNotNull(resultSet);
        assertEquals(0, resultSet.size());

        HashSet<String> resultHashSet = (HashSet<String>) answer.returnValueFor(HashSet.class);
        assertNotNull(resultHashSet);
        assertEquals(0, resultHashSet.size());

        LinkedHashSet<String> resultLinkedHashSet = (LinkedHashSet<String>) answer.returnValueFor(LinkedHashSet.class);
        assertNotNull(resultLinkedHashSet);
        assertEquals(0, resultLinkedHashSet.size());

        ArrayList<String> resultArrayList = (ArrayList<String>) answer.returnValueFor(ArrayList.class);
        assertNotNull(resultArrayList);
        assertEquals(0, resultArrayList.size());

        LinkedList<String> resultLinkedList = (LinkedList<String>) answer.returnValueFor(LinkedList.class);
        assertNotNull(resultLinkedList);
        assertEquals(0, resultLinkedList.size());
    }

    @Test
    public void testReturnValueForMap() {
        ReturnsEmptyValues answer = Mockito.mock(ReturnsEmptyValues.class);

        Map<String, String> result = (Map<String, String>) answer.returnValueFor(Map.class);
        assertNotNull(result);
        assertEquals(0, result.size());

        HashMap<String, String> resultHashMap = (HashMap<String, String>) answer.returnValueFor(HashMap.class);
        assertNotNull(resultHashMap);
        assertEquals(0, resultHashMap.size());

        LinkedHashMap<String, String> resultLinkedHashMap = (LinkedHashMap<String, String>) answer.returnValueFor(LinkedHashMap.class);
        assertNotNull(resultLinkedHashMap);
        assertEquals(0, resultLinkedHashMap.size());

        TreeMap<String, String> resultTreeMap = (TreeMap<String, String>) answer.returnValueFor(TreeMap.class);
        assertNotNull(resultTreeMap);
        assertEquals(0, resultTreeMap.size());

        TreeSet<String> resultTreeSet = (TreeSet<String>) answer.returnValueFor(TreeSet.class);
        assertNotNull(resultTreeSet);
        assertEquals(0, resultTreeSet.size());
    }
}