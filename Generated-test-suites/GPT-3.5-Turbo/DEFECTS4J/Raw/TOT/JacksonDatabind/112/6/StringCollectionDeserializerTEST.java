
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class StringCollectionDeserializerTest {

    ObjectMapper objectMapper;
    StringCollectionDeserializer deserializer;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        deserializer = new StringCollectionDeserializer(null, null);
    }

    @Test
    public void testDeserialize_WithEmptyArray_ReturnsEmptyCollection() throws JsonProcessingException {
        String json = "[]";
        JsonNode node = objectMapper.readTree(json);

        Collection<String> result = deserializer.deserialize(node.traverse(objectMapper), null);

        assertEquals(0, result.size());        
    }

    @Test
    public void testDeserialize_WithArrayContainingSingleString_ReturnsCollectionWithSingleString() throws JsonProcessingException {
        String json = "[\"Hello\"]";
        JsonNode node = objectMapper.readTree(json);

        Collection<String> result = deserializer.deserialize(node.traverse(objectMapper), null);

        assertEquals(1, result.size());
        assertTrue(result.contains("Hello"));
    }

    @Test
    public void testDeserialize_WithArrayContainingMultipleStrings_ReturnsCollectionWithAllStrings() throws JsonProcessingException {
        String json = "[\"Hello\", \"World\"]";
        JsonNode node = objectMapper.readTree(json);

        Collection<String> result = deserializer.deserialize(node.traverse(objectMapper), null);

        assertEquals(2, result.size());
        assertTrue(result.contains("Hello"));
        assertTrue(result.contains("World"));
    }

    @Test
    public void testDeserialize_WithArrayContainingEmptyStrings_ReturnsCollectionWithEmptyStrings() throws JsonProcessingException {
        String json = "[\"\"]";
        JsonNode node = objectMapper.readTree(json);

        Collection<String> result = deserializer.deserialize(node.traverse(objectMapper), null);

        assertEquals(1, result.size());
        assertTrue(result.contains(""));
    }

    @Test
    public void testDeserialize_WithArrayContainingNullValue_ReturnsCollectionWithNullValue() throws JsonProcessingException {
        String json = "[null]";
        JsonNode node = objectMapper.readTree(json);

        Collection<String> result = deserializer.deserialize(node.traverse(objectMapper), null);

        assertEquals(1, result.size());
        assertNull(result.iterator().next());
    }

    @Test
    public void testDeserialize_WithMixedArray_ReturnsCollectionWithAllValues() throws JsonProcessingException {
        String json = "[\"Hello\", null, \"World\", \"\"]";
        JsonNode node = objectMapper.readTree(json);

        Collection<String> result = deserializer.deserialize(node.traverse(objectMapper), null);

        assertEquals(4, result.size());
        assertTrue(result.contains("Hello"));
        assertTrue(result.contains(null));
        assertTrue(result.contains("World"));
        assertTrue(result.contains(""));
    }

    @Test
    public void testDeserialize_WithNonArray_ReturnsEmptyCollection() throws JsonProcessingException {
        String json = "\"Hello\"";
        JsonNode node = objectMapper.readTree(json);

        Collection<String> result = deserializer.deserialize(node.traverse(objectMapper), null);

        assertEquals(0, result.size());
    }
}

