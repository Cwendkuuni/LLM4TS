import org.junit.Assert;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
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

public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethod() {
        MockName mockName = MockName.create("mockName");
        Object mock = mock(String.class, Answers.RETURNS_DEEP_STUBS);
        when(mock.toString()).thenAnswer(new ReturnsEmptyValues());

        String result = mock.toString();
        String expected = "Mock for String, hashCode: " + System.identityHashCode(mock);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCompareToMethod() {
        Object mock1 = mock(Comparable.class, Answers.RETURNS_DEEP_STUBS);
        Object mock2 = mock(Comparable.class, Answers.RETURNS_DEEP_STUBS);

        when(mock1.compareTo(mock2)).thenAnswer(new ReturnsEmptyValues());

        int result = mock1.compareTo(mock2);
        Assert.assertEquals(1, result);
    }

    @Test
    public void testPrimitivesOrWrappers() {
        // Testing primitive types
        Integer intValue = 0;
        when(mock(Integer.class).answer(new InvocationOnMock())).thenAnswer(new ReturnsEmptyValues());
        Assert.assertEquals(Integer.valueOf(0), mock(Integer.class).answer(new InvocationOnMock()));

        byte byteValue = (byte) 0;
        when(mock(Byte.class).answer(new InvocationOnMock())).thenAnswer(new ReturnsEmptyValues());
        Assert.assertEquals(Byte.valueOf((byte) 0), mock(Byte.class).answer(new InvocationOnMock()));

        short shortValue = (short) 0;
        when(mock(Short.class).answer(new InvocationOnMock())).thenAnswer(new ReturnsEmptyValues());
        Assert.assertEquals(Short.valueOf((short) 0), mock(Short.class).answer(new InvocationOnMock()));

        long longValue = 0L;
        when(mock(Long.class).answer(new InvocationOnMock())).thenAnswer(new ReturnsEmptyValues());
        Assert.assertEquals(Long.valueOf(0), mock(Long.class).answer(new InvocationOnMock()));

        char charValue = (char) 0;
        when(mock(Character.class).answer(new InvocationOnMock())).thenAnswer(new ReturnsEmptyValues());
        Assert.assertEquals(Character.valueOf((char) 0), mock(Character.class).answer(new InvocationOnMock()));

        boolean booleanValue = false;
        when(mock(Boolean.class).answer(new InvocationOnMock())).thenAnswer(new ReturnsEmptyValues());
        Assert.assertEquals(Boolean.FALSE, mock(Boolean.class).answer(new InvocationOnMock()));

        float floatValue = 0.0f;
        when(mock(Float.class).answer(new InvocationOnMock())).thenAnswer(new ReturnsEmptyValues());
        Assert.assertEquals(Float.valueOf(0.0f), mock(Float.class).answer(new InvocationOnMock()));

        double doubleValue = 0.0;
        when(mock(Double.class).answer(new InvocationOnMock())).thenAnswer(new ReturnsEmptyValues());
        Assert.assertEquals(Double.valueOf(0.0), mock(Double.class).answer(new InvocationOnMock()));

        // Testing wrapper classes
        Integer intWrapper = Integer.valueOf(0);
        when(mock(Integer.class).answer(new InvocationOnMock())).thenAnswer(new ReturnsEmptyValues());
        Assert.assertEquals(Integer.valueOf(0), mock(Integer.class).answer(new InvocationOnMock()));

        Byte byteWrapper = Byte.valueOf((byte) 0);
        when(mock(Byte.class).answer(new InvocationOnMock())).thenAnswer(new ReturnsEmptyValues());
        Assert.assertEquals(Byte.valueOf((byte) 0), mock(Byte.class).answer(new InvocationOnMock()));

        Short shortWrapper = Short.valueOf((short) 0);
        when(mock(Short.class).answer(new InvocationOnMock())).thenAnswer(new ReturnsEmptyValues());
        Assert.assertEquals(Short.valueOf((short) 0), mock(Short.class).answer(new InvocationOnMock()));

        Long longWrapper = Long.valueOf(0L);
        when(mock(Long.class).answer(new InvocationOnMock())).thenAnswer(new ReturnsEmptyValues());
        Assert.assertEquals(Long.valueOf(0L), mock(Long.class).answer(new InvocationOnMock()));

        Character charWrapper = Character.valueOf((char) 0);
        when(mock(Character.class).answer(new InvocationOnMock())).thenAnswer(new ReturnsEmptyValues());
        Assert.assertEquals(Character.valueOf((char) 0), mock(Character.class).answer(new InvocationOnMock()));

        Boolean booleanWrapper = Boolean.FALSE;
        when(mock(Boolean.class).answer(new InvocationOnMock())).thenAnswer(new ReturnsEmptyValues());
        Assert.assertEquals(Boolean.FALSE, mock(Boolean.class).answer(new InvocationOnMock()));

        Float floatWrapper = Float.valueOf(0.0f);
        when(mock(Float.class).answer(new InvocationOnMock())).thenAnswer(new ReturnsEmptyValues());
        Assert.assertEquals(Float.valueOf(0.0f), mock(Float.class).answer(new InvocationOnMock()));

        Double doubleWrapper = Double.valueOf(0.0);
        when(mock(Double.class).answer(new InvocationOnMock())).thenAnswer(new ReturnsEmptyValues());
        Assert.assertEquals(Double.valueOf(0.0), mock(Double.class).answer(new InvocationOnMock()));
    }

    @Test
    public void testEmptyCollections() {
        // Testing empty collections
        when(mock(Collection.class).answer(new InvocationOnMock())).thenAnswer(new ReturnsEmptyValues());
        Collection<Object> result = mock(Collection.class).answer(new InvocationOnMock());
        Assert.assertTrue(result instanceof LinkedList);
        Assert.assertTrue(result.isEmpty());

        when(mock(Set.class).answer(new InvocationOnMock())).thenAnswer(new ReturnsEmptyValues());
        Set<Object> resultSet = mock(Set.class).answer(new InvocationOnMock());
        Assert.assertTrue(resultSet instanceof HashSet);
        Assert.assertTrue(resultSet.isEmpty());

        when(mock(HashSet.class).answer(new InvocationOnMock())).thenAnswer(new ReturnsEmptyValues());
        Set<Object> resultHashSet = mock(HashSet.class).answer(new InvocationOnMock());
        Assert.assertTrue(resultHashSet instanceof HashSet);
        Assert.assertTrue(resultHashSet.isEmpty());

        when(mock(SortedSet.class).answer(new InvocationOnMock())).thenAnswer(new ReturnsEmptyValues());
        SortedSet<Object> resultSortedSet = mock(SortedSet.class).answer(new InvocationOnMock());
        Assert.assertTrue(resultSortedSet instanceof TreeSet);
        Assert.assertTrue(resultSortedSet.isEmpty());

        when(mock(TreeSet.class).answer(new InvocationOnMock())).thenAnswer(new ReturnsEmptyValues());
        SortedSet<Object> resultTreeSet = mock(TreeSet.class).answer(new InvocationOnMock());
        Assert.assertTrue(resultTreeSet instanceof TreeSet);
        Assert.assertTrue(resultTreeSet.isEmpty());

        when(mock(LinkedHashSet.class).answer(new InvocationOnMock())).thenAnswer(new ReturnsEmptyValues());
        Set<Object> resultLinkedHashSet = mock(LinkedHashSet.class).answer(new InvocationOnMock());
        Assert.assertTrue(resultLinkedHashSet instanceof LinkedHashSet);
        Assert.assertTrue(resultLinkedHashSet.isEmpty());

        when(mock(List.class).answer(new InvocationOnMock())).thenAnswer(new ReturnsEmptyValues());
        List<Object> resultList = mock(List.class).answer(new InvocationOnMock());
        Assert.assertTrue(resultList instanceof LinkedList);
        Assert.assertTrue(resultList.isEmpty());

        when(mock(LinkedList.class).answer(new InvocationOnMock())).thenAnswer(new ReturnsEmptyValues());
        List<Object> resultLinkedList = mock(LinkedList.class).answer(new InvocationOnMock());
        Assert.assertTrue(resultLinkedList instanceof LinkedList);
        Assert.assertTrue(resultLinkedList.isEmpty());

        when(mock(ArrayList.class).answer(new InvocationOnMock())).thenAnswer(new ReturnsEmptyValues());
        List<Object> resultArrayList = mock(ArrayList.class).answer(new InvocationOnMock());
        Assert.assertTrue(resultArrayList instanceof ArrayList);
        Assert.assertTrue(resultArrayList.isEmpty());
    }

    @Test
    public void testEmptyMaps() {
        // Testing empty maps
        when(mock(Map.class).answer(new InvocationOnMock())).thenAnswer(new ReturnsEmptyValues());
        Map<Object, Object> result = mock(Map.class).answer(new InvocationOnMock());
        Assert.assertTrue(result instanceof HashMap);
        Assert.assertTrue(result.isEmpty());

        when(mock(HashMap.class).answer(new InvocationOnMock())).thenAnswer(new ReturnsEmptyValues());
        Map<Object, Object> resultHashMap = mock(HashMap.class).answer(new InvocationOnMock());
        Assert.assertTrue(resultHashMap instanceof HashMap);
        Assert.assertTrue(resultHashMap.isEmpty());

        when(mock(SortedMap.class).answer(new InvocationOnMock())).thenAnswer(new ReturnsEmptyValues());
        SortedMap<Object, Object> resultSortedMap = mock(SortedMap.class).answer(new InvocationOnMock());
        Assert.assertTrue(resultSortedMap instanceof TreeMap);
        Assert.assertTrue(resultSortedMap.isEmpty());

        when(mock(TreeMap.class).answer(new InvocationOnMock())).thenAnswer(new ReturnsEmptyValues());
        SortedMap<Object, Object> resultTreeMap = mock(TreeMap.class).answer(new InvocationOnMock());
        Assert.assertTrue(resultTreeMap instanceof TreeMap);
        Assert.assertTrue(resultTreeMap.isEmpty());

        when(mock(LinkedHashMap.class).answer(new InvocationOnMock())).thenAnswer(new ReturnsEmptyValues());
        Map<Object, Object> resultLinkedHashMap = mock(LinkedHashMap.class).answer(new InvocationOnMock());
        Assert.assertTrue(resultLinkedHashMap instanceof LinkedHashMap);
        Assert.assertTrue(resultLinkedHashMap.isEmpty());
    }
}