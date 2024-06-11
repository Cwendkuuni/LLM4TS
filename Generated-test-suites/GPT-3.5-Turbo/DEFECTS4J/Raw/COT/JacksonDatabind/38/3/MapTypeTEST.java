import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        Class<?> rawType = MapType.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType keyT = null;
        JavaType valueT = null;

        MapType mapType = MapType.construct(rawType, bindings, superClass, superInts, keyT, valueT);

        assertNotNull(mapType);
        assertEquals(rawType, mapType.getRawClass());
        assertEquals(bindings, mapType.getBindings());
        assertEquals(superClass, mapType.getSuperClass());
        assertArrayEquals(superInts, mapType.getInterfaces());
        assertEquals(keyT, mapType.getKeyType());
        assertEquals(valueT, mapType.getValueType());
    }

    @Test
    public void testConstructDeprecated() {
        Class<?> rawType = MapType.class;
        JavaType keyT = null;
        JavaType valueT = null;

        MapType mapType = MapType.construct(rawType, keyT, valueT);

        assertNotNull(mapType);
        assertEquals(rawType, mapType.getRawClass());
        assertNull(mapType.getBindings());
        assertNull(mapType.getSuperClass());
        assertNull(mapType.getInterfaces());
        assertEquals(keyT, mapType.getKeyType());
        assertEquals(valueT, mapType.getValueType());
    }

    @Test
    public void testWithKeyType() {
        Class<?> rawType = MapType.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType keyT = null;
        JavaType valueT = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        MapType mapType = new MapType(rawType, bindings, superClass, superInts, keyT, valueT, valueHandler, typeHandler, asStatic);
        JavaType newKeyType = null;

        MapType newMapType = mapType.withKeyType(newKeyType);

        assertNotSame(mapType, newMapType);
        assertEquals(rawType, newMapType.getRawClass());
        assertEquals(bindings, newMapType.getBindings());
        assertEquals(superClass, newMapType.getSuperClass());
        assertArrayEquals(superInts, newMapType.getInterfaces());
        assertEquals(newKeyType, newMapType.getKeyType());
        assertEquals(valueT, newMapType.getValueType());
        assertEquals(valueHandler, mapType.getValueHandler());
        assertEquals(typeHandler, mapType.getTypeHandler());
        assertEquals(asStatic, mapType.isStatic());
    }

    // ... repeat for other methods in the MapType class ...

    @Test
    public void testToString() {
        Class<?> rawType = MapType.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType keyT = null;
        JavaType valueT = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        MapType mapType = new MapType(rawType, bindings, superClass, superInts, keyT, valueT, valueHandler, typeHandler, asStatic);
        String expectedString = "[map type; class " + rawType.getName() + ", " + keyT + " -> " + valueT + "]";

        assertEquals(expectedString, mapType.toString());
    }

}