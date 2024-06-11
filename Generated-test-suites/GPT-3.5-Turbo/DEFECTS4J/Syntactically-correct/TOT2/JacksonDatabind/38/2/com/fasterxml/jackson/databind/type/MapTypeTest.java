import static org.junit.Assert.*;
import org.junit.Test;

public class MapTypeTest {

    @Test
    public void testConstruct() {
        MapType mapType = MapType.construct(String.class, null, null, null, new StringType(), new IntegerType());
        assertNotNull(mapType);
        assertEquals(String.class, mapType.getKeyType());
        assertEquals(Integer.class, mapType.getValueType());
    }

    @Test
    public void testWithStaticTyping() {
        MapType mapType = MapType.construct(String.class, null, null, null, new StringType(), new IntegerType()).withStaticTyping();
        assertTrue(mapType.isStaticTyping());
    }

    @Test
    public void testToString() {
        MapType mapType = MapType.construct(String.class, null, null, null, new StringType(), new IntegerType());
        assertEquals("[map type; class java.lang.String, class com.fasterxml.jackson.databind.type.StringType -> class java.lang.Integer]", mapType.toString());
    }

    // Additional test cases for edge cases and exceptions can be added here

}