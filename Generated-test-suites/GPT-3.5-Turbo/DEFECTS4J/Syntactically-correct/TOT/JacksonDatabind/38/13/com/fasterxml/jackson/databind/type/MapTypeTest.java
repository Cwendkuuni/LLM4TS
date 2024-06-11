import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MapTypeTest {

    private MapType mapType;

    @Before
    public void setup() {
        mapType = new MapType(null, null, null, null, null, null, null, null, false);
    }

    @Test
    public void construct_withRawTypeAndBindings_shouldCreateMapType() {
        // Given
        Class<?> rawType = MapTypeTest.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType keyT = null;
        JavaType valueT = null;

        // When
        MapType result = MapType.construct(rawType, bindings, superClass, superInts, keyT, valueT);

        // Then
        assertNotNull(result);
        assertEquals(rawType, result.getRawClass());
        assertNull(result.getKeyType());
        assertNull(result.getValueType());
    }

    @Test
    public void construct_withRawTypeAndKeyTAndValueT_shouldCreateMapType() {
        // Given
        Class<?> rawType = MapTypeTest.class;
        JavaType keyT = null;
        JavaType valueT = null;

        // When
        MapType result = MapType.construct(rawType, keyT, valueT);

        // Then
        assertNotNull(result);
        assertEquals(rawType, result.getRawClass());
        assertNull(result.getKeyType());
        assertNull(result.getValueType());
    }

    @Test
    public void withTypeHandler_withHandler_shouldReturnNewMapTypeWithHandler() {
        // Given
        Object handler = new Object();

        // When
        MapType result = mapType.withTypeHandler(handler);

        // Then
        assertNotNull(result);
        assertEquals(handler, result.getTypeHandler());
    }

    @Test
    public void withContentTypeHandler_withHandler_shouldReturnNewMapTypeWithContentTypeHandler() {
        // Given
        Object handler = new Object();

        // When
        MapType result = mapType.withContentTypeHandler(handler);

        // Then
        assertNotNull(result);
        assertEquals(handler, result.getContentType().getTypeHandler());
    }

    @Test
    public void withValueHandler_withHandler_shouldReturnNewMapTypeWithValueHandler() {
        // Given
        Object handler = new Object();

        // When
        MapType result = mapType.withValueHandler(handler);

        // Then
        assertNotNull(result);
        assertEquals(handler, result.getValueHandler());
    }

    @Test
    public void withContentValueHandler_withHandler_shouldReturnNewMapTypeWithContentValueHandler() {
        // Given
        Object handler = new Object();

        // When
        MapType result = mapType.withContentValueHandler(handler);

        // Then
        assertNotNull(result);
        assertEquals(handler, result.getContentType().getValueHandler());
    }

    @Test
    public void withStaticTyping_whenAsStaticIsTrue_shouldReturnNewMapTypeWithStaticTyping() {
        // Given
        mapType = mapType.withStaticTyping();

        // When
        MapType result = mapType.withStaticTyping();

        // Then
        assertSame(mapType, result);
    }

    @Test
    public void withStaticTyping_whenAsStaticIsFalse_shouldReturnNewMapTypeWithStaticTyping() {
        // Given
        MapType nonStaticMapType = mapType.withStaticTyping();

        // When
        MapType result = nonStaticMapType.withStaticTyping();

        // Then
        assertNotSame(nonStaticMapType, result);
        assertEquals(nonStaticMapType.getClass(), result.getClass());
        assertEquals(nonStaticMapType.getRawClass(), result.getRawClass());
        assertEquals(nonStaticMapType.getKeyType(), result.getKeyType());
        assertEquals(nonStaticMapType.getContentType(), result.getContentType());
        assertEquals(nonStaticMapType.isStatic(), result.isStatic());
    }

    @Test
    public void withContentType_withContentType_shouldReturnNewMapTypeWithContentType() {
        // Given
        JavaType contentType = new AnyJavaType();

        // When
        MapType result = mapType.withContentType(contentType);

        // Then
        assertNotNull(result);
        assertEquals(contentType, result.getContentType());
    }

    @Test
    public void withKeyType_withKeyType_shouldReturnNewMapTypeWithKeyType() {
        // Given
        JavaType keyType = new AnyJavaType();

        // When
        MapType result = mapType.withKeyType(keyType);

        // Then
        assertNotNull(result);
        assertEquals(keyType, result.getKeyType());
    }

    @Test
    public void refine_withRawTypeAndBindingsAndSuperClassAndSuperInterfaces_shouldReturnNewMapTypeWithRefinedType() {
        // Given
        Class<?> rawType = MapTypeTest.class;
        TypeBindings bindings = null;
        JavaType superClass = null;
        JavaType[] superInterfaces = null;

        // When
        MapType result = mapType.refine(rawType, bindings, superClass, superInterfaces);

        // Then
        assertNotNull(result);
        assertEquals(rawType, result.getRawClass());
    }

    @Test
    public void withKeyTypeHandler_withHandler_shouldReturnNewMapTypeWithKeyTypeHandler() {
        // Given
        Object handler = new Object();

        // When
        MapType result = mapType.withKeyTypeHandler(handler);

        // Then
        assertNotNull(result);
        assertEquals(handler, result.getKeyType().getTypeHandler());
    }

    @Test
    public void withKeyValueHandler_withHandler_shouldReturnNewMapTypeWithKeyValueHandler() {
        // Given
        Object handler = new Object();

        // When
        MapType result = mapType.withKeyValueHandler(handler);

        // Then
        assertNotNull(result);
        assertEquals(handler, result.getKeyType().getValueHandler());
    }

    @Test
    public void toString_shouldReturnStringRepresentationOfMapType() {
        // When
        String result = mapType.toString();

        // Then
        assertNotNull(result);
        assertTrue(result.contains("map type"));
        assertTrue(result.contains("class"));
        assertTrue(result.contains("null"));
    }
}