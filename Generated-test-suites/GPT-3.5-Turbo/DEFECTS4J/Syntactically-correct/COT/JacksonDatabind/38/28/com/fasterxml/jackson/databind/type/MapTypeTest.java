import org.junit.Assert;
import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        MapType mapType = MapType.construct(Map.class, null, null, null, String.class, Integer.class);
        Assert.assertNotNull(mapType);
        Assert.assertEquals(Map.class, mapType.getRawClass());
        Assert.assertEquals(String.class, mapType.getKeyType());
        Assert.assertEquals(Integer.class, mapType.getValueType());
    }
    
    @Test
    public void testWithKeyType() {
        MapType mapType = MapType.construct(Map.class, null, null, null, String.class, Integer.class);
        Assert.assertNotNull(mapType);
        Assert.assertEquals(Map.class, mapType.getRawClass());
        
        JavaType keyType = mapType.getKeyType().withStaticTyping();
        mapType = mapType.withKeyType(keyType);
        Assert.assertEquals(keyType, mapType.getKeyType());
    }
    
    @Test
    public void testWithContentType() {
        MapType mapType = MapType.construct(Map.class, null, null, null, String.class, Integer.class);
        Assert.assertNotNull(mapType);
        Assert.assertEquals(Map.class, mapType.getRawClass());
        
        JavaType contentType = mapType.getValueType().withStaticTyping();
        mapType = mapType.withContentType(contentType);
        Assert.assertEquals(contentType, mapType.getValueType());
    }
    
    @Test
    public void testWithKeyTypeHandler() {
        MapType mapType = MapType.construct(Map.class, null, null, null, String.class, Integer.class);
        Assert.assertNotNull(mapType);
        Assert.assertEquals(Map.class, mapType.getRawClass());
        
        Object typeHandler = new TypeHandler();
        mapType = mapType.withKeyTypeHandler(typeHandler);
        Assert.assertEquals(typeHandler, mapType.getKeyType().getTypeHandler());
    }
    
    @Test
    public void testWithKeyValueHandler() {
        MapType mapType = MapType.construct(Map.class, null, null, null, String.class, Integer.class);
        Assert.assertNotNull(mapType);
        Assert.assertEquals(Map.class, mapType.getRawClass());
        
        Object valueHandler = new ValueHandler();
        mapType = mapType.withKeyValueHandler(valueHandler);
        Assert.assertEquals(valueHandler, mapType.getKeyType().getValueHandler());
    }
    
    @Test
    public void testWithStaticTyping() {
        MapType mapType = MapType.construct(Map.class, null, null, null, String.class, Integer.class);
        Assert.assertNotNull(mapType);
        Assert.assertEquals(Map.class, mapType.getRawClass());
        
        mapType = mapType.withStaticTyping();
        Assert.assertTrue(mapType.isStaticTyping());
    }
    
    @Test
    public void testRefine() {
        MapType mapType = MapType.construct(Map.class, null, null, null, String.class, Integer.class);
        Assert.assertNotNull(mapType);
        Assert.assertEquals(Map.class, mapType.getRawClass());
        
        MapType refinedType = (MapType) mapType.refine(HashMap.class, null, null, null);
        Assert.assertEquals(HashMap.class, refinedType.getRawClass());
    }
    

}