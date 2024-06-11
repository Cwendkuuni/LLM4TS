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
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethod() {
        Object mock = mock(String.class);
        Answer answer = new ReturnsEmptyValues();

        when(mock.toString()).thenAnswer(answer);

        String actual = mock.toString();
        String expected = "Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testCompareToMethod() {
        Object mock1 = mock(Comparable.class);
        Object mock2 = mock(Comparable.class);

        Answer answer = new ReturnsEmptyValues();

        when(mock1.compareTo(mock2)).thenAnswer(answer);

        int actual = mock1.compareTo(mock2);
        int expected = 1;

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPrimitiveMethods() {
        Answer answer = new ReturnsEmptyValues();

        byte byteMock = mock(Byte.class);
        short shortMock = mock(Short.class);
        int intMock = mock(Integer.class);
        long longMock = mock(Long.class);
        char charMock = mock(Character.class);
        boolean booleanMock = mock(Boolean.class);

        when(byteMock.byteValue()).thenAnswer(answer);
        when(shortMock.shortValue()).thenAnswer(answer);
        when(intMock.intValue()).thenAnswer(answer);
        when(longMock.longValue()).thenAnswer(answer);
        when(charMock.charValue()).thenAnswer(answer);
        when(booleanMock.booleanValue()).thenAnswer(answer);

        byte actualByte = byteMock.byteValue();
        short actualShort = shortMock.shortValue();
        int actualInt = intMock.intValue();
        long actualLong = longMock.longValue();
        char actualChar = charMock.charValue();
        boolean actualBoolean = booleanMock.booleanValue();

        Assert.assertSame(ReturnsEmptyValues.Primitives.DEFAULT_BYTE, actualByte);
        Assert.assertSame(ReturnsEmptyValues.Primitives.DEFAULT_SHORT, actualShort);
        Assert.assertSame(ReturnsEmptyValues.Primitives.DEFAULT_INT, actualInt);
        Assert.assertSame(ReturnsEmptyValues.Primitives.DEFAULT_LONG, actualLong);
        Assert.assertSame(ReturnsEmptyValues.Primitives.DEFAULT_CHAR, actualChar);
        Assert.assertSame(ReturnsEmptyValues.Primitives.DEFAULT_BOOLEAN, actualBoolean);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnknownMethod() {
        Object mock = mock(ReturnsEmptyValues.class);
        Answer answer = new ReturnsEmptyValues();

        when(mock.unknownMethod()).thenAnswer(answer);

        mock.unknownMethod();
    }

    @Test
    public void testCollectionMethods() {
        Answer answer = new ReturnsEmptyValues();

        Collection<String> collectionMock = mock(Collection.class);

        when(collectionMock.size()).thenAnswer(answer);
        when(collectionMock.isEmpty()).thenAnswer(answer);
        when(collectionMock.add(anyString())).thenAnswer(answer);
        when(collectionMock.remove(anyString())).thenAnswer(answer);
        when(collectionMock.clear()).thenAnswer(answer);
        when(collectionMock.containsAll(anyCollection())).thenAnswer(answer);
        when(collectionMock.addAll(anyCollection())).thenAnswer(answer);
        when(collectionMock.removeAll(anyCollection())).thenAnswer(answer);
        when(collectionMock.retainAll(anyCollection())).thenAnswer(answer);
        when(collectionMock.iterator()).thenAnswer(answer);

        verifyZeroInteractions(collectionMock);
    }

    @Test
    public void testSetMethods() {
        Answer answer = new ReturnsEmptyValues();

        Set<String> setMock = mock(Set.class);

        when(setMock.size()).thenAnswer(answer);
        when(setMock.isEmpty()).thenAnswer(answer);
        when(setMock.add(anyString())).thenAnswer(answer);
        when(setMock.remove(anyString())).thenAnswer(answer);
        when(setMock.clear()).thenAnswer(answer);
        when(setMock.contains(anyString())).thenAnswer(answer);
        when(setMock.containsAll(anyCollection())).thenAnswer(answer);
        when(setMock.addAll(anyCollection())).thenAnswer(answer);
        when(setMock.removeAll(anyCollection())).thenAnswer(answer);
        when(setMock.retainAll(anyCollection())).thenAnswer(answer);
        when(setMock.iterator()).thenAnswer(answer);

        verifyZeroInteractions(setMock);
    }

    @Test
    public void testSortedSetMethods() {
        Answer answer = new ReturnsEmptyValues();

        SortedSet<String> sortedSetMock = mock(SortedSet.class);

        when(sortedSetMock.size()).thenAnswer(answer);
        when(sortedSetMock.isEmpty()).thenAnswer(answer);
        when(sortedSetMock.add(anyString())).thenAnswer(answer);
        when(sortedSetMock.remove(anyString())).thenAnswer(answer);
        when(sortedSetMock.clear()).thenAnswer(answer);
        when(sortedSetMock.contains(anyString())).thenAnswer(answer);
        when(sortedSetMock.containsAll(anyCollection())).thenAnswer(answer);
        when(sortedSetMock.addAll(anyCollection())).thenAnswer(answer);
        when(sortedSetMock.removeAll(anyCollection())).thenAnswer(answer);
        when(sortedSetMock.retainAll(anyCollection())).thenAnswer(answer);
        when(sortedSetMock.first()).thenAnswer(answer);
        when(sortedSetMock.last()).thenAnswer(answer);
        when(sortedSetMock.headSet(anyInt())).thenAnswer(answer);
        when(sortedSetMock.tailSet(anyInt())).thenAnswer(answer);
        when(sortedSetMock.subSet(anyInt(), anyInt())).thenAnswer(answer);
        when(sortedSetMock.comparator()).thenAnswer(answer);
        when(sortedSetMock.iterator()).thenAnswer(answer);

        verifyZeroInteractions(sortedSetMock);
    }

    @Test
    public void testMapMethods() {
        Answer answer = new ReturnsEmptyValues();

        Map<String, String> mapMock = mock(Map.class);

        when(mapMock.size()).thenAnswer(answer);
        when(mapMock.isEmpty()).thenAnswer(answer);
        when(mapMock.containsKey(anyString())).thenAnswer(answer);
        when(mapMock.containsValue(anyString())).thenAnswer(answer);
        when(mapMock.entrySet()).thenAnswer(answer);
        when(mapMock.keySet()).thenAnswer(answer);
        when(mapMock.values()).thenAnswer(answer);
        when(mapMock.get(anyString())).thenAnswer(answer);
        when(mapMock.put(anyString(), anyString())).thenAnswer(answer);
        when(mapMock.remove(anyString())).thenAnswer(answer);
        when(mapMock.clear()).thenAnswer(answer);

        verifyZeroInteractions(mapMock);
    }

    @Test
    public void testSortedMapMethods() {
        Answer answer = new ReturnsEmptyValues();

        SortedMap<String, String> sortedMapMock = mock(SortedMap.class);

        when(sortedMapMock.size()).thenAnswer(answer);
        when(sortedMapMock.isEmpty()).thenAnswer(answer);
        when(sortedMapMock.containsKey(anyString())).thenAnswer(answer);
        when(sortedMapMock.containsValue(anyString())).thenAnswer(answer);
        when(sortedMapMock.entrySet()).thenAnswer(answer);
        when(sortedMapMock.keySet()).thenAnswer(answer);
        when(sortedMapMock.values()).thenAnswer(answer);
        when(sortedMapMock.get(anyString())).thenAnswer(answer);
        when(sortedMapMock.put(anyString(), anyString())).thenAnswer(answer);
        when(sortedMapMock.remove(anyString())).thenAnswer(answer);
        when(sortedMapMock.clear()).thenAnswer(answer);
        when(sortedMapMock.firstKey()).thenAnswer(answer);
        when(sortedMapMock.lastKey()).thenAnswer(answer);
        when(sortedMapMock.headMap(anyInt())).thenAnswer(answer);
        when(sortedMapMock.tailMap(anyInt())).thenAnswer(answer);
        when(sortedMapMock.subMap(anyInt(), anyInt())).thenAnswer(answer);
        when(sortedMapMock.comparator()).thenAnswer(answer);

        verifyZeroInteractions(sortedMapMock);
    }
}