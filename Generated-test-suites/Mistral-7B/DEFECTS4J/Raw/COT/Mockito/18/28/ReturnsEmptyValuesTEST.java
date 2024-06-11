import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
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
    public void testToStringMethod() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);
        Object mock = Mockito.mock(Object.class);
        Mockito.when(mock.toString()).thenReturn("Mock Object");
        Mockito.when(returnsEmptyValues.answer(ArgumentMatchers.method(String.class, "toString"))).thenReturn(returnsEmptyValues);
        Mockito.when(returnsEmptyValues.answer(ArgumentMatchers.method(Object.class, "toString"))).thenReturn(mock);

        Assert.assertEquals("Mock for Mockito.Mock$1, hashCode: 16137515", returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class)));
    }

    @Test
    public void testCompareToMethod() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);
        Object obj1 = Mockito.mock(Object.class);
        Object obj2 = Mockito.mock(Object.class);
        Mockito.when(returnsEmptyValues.answer(ArgumentMatchers.method(Object.class, "compareTo", Object.class))).thenReturn(0);

        Assert.assertEquals(0, returnsEmptyValues.answer(Mockito.argThat(m -> m.getMethod().getName().equals("compareTo"))).invoke(obj1, obj1));
        Assert.assertEquals(1, returnsEmptyValues.answer(Mockito.argThat(m -> m.getMethod().getName().equals("compareTo"))).invoke(obj1, obj2));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCollectionMethodWithModifyingCode() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);
        ArrayList<Object> list = Mockito.mock(ArrayList.class);
        Mockito.when(returnsEmptyValues.returnValueFor(ArrayList.class)).thenReturn(list);

        list.add(Mockito.mock(Object.class));
    }

    @Test
    public void testCollectionMethods() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);

        Collection<Object> collection = returnsEmptyValues.returnValueFor(Collection.class);
        Assert.assertTrue(collection.isEmpty());

        Set<Object> set = returnsEmptyValues.returnValueFor(Set.class);
        Assert.assertTrue(set.isEmpty());

        HashSet<Object> hashSet = returnsEmptyValues.returnValueFor(HashSet.class);
        Assert.assertTrue(hashSet.isEmpty());

        SortedSet<Object> sortedSet = returnsEmptyValues.returnValueFor(SortedSet.class);
        Assert.assertTrue(sortedSet.isEmpty());

        TreeSet<Object> treeSet = returnsEmptyValues.returnValueFor(TreeSet.class);
        Assert.assertTrue(treeSet.isEmpty());

        LinkedHashSet<Object> linkedHashSet = returnsEmptyValues.returnValueFor(LinkedHashSet.class);
        Assert.assertTrue(linkedHashSet.isEmpty());

        List<Object> list = returnsEmptyValues.returnValueFor(List.class);
        Assert.assertTrue(list.isEmpty());

        LinkedList<Object> linkedList = returnsEmptyValues.returnValueFor(LinkedList.class);
        Assert.assertTrue(linkedList.isEmpty());

        ArrayList<Object> arrayList = returnsEmptyValues.returnValueFor(ArrayList.class);
        Assert.assertTrue(arrayList.isEmpty());

        Map<Object, Object> map = returnsEmptyValues.returnValueFor(Map.class);
        Assert.assertTrue(map.isEmpty());

        HashMap<Object, Object> hashMap = returnsEmptyValues.returnValueFor(HashMap.class);
        Assert.assertTrue(hashMap.isEmpty());

        SortedMap<Object, Object> sortedMap = returnsEmptyValues.returnValueFor(SortedMap.class);
        Assert.assertTrue(sortedMap.isEmpty());

        TreeMap<Object, Object> treeMap = returnsEmptyValues.returnValueFor(TreeMap.class);
        Assert.assertTrue(treeMap.isEmpty());

        LinkedHashMap<Object, Object> linkedHashMap = returnsEmptyValues.returnValueFor(LinkedHashMap.class);
        Assert.assertTrue(linkedHashMap.isEmpty());
    }
}