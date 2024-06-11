import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser("{}"); // empty JSON
        NullifyingDeserializer deserializer = new NullifyingDeserializer();

        assertNull(deserializer.deserialize(parser, null));
    }

    // Edge Case: testing with a valid JSON object
    @Test
    public void testDeserializeWithValidJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser("{\"name\":\"John\"}");
        NullifyingDeserializer deserializer = new NullifyingDeserializer();

        assertNull(deserializer.deserialize(parser, null));
    }

    // Exception Scenario: testing with a JsonProcessingException
    @Test(expected = IOException.class)
    public void testDeserializeWithJsonProcessingException() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFactory jsonFactory = mapper.getFactory();
        JsonParser parser = jsonFactory.getParser("Invalid JSON");
        NullifyingDeserializer deserializer = new NullifyingDeserializer();

        deserializer.deserialize(parser, null);
    }
}