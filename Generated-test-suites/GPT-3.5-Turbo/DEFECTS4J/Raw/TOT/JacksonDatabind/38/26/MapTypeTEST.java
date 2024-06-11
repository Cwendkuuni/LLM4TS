import com.fasterxml.jackson.databind.JavaType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MapTypeTest {

    private MapType mapType;
    private JavaType keyType;
    private JavaType valueType;

    @Before
    public void setup() {
        keyType = new SomeKeyType();
        valueType = new SomeValueType();
        mapType = MapType.construct(SomeMap.class, keyType, valueType);
    }

    @Test
    public void testConstruct() {
        Assert.assertEquals(SomeMap.class, mapType.getRawClass());
        Assert.assertEquals(null, mapType.getTypeHandler());
        Assert.assertEquals(false, mapType.isStaticTyping());
        Assert.assertEquals(keyType, mapType.getKeyType());
        Assert.assertEquals(valueType, mapType.getValueType());
    }

    @Test
    public void testWithStaticTyping() {
        MapType staticMapType = mapType.withStaticTyping();
        Assert.assertTrue(staticMapType.isStaticTyping());
        Assert.assertEquals(keyType.withStaticTyping(), staticMapType.getKeyType());
        Assert.assertEquals(valueType.withStaticTyping(), staticMapType.getValueType());
    }

    @Test
    public void testWithKeyType() {
        JavaType newKeyType = new SomeNewKeyType();
        MapType newMapType = mapType.withKeyType(newKeyType);
        Assert.assertEquals(newKeyType, newMapType.getKeyType());
        Assert.assertEquals(valueType, newMapType.getValueType());
    }

    @Test
    public void testWithContentType() {
        JavaType newContentType = new SomeNewContentType();
        MapType newMapType = mapType.withContentType(newContentType);
        Assert.assertEquals(keyType, newMapType.getKeyType());
        Assert.assertEquals(newContentType, newMapType.getValueType());
    }

    @Test
    public void testWithKeyTypeHandler() {
        Object keyHandler = new SomeKeyHandler();
        MapType newMapType = mapType.withKeyTypeHandler(keyHandler);
        Assert.assertEquals(keyType.withTypeHandler(keyHandler), newMapType.getKeyType());
        Assert.assertEquals(valueType, newMapType.getValueType());
    }

    @Test
    public void testWithKeyValueHandler() {
        Object keyHandler = new SomeKeyHandler();
        MapType newMapType = mapType.withKeyValueHandler(keyHandler);
        Assert.assertEquals(keyType.withValueHandler(keyHandler), newMapType.getKeyType());
        Assert.assertEquals(valueType, newMapType.getValueType());
    }

    // Add your own test cases for other methods
    
}