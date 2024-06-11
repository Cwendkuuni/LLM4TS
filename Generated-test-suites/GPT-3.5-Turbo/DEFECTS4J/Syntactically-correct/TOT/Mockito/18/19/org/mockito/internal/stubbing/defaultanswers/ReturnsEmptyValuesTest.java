import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues returnsEmptyValues;

    @Before
    public void setUp() {
        returnsEmptyValues = new ReturnsEmptyValues();
    }

    @Test
    public void testAnswer_toString() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> answer = returnsEmptyValues;
        
        // Set up mock
        Object mock = new Object();
        invocationMock(invocation, mock);
        
        // Set up method
        mockToStringMethod(invocation, true);
        
        // Perform test
        Object result = answer.answer(invocation);
        
        // Verify result
        assertEquals("Mock for Object, hashCode: " + mock.hashCode(), result);
    }

    @Test
    public void testAnswer_compareTo_sameReference() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> answer = returnsEmptyValues;
        
        // Set up mock
        Object mock = new Object();
        invocationMock(invocation, mock);
        
        // Set up method
        mockCompareToMethod(invocation, false, 0);
        
        // Perform test
        Object result = answer.answer(invocation);
        
        // Verify result
        assertEquals(0, result);
    }

    @Test
    public void testAnswer_compareTo_differentReference() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> answer = returnsEmptyValues;
        
        // Set up mock
        Object mock = new Object();
        invocationMock(invocation, mock);
        
        // Set up method
        mockCompareToMethod(invocation, true, 1);
        
        // Perform test
        Object result = answer.answer(invocation);
        
        // Verify result
        assertEquals(1, result);
    }

    @Test
    public void testAnswer_returnValueFor_primitive() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> answer = returnsEmptyValues;
        
        // Set up method
        returnTypeMethod(invocation, int.class);
        
        // Perform test
        Object result = answer.answer(invocation);
        
        // Verify result
        assertEquals(0, result);
    }

    @Test
    public void testAnswer_returnValueFor_wrapper() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> answer = returnsEmptyValues;
        
        // Set up method
        returnTypeMethod(invocation, Integer.class);
        
        // Perform test
        Object result = answer.answer(invocation);
        
        // Verify result
        assertEquals(0, result);
    }

    @Test
    public void testAnswer_returnValueFor_Collection() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> answer = returnsEmptyValues;
        
        // Set up method
        returnTypeMethod(invocation, Collection.class);
        
        // Perform test
        Object result = answer.answer(invocation);
        
        // Verify result
        assertEquals(new LinkedList<Object>(), result);
    }

    @Test
    public void testAnswer_returnValueFor_Set() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> answer = returnsEmptyValues;
        
        // Set up method
        returnTypeMethod(invocation, Set.class);
        
        // Perform test
        Object result = answer.answer(invocation);
        
        // Verify result
        assertEquals(new HashSet<Object>(), result);
    }

    @Test
    public void testAnswer_returnValueFor_HashSet() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> answer = returnsEmptyValues;
        
        // Set up method
        returnTypeMethod(invocation, HashSet.class);
        
        // Perform test
        Object result = answer.answer(invocation);
        
        // Verify result
        assertEquals(new HashSet<Object>(), result);
    }

    @Test
    public void testAnswer_returnValueFor_SortedSet() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> answer = returnsEmptyValues;
        
        // Set up method
        returnTypeMethod(invocation, SortedSet.class);
        
        // Perform test
        Object result = answer.answer(invocation);
        
        // Verify result
        assertEquals(new TreeSet<Object>(), result);
    }

    @Test
    public void testAnswer_returnValueFor_TreeSet() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> answer = returnsEmptyValues;
        
        // Set up method
        returnTypeMethod(invocation, TreeSet.class);
        
        // Perform test
        Object result = answer.answer(invocation);
        
        // Verify result
        assertEquals(new TreeSet<Object>(), result);
    }

    @Test
    public void testAnswer_returnValueFor_LinkedHashSet() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> answer = returnsEmptyValues;
        
        // Set up method
        returnTypeMethod(invocation, LinkedHashSet.class);
        
        // Perform test
        Object result = answer.answer(invocation);
        
        // Verify result
        assertEquals(new LinkedHashSet<Object>(), result);
    }

    @Test
    public void testAnswer_returnValueFor_List() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> answer = returnsEmptyValues;
        
        // Set up method
        returnTypeMethod(invocation, List.class);
        
        // Perform test
        Object result = answer.answer(invocation);
        
        // Verify result
        assertEquals(new LinkedList<Object>(), result);
    }

    @Test
    public void testAnswer_returnValueFor_LinkedList() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> answer = returnsEmptyValues;
        
        // Set up method
        returnTypeMethod(invocation, LinkedList.class);
        
        // Perform test
        Object result = answer.answer(invocation);
        
        // Verify result
        assertEquals(new LinkedList<Object>(), result);
    }

    @Test
    public void testAnswer_returnValueFor_ArrayList() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> answer = returnsEmptyValues;
        
        // Set up method
        returnTypeMethod(invocation, ArrayList.class);
        
        // Perform test
        Object result = answer.answer(invocation);
        
        // Verify result
        assertEquals(new ArrayList<Object>(), result);
    }

    @Test
    public void testAnswer_returnValueFor_Map() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> answer = returnsEmptyValues;
        
        // Set up method
        returnTypeMethod(invocation, Map.class);
        
        // Perform test
        Object result = answer.answer(invocation);
        
        // Verify result
        assertEquals(new HashMap<Object, Object>(), result);
    }

    @Test
    public void testAnswer_returnValueFor_HashMap() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> answer = returnsEmptyValues;
        
        // Set up method
        returnTypeMethod(invocation, HashMap.class);
        
        // Perform test
        Object result = answer.answer(invocation);
        
        // Verify result
        assertEquals(new HashMap<Object, Object>(), result);
    }

    @Test
    public void testAnswer_returnValueFor_SortedMap() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> answer = returnsEmptyValues;
        
        // Set up method
        returnTypeMethod(invocation, SortedMap.class);
        
        // Perform test
        Object result = answer.answer(invocation);
        
        // Verify result
        assertEquals(new TreeMap<Object, Object>(), result);
    }

    @Test
    public void testAnswer_returnValueFor_TreeMap() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> answer = returnsEmptyValues;
        
        // Set up method
        returnTypeMethod(invocation, TreeMap.class);
        
        // Perform test
        Object result = answer.answer(invocation);
        
        // Verify result
        assertEquals(new TreeMap<Object, Object>(), result);
    }

    @Test
    public void testAnswer_returnValueFor_LinkedHashMap() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> answer = returnsEmptyValues;
        
        // Set up method
        returnTypeMethod(invocation, LinkedHashMap.class);
        
        // Perform test
        Object result = answer.answer(invocation);
        
        // Verify result
        assertEquals(new LinkedHashMap<Object, Object>(), result);
    }

    private void invocationMock(InvocationOnMock invocation, Object mock) {
        mockObject(invocation, mock, false);
    }
    
    private void invocationSpy(InvocationOnMock invocation, Object mock) {
        mockObject(invocation, mock, true);
    }
    
    private void mockObject(InvocationOnMock invocation, Object mock, boolean spied) {
        ObjectMethodsGuru methodsGuru = mock(ObjectMethodsGuru.class);
        
        // Set up methodsGuru.isToString()
        if (invocation.getMethod().equals(methodsGuru.getClass().getMethod("isToString", java.lang.reflect.Method.class))) {
            if (!spied) {
                invocationSpied(invocation, methodsGuru, false);
            } else {
                invocationSpied(invocation, methodsGuru, true);
            }
        }
        
        // Set up methodsGuru.isCompareToMethod()
        if (invocation.getMethod().equals(methodsGuru.getClass().getMethod("isCompareToMethod", java.lang.reflect.Method.class))) {
            if (!spied) {
                invocationSpied(invocation, methodsGuru, false);
            } else {
                invocationSpied(invocation, methodsGuru, true);
            }
        }

        // Set up invocation.getMock()
        if (invocation.getMethod().equals(invocation.getClass().getMethod("getMock"))) {
            if (!spied) {
                invocationReturnValue(invocation, mock);
            } else {
                invocationReturnValue(invocation, mock);
            }
        }
    }
    
    private void invocationSpied(InvocationOnMock invocation, Object mock, boolean spyOn) {
        if (spyOn) {
            invocationReturnValue(invocation, true);
        } else {
            invocationReturnValue(invocation, false);
        }
    }
    
    private void invocationReturnValue(InvocationOnMock invocation, Object value) {
        try {
            invocation.getClass().getMethod("getMock").invoke(invocation, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void invocationReturnValue(InvocationOnMock invocation, boolean value) {
        try {
            invocation.getClass().getMethod("getMock").invoke(invocation, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void mockToStringMethod(InvocationOnMock invocation, boolean isToString) {
        ObjectMethodsGuru methodsGuru = mock(ObjectMethodsGuru.class);
        
        // Set up methodsGuru.isToString()
        if (invocation.getMethod().equals(methodsGuru.getClass().getMethod("isToString", java.lang.reflect.Method.class))) {
            invocationReturnValue(invocation, isToString);
        }
    }
    
    private void mockCompareToMethod(InvocationOnMock invocation, boolean isCompareToMethod, int argumentIndex) {
        ObjectMethodsGuru methodsGuru = mock(ObjectMethodsGuru.class);
        
        // Set up methodsGuru.isCompareToMethod()
        if (invocation.getMethod().equals(methodsGuru.getClass().getMethod("isCompareToMethod", java.lang.reflect.Method.class))) {
            invocationReturnValue(invocation, isCompareToMethod);
        }
        
        // Set up invocation.getArguments()
        if (invocation.getMethod().equals(invocation.getClass().getMethod("getArguments"))) {
            invocationComparedObject(invocation, argumentIndex);
        }
    }
    
    private void returnTypeMethod(InvocationOnMock invocation, Class<?> returnType) {
        // Set up invocation.getMethod()
        if (invocation.getMethod().equals(invocation.getClass().getMethod("getMethod"))) {
            mockMethod(invocation, returnType);
        }
    }
    
    private void mockMethod(InvocationOnMock invocation, Class<?> returnType) {
        java.lang.reflect.Method method = mock(java.lang.reflect.Method.class);
        
        // Set up method.getReturnType()
        if (invocation.getMethod().equals(method.getClass().getMethod("getReturnType"))) {
            returnType(invocation, returnType);
        }
    }
    
    private void returnType(InvocationOnMock invocation, Class<?> returnType) {
        try {
            invocation.getMethod().invoke(invocation, returnType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void invocationComparedObject(InvocationOnMock invocation, int argumentIndex) {
        Object[] arguments = new Object[1];
        arguments[0] = invocation.getMock();
        
        try {
            invocation.getMethod().invoke(invocation, arguments);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}