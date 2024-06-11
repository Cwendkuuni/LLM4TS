import static org.junit.Assert.*;
import org.junit.Test;

public class StringArrayDeserializerTest {

    // Test deserialize method
    @Test
    public void testDeserialize() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        // Test empty array
        String[] expectedEmpty = new String[0];
        String[] resultEmpty = deserializer.deserialize("[]");

        assertArrayEquals(expectedEmpty, resultEmpty);

        // Test array with one element
        String[] expectedSingle = new String[] { "value" };
        String[] resultSingle = deserializer.deserialize("[\"value\"]");

        assertArrayEquals(expectedSingle, resultSingle);

        // Test array with multiple elements
        String[] expectedMultiple = new String[] { "value1", "value2", "value3" };
        String[] resultMultiple = deserializer.deserialize("[\"value1\",\"value2\",\"value3\"]");

        assertArrayEquals(expectedMultiple, resultMultiple);

        // Test array with null value
        String[] expectedNull = new String[] { null };
        String[] resultNull = deserializer.deserialize("[null]");

        assertArrayEquals(expectedNull, resultNull);
    }

    // Test deserializeWithType method
    @Test
    public void testDeserializeWithType() throws Exception {
        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        // Test array with type information
        Object resultWithType = deserializer.deserializeWithType("[\"value\"]");

        assertNotNull(resultWithType);
        assertTrue(resultWithType instanceof String[]);
    }
}