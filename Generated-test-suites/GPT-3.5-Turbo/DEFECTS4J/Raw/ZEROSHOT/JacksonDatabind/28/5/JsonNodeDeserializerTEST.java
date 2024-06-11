import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.RawValue;
import org.junit.Assert;
import org.junit.Test;

public class JsonNodeDeserializerTest {

    @Test
    public void testGetNullValue() {
        DeserializationContext ctxt = new DeserializationContext(null, null);
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonNode result = jsonNodeDeserializer.getNullValue(ctxt);
        Assert.assertEquals(NullNode.getInstance(), result);
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonParser p = new JsonParser(null, null);
        DeserializationContext ctxt = new DeserializationContext(null, null);
        JsonNodeDeserializer jsonNodeDeserializer = new JsonNodeDeserializer();
        JsonNode result = jsonNodeDeserializer.deserialize(p, ctxt);
        Assert.assertEquals(null, result);
    }

    @Test
    public void testObjectDeserializerDeserialize() throws IOException {
        JsonParser p = new JsonParser(null, null);
        DeserializationContext ctxt = new DeserializationContext(null, null);
        JsonNodeDeserializer.ObjectDeserializer objectDeserializer = new JsonNodeDeserializer.ObjectDeserializer();
        JsonNode result = objectDeserializer.deserialize(p, ctxt);
        Assert.assertEquals(null, result);
    }

    @Test
    public void testArrayDeserializerDeserialize() throws IOException {
        JsonParser p = new JsonParser(null, null);
        DeserializationContext ctxt = new DeserializationContext(null, null);
        JsonNodeDeserializer.ArrayDeserializer arrayDeserializer = new JsonNodeDeserializer.ArrayDeserializer();
        JsonNode result = arrayDeserializer.deserialize(p, ctxt);
        Assert.assertEquals(null, result);
    }
}