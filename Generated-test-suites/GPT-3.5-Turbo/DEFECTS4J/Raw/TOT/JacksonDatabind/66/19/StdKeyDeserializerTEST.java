import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer.StringKD;
import java.io.IOException;
import static org.junit.Assert.*;

public class StdKeyDeserializerTest {

    @Test
    public void testDeserializeKey_String() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        DeserializationContext ctxt = null;
        String key = "test_key";

        Object result = deserializer.deserializeKey(key, ctxt);

        assertEquals(key, result);
    }

    @Test
    public void testDeserializeKey_Enum_Valid() throws IOException {
        // Initialize the deserializer and context
        StdKeyDeserializer deserializer = new StdKeyDeserializer.EnumKD(AnnotatedMethod, EnumResolver);
        DeserializationContext ctxt = null;

        // Create a valid key
        String key = "VALUE1";

        // Call the deserializeKey method
        Object result = deserializer.deserializeKey(key, ctxt);

        // Assert that the result matches the expected Enum value
        assertEquals(Enum.VALUE1, result);
    }

    @Test
    public void testDeserializeKey_Enum_Invalid() throws IOException {
        // Initialize the deserializer and context
        StdKeyDeserializer deserializer = new StdKeyDeserializer.EnumKD(AnnotatedMethod, EnumResolver);
        DeserializationContext ctxt = null;

        // Create an invalid key
        String key = "INVALID_VALUE";

        // Call the deserializeKey method
        Object result = deserializer.deserializeKey(key, ctxt);

        // Assert that the result is null
        assertNull(result);
    }

    @Test
    public void testDeserializeKey_InvalidKey() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        DeserializationContext ctxt = null;
        String key = null;

        Object result = deserializer.deserializeKey(key, ctxt);

        assertNull(result);
    }

    @Test(expected = IOException.class)
    public void testDeserializeKey_ExceptionThrown() throws IOException {
        StdKeyDeserializer deserializer = StdKeyDeserializer.forType(String.class);
        DeserializationContext ctxt = null;
        String key = "test_key";

        deserializer.deserializeKey(key, ctxt);
    }

}