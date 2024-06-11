
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsEmptyValuesTest {
    private ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
    
    @Test
    public void testAnswerToStringDefaultName() {
        // Given
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(String.class.getMethod("toString"));
        when(invocation.getMock()).thenReturn(mock(Object.class));
        
        // When
        Object result = returnsEmptyValues.answer(invocation);
        
        // Then
        assertEquals("Mock for Object, hashCode: 0", result);
    }
    
    @Test
    public void testAnswerToStringCustomName() {
        // Given
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(String.class.getMethod("toString"));
        when(invocation.getMock()).thenReturn(mock(Object.class));
        MockName name = mock(MockName.class);
        when(name.isDefault()).thenReturn(false);
        when(name.toString()).thenReturn("Custom Mock");
        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockName(any())).thenReturn(name);
        
        // When
        Object result = returnsEmptyValues.answer(invocation);
        
        // Then
        assertEquals("Custom Mock", result);
    }
    
    @Test
    public void testAnswerCompareToSameReference() {
        // Given
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Comparable.class.getMethod("compareTo", Object.class));
        Object mock = mock(Object.class);
        when(invocation.getArguments()).thenReturn(new Object[]{mock});
        when(invocation.getMock()).thenReturn(mock);
        
        // When
        Object result = returnsEmptyValues.answer(invocation);
        
        // Then
        assertEquals(0, result);
    }
    
    @Test
    public void testAnswerCompareToDifferentReference() {
        // Given
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Comparable.class.getMethod("compareTo", Object.class));
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        when(invocation.getArguments()).thenReturn(new Object[]{mock2});
        when(invocation.getMock()).thenReturn(mock1);
        
        // When
        Object result = returnsEmptyValues.answer(invocation);
        
        // Then
        assertEquals(1, result);
    }
    
    @Test
    public void testAnswerReturnTypePrimitive() {
        // Given
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Primitives.class.getMethod("defaultValueForPrimitiveOrWrapper", Class.class));
        when(invocation.getMethod().getReturnType()).thenReturn(int.class);
        
        // When
        Object result = returnsEmptyValues.answer(invocation);
        
        // Then
        assertEquals(0, result);
    }
    
    @Test
    public void testAnswerReturnTypeWrapperClass() {
        // Given
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Primitives.class.getMethod("defaultValueForPrimitiveOrWrapper", Class.class));
        when(invocation.getMethod().getReturnType()).thenReturn(Integer.class);
        
        // When
        Object result = returnsEmptyValues.answer(invocation);
        
        // Then
        assertEquals(0, result);
    }
    
    @Test
    public void testAnswerReturnTypeCollection() {
        // Given
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        when(invocation.getMethod().getReturnType()).thenReturn(Collection.class);
        
        // When
        Object result = returnsEmptyValues.answer(invocation);
        
        // Then
        assertTrue(result instanceof LinkedList);
        assertTrue(((LinkedList)result).isEmpty());
    }
    
    @Test
    public void testAnswerReturnTypeSet() {
        // Given
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        when(invocation.getMethod().getReturnType()).thenReturn(Set.class);
        
        // When
        Object result = returnsEmptyValues.answer(invocation);
        
        // Then
        assertTrue(result instanceof HashSet);
        assertTrue(((HashSet)result).isEmpty());
    }
    
    @Test
    public void testAnswerReturnTypeHashSet() {
        // Given
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        when(invocation.getMethod().getReturnType()).thenReturn(HashSet.class);
        
        // When
        Object result = returnsEmptyValues.answer(invocation);
        
        // Then
        assertTrue(result instanceof HashSet);
        assertTrue(((HashSet)result).isEmpty());
    }
    
    @Test
    public void testAnswerReturnTypeSortedSet() {
        // Given
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        when(invocation.getMethod().getReturnType()).thenReturn(SortedSet.class);
        
        // When
        Object result = returnsEmptyValues.answer(invocation);
        
        // Then
        assertTrue(result instanceof TreeSet);
        assertTrue(((TreeSet)result).isEmpty());
    }
    
    @Test
    public void testAnswerReturnTypeTreeSet() {
        // Given
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        when(invocation.getMethod().getReturnType()).thenReturn(TreeSet.class);
        
        // When
        Object result = returnsEmptyValues.answer(invocation);
        
        // Then
        assertTrue(result instanceof TreeSet);
        assertTrue(((TreeSet)result).isEmpty());
    }
    
    @Test
    public void testAnswerReturnTypeLinkedHashSet() {
        // Given
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        when(invocation.getMethod().getReturnType()).thenReturn(LinkedHashSet.class);
        
        // When
        Object result = returnsEmptyValues.answer(invocation);
        
        // Then
        assertTrue(result instanceof LinkedHashSet);
        assertTrue(((LinkedHashSet)result).isEmpty());
    }
    
    @Test
    public void testAnswerReturnTypeList() {
        // Given
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        when(invocation.getMethod().getReturnType()).thenReturn(List.class);
        
        // When
        Object result = returnsEmptyValues.answer(invocation);
        
        // Then
        assertTrue(result instanceof LinkedList);
        assertTrue(((LinkedList)result).isEmpty());
    }
    
    @Test
    public void testAnswerReturnTypeLinkedList() {
        // Given
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        when(invocation.getMethod().getReturnType()).thenReturn(LinkedList.class);
        
        // When
        Object result = returnsEmptyValues.answer(invocation);
        
        // Then
        assertTrue(result instanceof LinkedList);
        assertTrue(((LinkedList)result).isEmpty());
    }
    
    @Test
    public void testAnswerReturnTypeArrayList() {
        // Given
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        when(invocation.getMethod().getReturnType()).thenReturn(ArrayList.class);
        
        // When
        Object result = returnsEmptyValues.answer(invocation);
        
        // Then
        assertTrue(result instanceof ArrayList);
        assertTrue(((ArrayList)result).isEmpty());
    }
    
    @Test
    public void testAnswerReturnTypeMap() {
        // Given
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        when(invocation.getMethod().getReturnType()).thenReturn(Map.class);
        
        // When
        Object result = returnsEmptyValues.answer(invocation);
        
        // Then
        assertTrue(result instanceof HashMap);
        assertTrue(((HashMap)result).isEmpty());
    }
    
    @Test
    public void testAnswerReturnTypeHashMap() {
        // Given
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        when(invocation.getMethod().getReturnType()).thenReturn(HashMap.class);
        
        // When
        Object result = returnsEmptyValues.answer(invocation);
        
        // Then
        assertTrue(result instanceof HashMap);
        assertTrue(((HashMap)result).isEmpty());
    }
    
    @Test
    public void testAnswerReturnTypeSortedMap() {
        // Given
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        when(invocation.getMethod().getReturnType()).thenReturn(SortedMap.class);
        
        // When
        Object result = returnsEmptyValues.answer(invocation);
        
        // Then
        assertTrue(result instanceof TreeMap);
        assertTrue(((TreeMap)result).isEmpty());
    }
    
    @Test
    public void testAnswerReturnTypeTreeMap() {
        // Given
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        when(invocation.getMethod().getReturnType()).thenReturn(TreeMap.class);
        
        // When
        Object result = returnsEmptyValues.answer(invocation);
        
        // Then
        assertTrue(result instanceof TreeMap);
        assertTrue(((TreeMap)result).isEmpty());
    }
    
    @Test
    public void testAnswerReturnTypeLinkedHashMap() {
        // Given
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        when(invocation.getMethod().getReturnType()).thenReturn(LinkedHashMap.class);
        
        // When
        Object result = returnsEmptyValues.answer(invocation);
        
        // Then
        assertTrue(result instanceof LinkedHashMap);
        assertTrue(((LinkedHashMap)result).isEmpty());
    }
}
