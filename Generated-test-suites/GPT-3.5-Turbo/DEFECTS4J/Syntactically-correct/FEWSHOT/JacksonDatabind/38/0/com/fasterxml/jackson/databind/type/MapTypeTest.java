import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.JavaType;
import org.junit.Test;

public class MapTypeTest {
    @Test
    public void testConstruct() {
        Class<?> rawType = TestClass.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = {new JavaType()};
        JavaType keyT = new JavaType();
        JavaType valueT = new JavaType();

        MapType mapType = MapType.construct(rawType, bindings, superClass, superInts, keyT, valueT);

        assertEquals(rawType, mapType.getGenericRawType());
        assertEquals(bindings, mapType.getBindings());
        assertEquals(superClass, mapType.getSuperClass());
        assertEquals(superInts, mapType.getSuperInterfaces());
        assertEquals(keyT, mapType.getKeyType());
        assertEquals(valueT, mapType.getValueType());
    }

    @Test
    public void testConstructDeprecated() {
        Class<?> rawType = TestClass.class;
        JavaType keyT = new JavaType();
        JavaType valueT = new JavaType();

        MapType mapType = MapType.construct(rawType, keyT, valueT);

        assertEquals(rawType, mapType.getGenericRawType());
        assertEquals(keyT, mapType.getKeyType());
        assertEquals(valueT, mapType.getValueType());
    }

    @Test
    public void testWithValueTypeHandler() {
        MapType mapType = new MapType();
        Object valueHandler = new Object();

        MapType result = mapType.withValueTypeHandler(valueHandler);

        assertEquals(mapType, result);
        assertEquals(valueHandler, result.getValueType().getTypeHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        MapType mapType = new MapType();
        Object valueHandler = new Object();

        MapType result = mapType.withContentValueHandler(valueHandler);

        assertEquals(mapType, result);
        assertEquals(valueHandler, result.getValueType().getValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        MapType mapType = new MapType();
        JavaType keyType = new JavaType();
        JavaType valueType = new JavaType();

        MapType result = mapType.withStaticTyping();

        assertEquals(mapType, result);
        assertEquals(keyType.withStaticTyping(), result.getKeyType());
        assertEquals(valueType.withStaticTyping(), result.getValueType());
        assertEquals(true, result.getAsStatic());
    }

    @Test
    public void testWithContentType() {
        MapType mapType = new MapType();
        JavaType contentType = new JavaType();

        MapType result = mapType.withContentType(contentType);

        assertEquals(mapType, result);
        assertEquals(contentType, result.getValueType());
    }

    @Test
    public void testWithKeyType() {
        MapType mapType = new MapType();
        JavaType keyType = new JavaType();

        MapType result = mapType.withKeyType(keyType);

        assertEquals(mapType, result);
        assertEquals(keyType, result.getKeyType());
    }

    @Test
    public void testRefine() {
        MapType mapType = new MapType();
        Class<?> rawType = TestClass.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = {new JavaType()};
        JavaType keyType = new JavaType();
        JavaType valueType = new JavaType();

        MapType result = mapType.refine(rawType, bindings, superClass, superInterfaces);

        assertEquals(rawType, result.getGenericRawType());
        assertEquals(bindings, result.getBindings());
        assertEquals(superClass, result.getSuperClass());
        assertEquals(superInterfaces, result.getSuperInterfaces());
        assertEquals(keyType, result.getKeyType());
        assertEquals(valueType, result.getValueType());
    }

    @Test
    public void testWithKeyTypeHandler() {
        MapType mapType = new MapType();
        Object keyHandler = new Object();

        MapType result = mapType.withKeyTypeHandler(keyHandler);

        assertEquals(mapType, result);
        assertEquals(keyHandler, result.getKeyType().getTypeHandler());
    }

    @Test
    public void testWithKeyValueHandler() {
        MapType mapType = new MapType();
        Object keyHandler = new Object();

        MapType result = mapType.withKeyValueHandler(keyHandler);

        assertEquals(mapType, result);
        assertEquals(keyHandler, result.getKeyType().getValueHandler());
    }

    @Test
    public void testToString() {
        MapType mapType = new MapType();
        Class<?> rawType = TestClass.class;
        JavaType keyType = new JavaType();
        JavaType valueType = new JavaType();

        mapType.setGenericRawType(rawType);
        mapType.setKeyType(keyType);
        mapType.setValueType(valueType);

        String expectedResult = "[map type; class " + rawType.getName() + ", " + keyType + " -> " + valueType + "]";
        assertEquals(expectedResult, mapType.toString());
    }
}