import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import org.mockito.stubbing.Answer;
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
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.internal.util.Primitives;
import org.mockito.internal.util.MockUtil;

public class ReturnsEmptyValuesTest {
    
    private ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
    private ObjectMethodsGuru objectMethodsGuru = new ObjectMethodsGuru();
    private MockUtil mockUtil = new MockUtil();
    
    @Test
    public void testAnswerToStringDefault() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(objectMethodsGuru.isToString(invocation.getMethod())).thenReturn(true);
        Object mock = mock(Object.class);
        when(invocation.getMock()).thenReturn(mock);
        MockName name = mockUtil.getMockName(mock);
        when(name.isDefault()).thenReturn(true);
        when(mockUtil.getMockSettings(mock).getTypeToMock().getSimpleName()).thenReturn("Object");
        when(mock.hashCode()).thenReturn(123);
        
        String expected = "Mock for Object, hashCode: 123";
        assertEquals(expected, returnsEmptyValues.answer(invocation));
    }
    
    @Test
    public void testAnswerToStringNonDefault() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(objectMethodsGuru.isToString(invocation.getMethod())).thenReturn(true);
        Object mock = mock(Object.class);
        when(invocation.getMock()).thenReturn(mock);
        MockName name = mockUtil.getMockName(mock);
        when(name.isDefault()).thenReturn(false);
        when(name.toString()).thenReturn("MockObj");
        
        String expected = "MockObj";
        assertEquals(expected, returnsEmptyValues.answer(invocation));
    }
    
    @Test
    public void testAnswerCompareTo() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(objectMethodsGuru.isCompareToMethod(invocation.getMethod())).thenReturn(true);
        
        int expected = 1;
        assertEquals(expected, returnsEmptyValues.answer(invocation));
    }
    
    @Test
    public void testReturnValueForPrimitive() {
        Class<?> type = int.class;
        int expected = 0;
        assertEquals(expected, returnsEmptyValues.returnValueFor(type));
    }
    
    @Test
    public void testReturnValueForWrapperClass() {
        Class<?> type = Integer.class;
        int expected = 0;
        assertEquals(expected, returnsEmptyValues.returnValueFor(type));
    }
    
    @Test
    public void testReturnValueForCollection() {
        Class<?> type = Collection.class;
        Collection<Object> expected = new LinkedList<Object>();
        assertEquals(expected, returnsEmptyValues.returnValueFor(type));
    }
    
    @Test
    public void testReturnValueForSet() {
        Class<?> type = Set.class;
        Set<Object> expected = new HashSet<Object>();
        assertEquals(expected, returnsEmptyValues.returnValueFor(type));
    }
    
    @Test
    public void testReturnValueForHashSet() {
        Class<?> type = HashSet.class;
        Set<Object> expected = new HashSet<Object>();
        assertEquals(expected, returnsEmptyValues.returnValueFor(type));
    }
    
    @Test
    public void testReturnValueForSortedSet() {
        Class<?> type = SortedSet.class;
        Set<Object> expected = new TreeSet<Object>();
        assertEquals(expected, returnsEmptyValues.returnValueFor(type));
    }
    
    @Test
    public void testReturnValueForTreeSet() {
        Class<?> type = TreeSet.class;
        Set<Object> expected = new TreeSet<Object>();
        assertEquals(expected, returnsEmptyValues.returnValueFor(type));
    }
    
    @Test
    public void testReturnValueForLinkedHashSet() {
        Class<?> type = LinkedHashSet.class;
        Set<Object> expected = new LinkedHashSet<Object>();
        assertEquals(expected, returnsEmptyValues.returnValueFor(type));
    }
    
    @Test
    public void testReturnValueForList() {
        Class<?> type = List.class;
        List<Object> expected = new LinkedList<Object>();
        assertEquals(expected, returnsEmptyValues.returnValueFor(type));
    }
    
    @Test
    public void testReturnValueForLinkedList() {
        Class<?> type = LinkedList.class;
        List<Object> expected = new LinkedList<Object>();
        assertEquals(expected, returnsEmptyValues.returnValueFor(type));
    }
    
    @Test
    public void testReturnValueForArrayList() {
        Class<?> type = ArrayList.class;
        List<Object> expected = new ArrayList<Object>();
        assertEquals(expected, returnsEmptyValues.returnValueFor(type));
    }
    
    @Test
    public void testReturnValueForMap() {
        Class<?> type = Map.class;
        Map<Object, Object> expected = new HashMap<Object, Object>();
        assertEquals(expected, returnsEmptyValues.returnValueFor(type));
    }
    
    @Test
    public void testReturnValueForHashMap() {
        Class<?> type = HashMap.class;
        Map<Object, Object> expected = new HashMap<Object, Object>();
        assertEquals(expected, returnsEmptyValues.returnValueFor(type));
    }
    
    @Test
    public void testReturnValueForSortedMap() {
        Class<?> type = SortedMap.class;
        Map<Object, Object> expected = new TreeMap<Object, Object>();
        assertEquals(expected, returnsEmptyValues.returnValueFor(type));
    }
    
    @Test
    public void testReturnValueForTreeMap() {
        Class<?> type = TreeMap.class;
        Map<Object, Object> expected = new TreeMap<Object, Object>();
        assertEquals(expected, returnsEmptyValues.returnValueFor(type));
    }
    
    @Test
    public void testReturnValueForLinkedHashMap() {
        Class<?> type = LinkedHashMap.class;
        Map<Object, Object> expected = new LinkedHashMap<Object, Object>();
        assertEquals(expected, returnsEmptyValues.returnValueFor(type));
    }
    
    @Test
    public void testReturnValueForOtherTypes() {
        Class<?> type = Object.class;
        assertNull(returnsEmptyValues.returnValueFor(type));
    }
}