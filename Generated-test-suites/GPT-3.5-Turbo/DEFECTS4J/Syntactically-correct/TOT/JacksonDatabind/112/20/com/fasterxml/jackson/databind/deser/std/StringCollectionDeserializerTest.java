
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class StringCollectionDeserializerTest {

    private ObjectMapper objectMapper;
    private JsonFactory jsonFactory;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        jsonFactory = new JsonFactory();
    }

    @Test
    public void deserialize_SuccessfulDeserialization_CollectionOfString() throws IOException {
        // Arrange
        String json = "[\"value1\", \"value2\", \"value3\"]";
        JsonParser parser = jsonFactory.createParser(json);

        // Act
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        Collection<String> result = deserializer.deserialize(parser, objectMapper.getDeserializationContext());

        // Assert
        Collection<String> expected = Arrays.asList("value1", "value2", "value3");
        assertEquals(expected, result);
    }

    @Test(expected = JsonParseException.class)
    public void deserialize_InvalidJsonFormat_ThrowsJsonParseException() throws IOException {
        // Arrange
        String json = "\"value1\"";
        JsonParser parser = jsonFactory.createParser(json);

        // Act
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        deserializer.deserialize(parser, objectMapper.getDeserializationContext());
    }

    @Test(expected = InvalidFormatException.class)
    public void deserialize_InvalidValueFormat_ThrowsInvalidFormatException() throws IOException {
        // Arrange
        String json = "[123, \"value2\", \"value3\"]";
        JsonParser parser = jsonFactory.createParser(json);

        // Act
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        deserializer.deserialize(parser, objectMapper.getDeserializationContext());
    }

    @Test
    public void deserialize_NullValueWithSkipNullValuesOption_NullValueSkipped() throws IOException {
        // Arrange
        String json = "[null, \"value2\", null, \"value3\"]";
        JsonParser parser = jsonFactory.createParser(json);

        // Act
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        deserializer.setSkipNullValues(true);
        Collection<String> result = deserializer.deserialize(parser, objectMapper.getDeserializationContext());

        // Assert
        Collection<String> expected = Arrays.asList("value2", "value3");
        assertEquals(expected, result);
    }

    @Test
    public void deserialize_NullValueWithoutSkipNullValuesOption_NullValueIncluded() throws IOException {
        // Arrange
        String json = "[null, \"value2\", null, \"value3\"]";
        JsonParser parser = jsonFactory.createParser(json);

        // Act
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        deserializer.setSkipNullValues(false);
        Collection<String> result = deserializer.deserialize(parser, objectMapper.getDeserializationContext());

        // Assert
        Collection<String> expected = Arrays.asList(null, "value2", null, "value3");
        assertEquals(expected, result);
    }

    @Test
    public void deserialize_EmptyArray_ReturnsEmptyCollection() throws IOException {
        // Arrange
        String json = "[]";
        JsonParser parser = jsonFactory.createParser(json);

        // Act
        StringCollectionDeserializer deserializer = new StringCollectionDeserializer(null, null, null);
        Collection<String> result = deserializer.deserialize(parser, objectMapper.getDeserializationContext());

        // Assert
        Collection<String> expected = new ArrayList<>();
        assertEquals(expected, result);
    }
}

