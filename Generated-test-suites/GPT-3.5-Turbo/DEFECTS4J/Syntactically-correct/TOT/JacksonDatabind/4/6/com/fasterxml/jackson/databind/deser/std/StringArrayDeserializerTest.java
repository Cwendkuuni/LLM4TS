import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

@RunWith(MockitoJUnitRunner.class)
public class StringArrayDeserializerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JsonFactory jsonFactory = new JsonFactory(objectMapper);
    private final DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);

    @Test
    public void testDeserialize_ValidStringArray_ReturnsStringArray() throws JsonProcessingException, IOException {
        String json = "[\"foo\", \"bar\"]";
        JsonParser jsonParser = jsonFactory.createParser(json);
        String[] expected = {"foo", "bar"};

        String[] result = StringArrayDeserializer.instance.deserialize(jsonParser, deserializationContext);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserialize_EmptyStringArray_ReturnsEmptyStringArray() throws JsonProcessingException, IOException {
        String json = "[]";
        JsonParser jsonParser = jsonFactory.createParser(json);
        String[] expected = {};

        String[] result = StringArrayDeserializer.instance.deserialize(jsonParser, deserializationContext);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserialize_NullStringArray_ReturnsNullStringArray() throws JsonProcessingException, IOException {
        String json = "null";
        JsonParser jsonParser = jsonFactory.createParser(json);
        String[] expected = null;

        String[] result = StringArrayDeserializer.instance.deserialize(jsonParser, deserializationContext);

        assertNull(result);
    }

    @Test(expected = JsonParseException.class)
    public void testDeserialize_InvalidJson_ThrowsJsonParseException() throws JsonProcessingException, IOException {
        String json = "[\"foo\", \"bar\"";
        JsonParser jsonParser = jsonFactory.createParser(json);

        StringArrayDeserializer.instance.deserialize(jsonParser, deserializationContext);
    }

    @Test(expected = MismatchedInputException.class)
    public void testDeserialize_InvalidType_ThrowsMismatchedInputException() throws JsonProcessingException, IOException {
        String json = "1";
        JsonParser jsonParser = jsonFactory.createParser(json);

        StringArrayDeserializer.instance.deserialize(jsonParser, deserializationContext);
    }

    @Test
    public void testDeserialize_CustomDeserializer_ReturnsStringArrayWithCustomDeserialization() throws JsonProcessingException, IOException {
        String json = "[\"foo\", \"bar\"]";
        JsonParser jsonParser = jsonFactory.createParser(json);
        JsonDeserializer<String> customDeserializer = Mockito.mock(JsonDeserializer.class);
        Mockito.when(customDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn("custom");
        StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer(customDeserializer);
        String[] expected = {"custom", "custom"};

        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserialize_CustomDeserializer_ReturnsNullStringArrayWithCustomDeserialization() throws JsonProcessingException, IOException {
        String json = "[null]";
        JsonParser jsonParser = jsonFactory.createParser(json);
        JsonDeserializer<String> customDeserializer = Mockito.mock(JsonDeserializer.class);
        Mockito.when(customDeserializer.deserialize(jsonParser, deserializationContext)).thenReturn(null);
        StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer(customDeserializer);
        String[] expected = {null};

        String[] result = stringArrayDeserializer.deserialize(jsonParser, deserializationContext);

        assertArrayEquals(expected, result);
    }

    @Test(expected = NullPointerException.class)
    public void testDeserialize_CustomDeserializerWithNullValue_ReturnsNullAndThrowsNullPointerException() throws JsonProcessingException, IOException {
        String json = "[null]";
        JsonParser jsonParser = jsonFactory.createParser(json);
        JsonDeserializer<String> customDeserializer = Mockito.mock(JsonDeserializer.class);
        Mockito.when(customDeserializer.deserialize(jsonParser, deserializationContext)).thenThrow(new NullPointerException());
        StringArrayDeserializer stringArrayDeserializer = new StringArrayDeserializer(customDeserializer);

        stringArrayDeserializer.deserialize(jsonParser, deserializationContext);
    }

    private void assertArrayEquals(String[] expected, String[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
}