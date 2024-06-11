package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethod() {
        MockName mockName = mock(MockName.class);
        when(mockName.toString()).thenAnswer(Answers.callRealMethod());
        Object mock = mock(String.class, new Object[]{mockName});

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object answer = returnsEmptyValues.answer(new InvocationOnMock(mock, null, "toString()"));

        verify(mockName).toString();
        assertEquals("Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode(), answer);
    }

    @Test
    public void testCompareToMethod() {
        Object mock1 = mock(Comparable.class);
        Object mock2 = mock(Comparable.class);

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        when(mock1.compareTo(mock2)).thenAnswer(Answers.callRealMethod());

        int answer = (int) returnsEmptyValues.answer(new InvocationOnMock(mock1, new Object[]{mock2}));

        verify(mock1).compareTo(mock2);
        assertEquals(1, answer);
    }

    @Test
    public void testPrimitivesAndWrapperTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        byte byteValue = (byte) returnsEmptyValues.returnValueFor(byte.class);
        short shortValue = (short) returnsEmptyValues.returnValueFor(short.class);
        int intValue = (int) returnsEmptyValues.returnValueFor(int.class);
        long longValue = (long) returnsEmptyValues.returnValueFor(long.class);
        float floatValue = (float) returnsEmptyValues.returnValueFor(float.class);
        double doubleValue = (double) returnsEmptyValues.returnValueFor(double.class);
        boolean booleanValue = (boolean) returnsEmptyValues.returnValueFor(boolean.class);
        Character charValue = (Character) returnsEmptyValues.returnValueFor(Character.class);
        Integer integerValue = (Integer) returnsEmptyValues.returnValueFor(Integer.class);
        Long longWrapperValue = (Long) returnsEmptyValues.returnValueFor(Long.class);

        assertEquals(0, byteValue);
        assertEquals(0, shortValue);
        assertEquals(0, intValue);
        assertEquals(0L, longValue);
        assertEquals(0.0f, floatValue, 0.0f);
        assertEquals(0.0d, doubleValue, 0.0d);
        assertEquals(false, booleanValue);
        assertEquals(Character.MIN_VALUE, charValue);
        assertEquals(0, integerValue);
        assertEquals(0L, longWrapperValue);
    }

    @Test
    public void testEmptyCollectionTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Collection<Object> collection = (Collection<Object>) returnsEmptyValues.returnValueFor(Collection.class);
        Set<Object> set = (Set<Object>) returnsEmptyValues.returnValueFor(Set.class);
        LinkedHashSet<Object> linkedHashSet = (LinkedHashSet<Object>) returnsEmptyValues.returnValueFor(LinkedHashSet.class);
        HashSet<Object> hashSet = (HashSet<Object>) returnsEmptyValues.returnValueFor(HashSet.class);
        SortedSet<Object> sortedSet = (SortedSet<Object>) returnsEmptyValues.returnValueFor(SortedSet.class);
        TreeSet<Object> treeSet = (TreeSet<Object>) returnsEmptyValues.returnValueFor(TreeSet.class);
        LinkedHashSet<Object> linkedTreeSet = (LinkedHashSet<Object>) returnsEmptyValues.returnValueFor(LinkedTreeSet.class);
        List<Object> list = (List<Object>) returnsEmptyValues.returnValueFor(List.class);
        LinkedList<Object> linkedList = (LinkedList<Object>) returnsEmptyValues.returnValueFor(LinkedList.class);
        ArrayList<Object> arrayList = (ArrayList<Object>) returnsEmptyValues.returnValueFor(ArrayList.class);

        assertTrue(collection.isEmpty());
        assertTrue(set.isEmpty());
        assertTrue(linkedHashSet.isEmpty());
        assertTrue(hashSet.isEmpty());
        assertTrue(sortedSet.isEmpty());
        assertTrue(treeSet.isEmpty());
        assertTrue(linkedTreeSet.isEmpty());
        assertTrue(list.isEmpty());
        assertTrue(linkedList.isEmpty());
        assertTrue(arrayList.isEmpty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testModifyCollection() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        List<Object> list = (List<Object>) returnsEmptyValues.returnValueFor(List.class);

        list.add("Test");
    }
}