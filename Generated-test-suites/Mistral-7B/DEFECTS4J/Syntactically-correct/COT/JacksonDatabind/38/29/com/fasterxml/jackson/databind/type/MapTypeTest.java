import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.MapType;

@RunWith(JUnit4.class)
public class MapTypeTest {

    @Test
    public void testMapTypeConstructor() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        MapType expectedMapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);

        MapType actualMapType = MapType.construct(rawType, bindings, null, null, keyType, valueType);
        assertEquals(expectedMapType, actualMapType);
    }

    @Test
    public void testMapTypeWithTypeHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        Object valueHandler = new Object() {};
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType);

        MapType actualMapType = mapType.withTypeHandler(valueHandler);
        MapType expectedMapType = new MapType(rawType, bindings, null, null, keyType, valueType, valueHandler, null, false);
        assertEquals(expectedMapType, actualMapType);
    }

    @Test
    public void testMapTypeWithContentTypeHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        Object contentTypeHandler = new Object() {};
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType);

        MapType actualMapType = mapType.withContentTypeHandler(contentTypeHandler);
        MapType expectedMapType = new MapType(rawType, bindings, null, null, keyType, valueType.withTypeHandler(contentTypeHandler), null, null, false);
        assertEquals(expectedMapType, actualMapType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeWithInvalidTypeHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        Object invalidTypeHandler = new Object() {};
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType);

        MapType actualMapType = mapType.withTypeHandler(invalidTypeHandler);
        fail("Should throw IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeWithInvalidContentTypeHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        Object invalidContentTypeHandler = new Object() {};
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType);

        MapType actualMapType = mapType.withContentTypeHandler(invalidContentTypeHandler);
        fail("Should throw IllegalArgumentException");
    }

    @Test
    public void testMapTypeWithValueHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        Object valueHandler = new Object() {};
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType);

        MapType actualMapType = mapType.withValueHandler(valueHandler);
        MapType expectedMapType = new MapType(rawType, bindings, null, null, keyType, valueType, null, valueHandler, false);
        assertEquals(expectedMapType, actualMapType);
    }

    @Test
    public void testMapTypeWithContentValueHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        Object contentValueHandler = new Object() {};
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType);

        MapType actualMapType = mapType.withContentValueHandler(contentValueHandler);
        MapType expectedMapType = new MapType(rawType, bindings, null, null, keyType, valueType.withValueHandler(contentValueHandler), null, null, false);
        assertEquals(expectedMapType, actualMapType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeWithInvalidContentValueHandler() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        Object invalidContentValueHandler = new Object() {};
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType);

        MapType actualMapType = mapType.withContentValueHandler(invalidContentValueHandler);
        fail("Should throw IllegalArgumentException");
    }

    @Test
    public void testMapTypeWithStaticTyping() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType);

        MapType actualMapType = mapType.withStaticTyping();
        MapType expectedMapType = new MapType(rawType, bindings, null, null, keyType.withStaticTyping(), valueType.withStaticTyping(), null, null, true);
        assertEquals(expectedMapType, actualMapType);
    }

    @Test
    public void testMapTypeWithContentType() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        JavaType contentType = JavaType.constructCollectionType(ArrayList.class, String.class);
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType);

        MapType actualMapType = mapType.withContentType(contentType);
        MapType expectedMapType = new MapType(rawType, bindings, null, null, keyType, contentType, null, null, false);
        assertEquals(expectedMapType, actualMapType);
    }

    @Test
    public void testMapTypeWithKeyType() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        JavaType newKeyType = JavaType.constructCollectionType(ArrayList.class, String.class);
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType);

        MapType actualMapType = mapType.withKeyType(newKeyType);
        MapType expectedMapType = new MapType(rawType, bindings, null, null, newKeyType, valueType, null, null, false);
        assertEquals(expectedMapType, actualMapType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapTypeWithInvalidKeyType() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        JavaType invalidKeyType = JavaType.constructCollectionType(Map.class, String.class);
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType);

        MapType actualMapType = mapType.withKeyType(invalidKeyType);
        fail("Should throw IllegalArgumentException");
    }

    @Test
    public void testMapTypeRefine() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        JavaType refinedKeyType = JavaType.constructCollectionType(ArrayList.class, String.class);
        JavaType refinedValueType = JavaType.constructCollectionType(ArrayList.class, Integer.class);
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType);

        MapType actualMapType = mapType.refine(Map.class, bindings, null, null);
        MapType expectedMapType = new MapType(rawType, bindings, null, null, refinedKeyType, refinedValueType, null, null, false);
        assertEquals(expectedMapType, actualMapType);
    }

    @Test
    public void testMapTypeToString() {
        Class<?> rawType = Map.class;
        TypeBindings bindings = TypeBindings.empty();
        JavaType keyType = JavaType.constructCollectionType(List.class, String.class);
        JavaType valueType = JavaType.constructCollectionType(List.class, Integer.class);
        MapType mapType = new MapType(rawType, bindings, null, null, keyType, valueType);

        String expectedString = "[map type; class " + rawType.getName() + ", " + keyType + " -> " + valueType + "]";
        String actualString = mapType.toString();
        assertEquals(expectedString, actualString);
    }
}