
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerToToStringMethod() {
        ReturnsEmptyValues rev = new ReturnsEmptyValues();
        
        InvocationOnMock mockInvocation = createMockInvocation("toString");
        assertEquals("Mock for Object, hashCode: " + System.identityHashCode(mockInvocation.getMock()), rev.answer(mockInvocation));
    }
    
    @Test
    public void testAnswerToCompareToMethod() {
        ReturnsEmptyValues rev = new ReturnsEmptyValues();
        
        InvocationOnMock mockInvocation = createMockInvocation("compareTo");
        assertEquals(1, rev.answer(mockInvocation));
    }
    
    @Test
    public void testAnswerToPrimitiveReturnType() {
        ReturnsEmptyValues rev = new ReturnsEmptyValues();
        
        InvocationOnMock mockInvocation = createMockInvocation("primitiveMethod");
        assertEquals(0, rev.answer(mockInvocation));
    }
    
    @Test
    public void testAnswerToPrimitiveWrapperReturnType() {
        ReturnsEmptyValues rev = new ReturnsEmptyValues();
        
        InvocationOnMock mockInvocation = createMockInvocation("wrapperMethod");
        assertEquals(0, rev.answer(mockInvocation));
    }
    
    @Test
    public void testAnswerToCollectionReturnType() {
        ReturnsEmptyValues rev = new ReturnsEmptyValues();
        
        InvocationOnMock mockInvocation = createMockInvocation("collectionMethod");
        assertTrue(rev.answer(mockInvocation) instanceof LinkedList);
    }
    
    @Test
    public void testAnswerToSetReturnType() {
        ReturnsEmptyValues rev = new ReturnsEmptyValues();
        
        InvocationOnMock mockInvocation = createMockInvocation("setMethod");
        assertTrue(rev.answer(mockInvocation) instanceof HashSet);
    }
    
    @Test
    public void testAnswerToHashSetReturnType() {
        ReturnsEmptyValues rev = new ReturnsEmptyValues();
        
        InvocationOnMock mockInvocation = createMockInvocation("hashSetMethod");
        assertTrue(rev.answer(mockInvocation) instanceof HashSet);
    }
    
    @Test
    public void testAnswerToSortedSetReturnType() {
        ReturnsEmptyValues rev = new ReturnsEmptyValues();
        
        InvocationOnMock mockInvocation = createMockInvocation("sortedSetMethod");
        assertTrue(rev.answer(mockInvocation) instanceof TreeSet);
    }
    
    @Test
    public void testAnswerToTreeSetReturnType() {
        ReturnsEmptyValues rev = new ReturnsEmptyValues();
        
        InvocationOnMock mockInvocation = createMockInvocation("treeSetMethod");
        assertTrue(rev.answer(mockInvocation) instanceof TreeSet);
    }
    
    @Test
    public void testAnswerToLinkedHashSetReturnType() {
        ReturnsEmptyValues rev = new ReturnsEmptyValues();
        
        InvocationOnMock mockInvocation = createMockInvocation("linkedHashSetMethod");
        assertTrue(rev.answer(mockInvocation) instanceof LinkedHashSet);
    }
    
    @Test
    public void testAnswerToListReturnType() {
        ReturnsEmptyValues rev = new ReturnsEmptyValues();
        
        InvocationOnMock mockInvocation = createMockInvocation("listMethod");
        assertTrue(rev.answer(mockInvocation) instanceof LinkedList);
    }
    
    @Test
    public void testAnswerToLinkedListReturnType() {
        ReturnsEmptyValues rev = new ReturnsEmptyValues();
        
        InvocationOnMock mockInvocation = createMockInvocation("linkedListMethod");
        assertTrue(rev.answer(mockInvocation) instanceof LinkedList);
    }
    
    @Test
    public void testAnswerToArrayListReturnType() {
        ReturnsEmptyValues rev = new ReturnsEmptyValues();
        
        InvocationOnMock mockInvocation = createMockInvocation("arrayListMethod");
        assertTrue(rev.answer(mockInvocation) instanceof ArrayList);
    }
    
    @Test
    public void testAnswerToMapReturnType() {
        ReturnsEmptyValues rev = new ReturnsEmptyValues();
        
        InvocationOnMock mockInvocation = createMockInvocation("mapMethod");
        assertTrue(rev.answer(mockInvocation) instanceof HashMap);
    }
    
    @Test
    public void testAnswerToHashMapReturnType() {
        ReturnsEmptyValues rev = new ReturnsEmptyValues();
        
        InvocationOnMock mockInvocation = createMockInvocation("hashMapMethod");
        assertTrue(rev.answer(mockInvocation) instanceof HashMap);
    }
    
    @Test
    public void testAnswerToSortedMapReturnType() {
        ReturnsEmptyValues rev = new ReturnsEmptyValues();
        
        InvocationOnMock mockInvocation = createMockInvocation("sortedMapMethod");
        assertTrue(rev.answer(mockInvocation) instanceof TreeMap);
    }
    
    @Test
    public void testAnswerToTreeMapReturnType() {
        ReturnsEmptyValues rev = new ReturnsEmptyValues();
        
        InvocationOnMock mockInvocation = createMockInvocation("treeMapMethod");
        assertTrue(rev.answer(mockInvocation) instanceof TreeMap);
    }
    
    @Test
    public void testAnswerToLinkedHashMapReturnType() {
        ReturnsEmptyValues rev = new ReturnsEmptyValues();
        
        InvocationOnMock mockInvocation = createMockInvocation("linkedHashMapMethod");
        assertTrue(rev.answer(mockInvocation) instanceof LinkedHashMap);
    }
    
    private InvocationOnMock createMockInvocation(String methodName) {
        InvocationOnMock mockInvocation = mock(InvocationOnMock.class);
        Method method = mock(Method.class);
        when(method.getName()).thenReturn(methodName);
        when(mockInvocation.getMethod()).thenReturn(method);
        
        return mockInvocation;
    }
}
