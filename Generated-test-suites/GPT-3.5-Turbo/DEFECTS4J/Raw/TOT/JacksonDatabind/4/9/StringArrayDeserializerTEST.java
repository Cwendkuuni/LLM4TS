import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

    private StringArrayDeserializer stringArrayDeserializer;

    @Before
    public void setUp() {
        stringArrayDeserializer = StringArrayDeserializer.instance;
    }

    @Test
    public void testDeserialize() throws IOException {
        String json = "[\"value1\", \"value2\", \"value3\"]";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        assertEquals(3, result.length);
        assertEquals("value1", result[0]);
        assertEquals("value2", result[1]);
        assertEquals("value3", result[2]);
    }

    @Test
    public void testDeserializeWithNullValues() throws IOException {
        String json = "[\"value1\", null, \"value3\"]";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        assertEquals(3, result.length);
        assertEquals("value1", result[0]);
        assertNull(result[1]);
        assertEquals("value3", result[2]);
    }

    @Test
    public void testDeserializeWithEmptyArray() throws IOException {
        String json = "[]";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        assertEquals(0, result.length);
    }

    @Test
    public void testDeserializeWithNonArray() throws IOException {
        String json = "\"value\"";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
        assertThrows(JsonMappingException.class, () -> stringArrayDeserializer.deserialize(jsonParser, deserializationContext));
    }

    @Test
    public void testDeserializeWithEmptyStringAsNull() throws IOException {
        String json = "[\"\"]";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);
        DeserializationContext deserializationContext = objectMapper.getDeserializationContext();
        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
        assertNull(result);
    }

}