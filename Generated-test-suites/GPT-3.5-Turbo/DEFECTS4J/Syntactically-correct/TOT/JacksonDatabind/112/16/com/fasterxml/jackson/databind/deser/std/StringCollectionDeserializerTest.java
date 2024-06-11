import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.Assert.assertEquals;

public class StringCollectionDeserializerTest {
    private static ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testDeserialize_NullParser_ReturnsEmptyCollection() throws IOException {
        // Arrange
        String jsonData = null;
        JsonParser jsonParser = new JsonFactory().createParser(jsonData);
        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer();

        // Act
        Collection<String> result = stringCollectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    public void testDeserialize_EmptyArray_ReturnsEmptyCollection() throws IOException {
        // Arrange
        String jsonData = "[]";
        JsonParser jsonParser = new JsonFactory().createParser(jsonData);
        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer();

        // Act
        Collection<String> result = stringCollectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());

        // Assert
        assertEquals(0, result.size());
    }

    @Test
    public void testDeserialize_SingleElementArray_ReturnsCollectionWithSingleElement() throws IOException {
        // Arrange
        String jsonData = "[\"value1\"]";
        JsonParser jsonParser = new JsonFactory().createParser(jsonData);
        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer();

        // Act
        Collection<String> result = stringCollectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());

        // Assert
        assertEquals(1, result.size());
        assertEquals("value1", result.iterator().next());
    }

    @Test
    public void testDeserialize_MultipleElementsArray_ReturnsCollectionWithAllElements() throws IOException {
        // Arrange
        String jsonData = "[\"value1\", \"value2\", \"value3\"]";
        JsonParser jsonParser = new JsonFactory().createParser(jsonData);
        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer();

        // Act
        Collection<String> result = stringCollectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());

        // Assert
        assertEquals(3, result.size());
        List<String> expected = Arrays.asList("value1", "value2", "value3");
        assertEquals(expected, new ArrayList<>(result));
    }

    @Test
    public void testDeserialize_InvalidJson_ThrowsJsonMappingException() throws IOException {
        // Arrange
        String jsonData = "[\"value1\", \"value2\", ";
        JsonParser jsonParser = new JsonFactory().createParser(jsonData);
        StringCollectionDeserializer stringCollectionDeserializer = new StringCollectionDeserializer();

        // Act & Assert
        org.junit.jupiter.api.Assertions.assertThrows(JsonMappingException.class, () -> {
            Collection<String> result = stringCollectionDeserializer.deserialize(jsonParser, objectMapper.getDeserializationContext());
        });
    }
}