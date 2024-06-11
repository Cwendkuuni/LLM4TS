package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.junit.runner.RunWith;
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
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.JUnit4.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void test_toString_method() {
        Object mock = mock(String.class);
        Answer answer = new ReturnsEmptyValues();
        when(mock.toString()).thenAnswer(answer);

        String expected = "Mock for " + String.class.getSimpleName() + ", hashCode: 0";
        String result = mock.toString();
        verify(mock).toString();
        assert (expected.equals(result));
    }

    @Test
    public void test_compareTo_method() {
        Object mock1 = mock(Comparable.class);
        Object mock2 = mock(Comparable.class);
        Answer answer = new ReturnsEmptyValues();

        when(mock1.compareTo(ArgumentMatchers.any())).thenAnswer(answer);
        when(mock2.compareTo(mock1)).thenReturn(1);

        int result = mock1.compareTo(mock2);
        verify(mock1).compareTo(mock2);
        assert (result == 1);
    }

    @Test
    public void test_primitive_types() {
        Answer answer = new ReturnsEmptyValues();

        byte expectedByte = 0;
        byte resultByte = (byte) answer.answer(Mockito.mock(Byte.class).getInvocationOnMock());
        assert (expectedByte == resultByte);

        short expectedShort = 0;
        short resultShort = (short) answer.answer(Mockito.mock(Short.class).getInvocationOnMock());
        assert (expectedShort == resultShort);

        int expectedInt = 0;
        int resultInt = (int) answer.answer(Mockito.mock(Integer.class).getInvocationOnMock());
        assert (expectedInt == resultInt);

        long expectedLong = 0L;
        long resultLong = (long) answer.answer(Mockito.mock(Long.class).getInvocationOnMock());
        assert (expectedLong == resultLong);

        float expectedFloat = 0F;
        float resultFloat = (float) answer.answer(Mockito.mock(Float.class).getInvocationOnMock());
        assert (expectedFloat == resultFloat);

        double expectedDouble = 0D;
        double resultDouble = (double) answer.answer(Mockito.mock(Double.class).getInvocationOnMock());
        assert (expectedDouble == resultDouble);

        char expectedChar = (char) 0;
        char resultChar = (char) answer.answer(Mockito.mock(Character.class).getInvocationOnMock());
        assert (expectedChar == resultChar);

        boolean expectedBoolean = false;
        boolean resultBoolean = (boolean) answer.answer(Mockito.mock(Boolean.class).getInvocationOnMock());
        assert (expectedBoolean == resultBoolean);
    }

    @Test(expected = NullPointerException.class)
    public void test_null_pointer_exception() {
        Answer answer = new ReturnsEmptyValues();
        answer.answer(Mockito.mock(NullPointerException.class).getInvocationOnMock());
    }

    @Test
    public void test_empty_collection_types() {
        Answer answer = new ReturnsEmptyValues();

        Collection<Object> expectedCollection = new ArrayList<>();
        Collection<Object> resultCollection = (Collection<Object>) answer.answer(Mockito.mock(Collection.class).getInvocationOnMock());
        assert (expectedCollection.isEmpty() && resultCollection.isEmpty());

        Set<Object> expectedSet = new HashSet<>();
        Set<Object> resultSet = (Set<Object>) answer.answer(Mockito.mock(Set.class).getInvocationOnMock());
        assert (expectedSet.isEmpty() && resultSet.isEmpty());

        HashSet<Object> expectedHashSet = new HashSet<>();
        HashSet<Object> resultHashSet = (HashSet<Object>) answer.answer(Mockito.mock(HashSet.class).getInvocationOnMock());
        assert (expectedHashSet.isEmpty() && resultHashSet.isEmpty());

        SortedSet<Object> expectedTreeSet = new TreeSet<>();
        SortedSet<Object> resultTreeSet = (SortedSet<Object>) answer.answer(Mockito.mock(SortedSet.class).getInvocationOnMock());
        assert (expectedTreeSet.isEmpty() && resultTreeSet.isEmpty());

        TreeSet<Object> expectedTreeHashSet = new TreeSet<>();
        TreeSet<Object> resultTreeHashSet = (TreeSet<Object>) answer.answer(Mockito.mock(TreeSet.class).getInvocationOnMock());
        assert (expectedTreeHashSet.isEmpty() && resultTreeHashSet.isEmpty());

        LinkedHashSet<Object> expectedLinkedHashSet = new LinkedHashSet<>();
        LinkedHashSet<Object> resultLinkedHashSet = (LinkedHashSet<Object>) answer.answer(Mockito.mock(LinkedHashSet.class).getInvocationOnMock());
        assert (expectedLinkedHashSet.isEmpty() && resultLinkedHashSet.isEmpty());

        List<Object> expectedList = new ArrayList<>();
        List<Object> resultList = (List<Object>) answer.answer(Mockito.mock(List.class).getInvocationOnMock());
        assert (expectedList.isEmpty() && resultList.isEmpty());

        LinkedList<Object> expectedLinkedList = new LinkedList<>();
        LinkedList<Object> resultLinkedList = (LinkedList<Object>) answer.answer(Mockito.mock(LinkedList.class).getInvocationOnMock());
        assert (expectedLinkedList.isEmpty() && resultLinkedList.isEmpty());

        ArrayList<Object> expectedArrayList = new ArrayList<>();
        ArrayList<Object> resultArrayList = (ArrayList<Object>) answer.answer(Mockito.mock(ArrayList.class).getInvocationOnMock());
        assert (expectedArrayList.isEmpty() && resultArrayList.isEmpty());

        Map<Object, Object> expectedMap = new HashMap<>();
        Map<Object, Object> resultMap = (Map<Object, Object>) answer.answer(Mockito.mock(Map.class).getInvocationOnMock());
        assert (expectedMap.isEmpty() && resultMap.isEmpty());

        HashMap<Object, Object> expectedHashMap = new HashMap<>();
        HashMap<Object, Object> resultHashMap = (HashMap<Object, Object>) answer.answer(Mockito.mock(HashMap.class).getInvocationOnMock());
        assert (expectedHashMap.isEmpty() && resultHashMap.isEmpty());

        SortedMap<Object, Object> expectedTreeMap = new TreeMap<>();
        SortedMap<Object, Object> resultTreeMap = (SortedMap<Object, Object>) answer.answer(Mockito.mock(SortedMap.class).getInvocationOnMock());
        assert (expectedTreeMap.isEmpty() && resultTreeMap.isEmpty());

        TreeMap<Object, Object> expectedTreeHashMap = new TreeMap<>();
        TreeMap<Object, Object> resultTreeHashMap = (TreeMap<Object, Object>) answer.answer(Mockito.mock(TreeMap.class).getInvocationOnMock());
        assert (expectedTreeHashMap.isEmpty() && resultTreeHashMap.isEmpty());

        LinkedHashMap<Object, Object> expectedLinkedHashMap = new LinkedHashMap<>();
        LinkedHashMap<Object, Object> resultLinkedHashMap = (LinkedHashMap<Object, Object>) answer.answer(Mockito.mock(LinkedHashMap.class).getInvocationOnMock());
        assert (expectedLinkedHashMap.isEmpty() && resultLinkedHashMap.isEmpty());
    }
}