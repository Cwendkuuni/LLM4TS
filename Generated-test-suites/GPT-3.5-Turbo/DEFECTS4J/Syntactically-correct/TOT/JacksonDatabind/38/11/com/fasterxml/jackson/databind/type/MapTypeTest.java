import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

    private MapType mapType;

    @Test
    public void construct_withRawTypeAndTypeBindings_shouldConstructMapType() {
        // Arrange
        Class<?> rawType = Map.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInts = new JavaType[1];
        JavaType keyT = new JavaType();
        JavaType valueT = new JavaType();

        // Act
        mapType = MapType.construct(rawType, bindings, superClass, superInts, keyT, valueT);

        // Assert
        assertNotNull(mapType);
        assertEquals(rawType, mapType.getRawType());
        assertEquals(bindings, mapType.getBindings());
        assertEquals(superClass, mapType.getSuperClass());
        assertArrayEquals(superInts, mapType.getInterfaces());
        assertEquals(keyT, mapType.getKeyType());
        assertEquals(valueT, mapType.getValueType());
    }

    @Test
    public void construct_withRawTypeAndKeyTAndValueT_shouldConstructMapType() {
        // Arrange
        Class<?> rawType = Map.class;
        JavaType keyT = new JavaType();
        JavaType valueT = new JavaType();

        // Act
        mapType = MapType.construct(rawType, keyT, valueT);

        // Assert
        assertNotNull(mapType);
        assertEquals(rawType, mapType.getRawType());
        assertNull(mapType.getBindings());
        assertNull(mapType.getSuperClass());
        assertNull(mapType.getInterfaces());
        assertEquals(keyT, mapType.getKeyType());
        assertEquals(valueT, mapType.getValueType());
    }

    @Test
    public void _narrow_withSubclass_shouldReturnNewMapTypeWithSubclass() {
        // Arrange
        Class<?> subclass = LinkedHashMap.class;

        // Act
        MapType narrowedMapType = (MapType) mapType._narrow(subclass);

        // Assert
        assertNotNull(narrowedMapType);
        assertEquals(subclass, narrowedMapType.getRawType());
        assertEquals(mapType.getBindings(), narrowedMapType.getBindings());
        assertEquals(mapType.getSuperClass(), narrowedMapType.getSuperClass());
        assertArrayEquals(mapType.getInterfaces(), narrowedMapType.getInterfaces());
        assertEquals(mapType.getKeyType(), narrowedMapType.getKeyType());
        assertEquals(mapType.getValueType(), narrowedMapType.getValueType());
    }

    @Test
    public void withTypeHandler_withTypeHandler_shouldReturnNewMapTypeWithTypeHandler() {
        // Arrange
        Object handler = new TypeHandler();

        // Act
        MapType mapTypeWithTypeHandler = mapType.withTypeHandler(handler);

        // Assert
        assertNotNull(mapTypeWithTypeHandler);
        assertEquals(mapType.getRawType(), mapTypeWithTypeHandler.getRawType());
        assertEquals(mapType.getBindings(), mapTypeWithTypeHandler.getBindings());
        assertEquals(mapType.getSuperClass(), mapTypeWithTypeHandler.getSuperClass());
        assertArrayEquals(mapType.getInterfaces(), mapTypeWithTypeHandler.getInterfaces());
        assertEquals(mapType.getKeyType(), mapTypeWithTypeHandler.getKeyType());
        assertEquals(mapType.getValueType(), mapTypeWithTypeHandler.getValueType());
        assertEquals(handler, mapTypeWithTypeHandler.getTypeHandler());
    }

    @Test
    public void withContentTypeHandler_withContentValueHandler_shouldReturnNewMapTypeWithContentValueHandler() {
        // Arrange
        Object handler = new TypeHandler();

        // Act
        MapType mapTypeWithContentValueHandler = mapType.withContentValueHandler(handler);

        // Assert
        assertNotNull(mapTypeWithContentValueHandler);
        assertEquals(mapType.getRawType(), mapTypeWithContentValueHandler.getRawType());
        assertEquals(mapType.getBindings(), mapTypeWithContentValueHandler.getBindings());
        assertEquals(mapType.getSuperClass(), mapTypeWithContentValueHandler.getSuperClass());
        assertArrayEquals(mapType.getInterfaces(), mapTypeWithContentValueHandler.getInterfaces());
        assertEquals(mapType.getKeyType(), mapTypeWithContentValueHandler.getKeyType());
        assertEquals(mapType.getValueType().withValueHandler(handler), mapTypeWithContentValueHandler.getValueType());
        assertEquals(mapType.getValueHandler(), mapTypeWithContentValueHandler.getValueHandler());
        assertEquals(mapType.getTypeHandler(), mapTypeWithContentValueHandler.getTypeHandler());
    }

    @Test
    public void withValueHandler_withValueHandler_shouldReturnNewMapTypeWithValueHandler() {
        // Arrange
        Object handler = new ValueHandler();

        // Act
        MapType mapTypeWithValueHandler = mapType.withValueHandler(handler);

        // Assert
        assertNotNull(mapTypeWithValueHandler);
        assertEquals(mapType.getRawType(), mapTypeWithValueHandler.getRawType());
        assertEquals(mapType.getBindings(), mapTypeWithValueHandler.getBindings());
        assertEquals(mapType.getSuperClass(), mapTypeWithValueHandler.getSuperClass());
        assertArrayEquals(mapType.getInterfaces(), mapTypeWithValueHandler.getInterfaces());
        assertEquals(mapType.getKeyType(), mapTypeWithValueHandler.getKeyType());
        assertEquals(mapType.getValueType(), mapTypeWithValueHandler.getValueType());
        assertEquals(handler, mapTypeWithValueHandler.getValueHandler());
        assertEquals(mapType.getTypeHandler(), mapTypeWithValueHandler.getTypeHandler());
    }

    @Test
    public void withContentValueHandler_withValueHandler_shouldReturnNewMapTypeWithContentValueHandler() {
        // Arrange
        Object handler = new ValueHandler();

        // Act
        MapType mapTypeWithContentValueHandler = mapType.withContentValueHandler(handler);

        // Assert
        assertNotNull(mapTypeWithContentValueHandler);
        assertEquals(mapType.getRawType(), mapTypeWithContentValueHandler.getRawType());
        assertEquals(mapType.getBindings(), mapTypeWithContentValueHandler.getBindings());
        assertEquals(mapType.getSuperClass(), mapTypeWithContentValueHandler.getSuperClass());
        assertArrayEquals(mapType.getInterfaces(), mapTypeWithContentValueHandler.getInterfaces());
        assertEquals(mapType.getKeyType(), mapTypeWithContentValueHandler.getKeyType());
        assertEquals(mapType.getValueType().withValueHandler(handler), mapTypeWithContentValueHandler.getValueType());
        assertEquals(mapType.getValueHandler(), mapTypeWithContentValueHandler.getValueHandler());
        assertEquals(mapType.getTypeHandler(), mapTypeWithContentValueHandler.getTypeHandler());
    }

    @Test
    public void withStaticTyping_withStaticTyping_shouldReturnNewMapTypeWithStaticTyping() {
        // Act
        MapType mapTypeWithStaticTyping = mapType.withStaticTyping();

        // Assert
        assertNotNull(mapTypeWithStaticTyping);
        assertEquals(mapType.getRawType(), mapTypeWithStaticTyping.getRawType());
        assertEquals(mapType.getBindings(), mapTypeWithStaticTyping.getBindings());
        assertEquals(mapType.getSuperClass(), mapTypeWithStaticTyping.getSuperClass());
        assertArrayEquals(mapType.getInterfaces(), mapTypeWithStaticTyping.getInterfaces());
        assertEquals(mapType.getKeyType().withStaticTyping(), mapTypeWithStaticTyping.getKeyType());
        assertEquals(mapType.getValueType().withStaticTyping(), mapTypeWithStaticTyping.getValueType());
        assertEquals(mapType.getValueHandler(), mapTypeWithStaticTyping.getValueHandler());
        assertEquals(mapType.getTypeHandler(), mapTypeWithStaticTyping.getTypeHandler());
        assertTrue(mapTypeWithStaticTyping.isStatic());
    }

    @Test
    public void withContentType_withContentType_shouldReturnNewMapTypeWithContentType() {
        // Arrange
        JavaType contentType = new JavaType();

        // Act
        MapType mapTypeWithContentType = mapType.withContentType(contentType);

        // Assert
        assertNotNull(mapTypeWithContentType);
        assertEquals(mapType.getRawType(), mapTypeWithContentType.getRawType());
        assertEquals(mapType.getBindings(), mapTypeWithContentType.getBindings());
        assertEquals(mapType.getSuperClass(), mapTypeWithContentType.getSuperClass());
        assertArrayEquals(mapType.getInterfaces(), mapTypeWithContentType.getInterfaces());
        assertEquals(mapType.getKeyType(), mapTypeWithContentType.getKeyType());
        assertEquals(contentType, mapTypeWithContentType.getValueType());
        assertEquals(mapType.getValueHandler(), mapTypeWithContentType.getValueHandler());
        assertEquals(mapType.getTypeHandler(), mapTypeWithContentType.getTypeHandler());
        assertEquals(mapType.isStatic(), mapTypeWithContentType.isStatic());
    }

    @Test
    public void withKeyType_withKeyType_shouldReturnNewMapTypeWithKeyType() {
        // Arrange
        JavaType keyType = new JavaType();

        // Act
        MapType mapTypeWithKeyType = mapType.withKeyType(keyType);

        // Assert
        assertNotNull(mapTypeWithKeyType);
        assertEquals(mapType.getRawType(), mapTypeWithKeyType.getRawType());
        assertEquals(mapType.getBindings(), mapTypeWithKeyType.getBindings());
        assertEquals(mapType.getSuperClass(), mapTypeWithKeyType.getSuperClass());
        assertArrayEquals(mapType.getInterfaces(), mapTypeWithKeyType.getInterfaces());
        assertEquals(keyType, mapTypeWithKeyType.getKeyType());
        assertEquals(mapType.getValueType(), mapTypeWithKeyType.getValueType());
        assertEquals(mapType.getValueHandler(), mapTypeWithKeyType.getValueHandler());
        assertEquals(mapType.getTypeHandler(), mapTypeWithKeyType.getTypeHandler());
        assertEquals(mapType.isStatic(), mapTypeWithKeyType.isStatic());
    }

    @Test
    public void refine_withRawTypeAndBindingsAndSuperClassAndSuperInterfaces_shouldReturnNewMapTypeWithRefinedInfo() {
        // Arrange
        Class<?> rawType = LinkedHashMap.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = new JavaType();
        JavaType[] superInterfaces = new JavaType[1];
        
        // Act
        MapType refinedMapType = (MapType) mapType.refine(rawType, bindings, superClass, superInterfaces);

        // Assert
        assertNotNull(refinedMapType);
        assertEquals(rawType, refinedMapType.getRawType());
        assertEquals(bindings, refinedMapType.getBindings());
        assertEquals(superClass, refinedMapType.getSuperClass());
        assertArrayEquals(superInterfaces, refinedMapType.getInterfaces());
        assertEquals(mapType.getKeyType(), refinedMapType.getKeyType());
        assertEquals(mapType.getValueType(), refinedMapType.getValueType());
        assertEquals(mapType.getValueHandler(), refinedMapType.getValueHandler());
        assertEquals(mapType.getTypeHandler(), refinedMapType.getTypeHandler());
        assertEquals(mapType.isStatic(), refinedMapType.isStatic());
    }

    @Test
    public void withKeyTypeHandler_withKeyTypeHandler_shouldReturnNewMapTypeWithKeyTypeHandler() {
        // Arrange
        Object handler = new TypeHandler();

        // Act
        MapType mapTypeWithKeyTypeHandler = mapType.withKeyTypeHandler(handler);

        // Assert
        assertNotNull(mapTypeWithKeyTypeHandler);
        assertEquals(mapType.getRawType(), mapTypeWithKeyTypeHandler.getRawType());
        assertEquals(mapType.getBindings(), mapTypeWithKeyTypeHandler.getBindings());
        assertEquals(mapType.getSuperClass(), mapTypeWithKeyTypeHandler.getSuperClass());
        assertArrayEquals(mapType.getInterfaces(), mapTypeWithKeyTypeHandler.getInterfaces());
        assertEquals(mapType.getKeyType().withTypeHandler(handler), mapTypeWithKeyTypeHandler.getKeyType());
        assertEquals(mapType.getValueType(), mapTypeWithKeyTypeHandler.getValueType());
        assertEquals(mapType.getValueHandler(), mapTypeWithKeyTypeHandler.getValueHandler());
        assertEquals(mapType.getTypeHandler(), mapTypeWithKeyTypeHandler.getTypeHandler());
        assertEquals(mapType.isStatic(), mapTypeWithKeyTypeHandler.isStatic());
    }

    @Test
    public void withKeyValueHandler_withKeyValueHandler_shouldReturnNewMapTypeWithKeyValueHandler() {
        // Arrange
        Object handler = new KeyValueHandler();

        // Act
        MapType mapTypeWithKeyValueHandler = mapType.withKeyValueHandler(handler);

        // Assert
        assertNotNull(mapTypeWithKeyValueHandler);
        assertEquals(mapType.getRawType(), mapTypeWithKeyValueHandler.getRawType());
        assertEquals(mapType.getBindings(), mapTypeWithKeyValueHandler.getBindings());
        assertEquals(mapType.getSuperClass(), mapTypeWithKeyValueHandler.getSuperClass());
        assertArrayEquals(mapType.getInterfaces(), mapTypeWithKeyValueHandler.getInterfaces());
        assertEquals(mapType.getKeyType().withValueHandler(handler), mapTypeWithKeyValueHandler.getKeyType());
        assertEquals(mapType.getValueType(), mapTypeWithKeyValueHandler.getValueType());
        assertEquals(mapType.getValueHandler(), mapTypeWithKeyValueHandler.getValueHandler());
        assertEquals(mapType.getTypeHandler(), mapTypeWithKeyValueHandler.getTypeHandler());
        assertEquals(mapType.isStatic(), mapTypeWithKeyValueHandler.isStatic());
    }

    @Test
    public void toString_shouldReturnStringRepresentation() {
        // Arrange
        String expected = "[map type; class " + mapType.getRawType().getName()
                + ", " + mapType.getKeyType() + " -> " + mapType.getValueType() + "]";

        // Act
        String result = mapType.toString();

        // Assert
        assertEquals(expected, result);
    }
}