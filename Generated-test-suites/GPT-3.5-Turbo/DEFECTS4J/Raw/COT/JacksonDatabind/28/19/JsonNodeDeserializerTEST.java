
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.JsonNodeDeserializer;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonToken;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonNodeDeserializerTest {

    @Test
    public void testGetNullValue() {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        DeserializationContext ctxt = null;
        JsonNode result = deserializer.getNullValue(ctxt);
        assertEquals(NullNode.getInstance(), result);
    }
    
    @Test
    public void testDeserialize() throws Exception {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        DeserializationContext ctxt = null;
        JsonParser p = new ObjectMapper().getFactory().createParser("{}");
        JsonNode result = deserializer.deserialize(p, ctxt);
        assertTrue(result instanceof ObjectNode);
        
        p = new ObjectMapper().getFactory().createParser("[]");
        result = deserializer.deserialize(p, ctxt);
        assertTrue(result instanceof ArrayNode);
        
        p = new ObjectMapper().getFactory().createParser("null");
        result = deserializer.deserialize(p, ctxt);
        assertEquals(NullNode.getInstance(), result);
    }
    
    @Test
    public void testObjectDeserializerDeserialize() throws Exception {
        JsonNodeDeserializer.ObjectDeserializer deserializer = new JsonNodeDeserializer.ObjectDeserializer();
        DeserializationContext ctxt = null;
        JsonParser p = new ObjectMapper().getFactory().createParser("{}");
        JsonNode result = deserializer.deserialize(p, ctxt);
        assertTrue(result instanceof ObjectNode);
        
        p = new ObjectMapper().getFactory().createParser("null");
        result = deserializer.deserialize(p, ctxt);
        assertEquals(NullNode.getInstance(), result);
    }
    
    @Test
    public void testArrayDeserializerDeserialize() throws Exception {
        JsonNodeDeserializer.ArrayDeserializer deserializer = new JsonNodeDeserializer.ArrayDeserializer();
        DeserializationContext ctxt = null;
        JsonParser p = new ObjectMapper().getFactory().createParser("[]");
        JsonNode result = deserializer.deserialize(p, ctxt);
        assertTrue(result instanceof ArrayNode);
        
        p = new ObjectMapper().getFactory().createParser("null");
        result = deserializer.deserialize(p, ctxt);
        assertEquals(NullNode.getInstance(), result);
    }
    
    @Test(expected=org.apache.commons.lang.NotImplementedException.class)
    public void testDeserializeAny() throws Exception {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        DeserializationContext ctxt = null;
        JsonParser p = new ObjectMapper().getFactory().createParser("{}");
        JsonNodeFactory nodeFactory = new JsonNodeFactory(true);
        deserializer.deserializeAny(p, ctxt, nodeFactory);
    }
    
    @Test(expected=org.apache.commons.lang.NotImplementedException.class)
    public void testFromInt() throws Exception {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        DeserializationContext ctxt = null;
        JsonParser p = new ObjectMapper().getFactory().createParser("1");
        JsonNodeFactory nodeFactory = new JsonNodeFactory(true);
        deserializer._fromInt(p, ctxt, nodeFactory);
    }
    
    @Test(expected=org.apache.commons.lang.NotImplementedException.class)
    public void testFromFloat() throws Exception {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        DeserializationContext ctxt = null;
        JsonParser p = new ObjectMapper().getFactory().createParser("1.5");
        JsonNodeFactory nodeFactory = new JsonNodeFactory(true);
        deserializer._fromFloat(p, ctxt, nodeFactory);
    }
    
    @Test(expected=org.apache.commons.lang.NotImplementedException.class)
    public void testFromEmbedded() throws Exception {
        JsonNodeDeserializer deserializer = new JsonNodeDeserializer();
        DeserializationContext ctxt = null;
        JsonParser p = new ObjectMapper().getFactory().createParser("\"test\"");
        JsonNodeFactory nodeFactory = new JsonNodeFactory(true);
        deserializer._fromEmbedded(p, ctxt, nodeFactory);
    }
}
