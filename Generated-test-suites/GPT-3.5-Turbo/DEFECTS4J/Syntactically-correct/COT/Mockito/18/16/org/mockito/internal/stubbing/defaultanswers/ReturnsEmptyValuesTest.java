import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import org.mockito.stubbing.Answer;

public class ReturnsEmptyValuesTest {
    
    private ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
    private ObjectMethodsGuru methodsGuru = new ObjectMethodsGuru();
    private MockUtil mockUtil = new MockUtil();
    
    /* Test case for answer() method */
    @Test
    public void testAnswer() {
        // Test for toString() method
        InvocationOnMock toStringInvocation = mock(InvocationOnMock.class);
        when(methodsGuru.isToString(toStringInvocation.getMethod())).thenReturn(true);
        Object mock = mock(Object.class);
        MockName name = mockUtil.getMockName(mock);
        when(name.isDefault()).thenReturn(true);
        when(mockUtil.getMockSettings(mock).getTypeToMock().getSimpleName()).thenReturn("Object");
        when(mock.hashCode()).thenReturn(1);
        assertEquals("Mock for Object, hashCode: 1", returnsEmptyValues.answer(toStringInvocation));
        when(name.isDefault()).thenReturn(false);
        when(name.toString()).thenReturn("Mock Object");
        assertEquals("Mock Object", returnsEmptyValues.answer(toStringInvocation));
        
        // Test for compareTo() method
        InvocationOnMock compareToInvocation = mock(InvocationOnMock.class);
        when(methodsGuru.isCompareToMethod(compareToInvocation.getMethod())).thenReturn(true);
        Object argument = mock(Object.class);
        when(argument).thenReturn(argument);
        assertEquals(0, returnsEmptyValues.answer(compareToInvocation));
        when(argument).thenReturn(mock(Object.class));
        assertEquals(1, returnsEmptyValues.answer(compareToInvocation));
        
        // Test for primitive and wrapper types
        InvocationOnMock returnTypeInvocation = mock(InvocationOnMock.class);
        Class<?> primitiveReturnType = int.class;
        Class<?> wrapperReturnType = Integer.class;
        when(returnTypeInvocation.getMethod().getReturnType()).thenReturn(primitiveReturnType);
        assertEquals(0, returnsEmptyValues.answer(returnTypeInvocation));
        when(returnTypeInvocation.getMethod().getReturnType()).thenReturn(wrapperReturnType);
        assertEquals(0, returnsEmptyValues.answer(returnTypeInvocation));
        
        // Test for Collection types
        InvocationOnMock collectionReturnTypeInvocation = mock(InvocationOnMock.class);
        when(collectionReturnTypeInvocation.getMethod().getReturnType()).thenReturn(Collection.class);
        assertEquals(new LinkedList<Object>(), returnsEmptyValues.answer(collectionReturnTypeInvocation));
        
        // Test for Set types
        InvocationOnMock setReturnTypeInvocation = mock(InvocationOnMock.class);
        when(setReturnTypeInvocation.getMethod().getReturnType()).thenReturn(Set.class);
        assertEquals(new HashSet<Object>(), returnsEmptyValues.answer(setReturnTypeInvocation));
        
        // Test for HashSet types
        InvocationOnMock hashSetReturnTypeInvocation = mock(InvocationOnMock.class);
        when(hashSetReturnTypeInvocation.getMethod().getReturnType()).thenReturn(HashSet.class);
        assertEquals(new HashSet<Object>(), returnsEmptyValues.answer(hashSetReturnTypeInvocation));
        
        // Test for SortedSet types
        InvocationOnMock sortedSetReturnTypeInvocation = mock(InvocationOnMock.class);
        when(sortedSetReturnTypeInvocation.getMethod().getReturnType()).thenReturn(SortedSet.class);
        assertEquals(new TreeSet<Object>(), returnsEmptyValues.answer(sortedSetReturnTypeInvocation));
        
        // Test for TreeSet types
        InvocationOnMock treeSetReturnTypeInvocation = mock(InvocationOnMock.class);
        when(treeSetReturnTypeInvocation.getMethod().getReturnType()).thenReturn(TreeSet.class);
        assertEquals(new TreeSet<Object>(), returnsEmptyValues.answer(treeSetReturnTypeInvocation));
        
        // Test for LinkedHashSet types
        InvocationOnMock linkedHashSetReturnTypeInvocation = mock(InvocationOnMock.class);
        when(linkedHashSetReturnTypeInvocation.getMethod().getReturnType()).thenReturn(LinkedHashSet.class);
        assertEquals(new LinkedHashSet<Object>(), returnsEmptyValues.answer(linkedHashSetReturnTypeInvocation));
        
        // Test for List types
        InvocationOnMock listReturnTypeInvocation = mock(InvocationOnMock.class);
        when(listReturnTypeInvocation.getMethod().getReturnType()).thenReturn(List.class);
        assertEquals(new LinkedList<Object>(), returnsEmptyValues.answer(listReturnTypeInvocation));
        
        // Test for LinkedList types
        InvocationOnMock linkedListReturnTypeInvocation = mock(InvocationOnMock.class);
        when(linkedListReturnTypeInvocation.getMethod().getReturnType()).thenReturn(LinkedList.class);
        assertEquals(new LinkedList<Object>(), returnsEmptyValues.answer(linkedListReturnTypeInvocation));
        
        // Test for ArrayList types
        InvocationOnMock arrayListReturnTypeInvocation = mock(InvocationOnMock.class);
        when(arrayListReturnTypeInvocation.getMethod().getReturnType()).thenReturn(ArrayList.class);
        assertEquals(new ArrayList<Object>(), returnsEmptyValues.answer(arrayListReturnTypeInvocation));
        
        // Test for Map types
        InvocationOnMock mapReturnTypeInvocation = mock(InvocationOnMock.class);
        when(mapReturnTypeInvocation.getMethod().getReturnType()).thenReturn(Map.class);
        assertEquals(new HashMap<Object, Object>(), returnsEmptyValues.answer(mapReturnTypeInvocation));
        
        // Test for HashMap types
        InvocationOnMock hashMapReturnTypeInvocation = mock(InvocationOnMock.class);
        when(hashMapReturnTypeInvocation.getMethod().getReturnType()).thenReturn(HashMap.class);
        assertEquals(new HashMap<Object, Object>(), returnsEmptyValues.answer(hashMapReturnTypeInvocation));
        
        // Test for SortedMap types
        InvocationOnMock sortedMapReturnTypeInvocation = mock(InvocationOnMock.class);
        when(sortedMapReturnTypeInvocation.getMethod().getReturnType()).thenReturn(SortedMap.class);
        assertEquals(new TreeMap<Object, Object>(), returnsEmptyValues.answer(sortedMapReturnTypeInvocation));
        
        // Test for TreeMap types
        InvocationOnMock treeMapReturnTypeInvocation = mock(InvocationOnMock.class);
        when(treeMapReturnTypeInvocation.getMethod().getReturnType()).thenReturn(TreeMap.class);
        assertEquals(new TreeMap<Object, Object>(), returnsEmptyValues.answer(treeMapReturnTypeInvocation));
        
        // Test for LinkedHashMap types
        InvocationOnMock linkedHashMapReturnTypeInvocation = mock(InvocationOnMock.class);
        when(linkedHashMapReturnTypeInvocation.getMethod().getReturnType()).thenReturn(LinkedHashMap.class);
        assertEquals(new LinkedHashMap<Object, Object>(), returnsEmptyValues.answer(linkedHashMapReturnTypeInvocation));
    }
}