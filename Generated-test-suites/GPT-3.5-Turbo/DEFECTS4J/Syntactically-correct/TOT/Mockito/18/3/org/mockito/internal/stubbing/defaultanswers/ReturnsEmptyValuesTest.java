import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.io.Serializable;
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
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues returnsEmptyValues;

    @Before
    public void setUp() {
        returnsEmptyValues = new ReturnsEmptyValues();
    }

    @Test
    public void testAnswer_ReturnsExpectedObject_ForToStringMethod() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));
        when(invocation.getMock()).thenReturn(mock(Serializable.class));

        // Act
        Object result = returnsEmptyValues.answer(invocation);

        // Assert
        assertEquals("Mock for Serializable, hashCode: " + invocation.getMock().hashCode(), result);
    }


    @Test
    public void testAnswer_ReturnsExpectedInteger_ForCompareToMethod() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Comparable.class.getMethod("compareTo", Object.class));
        when(invocation.getMock()).thenReturn(mock(Comparable.class));
        when(invocation.getArguments()).thenReturn(new Object[] { invocation.getMock() });

        // Act
        Object result = returnsEmptyValues.answer(invocation);

        // Assert
        assertEquals(0, result);
    }


    @Test
    public void testAnswer_ReturnsExpectedValue_ForPrimitiveReturningMethod() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Integer.class.getMethod("intValue"));
        when(invocation.getMethod().getReturnType()).thenReturn(int.class);

        // Act
        Object result = returnsEmptyValues.answer(invocation);

        // Assert
        assertEquals(0, result);
    }


    @Test
    public void testAnswer_ReturnsExpectedValue_ForPrimitiveWrapperReturningMethod() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Integer.class.getMethod("valueOf", int.class));
        when(invocation.getMethod().getReturnType()).thenReturn(Integer.class);

        // Act
        Object result = returnsEmptyValues.answer(invocation);

        // Assert
        assertEquals(0, result);
    }


    @Test
    public void testAnswer_ReturnsExpectedList_ForCollectionReturningMethod() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Collection.class.getMethod("iterator"));
        when(invocation.getMethod().getReturnType()).thenReturn(Collection.class);

        // Act
        Object result = returnsEmptyValues.answer(invocation);

        // Assert
        assertTrue(result instanceof LinkedList);
        assertEquals(0, ((Collection) result).size());
    }


    @Test
    public void testAnswer_ReturnsExpectedSet_ForSetReturningMethod() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Set.class.getMethod("iterator"));
        when(invocation.getMethod().getReturnType()).thenReturn(Set.class);

        // Act
        Object result = returnsEmptyValues.answer(invocation);

        // Assert
        assertTrue(result instanceof HashSet);
        assertEquals(0, ((Set) result).size());
    }


    @Test
    public void testAnswer_ReturnsExpectedSet_ForHashSetReturningMethod() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(HashSet.class.getMethod("iterator"));
        when(invocation.getMethod().getReturnType()).thenReturn(HashSet.class);

        // Act
        Object result = returnsEmptyValues.answer(invocation);

        // Assert
        assertTrue(result instanceof HashSet);
        assertEquals(0, ((Set) result).size());
    }


    @Test
    public void testAnswer_ReturnsExpectedSet_ForSortedSetReturningMethod() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(SortedSet.class.getMethod("iterator"));
        when(invocation.getMethod().getReturnType()).thenReturn(SortedSet.class);

        // Act
        Object result = returnsEmptyValues.answer(invocation);

        // Assert
        assertTrue(result instanceof TreeSet);
        assertEquals(0, ((Set) result).size());
    }


    @Test
    public void testAnswer_ReturnsExpectedSet_ForTreeSetReturningMethod() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TreeSet.class.getMethod("iterator"));
        when(invocation.getMethod().getReturnType()).thenReturn(TreeSet.class);

        // Act
        Object result = returnsEmptyValues.answer(invocation);

        // Assert
        assertTrue(result instanceof TreeSet);
        assertEquals(0, ((Set) result).size());
    }


    @Test
    public void testAnswer_ReturnsExpectedSet_ForLinkedHashSetReturningMethod() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(LinkedHashSet.class.getMethod("iterator"));
        when(invocation.getMethod().getReturnType()).thenReturn(LinkedHashSet.class);

        // Act
        Object result = returnsEmptyValues.answer(invocation);

        // Assert
        assertTrue(result instanceof LinkedHashSet);
        assertEquals(0, ((Set) result).size());
    }


    @Test
    public void testAnswer_ReturnsExpectedList_ForListReturningMethod() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(List.class.getMethod("iterator"));
        when(invocation.getMethod().getReturnType()).thenReturn(List.class);

        // Act
        Object result = returnsEmptyValues.answer(invocation);

        // Assert
        assertTrue(result instanceof LinkedList);
        assertEquals(0, ((List) result).size());
    }


    @Test
    public void testAnswer_ReturnsExpectedList_ForLinkedListReturningMethod() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(LinkedList.class.getMethod("iterator"));
        when(invocation.getMethod().getReturnType()).thenReturn(LinkedList.class);

        // Act
        Object result = returnsEmptyValues.answer(invocation);

        // Assert
        assertTrue(result instanceof LinkedList);
        assertEquals(0, ((List) result).size());
    }


    @Test
    public void testAnswer_ReturnsExpectedList_ForArrayListReturningMethod() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ArrayList.class.getMethod("iterator"));
        when(invocation.getMethod().getReturnType()).thenReturn(ArrayList.class);

        // Act
        Object result = returnsEmptyValues.answer(invocation);

        // Assert
        assertTrue(result instanceof ArrayList);
        assertEquals(0, ((List) result).size());
    }


    @Test
    public void testAnswer_ReturnsExpectedMap_ForMapReturningMethod() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Map.class.getMethod("entrySet"));
        when(invocation.getMethod().getReturnType()).thenReturn(Map.class);

        // Act
        Object result = returnsEmptyValues.answer(invocation);

        // Assert
        assertTrue(result instanceof HashMap);
        assertEquals(0, ((Map) result).size());
    }


    @Test
    public void testAnswer_ReturnsExpectedMap_ForHashMapReturningMethod() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(HashMap.class.getMethod("entrySet"));
        when(invocation.getMethod().getReturnType()).thenReturn(HashMap.class);

        // Act
        Object result = returnsEmptyValues.answer(invocation);

        // Assert
        assertTrue(result instanceof HashMap);
        assertEquals(0, ((Map) result).size());
    }


    @Test
    public void testAnswer_ReturnsExpectedMap_ForSortedMapReturningMethod() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(SortedMap.class.getMethod("entrySet"));
        when(invocation.getMethod().getReturnType()).thenReturn(SortedMap.class);

        // Act
        Object result = returnsEmptyValues.answer(invocation);

        // Assert
        assertTrue(result instanceof TreeMap);
        assertEquals(0, ((Map) result).size());
    }


    @Test
    public void testAnswer_ReturnsExpectedMap_ForTreeMapReturningMethod() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TreeMap.class.getMethod("entrySet"));
        when(invocation.getMethod().getReturnType()).thenReturn(TreeMap.class);

        // Act
        Object result = returnsEmptyValues.answer(invocation);

        // Assert
        assertTrue(result instanceof TreeMap);
        assertEquals(0, ((Map) result).size());
    }


    @Test
    public void testAnswer_ReturnsExpectedMap_ForLinkedHashMapReturningMethod() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(LinkedHashMap.class.getMethod("entrySet"));
        when(invocation.getMethod().getReturnType()).thenReturn(LinkedHashMap.class);

        // Act
        Object result = returnsEmptyValues.answer(invocation);

        // Assert
        assertTrue(result instanceof LinkedHashMap);
        assertEquals(0, ((Map) result).size());
    }


    @Test
    public void testAnswer_ReturnsNull_ForOtherMethods() throws Throwable {
        // Arrange
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("hashCode"));
        when(invocation.getMethod().getReturnType()).thenReturn(int.class);

        // Act
        Object result = returnsEmptyValues.answer(invocation);

        // Assert
        assertNull(result);
    }
}