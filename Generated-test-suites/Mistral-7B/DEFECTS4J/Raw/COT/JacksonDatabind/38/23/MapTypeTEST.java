import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.javasupport.ClassIdResolver;
import com.fasterxml.jackson.databind.type.MapType;

@RunWith(BlockJUnit4ClassRunner.class)
public class MapTypeTest {

    @Test
    public void testConstructor() {
        JavaType keyType = JavaType.constructType(int.class, int.class);
        JavaType valueType = JavaType.constructType(String.class);
        MapType mapType = MapType.construct(Map.class, new TypeBindings(), null, null, keyType, valueType);
        assertNotNull(mapType);
    }

    @Test
    public void testConstructDeprecated() {
        JavaType keyType = JavaType.constructType(int.class, int.class);
        JavaType valueType = JavaType.constructType(String.class);
        try {
            MapType mapType = MapType.construct(Map.class, keyType, valueType);
            fail("Expected deprecated constructor call to fail");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testNarrow() {
        JavaType rawType = HashMap.class;
        JavaType mapType = MapType.construct(rawType, null, null, null, null, null);
        Class<?> subclass = LinkedHashMap.class;
        JavaType narrowedType = mapType.narrow(subclass);
        assertNotNull(narrowedType);
        assertTrue(narrowedType.getRawClass().isAssignableFrom(subclass));
    }

    @Test
    public void testNarrowDeprecated() {
        JavaType rawType = HashMap.class;
        JavaType mapType = MapType.construct(rawType, null, null, null, null, null);
        Class<?> subclass = LinkedHashMap.class;
        try {
            JavaType narrowedType = mapType._narrow(subclass);
            fail("Expected deprecated method call to fail");
        } catch (IllegalAccessException e) {
            // expected
        }
    }

    @Test
    public void testWithTypeHandler() {
        JavaType keyType = JavaType.constructType(int.class, int.class);
        JavaType valueType = JavaType.constructType(String.class);
        MapType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        Object handler = new Object();
        JavaType handlerType = JavaType.constructType(handler.getClass());
        MapType withHandlerType = mapType.withTypeHandler(handlerType);
        assertNotNull(withHandlerType);
        assertEquals(handlerType, withHandlerType.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        JavaType keyType = JavaType.constructType(int.class, int.class);
        JavaType valueType = JavaType.constructType(String.class);
        MapType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        Object handler = new Object();
        JavaType handlerType = JavaType.constructType(handler.getClass());
        JavaType contentTypeHandler = JavaType.constructType(String.class, handlerType);
        MapType withContentHandlerType = mapType.withContentTypeHandler(contentTypeHandler);
        assertNotNull(withContentHandlerType);
        assertEquals(contentTypeHandler, withContentHandlerType.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        JavaType keyType = JavaType.constructType(int.class, int.class);
        JavaType valueType = JavaType.constructType(String.class);
        MapType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        Object handler = new Object();
        JavaType handlerType = JavaType.constructType(handler.getClass());
        MapType withHandlerType = mapType.withValueHandler(handlerType);
        assertNotNull(withHandlerType);
        assertEquals(handlerType, withHandlerType.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        JavaType keyType = JavaType.constructType(int.class, int.class);
        JavaType valueType = JavaType.constructType(String.class);
        MapType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        Object handler = new Object();
        JavaType handlerType = JavaType.constructType(handler.getClass());
        JavaType contentValueHandler = JavaType.constructType(handlerType, valueType);
        MapType withContentHandlerType = mapType.withContentValueHandler(contentValueHandler);
        assertNotNull(withContentHandlerType);
        assertEquals(contentValueHandler, withContentHandlerType.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType keyType = JavaType.constructType(int.class, int.class);
        JavaType valueType = JavaType.constructType(String.class);
        MapType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        MapType staticType = mapType.withStaticTyping();
        assertTrue(staticType.isStatic());
    }

    @Test
    public void testWithContentType() {
        JavaType keyType = JavaType.constructType(int.class, int.class);
        JavaType valueType = JavaType.constructType(String.class);
        JavaType newValueType = JavaType.constructType(Integer.class);
        MapType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        MapType withNewValueType = mapType.withContentType(newValueType);
        assertNotNull(withNewValueType);
        assertEquals(newValueType, withNewValueType.getValueType());
    }

    @Test
    public void testWithKeyType() {
        JavaType rawType = HashMap.class;
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        MapType mapType = MapType.construct(rawType, null, null, null, null, valueType);
        JavaType newKeyType = JavaType.constructType(Integer.class);
        MapType withNewKeyType = mapType.withKeyType(newKeyType);
        assertNotNull(withNewKeyType);
        assertEquals(newKeyType, withNewKeyType.getKeyType());
    }

    @Test
    public void testRefine() {
        JavaType rawType = HashMap.class;
        JavaType keyType = JavaType.constructType(String.class);
        JavaType valueType = JavaType.constructType(Integer.class);
        JavaType refinedType = MapType.construct(rawType, null, null, null, null, null).refine(LinkedHashMap.class, null, null, null);
        assertNotNull(refinedType);
        assertEquals(LinkedHashMap.class, refinedType.getRawClass());
        assertEquals(keyType, refinedType.getKeyType());
        assertEquals(valueType, refinedType.getValueType());
    }

    @Test
    public void testWithKeyTypeHandler() {
        JavaType keyType = JavaType.constructType(int.class, int.class);
        JavaType valueType = JavaType.constructType(String.class);
        MapType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        Object handler = new Object();
        JavaType handlerType = JavaType.constructType(handler.getClass());
        MapType withHandlerType = mapType.withKeyTypeHandler(handlerType);
        assertNotNull(withHandlerType);
        assertEquals(handlerType, withHandlerType.getKeyTypeHandler());
    }

    @Test
    public void testWithKeyValueHandler() {
        JavaType keyType = JavaType.constructType(int.class, int.class);
        JavaType valueType = JavaType.constructType(String.class);
        MapType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        Object handler = new Object();
        JavaType handlerType = JavaType.constructType(ClassIdResolver.class);
        JavaType keyValueHandler = JavaType.constructType(handlerType, keyType, valueType);
        MapType withHandlerType = mapType.withKeyValueHandler(keyValueHandler);
        assertNotNull(withHandlerType);
        assertEquals(keyValueHandler, withHandlerType.getKeyValueHandler());
    }

    @Test
    public void testToString() {
        JavaType keyType = JavaType.constructType(int.class, int.class);
        JavaType valueType = JavaType.constructType(String.class);
        MapType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        String result = mapType.toString();
        String expected = "[map type; class Map<K,V> -> Map.Entry<K,V>]";
        assertEquals(expected, result);
    }
}