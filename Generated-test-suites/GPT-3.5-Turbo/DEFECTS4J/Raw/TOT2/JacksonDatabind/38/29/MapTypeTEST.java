import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        MapType mapType = MapType.construct(HashMap.class, new TypeBindings(), null, null, new SimpleType(String.class), new SimpleType(Integer.class));
        assertNotNull(mapType);
        assertEquals(HashMap.class, mapType.getClass());
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
    }

    @Test
    public void testWithContentTypeHandler() {
        MapType mapType = new MapType(HashMap.class, new TypeBindings(), null, null, new SimpleType(String.class), new SimpleType(Integer.class));
        Object handler = new Object();
        MapType newMapType = mapType.withContentTypeHandler(handler);
        assertNotNull(newMapType);
        assertEquals(handler, newMapType.contentTypeHandler);
    }

    @Test
    public void testToString() {
        MapType mapType = new MapType(HashMap.class, new TypeBindings(), null, null, new SimpleType(String.class), new SimpleType(Integer.class));
        assertEquals("[map type; class java.util.HashMap, class java.lang.String -> class java.lang.Integer]", mapType.toString());
    }
}