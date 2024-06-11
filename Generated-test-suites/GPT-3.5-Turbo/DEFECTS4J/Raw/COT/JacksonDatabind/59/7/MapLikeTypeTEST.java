import static org.junit.Assert.*;
import org.junit.Test;

public class MapLikeTypeTest {

    @Test
    public void testWithKeyType() {
        // Given
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        JavaType keyType = new JavaType(String.class);

        // When
        MapLikeType result = mapLikeType.withKeyType(keyType);

        // Then
        assertEquals(keyType, result.getKeyType());
    }

    @Test
    public void testWithContentType() {
        // Given
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        JavaType contentType = new JavaType(Integer.class);

        // When
        MapLikeType result = mapLikeType.withContentType(contentType);

        // Then
        assertEquals(contentType, result.getContentType());
    }

    @Test
    public void testWithKeyTypeHandler() {
        // Given
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        Object typeHandler = new Object();

        // When
        MapLikeType result = mapLikeType.withKeyTypeHandler(typeHandler);

        // Then
        assertEquals(typeHandler, result.getKeyType().getTypeHandler());
    }

    @Test
    public void testWithKeyValueHandler() {
        // Given
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        Object valueHandler = new Object();

        // When
        MapLikeType result = mapLikeType.withKeyValueHandler(valueHandler);

        // Then
        assertEquals(valueHandler, result.getContentType().getValueHandler());
    }

    @Test
    public void testIsTrueMapType() {
        // Given
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);

        // When
        boolean result = mapLikeType.isTrueMapType();

        // Then
        assertTrue(result);
    }

    @Test
    public void testEquals() {
        // Given
        MapLikeType mapLikeType1 = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        MapLikeType mapLikeType2 = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);

        // Then
        assertEquals(mapLikeType1, mapLikeType2);
    }

}