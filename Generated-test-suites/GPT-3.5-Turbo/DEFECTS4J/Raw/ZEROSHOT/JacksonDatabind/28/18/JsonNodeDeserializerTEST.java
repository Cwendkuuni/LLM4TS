import static org.junit.Assert.*;
import java.io.IOException;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.RawValue;


public class JsonNodeDeserializerTest {

    @Test
    public void testGetNullValue() {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        DeserializationContext ctxt = null;
        JsonNode result = deserializer.getNullValue(ctxt);
        assertEquals(result, NullNode.getInstance());
    }

    @Test
    public void testDeserializeObject() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.deserializeObject(p, ctxt, nodeFactory);
        assertEquals(result, null);
    }

    @Test
    public void testDeserializeArray() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.deserializeArray(p, ctxt, nodeFactory);
        assertEquals(result, null);
    }

    @Test
    public void testDeserializeAny() throws IOException {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonNode result = deserializer.deserializeAny(p, ctxt, nodeFactory);
        assertEquals(result, null);
    }

    @Test
    public void testGetDeserializer() {
        Class<?> nodeClass = ObjectNode.class;
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        JsonDeserializer<? extends JsonNode> result = JsonNodeDeserializer.getDeserializer(nodeClass);
        assertEquals(result, ObjectDeserializer.getInstance());
        
        nodeClass = ArrayNode.class;
        result = JsonNodeDeserializer.getDeserializer(nodeClass);
        assertEquals(result, ArrayDeserializer.getInstance());
        
        nodeClass = JsonNode.class;
        result = JsonNodeDeserializer.getDeserializer(nodeClass);
        assertEquals(result, JsonNodeDeserializer.instance);
    }
}