import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        // Typical use case
        MapType mapType = MapType.construct(HashMap.class, TypeBindings.emptyBindings(),
                null, null,
                SimpleType.construct(String.class), SimpleType.construct(Integer.class));
        assertNotNull(mapType);
        
        // Edge case: rawType is null
        try {
            MapType nullMapType = MapType.construct(null, TypeBindings.emptyBindings(),
                    null, null,
                    SimpleType.construct(String.class), SimpleType.construct(Integer.class));
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Edge case: keyT is null
        try {
            MapType nullKeyTType = MapType.construct(HashMap.class, TypeBindings.emptyBindings(),
                    null, null,
                    null, SimpleType.construct(Integer.class));
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Edge case: valueT is null
        try {
            MapType nullValueTType = MapType.construct(HashMap.class, TypeBindings.emptyBindings(),
                    null, null,
                    SimpleType.construct(String.class), null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
    
    @Test
    public void testWithKeyType() {
        MapType mapType = MapType.construct(HashMap.class, TypeBindings.emptyBindings(),
                null, null,
                SimpleType.construct(String.class), SimpleType.construct(Integer.class));
        
        JavaType newKeyType = SimpleType.construct(Object.class);
        MapType updatedMapType = mapType.withKeyType(newKeyType);
        assertNotNull(updatedMapType);
        assertNotEquals(mapType, updatedMapType);
        assertEquals(newKeyType, updatedMapType.getKeyType());
    }
    
    @Test
    public void testWithContentType() {
        MapType mapType = MapType.construct(HashMap.class, TypeBindings.emptyBindings(),
                null, null,
                SimpleType.construct(String.class), SimpleType.construct(Integer.class));
        
        JavaType newValueType = SimpleType.construct(Long.class);
        MapType updatedMapType = mapType.withContentType(newValueType);
        assertNotNull(updatedMapType);
        assertNotEquals(mapType, updatedMapType);
        assertEquals(newValueType, updatedMapType.getContentType());
    }
    
    @Test
    public void testWithStaticTyping() {
        MapType mapType = MapType.construct(HashMap.class, TypeBindings.emptyBindings(),
                null, null,
                SimpleType.construct(String.class), SimpleType.construct(Integer.class));
        
        MapType updatedMapType = mapType.withStaticTyping();
        assertNotNull(updatedMapType);
        assertNotEquals(mapType, updatedMapType);
        assertTrue(updatedMapType.isStaticTyping());
    }
    
    @Test
    public void testWithKeyTypeHandler() {
        MapType mapType = MapType.construct(HashMap.class, TypeBindings.emptyBindings(),
                null, null,
                SimpleType.construct(String.class), SimpleType.construct(Integer.class));
        
        Object keyTypeHandler = new KeyTypeHandler();
        MapType updatedMapType = mapType.withKeyTypeHandler(keyTypeHandler);
        assertNotNull(updatedMapType);
        assertNotEquals(mapType, updatedMapType);
        assertEquals(keyTypeHandler, updatedMapType.getKeyTypeHandler());
    }
    
    @Test
    public void testWithKeyValueHandler() {
        MapType mapType = MapType.construct(HashMap.class, TypeBindings.emptyBindings(),
                null, null,
                SimpleType.construct(String.class), SimpleType.construct(Integer.class));
        
        Object keyValueHandler = new KeyValueHandler();
        MapType updatedMapType = mapType.withKeyValueHandler(keyValueHandler);
        assertNotNull(updatedMapType);
        assertNotEquals(mapType, updatedMapType);
        assertEquals(keyValueHandler, updatedMapType.getKeyValueHandler());
    }
    
    @Test
    public void testToString() {
        MapType mapType = MapType.construct(HashMap.class, TypeBindings.emptyBindings(),
                null, null,
                SimpleType.construct(String.class), SimpleType.construct(Integer.class));
        
        String expectedString = "[map type; class java.util.HashMap, java.lang.String -> java.lang.Integer]";
        assertEquals(expectedString, mapType.toString());
    }
}

class KeyTypeHandler {}
class KeyValueHandler {}