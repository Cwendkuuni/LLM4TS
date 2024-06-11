import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

    @Test
    public void testConstructionWithKeyAndValueTypes() {
        // Test constructing MapType with key and value types
        MapType mapType = MapType.construct(String.class, TypeBindings.emptyBindings(), null, null, new SimpleType(String.class), new SimpleType(Integer.class));
        assertNotNull(mapType);
        assertEquals(String.class, mapType.getKeyType().getRawClass());
        assertEquals(Integer.class, mapType.getValueType().getRawClass());
    }

    @Test
    public void testWithTypeHandler() {
        // Test setting type handler for MapType
        Object typeHandler = new TypeHandler();
        MapType mapType = MapType.construct(String.class, TypeBindings.emptyBindings(), null, null, new SimpleType(String.class), new SimpleType(Integer.class));
        MapType mapTypeWithTypeHandler = mapType.withTypeHandler(typeHandler);
        assertNotNull(mapTypeWithTypeHandler);
        assertEquals(typeHandler, mapTypeWithTypeHandler.getTypeHandler());
    }

    @Test
    public void testWithStringRepresentation() {
        // Test toString method for MapType
        MapType mapType = MapType.construct(String.class, TypeBindings.emptyBindings(), null, null, new SimpleType(String.class), new SimpleType(Integer.class));
        String expectedString = "[map type; class java.lang.String, class java.lang.String -> class java.lang.Integer]";
        assertEquals(expectedString, mapType.toString());
    }

    @Test
    public void testWithInvalidKeyType() {
        // Test setting invalid key type for MapType
        MapType mapType = MapType.construct(String.class, TypeBindings.emptyBindings(), null, null, new SimpleType(String.class), new SimpleType(Integer.class));
        MapType mapTypeWithInvalidKeyType = mapType.withKeyType(new SimpleType(Boolean.class));
        assertNotNull(mapTypeWithInvalidKeyType);
        assertNotEquals(Boolean.class, mapTypeWithInvalidKeyType.getKeyType().getRawClass());
    }

    @Test
    public void testWithStaticTyping() {
        // Test setting static typing for MapType
        MapType mapType = MapType.construct(String.class, TypeBindings.emptyBindings(), null, null, new SimpleType(String.class), new SimpleType(Integer.class));
        MapType mapTypeWithStaticTyping = mapType.withStaticTyping();
        assertNotNull(mapTypeWithStaticTyping);
        assertTrue(mapTypeWithStaticTyping.isStaticTyping());
    }

}