import org.junit.Assert;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;

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

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerToStringMethod() {
        Mockito.when(mock.toString()).thenAnswer(Answers.callRealMethod());
        String result = mock.toString();
        Assert.assertNotNull(result);
    }

    @Test
    public void testAnswerCompareToMethod() {
        Object mock = Mockito.mock(Comparable.class);
        Mockito.when(mock.compareTo(ArgumentMatchers.any())).thenAnswer(Answers.returns(1));
        int result = mock.compareTo(new Object());
        Assert.assertEquals(1, result);
    }

    @Test
    public void testReturnValueForPrimitiveTypes() {
        Object returnsEmptyValues = Mockito.spy(new ReturnsEmptyValues());
        int result = (int) returnsEmptyValues.returnValueFor(int.class);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testReturnValueForWrapperTypes() {
        Object returnsEmptyValues = Mockito.spy(new ReturnsEmptyValues());
        Integer result = (Integer) returnsEmptyValues.returnValueFor(Integer.class);
        Assert.assertEquals(0, result.intValue());
    }

    @Test
    public void testReturnValueForCollectionType() {
        Object returnsEmptyValues = Mockito.spy(new ReturnsEmptyValues());
        Collection<String> result = (Collection<String>) returnsEmptyValues.returnValueFor(Collection.class);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueForSetType() {
        Object returnsEmptyValues = Mockito.spy(new ReturnsEmptyValues());
        Set<String> result = (Set<String>) returnsEmptyValues.returnValueFor(Set.class);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueForHashSetType() {
        Object returnsEmptyValues = Mockito.spy(new ReturnsEmptyValues());
        Set<String> result = (Set<String>) returnsEmptyValues.returnValueFor(HashSet.class);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueForSortedSetType() {
        Object returnsEmptyValues = Mockito.spy(new ReturnsEmptyValues());
        SortedSet<String> result = (SortedSet<String>) returnsEmptyValues.returnValueFor(TreeSet.class);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueForLinkedHashSetType() {
        Object returnsEmptyValues = Mockito.spy(new ReturnsEmptyValues());
        Set<String> result = (Set<String>) returnsEmptyValues.returnValueFor(LinkedHashSet.class);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueForListType() {
        Object returnsEmptyValues = Mockito.spy(new ReturnsEmptyValues());
        List<String> result = (List<String>) returnsEmptyValues.returnValueFor(List.class);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueForLinkedListType() {
        Object returnsEmptyValues = Mockito.spy(new ReturnsEmptyValues());
        List<String> result = (List<String>) returnsEmptyValues.returnValueFor(LinkedList.class);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueForArrayListType() {
        Object returnsEmptyValues = Mockito.spy(new ReturnsEmptyValues());
        List<String> result = (List<String>) returnsEmptyValues.returnValueFor(ArrayList.class);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueForMapType() {
        Object returnsEmptyValues = Mockito.spy(new ReturnsEmptyValues());
        Map<String, String> result = (Map<String, String>) returnsEmptyValues.returnValueFor(Map.class);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueForHashMapType() {
        Object returnsEmptyValues = Mockito.spy(new ReturnsEmptyValues());
        Map<String, String> result = (Map<String, String>) returnsEmptyValues.returnValueFor(HashMap.class);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueForSortedMapType() {
        Object returnsEmptyValues = Mockito.spy(new ReturnsEmptyValues());
        SortedMap<String, String> result = (SortedMap<String, String>) returnsEmptyValues.returnValueFor(TreeMap.class);
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueForLinkedHashMapType() {
        Object returnsEmptyValues = Mockito.spy(new ReturnsEmptyValues());
        Map<String, String> result = (Map<String, String>) returnsEmptyValues.returnValueFor(LinkedHashMap.class);
        Assert.assertTrue(result.isEmpty());
    }

    // TODO test return empty Iterable

    private Object mock;
}