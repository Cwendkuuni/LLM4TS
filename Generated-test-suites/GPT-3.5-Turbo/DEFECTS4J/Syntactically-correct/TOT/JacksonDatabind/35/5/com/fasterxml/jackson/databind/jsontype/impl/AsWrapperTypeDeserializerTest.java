import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class AsWrapperTypeDeserializerTest {

    @Test
    public void testDeserializeTypedFromObject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"type\":\"custom\",\"value\":42}";

        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext context = mapper.getDeserializationContext();
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                TypeFactory.defaultInstance().constructType(Integer.class),
                null,
                "type",
                true,
                null
        );

        Object result = deserializer.deserializeTypedFromObject(parser, context);

        Assert.assertEquals(42, result);
    }

    @Test
    public void testDeserializeTypedFromArray() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "[{\"type\":\"custom\",\"value\":42}]";

        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext context = mapper.getDeserializationContext();
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                TypeFactory.defaultInstance().constructType(Integer.class),
                null,
                "type",
                true,
                null
        );

        Object result = deserializer.deserializeTypedFromArray(parser, context);

        Assert.assertEquals(42, result);
    }

    @Test
    public void testDeserializeTypedFromScalar() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "\"custom\"";

        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext context = mapper.getDeserializationContext();
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                TypeFactory.defaultInstance().constructType(Integer.class),
                null,
                "type",
                true,
                null
        );

        Object result = deserializer.deserializeTypedFromScalar(parser, context);

        Assert.assertEquals("custom", result);
    }

    @Test
    public void testDeserializeTypedFromAny() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"type\":\"custom\",\"value\":42}";

        JsonParser parser = mapper.getFactory().createParser(json);
        DeserializationContext context = mapper.getDeserializationContext();
        AsWrapperTypeDeserializer deserializer = new AsWrapperTypeDeserializer(
                TypeFactory.defaultInstance().constructType(Integer.class),
                null,
                "type",
                true,
                null
        );

        Object result = deserializer.deserializeTypedFromAny(parser, context);

        Assert.assertEquals(42, result);
    }
}