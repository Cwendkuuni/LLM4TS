import org.junit.Assert;
import org.junit.Test;
import org.mockito.Answer;
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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerToStringMethod() {
        Object mock = mock(String.class);
        Answer<Object> answer = new ReturnsEmptyValues();
        when(mock.toString()).then(answer);

        String result = mock.toString();
        Assert.assertEquals("Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode(), result);
        verify(mock).toString();
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testAnswerCompareToMethod() {
        Object mock = mock(Comparable.class);
        Answer<Object> answer = new ReturnsEmptyValues();
        when(mock.compareTo(mock)).then(answer);

        int result = mock.compareTo(mock);
        Assert.assertEquals(0, result);
        verify(mock).compareTo(mock);
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testAnswerPrimitiveTypes() {
        Answer<Object> answer = new ReturnsEmptyValues();

        byte resultByte = (byte) answer.answer(new InvocationOnMock(ReturnsEmptyValues.class, "byteValue", new Object[0]));
        short resultShort = (short) answer.answer(new InvocationOnMock(ReturnsEmptyValues.class, "shortValue", new Object[0]));
        int resultInt = (int) answer.answer(new InvocationOnMock(ReturnsEmptyValues.class, "intValue", new Object[0]));
        long resultLong = (long) answer.answer(new InvocationOnMock(ReturnsEmptyValues.class, "longValue", new Object[0]));
        float resultFloat = (float) answer.answer(new InvocationOnMock(ReturnsEmptyValues.class, "floatValue", new Object[0]));
        double resultDouble = (double) answer.answer(new InvocationOnMock(ReturnsEmptyValues.class, "doubleValue", new Object[0]));
        boolean resultBoolean = (boolean) answer.answer(new InvocationOnMock(ReturnsEmptyValues.class, "booleanValue", new Object[0]));
        char resultChar = (char) answer.answer(new InvocationOnMock(ReturnsEmptyValues.class, "charValue", new Object[0]));

        Assert.assertEquals(0, resultByte);
        Assert.assertEquals(0, resultShort);
        Assert.assertEquals(0, resultInt);
        Assert.assertEquals(0L, resultLong);
        Assert.assertEquals(0.0f, resultFloat, 0.001f);
        Assert.assertEquals(0.0d, resultDouble, 0.001d);
        Assert.assertEquals(false, resultBoolean);
        Assert.assertEquals((char) 0, resultChar);
    }

    @Test
    public void testAnswerEmptyCollection() {
        Answer<Object> answer = new ReturnsEmptyValues();

        Collection<String> resultCollection = (Collection<String>) answer.answer(new InvocationOnMock(ReturnsEmptyValues.class, "collectionValue", new Object[0]));
        Assert.assertTrue(resultCollection.isEmpty());

        resultCollection.add("Test");
        Assert.assertEquals(1, resultCollection.size());
        verify(answer).answer(new InvocationOnMock(Collection.class, "add", new Object[]{resultCollection, "Test"}));
    }

    @Test
    public void testAnswerEmptySet() {
        Answer<Object> answer = new ReturnsEmptyValues();

        Set<String> resultSet = (Set<String>) answer.answer(new InvocationOnMock(ReturnsEmptyValues.class, "setValue", new Object[0]));
        Assert.assertTrue(resultSet.isEmpty());

        resultSet.add("Test");
        Assert.assertEquals(1, resultSet.size());
        verify(answer).answer(new InvocationOnMock(Set.class, "add", new Object[]{resultSet, "Test"}));
    }

    @Test
    public void testAnswerEmptyHashSet() {
        Answer<Object> answer = new ReturnsEmptyValues();

        Set<String> resultHashSet = (Set<String>) answer.answer(new InvocationOnMock(ReturnsEmptyValues.class, "hashSetValue", new Object[0]));
        Assert.assertTrue(resultHashSet instanceof HashSet);
        Assert.assertTrue(resultHashSet.isEmpty());

        resultHashSet.add("Test");
        Assert.assertEquals(1, resultHashSet.size());
        verify(answer).answer(new InvocationOnMock(HashSet.class, "add", new Object[]{resultHashSet, "Test"}));
    }

    @Test
    public void testAnswerEmptyTreeSet() {
        Answer<Object> answer = new ReturnsEmptyValues();

        SortedSet<String> resultTreeSet = (SortedSet<String>) answer.answer(new InvocationOnMock(ReturnsEmptyValues.class, "treeSetValue", new Object[0]));
        Assert.assertTrue(resultTreeSet.isEmpty());

        resultTreeSet.add("Test");
        Assert.assertEquals(1, resultTreeSet.size());
        verify(answer).answer(new InvocationOnMock(TreeSet.class, "add", new Object[]{resultTreeSet, "Test"}));
    }

    @Test
    public void testAnswerEmptyLinkedHashSet() {
        Answer<Object> answer = new ReturnsEmptyValues();

        SortedSet<String> resultLinkedHashSet = (SortedSet<String>) answer.answer(new InvocationOnMock(ReturnsEmptyValues.class, "linkedHashSetValue", new Object[0]));
        Assert.assertTrue(resultLinkedHashSet instanceof LinkedHashSet);
        Assert.assertTrue(resultLinkedHashSet.isEmpty());

        resultLinkedHashSet.add("Test");
        Assert.assertEquals(1, resultLinkedHashSet.size());
        verify(answer).answer(new InvocationOnMock(LinkedHashSet.class, "add", new Object[]{resultLinkedHashSet, "Test"}));
    }

    @Test
    public void testAnswerEmptyList() {
        Answer<Object> answer = new ReturnsEmptyValues();

        List<String> resultList = (List<String>) answer.answer(new InvocationOnMock(ReturnsEmptyValues.class, "listValue", new Object[0]));
        Assert.assertTrue(resultList.isEmpty());

        resultList.add("Test");
        Assert.assertEquals(1, resultList.size());
        verify(answer).answer(new InvocationOnMock(List.class, "add", new Object[]{resultList, "Test"}));
    }

    @Test
    public void testAnswerEmptyLinkedList() {
        Answer<Object> answer = new ReturnsEmptyValues();

        List<String> resultLinkedList = (List<String>) answer.answer(new InvocationOnMock(ReturnsEmptyValues.class, "linkedListValue", new Object[0]));
        Assert.assertTrue(resultLinkedList instanceof LinkedList);
        Assert.assertTrue(resultLinkedList.isEmpty());

        resultLinkedList.add("Test");
        Assert.assertEquals(1, resultLinkedList.size());
        verify(answer).answer(new InvocationOnMock(LinkedList.class, "add", new Object[]{resultLinkedList, "Test"}));
    }

    @Test
    public void testAnswerEmptyArrayList() {
        Answer<Object> answer = new ReturnsEmptyValues();

        List<String> resultArrayList = (List<String>) answer.answer(new InvocationOnMock(ReturnsEmptyValues.class, "arrayListValue", new Object[0]));
        Assert.assertTrue(resultArrayList instanceof ArrayList);
        Assert.assertTrue(resultArrayList.isEmpty());

        resultArrayList.add("Test");
        Assert.assertEquals(1, resultArrayList.size());
        verify(answer).answer(new InvocationOnMock(ArrayList.class, "add", new Object[]{resultArrayList, "Test"}));
    }

    @Test
    public void testAnswerEmptyMap() {
        Answer<Object> answer = new ReturnsEmptyValues();

        Map<String, String> resultMap = (Map<String, String>) answer.answer(new InvocationOnMock(ReturnsEmptyValues.class, "mapValue", new Object[0]));
        Assert.assertTrue(resultMap.isEmpty());

        resultMap.put("Key", "Test");
        Assert.assertEquals(1, resultMap.size());
        verify(answer).answer(new InvocationOnMock(Map.class, "put", new Object[]{resultMap, "Key", "Test"}));
    }

    @Test
    public void testAnswerEmptyHashMap() {
        Answer<Object> answer = new ReturnsEmptyValues();

        Map<String, String> resultHashMap = (Map<String, String>) answer.answer(new InvocationOnMock(ReturnsEmptyValues.class, "hashMapValue", new Object[0]));
        Assert.assertTrue(resultHashMap instanceof HashMap);
        Assert.assertTrue(resultHashMap.isEmpty());

        resultHashMap.put("Key", "Test");
        Assert.assertEquals(1, resultHashMap.size());
        verify(answer).answer(new InvocationOnMock(HashMap.class, "put", new Object[]{resultHashMap, "Key", "Test"}));
    }

    @Test
    public void testAnswerEmptyTreeMap() {
        Answer<Object> answer = new ReturnsEmptyValues();

        SortedMap<String, String> resultTreeMap = (SortedMap<String, String>) answer.answer(new InvocationOnMock(ReturnsEmptyValues.class, "treeMapValue", new Object[0]));
        Assert.assertTrue(resultTreeMap.isEmpty());

        resultTreeMap.put("Key", "Test");
        Assert.assertEquals(1, resultTreeMap.size());
        verify(answer).answer(new InvocationOnMock(TreeMap.class, "put", new Object[]{resultTreeMap, "Key", "Test"}));
    }

    @Test
    public void testAnswerEmptyLinkedHashMap() {
        Answer<Object> answer = new ReturnsEmptyValues();

        Map<String, String> resultLinkedHashMap = (Map<String, String>) answer.answer(new InvocationOnMock(ReturnsEmptyValues.class, "linkedHashMapValue", new Object[0]));
        Assert.assertTrue(resultLinkedHashMap instanceof LinkedHashMap);
        Assert.assertTrue(resultLinkedHashMap.isEmpty());

        resultLinkedHashMap.put("Key", "Test");
        Assert.assertEquals(1, resultLinkedHashMap.size());
        verify(answer).answer(new InvocationOnMock(LinkedHashMap.class, "put", new Object[]{resultLinkedHashMap, "Key", "Test"}));
    }
}