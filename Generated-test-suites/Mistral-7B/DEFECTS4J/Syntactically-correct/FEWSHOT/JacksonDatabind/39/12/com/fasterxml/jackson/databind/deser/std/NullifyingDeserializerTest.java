package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Test;

public class NullifyingDeserializerTest {

    @Test
    public void testDeserializeNullifyingDeserializerWithJsonParser() throws IOException {
        JsonParser jsonParser = createJsonParser("{\"name\":\"John\"}");
        Object result = NullifyingDeserializer.instance.deserialize(jsonParser, new TestContext());
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeNullifyingDeserializerWithJsonParserAndTypeDeserializer() throws IOException {
        JsonParser jsonParser = createJsonParser("{\"name\":[1,2,3]}");
        Object result = NullifyingDeserializer.instance.deserializeWithType(jsonParser, new TestContext(), new StdTypeDeserializer());
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeNullifyingDeserializerWithJsonParserAndCustomTypeDeserializer() throws IOException {
        JsonParser jsonParser = createJsonParser("{\"name\":{\"type\":\"MyCustomType\",\"value\":42}}");
        Object result = NullifyingDeserializer.instance.deserializeWithType(jsonParser, new TestContext(), new CustomTypeDeserializer());
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeNullifyingDeserializerWithObjectReader() throws IOException {
        ObjectReader objectReader = createObjectMapper().readerFor(Object.class);
        JsonNode jsonNode = createJsonParser("{\"name\":\"John\"}").readValueAsTree();
        Object result = NullifyingDeserializer.instance.deserialize(objectReader, new TestContext());
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeNullifyingDeserializerWithObjectReaderAndTypeDeserializer() throws IOException {
        ObjectReader objectReader = createObjectMapper().readerFor(Object.class);
        JsonNode jsonNode = createJsonParser("{\"name\":[1,2,3]}").readValueAsTree();
        Object result = NullifyingDeserializer.instance.deserializeWithType(objectReader, new TestContext(), new StdTypeDeserializer());
        Assert.assertNull(result);
    }

    @Test
    public void testDeserializeNullifyingDeserializerWithObjectReaderAndCustomTypeDeserializer() throws IOException {
        ObjectReader objectReader = createObjectMapper().readerFor(Object.class);
        JsonNode jsonNode = createJsonParser("{\"name\":{\"type\":\"MyCustomType\",\"value\":42}}").readValueAsTree();
        Object result = NullifyingDeserializer.instance.deserializeWithType(objectReader, new TestContext(), new CustomTypeDeserializer());
        Assert.assertNull(result);
    }

    private JsonParser createJsonParser(String jsonString) throws IOException {
        return createObjectMapper().getFactory().createParser(jsonString);
    }

    private ObjectMapper createObjectMapper() {
        return new ObjectMapper();
    }

    private static class TestContext extends DeserializationContext {
        @Override
        public ObjectReader _findRootValue(String name, Class<?> cls) {
            return null;
        }
    }

    private static class CustomTypeDeserializer extends StdTypeDeserializer<Object> {
        public CustomTypeDeserializer() { super(Object.class); }

        @Override
        public Object deserializeTypedFromAny(JsonParser p, DeserializationContext ctxt) throws IOException {
            return null;
        }
    }
}