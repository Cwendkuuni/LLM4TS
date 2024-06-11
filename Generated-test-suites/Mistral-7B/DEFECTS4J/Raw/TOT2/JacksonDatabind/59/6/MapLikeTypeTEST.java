import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.TypeDeserializer;
import com.fasterxml.jackson.databind.TypeHandler;
import com.fasterxml.jackson.databind.TypeResolverBuilder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializerBase;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.util.ObjectBuffer;

import java.io.Serializable;
import java.lang.reflect.Type;

public class MapLikeTypeTest {

    @Test
    public void testConstruct() {
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class);
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType mapLikeType = MapLikeType.construct(mapType.getRawClass(), keyType, valueType);
        assertEquals("Map<String, Integer>", mapLikeType.getCanonicalClass().getName());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getContentType());
    }

    @Test
    public void testUpgradeFrom() {
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class);
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType mapLikeType = JavaType.constructRawStatic(MapLikeType.class);
        MapLikeType mapLikeTypeInstance = MapLikeType.upgradeFrom(listType, keyType, valueType);
        assertEquals("[map-like type; class class java.util.List, java.lang.String -> java.lang.Integer]", mapLikeTypeInstance.toString());
        assertEquals("Map<String, Integer>", mapLikeTypeInstance.getCanonicalClass().getName());
        assertEquals(keyType, mapLikeTypeInstance.getKeyType());
        assertEquals(valueType, mapLikeTypeInstance.getContentType());
    }

    @Test
    public void testIsTrueMapType() {
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class);
        JavaType mapLikeType = JavaType.constructRawStatic(MapLikeType.class);
        assertTrue(MapLikeType.class.isAssignableFrom(mapType.getRawClass()));
        assertTrue(MapLikeType.class.isAssignableFrom(mapType.getRawClass().getSuperclass()));
        assertTrue(mapLikeType.isTrueMapType());
        assertFalse(listType.isTrueMapType());
    }

    @Test
    public void testGetGenericSignature() {
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType mapLikeType = JavaType.constructRawStatic(MapLikeType.class);
        JavaType mapLikeTypeInstance = new MapLikeType(mapType.getRawClass(), TypeBindings.defaultBindings(), mapType, null, keyType, valueType);
        String expected = "[map-like type; class java.util.Map, java.lang.String -> java.lang.Integer]";
        assertEquals(expected, mapLikeInstance.getGenericSignature().toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithInvalidBaseType() {
        JavaType listType = TypeFactory.defaultInstance().constructType(List.class);
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        MapLikeType.construct(listType.getRawClass(), keyType, valueType);
    }

    @Test
    public void testWithKeyType() {
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType mapLikeType = new MapLikeType(mapType.getRawClass(), TypeBindings.defaultBindings(), mapType, null, keyType, valueType);
        JavaType newMapLikeType = mapLikeType.withKeyType(TypeFactory.defaultInstance().constructType(Long.class));
        assertNotEquals(mapLikeType.getKeyType(), newMapLikeType.getKeyType());
        assertEquals(mapLikeType.getContentType(), newMapLikeType.getContentType());
    }

    @Test
    public void testWithContentType() {
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType mapLikeType = new MapLikeType(mapType.getRawClass(), TypeBindings.defaultBindings(), mapType, null, keyType, valueType);
        JavaType newMapLikeType = mapLikeType.withContentType(TypeFactory.defaultInstance().constructType(Long.class));
        assertEquals(mapLikeType.getKeyType(), newMapLikeType.getKeyType());
        assertNotEquals(mapLikeType.getContentType(), newMapLikeType.getContentType());
    }

    @Test
    public void testWithTypeHandler() {
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType mapLikeType = new MapLikeType(mapType.getRawClass(), TypeBindings.defaultBindings(), mapType, null, keyType, valueType);
        Object handler = new Object() {};
        JavaType newMapLikeType = mapLikeType.withTypeHandler(handler);
        assertSame(handler, newMapLikeType.getValueHandler());
        assertEquals(mapLikeType.getKeyType(), newMapLikeType.getKeyType());
        assertEquals(mapLikeType.getContentType(), newMapLikeType.getContentType());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType mapLikeType = new MapLikeType(mapType.getRawClass(), TypeBindings.defaultBindings(), mapType, null, keyType, valueType);
        Object handler = new Object() {};
        JavaType newMapLikeType = mapLikeType.withContentTypeHandler(handler);
        assertSame(handler, newMapLikeType.getContentTypeHandler());
        assertEquals(mapLikeType.getKeyType(), newMapLikeType.getKeyType());
        assertEquals(mapLikeType.getContentType(), newMapLikeType.getContentType());
    }

    @Test
    public void testWithValueHandler() {
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType mapLikeType = new MapLikeType(mapType.getRawClass(), TypeBindings.defaultBindings(), mapType, null, keyType, valueType);
        Object handler = new Object() {};
        JavaType newMapLikeType = mapLikeType.withValueHandler(handler);
        assertSame(handler, newMapLikeType.getValueHandler());
        assertEquals(mapLikeType.getKeyType(), newMapLikeType.getKeyType());
        assertEquals(mapLikeType.getContentType(), newMapLikeType.getContentType());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType mapLikeType = new MapLikeType(mapType.getRawClass(), TypeBindings.defaultBindings(), mapType, null, keyType, valueType);
        Object handler = new Object() {};
        JavaType newMapLikeType = mapLikeType.withContentValueHandler(handler);
        assertSame(handler, newMapLikeType.getContentValueHandler());
        assertEquals(mapLikeType.getKeyType(), newMapLikeType.getKeyType());
        assertEquals(mapLikeType.getContentType(), newMapLikeType.getContentType());
    }

    @Test
    public void testWithKeyTypeHandler() {
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType mapLikeType = new MapLikeType(mapType.getRawClass(), TypeBindings.defaultBindings(), mapType, null, keyType, valueType);
        Object handler = new Object() {};
        JavaType newMapLikeType = mapLikeType.withKeyTypeHandler(handler);
        assertSame(handler, newMapLikeType.getKeyTypeHandler());
        assertEquals(mapLikeType.getKeyType(), newMapLikeType.getKeyType());
        assertEquals(mapLikeType.getContentType(), newMapLikeType.getContentType());
    }

    @Test
    public void testWithKeyValueHandler() {
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType mapLikeType = new MapLikeType(mapType.getRawClass(), TypeBindings.defaultBindings(), mapType, null, keyType, valueType);
        Object handler = new Object() {};
        JavaType newMapLikeType = mapLikeType.withKeyValueHandler(handler);
        assertSame(handler, newMapLikeType.getKeyValueHandler());
        assertEquals(mapLikeType.getKeyType(), newMapLikeType.getKeyType());
        assertEquals(mapLikeType.getContentType(), newMapLikeType.getContentType());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType mapLikeType = new MapLikeType(mapType.getRawClass(), TypeBindings.defaultBindings(), mapType, null, keyType, valueType);
        JavaType newMapLikeType = mapLikeType.withStaticTyping();
        assertTrue(newMapLikeType.isStatic());
        assertEquals(mapLikeType.getKeyType(), newMapLikeType.getKeyType());
        assertEquals(mapLikeType.getContentType(), newMapLikeType.getContentType());
    }

    @Test
    public void testRefine() {
        JavaType mapType = TypeFactory.defaultInstance().constructType(Map.class);
        JavaType keyType = TypeFactory.defaultInstance().constructType(String.class);
        JavaType valueType = TypeFactory.defaultInstance().constructType(Integer.class);
        JavaType mapLikeType = new MapLikeType(mapType.getRawClass(), TypeBindings.defaultBindings(), mapType, null, keyType, valueType);
        Class<Map<String, Integer>> rawType = (Class<Map<String, Integer>>) mapLikeType.getRawClass();
        TypeBindings bindings = TypeBindings.defaultBindings();
        JavaType newMapLikeType = mapLikeType.refine(rawType, bindings, mapType, null);
        assertNotSame(mapLikeType, newMapLikeType);
        assertEquals(rawType, newMapLikeType.getRawClass());
        assertEquals(keyType, newMapLikeType.getKeyType());
        assertEquals(valueType, newMapLikeType.getContentType());
    }
}