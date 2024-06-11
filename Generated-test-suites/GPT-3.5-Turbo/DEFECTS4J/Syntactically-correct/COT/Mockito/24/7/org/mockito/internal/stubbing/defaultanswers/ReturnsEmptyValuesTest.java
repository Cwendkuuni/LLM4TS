
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer_toString() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));
        assertEquals("Mock for Object, hashCode: 0", returnsEmptyValues.answer(invocation));
    }

    @Test
    public void testAnswer_compareTo() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Comparable.class.getMethod("compareTo", Object.class));
        assertEquals(1, returnsEmptyValues.answer(invocation));
    }

    @Test
    public void testAnswer_returnValueFor_PrimitiveBoolean() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertEquals(false, returnsEmptyValues.returnValueFor(boolean.class));
    }

    @Test
    public void testAnswer_returnValueFor_PrimitiveByte() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertEquals((byte) 0, returnsEmptyValues.returnValueFor(byte.class));
    }

    @Test
    public void testAnswer_returnValueFor_PrimitiveChar() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertEquals('\0', returnsEmptyValues.returnValueFor(char.class));
    }

    @Test
    public void testAnswer_returnValueFor_PrimitiveShort() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertEquals((short) 0, returnsEmptyValues.returnValueFor(short.class));
    }

    @Test
    public void testAnswer_returnValueFor_PrimitiveInt() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertEquals(0, returnsEmptyValues.returnValueFor(int.class));
    }

    @Test
    public void testAnswer_returnValueFor_PrimitiveLong() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertEquals(0L, returnsEmptyValues.returnValueFor(long.class));
    }

    @Test
    public void testAnswer_returnValueFor_PrimitiveFloat() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertEquals(0.0f, returnsEmptyValues.returnValueFor(float.class), 0.0f);
    }

    @Test
    public void testAnswer_returnValueFor_PrimitiveDouble() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertEquals(0.0, returnsEmptyValues.returnValueFor(double.class), 0.0);
    }

    @Test
    public void testAnswer_returnValueFor_PrimitiveBooleanWrapper() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertEquals(Boolean.FALSE, returnsEmptyValues.returnValueFor(Boolean.class));
    }

    @Test
    public void testAnswer_returnValueFor_PrimitiveByteWrapper() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertEquals((byte) 0, returnsEmptyValues.returnValueFor(Byte.class));
    }

    @Test
    public void testAnswer_returnValueFor_PrimitiveCharWrapper() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertEquals('\0', returnsEmptyValues.returnValueFor(Character.class));
    }

    @Test
    public void testAnswer_returnValueFor_PrimitiveShortWrapper() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertEquals((short) 0, returnsEmptyValues.returnValueFor(Short.class));
    }

    @Test
    public void testAnswer_returnValueFor_PrimitiveIntWrapper() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertEquals(0, returnsEmptyValues.returnValueFor(Integer.class));
    }

    @Test
    public void testAnswer_returnValueFor_PrimitiveLongWrapper() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertEquals(0L, returnsEmptyValues.returnValueFor(Long.class));
    }

    @Test
    public void testAnswer_returnValueFor_PrimitiveFloatWrapper() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertEquals(0.0f, returnsEmptyValues.returnValueFor(Float.class), 0.0f);
    }

    @Test
    public void testAnswer_returnValueFor_PrimitiveDoubleWrapper() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertEquals(0.0, returnsEmptyValues.returnValueFor(Double.class), 0.0);
    }

    @Test
    public void testAnswer_returnValueFor_Collection() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Collection<Object> result = (Collection<Object>) returnsEmptyValues.returnValueFor(Collection.class);
        assertTrue(result instanceof LinkedList);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswer_returnValueFor_Set() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Set<Object> result = (Set<Object>) returnsEmptyValues.returnValueFor(Set.class);
        assertTrue(result instanceof HashSet);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswer_returnValueFor_HashSet() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        HashSet<Object> result = (HashSet<Object>) returnsEmptyValues.returnValueFor(HashSet.class);
        assertTrue(result instanceof HashSet);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswer_returnValueFor_SortedSet() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        SortedSet<Object> result = (SortedSet<Object>) returnsEmptyValues.returnValueFor(SortedSet.class);
        assertTrue(result instanceof TreeSet);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswer_returnValueFor_TreeSet() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        TreeSet<Object> result = (TreeSet<Object>) returnsEmptyValues.returnValueFor(TreeSet.class);
        assertTrue(result instanceof TreeSet);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswer_returnValueFor_LinkedHashSet() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        LinkedHashSet<Object> result = (LinkedHashSet<Object>) returnsEmptyValues.returnValueFor(LinkedHashSet.class);
        assertTrue(result instanceof LinkedHashSet);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswer_returnValueFor_List() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        List<Object> result = (List<Object>) returnsEmptyValues.returnValueFor(List.class);
        assertTrue(result instanceof LinkedList);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswer_returnValueFor_LinkedList() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        LinkedList<Object> result = (LinkedList<Object>) returnsEmptyValues.returnValueFor(LinkedList.class);
        assertTrue(result instanceof LinkedList);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswer_returnValueFor_ArrayList() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        ArrayList<Object> result = (ArrayList<Object>) returnsEmptyValues.returnValueFor(ArrayList.class);
        assertTrue(result instanceof ArrayList);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswer_returnValueFor_Map() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Map<Object, Object> result = (Map<Object, Object>) returnsEmptyValues.returnValueFor(Map.class);
        assertTrue(result instanceof HashMap);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswer_returnValueFor_HashMap() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        HashMap<Object, Object> result = (HashMap<Object, Object>) returnsEmptyValues.returnValueFor(HashMap.class);
        assertTrue(result instanceof HashMap);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswer_returnValueFor_SortedMap() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        SortedMap<Object, Object> result = (SortedMap<Object, Object>) returnsEmptyValues.returnValueFor(SortedMap.class);
        assertTrue(result instanceof TreeMap);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswer_returnValueFor_TreeMap() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        TreeMap<Object, Object> result = (TreeMap<Object, Object>) returnsEmptyValues.returnValueFor(TreeMap.class);
        assertTrue(result instanceof TreeMap);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswer_returnValueFor_LinkedHashMap() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        LinkedHashMap<Object, Object> result = (LinkedHashMap<Object, Object>) returnsEmptyValues.returnValueFor(LinkedHashMap.class);
        assertTrue(result instanceof LinkedHashMap);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testAnswer_returnValueFor_OtherClasses() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        assertNull(returnsEmptyValues.returnValueFor(String.class));
    }

}
