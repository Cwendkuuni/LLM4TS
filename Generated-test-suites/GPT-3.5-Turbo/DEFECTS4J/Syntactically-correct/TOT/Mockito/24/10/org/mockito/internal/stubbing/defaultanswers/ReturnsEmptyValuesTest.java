import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;

public class ReturnsEmptyValuesTest {
    
    private ReturnsEmptyValues returnsEmptyValues;
    
    @Before
    public void setUp() {
        returnsEmptyValues = new ReturnsEmptyValues();
    }
    
    @Test
    public void testAnswer_withToStringMethod_shouldReturnMockDescription() throws NoSuchMethodException, SecurityException {
        // Given
        InvocationOnMock invocation = prepareInvocation("toString");
        
        // When
        Object result = returnsEmptyValues.answer(invocation);
        
        // Then
        assertEquals("Mock for ReturnsEmptyValues, hashCode: " + invocation.getMock().hashCode(), result);
    }
    
    @Test
    public void testAnswer_withCompareToMethod_shouldReturnNonZero() throws NoSuchMethodException, SecurityException {
        // Given
        InvocationOnMock invocation = prepareInvocation("compareTo");
        
        // When
        Object result = returnsEmptyValues.answer(invocation);
        
        // Then
        assertEquals(1, result);
    }
    
    @Test
    public void testAnswer_withPrimitiveReturnType_shouldReturnDefaultValue() throws NoSuchMethodException, SecurityException {
        // Given
        InvocationOnMock invocation = prepareInvocation("intMethod");
        
        // When
        Object result = returnsEmptyValues.answer(invocation);
        
        // Then
        assertEquals(0, result);
    }
    
    @Test
    public void testAnswer_withWrapperClassReturnType_shouldReturnDefaultValue() throws NoSuchMethodException, SecurityException {
        // Given
        InvocationOnMock invocation = prepareInvocation("integerMethod");
        
        // When
        Object result = returnsEmptyValues.answer(invocation);
        
        // Then
        assertEquals(0, result);
    }
    
    @Test
    public void testAnswer_withCollectionReturnType_shouldReturnEmptyLinkedList() throws NoSuchMethodException, SecurityException {
        // Given
        InvocationOnMock invocation = prepareInvocation("collectionMethod");
        
        // When
        Object result = returnsEmptyValues.answer(invocation);
        
        // Then
        assertEquals(LinkedList.class, result.getClass());
        assertEquals(0, ((LinkedList<?>) result).size());
    }
    
    @Test
    public void testAnswer_withSetReturnType_shouldReturnEmptyHashSet() throws NoSuchMethodException, SecurityException {
        // Given
        InvocationOnMock invocation = prepareInvocation("setMethod");
        
        // When
        Object result = returnsEmptyValues.answer(invocation);
        
        // Then
        assertEquals(HashSet.class, result.getClass());
        assertEquals(0, ((HashSet<?>) result).size());
    }
    
    @Test
    public void testAnswer_withListReturnType_shouldReturnEmptyLinkedList() throws NoSuchMethodException, SecurityException {
        // Given
        InvocationOnMock invocation = prepareInvocation("listMethod");
        
        // When
        Object result = returnsEmptyValues.answer(invocation);
        
        // Then
        assertEquals(LinkedList.class, result.getClass());
        assertEquals(0, ((LinkedList<?>) result).size());
    }
    
    @Test
    public void testAnswer_withMapReturnType_shouldReturnEmptyHashMap() throws NoSuchMethodException, SecurityException {
        // Given
        InvocationOnMock invocation = prepareInvocation("mapMethod");
        
        // When
        Object result = returnsEmptyValues.answer(invocation);
        
        // Then
        assertEquals(HashMap.class, result.getClass());
        assertEquals(0, ((HashMap<?, ?>) result).size());
    }
    
    @Test
    public void testAnswer_withUnknownReturnType_shouldReturnNull() throws NoSuchMethodException, SecurityException {
        // Given
        InvocationOnMock invocation = prepareInvocation("unknownMethod");
        
        // When
        Object result = returnsEmptyValues.answer(invocation);
        
        // Then
        assertNull(result);
    }
    
    private InvocationOnMock prepareInvocation(String methodName) throws NoSuchMethodException, SecurityException {
        MockName mockName = new MockName();
        InvocationOnMock invocationMock = new InvocationOnMock() {
            @Override
            public Object getMock() {
                return this;
            }
            
            @Override
            public Method getMethod() {
                try {
                    return InvocationOnMock.class.getMethod(methodName);
                } catch (NoSuchMethodException e) {
                    return null;
                }
            }
            
            @Override
            public Object[] getArguments() {
                return null;
            }
            
            @Override
            public Object callRealMethod() throws Throwable {
                return null;
            }
            
            @Override
            public boolean isIgnored() {
                return false;
            }
        };
        mockName.appendTo(invocationMock);
        return invocationMock;
    }
}