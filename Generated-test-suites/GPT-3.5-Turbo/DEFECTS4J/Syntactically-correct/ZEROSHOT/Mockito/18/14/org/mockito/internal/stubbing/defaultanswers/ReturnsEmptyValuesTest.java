import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import org.mockito.stubbing.Answer;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;

import java.util.*;

import static org.junit.Assert.*;

public class ReturnsEmptyValuesTest {
    
    private ReturnsEmptyValues returnEmptyValues;

    @Before
    public void setUp() {
        returnEmptyValues = new ReturnsEmptyValues();
    }

    @Test
    public void testAnswer_toStringMethod() {
        InvocationOnMock invocation = createInvocation("toString", Object.class);
        
        Object result = returnEmptyValues.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof String);
    }

    @Test
    public void testAnswer_compareToMethod() {
        InvocationOnMock invocation = createInvocation("compareTo", Object.class);
        
        Object result = returnEmptyValues.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof Integer);
    }

    @Test
    public void testAnswer_primitiveType() {
        InvocationOnMock invocation = createInvocation("primitiveMethod", int.class);
        
        Object result = returnEmptyValues.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof Integer);
    }

    @Test
    public void testAnswer_primitiveWrapperType() {
        InvocationOnMock invocation = createInvocation("wrapperMethod", Integer.class);
        
        Object result = returnEmptyValues.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof Integer);
    }

    @Test
    public void testAnswer_collectionType() {
        InvocationOnMock invocation = createInvocation("listMethod", List.class);
        
        Object result = returnEmptyValues.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof LinkedList);
    }

    @Test
    public void testAnswer_setType() {
        InvocationOnMock invocation = createInvocation("setMethod", Set.class);
        
        Object result = returnEmptyValues.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof HashSet);
    }

    @Test
    public void testAnswer_hashSetType() {
        InvocationOnMock invocation = createInvocation("hashSetMethod", HashSet.class);
        
        Object result = returnEmptyValues.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof HashSet);
    }

    @Test
    public void testAnswer_sortedSetType() {
        InvocationOnMock invocation = createInvocation("sortedSetMethod", SortedSet.class);
        
        Object result = returnEmptyValues.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof TreeSet);
    }

    @Test
    public void testAnswer_treeSetType() {
        InvocationOnMock invocation = createInvocation("treeSetMethod", TreeSet.class);
        
        Object result = returnEmptyValues.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof TreeSet);
    }

    @Test
    public void testAnswer_linkedHashSetType() {
        InvocationOnMock invocation = createInvocation("linkedHashSetMethod", LinkedHashSet.class);
        
        Object result = returnEmptyValues.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof LinkedHashSet);
    }

    @Test
    public void testAnswer_listType() {
        InvocationOnMock invocation = createInvocation("listMethod", List.class);
        
        Object result = returnEmptyValues.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof LinkedList);
    }

    @Test
    public void testAnswer_linkedListType() {
        InvocationOnMock invocation = createInvocation("linkedListMethod", LinkedList.class);
        
        Object result = returnEmptyValues.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof LinkedList);
    }

    @Test
    public void testAnswer_arrayListType() {
        InvocationOnMock invocation = createInvocation("arrayListMethod", ArrayList.class);
        
        Object result = returnEmptyValues.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof ArrayList);
    }

    @Test
    public void testAnswer_mapType() {
        InvocationOnMock invocation = createInvocation("mapMethod", Map.class);
        
        Object result = returnEmptyValues.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof HashMap);
    }

    @Test
    public void testAnswer_hashMapType() {
        InvocationOnMock invocation = createInvocation("hashMapMethod", HashMap.class);
        
        Object result = returnEmptyValues.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof HashMap);
    }

    @Test
    public void testAnswer_sortedMapType() {
        InvocationOnMock invocation = createInvocation("sortedMapMethod", SortedMap.class);
        
        Object result = returnEmptyValues.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof TreeMap);
    }

    @Test
    public void testAnswer_treeMapType() {
        InvocationOnMock invocation = createInvocation("treeMapMethod", TreeMap.class);
        
        Object result = returnEmptyValues.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof TreeMap);
    }

    @Test
    public void testAnswer_linkedHashMapType() {
        InvocationOnMock invocation = createInvocation("linkedHashMapMethod", LinkedHashMap.class);
        
        Object result = returnEmptyValues.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof LinkedHashMap);
    }

    private InvocationOnMock createInvocation(String methodName, Class<?> returnType) {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(getMethod(methodName));
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(invocation.getArguments()).thenReturn(new Object[]{});
        when(invocation.getMethod().getReturnType()).thenReturn(returnType);
        return invocation;
    }

    private java.lang.reflect.Method getMethod(String methodName) {
        try {
            return ReturnsEmptyValues.class.getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}