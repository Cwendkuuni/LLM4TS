
import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringArrayDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class StringArrayDeserializerTest {

    @Test
    public void testDeserialize_WithValidInput_ExpectArray() throws Exception {
        // Arrange
        String json = "[\"test1\", \"test2\", \"test3\"]";
        JsonNode rootNode = new ObjectMapper().readTree(json);
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JsonParser jp = newJsonParser(rootNode);

        // Act
        String[] result = deserializer.deserialize(jp, ctxt);

        // Assert
        assertEquals(3, result.length);
        assertEquals("test1", result[0]);
        assertEquals("test2", result[1]);
        assertEquals("test3", result[2]);
    }

    @Test
    public void testDeserialize_WithNullInput_ExpectEmptyArray() throws Exception {
        // Arrange
        String json = "null";
        JsonNode rootNode = new ObjectMapper().readTree(json);
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JsonParser jp = newJsonParser(rootNode);

        // Act
        String[] result = deserializer.deserialize(jp, ctxt);

        // Assert
        assertEquals(0, result.length);
    }

    @Test(expected = JsonParseException.class)
    public void testDeserialize_WithInvalidInput_ExpectException() throws Exception {
        // Arrange
        String json = "{ \"key\": \"value\" }";
        JsonNode rootNode = new ObjectMapper().readTree(json);
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JsonParser jp = newJsonParser(rootNode);

        // Act & Assert
        deserializer.deserialize(jp, ctxt);
    }

    @Test
    public void testDeserialize_WithNonArrayInput_ExpectException() throws Exception {
        // Arrange
        String json = "\"test\"";
        JsonNode rootNode = new ObjectMapper().readTree(json);
        StringArrayDeserializer deserializer = StringArrayDeserializer.instance;
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        JsonParser jp = newJsonParser(rootNode);

        // Act & Assert
        try {
            deserializer.deserialize(jp, ctxt);
            fail("Expected JsonParseException");
        } catch (JsonParseException e) {
            // Success
        }
    }

    // Helper method to create a new JsonParser from JsonNode
    private JsonParser newJsonParser(JsonNode node) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.treeAsTokens(node);
    }
}
