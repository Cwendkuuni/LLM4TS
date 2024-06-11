import static org.junit.Assert.*;
import org.junit.Test;

public class FromStringDeserializerTest {

    @Test
    public void testDeserialize() {
        FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class) {
            @Override
            protected String _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return value;
            }
        };

        // Test basic functionality
        JsonParser jp = new JsonParser(new ByteArrayInputStream("\"test\"".getBytes()));
        DeserializationContext ctxt = new DeserializationContext(null, null);
        try {
            String result = deserializer.deserialize(jp, ctxt);
            assertEquals("test", result);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testDeserialize_EmptyString() {
        FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class) {
            @Override
            protected String _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return value;
            }

            @Override
            protected String _deserializeFromEmptyString() throws IOException {
                return "default";
            }
        };

        // Test empty string
        JsonParser jp = new JsonParser(new ByteArrayInputStream("\"\"".getBytes()));
        DeserializationContext ctxt = new DeserializationContext(null, null);
        try {
            String result = deserializer.deserialize(jp, ctxt);
            assertEquals("default", result);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testDeserialize_InvalidFormat() {
        FromStringDeserializer<Integer> deserializer = new FromStringDeserializer<Integer>(Integer.class) {
            @Override
            protected Integer _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return Integer.parseInt(value);
            }
        };

        // Test invalid format
        JsonParser jp = new JsonParser(new ByteArrayInputStream("\"test\"".getBytes()));
        DeserializationContext ctxt = new DeserializationContext(null, null);
        try {
            deserializer.deserialize(jp, ctxt);
            fail("No exception thrown for invalid format");
        } catch (JsonMappingException e) {
            assertEquals("Can not construct instance of java.lang.Integer from String value 'test': not a valid textual representation, problem: For input string: \"test\"",
                    e.getMessage());
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testDeserialize_Null() {
        FromStringDeserializer<String> deserializer = new FromStringDeserializer<String>(String.class) {
            @Override
            protected String _deserialize(String value, DeserializationContext ctxt) throws IOException {
                return value;
            }
        };

        // Test null
        JsonParser jp = new JsonParser(new ByteArrayInputStream("null".getBytes()));
        DeserializationContext ctxt = new DeserializationContext(null, null);
        try {
            String result = deserializer.deserialize(jp, ctxt);
            assertNull(result);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}