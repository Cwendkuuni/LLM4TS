import com.fasterxml.jackson.databind.JavaType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MapLikeTypeTest {

    private MapLikeType mapLikeType;

    @Before
    public void setup() {
        JavaType keyType = new JavaType(Key.class, null);
        JavaType valueType = new JavaType(Value.class, null);
        mapLikeType = new MapLikeType(Map.class, null, null, null, keyType, valueType, null, null, false);
    }

    @Test
    public void testWithKeyType() {
        JavaType newKeyType = new JavaType(String.class, null);
        MapLikeType updatedMapLikeType = mapLikeType.withKeyType(newKeyType);
        Assert.assertEquals(newKeyType, updatedMapLikeType.getKeyType());
    }

    @Test
    public void testWithContentType() {
        JavaType newContentType = new JavaType(Value.class, null);
        MapLikeType updatedMapLikeType = mapLikeType.withContentType(newContentType);
        Assert.assertEquals(newContentType, updatedMapLikeType.getContentType());
    }

    @Test
    public void testWithKeyTypeHandler() {
        Object keyTypeHandler = new Object();
        MapLikeType updatedMapLikeType = mapLikeType.withKeyTypeHandler(keyTypeHandler);
        Assert.assertEquals(keyTypeHandler, updatedMapLikeType.getKeyType().getTypeHandler());
    }

    @Test
    public void testWithKeyValueHandler() {
        Object keyValueHandler = new Object();
        MapLikeType updatedMapLikeType = mapLikeType.withKeyValueHandler(keyValueHandler);
        Assert.assertEquals(keyValueHandler, updatedMapLikeType.getKeyType().getValueHandler());
    }

    @Test
    public void testIsTrueMapType() {
        Assert.assertTrue(mapLikeType.isTrueMapType());
    }

}