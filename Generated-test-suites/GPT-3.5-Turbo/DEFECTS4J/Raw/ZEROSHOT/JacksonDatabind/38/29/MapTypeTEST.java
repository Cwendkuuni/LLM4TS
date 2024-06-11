import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

    // Initialize MapType instance for testing
    private MapType mapType;

    @Before
    public void setUp() {
        mapType = new MapType(Map.class, null, null,
                null, new JavaType(String.class), new JavaType(Integer.class),
                null, null, false);
    }

    @After
    public void tearDown() {
        mapType = null;
    }

    @Test
    public void testConstruct() {
        MapType testMapType = MapType.construct(Map.class, null, null,
                null, new JavaType(String.class), new JavaType(Integer.class));
        assertNotNull(testMapType);
    }

    @Test
    public void testWithKeyType() {
        JavaType keyType = new JavaType(String.class);
        MapType testMapType = mapType.withKeyType(keyType);
        assertEquals(testMapType.getKeyType(), keyType);
    }

    @Test
    public void testWithContentType() {
        JavaType contentType = new JavaType(Integer.class);
        MapType testMapType = mapType.withContentType(contentType);
        assertEquals(testMapType.getContentType(), contentType);
    }

    @Test
    public void testWithKeyTypeHandler() {
        Object keyTypeHandler = new Object();
        MapType testMapType = mapType.withKeyTypeHandler(keyTypeHandler);
        assertEquals(testMapType.getKeyType().getHandler(), keyTypeHandler);
    }

    @Test
    public void testWithKeyValueHandler() {
        Object keyValueHandler = new Object();
        MapType testMapType = mapType.withKeyValueHandler(keyValueHandler);
        assertEquals(testMapType.getValueType().getHandler(), keyValueHandler);
    }

    @Test
    public void testWithStaticTyping() {
        MapType testMapType = mapType.withStaticTyping();
        assertTrue(testMapType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        MapType testMapType = mapType.refine(HashMap.class, null, null, null);
        assertEquals(testMapType.getRawClass(), HashMap.class);
    }

    @Test
    public void testToString() {
        String toString = "[map type; class java.util.Map, java.lang.String -> java.lang.Integer]";
        assertEquals(mapType.toString(), toString);
    }
}