import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        MapType mapType = MapType.construct(HashMap.class, new TypeBindings(), null, null, new SimpleType(String.class), new SimpleType(Integer.class));
        assertNotNull(mapType);
        assertEquals(HashMap.class, mapType.getKeyType());
        assertEquals(SimpleType.class, mapType.getKeyType().getClass());
        assertEquals(String.class, mapType.getKeyType().getRawClass());
        assertEquals(SimpleType.class, mapType.getValueType().getClass());
        assertEquals(Integer.class, mapType.getValueType().getRawClass());
    }

    @Test
    public void testToString() {
        MapType mapType = MapType.construct(HashMap.class, new TypeBindings(), null, null, new SimpleType(String.class), new SimpleType(Integer.class));
        assertEquals("[map type; class java.util.HashMap, java.lang.String -> java.lang.Integer]", mapType.toString());
    }

    @Test
    public void testRefine() {
        MapType mapType = MapType.construct(HashMap.class, new TypeBindings(), null, null, new SimpleType(String.class), new SimpleType(Integer.class));
        JavaType refinedMapType = mapType.refine(Map.class, new TypeBindings(), null, null);
        assertNotNull(refinedMapType);
        assertEquals(Map.class, refinedMapType.getRawClass());
    }

    // Add more test cases for other methods...

}