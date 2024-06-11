import static org.junit.Assert.*;
import org.junit.Test;

public class MapLikeTypeTest {

    @Test
    public void testUpgradeFrom() {
        JavaType baseType = new SimpleType(String.class);
        JavaType keyType = new SimpleType(String.class);
        JavaType valueType = new SimpleType(Integer.class);
        MapLikeType mapLikeType = MapLikeType.upgradeFrom(baseType, keyType, valueType);

        assertEquals(baseType, mapLikeType.getContentType());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getContentType());
    }

    @Test
    public void testConstruct() {
        JavaType keyType = new SimpleType(String.class);
        JavaType valueType = new SimpleType(Integer.class);
        MapLikeType mapLikeType = MapLikeType.construct(HashMap.class, keyType, valueType);

        assertEquals(HashMap.class, mapLikeType.getRawClass());
        assertEquals(keyType, mapLikeType.getKeyType());
        assertEquals(valueType, mapLikeType.getContentType());
    }

    @Test
    public void testWithKeyType() {
        JavaType keyType = new SimpleType(String.class);
        JavaType valueType = new SimpleType(Integer.class);
        MapLikeType mapLikeType = new MapLikeType(HashMap.class, keyType, valueType);
        JavaType newKeyType = new SimpleType(Double.class);

        MapLikeType updatedMapLikeType = mapLikeType.withKeyType(newKeyType);

        assertEquals(HashMap.class, updatedMapLikeType.getRawClass());
        assertEquals(newKeyType, updatedMapLikeType.getKeyType());
        assertEquals(valueType, updatedMapLikeType.getContentType());
    }

    // More test cases for other methods should be added here

}