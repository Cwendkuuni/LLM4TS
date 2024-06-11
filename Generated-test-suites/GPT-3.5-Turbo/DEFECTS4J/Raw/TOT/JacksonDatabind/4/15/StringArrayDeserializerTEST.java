import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class StringArrayDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDeserialize_WithValidJsonArray_ShouldReturnStringArray() throws IOException {
        // Arrange
        String json = "[\"value1\", \"value2\", \"value3\"]";
        JsonParser parser = objectMapper.getFactory().createParser(json);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        String[] expected = new String[]{"value1", "value2", "value3"};

        // Act
        String[] result = StringArrayDeserializer.instance.deserialize(parser, ctxt);

        // Assert
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserialize_WithNullValuesInJsonArray_ShouldReturnStringArrayWithNulls() throws IOException {
        // Arrange
        String json = "[\"value1\", null, \"value3\"]";
        JsonParser parser = objectMapper.getFactory().createParser(json);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        String[] expected = new String[]{"value1", null, "value3"};

        // Act
        String[] result = StringArrayDeserializer.instance.deserialize(parser, ctxt);

        // Assert
        assertArrayEquals(expected, result);
    }

    @Test
    public void testDeserialize_WithEmptyJsonArray_ShouldReturnEmptyStringArray() throws IOException {
        // Arrange
        String json = "[]";
        JsonParser parser = objectMapper.getFactory().createParser(json);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        String[] expected = new String[]{};

        // Act
        String[] result = StringArrayDeserializer.instance.deserialize(parser, ctxt);

        // Assert
        assertArrayEquals(expected, result);
    }

    @Test(expected = IOException.class)
    public void testDeserialize_WithNonArrayJson_ShouldThrowIOException() throws IOException {
        // Arrange
        String json = "\"value\"";
        JsonParser parser = objectMapper.getFactory().createParser(json);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        // Act
        StringArrayDeserializer.instance.deserialize(parser, ctxt);

        // Assert (Exception expected)
    }

    @Test(expected = IOException.class)
    public void testDeserialize_WithInvalidJsonArray_ShouldThrowIOException() throws IOException {
        // Arrange
        String json = "[\"value1\", \"value2\",";
        JsonParser parser = objectMapper.getFactory().createParser(json);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        // Act
        StringArrayDeserializer.instance.deserialize(parser, ctxt);

        // Assert (Exception expected)
    }

    @Test
    public void testDeserialize_WithCustomElementDeserializerAndValidJsonArray_ShouldReturnStringArray() throws IOException {
        // Arrange
        String json = "[\"value1\", \"value2\", \"value3\"]";
        JsonParser parser = objectMapper.getFactory().createParser(json);
        DeserializationContext ctxt = objectMapper.getDeserializationContext();

        JsonDeserializer<String> customDeserializer = new CustomStringDeserializer();
        StringArrayDeserializer deserializer = new StringArrayDeserializer(customDeserializer);

        String[] expected = new String[]{"value1", "value2", "value3"};

        // Act
        String[] result = deserializer.deserialize(parser, ctxt);

        // Assert
        assertArrayEquals(expected, result);
    }

    private static class CustomStringDeserializer extends JsonDeserializer<String> {
        @Override
        public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return p.getText().toUpperCase();
        }
    }
}