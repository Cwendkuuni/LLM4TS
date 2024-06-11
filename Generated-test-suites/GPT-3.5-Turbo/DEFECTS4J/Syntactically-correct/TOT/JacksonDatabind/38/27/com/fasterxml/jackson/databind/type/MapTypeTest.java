import org.junit.*;
import static org.junit.Assert.*;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        // Typical use case
        MapType mapType = MapType.construct(HashMap.class, new TypeBindings(), null, null, String.class, Integer.class);
        assertNotNull(mapType);
        
        // Edge case: Empty super class and super interfaces
        mapType = MapType.construct(LinkedHashMap.class, new TypeBindings(), null, new JavaType[]{}, String.class, Integer.class);
        assertNotNull(mapType);
        
        // Error scenario: Null raw type
        try {
            mapType = MapType.construct(null, new TypeBindings(), null, null, String.class, Integer.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
        
        // Error scenario: Null key type
        try {
            mapType = MapType.construct(HashMap.class, new TypeBindings(), null, null, null, Integer.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
        
        // Error scenario: Null value type
        try {
            mapType = MapType.construct(HashMap.class, new TypeBindings(), null, null, String.class, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }
    
    @Test
    public void testWithKeyType() {
        // Typical use case
        MapType originalMapType = MapType.construct(HashMap.class, new TypeBindings(), null, null, Object.class, Integer.class);
        MapType newMapType = originalMapType.withKeyType(String.class);
        assertNotNull(newMapType);
        assertNotEquals(originalMapType, newMapType);
        assertEquals(originalMapType.getValueType(), newMapType.getValueType());
        assertEquals(String.class, newMapType.getKeyType());

        // Edge case: Same key type as original map type
        originalMapType = MapType.construct(HashMap.class, new TypeBindings(), null, null, String.class, Integer.class);
        newMapType = originalMapType.withKeyType(String.class);
        assertNotNull(newMapType);
        assertEquals(originalMapType, newMapType);

        // Error scenario: Null key type
        try {
            originalMapType.withKeyType(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }
    
    @Test
    public void testWithValueType() {
        // Typical use case
        MapType originalMapType = MapType.construct(HashMap.class, new TypeBindings(), null, null, String.class, Object.class);
        MapType newMapType = originalMapType.withValueType(Integer.class);
        assertNotNull(newMapType);
        assertNotEquals(originalMapType, newMapType);
        assertEquals(originalMapType.getKeyType(), newMapType.getKeyType());
        assertEquals(Integer.class, newMapType.getValueType());

        // Edge case: Same value type as original map type
        originalMapType = MapType.construct(LinkedHashMap.class, new TypeBindings(), null, null, String.class, Integer.class);
        newMapType = originalMapType.withValueType(Integer.class);
        assertNotNull(newMapType);
        assertEquals(originalMapType, newMapType);

        // Error scenario: Null value type
        try {
            originalMapType.withValueType(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    @Test
    public void testWithTypeHandler() {
        // Typical use case
        MapType originalMapType = MapType.construct(HashMap.class, new TypeBindings(), null, null, String.class, Integer.class);
        MapType newMapType = originalMapType.withTypeHandler("TypeHandler");
        assertNotNull(newMapType);
        assertNotEquals(originalMapType, newMapType);
        assertEquals(originalMapType.getKeyType(), newMapType.getKeyType());
        assertEquals(originalMapType.getValueType(), newMapType.getValueType());
        assertEquals("TypeHandler", newMapType.getTypeHandler());
        
        // Error scenario: Null type handler
        try {
            originalMapType.withTypeHandler(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }
    
}