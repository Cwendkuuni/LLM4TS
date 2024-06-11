import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        MapType mapType = MapType.construct(null, null, null, null, null, null);
        assertEquals(null, mapType);
    }

    @Test
    public void testWithStaticTyping() {
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType result = mapType.withStaticTyping();
        assertEquals(mapType, result);
    }

    @Test
    public void testWithContentTypeHandler() {
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType result = mapType.withContentTypeHandler(null);
        assertEquals(mapType, result);
    }

    @Test
    public void testWithContentValueHandler() {
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType result = mapType.withContentValueHandler(null);
        assertEquals(mapType, result);
    }

    @Test
    public void testWithKeyType() {
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType result = mapType.withKeyType(null);
        assertEquals(mapType, result);
    }

    @Test
    public void testRefine() {
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        JavaType result = mapType.refine(null, null, null, null);
        assertNotEquals(mapType, result);
    }

    @Test
    public void testWithKeyTypeHandler() {
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType result = mapType.withKeyTypeHandler(null);
        assertEquals(mapType, result);
    }

    @Test
    public void testWithKeyValueHandler() {
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        MapType result = mapType.withKeyValueHandler(null);
        assertEquals(mapType, result);
    }

    @Test
    public void testToString() {
        MapType mapType = new MapType(null, null, null, null, null, null, null, null, false);
        String result = mapType.toString();
        assertEquals("[map type; class null, null -> null]", result);
    }
}