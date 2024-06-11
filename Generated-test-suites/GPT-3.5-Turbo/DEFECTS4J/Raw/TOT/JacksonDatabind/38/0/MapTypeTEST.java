import com.fasterxml.jackson.databind.JavaType;
import org.junit.Assert;
import org.junit.Test;

public class MapTypeTest {

    @Test
    public void construct_WithRawTypeAndBindings_ShouldCreateMapType() {
        // Arrange
        Class<?> rawType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;

        // Act
        MapType mapType = MapType.construct(rawType, bindings, superClass, superInts, keyT, valueT);

        // Assert
        Assert.assertEquals(rawType, mapType.getRawClass());
        Assert.assertEquals(bindings, mapType.getBindings());
        Assert.assertEquals(superClass, mapType.getSuperClass());
        Assert.assertArrayEquals(superInts, mapType.getInterfaces());
        Assert.assertEquals(keyT, mapType.getKeyType());
        Assert.assertEquals(valueT, mapType.getContentType());
    }

    @Test
    public void construct_WithRawTypeAndKeyValueType_ShouldCreateMapType() {
        // Arrange
        Class<?> rawType = String.class;
        JavaType keyT = String.class;
        JavaType valueT = Integer.class;

        // Act
        MapType mapType = MapType.construct(rawType, keyT, valueT);

        // Assert
        Assert.assertEquals(rawType, mapType.getRawClass());
        Assert.assertEquals(keyT, mapType.getKeyType());
        Assert.assertEquals(valueT, mapType.getContentType());
    }

    @Test
    public void withTypeHandler_WithTypeHandler_ShouldSetTypeHandler() {
        // Arrange
        Object typeHandler = new Object();

        // Act
        MapType mapType = new MapType().withTypeHandler(typeHandler);

        // Assert
        Assert.assertEquals(typeHandler, mapType.getTypeHandler());
    }

    @Test
    public void withContentTypeHandler_WithContentTypeHandler_ShouldSetContentTypeHandler() {
        // Arrange
        Object contentTypeHandler = new Object();

        // Act
        MapType mapType = new MapType().withContentTypeHandler(contentTypeHandler);

        // Assert
        Assert.assertEquals(contentTypeHandler, mapType.getContentTypeHandler());
    }

    @Test
    public void withValueHandler_WithValueHandler_ShouldSetValueHandler() {
        // Arrange
        Object valueHandler = new Object();

        // Act
        MapType mapType = new MapType().withValueHandler(valueHandler);

        // Assert
        Assert.assertEquals(valueHandler, mapType.getValueHandler());
    }

    @Test
    public void withContentValueHandler_WithContentValueHandler_ShouldSetContentValueHandler() {
        // Arrange
        Object contentValueHandler = new Object();

        // Act
        MapType mapType = new MapType().withContentValueHandler(contentValueHandler);

        // Assert
        Assert.assertEquals(contentValueHandler, mapType.getContentType().getValueHandler());
    }

    @Test
    public void withStaticTyping_WhenStatic_ShouldReturnSameMapType() {
        // Arrange
        MapType mapType = new MapType().withStaticTyping();

        // Act
        MapType result = mapType.withStaticTyping();

        // Assert
        Assert.assertEquals(mapType, result);
    }

    @Test
    public void withStaticTyping_WhenNotStatic_ShouldReturnNewMapTypeWithStaticTyping() {
        // Arrange
        MapType mapType = new MapType();

        // Act
        MapType result = mapType.withStaticTyping();

        // Assert
        Assert.assertNotEquals(mapType, result);
        Assert.assertTrue(result.isStatic());
    }

    @Test
    public void withContentType_WithContentType_ShouldReturnNewMapTypeWithContentType() {
        // Arrange
        MapType mapType = new MapType();
        JavaType contentType = new JavaType();

        // Act
        MapType result = mapType.withContentType(contentType);

        // Assert
        Assert.assertNotEquals(mapType, result);
        Assert.assertEquals(contentType, result.getContentType());
    }

    @Test
    public void withKeyType_WithKeyType_ShouldReturnNewMapTypeWithKeyType() {
        // Arrange
        MapType mapType = new MapType();
        JavaType keyType = new JavaType();

        // Act
        MapType result = mapType.withKeyType(keyType);

        // Assert
        Assert.assertNotEquals(mapType, result);
        Assert.assertEquals(keyType, result.getKeyType());
    }

    @Test
    public void refine_WithRawTypeAndBindings_ShouldReturnNewMapTypeWithRefinedValues() {
        // Arrange
        MapType mapType = new MapType();
        Class<?> rawType = String.class;
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;

        // Act
        JavaType result = mapType.refine(rawType, bindings, superClass, superInterfaces);

        // Assert
        Assert.assertNotEquals(mapType, result);
        Assert.assertEquals(rawType, result.getRawClass());
        Assert.assertEquals(bindings, result.getBindings());
        Assert.assertEquals(superClass, result.getSuperClass());
        Assert.assertArrayEquals(superInterfaces, result.getInterfaces());
    }

    @Test
    public void withKeyTypeHandler_WithTypeHandler_ShouldReturnNewMapTypeWithKeyTypeHandler() {
        // Arrange
        MapType mapType = new MapType();
        Object keyTypeHandler = new Object();

        // Act
        MapType result = mapType.withKeyTypeHandler(keyTypeHandler);

        // Assert
        Assert.assertNotEquals(mapType, result);
        Assert.assertEquals(keyTypeHandler, result.getKeyType().getTypeHandler());
    }

    @Test
    public void withKeyValueHandler_WithValueHandler_ShouldReturnNewMapTypeWithKeyValueHandler() {
        // Arrange
        MapType mapType = new MapType();
        Object keyValueHandler = new Object();

        // Act
        MapType result = mapType.withKeyValueHandler(keyValueHandler);

        // Assert
        Assert.assertNotEquals(mapType, result);
        Assert.assertEquals(keyValueHandler, result.getKeyType().getValueHandler());
    }

    @Test
    public void toString_WhenCalled_ShouldReturnStringRepresentation() {
        // Arrange
        MapType mapType = new MapType();
        String expected = "[map type; class " + mapType.getRawClass().getName() + ", " + mapType.getKeyType() + " -> " + mapType.getContentType() + "]";

        // Act
        String result = mapType.toString();

        // Assert
        Assert.assertEquals(expected, result);
    }
}