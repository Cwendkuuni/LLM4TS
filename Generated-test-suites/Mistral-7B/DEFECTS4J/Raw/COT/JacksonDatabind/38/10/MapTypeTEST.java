import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;
import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

    @Test
    public void testMapTypeConstructor() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapType mapType = MapType.construct(mapClass, bindings, null, null, keyType, valueType);
        assertEquals(mapClass, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testMapTypeWithTypeHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapType mapType = MapType.construct(mapClass, bindings, null, null, keyType, valueType);
        Object handler = new Object();
        MapType newMapType = mapType.withTypeHandler(handler);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertNotSame(mapType.getTypeHandler(), newMapType.getTypeHandler());
        assertSame(handler, newMapType.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeInvalidConstructor() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapType mapType = MapType.construct(mapClass, bindings, null, null, keyType, valueType);
        JavaType invalidType = TypeFactory.defaultInstance().constructType(MapType.class);
        MapType newMapType = mapType.withTypeHandler(invalidType);
    }

    @Test
    public void testMapTypeWithContentTypeHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapType mapType = MapType.construct(mapClass, bindings, null, null, keyType, valueType);
        Object handler = new Object();
        JavaType contentType = TypeFactory.defaultInstance().constructType(Map.class);
        MapType newMapType = mapType.withContentTypeHandler(handler);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        JavaType newValueType = newMapType.getValueType();
        assertEquals(valueType, newValueType);
        assertNotSame(valueType.getTypeHandler(), newValueType.getTypeHandler());
        assertSame(handler, newValueType.getTypeHandler());
    }

    @Test
    public void testMapTypeWithValueHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapType mapType = MapType.construct(mapClass, bindings, null, null, keyType, valueType);
        Object handler = new Object();
        MapType newMapType = mapType.withValueHandler(handler);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertNotSame(mapType.getValueHandler(), newMapType.getValueHandler());
        assertSame(handler, newMapType.getValueHandler());
    }

    @Test
    public void testMapTypeWithContentValueHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapType mapType = MapType.construct(mapClass, bindings, null, null, keyType, valueType);
        Object handler = new Object();
        JavaType contentValueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapType newMapType = mapType.withContentValueHandler(handler);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        JavaType newValueType = newMapType.getValueType();
        assertEquals(valueType, newValueType);
        JavaType newContentValueType = newValueType.getValueType();
        assertEquals(contentValueType, newContentValueType);
        assertNotSame(contentValueType.getValueHandler(), newContentValueType.getValueHandler());
        assertSame(handler, newContentValueType.getValueHandler());
    }

    @Test
    public void testMapTypeWithStaticTyping() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapType mapType = MapType.construct(mapClass, bindings, null, null, keyType, valueType);
        MapType newMapType = mapType.withStaticTyping();
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertTrue(newMapType.isStatic());
        assertFalse(mapType.isStatic());
    }

    @Test
    public void testMapTypeWithContentType() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType contentType = TypeFactory.defaultInstance().constructType(String.class);
        MapType mapType = MapType.construct(mapClass, bindings, null, null, keyType, valueType);
        MapType newMapType = mapType.withContentType(contentType);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(contentType, newMapType.getValueType());
    }

    @Test
    public void testMapTypeWithKeyType() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType newKeyType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapType mapType = MapType.construct(mapClass, bindings, null, null, keyType, valueType);
        MapType newMapType = mapType.withKeyType(newKeyType);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(newKeyType, newMapType.getKeyType());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, newMapType.getValueType());
    }

    @Test
    public void testMapTypeRefine() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapType mapType = MapType.construct(mapClass, bindings, null, null, keyType, valueType);
        Class<Map<Integer, String>> newMapClass = Map.class;
        TypeBindings newBindings = TypeBindings.defaultBindings();
        JavaType newSuperClass = null;
        JavaType[] newSuperInterfaces = new JavaType[0];
        MapType newMapType = mapType.refine(newMapClass, newBindings, newSuperClass, newSuperInterfaces);
        assertEquals(newMapClass, newMapType.getRawClass());
        assertEquals(newKeyType, newMapType.getKeyType());
        assertEquals(valueType, newMapType.getValueType());
    }

    @Test
    public void testMapTypeWithKeyTypeHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapType mapType = MapType.construct(mapClass, bindings, null, null, keyType, valueType);
        Object handler = new Object();
        MapType newMapType = mapType.withKeyTypeHandler(handler);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertNotSame(mapType.getKeyTypeHandler(), newMapType.getKeyTypeHandler());
        assertSame(handler, newMapType.getKeyTypeHandler());
    }

    @Test
    public void testMapTypeWithKeyValueHandler() {
        Class<Map<String, Integer>> mapClass = Map.class;
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapType mapType = MapType.construct(mapClass, bindings, null, null, keyType, valueType);
        Object handler = new Object();
        MapType newMapType = mapType.withKeyValueHandler(handler);
        assertEquals(mapType.getRawClass(), newMapType.getRawClass());
        assertEquals(mapType.getKeyType(), newMapType.getKeyType());
        assertEquals(mapType.getValueType(), newMapType.getValueType());
        assertNotSame(mapType.getKeyHandler(), newMapType.getKeyHandler());
        assertSame(handler, newMapType.getKeyHandler());
    }
}