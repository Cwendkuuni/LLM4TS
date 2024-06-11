import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MapTypeTest {

    @Test
    public void testConstructWithValidArguments() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapType mapType = MapType.construct(mapClass, bindings, superClass, superInts, keyType, valueType);

        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
        assertNotNull(mapType.getTypeHandler());
        assertNotNull(mapType.getValueHandler());
    }

    @Test
    public void testWithKeyTypeHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;
        Object keyHandler = new Object();

        MapType mapType = new MapType(mapClass, bindings, superClass, superInts, keyType, valueType);
        MapType mapTypeWithKeyHandler = mapType.withKeyTypeHandler(keyHandler);

        assertEquals(mapType.getRawClass(), mapTypeWithKeyHandler.getRawClass());
        assertEquals(keyType, mapTypeWithKeyHandler.getKeyType());
        assertEquals(valueType, mapTypeWithKeyHandler.getValueType());
        assertNotNull(mapTypeWithKeyHandler.getTypeHandler());
        assertEquals(keyHandler, mapTypeWithKeyHandler.getKeyTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidArguments() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = null;
        JavaType superClass = Object.class;
        JavaType[] superInts = new JavaType[0];
        JavaType keyType = String.class;
        JavaType valueType = Integer.class;

        MapType mapType = MapType.construct(mapClass, bindings, superClass, superInts, keyType, valueType);
    }
}