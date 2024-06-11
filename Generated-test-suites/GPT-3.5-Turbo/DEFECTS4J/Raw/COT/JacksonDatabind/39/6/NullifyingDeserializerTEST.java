import org.junit.Test;
import org.junit.Assert;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserialize() throws IOException {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser parser = new JsonFactory().createParser("null");
        DeserializationContext context = new ObjectMapper().getDeserializationContext();
        Object result = deserializer.deserialize(parser, context);
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeWithType() throws IOException {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser parser = new JsonFactory().createParser("null");
        DeserializationContext context = new ObjectMapper().getDeserializationContext();
        TypeDeserializer typeDeserializer = new SomeTypeDeserializer();
        Object result = deserializer.deserializeWithType(parser, context, typeDeserializer);
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeWithTypeArray() throws IOException {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser parser = new JsonFactory().createParser("[1, 2, 3]");
        DeserializationContext context = new ObjectMapper().getDeserializationContext();
        TypeDeserializer typeDeserializer = new SomeTypeDeserializer();
        Object result = deserializer.deserializeWithType(parser, context, typeDeserializer);
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeWithTypeObject() throws IOException {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser parser = new JsonFactory().createParser("{\"field\": \"value\"}");
        DeserializationContext context = new ObjectMapper().getDeserializationContext();
        TypeDeserializer typeDeserializer = new SomeTypeDeserializer();
        Object result = deserializer.deserializeWithType(parser, context, typeDeserializer);
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeWithTypeFieldName() throws IOException {
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        JsonParser parser = new JsonFactory().createParser("{\"field\": \"value\"}");
        DeserializationContext context = new ObjectMapper().getDeserializationContext();
        TypeDeserializer typeDeserializer = new SomeTypeDeserializer();
        Object result = deserializer.deserializeWithType(parser, context, typeDeserializer);
        Assert.assertNull(result);
    }

}