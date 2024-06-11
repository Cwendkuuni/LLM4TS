import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;

import java.util.*;

import static org.junit.Assert.*;

public class ReturnsEmptyValuesTest {
    
    private ReturnsEmptyValues emptyValues;
    
    @Before
    public void setUp() {
        emptyValues = new ReturnsEmptyValues();
    }
    
    @Test
    public void testToStringReturnsMockDescription() {
        // Arrange
        InvocationOnMock invocation = new InvocationOnMock(
                null,
                null,
                null,
                ReturnsEmptyValues.class.getDeclaredMethods()[0],
                null,
                null
        );
        
        // Act
        Object result = emptyValues.answer(invocation);
        
        // Assert
        assertTrue(result instanceof String);
        assertTrue(result.toString().startsWith("Mock for "));
    }
    
    @Test
    public void testCompareToReturnsNonZero() {
        // Arrange
        InvocationOnMock invocation = new InvocationOnMock(
                null,
                null,
                null,
                ReturnsEmptyValues.class.getDeclaredMethods()[1],
                null,
                null
        );
        
        // Act
        Object result = emptyValues.answer(invocation);
        
        // Assert
        assertTrue(result instanceof Integer);
        assertNotEquals(0, result);
    }
    
    @Test
    public void testReturnValueForPrimitiveOrWrapper() {
        // Arrange
        Class<?>[] types = {
                boolean.class, Boolean.class,
                byte.class, Byte.class,
                char.class, Character.class,
                short.class, Short.class,
                int.class, Integer.class,
                long.class, Long.class,
                float.class, Float.class,
                double.class, Double.class
        };
        
        // Act & Assert
        for (Class<?> type : types) {
            Object result = emptyValues.returnValueFor(type);
            assertNotNull(result);
            assertEquals(type, result.getClass());
            assertNotEquals(0, result);
        }
    }
    
    @Test
    public void testReturnValueForCollection() {
        // Arrange
        Class<?>[] types = {
                Collection.class,
                Set.class,
                HashSet.class,
                SortedSet.class,
                TreeSet.class,
                LinkedHashSet.class,
                List.class,
                LinkedList.class,
                ArrayList.class
        };
        
        // Act & Assert
        for (Class<?> type : types) {
            Object result = emptyValues.returnValueFor(type);
            assertNotNull(result);
            assertTrue(result instanceof Collection);
            assertEquals(type, result.getClass());
            assertTrue(((Collection<?>) result).isEmpty());
        }
    }
    
    @Test
    public void testReturnValueForMap() {
        // Arrange
        Class<?>[] types = {
                Map.class,
                HashMap.class,
                SortedMap.class,
                TreeMap.class,
                LinkedHashMap.class
        };
        
        // Act & Assert
        for (Class<?> type : types) {
            Object result = emptyValues.returnValueFor(type);
            assertNotNull(result);
            assertTrue(result instanceof Map);
            assertEquals(type, result.getClass());
            assertTrue(((Map<?, ?>) result).isEmpty());
        }
    }
    
    @Test
    public void testReturnValueForOtherTypes() {
        // Arrange
        Class<?>[] types = {
                String.class,
                Integer.class,
                Object.class
        };
        
        // Act & Assert
        for (Class<?> type : types) {
            Object result = emptyValues.returnValueFor(type);
            assertNull(result);
        }
    }
    
}