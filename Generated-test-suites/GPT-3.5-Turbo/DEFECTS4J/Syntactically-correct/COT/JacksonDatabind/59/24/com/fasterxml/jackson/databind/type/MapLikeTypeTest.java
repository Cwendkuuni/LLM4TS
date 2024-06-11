
import org.junit.Assert;
import org.junit.Test;

public class MapLikeTypeTest {

    @Test
    public void testIsContainerType() {
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, null, null, null, null, false);
        Assert.assertTrue(mapLikeType.isContainerType());
    }

    @Test
    public void testIsMapLikeType() {
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, null, null, null, null, false);
        Assert.assertTrue(mapLikeType.isMapLikeType());
    }

    @Test
    public void testGetKeyType() {
        JavaType keyType = new JavaType();
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, keyType, null, null, null, false);
        Assert.assertEquals(keyType, mapLikeType.getKeyType());
    }

    @Test
    public void testGetContentType() {
        JavaType contentType = new JavaType();
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, null, contentType, null, null, false);
        Assert.assertEquals(contentType, mapLikeType.getContentType());
    }

    @Test
    public void testGetContentValueHandler() {
        Object contentValueHandler = new Object();
        JavaType contentType = new JavaType();
        contentType.setValueHandler(contentValueHandler);
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, null, contentType, null, null, false);
        Assert.assertEquals(contentValueHandler, mapLikeType.getContentValueHandler());
    }

    @Test
    public void testGetContentTypeHandler() {
        Object contentTypeHandler = new Object();
        JavaType contentType = new JavaType();
        contentType.setTypeHandler(contentTypeHandler);
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, null, contentType, null, null, false);
        Assert.assertEquals(contentTypeHandler, mapLikeType.getContentTypeHandler());
    }

    @Test
    public void testHasKeyValueHandlers() {
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        JavaType keyType = new JavaType();
        JavaType valueType = new JavaType();
        keyType.setValueHandler(valueHandler);
        keyType.setTypeHandler(typeHandler);
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, keyType, valueType, null, null, false);
        Assert.assertTrue(mapLikeType.hasHandlers());
    }

    @Test
    public void testHasKeyTypeHandler() {
        Object keyTypeHandler = new Object();
        JavaType keyType = new JavaType();
        keyType.setTypeHandler(keyTypeHandler);
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, keyType, null, null, null, false);
        Assert.assertEquals(keyTypeHandler, mapLikeType.getKeyType().getTypeHandler());
    }

    @Test
    public void testHasKeyValueHandler() {
        Object keyValueHandler = new Object();
        JavaType keyType = new JavaType();
        keyType.setValueHandler(keyValueHandler);
        MapLikeType mapLikeType = new MapLikeType(null, null, null, null, keyType, null, null, null, false);
        Assert.assertEquals(keyValueHandler, mapLikeType.getKeyType().getValueHandler());
    }

    @Test
    public void testIsTrueMapType() {
        MapLikeType mapLikeType = new MapLikeType(Map.class, null, null, null, null, null, null, null, false);
        Assert.assertTrue(mapLikeType.isTrueMapType());
    }

    @Test
    public void testEquals() {
        JavaType keyType = new JavaType();
        JavaType valueType = new JavaType();
        MapLikeType mapLikeType1 = new MapLikeType(null, null, null, null, keyType, valueType, null, null, false);
        MapLikeType mapLikeType2 = new MapLikeType(null, null, null, null, keyType, valueType, null, null, false);
        Assert.assertEquals(mapLikeType1, mapLikeType2);
    }
}
