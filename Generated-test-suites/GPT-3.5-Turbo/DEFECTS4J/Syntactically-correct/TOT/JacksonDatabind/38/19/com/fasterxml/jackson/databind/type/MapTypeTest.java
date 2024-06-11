import org.junit.Test;
import static org.junit.Assert.*;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        // Typical use case
        MapType mapType = MapType.construct(Map.class, new TypeBindings(), null, null, String.class, Integer.class);
        assertNotNull(mapType);

        // Edge case: empty TypeBindings
        mapType = MapType.construct(Map.class, new TypeBindings(), null, null, null, null);
        assertNotNull(mapType);

        // Error scenario: null rawType
        try {
            mapType = MapType.construct(null, new TypeBindings(), null, null, String.class, Integer.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }

        // Error scenario: null keyType
        try {
            mapType = MapType.construct(Map.class, new TypeBindings(), null, null, null, Integer.class);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }

        // Error scenario: null valueType
        try {
            mapType = MapType.construct(Map.class, new TypeBindings(), null, null, String.class, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }

    @Test
    public void testWithKeyType() {
        MapType mapType = MapType.construct(Map.class, new TypeBindings(), null, null, String.class, Integer.class);

        // Typical use case: change keyType
        mapType = mapType.withKeyType(Long.class);
        assertEquals(Long.class, mapType.getKeyType());

        // Edge case: same keyType
        MapType sameKeyType = mapType.withKeyType(String.class);
        assertSame(mapType, sameKeyType);

        // Error scenario: null keyType
        try {
            mapType = mapType.withKeyType(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception expected
        }
    }

    // similarly, other methods can be tested....

}