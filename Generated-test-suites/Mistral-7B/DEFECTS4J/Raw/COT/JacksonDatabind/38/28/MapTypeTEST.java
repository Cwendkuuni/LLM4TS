import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;

@RunWith(JUnit4.class)
public class MapTypeTest {

    // Test constructor
    @Test
    public void testMapTypeConstructor() {
        JavaType keyType = JavaType.constructType(int.class, int.class);
        JavaType valueType = JavaType.constructType(String.class, String.class);
        MapType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        assertEquals(Map.class, mapType.getRawClass());
        assertEquals(keyType, mapType.getKeyType());
        assertEquals(valueType, mapType.getValueType());
    }

    // Test deprecated constructors
    @Test
    public void testMapTypeConstructors_Deprecated() {
        JavaType keyType = JavaType.constructType(int.class, int.class);
        JavaType valueType = JavaType.constructType(String.class, String.class);

        // Test construct
        MapType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        assertNull(mapType.getBindings());
        assertNull(mapType.getSuperClass());
        assertNull(mapType.getSuperInterfaces());
        assertEquals(mapType.getClass(), MapType.class);

        // Test construct with wrong argument order
        assertThrows(IllegalArgumentException.class, () -> MapType.construct(valueType, keyType, Map.class));

        // Test _narrow method (deprecated)
        MapType subclassMapType = MapType.construct(HashMap.class, null, null, null, keyType, valueType);
        MapType narrowedMapType = mapType.withTypeHandler(subclassMapType);
        assertEquals(HashMap.class, narrowedMapType.getRawClass());
    }

    // Test other methods
    @Test
    public void testMapTypeOtherMethods() {
        JavaType keyType = JavaType.constructType(int.class, int.class);
        JavaType valueType = JavaType.constructType(String.class, String.class);
        MapType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);

        // Test withTypeHandler method
        Object valueHandler = new Object() {};
        MapType withValueHandler = mapType.withTypeHandler(valueHandler);
        assertNotNull(withValueHandler.getTypeHandler());
        assertEquals(mapType.getClass(), withValueHandler.getClass());

        // Test withContentTypeHandler method
        Object contentTypeHandler = new Object() {};
        MapType withContentTypeHandler = mapType.withContentTypeHandler(contentTypeHandler);
        assertNotNull(withContentTypeHandler.getTypeHandler());
        assertNotNull(withContentTypeHandler.getValueType().getTypeHandler());
        assertEquals(mapType.getClass(), withContentTypeHandler.getClass());

        // Test withValueHandler method
        Object valueHandler2 = new Object() {};
        MapType withValueHandler2 = mapType.withValueHandler(valueHandler2);
        assertNotNull(withValueHandler2.getValueHandler());
        assertEquals(mapType.getClass(), withValueHandler2.getClass());

        // Test withContentValueHandler method
        Object contentValueHandler = new Object() {};
        MapType withContentValueHandler = mapType.withContentValueHandler(contentValueHandler);
        assertNotNull(withContentValueHandler.getValueHandler());
        assertNotNull(withContentValueHandler.getValueType().getValueHandler());
        assertEquals(mapType.getClass(), withContentValueHandler.getClass());

        // Test withStaticTyping method
        MapType withStaticTyping = mapType.withStaticTyping();
        assertTrue(withStaticTyping.isStatic());
        assertEquals(mapType.getClass(), withStaticTyping.getClass());

        // Test withContentType method
        JavaType newValueType = JavaType.constructType(Long.class, Long.class);
        MapType withContentType = mapType.withContentType(newValueType);
        assertNotEquals(valueType, newValueType);
        assertEquals(mapType.getClass(), withContentType.getClass());
        assertEquals(newValueType, withContentType.getValueType());

        // Test withKeyType method
        JavaType newKeyType = JavaType.constructType(long.class, long.class);
        MapType withKeyType = mapType.withKeyType(newKeyType);
        assertNotEquals(keyType, newKeyType);
        assertEquals(mapType.getClass(), withKeyType.getClass());
        assertEquals(newKeyType, withKeyType.getKeyType());

        // Test refine method
        JavaType rawType = HashMap.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = Collection.class;
        JavaType[] superInterfaces = new JavaType[] {Serializable.class, Cloneable.class};
        MapType refinedMapType = mapType.refine(rawType, bindings, superClass, superInterfaces);
        assertNotEquals(mapType, refinedMapType);
        assertEquals(rawType, refinedMapType.getRawClass());
        assertEquals(bindings, refinedMapType.getBindings());
        assertEquals(superClass, refinedMapType.getSuperClass());
        assertArrayEquals(superInterfaces, refinedMapType.getSuperInterfaces());
    }

    // Test edge cases and exceptions
    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeConstructors_IllegalArgumentException() {
        JavaType keyType = JavaType.constructType(int[].class, int[].class);
        JavaType valueType = JavaType.constructType(String[].class, String[].class);
        MapType.construct(Map.class, null, null, null, keyType, valueType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeConstructors_IllegalArgumentException_MapTypeNull() {
        JavaType keyType = JavaType.constructType(int.class, int.class);
        JavaType valueType = JavaType.constructType(String.class, String.class);
        MapType mapType = null;
        assertThrows(IllegalArgumentException.class, () -> MapType.construct(Map.class, null, null, null, keyType, valueType, mapType));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeConstructors_IllegalArgumentException_BindingsNull() {
        JavaType keyType = JavaType.constructType(int.class, int.class);
        JavaType valueType = JavaType.constructType(String.class, String.class);
        MapType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        assertThrows(IllegalArgumentException.class, () -> mapType.withBindings(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeConstructors_IllegalArgumentException_SuperClassNull() {
        JavaType keyType = JavaType.constructType(int.class, int.class);
        JavaType valueType = JavaType.constructType(String.class, String.class);
        MapType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        assertThrows(IllegalArgumentException.class, () -> mapType.withSuperClass(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeConstructors_IllegalArgumentException_SuperInterfacesNull() {
        JavaType keyType = JavaType.constructType(int.class, int.class);
        JavaType valueType = JavaType.constructType(String.class, String.class);
        MapType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        JavaType[] superInterfaces = null;
        assertThrows(IllegalArgumentException.class, () -> mapType.withSuperInterfaces(superInterfaces));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeConstructors_IllegalArgumentException_KeyTypeNull() {
        JavaType valueType = JavaType.constructType(String.class, String.class);
        MapType.construct(Map.class, null, null, null, null, valueType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeConstructors_IllegalArgumentException_ValueTypeNull() {
        JavaType keyType = JavaType.constructType(int.class, int.class);
        MapType.construct(Map.class, null, null, null, keyType, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeConstructors_IllegalArgumentException_ValueTypeNotMapLikeType() {
        JavaType keyType = JavaType.constructType(int.class, int.class);
        JavaType valueType = JavaType.constructType(String.class, Integer.TYPE);
        assertThrows(IllegalArgumentException.class, () -> MapType.construct(Map.class, null, null, null, keyType, valueType));
    }

    @Test(expected = NullPointerException.class)
    public void testMapTypeOtherMethods_NullPointerException() {
        JavaType keyType = JavaType.constructType(int.class, int.class);
        JavaType valueType = JavaType.constructType(String.class, String.class);
        MapType mapType = MapType.construct(Map.class, null, null, null, keyType, valueType);
        mapType.withBindings(null);
    }
}