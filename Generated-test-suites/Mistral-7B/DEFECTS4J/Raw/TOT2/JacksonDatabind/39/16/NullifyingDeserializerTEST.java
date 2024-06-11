import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{}");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserialize(parser, new TestContext());
        assertNull(result);
    }

    // Edge case: Test with empty JsonParser
    @Test
    public void testDeserializeEmptyJsonParser() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserialize(parser, new TestContext());
        assertNull(result);
    }

    // Edge case: Test with JsonParser containing an array
    @Test
    public void testDeserializeArray() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[1, 2, 3]");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserialize(parser, new TestContext());
        assertNull(result);
    }

    // Edge case: Test with JsonParser containing an object
    @Test
    public void testDeserializeObject() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{\"name\": \"John\", \"age\": 30}");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserialize(parser, new TestContext());
        assertNull(result);
    }

    // Exception handling: Test with JsonProcessingException
    @Test(expected = JsonProcessingException.class)
    public void testDeserializeJsonProcessingException() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{someInvalidJson}");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        deserializer.deserialize(parser, new TestContext());
    }
}