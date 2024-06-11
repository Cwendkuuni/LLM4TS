import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import org.mockito.stubbing.Answer;

public class ReturnsEmptyValuesTest {
    
    @Test
    public void testAnswerToString() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = createInvocation("toString");

        Object result = answer.answer(invocation);

        assertTrue(result instanceof String);
        assertNotNull(result);
    }
    
    @Test
    public void testAnswerCompareTo() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = createInvocation("compareTo");

        Object result = answer.answer(invocation);

        assertTrue(result instanceof Integer);
        assertNotNull(result);
    }
    
    @Test
    public void testReturnTypePrimitive() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        Class<?> returnType = int.class;

        Object result = answer.returnValueFor(returnType);

        assertTrue(result instanceof Integer);
        assertNotNull(result);
    }
    
    @Test
    public void testReturnTypeCollection() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        Class<?> returnType = Collection.class;

        Object result = answer.returnValueFor(returnType);

        assertTrue(result instanceof LinkedList);
        assertNotNull(result);
    }
    
    @Test
    public void testReturnTypeSet() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        Class<?> returnType = Set.class;

        Object result = answer.returnValueFor(returnType);

        assertTrue(result instanceof HashSet);
        assertNotNull(result);
    }
    
    @Test
    public void testReturnTypeHashSet() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        Class<?> returnType = HashSet.class;

        Object result = answer.returnValueFor(returnType);

        assertTrue(result instanceof HashSet);
        assertNotNull(result);
    }
    
    @Test
    public void testReturnTypeSortedSet() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        Class<?> returnType = SortedSet.class;

        Object result = answer.returnValueFor(returnType);

        assertTrue(result instanceof TreeSet);
        assertNotNull(result);
    }
    
    @Test
    public void testReturnTypeTreeSet() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        Class<?> returnType = TreeSet.class;

        Object result = answer.returnValueFor(returnType);

        assertTrue(result instanceof TreeSet);
        assertNotNull(result);
    }
    
    @Test
    public void testReturnTypeLinkedHashSet() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        Class<?> returnType = LinkedHashSet.class;

        Object result = answer.returnValueFor(returnType);

        assertTrue(result instanceof LinkedHashSet);
        assertNotNull(result);
    }
    
    @Test
    public void testReturnTypeList() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        Class<?> returnType = List.class;

        Object result = answer.returnValueFor(returnType);

        assertTrue(result instanceof LinkedList);
        assertNotNull(result);
    }
    
    @Test
    public void testReturnTypeLinkedList() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        Class<?> returnType = LinkedList.class;

        Object result = answer.returnValueFor(returnType);

        assertTrue(result instanceof LinkedList);
        assertNotNull(result);
    }
    
    @Test
    public void testReturnTypeArrayList() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        Class<?> returnType = ArrayList.class;

        Object result = answer.returnValueFor(returnType);

        assertTrue(result instanceof ArrayList);
        assertNotNull(result);
    }
    
    @Test
    public void testReturnTypeMap() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        Class<?> returnType = Map.class;

        Object result = answer.returnValueFor(returnType);

        assertTrue(result instanceof HashMap);
        assertNotNull(result);
    }
    
    @Test
    public void testReturnTypeHashMap() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        Class<?> returnType = HashMap.class;

        Object result = answer.returnValueFor(returnType);

        assertTrue(result instanceof HashMap);
        assertNotNull(result);
    }
    
    @Test
    public void testReturnTypeSortedMap() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        Class<?> returnType = SortedMap.class;

        Object result = answer.returnValueFor(returnType);

        assertTrue(result instanceof TreeMap);
        assertNotNull(result);
    }
    
    @Test
    public void testReturnTypeTreeMap() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        Class<?> returnType = TreeMap.class;

        Object result = answer.returnValueFor(returnType);

        assertTrue(result instanceof TreeMap);
        assertNotNull(result);
    }
    
    @Test
    public void testReturnTypeLinkedHashMap() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        Class<?> returnType = LinkedHashMap.class;

        Object result = answer.returnValueFor(returnType);

        assertTrue(result instanceof LinkedHashMap);
        assertNotNull(result);
    }
    
    private InvocationOnMock createInvocation(String methodName) {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Method method = mock(Method.class);

        when(invocation.getMethod()).thenReturn(method);
        when(method.getName()).thenReturn(methodName);

        return invocation;
    }
}