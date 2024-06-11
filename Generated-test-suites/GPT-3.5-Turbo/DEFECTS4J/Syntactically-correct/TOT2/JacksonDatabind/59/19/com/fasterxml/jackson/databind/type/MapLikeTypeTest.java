import org.junit.Test;
import static org.junit.Assert.*;

public class MapLikeTypeTest {

    @Test
    public void testUpgradeFrom() {
        // Test upgrading from basic type
        JavaType baseType = new SimpleType(String.class);
        JavaType keyType = new SimpleType(String.class);
        JavaType valueType = new SimpleType(Integer.class);
        
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);
        
        assertNotNull(mapLikeType);
        assertEquals(String.class, mapLikeType.getKeyType().getRawClass());
        assertEquals(Integer.class, mapLikeType.getContentType().getRawClass());
    }

    @Test
    public void testConstruct() {
        // Test constructing MapLikeType
        Class<?> rawType = HashMap.class;
        JavaType keyType = new SimpleType(String.class);
        JavaType valueType = new SimpleType(Integer.class);

        MapLikeType mapLikeType = MapLikeType.construct(rawType, keyType, valueType);

        assertNotNull(mapLikeType);
        assertEquals(HashMap.class, mapLikeType.getRawClass());
        assertEquals(String.class, mapLikeType.getKeyType().getRawClass());
        assertEquals(Integer.class, mapLikeType.getContentType().getRawClass());
    }

    @Test
    public void testWithKeyType() {
        // Test setting key type
        JavaType keyType = new SimpleType(String.class);
        JavaType valueType = new SimpleType(Integer.class);

        MapLikeType mapLikeType = new MapLikeType(HashMap.class, null, null, null, null, null, null, null, false);
        MapLikeType updatedType = mapLikeType.withKeyType(keyType);

        assertNotNull(updatedType);
        assertEquals(String.class, updatedType.getKeyType().getRawClass());
    }

    @Test
    public void testWithContentType() {
        // Test setting content type
        JavaType keyType = new SimpleType(String.class);
        JavaType valueType = new SimpleType(Integer.class);

        MapLikeType mapLikeType = new MapLikeType(HashMap.class, null, null, null, keyType, null, null, null, false);
        MapLikeType updatedType = mapLikeType.withContentType(valueType);

        assertNotNull(updatedType);
        assertEquals(Integer.class, updatedType.getContentType().getRawClass());
    }

    // Other test methods for remaining public methods in the class

}