import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.Primitives;
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

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

    @Test
    public void testAnswer_toString() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = ReturnsEmptyValues.class.getDeclaredMethod("returnValueFor", Class.class);
        when(invocation.getMethod()).thenReturn(method);
        String result = returnsEmptyValues.answer(invocation);
        assertEquals("Mock for Object, hashCode: 0", result);
    }

    @Test
    public void testAnswer_compareTo() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = ReturnsEmptyValues.class.getDeclaredMethod("returnValueFor", Class.class);
        when(invocation.getMethod()).thenReturn(method);
        Integer result = returnsEmptyValues.answer(invocation);
        assertEquals(Integer.valueOf(1), result);
    }

    @Test
    public void testReturnValueFor_primitiveOrWrapper() {
        Object result = returnsEmptyValues.returnValueFor(int.class);
        assertEquals(0, result);

        result = returnsEmptyValues.returnValueFor(Integer.class);
        assertEquals(Integer.valueOf(0), result);
    }

    @Test
    public void testReturnValueFor_Collection() {
        Object result = returnsEmptyValues.returnValueFor(Collection.class);
        assertTrue(result instanceof LinkedList);
    }

    @Test
    public void testReturnValueFor_Set() {
        Object result = returnsEmptyValues.returnValueFor(Set.class);
        assertTrue(result instanceof HashSet);
    }

    @Test
    public void testReturnValueFor_HashSet() {
        Object result = returnsEmptyValues.returnValueFor(HashSet.class);
        assertTrue(result instanceof HashSet);
    }

    @Test
    public void testReturnValueFor_SortedSet() {
        Object result = returnsEmptyValues.returnValueFor(SortedSet.class);
        assertTrue(result instanceof TreeSet);
    }

    @Test
    public void testReturnValueFor_TreeSet() {
        Object result = returnsEmptyValues.returnValueFor(TreeSet.class);
        assertTrue(result instanceof TreeSet);
    }

    @Test
    public void testReturnValueFor_LinkedHashSet() {
        Object result = returnsEmptyValues.returnValueFor(LinkedHashSet.class);
        assertTrue(result instanceof LinkedHashSet);
    }

    @Test
    public void testReturnValueFor_List() {
        Object result = returnsEmptyValues.returnValueFor(List.class);
        assertTrue(result instanceof LinkedList);
    }

    @Test
    public void testReturnValueFor_LinkedList() {
        Object result = returnsEmptyValues.returnValueFor(LinkedList.class);
        assertTrue(result instanceof LinkedList);
    }

    @Test
    public void testReturnValueFor_ArrayList() {
        Object result = returnsEmptyValues.returnValueFor(ArrayList.class);
        assertTrue(result instanceof ArrayList);
    }

    @Test
    public void testReturnValueFor_Map() {
        Object result = returnsEmptyValues.returnValueFor(Map.class);
        assertTrue(result instanceof HashMap);
    }

    @Test
    public void testReturnValueFor_HashMap() {
        Object result = returnsEmptyValues.returnValueFor(HashMap.class);
        assertTrue(result instanceof HashMap);
    }

    @Test
    public void testReturnValueFor_SortedMap() {
        Object result = returnsEmptyValues.returnValueFor(SortedMap.class);
        assertTrue(result instanceof TreeMap);
    }

    @Test
    public void testReturnValueFor_TreeMap() {
        Object result = returnsEmptyValues.returnValueFor(TreeMap.class);
        assertTrue(result instanceof TreeMap);
    }

    @Test
    public void testReturnValueFor_LinkedHashMap() {
        Object result = returnsEmptyValues.returnValueFor(LinkedHashMap.class);
        assertTrue(result instanceof LinkedHashMap);
    }

    @Test
    public void testReturnValueFor_otherTypes() {
        Object result = returnsEmptyValues.returnValueFor(Iterable.class);
        assertNull(result);

        result = returnsEmptyValues.returnValueFor(String.class);
        assertNull(result);

        result = returnsEmptyValues.returnValueFor(Integer[].class);
        assertNull(result);
    }

}