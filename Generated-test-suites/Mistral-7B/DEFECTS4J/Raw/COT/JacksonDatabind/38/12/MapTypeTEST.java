import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;

@RunWith(JUnit4.class)
public class MapTypeTest {

    @Test
    public void testConstructor() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = _bogusSuperClass(Map.class);
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = JavaType.constructParametricType(Map.Entry.class, String.class, Integer.class);
        JavaType valueT = JavaType.constructRawType(Integer.class);

        MapType mapTypeInstance = MapType.construct(mapType, bindings, superClass, superInts, keyT, valueT);

        assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", mapTypeInstance.toString());
        assertEquals(keyT, mapTypeInstance.getKeyType());
        assertEquals(valueT, mapTypeInstance.getValueType());
        assertFalse(mapTypeInstance.isStatic());
    }

    @Test
    public void testConstructDeprecated() {
        Class<Map<String, Integer>> mapType = Map.class;
        JavaType keyT = JavaType.constructRawType(String.class);
        JavaType valueT = JavaType.constructRawType(Integer.class);

        MapType mapTypeInstance = MapType.construct(mapType, keyT, valueT);

        assertEquals("java.util.Map<java.lang.String, java.lang.Integer>", mapTypeInstance.toString());
        assertEquals(keyT, mapTypeInstance.getKeyType());
        assertEquals(valueT, mapTypeInstance.getValueType());
        assertFalse(mapTypeInstance.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSubclass() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = JavaType.constructRawType(Object.class);
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = JavaType.constructRawType(String.class);
        JavaType valueT = JavaType.constructRawType(Integer.class);

        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, null, null, false);
    }

    @Test
    public void testWithTypeHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = _bogusSuperClass(Map.class);
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = JavaType.constructParametricType(Map.Entry.class, String.class, Integer.class);
        JavaType valueT = JavaType.constructRawType(Integer.class);
        Object valueHandler = new Object() {};

        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, valueHandler, null, false);

        MapType withTypeHandlerInstance = mapTypeInstance.withTypeHandler(new Object() {});

        assertNotSame(mapTypeInstance, withTypeHandlerInstance);
        assertEquals(mapTypeInstance.getValueType(), withTypeHandlerInstance.getValueType());
        assertEquals(new Object() {}, withTypeHandlerInstance.getTypeHandler());
    }

    @Test
    public void testWithContentTypeHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = _bogusSuperClass(Map.class);
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = JavaType.constructParametricType(Map.Entry.class, String.class, Integer.class);
        JavaType valueT = JavaType.constructRawType(Integer.class);
        Object typeHandler = new Object() {};

        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, null, typeHandler, false);

        JavaType contentTypeHandler = JavaType.constructRawType(Object.class);

        MapType withContentTypeHandlerInstance = mapTypeInstance.withContentTypeHandler(contentTypeHandler);

        assertNotSame(mapTypeInstance, withContentTypeHandlerInstance);
        assertEquals(mapTypeInstance.getValueType(), withContentTypeHandlerInstance.getValueType());
        assertEquals(contentTypeHandler, withContentTypeHandlerInstance.getContentTypeHandler());
    }

    @Test
    public void testWithValueHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = _bogusSuperClass(Map.class);
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = JavaType.constructParametricType(Map.Entry.class, String.class, Integer.class);
        JavaType valueT = JavaType.constructRawType(Integer.class);
        Object valueHandler = new Object() {};

        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, valueHandler, null, false);

        MapType withValueHandlerInstance = mapTypeInstance.withValueHandler(new Object() {});

        assertNotSame(mapTypeInstance, withValueHandlerInstance);
        assertEquals(mapTypeInstance.getValueType(), withValueHandlerInstance.getValueType());
        assertEquals(new Object() {}, withValueHandlerInstance.getValueHandler());
    }

    @Test
    public void testWithContentValueHandler() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = _bogusSuperClass(Map.class);
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = JavaType.constructParametricType(Map.Entry.class, String.class, Integer.class);
        JavaType valueT = JavaType.constructRawType(Integer.class);
        Object contentValueHandler = new Object() {};

        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, null, null, false);

        MapType withContentValueHandlerInstance = mapTypeInstance.withContentValueHandler(contentValueHandler);

        assertNotSame(mapTypeInstance, withContentValueHandlerInstance);
        assertEquals(mapTypeInstance.getValueType(), withContentValueHandlerInstance.getValueType());
        assertEquals(contentValueHandler, withContentValueHandlerInstance.getContentValueHandler());
    }

    @Test
    public void testWithStaticTyping() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = _bogusSuperClass(Map.class);
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = JavaType.constructParametricType(Map.Entry.class, String.class, Integer.class);
        JavaType valueT = JavaType.constructRawType(Integer.class);

        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, null, null, false);

        MapType withStaticTypingInstance = mapTypeInstance.withStaticTyping();

        assertTrue(withStaticTypingInstance.isStatic());
        assertFalse(mapTypeInstance.isStatic());
    }

    @Test
    public void testWithContentType() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = _bogusSuperClass(Map.class);
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = JavaType.constructParametricType(Map.Entry.class, String.class, Integer.class);
        JavaType valueT = JavaType.constructRawType(Integer.class);
        JavaType contentType = JavaType.constructRawType(Integer[].class);

        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, null, null, false);

        MapType withContentTypeInstance = mapTypeInstance.withContentType(contentType);

        assertNotSame(mapTypeInstance, withContentTypeInstance);
        assertEquals(mapTypeInstance.getValueType(), withContentTypeInstance.getValueType());
        assertEquals(contentType, withContentTypeInstance.getContentType());
    }

    @Test
    public void testWithKeyType() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = _bogusSuperClass(Map.class);
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = JavaType.constructRawType(String.class);
        JavaType valueT = JavaType.constructRawType(Integer.class);
        JavaType newKeyT = JavaType.constructParametricType(Map.Entry.class, String.class, Integer.class);

        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, null, null, false);

        MapType withKeyTypeInstance = mapTypeInstance.withKeyType(newKeyT);

        assertNotSame(mapTypeInstance, withKeyTypeInstance);
        assertEquals(newKeyT, withKeyTypeInstance.getKeyType());
        assertEquals(mapTypeInstance.getValueType(), withKeyTypeInstance.getValueType());
    }

    @Test
    public void testRefine() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = _bogusSuperClass(Map.class);
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = JavaType.constructParametricType(Map.Entry.class, String.class, Integer.class);
        JavaType valueT = JavaType.constructRawType(Integer.class);

        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, null, null, false);

        Class<Map<String, Long>> newMapType = Map.class;

        MapType refinedMapTypeInstance = mapTypeInstance.refine(newMapType.getClass(), bindings, superClass, superInts);

        assertNotSame(mapTypeInstance, refinedMapTypeInstance);
        assertEquals(newMapType.getClass(), refinedMapTypeInstance.getClass());
        assertEquals(mapTypeInstance.getKeyType(), refinedMapTypeInstance.getKeyType());
        assertEquals(mapTypeInstance.getValueType(), refinedMapTypeInstance.getValueType());
    }

    @Test
    public void testToString() {
        Class<Map<String, Integer>> mapType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType superClass = _bogusSuperClass(Map.class);
        JavaType[] superInts = new JavaType[0];
        JavaType keyT = JavaType.constructParametricType(Map.Entry.class, String.class, Integer.class);
        JavaType valueT = JavaType.constructRawType(Integer.class);

        MapType mapTypeInstance = new MapType(mapType, bindings, superClass, superInts, keyT, valueT, null, null, false);

        assertEquals("[map type; class Map<java.lang.String, java.lang.Integer>, java.util.Map<java.lang.String, java.lang.Integer> -> java.lang.Integer]", mapTypeInstance.toString());
    }

    private static JavaType _bogusSuperClass(Class<?> clazz) {
        return JavaType.constructRawType(Object.class);
    }
}