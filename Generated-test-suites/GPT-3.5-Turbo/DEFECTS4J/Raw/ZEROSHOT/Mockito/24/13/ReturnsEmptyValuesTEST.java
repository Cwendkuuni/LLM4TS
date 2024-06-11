import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.Primitives;
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

    private ReturnsEmptyValues returnsEmptyValues;
    
    @Before
    public void setUp() {
        returnsEmptyValues = new ReturnsEmptyValues();
    }

    @Test
    public void testToStringReturnsMockDescription() {
        InvocationOnMock invocation = createInvocation("toString");
        Object result = returnsEmptyValues.answer(invocation);
        
        Object mock = invocation.getMock();
        MockName name = MockUtil.getMockName(mock);
        String expected = "Mock for " + MockUtil.getMockSettings(mock).getTypeToMock().getSimpleName() + ", hashCode: " + mock.hashCode();
        
        assertEquals(expected, result);
    }
    
    @Test
    public void testCompareToReturnsNonZeroValue() {
        InvocationOnMock invocation = createInvocation("compareTo");
        Object result = returnsEmptyValues.answer(invocation);
        
        assertEquals(1, result);
    }
    
    @Test
    public void testReturnValueForPrimitiveReturnsDefaultValue() {
        Class<?> type = int.class;
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testReturnValueForWrapperClassReturnsConsistentValue() {
        Class<?> type = Integer.class;
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertEquals(0, result);
    }
    
    @Test
    public void testReturnValueForCollectionReturnsEmptyLinkedList() {
        Class<?> type = Collection.class;
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertTrue(result instanceof LinkedList<?>);
        assertTrue(((LinkedList<?>) result).isEmpty());
    }
    
    @Test
    public void testReturnValueForSetReturnsEmptyHashSet() {
        Class<?> type = Set.class;
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertTrue(result instanceof HashSet<?>);
        assertTrue(((HashSet<?>) result).isEmpty());
    }
    
    @Test
    public void testReturnValueForHashSetReturnsEmptyHashSet() {
        Class<?> type = HashSet.class;
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertTrue(result instanceof HashSet<?>);
        assertTrue(((HashSet<?>) result).isEmpty());
    }
    
    @Test
    public void testReturnValueForSortedSetReturnsEmptyTreeSet() {
        Class<?> type = SortedSet.class;
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertTrue(result instanceof TreeSet<?>);
        assertTrue(((TreeSet<?>) result).isEmpty());
    }
    
    @Test
    public void testReturnValueForTreeSetReturnsEmptyTreeSet() {
        Class<?> type = TreeSet.class;
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertTrue(result instanceof TreeSet<?>);
        assertTrue(((TreeSet<?>) result).isEmpty());
    }
    
    @Test
    public void testReturnValueForLinkedHashSetReturnsEmptyLinkedHashSet() {
        Class<?> type = LinkedHashSet.class;
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertTrue(result instanceof LinkedHashSet<?>);
        assertTrue(((LinkedHashSet<?>) result).isEmpty());
    }
    
    @Test
    public void testReturnValueForListReturnsEmptyLinkedList() {
        Class<?> type = List.class;
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertTrue(result instanceof LinkedList<?>);
        assertTrue(((LinkedList<?>) result).isEmpty());
    }
    
    @Test
    public void testReturnValueForLinkedListReturnsEmptyLinkedList() {
        Class<?> type = LinkedList.class;
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertTrue(result instanceof LinkedList<?>);
        assertTrue(((LinkedList<?>) result).isEmpty());
    }
    
    @Test
    public void testReturnValueForArrayListReturnsEmptyArrayList() {
        Class<?> type = ArrayList.class;
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertTrue(result instanceof ArrayList<?>);
        assertTrue(((ArrayList<?>) result).isEmpty());
    }
    
    @Test
    public void testReturnValueForMapReturnsEmptyHashMap() {
        Class<?> type = Map.class;
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertTrue(result instanceof HashMap<?, ?>);
        assertTrue(((HashMap<?, ?>) result).isEmpty());
    }
    
    @Test
    public void testReturnValueForHashMapReturnsEmptyHashMap() {
        Class<?> type = HashMap.class;
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertTrue(result instanceof HashMap<?, ?>);
        assertTrue(((HashMap<?, ?>) result).isEmpty());
    }
    
    @Test
    public void testReturnValueForSortedMapReturnsEmptyTreeMap() {
        Class<?> type = SortedMap.class;
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertTrue(result instanceof TreeMap<?, ?>);
        assertTrue(((TreeMap<?, ?>) result).isEmpty());
    }
    
    @Test
    public void testReturnValueForTreeMapReturnsEmptyTreeMap() {
        Class<?> type = TreeMap.class;
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertTrue(result instanceof TreeMap<?, ?>);
        assertTrue(((TreeMap<?, ?>) result).isEmpty());
    }
    
    @Test
    public void testReturnValueForLinkedHashMapReturnsEmptyLinkedHashMap() {
        Class<?> type = LinkedHashMap.class;
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertTrue(result instanceof LinkedHashMap<?, ?>);
        assertTrue(((LinkedHashMap<?, ?>) result).isEmpty());
    }
    
    @Test
    public void testReturnValueForOtherTypesReturnsNull() {
        Class<?> type = Object.class;
        Object result = returnsEmptyValues.returnValueFor(type);
        
        assertNull(result);
    }
    
    private InvocationOnMock createInvocation(String methodName) {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(getMethodByName(methodName));
        when(invocation.getMock()).thenReturn(mock(MyMockedClass.class));
        
        return invocation;
    }
    
    private Method getMethodByName(String methodName) {
        Method[] methods = MyMockedClass.class.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }
        return null;
    }
    
    private static class MyMockedClass {
        public void toString() {}
        public int compareTo(Object other) { return 0; }
    }
}