import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer_stringMethodToString() {
        // Arrange
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(String.class.getMethod("toString"));

        // Act
        Object result = returnValue.answer(invocation);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof String);
        assertTrue(result.toString().startsWith("Mock for"));
    }

    @Test
    public void testAnswer_compareToMethod() {
        // Arrange
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Comparable.class.getMethod("compareTo", Object.class));

        // Act
        Object result = returnValue.answer(invocation);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Integer);
        assertTrue((int) result == 0 || (int) result == 1);
    }

    @Test
    public void testAnswer_primitiveReturnType() {
        // Arrange
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Integer.class.getMethod("intValue"));

        // Act
        Object result = returnValue.answer(invocation);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Integer);
        assertTrue((int) result == 0);
    }

    @Test
    public void testAnswer_primitiveWrapperReturnType() {
        // Arrange
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Integer.class.getMethod("valueOf", String.class));

        // Act
        Object result = returnValue.answer(invocation);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Integer);
        assertTrue((int) result == 0);
    }

    @Test
    public void testAnswer_collectionReturnType() {
        // Arrange
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Collection.class.getMethod("toArray"));

        // Act
        Object result = returnValue.answer(invocation);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof LinkedList);
        assertTrue(((Collection) result).isEmpty());
    }

    @Test
    public void testAnswer_setReturnType() {
        // Arrange
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Set.class.getMethod("isEmpty"));

        // Act
        Object result = returnValue.answer(invocation);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof HashSet);
        assertTrue(((Set) result).isEmpty());
    }

    @Test
    public void testAnswer_hashSetReturnType() {
        // Arrange
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(HashSet.class.getMethod("size"));

        // Act
        Object result = returnValue.answer(invocation);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof HashSet);
        assertTrue(((Set) result).isEmpty());
    }

    @Test
    public void testAnswer_sortedSetReturnType() {
        // Arrange
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(SortedSet.class.getMethod("subSet", Object.class, Object.class));

        // Act
        Object result = returnValue.answer(invocation);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof TreeSet);
        assertTrue(((Set) result).isEmpty());
    }

    @Test
    public void testAnswer_treeSetReturnType() {
        // Arrange
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TreeSet.class.getMethod("add", Object.class));

        // Act
        Object result = returnValue.answer(invocation);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof TreeSet);
        assertTrue(((Set) result).isEmpty());
    }

    @Test
    public void testAnswer_linkedHashSetReturnType() {
        // Arrange
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(LinkedHashSet.class.getMethod("iterator"));

        // Act
        Object result = returnValue.answer(invocation);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof LinkedHashSet);
        assertTrue(((Set) result).isEmpty());
    }

    @Test
    public void testAnswer_listReturnType() {
        // Arrange
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(List.class.getMethod("size"));

        // Act
        Object result = returnValue.answer(invocation);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof LinkedList);
        assertTrue(((List) result).isEmpty());
    }

    @Test
    public void testAnswer_linkedListReturnType() {
        // Arrange
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(LinkedList.class.getMethod("getLast"));

        // Act
        Object result = returnValue.answer(invocation);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof LinkedList);
        assertTrue(((List) result).isEmpty());
    }

    @Test
    public void testAnswer_arrayListReturnType() {
        // Arrange
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ArrayList.class.getMethod("remove", Object.class));

        // Act
        Object result = returnValue.answer(invocation);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof ArrayList);
        assertTrue(((List) result).isEmpty());
    }

    @Test
    public void testAnswer_mapReturnType() {
        // Arrange
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Map.class.getMethod("containsKey", Object.class));

        // Act
        Object result = returnValue.answer(invocation);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof HashMap);
        assertTrue(((Map) result).isEmpty());
    }

    @Test
    public void testAnswer_hashMapReturnType() {
        // Arrange
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(HashMap.class.getMethod("entrySet"));

        // Act
        Object result = returnValue.answer(invocation);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof HashMap);
        assertTrue(((Map) result).isEmpty());
    }

    @Test
    public void testAnswer_sortedMapReturnType() {
        // Arrange
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(SortedMap.class.getMethod("firstKey"));

        // Act
        Object result = returnValue.answer(invocation);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof TreeMap);
        assertTrue(((Map) result).isEmpty());
    }

    @Test
    public void testAnswer_treeMapReturnType() {
        // Arrange
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TreeMap.class.getMethod("put", Object.class, Object.class));

        // Act
        Object result = returnValue.answer(invocation);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof TreeMap);
        assertTrue(((Map) result).isEmpty());
    }

    @Test
    public void testAnswer_linkedHashMapReturnType() {
        // Arrange
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(LinkedHashMap.class.getMethod("values"));

        // Act
        Object result = returnValue.answer(invocation);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof LinkedHashMap);
        assertTrue(((Map) result).isEmpty());
    }
}