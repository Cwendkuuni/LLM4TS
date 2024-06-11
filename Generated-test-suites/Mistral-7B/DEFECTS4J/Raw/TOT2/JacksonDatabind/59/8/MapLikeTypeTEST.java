import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.type.MapLikeType;

public class MapLikeTypeTest {
    @Test
    public void testConstructorWithKeyType() {
        JavaType keyType = new SimpleType("String");
        JavaType valueType = new SimpleType("Integer");
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType);
        assertNotNull(mapLikeType);
        assertEquals(Map.class, mapLikeType.getRawType());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getContentType());
    }
}