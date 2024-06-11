import org.junit.Assert;
import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        MapType mapType = MapType.construct(HashMap.class, new TypeBindings(), null, null, String.class, Integer.class);
        Assert.assertEquals("HashMap", mapType.getClassName());
        Assert.assertEquals(String.class, mapType.getKeyType());
        Assert.assertEquals(Integer.class, mapType.getValueType());
    }
    
    @Test
    public void testWithKeyValueHandler() {
        Object keyHandler = new Object();
        Object valueHandler = new Object();
        
        MapType mapType = new MapType(HashMap.class, new TypeBindings(), null, null, String.class, Integer.class);
        MapType updatedMapType = mapType.withKeyValueHandler(keyHandler);
        
        Assert.assertEquals(keyHandler, updatedMapType.getKeyType().getValueHandler());
        Assert.assertEquals(valueHandler, updatedMapType.getValueType());
    }

    @Test
    public void testWithStaticTyping() {
        MapType mapType = new MapType(HashMap.class, new TypeBindings(), null, null, String.class, Integer.class);
        MapType updatedMapType = mapType.withStaticTyping();
        
        Assert.assertTrue(updatedMapType.isStatic());
    }
}