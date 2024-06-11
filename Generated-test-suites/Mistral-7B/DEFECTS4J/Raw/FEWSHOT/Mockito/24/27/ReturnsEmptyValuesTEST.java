package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer_toStringMethod() {
        Object mock = mock(String.class);
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock.mockingDetails().getInvocationCalls().get(0);

        String result = (String) answer.answer(invocation);

        assertTrue(result.contains("Mock for"));
    }

    @Test
    public void testAnswer_compareToMethod() {
        Object mock1 = mock(Comparable.class);
        Object mock2 = mock(Comparable.class);

        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        InvocationOnMock invocation1 = mock1.mockingDetails().getInvocationCalls().get(0);
        InvocationOnMock invocation2 = mock2.mockingDetails().getInvocationCalls().get(0);

        int result1 = (Integer) answer.answer(invocation1);
        int result2 = (Integer) answer.answer(invocation2);

        assertNotSame(result1, result2);
        assertTrue(result1 > 0);
    }

    @Test
    public void testReturnValueFor_primitiveTypes() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        byte resultByte = (byte) answer.returnValueFor(byte.class);
        short resultShort = (short) answer.returnValueFor(short.class);
        int resultInt = (int) answer.returnValueFor(int.class);
        long resultLong = (long) answer.returnValueFor(long.class);
        float resultFloat = (float) answer.returnValueFor(float.class);
        double resultDouble = (double) answer.returnValueFor(double.class);
        char resultChar = (char) answer.returnValueFor(char.class);
        boolean resultBoolean = (boolean) answer.returnValueFor(boolean.class);

        assertEquals(0, resultByte);
        assertEquals(0, resultShort);
        assertEquals(0, resultInt);
        assertEquals(0L, resultLong);
        assertEquals(0.0f, resultFloat, 0.001f);
        assertEquals(0.0d, resultDouble, 0.001d);
        assertEquals((char) 0, resultChar);
        assertEquals(false, resultBoolean);
    }

    @Test
    public void testReturnValueFor_Collections() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        Collection<Object> resultCollection = (Collection<Object>) answer.returnValueFor(Collection.class);
        Set<Object> resultSet = (Set<Object>) answer.returnValueFor(Set.class);
        HashSet<Object> resultHashSet = (HashSet<Object>) answer.returnValueFor(HashSet.class);
        SortedSet<Object> resultSortedSet = (SortedSet<Object>) answer.returnValueFor(SortedSet.class);
        TreeSet<Object> resultTreeSet = (TreeSet<Object>) answer.returnValueFor(TreeSet.class);
        LinkedHashSet<Object> resultLinkedHashSet = (LinkedHashSet<Object>) answer.returnValueFor(LinkedHashSet.class);
        List<Object> resultList = (List<Object>) answer.returnValueFor(List.class);
        LinkedList<Object> resultLinkedList = (LinkedList<Object>) answer.returnValueFor(LinkedList.class);
        ArrayList<Object> resultArrayList = (ArrayList<Object>) answer.returnValueFor(ArrayList.class);

        assertTrue(resultCollection.isEmpty());
        assertTrue(resultSet.isEmpty());
        assertTrue(resultHashSet.isEmpty());
        assertTrue(resultSortedSet.isEmpty());
        assertTrue(resultTreeSet.isEmpty());
        assertTrue(resultLinkedHashSet.isEmpty());
        assertTrue(resultList.isEmpty());
        assertTrue(resultLinkedList.isEmpty());
        assertTrue(resultArrayList.isEmpty());
    }

    @Test
    public void testReturnValueFor_otherTypes() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        Map<Object, Object> resultMap = (Map<Object, Object>) answer.returnValueFor(Map.class);
        HashMap<Object, Object> resultHashMap = (HashMap<Object, Object>) answer.returnValueFor(HashMap.class);
        SortedMap<Object, Object> resultSortedMap = (SortedMap<Object, Object>) answer.returnValueFor(SortedMap.class);
        TreeMap<Object, Object> resultTreeMap = (TreeMap<Object, Object>) answer.returnValueFor(TreeMap.class);
        LinkedHashMap<Object, Object> resultLinkedHashMap = (LinkedHashMap<Object, Object>) answer.returnValueFor(LinkedHashMap.class);

        assertTrue(resultMap.isEmpty());
        assertTrue(resultHashMap.isEmpty());
        assertTrue(resultSortedMap.isEmpty());
        assertTrue(resultTreeMap.isEmpty());
        assertTrue(resultLinkedHashMap.isEmpty());
    }
}