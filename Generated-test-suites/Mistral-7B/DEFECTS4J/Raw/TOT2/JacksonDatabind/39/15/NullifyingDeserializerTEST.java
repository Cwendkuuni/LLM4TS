package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TypeDeserializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class NullifyingDeserializerTest {
    @Test
    public void testDeserialize() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("{}");
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserialize(parser, new TestContext());
        assertNull(result);
    }

    @Test
    public void testDeserializeWithType() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("[]");
        TypeDeserializer typeDeserializer = mapper.getTypeDeserializerForAnnotation(ArrayType.class);
        NullifyingDeserializer deserializer = NullifyingDeserializer.instance;
        Object result = deserializer.deserializeWithType(parser, new TestContext(), typeDeserializer);
        assertNull(result);
    }
}

class TestContext extends DeserializationContext {
    @Override
    public Object findValueInContext(String name) {
        return null;
    }
}