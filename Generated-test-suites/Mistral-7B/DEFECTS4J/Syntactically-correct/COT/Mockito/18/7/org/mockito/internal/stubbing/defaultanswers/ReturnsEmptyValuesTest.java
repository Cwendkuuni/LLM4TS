package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethod() {
        Object mock = mock(String.class);
        Answer answer = new ReturnsEmptyValues();

        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.setMethod(Mockito.method(String.class, "toString"));
        invocation.setMock(mock);

        String result = (String) answer.answer(invocation);

        String expected = "Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode();
        assertEquals(expected, result);
    }

    @Test
    public void testCompareToMethod() {
        Object mock = mock(String.class);
        Object anotherMock = mock(String.class);

        Answer answer = new ReturnsEmptyValues();

        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.setMethod(Mockito.method(String.class, "compareTo", ArgumentMatchers.any(String.class)));
        invocation.setArguments(new Object[]{anotherMock});
        invocation.setMock(mock);

        int result = (Integer) answer.answer(invocation);

        // Mockito by default should return 0 if references are the same, otherwise some other value because they are not the same.
        // Only for compareTo() method by the Comparable interface.
        // In this test case, we assume that references are not the same.
        int anyInt = 1;
        assertNotEquals(0, result);
        assertNotEquals(anyInt, result);
    }

    @Test
    public void testPrimitiveTypes() {
        Answer answer = new ReturnsEmptyValues();

        // Testing int type
        InvocationOnMock invocationInt = Mockito.mock(InvocationOnMock.class);
        invocationInt.setMethod(Mockito.method(int.class, "valueOf", ArgumentMatchors.anyInt()));

        int resultInt = (int) answer.answer(invocationInt);
        int expectedInt = 0;
        assertEquals(expectedInt, resultInt);

        // Testing long type
        InvocationOnMock invocationLong = Mockito.mock(InvocationOnMock.class);
        invocationLong.setMethod(Mockito.method(long.class, "valueOf", ArgumentMatchors.anyLong()));

        long resultLong = (long) answer.answer(invocationLong);
        long expectedLong = 0L;
        assertEquals(expectedLong, resultLong);

        // Testing char type
        InvocationOnMock invocationChar = Mockito.mock(InvocationOnMock.class);
        invocationChar.setMethod(Mockito.method(char.class, "valueOf", ArgumentMatchors.anyChar()));

        char resultChar = (char) answer.answer(invocationChar);
        char expectedChar = (char) 0;
        assertEquals(expectedChar, resultChar);

        // Testing boolean type
        InvocationOnMock invocationBoolean = Mockito.mock(InvocationOnMock.class);
        invocationBoolean.setMethod(Mockito.method(boolean.class, "valueOf", ArgumentMatchors.anyBoolean()));

        boolean resultBoolean = (boolean) answer.answer(invocationBoolean);
        boolean expectedBoolean = false;
        assertEquals(expectedBoolean, resultBoolean);
    }

    @Test(expected = AssertionError.class)
    public void testNullReturnType() {
        Answer answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.setMethod(Mockito.method(Null.class, "valueOf"));
        answer.answer(invocation);
    }

    @Test
    public void testEmptyCollection() {
        Answer answer = new ReturnsEmptyValues();

        // Testing Collection type
        InvocationOnMock invocationCollection = Mockito.mock(InvocationOnMock.class);
        invocationCollection.setMethod(Mockito.method(Collection.class, "newInstance"));

        Collection<String> resultCollection = (Collection<String>) answer.answer(invocationCollection);
        assertTrue(resultCollection.isEmpty());

        // Testing Set type
        InvocationOnMock invocationSet = Mockito.mock(InvocationOnMock.class);
        invocationSet.setMethod(Mockito.method(Set.class, "newInstance"));

        Set<String> resultSet = (Set<String>) answer.answer(invocationSet);
        assertTrue(resultSet.isEmpty());

        // Testing HashSet type
        InvocationOnMock invocationHashSet = Mockito.mock(InvocationOnMock.class);
        invocationHashSet.setMethod(Mockito.method(HashSet.class, "newInstance"));

        Set<String> resultHashSet = (Set<String>) answer.answer(invocationHashSet);
        assertTrue(resultHashSet.isEmpty());

        // Testing LinkedHashSet type
        InvocationOnMock invocationLinkedHashSet = Mockito.mock(InvocationOnMock.class);
        invocationLinkedHashSet.setMethod(Mockito.method(LinkedHashSet.class, "newInstance"));

        Set<String> resultLinkedHashSet = (Set<String>) answer.answer(invocationLinkedHashSet);
        assertTrue(resultLinkedHashSet.isEmpty());

        // Testing SortedSet type
        InvocationOnMock invocationSortedSet = Mockito.mock(InvocationOnMock.class);
        invocationSortedSet.setMethod(Mockito.method(SortedSet.class, "newInstance"));

        SortedSet<String> resultSortedSet = (SortedSet<String>) answer.answer(invocationSortedSet);
        assertTrue(resultSortedSet.isEmpty());

        // Testing TreeSet type
        InvocationOnMock invocationTreeSet = Mockito.mock(InvocationOnMock.class);
        invocationTreeSet.setMethod(Mockito.method(TreeSet.class, "newInstance"));

        SortedSet<String> resultTreeSet = (SortedSet<String>) answer.answer(invocationTreeSet);
        assertTrue(resultTreeSet.isEmpty());

        // Testing ArrayList type
        InvocationOnMock invocationArrayList = Mockito.mock(InvocationOnMock.class);
        invocationArrayList.setMethod(Mockito.method(ArrayList.class, "newInstance", int.class));

        List<String> resultArrayList = (List<String>) answer.answer(invocationArrayList);
        assertTrue(resultArrayList.isEmpty());

        // Testing LinkedList type
        InvocationOnMock invocationLinkedList = Mockito.mock(InvocationOnMock.class);
        invocationLinkedList.setMethod(Mockito.method(LinkedList.class, "newInstance"));

        List<String> resultLinkedList = (List<String>) answer.answer(invocationLinkedList);
        assertTrue(resultLinkedList.isEmpty());
    }

    @Test
    public void testEmptyMap() {
        Answer answer = new ReturnsEmptyValues();

        // Testing Map type
        InvocationOnMock invocationMap = Mockito.mock(InvocationOnMock.class);
        invocationMap.setMethod(Mockito.method(Map.class, "newInstance"));

        Map<String, String> resultMap = (Map<String, String>) answer.answer(invocationMap);
        assertTrue(resultMap.isEmpty());

        // Testing HashMap type
        InvocationOnMock invocationHashMap = Mockito.mock(InvocationOnMock.class);
        invocationHashMap.setMethod(Mockito.method(HashMap.class, "newInstance"));

        Map<String, String> resultHashMap = (Map<String, String>) answer.answer(invocationHashMap);
        assertTrue(resultHashMap.isEmpty());

        // Testing LinkedHashMap type
        InvocationOnMock invocationLinkedHashMap = Mockito.mock(InvocationOnMock.class);
        invocationLinkedHashMap.setMethod(Mockito.method(LinkedHashMap.class, "newInstance"));

        Map<String, String> resultLinkedHashMap = (Map<String, String>) answer.answer(invocationLinkedHashMap);
        assertTrue(resultLinkedHashMap.isEmpty());

        // Testing TreeMap type
        InvocationOnMock invocationTreeMap = Mockito.mock(InvocationOnMock.class);
        invocationTreeMap.setMethod(Mockito.method(TreeMap.class, "newInstance"));

        SortedMap<String, String> resultTreeMap = (SortedMap<String, String>) answer.answer(invocationTreeMap);
        assertTrue(resultTreeMap.isEmpty());
    }

    @Test
    public void testToStringWithDefaultMockName() {
        Object mock = mock(Object.class);
        Answer answer = new ReturnsEmptyValues();

        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.setMethod(Mockito.method(Object.class, "toString"));
        invocation.setMock(mock);

        String result = (String) answer.answer(invocation);

        String expected = "Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode();
        assertEquals(expected, result);
    }

    @Test
    public void testToStringWithCustomMockName() {
        Object mock = mock(Object.class, "CustomMockName");
        Answer answer = new ReturnsEmptyValues();

        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.setMethod(Mockito.method(Object.class, "toString"));
        invocation.setMock(mock);

        String result = (String) answer.answer(invocation);

        String expected = "CustomMockName";
        assertEquals(expected, result);
    }
}