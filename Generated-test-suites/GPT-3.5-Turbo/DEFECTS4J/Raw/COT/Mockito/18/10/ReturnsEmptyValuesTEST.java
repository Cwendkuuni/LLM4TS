import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsEmptyValuesTest {

    @Test
    public void testToString() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertEquals("Mock for Object, hashCode: " + invocation.getMock().hashCode(), result);
    }
    
    @Test
    public void testCompareTo() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Comparable.class.getMethod("compareTo", Object.class));
        when(invocation.getMock()).thenReturn(invocation.getArguments()[0]);

        Object result = returnsEmptyValues.answer(invocation);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testReturnValueFor_primitives() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        
        assertEquals(0, returnsEmptyValues.returnValueFor(int.class));
        assertEquals(0L, returnsEmptyValues.returnValueFor(long.class));
        assertEquals(0.0f, returnsEmptyValues.returnValueFor(float.class), 0.0);
        assertEquals(0.0, returnsEmptyValues.returnValueFor(double.class), 0.0);
        assertEquals(false, returnsEmptyValues.returnValueFor(boolean.class));
        assertNull(returnsEmptyValues.returnValueFor(void.class));
        assertNull(returnsEmptyValues.returnValueFor(Integer.class));
    }
    
    @Test
    public void testReturnValueFor_Collections() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        
        LinkedList<Object> emptyLinkedList = new LinkedList<>();
        HashSet<Object> emptyHashSet = new HashSet<>();
        TreeSet<Object> emptyTreeSet = new TreeSet<>();
        LinkedHashSet<Object> emptyLinkedHashSet = new LinkedHashSet<>();
        ArrayList<Object> emptyArrayList = new ArrayList<>();
        HashMap<Object, Object> emptyHashMap = new HashMap<>();
        TreeMap<Object, Object> emptyTreeMap = new TreeMap<>();
        LinkedHashMap<Object, Object> emptyLinkedHashMap = new LinkedHashMap<>();
        
        assertEquals(emptyLinkedList, returnsEmptyValues.returnValueFor(Collection.class));
        assertEquals(emptyHashSet, returnsEmptyValues.returnValueFor(Set.class));
        assertEquals(emptyHashSet, returnsEmptyValues.returnValueFor(HashSet.class));
        assertEquals(emptyTreeSet, returnsEmptyValues.returnValueFor(SortedSet.class));
        assertEquals(emptyTreeSet, returnsEmptyValues.returnValueFor(TreeSet.class));
        assertEquals(emptyLinkedHashSet, returnsEmptyValues.returnValueFor(LinkedHashSet.class));
        assertEquals(emptyLinkedList, returnsEmptyValues.returnValueFor(List.class));
        assertEquals(emptyLinkedList, returnsEmptyValues.returnValueFor(LinkedList.class));
        assertEquals(emptyArrayList, returnsEmptyValues.returnValueFor(ArrayList.class));
        assertEquals(emptyHashMap, returnsEmptyValues.returnValueFor(Map.class));
        assertEquals(emptyHashMap, returnsEmptyValues.returnValueFor(HashMap.class));
        assertEquals(emptyTreeMap, returnsEmptyValues.returnValueFor(SortedMap.class));
        assertEquals(emptyTreeMap, returnsEmptyValues.returnValueFor(TreeMap.class));
        assertEquals(emptyLinkedHashMap, returnsEmptyValues.returnValueFor(LinkedHashMap.class));
    }

    @Test
    public void testReturnValueFor_otherTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        
        assertNull(returnsEmptyValues.returnValueFor(String.class));
        assertNull(returnsEmptyValues.returnValueFor(Integer.class));
        assertNull(returnsEmptyValues.returnValueFor(Object.class));
        assertNull(returnsEmptyValues.returnValueFor(ReturnsEmptyValues.class));
    }
}