import java.io.IOException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.AsWrapperTypeDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Assert;
import org.junit.Test;

public class AsWrapperTypeDeserializerTest {

    private ObjectMapper objectMapper = new ObjectMapper();
    private JsonFactory jsonFactory = new JsonFactory();

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        AsWrapperTypeDeserializer deserializer = createDeserializer();
        JsonParser jp = createJsonParser("{\"type\":\"test\",\"value\":\"example\"}");
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        Assert.assertEquals("example", deserializer.deserializeTypedFromObject(jp, ctxt));
    }

    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        AsWrapperTypeDeserializer deserializer = createDeserializer();
        JsonParser jp = createJsonParser("[{\"type\":\"test\",\"value\":\"example\"}]");
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        Assert.assertEquals("example", deserializer.deserializeTypedFromArray(jp, ctxt));
    }

    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        AsWrapperTypeDeserializer deserializer = createDeserializer();
        JsonParser jp = createJsonParser("\"example\"");
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        Assert.assertEquals("example", deserializer.deserializeTypedFromScalar(jp, ctxt));
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        AsWrapperTypeDeserializer deserializer = createDeserializer();
        JsonParser jp = createJsonParser("\"example\"");
        DeserializationContext ctxt = objectMapper.getDeserializationContext();
        Assert.assertEquals("example", deserializer.deserializeTypedFromAny(jp, ctxt));
    }

    private AsWrapperTypeDeserializer createDeserializer() {
        JavaType bt = objectMapper.constructType(String.class);
        TypeIdResolver idRes = null; // TODO: Create a TypeIdResolver implementation if needed
        String typePropertyName = "type";
        boolean typeIdVisible = true;
        Class<?> defaultImpl = null; // TODO: Specify a default implementation class if needed
        return new AsWrapperTypeDeserializer(bt, idRes, typePropertyName, typeIdVisible, defaultImpl);
    }

    private JsonParser createJsonParser(String json) throws IOException {
        return jsonFactory.createParser(json);
    }

}