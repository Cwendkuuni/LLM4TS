import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    private StdKeyDeserializer stringKeyDeserializer;
    private StdKeyDeserializer integerKeyDeserializer;
    private StdKeyDeserializer booleanKeyDeserializer;

    @Before
    public void setUp() {
        stringKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_STRING, String.class);
        integerKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_INT, Integer.class);
        booleanKeyDeserializer = new StdKeyDeserializer(StdKeyDeserializer.TYPE_BOOLEAN, Boolean.class);
    }

    @Test
    public void testDeserializeStringKey_ValidInput_ReturnsKey() {
        String key = "test";
        String result = (String) stringKeyDeserializer.deserializeKey(key, null);

        assertEquals(key, result);
    }

    @Test
    public void testDeserializeStringKey_NullInput_ReturnsNull() {
        String key = null;
        String result = (String) stringKeyDeserializer.deserializeKey(key, null);

        assertNull(result);
    }

    @Test
    public void testDeserializeIntegerKey_ValidInput_ReturnsKey() {
        String key = "42";
        Integer result = (Integer) integerKeyDeserializer.deserializeKey(key, null);

        assertEquals(Integer.valueOf(42), result);
    }

    @Test
    public void testDeserializeIntegerKey_InvalidInput_ThrowsException() {
        String key = "not_an_integer";

        try {
            integerKeyDeserializer.deserializeKey(key, null);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("not a valid representation: For input string: \"not_an_integer\"", e.getMessage());
        }
    }

    @Test
    public void testDeserializeBooleanKey_ValidInput_ReturnsKey() {
        String key = "true";
        Boolean result = (Boolean) booleanKeyDeserializer.deserializeKey(key, null);

        assertTrue(result);
    }

    @Test
    public void testDeserializeBooleanKey_InvalidInput_ThrowsException() {
        String key = "not_a_boolean";

        try {
            booleanKeyDeserializer.deserializeKey(key, null);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("not a valid representation: For input string: \"not_a_boolean\"", e.getMessage());
        }
    }

}