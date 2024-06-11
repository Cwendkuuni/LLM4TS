package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.*;

import static org.junit.Assert.*;

public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethod() {
        Mockito<Object> mock = Mockito.mock(Object.class);
        Answer<?> answer = ReturnsEmptyValues.instance();

        Mockito.doAnswer(answer).when(mock).toString();

        String result = mock.toString();
        String expected = "Mock for " + Object.class.getSimpleName() + ", hashCode: " + mock.hashCode();

        assertEquals(expected, result);
    }

    @Test
    public void testCompareToMethod() {
        Object mock = Mockito.mock(Comparable.class);
        Object other = Mockito.mock(Comparable.class);

        Answer<?> answer = ReturnsEmptyValues.instance();
        Mockito.doAnswer(answer).when(mock).compareTo(ArgumentMatchers.any());
        Mockito.doAnswer(answer).when(other).compareTo(ArgumentMatchers.eq(mock));

        int result = mock.compareTo(other);
        int expected = 1;

        assertEquals(expected, result);

        //Tests when mocks are equal
        result = other.compareTo(mock);
        assertEquals(0, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMutableCollection() {
        Mockito<List<String>> mock = Mockito.mock(List.class);
        Answer<?> answer = ReturnsEmptyValues.instance();
        Mockito.doAnswer(answer).when(mock).add(ArgumentMatchers.anyString());

        List<String> list = mock.asList();
        list.add("test");
    }

    @Test
    public void testEmptySets() {
        Mockito<Set<String>> mock1 = Mockito.mock(Set.class);
        Answer<?> answer = ReturnsEmptyValues.instance();
        Mockito.doAnswer(answer).when(mock1).add(ArgumentMatchers.anyString());

        Set<String> set1 = mock1.asSet();
        Set<String> set2 = new HashSet<>();

        assertTrue(set1.isEmpty());
        assertTrue(set2.isEmpty());

        assertFalse(set1.containsAll(Arrays.asList("test1", "test2")));
        assertFalse(set2.containsAll(Arrays.asList("test1", "test2")));

        Set<String> set3 = Mockito.spy(new HashSet<>());
        Mockito.doAnswer(answer).when(set3).add(ArgumentMatchers.anyString());

        set3.add("test3");
        set3.add("test3");

        //Should return consistent values for Set.size()
        assertEquals(set1.size(), set3.size());
        assertEquals(0, set1.size());
        assertEquals(1, set3.size());

        //Should return consistent values for Set.contains()
        assertFalse(set1.contains("test1"));
        assertFalse(set3.contains("test1"));
        assertFalse(set1.contains("test2"));
        assertFalse(set3.contains("test2"));
        assertTrue(set3.contains("test3"));
    }

    @Test
    public void testEmptyMaps() {
        Mockito<Map<String, String>> mock1 = Mockito.mock(Map.class);
        Answer<?> answer = ReturnsEmptyValues.instance();
        Mockito.doAnswer(answer).when(mock1).put(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());

        Map<String, String> map1 = mock1.asMap();
        Map<String, String> map2 = new HashMap<>();

        assertFalse(map1.containsKey("test1"));
        assertFalse(map2.containsKey("test1"));
        assertFalse(map1.containsValue("test1"));
        assertFalse(map2.containsValue("test1"));

        //Should return consistent values for Map.size()
        assertEquals(map1.size(), map2.size());
        assertEquals(0, map1.size());
        assertEquals(0, map2.size());
    }

    @Test
    public void testEmptySortedMap() {
        Mockito<SortedMap<String, String>> mock1 = Mockito.mock(SortedMap.class);
        Answer<?> answer = ReturnsEmptyValues.instance();
        Mockito.doAnswer(answer).when(mock1).put(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());

        SortedMap<String, String> sortedMap1 = mock1.asSortedMap();
        SortedMap<String, String> sortedMap2 = new TreeMap<>();

        assertFalse(sortedMap1.containsKey("test1"));
        assertFalse(sortedMap2.containsKey("test1"));

        //Should return consistent values for SortedMap.size()
        assertEquals(sortedMap1.size(), sortedMap2.size());
        assertEquals(0, sortedMap1.size());
        assertEquals(0, sortedMap2.size());
    }
}