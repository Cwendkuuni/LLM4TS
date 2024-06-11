package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringAnswer() {
        //given
        Object mock = mock(String.class);
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        //when
        String answer = (String) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(String.class, "toString()")));

        //then
        assertEquals("Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode(), answer);
    }

    @Test
    public void testCompareToMethodAnswer() {
        //given
        Object obj1 = mock(Comparable.class);
        Object obj2 = mock(Comparable.class);

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        //when
        int answer = (Integer) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(Comparable.class, "compareTo", Object.class).withArguments(obj1)));

        //then
        assertEquals(1, answer);
    }

    @Test
    public void testPrimitiveTypes() {
        //given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        //when
        byte answerByte = (Byte) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(Byte.TYPE, "valueOf", byte.class)));
        short answerShort = (Short) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(Short.TYPE, "valueOf", short.class)));
        int answerInt = (Integer) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(Integer.TYPE, "valueOf", int.class)));
        long answerLong = (Long) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(Long.TYPE, "valueOf", long.class)));
        float answerFloat = (Float) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(Float.TYPE, "valueOf", float.class)));
        double answerDouble = (Double) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(Double.TYPE, "valueOf", double.class)));
        char answerChar = (Character) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(Character.TYPE, "valueOf", char.class)));
        boolean answerBoolean = (Boolean) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(Boolean.TYPE, "valueOf", boolean.class)));

        //then
        assertEquals(Byte.MIN_VALUE, answerByte);
        assertEquals(Short.MIN_VALUE, answerShort);
        assertEquals(0, answerInt);
        assertEquals(0L, answerLong);
        assertEquals(0.0f, answerFloat, 0.01f);
        assertEquals(0.0d, answerDouble, 0.01d);
        assertEquals((char) 0, answerChar);
        assertEquals(false, answerBoolean);
    }

    @Test
    public void testEmptyCollections() {
        //given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        //when
        Collection<String> answerCollection = (Collection<String>) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(Collection.class, "newInstance")));
        Set<Integer> answerSet = (Set<Integer>) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(Set.class, "newInstance")));
        HashSet<String> answerHashSet = (HashSet<String>) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(HashSet.class, "newInstance")));
        SortedSet<Integer> answerSortedSet = (SortedSet<Integer>) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(SortedSet.class, "newInstance")));
        TreeSet<String> answerTreeSet = (TreeSet<String>) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(TreeSet.class, "newInstance")));
        LinkedHashSet<Integer> answerLinkedHashSet = (LinkedHashSet<Integer>) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(LinkedHashSet.class, "newInstance")));
        List<String> answerList = (List<String>) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(List.class, "newInstance", int.class)));
        LinkedList<Integer> answerLinkedList = (LinkedList<Integer>) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(LinkedList.class, "newInstance")));
        ArrayList<String> answerArrayList = (ArrayList<String>) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(ArrayList.class, "newInstance", int.class)));

        //then
        assertNotNull(answerCollection);
        assertEquals(0, answerCollection.size());
        verifyNoInteractions(answerCollection);

        assertNotNull(answerSet);
        assertEquals(0, answerSet.size());
        verifyNoInteractions(answerSet);

        assertNotNull(answerHashSet);
        assertEquals(0, answerHashSet.size());
        verifyNoInteractions(answerHashSet);

        assertNotNull(answerSortedSet);
        assertEquals(0, answerSortedSet.size());
        verifyNoInteractions(answerSortedSet);

        assertNotNull(answerTreeSet);
        assertEquals(0, answerTreeSet.size());
        verifyNoInteractions(answerTreeSet);

        assertNotNull(answerLinkedHashSet);
        assertEquals(0, answerLinkedHashSet.size());
        verifyNoInteractions(answerLinkedHashSet);

        assertNotNull(answerList);
        assertEquals(0, answerList.size());
        verifyNoInteractions(answerList);

        assertNotNull(answerLinkedList);
        assertEquals(0, answerLinkedList.size());
        verifyNoInteractions(answerLinkedList);

        assertNotNull(answerArrayList);
        assertEquals(0, answerArrayList.size());
        verifyNoInteractions(answerArrayList);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testEmptyCollectionsModification() {
        //given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        //when
        Collection<String> answerCollection = (Collection<String>) returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(Collection.class, "newInstance")));
        answerCollection.add("test");

        //then
        //this test case should fail with UnsupportedOperationException because the returned collection is not modifiable
    }

    @Test
    public void testNullAnswer() {
        //given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        //when
        Object answer = returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(Object.class, "valueOf", Class.class)));

        //then
        assertNull(answer);
    }
}