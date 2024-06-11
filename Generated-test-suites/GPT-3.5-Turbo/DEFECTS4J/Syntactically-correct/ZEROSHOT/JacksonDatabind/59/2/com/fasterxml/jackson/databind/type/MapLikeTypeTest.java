import org.junit.Test;
import static org.junit.Assert.*;

public class MapLikeTypeTest {

    @Test
    public void testWithKeyType() {
        JavaType keyType = new JavaType();
        JavaType valueType = new JavaType();
        MapLikeType mapLikeType = new MapLikeType(keyType, valueType);

        MapLikeType result = mapLikeType.withKeyType(keyType);

        assertSame(keyType, result.getKeyType());
    }

    @Test
    public void testWithContentType() {
        JavaType keyType = new JavaType();
        JavaType valueType = new JavaType();
        MapLikeType mapLikeType = new MapLikeType(keyType, valueType);

        MapLikeType result = mapLikeType.withContentType(valueType);

        assertSame(valueType, result.getContentType());
    }

    @Test
    public void testWithStaticTyping() {
        JavaType keyType = new JavaType();
        JavaType valueType = new JavaType();
        MapLikeType mapLikeType = new MapLikeType(keyType, valueType);

        MapLikeType result = mapLikeType.withStaticTyping();

        assertTrue(result.isStaticTyping());
    }

    @Test
    public void testWithKeyTypeHandler() {
        JavaType keyType = new JavaType();
        JavaType valueType = new JavaType();
        MapLikeType mapLikeType = new MapLikeType(keyType, valueType);

        MapLikeType result = mapLikeType.withKeyTypeHandler(null);

        assertSame(null, result.getKeyType().getTypeHandler());
    }

    @Test
    public void testWithKeyValueHandler() {
        JavaType keyType = new JavaType();
        JavaType valueType = new JavaType();
        MapLikeType mapLikeType = new MapLikeType(keyType, valueType);

        MapLikeType result = mapLikeType.withKeyValueHandler(null);

        assertSame(null, result.getKeyType().getValueHandler());
    }

}