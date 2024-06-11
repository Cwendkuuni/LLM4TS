import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize_ReturnsNull() throws IOException {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonNode node = new ObjectMapper().readTree("");
        Object result = deserializer.deserialize(node.traverse(), null);
        assertNull(result);
    }
    
    @Test
    public void testDeserializeWithType_DeserializesArray_ReturnsNull() throws IOException {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonNode node = new ObjectMapper().readTree("[]");
        Object result = deserializer.deserializeWithType(node.traverse(), null, null);
        assertNull(result);
    }
    
    @Test
    public void testDeserializeWithType_DeserializesObject_ReturnsNull() throws IOException {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonNode node = new ObjectMapper().readTree("{}");
        Object result = deserializer.deserializeWithType(node.traverse(), null, null);
        assertNull(result);
    }
    
    @Test
    public void testDeserializeWithType_DeserializesFieldName_ReturnsNull() throws IOException {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonNode node = new ObjectMapper().readTree("\"fieldName\"");
        Object result = deserializer.deserializeWithType(node.traverse(), null, null);
        assertNull(result);
    }
    
    @Test
    public void testDeserializeWithType_DeserializesNull_ReturnsNull() throws IOException {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonNode node = NullNode.getInstance();
        Object result = deserializer.deserializeWithType(node.traverse(), null, null);
        assertNull(result);
    }
}