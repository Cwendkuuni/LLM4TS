import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import java.util.*;

public class ReturnsEmptyValuesTest {
    
    private ReturnsEmptyValues returnsEmptyValues;
    
    @Before
    public void setUp() {
        returnsEmptyValues = new ReturnsEmptyValues();
    }
    
    @Test
    public void testAnswer_toStringMethod() {
        // Test 1: Returns description of mock for toString() method
        InvocationOnMock invocation = createInvocationOnMock("toString");
        Object result = returnsEmptyValues.answer(invocation);
        
        // Assertions
        assertEquals("Mock for ReturnsEmptyValues, hashCode: " + returnsEmptyValues.hashCode(), result);
    }
    
    @Test
    public void testAnswer_compareToMethod() {
        // Test 2: Returns zero if references are equals otherwise non-zero for Comparable#compareTo(T other) method
        // Positive Case: When references are equals
        InvocationOnMock invocation1 = createInvocationOnMock("compareTo");
        setInvocationArgument(invocation1, 0, invocation1.getMock());
        Object result1 = returnsEmptyValues.answer(invocation1);
        
        // Assertions
        assertEquals(0, result1);
        
        // Negative Case: When references are not equals
        InvocationOnMock invocation2 = createInvocationOnMock("compareTo");
        setInvocationArgument(invocation2, 0, new Object());
        Object result2 = returnsEmptyValues.answer(invocation2);
        
        // Assertions
        assertNotEquals(0, result2);
    }
    
    @Test
    public void testAnswer_primitiveReturnType() {
        // Test 3: Returns appropriate primitive for primitive-returning methods
        InvocationOnMock invocation1 = createInvocationOnMock("intValue");
        Object result1 = returnsEmptyValues.answer(invocation1);
        
        InvocationOnMock invocation2 = createInvocationOnMock("doubleValue");
        Object result2 = returnsEmptyValues.answer(invocation2);
        
        InvocationOnMock invocation3 = createInvocationOnMock("booleanValue");
        Object result3 = returnsEmptyValues.answer(invocation3);
        
        // Assertions
        assertEquals(0, result1);
        assertEquals(0.0, result2);
        assertEquals(false, result3);
    }
    
    @Test
    public void testAnswer_primitiveWrapperReturnType() {
        // Test 4: Returns consistent values for primitive wrapper classes
        InvocationOnMock invocation1 = createInvocationOnMock("getInteger");
        Object result1 = returnsEmptyValues.answer(invocation1);
        
        InvocationOnMock invocation2 = createInvocationOnMock("getBoolean");
        Object result2 = returnsEmptyValues.answer(invocation2);
        
        InvocationOnMock invocation3 = createInvocationOnMock("getFloat");
        Object result3 = returnsEmptyValues.answer(invocation3);
        
        // Assertions
        assertEquals(new Integer(0), result1);
        assertEquals(Boolean.FALSE, result2);
        assertEquals(0.0f, result3);
    }
    
    @Test
    public void testAnswer_collectionReturnType() {
        // Test 5: Returns empty collection for collection-returning methods
        InvocationOnMock invocation1 = createInvocationOnMock("getCollection");
        Object result1 = returnsEmptyValues.answer(invocation1);
        
        InvocationOnMock invocation2 = createInvocationOnMock("getSet");
        Object result2 = returnsEmptyValues.answer(invocation2);
        
        InvocationOnMock invocation3 = createInvocationOnMock("getHashSet");
        Object result3 = returnsEmptyValues.answer(invocation3);
        
        // Assertions
        assertTrue(result1 instanceof Collection);
        assertEquals(0, ((Collection<?>) result1).size());
        
        assertTrue(result2 instanceof Set);
        assertEquals(0, ((Set<?>) result2).size());
        
        assertTrue(result3 instanceof HashSet);
        assertEquals(0, ((HashSet<?>) result3).size());
    }
    
    @Test
    public void testAnswer_listReturnType() {
        // Test 6: Returns empty list for list-returning methods
        InvocationOnMock invocation1 = createInvocationOnMock("getList");
        Object result1 = returnsEmptyValues.answer(invocation1);
        
        InvocationOnMock invocation2 = createInvocationOnMock("getLinkedList");
        Object result2 = returnsEmptyValues.answer(invocation2);
        
        InvocationOnMock invocation3 = createInvocationOnMock("getArrayList");
        Object result3 = returnsEmptyValues.answer(invocation3);
        
        // Assertions
        assertTrue(result1 instanceof List);
        assertEquals(0, ((List<?>) result1).size());
        
        assertTrue(result2 instanceof List);
        assertEquals(0, ((List<?>) result2).size());
        
        assertTrue(result3 instanceof List);
        assertEquals(0, ((List<?>) result3).size());
    }
    
    @Test
    public void testAnswer_mapReturnType() {
        // Test 7: Returns empty map for map-returning methods
        InvocationOnMock invocation1 = createInvocationOnMock("getMap");
        Object result1 = returnsEmptyValues.answer(invocation1);
        
        InvocationOnMock invocation2 = createInvocationOnMock("getHashMap");
        Object result2 = returnsEmptyValues.answer(invocation2);
        
        InvocationOnMock invocation3 = createInvocationOnMock("getSortedMap");
        Object result3 = returnsEmptyValues.answer(invocation3);
        
        // Assertions
        assertTrue(result1 instanceof Map);
        assertEquals(0, ((Map<?, ?>) result1).size());
        
        assertTrue(result2 instanceof Map);
        assertEquals(0, ((Map<?, ?>) result2).size());
        
        assertTrue(result3 instanceof Map);
        assertEquals(0, ((Map<?, ?>) result3).size());
    }
    
    private InvocationOnMock createInvocationOnMock(String methodName) {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(getMethod(methodName));
        when(invocation.getMock()).thenReturn(returnsEmptyValues);
        return invocation;
    }
    
    private void setInvocationArgument(InvocationOnMock invocation, int argumentIndex, Object argument) {
        when(invocation.getArguments()).thenReturn(new Object[] { argument });
    }
    
    private Method getMethod(String methodName) {
        try {
            return ReturnsEmptyValues.class.getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}