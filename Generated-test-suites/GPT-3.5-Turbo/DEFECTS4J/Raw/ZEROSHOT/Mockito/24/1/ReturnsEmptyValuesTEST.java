import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import org.mockito.stubbing.Answer;
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

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ReturnsEmptyValuesTest {
    private ReturnsEmptyValues returnsEmptyValues;
    private InvocationOnMock invocation;

    @Before
    public void setUp() {
        returnsEmptyValues = new ReturnsEmptyValues();
        invocation = mock(InvocationOnMock.class);
    }

    @Test
    public void testAnswer_ToStringMethod() {
        // Setup
        when(invocation.getMethod()).thenReturn(Object.class.getMethods()[0]);
        Object mock = mock(Object.class);
        MockName name = mock(MockName.class);
        when(mockUtil.getMockName(mock)).thenReturn(name);
        when(name.isDefault()).thenReturn(true);
        when(mockUtil.getMockSettings(mock).getTypeToMock()).thenReturn(Object.class);

        // Run
        Object result = returnsEmptyValues.answer(invocation);

        // Verify
        assertEquals("Mock for Object, hashCode: " + mock.hashCode(), result);
    }

    @Test
    public void testAnswer_CompareToMethod() {
        // Setup
        when(invocation.getMethod()).thenReturn(Comparable.class.getMethods()[0]);

        // Run
        Object result = returnsEmptyValues.answer(invocation);

        // Verify
        assertEquals(1, result);
    }

    @Test
    public void testAnswer_PrimitiveWrapperClass() {
        // Setup
        when(invocation.getMethod()).thenReturn(Integer.class.getMethods()[0]);

        // Run
        Object result = returnsEmptyValues.answer(invocation);

        // Verify
        assertEquals(0, result);
    }

    @Test
    public void testAnswer_PrimitiveReturnType() {
        // Setup
        when(invocation.getMethod()).thenReturn(int.class.getMethods()[0]);

        // Run
        Object result = returnsEmptyValues.answer(invocation);

        // Verify
        assertEquals(0, result);
    }

    @Test
    public void testAnswer_CollectionReturnType() {
        // Setup
        when(invocation.getMethod()).thenReturn(Collection.class.getMethods()[0]);

        // Run
        Object result = returnsEmptyValues.answer(invocation);

        // Verify
        assertEquals(new LinkedList<Object>(), result);
    }

    @Test
    public void testAnswer_SetReturnType() {
        // Setup
        when(invocation.getMethod()).thenReturn(Set.class.getMethods()[0]);

        // Run
        Object result = returnsEmptyValues.answer(invocation);

        // Verify
        assertEquals(new HashSet<Object>(), result);
    }

    @Test
    public void testAnswer_HashSetReturnType() {
        // Setup
        when(invocation.getMethod()).thenReturn(HashSet.class.getMethods()[0]);

        // Run
        Object result = returnsEmptyValues.answer(invocation);

        // Verify
        assertEquals(new HashSet<Object>(), result);
    }

    @Test
    public void testAnswer_SortedSetReturnType() {
        // Setup
        when(invocation.getMethod()).thenReturn(SortedSet.class.getMethods()[0]);

        // Run
        Object result = returnsEmptyValues.answer(invocation);

        // Verify
        assertEquals(new TreeSet<Object>(), result);
    }

    @Test
    public void testAnswer_TreeSetReturnType() {
        // Setup
        when(invocation.getMethod()).thenReturn(TreeSet.class.getMethods()[0]);

        // Run
        Object result = returnsEmptyValues.answer(invocation);

        // Verify
        assertEquals(new TreeSet<Object>(), result);
    }

    @Test
    public void testAnswer_LinkedHashSetReturnType() {
        // Setup
        when(invocation.getMethod()).thenReturn(LinkedHashSet.class.getMethods()[0]);

        // Run
        Object result = returnsEmptyValues.answer(invocation);

        // Verify
        assertEquals(new LinkedHashSet<Object>(), result);
    }

    @Test
    public void testAnswer_ListReturnType() {
        // Setup
        when(invocation.getMethod()).thenReturn(List.class.getMethods()[0]);

        // Run
        Object result = returnsEmptyValues.answer(invocation);

        // Verify
        assertEquals(new LinkedList<Object>(), result);
    }

    @Test
    public void testAnswer_LinkedListReturnType() {
        // Setup
        when(invocation.getMethod()).thenReturn(LinkedList.class.getMethods()[0]);

        // Run
        Object result = returnsEmptyValues.answer(invocation);

        // Verify
        assertEquals(new LinkedList<Object>(), result);
    }

    @Test
    public void testAnswer_ArrayListReturnType() {
        // Setup
        when(invocation.getMethod()).thenReturn(ArrayList.class.getMethods()[0]);

        // Run
        Object result = returnsEmptyValues.answer(invocation);

        // Verify
        assertEquals(new ArrayList<Object>(), result);
    }

    @Test
    public void testAnswer_MapReturnType() {
        // Setup
        when(invocation.getMethod()).thenReturn(Map.class.getMethods()[0]);

        // Run
        Object result = returnsEmptyValues.answer(invocation);

        // Verify
        assertEquals(new HashMap<Object, Object>(), result);
    }

    @Test
    public void testAnswer_HashMapReturnType() {
        // Setup
        when(invocation.getMethod()).thenReturn(HashMap.class.getMethods()[0]);

        // Run
        Object result = returnsEmptyValues.answer(invocation);

        // Verify
        assertEquals(new HashMap<Object, Object>(), result);
    }

    @Test
    public void testAnswer_SortedMapReturnType() {
        // Setup
        when(invocation.getMethod()).thenReturn(SortedMap.class.getMethods()[0]);

        // Run
        Object result = returnsEmptyValues.answer(invocation);

        // Verify
        assertEquals(new TreeMap<Object, Object>(), result);
    }

    @Test
    public void testAnswer_TreeMapReturnType() {
        // Setup
        when(invocation.getMethod()).thenReturn(TreeMap.class.getMethods()[0]);

        // Run
        Object result = returnsEmptyValues.answer(invocation);

        // Verify
        assertEquals(new TreeMap<Object, Object>(), result);
    }

    @Test
    public void testAnswer_LinkedHashMapReturnType() {
        // Setup
        when(invocation.getMethod()).thenReturn(LinkedHashMap.class.getMethods()[0]);

        // Run
        Object result = returnsEmptyValues.answer(invocation);

        // Verify
        assertEquals(new LinkedHashMap<Object, Object>(), result);
    }
}