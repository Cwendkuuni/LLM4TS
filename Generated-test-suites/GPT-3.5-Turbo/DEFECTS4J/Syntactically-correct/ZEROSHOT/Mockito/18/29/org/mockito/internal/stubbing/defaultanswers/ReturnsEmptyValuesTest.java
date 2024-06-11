import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import java.util.Collection;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.LinkedHashMap;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
    
    @Test
    public void testToString() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        MockName mockName = mock(MockName.class);
        Object mock = new Object();
        when(invocation.getMethod()).thenReturn(new Object().getClass().getDeclaredMethods()[0]);
        when(mockUtil.getMockName(mock)).thenReturn(mockName);
        when(mockUtil.getMockSettings(mock).getTypeToMock()).thenReturn(new Object().getClass());
        when(name.isDefault()).thenReturn(true);
        when(mock.hashCode()).thenReturn(12345);
        assertEquals("Mock for Object, hashCode: 12345", returnsEmptyValues.answer(invocation));
    }
    
    @Test
    public void testCompareTo() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object mock = new Object();
        when(invocation.getMethod()).thenReturn(new Object().getClass().getDeclaredMethods()[1]);
        when(invocation.getMock()).thenReturn(mock);
        when(invocation.getArguments()[0]).thenReturn(mock);
        assertEquals(0, returnsEmptyValues.answer(invocation));
    }
    
    @Test
    public void testReturnValueForPrimitive() {
        Class<?> returnType = int.class;
        Object expected = 0;
        Object actual = returnsEmptyValues.returnValueFor(returnType);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testReturnValueForPrimitiveWrapperClass() {
        Class<?> returnType = Integer.class;
        Object expected = 0;
        Object actual = returnsEmptyValues.returnValueFor(returnType);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testReturnValueForCollection() {
        Class<?> returnType = Collection.class;
        Object expected = new LinkedList<Object>();
        Object actual = returnsEmptyValues.returnValueFor(returnType);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testReturnValueForSet() {
        Class<?> returnType = Set.class;
        Object expected = new HashSet<Object>();
        Object actual = returnsEmptyValues.returnValueFor(returnType);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testReturnValueForHashSet() {
        Class<?> returnType = HashSet.class;
        Object expected = new HashSet<Object>();
        Object actual = returnsEmptyValues.returnValueFor(returnType);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testReturnValueForSortedSet() {
        Class<?> returnType = SortedSet.class;
        Object expected = new TreeSet<Object>();
        Object actual = returnsEmptyValues.returnValueFor(returnType);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testReturnValueForTreeSet() {
        Class<?> returnType = TreeSet.class;
        Object expected = new TreeSet<Object>();
        Object actual = returnsEmptyValues.returnValueFor(returnType);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testReturnValueForLinkedHashSet() {
        Class<?> returnType = LinkedHashSet.class;
        Object expected = new LinkedHashSet<Object>();
        Object actual = returnsEmptyValues.returnValueFor(returnType);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testReturnValueForList() {
        Class<?> returnType = List.class;
        Object expected = new LinkedList<Object>();
        Object actual = returnsEmptyValues.returnValueFor(returnType);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testReturnValueForLinkedList() {
        Class<?> returnType = LinkedList.class;
        Object expected = new LinkedList<Object>();
        Object actual = returnsEmptyValues.returnValueFor(returnType);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testReturnValueForArrayList() {
        Class<?> returnType = ArrayList.class;
        Object expected = new ArrayList<Object>();
        Object actual = returnsEmptyValues.returnValueFor(returnType);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testReturnValueForMap() {
        Class<?> returnType = Map.class;
        Object expected = new HashMap<Object, Object>();
        Object actual = returnsEmptyValues.returnValueFor(returnType);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testReturnValueForHashMap() {
        Class<?> returnType = HashMap.class;
        Object expected = new HashMap<Object, Object>();
        Object actual = returnsEmptyValues.returnValueFor(returnType);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testReturnValueForSortedMap() {
        Class<?> returnType = SortedMap.class;
        Object expected = new TreeMap<Object, Object>();
        Object actual = returnsEmptyValues.returnValueFor(returnType);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testReturnValueForTreeMap() {
        Class<?> returnType = TreeMap.class;
        Object expected = new TreeMap<Object, Object>();
        Object actual = returnsEmptyValues.returnValueFor(returnType);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testReturnValueForLinkedHashMap() {
        Class<?> returnType = LinkedHashMap.class;
        Object expected = new LinkedHashMap<Object, Object>();
        Object actual = returnsEmptyValues.returnValueFor(returnType);
        assertEquals(expected, actual);
    }
    
}